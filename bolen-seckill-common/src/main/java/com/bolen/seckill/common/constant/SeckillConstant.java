package com.bolen.seckill.common.constant;

import java.util.ResourceBundle;

public class SeckillConstant {

	// 活动标识号key值
	private static final String CODE_KEY = "seckill.code";
	// 活动标识号value值
	public static final String CODE_NO;
	static {
		ResourceBundle bundle = ResourceBundle.getBundle("seckillConfig");
		CODE_NO = bundle.getString(CODE_KEY);
	}
	//商品数量的key前缀
	public static final String PRODUCT_AMOUNT_KEY_PRE=CODE_NO+"-amount-";
	//抢购到商品的用户集合前缀
	public static final String PRODUCT_USERSET_KEY_PRE=CODE_NO+"-user-";
}
