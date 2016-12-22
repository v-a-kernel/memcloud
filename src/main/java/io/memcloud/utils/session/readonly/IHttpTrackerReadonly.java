package io.memcloud.utils.session.readonly;

import javax.servlet.http.HttpServletRequest;

import io.memcloud.utils.session.core.IAccount;
import io.memcloud.utils.session.core.IllegalSessionException;

public interface IHttpTrackerReadonly {

	public IAccount fetch(HttpServletRequest httpRequest)
	throws IllegalSessionException;
}
