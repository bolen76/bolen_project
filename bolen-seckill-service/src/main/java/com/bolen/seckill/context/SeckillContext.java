package com.bolen.seckill.context;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.alibaba.dubbo.common.utils.ConcurrentHashSet;
import com.bolen.seckill.common.constant.SeckillConstant;
import com.bolen.seckill.dao.mapper.SeckillProductMapper;
import com.bolen.seckill.dao.model.SeckillProduct;

public class SeckillContext implements ApplicationListener<ContextRefreshedEvent> {
	//秒杀商品的信息
	private static final ConcurrentHashMap<Integer, SeckillProduct> productInfo = new ConcurrentHashMap<>();
	//秒杀的结果,key-商品ID，value-抢到的人员id集合
	private static final ConcurrentHashMap<Integer, ConcurrentHashSet<Integer>> secResult = new ConcurrentHashMap<>();

	@Autowired
	SeckillProductMapper productMapper;

	/**
	 * 初始化秒杀商品的信息
	 */
	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		ApplicationContext parent = event.getApplicationContext().getParent();
		if (parent == null) {
			List<SeckillProduct> productList = productMapper.findByNo(SeckillConstant.CODE_NO);
			System.out.println("初始化。。。。。。。");
			for (SeckillProduct product : productList) {
				productInfo.put(product.getPid(), product);
				ConcurrentHashSet<Integer> hashSet = new ConcurrentHashSet<Integer>(product.getAmount()*2);
				secResult.put(product.getPid(), hashSet);
			}

		}

	}

	/**
	 * 获取秒杀的商品信息
	 * 
	 * @param pid
	 * @return
	 */
	public static SeckillProduct getProduct(Integer pid) {
		return productInfo.get(pid);
	}
	/**
	 * 获取秒杀的开始时间
	 * 
	 * @param pid
	 * @return
	 */
	public static Date getStartTime(Integer pid) {
		return productInfo.get(pid).getStartTime();
	}

	/**
	 * 增加用户到指定商品的抢购名单集合中
	 * @param uid
	 * @param pid
	 */
	public static void add(Integer pid,Integer uid) {
		ConcurrentHashSet<Integer> hashSet = secResult.get(pid);
		hashSet.add(uid);
	}
	
	/**
	 * 判断某人是否抢到某个商品
	 * @param pid
	 * @param uid
	 * @return
	 */
	public static boolean hasGain(Integer pid,Integer uid) {
		ConcurrentHashSet<Integer> set = secResult.get(pid);
		if(set==null){
			return false;
		}
		return set.contains(uid);
	}
	
	
	/**
	 * 获取所有秒杀的商品信息
	 * 
	 * @param pid
	 * @return
	 */
	public static Map<Integer,SeckillProduct> getAllProducts() {
		return productInfo;
	}
}
