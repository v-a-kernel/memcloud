/**
 * 
 */
package com.sohu.tv.memcloud.stats.dao;

import java.util.List;

import com.mongodb.DBObject;
import com.sohu.tv.memcloud.stats.model.Constants;
import com.sohu.tv.memcloud.stats.model.StatDBObject;

/**
 * @author ganghuawang
 *
 */
public interface IInstanceStatManager {

	/* 获取节点当前状态*/
	public StatDBObject getCurrentStat(String collName);
	
	/* 每日get次数趋势*/
	public List<StatDBObject> getDailyGetTrendStat(DBObject query, String collName, Constants.TimeUnit timeUnit);
	
	/* 每日set次数趋势*/
	public List<StatDBObject> getDailySetTrendStat(DBObject query, String collName, Constants.TimeUnit timeUnit);
	
	/* 每日hit次数趋势*/
	public List<StatDBObject> getDailyHitTrendStat(DBObject query, String collName, Constants.TimeUnit timeUnit);
	
	/* 每日miss次数趋势*/
	public List<StatDBObject> getDailyMissesTrendStat(DBObject query, String collName, Constants.TimeUnit timeUnit);
}
