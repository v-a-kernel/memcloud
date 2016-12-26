package io.memcloud.stats.business;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.apache.log4j.Logger;

import io.memcloud.memdns.dao.IMemInstanceDao;
import io.memcloud.memdns.dao.entry.MemInstance;
import net.rubyeye.xmemcached.MemcachedClient;
import net.rubyeye.xmemcached.MemcachedClientBuilder;
import net.rubyeye.xmemcached.XMemcachedClientBuilder;
import net.rubyeye.xmemcached.utils.AddrUtil;

/**
 * @author ganghuawang
 * 初始化memcached客户端，并监听状态
 */
public class MemInstanceConnectionPool implements IMemInstanceConnectionPool {

	private static Logger log = Logger.getLogger(MemInstanceConnectionPool.class);
	
	
	private static Map<String, MemcachedClient> clientPool = new ConcurrentHashMap<String,MemcachedClient>();

	@Resource( name = "memInstanceDao")
	private IMemInstanceDao memInstanceDao;
	
//	@Resource( name = "memInstanceFaultManager")
//	private IMemInstanceFaultManager memInstanceFaultManager;
	
	@Override
	public void init(){
		// 初始化xmemcached客户端
		List<MemInstance> memInstanceList = memInstanceDao.getAll();
		log.info("all memcached instance client is creating... ");
		for (MemInstance instance : memInstanceList) {
			createClient(instance.getHostIp(), instance.getPort());
		}
	}
	
	@Override
	public Map<String, MemcachedClient> getConnectionPool() {
		return clientPool;
	}
	
	@Override
	public void addClient(String host, int port) {
		createClient(host, port);
		log.info("xmemcached client list size :" + clientPool.size());
	}

	@Override
	public void removeClient(String host, int port) {
		MemcachedClient client = clientPool.get(host+":"+port);
		try {
			if(client != null) {
				client.shutdown();
			}
				
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			clientPool.remove(host+":"+port);
		}
		log.info("xmemcached client list size :" + clientPool.size());
	}
	
	/**
	 * 创建xmemcached客户端连接
	 * @param host
	 * @param port
	 */
	private void createClient(String host, int port){
		// 如果client已经存在，不处理
		if(clientPool.get(host+":"+port) != null) {
			return ;
		}

		MemcachedClientBuilder builder = new XMemcachedClientBuilder(
				AddrUtil.getAddresses(host+ ":" + port));
		try {

			// client参数优化
			builder.setConnectionPoolSize(1);
			builder.getConfiguration().setSessionIdleTimeout(20000); // 设为如果连接超时没有任何IO操作就发起心跳检测，默认5秒
			builder.getConfiguration().setStatisticsServer(false);	// 关闭客户端统计

			// 创建client
			MemcachedClient client = builder.build();
			client.addStateListener(new MemInstanceWatchdog());
			
			// 添加到client列表中
			clientPool.put(host + ":" + port, client);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
//	/**
//	 *  memcached客户端状态监听实现类
//	 */
//	class MemInstanceWatchdog implements MemcachedClientStateListener {
//
//		@Override
//		public void onConnected(MemcachedClient arg0, InetSocketAddress arg1) {
//			// 解除故障
//			memInstanceFaultManager.recoverFault(arg1.getHostName(), arg1.getPort());
//		}
//
//		@Override
//		public void onDisconnected(MemcachedClient arg0, InetSocketAddress arg1) {
//			
//		}
//
//		@Override
//		public void onException(MemcachedClient arg0, Throwable arg1) {
//			// 解析host和port
//			String failMess = arg1.getLocalizedMessage(); //Connect to 10.11.132.83:11213 fail,Connection refused: no further information
//			String host = failMess.split(":")[0].split(" ")[2];
//			String port = failMess.split(":")[1].split(" ")[0];
//			// 故障报警
//			memInstanceFaultManager.sendFaultMessage(host, Integer.valueOf(port));
//		}
//
//		@Override
//		public void onShutDown(MemcachedClient arg0) {
//			
//		}
//
//		@Override
//		public void onStarted(MemcachedClient arg0) {
//			
//		}
//		
//	}

	
}
