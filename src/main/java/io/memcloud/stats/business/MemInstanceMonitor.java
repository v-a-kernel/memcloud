/**
 * 
 */
package io.memcloud.stats.business;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import io.memcloud.memdns.dao.IAppDescDao;
import io.memcloud.memdns.dao.IAppMemGroupDao;
import io.memcloud.memdns.dao.entry.AppDesc;
import io.memcloud.memdns.dao.entry.AppMemGroup;
import io.memcloud.memdns.dao.entry.MemFault;
import io.memcloud.stats.dao.IMemFaultDao;
import io.memcloud.stats.util.EmailHandler;
import io.memcloud.stats.util.HttpClientHandler;
import com.sohu.tv.utils.DynamicProperties;

/**
 * @author ganghuawang
 *
 */
public class MemInstanceMonitor implements IMemInstanceMonitor {

	
	private static final Logger log = Logger.getLogger(MemInstanceMonitor.class);
	private static int DEFAULT_FAULT_TIMES = 3;	//可配置(fault.times)
	private static int DEFAULT_EXPIRES_TIME = 600;  //可配置(alarm.expires)
	private static boolean ischeck = false ;
	/** 用于存放故障的次数和最近的一次报警时间*/
	private static Map<String, Long> faultResult = new ConcurrentHashMap<String, Long>();
	/** 模板*/
	private String sns_url_template;
	private String fault_message_template;
	private String ok_message_template;
	
	@Resource(name = "memFaultDao")
	private IMemFaultDao memFaultDao;
	
	@Resource(name = "appDescDao")
	private IAppDescDao appDescDao;
	
	@Resource(name = "appMemGroupDao")
	private IAppMemGroupDao appMemGroupDao;
	
	
	public MemInstanceMonitor(){
		if(StringUtils.isNotEmpty(DynamicProperties.getDefaultInstance().getProperty("fault.times")))
			DEFAULT_FAULT_TIMES = Integer.valueOf(DynamicProperties.getDefaultInstance().getProperty("fault.times"));
		if(StringUtils.isNotEmpty(DynamicProperties.getDefaultInstance().getProperty("alarm.expires")))
			DEFAULT_EXPIRES_TIME = Integer.valueOf(DynamicProperties.getDefaultInstance().getProperty("alarm.expires"));
		sns_url_template = DynamicProperties.getDefaultInstance().getProperty("sns_url");
		fault_message_template = DynamicProperties.getDefaultInstance().getProperty("message_template");
		ok_message_template = DynamicProperties.getDefaultInstance().getProperty("ok_template");
	}

	/** 
	 * 确认故障已受理 
	 * 1、更改故障的状态
	 * 2、发送消息提醒
	 */
	@Override
	public void receiveFaultMessage(Long id) {
		MemFault memFault = memFaultDao.get(id);
		if(memFault != null && memFault.getStatus() == 0){
			memFault.setStatus(1);
			// 更改故障的状态
			memFaultDao.update(memFault);
			// 短信、邮件通知
			sendOKMessage(memFault);
		}
		
		//清空记录故障的次数
		String key = getKey(memFault.getIp(), memFault.getPort());
		faultResult.remove(key);
	}

	/** 
	 * 故障自动恢复了
	 */
	@Override
	public void recoverFault(String host, Integer port) {
		MemFault memFault = memFaultDao.getByHostAndPort(host, port);
		if(memFault != null && memFault.getStatus() == 0 )
			receiveFaultMessage(memFault.getId());
	}
	
	/**
	 * 1、确定是否出现故障，检验3次
	 * 2、如果是，记录故障信息
	 * 3、发出故障通知短信
	 */
	@Override
	public void sendFaultMessage(String host, Integer port) {
		initHistoryFaultTime();
		if(StringUtils.isEmpty(host) || port == null){
			log.error("ip or port is null: ip=" + host + " port=" + port);
			return ;
		}
		String key = getKey(host, port);

		// 检验超过次数才确认为故障
		Long times = faultResult.get(key) == null ? 0 : faultResult.get(key) ;
		times++ ;
		log.info("fault times :" + times + " host=" + host + " port="+port);
		if( times  < DEFAULT_FAULT_TIMES ){
			//故障次数累加
			faultResult.put(key, times); 
		}else{
			// 清空故障的次数
			faultResult.remove(key);
			
			// 报警的时长间隔
			if(faultResult.get(key+"_time") == null){
				faultResult.put(key + "_time", System.currentTimeMillis());
			}else {
				Long time = faultResult.get(key+"_time");
				if((System.currentTimeMillis()-time)/1000 > DEFAULT_EXPIRES_TIME){  //默认10分钟
					faultResult.remove(key+"_time");
				}else{
					log.info("not send message, expires time:" + DEFAULT_EXPIRES_TIME);
					return ;
				}
			}
			
			/** 故障记录入库 */ 
			MemFault memFault = memFaultDao.getByHostAndPort(host, port);
			if(memFault == null || memFault.getStatus() == 1 ){ //从来没有记录或者记录已经处理了，是新的故障
				memFault = new MemFault();
				memFault.setIp(host);
				memFault.setPort(port);
				memFault.setStatus(0);
				memFault.setCreateTime(System.currentTimeMillis());
				// 根据host:port查询AppMemGroup信息
				AppMemGroup appMemGroup = appMemGroupDao.getByHostAndPort(host, port);
				memFault.setAppId(appMemGroup.getAppId());
				memFault.setGroupId(appMemGroup.getId());
				// 入库
				memFault.setId(memFaultDao.save(memFault));
			}
			
			/** 发送信息 */
			String textMessage = fault_message_template.replace("$ip", memFault.getIp())
				.replace("$port", memFault.getPort()+"").replace("$id", memFault.getId()+""); 
			
			AppDesc appDesc = appDescDao.getByAppId(memFault.getAppId());
			if (appDesc != null) {
				// 发送邮件通知
				log.info("send email notify : email = " + appDesc.getNotifyEmails() + " message=" + textMessage);
				EmailHandler.getInstance().sendEmail(appDesc.getNotifyEmails(), textMessage);
				// 发送短信通知
				if (StringUtils.isNotEmpty(sns_url_template) && StringUtils.isNotEmpty(appDesc.getNotifyMobiles())) {
					//有多个接受人,用","分隔了 
					for (String mobile : appDesc.getNotifyMobiles().split(",")) {
						log.info("send mobile notify : mobile number = " + mobile + " message=" + textMessage);
						String sns_url = sns_url_template.replace("$mobile", mobile).replace("$message",textMessage);
						HttpClientHandler.notifyGetPage(sns_url);
					}
				}
			}
		}
		
	}
	
	/**
	 * 发送故障恢复短信
	 * @param memFault
	 */
	private void sendOKMessage(MemFault memFault){
		/** 发送信息 */
		String textMessage = ok_message_template.replace("$ip", memFault.getIp()).replace("$port", memFault.getPort()+""); 
		
		AppDesc appDesc = appDescDao.getByAppId(memFault.getAppId());
		if (appDesc != null) {
			// 发送邮件通知
			log.info("send email notify : email = " + appDesc.getNotifyEmails() + " message=" + textMessage);
			EmailHandler.getInstance().sendEmail(appDesc.getNotifyEmails(), textMessage);
			// 发送短信通知
			if (StringUtils.isNotEmpty(sns_url_template)
					&& StringUtils.isNotEmpty(appDesc.getNotifyMobiles())) {
				//有多个接受人,用","分隔了 
				for (String mobile : appDesc.getNotifyMobiles().split(",")) {
					log.info("send mobile notify : mobile number = " + mobile + " message=" + textMessage);
					String sns_url = sns_url_template.replace("$mobile", mobile).replace("$message",
									textMessage);
					HttpClientHandler.notifyGetPage(sns_url);
				}
			}
		}
	}
	
	/**
	 * 查询暂时不发送通知的故障
	 */
	private void initHistoryFaultTime(){
		if(!ischeck){
			List<MemFault> list = memFaultDao.getByGtTime(System.currentTimeMillis() - DEFAULT_EXPIRES_TIME * 1000);
			for(MemFault memFault : list){
				String key = getKey(memFault.getIp(), memFault.getPort());
				faultResult.put(key + "_time", memFault.getCreateTime());
			}
			ischeck = true ;
		}
	}

	private String getKey(String host, Integer port) {
		return host + ":" + port ;
	}

}
