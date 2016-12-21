package com.sohu.tv.utils;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.lang.builder.ToStringBuilder;

public class HttpResult {
	
	private int stateCode;

	private String content;

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getStateCode() {
		return stateCode;
	}

	public boolean getOK() {
		if (this.stateCode == HttpStatus.SC_OK || this.stateCode == HttpStatus.SC_MOVED_PERMANENTLY || this.stateCode == HttpStatus.SC_MOVED_TEMPORARILY) {
			return true;
		} else {
			return false;
		}
	}

	public void setStateCode(int stateCode) {
		this.stateCode = stateCode;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this).toString();
	}
	
	
}