package com.bolen.seckill.service.impl;

import java.util.Date;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolen.seckill.common.constant.SeckillConstant;
import com.bolen.seckill.common.dto.ResultInfo;
import com.bolen.seckill.common.util.RedisClient;
import com.bolen.seckill.context.SeckillContext;
import com.bolen.seckill.dao.mapper.SeckillProductMapper;
import com.bolen.seckill.dao.model.SeckillProduct;
import com.bolen.seckill.service.SeckillService;
import com.bolen.seckill.task.CheckOrderTask;
import com.bolen.seckill.task.OrderTask;
import com.bolen.seckill.task.OrderWorker;

@Service
public class SeckillServiceImpl implements SeckillService {
	private static final Logger logger = Logger.getLogger(SeckillServiceImpl.class);
	
	@Autowired
	SeckillProductMapper productMapper;
	@Autowired
	RedisClient redisClient;

	public void order(Integer uid, Integer pid) {
		SeckillProduct product = productMapper.selectByPrimaryKey(pid);
		System.out.println("product:" + product.getAmount());
		logger.info("hahaha");

	}

	@Override
	public void launch() {
		List<SeckillProduct> list = productMapper.findByNo(SeckillConstant.CODE_NO);

		for (SeckillProduct product : list) {
			redisClient.set(SeckillConstant.PRODUCT_AMOUNT_KEY_PRE + product.getPid().toString(),
					product.getAmount().toString());
		}

	}

	@Override
	public ResultInfo shop(Integer pid, Integer uid) {

		ResultInfo result = new ResultInfo(true);
		// 防止捣乱.
		SeckillProduct product = SeckillContext.getProduct(pid);

		Date currentTime = new Date();
		if (product == null || currentTime.before(product.getStartTime())) {
			result.setMessage("别捣蛋.....");
			logger.info("违规秒杀行为：pid[" + pid + "],uid[" + uid + "],product[" + product + "].");
			return result;
		}
		// 单个人秒杀次数控制
		/*boolean hasGain = SeckillContext.hasGain(pid, uid);
		if (hasGain) {
			result.setMessage("你已经抢到了，不能再抢.....");
			return result;
		}*/
		// 集群环境下，要去redis服务器查看，防止单人多抢
		String usersetKey = SeckillConstant.PRODUCT_USERSET_KEY_PRE + pid;
		boolean sismember = redisClient.sismember(usersetKey, uid.toString());
		if (sismember) {
			result.setMessage("你已经抢到了，不能再抢.....");
			return result;
		}

		String pidAmountKey = SeckillConstant.PRODUCT_AMOUNT_KEY_PRE + pid;

		Integer remain = redisClient.decrese(pidAmountKey);
		if (remain < 0) {
			result.setMessage("抢购失败.....");
			return result;
		}
		// 走到这里，就是秒杀成功了
		OrderTask orderTask = new OrderTask(pid, uid);
		try {
			OrderWorker.submit(orderTask);
		} catch (Exception e) {
			logger.info("秒杀失败：pid[" + pid + "],uid[" + uid + "],product[" + product + "].", e);
			result.setMessage("秒杀失败......");
			// 恢复商品数量
			redisClient.increse(pidAmountKey);
			return result;
		}
		redisClient.sadd(usersetKey, uid.toString());
//		SeckillContext.add(pid, uid);
		logger.info("秒杀成功：pid[" + pid + "],uid[" + uid + "],product[" + product + "].");
		result.setMessage("正在生成订单，请稍等......");
		return result;

	}

	@Override
	public void checkOrder() {
		CheckOrderTask checkOrderTask = new CheckOrderTask();
		OrderWorker.scheduledExecutor.scheduleAtFixedRate(checkOrderTask, 1, 5, TimeUnit.MINUTES);
	}

}
