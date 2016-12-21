package com.sohu.tv.memcloud.stats.dao.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.sohu.tv.memcloud.stats.MemStatSummary;
import com.sohu.tv.memcloud.stats.dao.IInstanceStatManager;
import com.sohu.tv.memcloud.stats.dao.IMemStatDao;

public class MemStatDaoImplDemo {

	public static void main(String[] args) {
		ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		IInstanceStatManager instatnceStatManager = (InstanceStatManagerImpl)factory.getBean("instatnceStatManager");
		IMemStatDao memStatDao = new MemStatDaoImpl(instatnceStatManager);
		
//		LinkedHashMap<String, Long> f = memStatDao.trendGet("10.10.79.214", 11211, StatDateType.todayBegin());
//		System.out.println(f);
		
		MemStatSummary  s = memStatDao.summary("10.10.79.214", 11211);
		System.out.println(s);
		
	}

}
