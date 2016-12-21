package com.sohu.tv.utils;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class DumpUtil {

	public static void main(String[] args) {
		Set<String> list = new LinkedHashSet<String>();
		list.add("成龙");
		list.add("李连杰");
		dump2file(list, "E:/data/index/star_list_20110225.txt");
		System.out.println("结束");
	}

	public static void dump2file(@SuppressWarnings("rawtypes") Set list, String fileName)  {
		File file = new File(fileName);
//		if(! file.exists()) {
//		}
		PrintStream out = null;
		try {
			out = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)));
		} catch (FileNotFoundException e) {
			new IllegalStateException(e);
		}
		for (Object item : list) {
			out.println(item);
		}
		out.flush();
		out.close();
	}
	
	public static void dump2file(@SuppressWarnings("rawtypes") List list, String fileName)  {
		File file = new File(fileName);
//		if(! file.exists()) {
//		}
		PrintStream out = null;
		try {
			out = new PrintStream(new BufferedOutputStream(new FileOutputStream(file)));
		} catch (FileNotFoundException e) {
			new IllegalStateException(e);
		}
		for (Object item : list) {
			out.println(item);
		}
		out.flush();
		out.close();
	}
}
