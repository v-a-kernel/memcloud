package io.memcloud.stats.business;

import java.util.Map;

import net.rubyeye.xmemcached.MemcachedClient;

public interface IMemInstanceConnectionPool {

	/**
	 * 初始化所有的客户端
	 */
	public void init();
	
	/**
	 * 删除客户端
	 * @param host
	 * @param port
	 */
	public void removeClient(String host, int port);
	
	/**
	 * 添加客户端
	 * @param host
	 * @param port
	 */
	public void addClient(String host, int port);
	
	
	public Map<String, MemcachedClient> getConnectionPool();
	
}
