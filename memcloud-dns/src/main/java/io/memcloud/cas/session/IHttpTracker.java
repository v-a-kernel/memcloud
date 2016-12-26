package io.memcloud.cas.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.memcloud.cas.session.core.IAccount;
import io.memcloud.cas.session.readonly.IHttpTrackerReadonly;

public interface IHttpTracker extends IHttpTrackerReadonly {

	public String save(IAccount account,int expireSec,HttpServletRequest httpRequest,HttpServletResponse httpResponse);
	
	public void remove(HttpServletRequest httpRequest,HttpServletResponse httpResponse);
}
