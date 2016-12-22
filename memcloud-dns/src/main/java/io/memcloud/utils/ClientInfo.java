package io.memcloud.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 获取用户端信息类
 * 
 * @author zhanghuawei
 * 
 */
public class ClientInfo {
	/**
	 * 获取用户cookie信息
	 * 
	 * @return String
	 */
	public static String getCookie(HttpServletRequest request, String name) {
		Cookie[] cookies=request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(name)) {
					try {
						return URLDecoder.decode(cookies[i].getValue(),"UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}
	/**
	 * 获取用户cookie信息
	 * @param request
	 * @param name
	 */
	public static Cookie getCookieObject(HttpServletRequest request, String name) {
		Cookie[] cookies=request.getCookies();
		if (cookies != null && cookies.length > 0) {
			for (int i = 0; i < cookies.length; i++) {
				if (cookies[i].getName().equals(name)) {
					return cookies[i];
				}
			}
		}
		return null;
	}	
	/**
	 * 设置cookie信息
	 */
	public static void setCookie(HttpServletResponse response,String name,String value,String domain,String path,int expire) {
		Cookie cookie = new Cookie(name,value);
		if(domain!=null && !domain.equals("")){
			cookie.setDomain(domain);
		}
		if(path!=null && !path.equals("")){
			cookie.setPath(path);
		}
		if(expire>0){
			cookie.setMaxAge(expire);
		}
		response.addCookie(cookie);
	}
	
	public static void removeCookie(HttpServletResponse response,String name,String value,String domain,String path) {
		if(value == null) {
			value = "";
		}
		Cookie cookie = new Cookie(name,value);
		if(domain!=null && !domain.equals("")){
			cookie.setDomain(domain);
		}
		if(path!=null && !path.equals("")){
			cookie.setPath(path);
		}
		cookie.setMaxAge(0);//0表示删除COOKIE
		response.addCookie(cookie);
	}
	/**
	 * 设置cookie信息
	 */
	public static void deleteCookie(HttpServletResponse response,Cookie cookie,String domain,String path) {
		if (cookie != null) {
			cookie.setMaxAge(0);
			if(domain!=null && !domain.equals("")){
				cookie.setDomain(domain);
			}
			cookie.setDomain(domain);
			path = (path == null ? "/" : path);
			cookie.setPath(path);
			response.addCookie(cookie);
		}
	}	
	/**
	 * 获取客户端ip地址
	 * 
	 * @param request
	 * @return ip地址
	 */
	public static String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("Cdn-Src-Ip");//2011-05-06 有的CDN厂商取源时通过Cdn-Src-Ip传递源IP
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("x-forwarded-for");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		if(ip!=null){
			ip.trim().replace(";",",").replace(" ",",");
			String[] ipList=ip.split(",");
			if(ipList!=null && ipList.length>0){
				ip=ipList[0];
			}
		}
		return ip;
	}
}
