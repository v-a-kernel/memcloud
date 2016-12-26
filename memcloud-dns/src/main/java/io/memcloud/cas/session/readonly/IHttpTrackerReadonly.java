package io.memcloud.cas.session.readonly;

import javax.servlet.http.HttpServletRequest;

import io.memcloud.cas.session.core.IAccount;
import io.memcloud.cas.session.core.IllegalSessionException;

public interface IHttpTrackerReadonly {

	public IAccount fetch(HttpServletRequest httpRequest)
	throws IllegalSessionException;
}
