package com.sohu.tv.utils.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sohu.tv.utils.session.core.IAccount;
import com.sohu.tv.utils.session.readonly.IHttpTrackerReadonly;

public interface IHttpTracker extends IHttpTrackerReadonly {

	public String save(IAccount account,int expireSec,HttpServletRequest httpRequest,HttpServletResponse httpResponse);
	
	public void remove(HttpServletRequest httpRequest,HttpServletResponse httpResponse);
}
