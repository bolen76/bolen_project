package com.bolen.seckill.service;

import com.bolen.seckill.common.dto.ResultInfo;

public interface SeckillService {

	public void order(Integer uid, Integer pid);

	public void launch();

	public ResultInfo shop(Integer pid, Integer uid);

	public void checkOrder();
}
