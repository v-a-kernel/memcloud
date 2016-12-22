package io.memcloud.stats;


import java.util.List;
import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.mongodb.BasicDBObject;
import io.memcloud.memdns.dao.IAppMemGroupDao;
import io.memcloud.stats.business.IMemInstanceMonitor;
import io.memcloud.stats.dao.IInstanceStatManager;
import io.memcloud.stats.dao.impl.InstanceStatManagerImpl;
import io.memcloud.stats.model.Constants;
import io.memcloud.stats.model.StatDBObject;


public class TestInstanceStatManager {

	public IInstanceStatManager stat ;
	private IAppMemGroupDao appMemGroupDao;
	private IMemInstanceMonitor memInstanceMonitor;
	
	@Before
	public void setUp() throws Exception {
		ApplicationContext factory = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		stat = (InstanceStatManagerImpl)factory.getBean("instatnceStatManager");
		appMemGroupDao = (IAppMemGroupDao)factory.getBean("appMemGroupDao");
		memInstanceMonitor = (IMemInstanceMonitor)factory.getBean("memInstanceMonitor");
	}
	
	@Test
	public void fault(){
		memInstanceMonitor.receiveFaultMessage(10L);
//		memInstanceMonitor.sendFaultMessage("10.10.83.177", 12101);
//		memInstanceMonitor.sendFaultMessage("10.10.83.177", 12101);
//		memInstanceMonitor.sendFaultMessage("10.10.83.177", 12101);
	}
	
//	@Test
	public void memGroup(){
		System.out.println(appMemGroupDao.getByHostAndPort("10.10.83.177", 12105));
	}
	
//	@Test
	public void curr(){
//		StatDBObject result = stat.getCurrentStat("coll_10.10.79.214:11211");//10.10.79.214:11211
		StatDBObject result = stat.getCurrentStat("coll_10.10.79.75:14501");//
		
//		{ "_id" : { "$oid" : "4fddafc5dc7ec8952b513c60"} , "delete_hits" : 0 , "datetime" : "2012-06-17 18:21:57" , "bytes" : 7550518888 , "total_items" : 3713613238 , "rusage_system" : "89612.447837" , "listen_disabled_num" : 0 , "auth_errors" : 0 , "evictions" : 0 , "version" : "1.4.6" , "pointer_size" : 64 , "time" : 1339928511 , "incr_hits" : 0 , "threads" : 4 , "limit_maxbytes" : 8589934592 , "bytes_read" : 190114223424 , "curr_connections" : 187 , "get_misses" : 51575653 , "reclaimed" : 4244639 , "bytes_written" : 37822695377 , "connection_structures" : 1285 , "cas_hits" : 0 , "delete_misses" : 0 , "total_connections" : 159136 , "rusage_user" : "52799.243294" , "cmd_flush" : 0 , "libevent" : "1.4.4-stable" , "uptime" : 3976742 , "pid" : 7920 , "cas_badval" : 0 , "get_hits" : 85412732 , "curr_items" : 84603797 , "cas_misses" : 0 , "accepting_conns" : 1 , "cmd_get" : 136988385 , "cmd_set" : 3713613243 , "auth_cmds" : 0 , "incr_misses" : 0 , "decr_misses" : 0 , "decr_hits" : 0 , "conn_yields" : 0}
		System.out.println(result.getDoc());
		/*
		{ "_id" : { "$oid" : "4fddafc5dc7ec8952b513c60"} ,
 "delete_hits" : 0 ,
 "datetime" : "2012-06-17 18:21:57" ,
 "bytes" : 7550518888 ,
 "total_items" : 3713613238 ,
 "rusage_system" : "89612.447837" ,
 "listen_disabled_num" : 0 ,
 "auth_errors" : 0 ,
 "evictions" : 0 ,
 "version" : "1.4.6" ,
 "pointer_size" : 64 ,
 "time" : 1339928511 ,
 "incr_hits" : 0 ,
 "threads" : 4 ,
 "limit_maxbytes" : 8589934592 ,
 "bytes_read" : 190114223424 ,
 "curr_connections" : 187 ,
 "get_misses" : 51575653 ,
 "reclaimed" : 4244639 ,
 "bytes_written" : 37822695377 ,
 "connection_structures" : 1285 ,
 "cas_hits" : 0 ,
 "delete_misses" : 0 ,
 "total_connections" : 159136 ,
 "rusage_user" : "52799.243294" ,
 "cmd_flush" : 0 ,
 "libevent" : "1.4.4-stable" ,
 "uptime" : 3976742 ,
 "pid" : 7920 ,
 "cas_badval" : 0 ,
 "get_hits" : 85412732 ,
 "curr_items" : 84603797 ,
 "cas_misses" : 0 ,
 "accepting_conns" : 1 ,
 "cmd_get" : 136988385 ,
 "cmd_set" : 3713613243 ,
 "auth_cmds" : 0 ,
 "incr_misses" : 0 ,
 "decr_misses" : 0 ,
 "decr_hits" : 0 ,
 "conn_yields" : 0}
		 * */
	}
	
//	@Test
	public void get(){
		long start = System.currentTimeMillis();
			
//		String dateStr = "2012-06-07 04";
		String dateStr = "2012-06-07";
		BasicDBObject query = new BasicDBObject();
		Pattern datePattern = Pattern.compile(dateStr+".*", Pattern.CASE_INSENSITIVE);
		query.put("datetime", datePattern);
			
//		List<StatDBObject> result = stat.getDailyGetTrendStat( query, "coll_10.10.79.214:11211", Constants.TimeUnit.MINUTES);
		List<StatDBObject> result = stat.getDailyGetTrendStat( query, "coll_10.10.79.214:11211", Constants.TimeUnit.MINUTES_5);
		for(StatDBObject stat : result){
			System.out.println(stat.getDoc());
		}
		
//		GregorianCalendar gc = new GregorianCalendar();
//		System.out.println(gc.get(Calendar.HOUR_OF_DAY)*60 + gc.get(Calendar.MINUTE));
		System.out.println(System.currentTimeMillis()-start);
	}


}
