package com.dudu.helper3.bean;

import java.util.List;

public class UserBankProvienceBean 
{
	private String status;
	private String info;
	private List<ProvienceBean> data;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public List<ProvienceBean> getData() {
		return data;
	}
	public void setData(List<ProvienceBean> data) {
		this.data = data;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	

}
