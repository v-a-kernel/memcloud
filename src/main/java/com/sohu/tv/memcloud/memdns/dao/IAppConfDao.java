package com.sohu.tv.memcloud.memdns.dao;

import com.sohu.tv.memcloud.memdns.dao.entry.AppConf;

public interface IAppConfDao {

	public AppConf get(Long appId);
	
	public Long save(AppConf appConf);
	
	public void update(AppConf appConf);
	
	public void delete(Long id);
}
