package com.bolen.seckill.task;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

import com.bolen.seckill.common.constant.OrderStatus;
import com.bolen.seckill.common.constant.SeckillConstant;
import com.bolen.seckill.common.util.RedisClient;
import com.bolen.seckill.common.util.SpringContextUtil;
import com.bolen.seckill.context.SeckillContext;
import com.bolen.seckill.dao.mapper.SeckillOrderMapper;
import com.bolen.seckill.dao.model.SeckillProduct;
import com.bolen.seckill.service.impl.SeckillServiceImpl;

public class CheckOrderTask implements Runnable {
	private static final SeckillOrderMapper orderMapper = SpringContextUtil.getBean(SeckillOrderMapper.class);
	private static final RedisClient redisClient = SpringContextUtil.getBean(RedisClient.class);
	private static final Map<Integer, SeckillProduct> products = SeckillContext.getAllProducts();
	private static final Logger logger = Logger.getLogger(SeckillServiceImpl.class);

	@Override
	public void run() {
		logger.info("检查未及时付款的订单...");
		Date currentTime = new Date();
		// 30分钟前的订单会被设置为失效
		Date limitTime = new Date(currentTime.getTime() - 30 * 60 * 1000);
		Map params = new HashMap<>();
		params.put("createTime", limitTime);
		params.put("poststatus", OrderStatus.INVALID.getStatus());
		params.put("prestatus", OrderStatus.UNPAYED.getStatus());
		for (SeckillProduct product : products.values()) {
			Date startTime = product.getStartTime();
			if (startTime.before(currentTime)) {
				Integer pid = product.getPid();
				params.put("pid", pid);
				try {
					int updateAmout = orderMapper.updateUnPayed(params);
					String pidAmountKey = SeckillConstant.PRODUCT_AMOUNT_KEY_PRE + pid;
					redisClient.incrby(pidAmountKey, updateAmout);
					logger.info("未在30分钟内支付订单的商品：pid[" + pid + "],数量[" + updateAmout + "]");
					// 抢了没付订单的人，还是否允许他再重新抢？

				} catch (Exception e) {
					logger.info(e);
				}

			}

		}
	}

}
