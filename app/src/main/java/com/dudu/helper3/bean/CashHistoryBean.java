package com.dudu.helper3.bean;

import java.util.List;

public class CashHistoryBean 
{
	private String code;
	private String msg;
	private List<CashHistoryDataBean> list;
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
	public List<CashHistoryDataBean> getList() {
		return list;
	}
	public void setList(List<CashHistoryDataBean> data) {
		this.list = data;
	}
	

}
