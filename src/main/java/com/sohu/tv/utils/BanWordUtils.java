package com.sohu.tv.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class BanWordUtils {
	protected static final Log log = LogFactory.getLog(BanWordUtils.class);
	private static String banWordFilePath = "banWord.properties";
	
	/** 禁词-行号 （不写文件，只加载，则用HashMap，否则用LinkedHashMap）*/
//	private static transient Map<String,Integer> banWordHash = new HashMap<String,Integer>();
	private static transient Map<String,Integer> banWordHash = new LinkedHashMap<String,Integer>();
	private static BufferedWriter writer = null;
	private static BufferedReader reader = null;
	private static File file = null;
	static {
		load();
	}

	public static Map<String,Integer> index() {
		return banWordHash;
	}
	
	public static void load() {
		try {
			log.info("(Re)Load BanWord From "+banWordFilePath+" ...");
			long tmStart = System.currentTimeMillis();
			long tmEnd = tmStart;
			reader = new BufferedReader(new FileReader(BanWordUtils.class.getClassLoader().getResource(banWordFilePath).getFile()));
			int lineNum = 0;
			String lineText = null;
			while ((lineText=reader.readLine()) != null) {
				lineNum ++;
				if (StringUtils.isNotEmpty(lineText)) {
					banWordHash.put(lineText.trim(), lineNum);
				}
			}
			tmEnd = System.currentTimeMillis();
			log.info("(Re)Load BanWord From "+banWordFilePath+" ...OK items:"+lineNum+",cost:"+((tmEnd-tmStart)/1000L)+"s,"+(tmEnd-tmStart)+"ms");
			
		} catch (FileNotFoundException e) {
			log.warn("(Re)Load BanWord FileNotFoundException: "+e.getMessage(), e);
		} catch (IOException e) {
			log.warn("(Re)Load BanWord Read IOException: "+e.getMessage(), e);
		} catch(Exception e) {
			if(reader==null) {
				log.warn("BanWord File: "+banWordFilePath+" Not Found in classpath", e);
				return;
			}
		} 
		finally {
			try {
				if(reader!=null) {
					reader.close();
				}
				
			} catch (IOException e) {
				log.warn("(Re)Load BanWord Close Exception: "+e.getMessage(), e);
			}
		}
	}

	public static void reload() {
		banWordHash.clear();
		load();
	}
	
	protected static void save() {
		log.info("Save BanWord into "+banWordFilePath+" ...");
		file = new File(BanWordUtils.class.getClassLoader().getResource(banWordFilePath).getFile());
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				log.warn("Save BanWord File Create IOException: "+e.getMessage(), e);
			} catch(Exception e) {
				log.warn("Save BanWord File Create Exception: "+e.getMessage(), e);
			} 
		}
		try {
			writer = new BufferedWriter(new FileWriter(file));
			Collection<String> c = banWordHash.keySet();
			Iterator<String> banWords = c.iterator();
			String banItem = "";
			while (banWords.hasNext()) {
				banItem = banWords.next();
				if (StringUtils.isNotEmpty(banItem)) {
					writer.write(banItem);
					writer.newLine();
					writer.flush();
				}
			}
		} catch (IOException e) {
			log.warn("Save BanWord IOEception: "+e.getMessage(),  e);
		} finally {
			try {
				if(writer!=null) {
					writer.close();
				}
				
			} catch (IOException e) {
				log.warn("Save BanWord File Close Exception: "+e.getMessage(),  e);
			}
		}
	}
	

	public static String hit(String text) {
		if (banWordHash==null || banWordHash.isEmpty() || StringUtils.isEmpty(text)) {
			return null;
		}
		Collection<String> c = banWordHash.keySet();
		Iterator<String> banWords = c.iterator();
		String banItem = null;
		int lineNum = 0;
		while (banWords.hasNext()) {
			lineNum ++;
			banItem = banWords.next();
			if (StringUtils.isNotEmpty(banItem) && text.indexOf(banItem) != -1) {
				log.info("BanWordHit: ban="+banItem+",line="+lineNum);
				return banItem;
			}
		}
		return null;
	}

	public static void append(String banWord, boolean refresh) {
		if(StringUtils.isNotEmpty(banWord)) {
			banWordHash.put(banWord.trim(), banWordHash.size()+1);
		}
		if(refresh) {
			save();
			reload();
		}
	}

	public static void remove(String banWord, boolean refresh) {
		if(StringUtils.isNotEmpty(banWord)) {
			banWordHash.remove(banWord);
		}
		if(refresh) {
			save();
			reload();
		}
	}
	
	public static void refresh() {
		save();
		reload();
	}
	
	public static void main(String[] args) {
		String ban = BanWordUtils.hit("001工程");
		log.info("命中："+ban);
	}
}

