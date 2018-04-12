package com.bolen.seckill.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bolen.seckill.common.dto.ResultInfo;
import com.bolen.seckill.service.SeckillService;

@Controller
@RequestMapping("/seckill")
public class SeckillController {
	@Autowired
	SeckillService seckillService;

	private static final Logger logger = Logger.getLogger(SeckillController.class);

	@RequestMapping("/shop")
	@ResponseBody
	public ResultInfo shop(Integer pid, Integer uid) {
		ResultInfo resultInfo = seckillService.shop(pid, uid);
		return resultInfo;
	}

	@RequestMapping("/launch")
	@ResponseBody
	public void launch(String username, String password) {
		// 用户权限校验，不能随意开启秒杀，略。。。

		seckillService.launch();
		//启动一个线程，扫描数据库未及时支付的订单
		seckillService.checkOrder();
	}

}
