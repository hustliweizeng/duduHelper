package com.dudu.helper3.bean;

import java.util.List;

public class CashHistoryBean 
{
	private String code;
	private String msg;
	private List<CashHistoryDataBean> data;
	public String getCode() {
		return code;
	}
	public void setStatus(String status) {
		this.code = status;
	}
	public String getMsg() {
		return msg;
	}
	public void setInfo(String info) {
		this.msg = info;
	}
	public List<CashHistoryDataBean> getData() {
		return data;
	}
	public void setData(List<CashHistoryDataBean> data) {
		this.data = data;
	}
	

}
