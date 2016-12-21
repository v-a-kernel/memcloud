/**
 * @filename			MD5.java
 * @function			Md5加密
 * @author				skyz <skyzhw@gmail.com>
 * @copyright 			ku6.com
 * @datetime			Jul 2, 2007
 * @lastmodify			Jul 2, 2007
 */
package com.sohu.tv.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Md5加密
 * 
 * @author skyz <skyzhw@gmail.com>
 * @version 1.0
 */

public class MD5 {

	private static final String hexDigits[] = { "0", "1", "2", "3", "4", "5",
			"6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };
	private static Log log=LogFactory.getLog(MD5.class);
	public MD5() {
	}

	public static String byteArrayToHexString(byte b[]) {
		StringBuffer resultSb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			resultSb.append(byteToHexString(b[i]));
		}
		return resultSb.toString();
	}

	private static String byteToHexString(byte b) {
		int n = b;
		if (n < 0) {
			n = 256 + n;
		}
		int d1 = n / 16;
		int d2 = n % 16;
		return (new StringBuilder()).append(hexDigits[d1])
				.append(hexDigits[d2]).toString();
	}

	/**
	 * md5加密
	 * 
	 * @param origin 要加密的字符串
	 */
	public static String MD5Encode(String origin) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			resultString = byteArrayToHexString(md.digest(resultString
					.getBytes()));
		} catch (NoSuchAlgorithmException e) {
			log.error("对字符串"+origin+"进行md5加密时发生错误",e);
		}
		return resultString;
	}
	
	public static String MD5Encode(String origin, String charset) {
		String resultString = null;
		try {
			resultString = new String(origin);
			MessageDigest md = MessageDigest.getInstance("MD5");
			try {
				resultString = byteArrayToHexString(md.digest(resultString.getBytes(charset)));
			}catch(UnsupportedEncodingException uee) {
				throw new IllegalArgumentException("UnsupportedEncodingException: "+charset);
			}
			
		} catch (NoSuchAlgorithmException e) {
			log.error("对字符串"+origin+"进行md5加密时发生错误",e);
		}
		return resultString;
	}
	
	public static String urlEncode(String plain, String charset) {
		try {
			return URLEncoder.encode(plain,charset);
		} catch (UnsupportedEncodingException e) {
			throw new IllegalStateException("charset type "+charset+" not supported",e);
		}
	}
	
	public static String urlEncode(String plain) {
		return urlEncode(plain, "UTF-8");
	}

	
	/**
	 * 计算MD5摘要
	 * */
	public static byte[] MD5Encode(byte[] plainBytes) throws NoSuchAlgorithmException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		return md.digest(plainBytes);
	}
}