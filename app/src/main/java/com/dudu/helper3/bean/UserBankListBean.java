package com.dudu.helper3.bean;

import java.util.List;

//获取支持的银行卡列表
public class UserBankListBean 
{
	private String status;
	private String info;
	private List<String> data;
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
	public List<String> getData() {
		return data;
	}
	public void setData(List<String> data) {
		this.data = data;
	}
	

}
