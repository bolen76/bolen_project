package com.bolen.seckill.task;

import java.util.Date;

import org.apache.log4j.Logger;

import com.bolen.seckill.common.constant.OrderStatus;
import com.bolen.seckill.common.constant.SeckillConstant;
import com.bolen.seckill.common.util.RedisClient;
import com.bolen.seckill.common.util.SpringContextUtil;
import com.bolen.seckill.context.SeckillContext;
import com.bolen.seckill.dao.mapper.SeckillOrderMapper;
import com.bolen.seckill.dao.model.SeckillOrder;

public class OrderTask implements Runnable {
	private Integer pid;
	private Integer uid;

	private static final SeckillOrderMapper orderMapper = SpringContextUtil.getBean(SeckillOrderMapper.class);
	private static final RedisClient redisClient = SpringContextUtil.getBean(RedisClient.class);
	private static final Logger logger = Logger.getLogger(OrderTask.class);

	public OrderTask(Integer pid, Integer uid) {
		super();
		this.pid = pid;
		this.uid = uid;
	}

	@Override
	public void run() {
//		try {
//			Thread.sleep(1);
//		} catch (InterruptedException e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		}
		SeckillOrder seckillOrder = new SeckillOrder();
		seckillOrder.setPid(this.pid);
		seckillOrder.setUid(this.uid);
		seckillOrder.setStatus(OrderStatus.UNPAYED.getStatus());
		seckillOrder.setCreateTime(new Date());
		seckillOrder.setPrice(SeckillContext.getProduct(this.pid).getSpecialPrice());
		String message = "生成订单失败";
		boolean flag = false;
		try {

			int insert = orderMapper.insert(seckillOrder);
			if (insert == 1) {
				flag = true;
				message = "生成订单成功";
			}
		} catch (Exception e) {
			logger.info("生成订单失败：pid[" + pid + "]," + "uid[" + uid + "].");
		}
		if (!flag) {
			// 订单生成失败，回滚
			String pidAmountKey = SeckillConstant.PRODUCT_AMOUNT_KEY_PRE + pid;
			redisClient.increse(pidAmountKey);
			String usersetKey = SeckillConstant.PRODUCT_USERSET_KEY_PRE + pid;
			redisClient.srem(usersetKey, this.uid.toString());
			//消息推送，通知用户秒杀失败
		}else{
			
		}
	}

}
