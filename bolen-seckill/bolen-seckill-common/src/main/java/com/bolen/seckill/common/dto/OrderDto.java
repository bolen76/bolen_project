package com.bolen.seckill.common.dto;

public class OrderDto {
	private Integer pid;
	private Integer uid;
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public OrderDto(Integer pid, Integer uid) {
		super();
		this.pid = pid;
		this.uid = uid;
	}
	
	
	
}
