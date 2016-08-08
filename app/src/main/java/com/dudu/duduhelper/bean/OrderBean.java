package com.dudu.duduhelper.bean;

import java.util.List;

public class OrderBean 
{
	private String status;
	private String info;
	private List<OrderDataBean> data;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public List<OrderDataBean> getData() {
		return data;
	}
	public void setData(List<OrderDataBean> data) {
		this.data = data;
	}
	

}
