package com.bolen.seckill.common.constant;

public enum OrderStatus {
	UNPAYED(1), PAYED(2), INVALID(3);

	private int status;

	private OrderStatus( int status) {
		 this.status = status;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

}
