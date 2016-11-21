package com.dudu.duduhelper.bean;

import java.util.List;

public class GetCouponSellBean 
{
	private String status;
	private String info;
	private List<GetCouponSellDataBean> data;
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
	public List<GetCouponSellDataBean> getData() {
		return data;
	}
	public void setData(List<GetCouponSellDataBean> data) {
		this.data = data;
	}
	

}
