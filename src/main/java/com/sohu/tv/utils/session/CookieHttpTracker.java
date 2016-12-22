package com.sohu.tv.utils.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sohu.tv.utils.ClientInfo;
import com.sohu.tv.utils.MD5;
import com.sohu.tv.utils.session.core.IAccount;
import com.sohu.tv.utils.session.core.IllegalSessionException;
import com.sohu.tv.utils.session.core.SessionAccount;

public class CookieHttpTracker implements IHttpTracker {

	protected static final Logger log = LoggerFactory.getLogger(CookieHttpTracker.class);
	
	private static final String domainDefault = null;
	private static final String pathDefault = "/";
	private static final int expireDefault = -1;
	
	private static final String secureKey = "memdns_cookie_key";
	
	static final String PPC_TOKEN = "PPC_TOKEN";
	static final String PPC_UID = "PPC_UID";
	static final String PPC_NAME = "PPC_NAME";
	
	@Override
	public IAccount fetch(HttpServletRequest httpRequest)
	throws IllegalSessionException 
	{
		
		String uid = ClientInfo.getCookie(httpRequest, PPC_UID);
		String uname = ClientInfo.getCookie(httpRequest, PPC_NAME);
		String tokenRemote = ClientInfo.getCookie(httpRequest, PPC_TOKEN);
		
		if(StringUtils.isEmpty(uid) || StringUtils.isEmpty(uname) || StringUtils.isEmpty(tokenRemote)) {//尚未登录
			return null;
		}
		
		StringBuffer plain = new StringBuffer();
		plain.append(uid).append("|")
			.append(uname).append("|")
			.append(secureKey).append("|");
		
		String tokenLocal = MD5.MD5Encode(plain.toString()).substring(16, 24);
		if (! StringUtils.equalsIgnoreCase(tokenRemote, tokenLocal)) {//表示没有登录
			throw new IllegalSessionException(IllegalSessionException.REASON.ArtificalToken, "remote is "+tokenRemote+",local is "+tokenLocal);
		}
		
		SessionAccount acc = new SessionAccount();
		acc.setUserId(Long.parseLong(uid));
		acc.setScreenName(uname);
		acc.setHeadFace("");
		return acc; 
	}

	@Override
	public String save(IAccount account, int expireSec,
			HttpServletRequest httpRequest, HttpServletResponse httpResponse) {
		
		String uid = account.getUserId()+"";
		String uname = MD5.urlEncode(account.getScreenName());
		
		StringBuffer plain = new StringBuffer();
		plain.append(uid).append("|")
			.append(uname).append("|")
			.append(secureKey).append("|");
		
		String token = MD5.MD5Encode(plain.toString()).substring(16, 24);
		
		if (expireSec == 0) {
			expireSec = expireDefault;
		}
		ClientInfo.setCookie(httpResponse, PPC_TOKEN, token, domainDefault, pathDefault, expireSec);
		ClientInfo.setCookie(httpResponse, PPC_UID, uid, domainDefault, pathDefault, expireSec);
		ClientInfo.setCookie(httpResponse, PPC_NAME,uname,domainDefault, pathDefault, expireSec);
		
		return token;
	}

	@Override
	public void remove(HttpServletRequest httpRequest,
			HttpServletResponse httpResponse) {

		ClientInfo.removeCookie(httpResponse, PPC_TOKEN, "", domainDefault, pathDefault);
		ClientInfo.removeCookie(httpResponse, PPC_UID, "", domainDefault, pathDefault);
		ClientInfo.removeCookie(httpResponse, PPC_NAME, "", domainDefault, pathDefault);
		
	}

}
