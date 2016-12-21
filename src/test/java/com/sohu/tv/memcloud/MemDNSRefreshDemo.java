package com.sohu.tv.memcloud;

public class MemDNSRefreshDemo {

	public static void main(String[] args) {
		MemDNSRefresh memDNS = new MemDNSRefresh();
		MemDNSLookup lookup = memDNS.refreshDNS("10001");
		System.out.println(lookup);
		System.out.println(lookup.getVersion());
		System.out.println(lookup.getMemGroup());
	}

}
