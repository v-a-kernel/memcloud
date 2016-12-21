package com.sohu.tv.utils.session.readonly;

import javax.servlet.http.HttpServletRequest;

import com.sohu.tv.utils.session.core.IAccount;
import com.sohu.tv.utils.session.core.IllegalSessionException;

public interface IHttpTrackerReadonly {

	public IAccount fetch(HttpServletRequest httpRequest)
	throws IllegalSessionException;
}
