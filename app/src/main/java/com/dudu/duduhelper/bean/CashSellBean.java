package com.dudu.duduhelper.bean;

//获取优惠券核销结果
public class CashSellBean 
{
	private String status;
	private String info;
	private CashSellDataBean data;
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
	public CashSellDataBean getData() {
		return data;
	}
	public void setData(CashSellDataBean data) {
		this.data = data;
	}

}
