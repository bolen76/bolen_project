package com.bolen.seckill.common.dto;

import java.util.HashMap;
import java.util.Map;

public class ResultInfo {

	private boolean success;

	private String message;

	private Map data;

	public ResultInfo(boolean success, String message, Map data) {
		super();
		this.success = success;
		this.message = message;
		this.data = data;
	}

	public ResultInfo(boolean success) {
		super();
		this.success = success;
		data = new HashMap<>();
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map getData() {
		return data;
	}

	public void setData(Map data) {
		this.data = data;
	}

	public Object get(String key) {
		return data.get(key);
	}

	public void set(String key, Object value) {
		data.put(key, value);
	}
}
