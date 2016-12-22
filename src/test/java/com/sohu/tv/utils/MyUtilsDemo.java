package com.sohu.tv.utils;

public class MyUtilsDemo {

	public static void main(String[] args) {
		long[] levelNumbBytes = new long[] {1024L*1024L*1024L,1024L*1024L,1024L,1L};
		String[] levelNameBytes = new String[] {"G","M","K","B"};
		
		System.out.println(MyUtils.humanized(0,levelNumbBytes,levelNameBytes));
		System.out.println(MyUtils.humanized(1023,levelNumbBytes,levelNameBytes));;
		System.out.println(MyUtils.humanized(1024,levelNumbBytes,levelNameBytes));;
		System.out.println(MyUtils.humanized(1024*1024,levelNumbBytes,levelNameBytes));;
		System.out.println(MyUtils.humanized(1024*1024*1024,levelNumbBytes,levelNameBytes));;
		System.out.println(MyUtils.humanized(1024*1024*1024+5,levelNumbBytes,levelNameBytes));;
		System.out.println(MyUtils.humanized(1024*1024*1024+1024*1024*5+1024*6+56,levelNumbBytes,levelNameBytes));;
		
		long[] levelNumb4Time = new long[] {60L*60L*24,60L*60L,60L,1L};
		String[] levelName4Time = new String[] {"天","小时","分","秒"};
		System.out.println(MyUtils.humanized(0,levelNumb4Time,levelName4Time));
		System.out.println(MyUtils.humanized(35,levelNumb4Time,levelName4Time));
		System.out.println(MyUtils.humanized(60,levelNumb4Time,levelName4Time));
		System.out.println(MyUtils.humanized(60*60,levelNumb4Time,levelName4Time));
		System.out.println(MyUtils.humanized(60*60*24,levelNumb4Time,levelName4Time));
		System.out.println(MyUtils.humanized(60*60*60,levelNumb4Time,levelName4Time));
		
	}

}
