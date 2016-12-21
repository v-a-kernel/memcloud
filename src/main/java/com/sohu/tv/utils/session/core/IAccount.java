package com.sohu.tv.utils.session.core;

public interface IAccount {

	public Long getUserId();
	
	public String getScreenName();
	
	public String getHeadFace();

	public void setAttribute(String attribute, Object value);
	
	public Object getAttribute(String attribute);
}
