package com.dudu.duduhelper.bean;

import java.util.List;

public class OrderDetailBean 
{
	private String status;
	private String info;
	private OrderDetailDataBean data;
	public String getStatus() 
	{
		return status;
	}
	public void setStatus(String status) 
	{
		this.status = status;
	}
	public String getInfo() 
	{
		return info;
	}
	public void setInfo(String info) 
	{
		this.info = info;
	}
	public OrderDetailDataBean getData() {
		return data;
	}
	public void setData(OrderDetailDataBean data) {
		this.data = data;
	}
	
	

}
