package com.sohu.tv.memcloud.memdns.business;

import com.sohu.tv.memcloud.memdns.dao.entry.AppConf;
import com.sohu.tv.memcloud.memdns.dao.entry.AppConfAudit;
import com.sohu.tv.memcloud.memdns.dao.entry.AppDesc;

public interface IMemDNSPublisher {

	/** 发布某个应用的最新配置 */
	public AppConfAudit publishAppConf(AppDesc appDesc, long userId);
	
//	public AppConfAudit publishAppConf(long appId, long userId);
	
	
	public AppConf getAppConf(long appId);
}
