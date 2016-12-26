/**
 * 
 */
package io.memcloud.stats.business;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.memcloud.stats.business.impl.MemInstanceConnectionPool;
import net.rubyeye.xmemcached.MemcachedClient;

/**
 * @author ganghuawang 随app一起启动，抓取memcached实例信息
 */
public class CaptureScheduler {

	private static final Logger LOG = LoggerFactory.getLogger(CaptureScheduler.class);

	@Resource(name = "memInstanceMetaFetcher")
	private IMemInstanceMetaFetcher memInstanceMetaFetcher;

	@Resource(name = "memInstanceConnectionPool")
	private MemInstanceConnectionPool memInstanceConnectionPool;

	@Resource(name = "memInstanceFaultCapture")
	private MemInstanceFaultCapture memInstanceFaultCapture;

	@Resource(name = "memInstanceStatsCapture")
	private MemInstanceStatsCapture memInstanceStatsCapture;

	public void start() {

		List<String> memInstances = memInstanceMetaFetcher.scanMemInstances();
		for (String instance : memInstances) {
			int idx = instance.indexOf(":");
			memInstanceConnectionPool.addClient(instance.substring(0, idx),
					Integer.parseInt(instance.substring(idx + 1)));
		}

		startFaultCapture();

		startStatsCapture();

	}

	private void startFaultCapture() {
		Map<String, MemcachedClient> clientPool = memInstanceConnectionPool.getConnectionPool();
		for (String clientName : clientPool.keySet()) {

			MemcachedClient client = clientPool.get(clientName);
			client.addStateListener(memInstanceFaultCapture);

		}
	}

	private void startStatsCapture() {

		ScheduledExecutorService scheduledService = Executors.newScheduledThreadPool(1);

		// 每个60s执行一次任务
		scheduledService.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				Map<String, MemcachedClient> clientPool = memInstanceConnectionPool.getConnectionPool();
				LOG.info("get state info,  memcached instances : " + clientPool.size());

				for (String clientName : clientPool.keySet()) {
					MemcachedClient client = clientPool.get(clientName);
					memInstanceStatsCapture.stat(client);
				}
				
			}

		}, 5, 60, TimeUnit.SECONDS);
	}

}
