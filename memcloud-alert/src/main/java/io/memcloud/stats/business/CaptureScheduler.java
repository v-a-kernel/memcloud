/**
 * 
 */
package io.memcloud.stats.business;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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

	private AtomicBoolean started = new AtomicBoolean(false);

	public void start() {

		if (started.compareAndSet(false, true)) {

			List<String> memInstances = memInstanceMetaFetcher.scanMemInstances();
			for (String instance : memInstances) {
				int idx = instance.indexOf(":");
				memInstanceConnectionPool.addClient(instance.substring(0, idx),
						Integer.parseInt(instance.substring(idx + 1)));
			}

			startFaultCapture();

			startStatsCapture();
		}

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

	public boolean hasStarted() {
		return started.get();
	}

	public static void main(String[] args) throws Exception {

		/*
		 * http://blog.csdn.net/qjyong/article/details/4745517
		 * 
		 * https://my.oschina.net/u/2301987/blog/626437
		 * 
		 * */
		ApplicationContext factory = new ClassPathXmlApplicationContext(
				"classpath:applicationContext-comms.xml",
				"classpath:applicationContext-business-stats.xml", 
				"classpath:applicationContext-data-mongodb.xml",
				"classpath:applicationContext-data-dao.xml"
				);

		CaptureScheduler captureScheduler = factory.getBean(CaptureScheduler.class);
		if (!captureScheduler.hasStarted()) {
			captureScheduler.start();
		}
	}

}
