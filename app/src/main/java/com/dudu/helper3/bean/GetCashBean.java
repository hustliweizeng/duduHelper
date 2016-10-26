package com.dudu.helper3.bean;

import java.util.List;

public class GetCashBean 
{
	private String status;
	private String info;
	private List<GetCashDataBean> data;
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
	public List<GetCashDataBean> getData() {
		return data;
	}
	public void setData(List<GetCashDataBean> data) {
		this.data = data;
	}
	

}
