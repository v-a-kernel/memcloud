package io.memcloud.utils;

import java.text.NumberFormat;

public class DateFormator {

	public static String numberFormat(double num) {
		return numberFormat((long) num);
	}

	public static String numberFormat(long num) {
		return NumberFormat.getInstance().format(num);
	}

	public static String percentFormat(double percent) {
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMaximumFractionDigits(2);
		return nf.format(percent);
	}

	static final long[] levelNumb4Bytes = new long[] { 1024L * 1024L * 1024L, 1024L * 1024L, 1024L, 1L };
	static final String[] levelName4Bytes = new String[] { "G ", "M ", "K ", "B " };

	public static String humanizedBytes(long bytes) {
		return humanized(bytes, levelNumb4Bytes, levelName4Bytes);
	}

	static final long[] levelNumb4Second = new long[] { 60L * 60L * 24, 60L * 60L, 60L, 1L };
	static final String[] levelName4Seoncd = new String[] { "days ", "hours ", "min ", "sec " };

	public static String humanizedSeconds(long seconds) {
		return humanized(seconds, levelNumb4Second, levelName4Seoncd);
	}

	public static String humanized(long rawData, long[] levelNumb, String[] levelName) {
		if (levelNumb == null || levelName == null || levelNumb.length != levelName.length) {
			throw new IllegalArgumentException("levelNumb size not equal to levelName size");
		}
		StringBuffer sb = new StringBuffer();

		long remainder = rawData;
		if (remainder <= 0) {
			sb.append(0).append(levelName[levelName.length - 1]);
		}
		boolean highLevelOK = false;// 已确定最高位单位
		int i = 0;
		while (remainder > 0 && i < levelNumb.length) {
			if (levelNumb[i] <= 0) {
				throw new IllegalArgumentException("levelNumb[i] should be greater than zero: i=" + i);
			}
			long quotient = remainder / levelNumb[i];// 商，整数部分
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

			remainder = remainder % levelNumb[i];// 余数部分
			i++;
		}
		return sb.toString();
	}

}
