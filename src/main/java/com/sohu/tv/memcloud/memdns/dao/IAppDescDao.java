package com.sohu.tv.memcloud.memdns.dao;

import java.util.List;

import com.sohu.tv.memcloud.memdns.dao.entry.AppDesc;

public interface IAppDescDao {

	public List<AppDesc> getByUid(Long uid);
	
	public AppDesc getByAppName(String appName);
	
	public AppDesc getByAppId(Long appId);
	
	public Long save(AppDesc appDesc);
	
	public void update(AppDesc appDesc);
	
	public void delete(Long id);
}
