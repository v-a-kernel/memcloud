package io.memcloud.memdns.util;

import java.io.IOException;
import java.io.Writer;

import io.downgoon.jresty.rest.view.JSHandler;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


import com.sohu.tv.utils.DynamicProperties;

/**
 * JSONP接口进行域名限制控制，对于第三方的域名需要在tv.sohu.com配置白名单才可以访问
 * */
public class MyRestrictJSHandler extends JSHandler {
	
	protected static final Log log = LogFactory.getLog(MyRestrictJSHandler.class);

	protected String defaultCallbackMethod = "com.sohu.tv.callback";
	protected String defaultCallbackDomain = "tv.sohu.com";
	
	@Override
	public String fromObject(Object obj, String resultCode, Writer stream)
			throws IOException {
		
		String[] truple = parseCallbackRestrict((String)super.getAttachment());
		//过滤允许的域名，支持个性化域名
		String dmAllowed = genAllowedDomain(truple[1]); 
		if(dmAllowed==null || "".equals(dmAllowed)) {
			log.warn("jsonp domain rejected: "+truple[1]);
			//简单JSONP，不含域限制的
			//不带闭包，容易造成污染：    APP.Callback({"attachment":{"indexValue":"96\u002e76","url":"http\u003a//index\u002etv\u002esohu\u002ecom/index/switch\u002daid/1006730\u002ehtml"},"debug":"","message":"","status":200}); 
			//带闭包，不会造成变量污染：(function(args){var par={"message":"\u7cfb\u7edf\u5f02\u5e38","status":500};your-method(par,args);})()
			truple[1] = defaultCallbackDomain;//对于不允许的域名访问JSONP时，依然强制到默认域名下；
			
		} else {
			truple[1] = dmAllowed;
			//复杂JSONP，包含域限制的，域限制只对防止操作COOKIE有点安全帮助
			//example: document.domain="weiguan.com"; (function(args){var par={"message":"\u7cfb\u7edf\u5f02\u5e38","status":500};your-method(par,args);})()
//			stream.write("document.domain=\"");
//			stream.write(truple[1]);//domain
//			stream.write("\"; ");
		}
		
		stream.write("(function(args){var par=");
		super.getDelegate().fromObject(obj, resultCode, stream);//json
		stream.write(";");
		stream.write(truple[0]);//callback method
		stream.write("(par,args);})()");
		return null;
	}
	
	public static final String LEVEL_ALLOW_SELF = "true";
	public static final String LEVEL_ALLOW_SUB = "sub";
	public static final String LEVEL_DENY = "false";
	
	/**
	 * 可以重新覆盖这个实现
	 * */
	protected String getLevel(String domain) {
		return DynamicProperties.getDefaultInstance().getProperty("jsonp.acl."+domain);
	}
	
	/**
	 * 生成允许的域名
	 * @param	domain		原始输入域名；
	 * @return	返回NULL （等效于返回默认的域名），表示不支持；返回domain的一个字串，表示不支持二级域名或个性化域名，但是根域是支持的；
	 * 			返回domain本身，表示整个域（包括二级子域或个性化域）；
	 * */
	protected String genAllowedDomain(String domain) {
		String level = getLevel(domain);
		if(LEVEL_ALLOW_SELF.equalsIgnoreCase(level) || LEVEL_ALLOW_SUB.equalsIgnoreCase(level)) {//完全支持该域名
			return domain;
		}
		if(LEVEL_DENY.equalsIgnoreCase(level)) {//完全拒绝该域名
			return null;
		}
		//如果去掉个性化域名部分，看主域名是否被允许
		int subidx = domain.indexOf(".");
		if(! (subidx!=-1 && subidx < domain.length()-1)) {
			return null;
		}
		String parent = domain.substring(subidx+1);
		level = getLevel(parent);//去掉个性域名，剩下父域名
		if(LEVEL_ALLOW_SUB.equalsIgnoreCase(level)) {//如果父域名支持个性化
			return domain;
		} 
		if(LEVEL_ALLOW_SELF.equalsIgnoreCase(level)) {//如果父域只支持本省，并不支持个性化
			return parent;
		}
		return null;//父域名不被支持或者被加入黑名单
	}
	
	/*
	 * example: http://index.tv.sohu.com/index/ping.jsonp?callback=APP.Callback@tv.sohu.com
	 * document.domain="tv.sohu.com"; (function(args){var par={"attachment":null,"debug":"20110227181136173618\u003a\u7cfb\u7edf\u8fd0\u884c\u6b63\u5e38","message":"\u6210\u529f","status":200};APP.Callback(par,args);})()
	 * */
	
	private String[] parseCallbackRestrict(String callbackRestrict) {
		String[] truple = new String[] {defaultCallbackMethod, defaultCallbackDomain};
		if(callbackRestrict!=null && !"".equals(callbackRestrict)) {
			int spidx = callbackRestrict.lastIndexOf("@");
			if(spidx==-1 || spidx==callbackRestrict.length()-1) {//只有前缀部分，没有后缀部分，重写前缀
				truple[0] = callbackRestrict;
			} else {//前缀后缀都有，则两者都重写
				truple[0] = callbackRestrict.substring(0, spidx);
				truple[1] = callbackRestrict.substring(spidx+1);
			}
		} 
		return truple;
	}
	
	/*
	 * cr=null
	 * com.weiguan.passport.callback@weiguan.com
	 * cr=
	 * com.weiguan.passport.callback@weiguan.com
	 * cr=APP.LOGIN
	 * APP.LOGIN@weiguan.com
	 * cr=APP.LOGIN@
	 * APP.LOGIN@@weiguan.com
	 * cr=APP.LOGIN@ku6.com
	 * APP.LOGIN@ku6.com
	 * 
	 * */
	
	
}
