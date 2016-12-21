package com.sohu.tv.utils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class MyUtils {

	protected final static Log log = LogFactory.getLog(MyUtils.class);
	
	public final static boolean isWhiteURL(final String url, final String[] whiteDomains) {
		if(whiteDomains==null || whiteDomains.length <= 0) {//表示没有允许的白名单
			return false;
		}
		String hostName = getHostName(url);
		if(hostName==null || hostName.trim().equals("")) {
			return false;
		}
		for (String rootDomain : whiteDomains) {
			if(isSubDomain(hostName,  rootDomain)) {
				return true;
			}
		}
		return false;
	}
	
	public static String numberFormat(double num) {
		return numberFormat((long)num);
	}
	
	public static String numberFormat(long num) {
		return NumberFormat.getInstance().format(num);
	}
	public static String percentFormat(double percent) {
		NumberFormat nf =  NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(2);
		return nf.format(percent);
	}
	
	static final long[] levelNumb4Bytes = new long[] {1024L*1024L*1024L,1024L*1024L,1024L,1L};
	static final String[] levelName4Bytes = new String[] {"G ","M ","K ","B "};
	public static String humanizedBytes(long bytes) {
		return humanized(bytes,levelNumb4Bytes,levelName4Bytes );
	}
	static final long[] levelNumb4Second = new long[] {60L*60L*24,60L*60L,60L,1L};
	static final String[] levelName4Seoncd = new String[] {"days ","hours ","min ","sec "};
	public static String humanizedSeconds(long seconds) {
		return humanized(seconds,levelNumb4Second,levelName4Seoncd );
	}
	
	public static String humanized(long rawData, long[] levelNumb, String[] levelName) {
		if (levelNumb == null || levelName == null || levelNumb.length!=levelName.length ) {
			throw new IllegalArgumentException("levelNumb size not equal to levelName size");
		}
		StringBuffer sb = new StringBuffer();
		
		long remainder = rawData;
		if (remainder <= 0) {
			sb.append(0).append(levelName[levelName.length-1]);
		}
		boolean highLevelOK = false;//已确定最高位单位
		int i = 0;
		while (remainder > 0 && i < levelNumb.length) {
			if (levelNumb[i] <= 0) {
				throw new IllegalArgumentException("levelNumb[i] should be greater than zero: i="+i);
			}
			long quotient = remainder / levelNumb[i];//商，整数部分
			if (!highLevelOK) {
				if (quotient > 0) {
					highLevelOK = true;
					sb.append(quotient).append(levelName[i]);
				} 
				
			} else {
				if (remainder > 0) {
					sb.append(quotient).append(levelName[i]);
				} else {
					break;
				}
			}
			
			remainder = remainder % levelNumb[i];//余数部分
			i ++;
		}
		return sb.toString();
	}
	
	/**
	 * 判断 targetDomain 是否是 rootDomain 的子域（包括相同）
	 * */
	public final static boolean isSubDomain(String targetDomain, String rootDomain) {
		if(targetDomain==null || targetDomain.equals("") || rootDomain==null || rootDomain.equals("")) {
			return false;
		}
		targetDomain = targetDomain.toLowerCase();
		rootDomain = rootDomain.toLowerCase();
		int foundIdx = targetDomain.indexOf(rootDomain);
		if(foundIdx!=-1 && (foundIdx==0 ||  targetDomain.charAt(foundIdx-1)=='.') && targetDomain.length()==foundIdx+rootDomain.length()) {
			return true;
		}
		return false;
	} 
	
	
	public final static String getHostName(final String url) {
		URL urlObj = null;
		try {
			urlObj = new URL(url);
		} catch (MalformedURLException e) {
			return null;
		}
		return (urlObj==null ? null:urlObj.getHost());
	}
	
	public final static String appendQS(final String urlAddr, Map<String,String> params ) {
		String url = urlAddr;
		if(params!=null && params.size() > 0) {
			Iterator<Entry<String, String>> entries = params.entrySet().iterator();
			while(entries.hasNext()) {
				Entry<String, String> e = entries.next();
				url = appendQS(url, e.getKey(), e.getValue());
			}
		}
		return url;
	}
	
	public final static String appendQS(String urlAddr,String param,String value) {
		return appendQueryString(urlAddr, param, value);
	}
	/**
	 * 给已有的 urlAddr增加输入参数
	 * */
	public final static String appendQueryString(String urlAddr,String param,String value) {
		String r = urlAddr;
		if(urlAddr.endsWith("/")) {
			r = urlAddr.substring(0, urlAddr.length()-"/".length()) + "?" + param+"="+urlencodeUTF8(value);
		} else if(urlAddr.indexOf("?")!=-1) {
			r = urlAddr + "&" + param + "=" + urlencodeUTF8(value);
		} else {
			r = urlAddr + "?" + param + "=" + urlencodeUTF8(value);
		}
		return r;
	}
	
	private static String urlencodeUTF8(String value) {
		if(value==null || value.equals("")) {
			return "null";
		}
		try {
			return URLEncoder.encode(value,"utf-8");
		} catch (UnsupportedEncodingException e) {
			return value;
		}
	}
	
	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	public final static String getNow() {
		return sdf.format(new Date());
	}
	
	@SuppressWarnings("unchecked")
	public static <T> List<T>  strToList(String str,String split,Class<T> clazz){
		List<T> list = null;
		if(str==null || "".equals(str)) return list;
		list = new ArrayList<T>();
		for(String tmp : str.split(split)){
				if(tmp==null || tmp.equals("")) {
					break;
				}
				tmp = tmp.trim();
				if(tmp==null || tmp.equals("")) {
					break;
				}
				if(clazz == String.class){
					list.add((T)tmp);
				}else if (clazz == Long.class) {
		    	    list.add((T)Long.valueOf(tmp));
	            } else if (clazz == Integer.class) {
	            	list.add((T)Integer.valueOf(tmp));	                
	            } else if (clazz == Double.class) {
	            	list.add((T)Double.valueOf(tmp));
	            } else if (clazz == Byte.class) {
	            	list.add((T)Byte.valueOf(tmp));
	            } else if (clazz == Short.class) {
	            	list.add((T)Short.valueOf(tmp));
	            } else {
	            	list.add((T)DefaultCodec.decode(tmp.getBytes()));	                
	            }
		}		
		return list;
	}

	
	
	private static SimpleDateFormat sdfTM = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String tm2str(long tm) {
		return sdfTM.format(new Date(tm));
	}
	
	public static long str2tm(String str)  {
		try {
			return sdfTM.parse(str).getTime();
		}catch(ParseException pe) {
			throw new IllegalArgumentException(pe);
		}
	}
}