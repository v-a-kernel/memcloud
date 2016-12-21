package com.sohu.tv.memcloud.stats.dao.impl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.regex.Pattern;

import com.mongodb.BasicDBObject;

import com.sohu.tv.memcloud.stats.MemStatSummary;
import com.sohu.tv.memcloud.stats.dao.IInstanceStatManager;
import com.sohu.tv.memcloud.stats.dao.IMemStatDao;
import com.sohu.tv.memcloud.stats.model.Constants;
import com.sohu.tv.memcloud.stats.model.StatDBObject;
import com.sohu.tv.utils.StatDateType;

/**
 * cmdType {0:GET,1:SET,2:Hit,3:Mis}
 * */
public class MemStatDaoImpl implements IMemStatDao {

	protected IInstanceStatManager instatnceStatManager;

	public MemStatDaoImpl() {

	}

	public MemStatDaoImpl(IInstanceStatManager instatnceStatManager) {
		this.instatnceStatManager = instatnceStatManager;
	}

	@Override
	public MemStatSummary summary(String memIP, int memPort) {
		MemStatSummary s = new MemStatSummary();
		String collectionName = Constants.COLL_PREFIX+memIP+":"+memPort;
		StatDBObject r = instatnceStatManager.getCurrentStat(collectionName);
		if (r == null) {//信息是实时获取的
			return null;
//			s.setUptimeSecond(1123455);
//			s.setCurrTimeSecond(System.currentTimeMillis()/1000L);
//			s.setVersion("1.4.6");
//			s.setCmd_get((Long)r.get(Constants.CMD_GET));
//			s.setCmd_set((Long)r.get(Constants.CMD_SET));
//			s.setGet_hit((Long)r.get(Constants.GET_HITS));
//			s.setGet_mis((Long)r.get(Constants.GET_MISSES));
//			s.setCurrItems((Long)r.get("curr_items"));
//			s.setTotalItems((Long)r.get("total_items"));
//			s.setUserBytes((Long)r.get("bytes"));
//			s.setTotalBytes((Long)r.get("limit_maxbytes"));
//			s.setCurrConns((Long)r.get("curr_connections"));
//			s.setTotalConns((Long)r.get("total_connections"));
//			s.setReadBytes((Long)r.get("bytes_read"));
//			s.setWriteBytes((Long)r.get("bytes_written"));
//			return s;
		}
		//基本信息
		s.setUptimeSecond((Long)r.get("uptime"));
		s.setCurrTimeSecond((Long)r.get("time"));
		s.setVersion((String)r.get("version"));

		//第一部分  命中率
		s.setCmd_get((Long)r.get(Constants.CMD_GET));
		s.setCmd_set((Long)r.get(Constants.CMD_SET));
//		s.setGet_hit((Long)r.get(Constants.GET_HITS));
//		s.setGet_mis((Long)r.get(Constants.GET_MISSES));
		//临时变更
		s.setGet_hit((Long)r.get(Constants.INCR_HITS));
		s.setGet_mis((Long)r.get(Constants.INCR_MISSES));

		//第二部分  内存使用率
		s.setCurrItems((Long)r.get("curr_items"));
		s.setTotalItems((Long)r.get("total_items"));
		s.setUsedBytes((Long)r.get("bytes"));
		s.setTotalBytes((Long)r.get("limit_maxbytes"));
		//第三部分  连接数
		s.setCurrConns((Long)r.get("curr_connections"));
		s.setTotalConns((Long)r.get("total_connections"));
		//第四部分 带宽
		s.setReadBytes((Long)r.get("bytes_read"));
		s.setWriteBytes((Long)r.get("bytes_written"));
		return s;
	}

	@Override
	public LinkedHashMap<String, Long> trendGet(String memIP, int memPort,
			Date statDate) {
		return trendCmd(memIP,memPort,statDate,0);
	}

	@Override
	public LinkedHashMap<String, Long> trendSet(String memIP, int memPort,
			Date statDate) {
		return trendCmd(memIP,memPort,statDate,1);
	}

	@Override
	public LinkedHashMap<String, Long> trendHit(String memIP, int memPort,
			Date statDate) {
		return trendCmd(memIP,memPort,statDate,2);
	}

	@Override
	public LinkedHashMap<String, Long> trendMis(String memIP, int memPort,
			Date statDate) {
		return trendCmd(memIP,memPort,statDate,3);
	}

	/**
	 * @param cmdType {0:GET,1:SET,2:Hit,3:Mis}
	 * */
	@Override
	public LinkedHashMap<String, Long> trendCmd(String memIP, int memPort,Date statDate, int cmdType) {
		LinkedHashMap<String, Long> f = new LinkedHashMap<String, Long>();//y=f(x)

		Pattern datePattern = Pattern.compile(StatDateType.format(statDate, "yyyy-MM-dd")+".*", Pattern.CASE_INSENSITIVE);
		BasicDBObject query = new BasicDBObject();
		query.put("datetime", datePattern);

		String collectionName = Constants.COLL_PREFIX+memIP+":"+memPort;
		List<StatDBObject> list = null;
		switch (cmdType) {
		case 0:
			list = instatnceStatManager.getDailyGetTrendStat(query, collectionName, Constants.TimeUnit.MINUTES_10);
			break;
		case 1:
			list = instatnceStatManager.getDailySetTrendStat(query, collectionName, Constants.TimeUnit.MINUTES_10);
			break;
		case 2:
			list = instatnceStatManager.getDailyHitTrendStat(query, collectionName, Constants.TimeUnit.MINUTES_10);
			break;
		case 3:
		default:
			list = instatnceStatManager.getDailyMissesTrendStat(query, collectionName, Constants.TimeUnit.MINUTES_10);
			break;
		}


		for (StatDBObject item : list) {
			String timeMin10 = ((String)item.get(Constants.DATETIME)).substring("yyyy-MM-dd ".length(), "yyyy-MM-dd ".length()+"HH:mm".length());
			f.put(timeMin10, (Long)item.get(Constants.STAT_DATA_KEY));
		}
		return f;//y=f(x)
	}

	public IInstanceStatManager getInstatnceStatManager() {
		return instatnceStatManager;
	}

	public void setInstatnceStatManager(IInstanceStatManager instatnceStatManager) {
		this.instatnceStatManager = instatnceStatManager;
	}


}
