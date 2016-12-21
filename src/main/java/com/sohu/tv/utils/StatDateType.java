package com.sohu.tv.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * REFER: http://stackoverflow.com/questions/3083781/start-and-end-date-of-a-current-month 
 * */
public class StatDateType implements IStatDateType {

	/** 统计日的日期性质 */
	public static int getTypeValue(Date statDate) {
		Calendar calendar = new GregorianCalendar();
		int v = STAT_DATE_TYPE_EOD;//默认普通日
		
		if(isEOW(calendar)) {
			v += STAT_DATE_TYPE_EOW;
		}
		if(isEOM(calendar)) {
			v += STAT_DATE_TYPE_EOM;
			
			if(isEOQ(calendar)) {//季末以月末为前提
				v += STAT_DATE_TYPE_EOQ;
				
				if(isEOY(calendar)) {//年末以季末为前提
					v += STAT_DATE_TYPE_EOY;
				}
			}
		}
		
		return v;
		
	}
	
	/** 周末，严格说是周六，不是周日 */
	public static boolean isEOW(Date statDate) {
		return isEOW(date2calendar(statDate));
	}
	
	public static boolean isEOW(Calendar calendar) {
		return calendar.getActualMaximum(Calendar.DAY_OF_WEEK) == calendar.get(Calendar.DAY_OF_WEEK);
	}
	
	/** 月末 */
	public static boolean isEOM(Date statDate) {
		return isEOM(date2calendar(statDate));
	}
	
	public static boolean isEOM(Calendar calendar) {
		//如果当月的最后一天 等于当前天，则表示今天是月末
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
	}
	
	
	/** 季末，必定是月末  
	 *  季度定义 ：3,6,9,12为季末   REFER:  http://baike.baidu.com/view/750983.htm
	 * */
	public static boolean isEOQ(Date statDate) {
		return isEOQ(date2calendar(statDate));
	}
	
	public static boolean isEOQ(Calendar calendar) {
		boolean isEOM = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
		return isEOM && (calendar.get(Calendar.MONTH)%3==0);
	}
	
	/** 年末，必定是月末 */
	public static boolean isEOY(Date statDate) {
		return isEOY(date2calendar(statDate));
		
	}
	
	public static boolean isEOY(Calendar calendar) {
		boolean isEOM = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
		return isEOM && (calendar.get(Calendar.MONTH)==12);
	}
	
	private static Calendar date2calendar(Date date) {
		Calendar calendar = Calendar.getInstance();
	    calendar.setTime(date);
	    return calendar;
	}
	
	public static Date todayBegin() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    return calendar.getTime();
	}
	
	public static Date yesterdayBegin() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
	    return calendar.getTime();
	}
	

	public static Date todayEnd() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 999);
	    return calendar.getTime();
	}
	
	public static Date yesterdayEnd() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
	    calendar.set(Calendar.MINUTE, 59);
	    calendar.set(Calendar.SECOND, 59);
	    calendar.set(Calendar.MILLISECOND, 999);
	    return calendar.getTime();
	}
//  REFER:  http://sharajava.iteye.com/blog/81551   SimpleDateFormat 是非线程安全的
//	private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//	private static final SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	public static int currentYYYYMMdd() {
		return Integer.parseInt(format(new Date(), "yyyyMMdd"));
	}
	
	public static int dateYYYYMMdd(Date date) {
		return Integer.parseInt(format(date, "yyyyMMdd"));
	}
	
	public static Date parse(String dateYYYY_MM_DD) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return sdf.parse(dateYYYY_MM_DD);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}
	
	public static Date parse(String dateString, String pattern) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			return sdf.parse(dateString);
		} catch (ParseException e) {
			throw new IllegalArgumentException(e.getMessage(), e);
		}
	}
	
	public static Date parseDate(String dateString, String pattern) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		return sdf.parse(dateString);
	}
	
	public static String format(Date date) {
		SimpleDateFormat formater = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return formater.format(date);
	}
	
	public static String format(long timestamp) {
		return format(timestamp, "yyyy/MM/dd HH:mm:ss");
	}
	
	public static String format(long timestamp, String pattern) {
		return format(new Date(timestamp), pattern);
	}
	
	public static String format(Date date, String pattern) {
		if(date == null)
			return "";
		return new SimpleDateFormat(pattern).format(date);
	}
	
	/** 把日期的HH:mm:ss SSS设置为0，只保留yyyyMMdd部分 */
	public static Date trimHHmmssSSS(Date date) {
		Calendar calendar = date2calendar(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
	    calendar.set(Calendar.MINUTE, 0);
	    calendar.set(Calendar.SECOND, 0);
	    calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/** 返回多少天前 */
	public static Date someDaysAgo(Date date, int days) {
		Calendar calendar = date2calendar(date);
		calendar.add(Calendar.DAY_OF_MONTH, -days);
		return calendar.getTime();
	}
	
	/** 多少天后 */
	public static Date someDaysLater(Date date, int days) {
		Calendar calendar = date2calendar(date);
		calendar.add(Calendar.DAY_OF_MONTH, days);
		return calendar.getTime();
	}
	
	/** 统计日期居然早于初始化日期 */
	public static boolean beforeThan(Date statDate, Date initDate) {
		if(initDate == null) {
			return false;
		}
		return statDate.getTime() < initDate.getTime();
	}
	
	/** 获取当前时间(2011-10-25)*/
	public static String getCurrentDate(){
		Calendar calendar = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return format(calendar.getTime(), sdf.toPattern());
	}
	
	public static int daysAgo(byte timeSpanType) {
		int daysAgo = 0;
		switch (timeSpanType) {
		case TIMPSPAN_TYPE_WEEK:
			daysAgo = 7;
			break;
		case TIMPSPAN_TYPE_MONTH:
			daysAgo = 30;
			break;
		case TIMPSPAN_TYPE_QUARTER:
			daysAgo = 90;
			break;
		case TIMPSPAN_TYPE_HALFYEAR:
			daysAgo = 182;
			break;
		case TIMPSPAN_TYPE_YEAR:
			daysAgo = 365;
			break;
		case TIMPSPAN_TYPE_ALL:
		default:
			daysAgo = 0;
			break;
		}
		return daysAgo;
	}
	
	public static void main(String[] args) {
		System.out.println(isEOM(parse("20111023")));
		System.out.println(isEOW(parse("20111023")));
		System.out.println(isEOW(parse("20111022")));
		
		System.out.println(format(someDaysAgo(parse("20111001"), 7)));
	}
}
