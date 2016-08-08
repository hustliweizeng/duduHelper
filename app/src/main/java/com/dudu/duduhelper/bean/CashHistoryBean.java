package com.dudu.duduhelper.bean;

import java.util.List;

public class CashHistoryBean 
{
	private String status;
	private String info;
	private List<CashHistoryDataBean> data;
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
	public List<CashHistoryDataBean> getData() {
		return data;
	}
	public void setData(List<CashHistoryDataBean> data) {
		this.data = data;
	}
	

}
