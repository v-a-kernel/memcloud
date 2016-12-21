package com.sohu.tv.utils;

public interface IStatDateType {

	/**
	 * 如果某一条既是周末，又是月末，同时还是季末，那么该数值等于：111，表达组合方式只需要累加就可以。
	 * */
	public final static int STAT_DATE_TYPE_EOD = 0;//普通
	public final static int STAT_DATE_TYPE_EOW = 001;//周末 End Of Week
	public final static int STAT_DATE_TYPE_EOM = 010;//月末 End Of Month
	public final static int STAT_DATE_TYPE_EOQ = 100;//季末 End Of Quarter （必定是月末）
	public final static int STAT_DATE_TYPE_EOY = 1000;//年末 End Of Year （必定是月末）
	
	
	public static final byte TIMPSPAN_TYPE_ALL = 0;
	public static final byte TIMPSPAN_TYPE_WEEK = 1;
	public static final byte TIMPSPAN_TYPE_MONTH = 2;
	public static final byte TIMPSPAN_TYPE_QUARTER = 3;
	public static final byte TIMPSPAN_TYPE_HALFYEAR = 4;
	public static final byte TIMPSPAN_TYPE_YEAR = 5;
}

