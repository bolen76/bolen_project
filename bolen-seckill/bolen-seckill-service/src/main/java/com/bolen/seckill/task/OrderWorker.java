package com.bolen.seckill.task;

import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class OrderWorker {
	//生成订单
	private static final ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 20, 30, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>(100));
	//处理未按时支付的订单
	public static final ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
	static {
		//饱和策略为抛出异常
		executor.setRejectedExecutionHandler(new ThreadPoolExecutor.AbortPolicy());
	}

	public static Future<?> submit(OrderTask orderTask) {
		Future<?> submit = executor.submit(orderTask);
		return submit;
	}

	public static void shutdown() {
		executor.shutdown();
	}
}
