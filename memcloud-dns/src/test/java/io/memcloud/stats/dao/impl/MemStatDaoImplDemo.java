package io.memcloud.stats.dao.impl;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import io.memcloud.stats.MemStatSummary;
import io.memcloud.stats.dao.IInstanceStatManager;
import io.memcloud.stats.dao.IMemStatDao;

public class MemStatDaoImplDemo {

	public static void main(String[] args) {
		ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		IInstanceStatManager instatnceStatManager = (InstanceStatManagerImpl)factory.getBean("instatnceStatManager");
		IMemStatDao memStatDao = new MemStatDaoImpl(instatnceStatManager);
		
//		LinkedHashMap<String, Long> f = memStatDao.trendGet("10.10.79.214", 11211, DateUtil.todayBegin());
//		System.out.println(f);
		
		MemStatSummary  s = memStatDao.summary("10.10.79.214", 11211);
		System.out.println(s);
		
	}

}
