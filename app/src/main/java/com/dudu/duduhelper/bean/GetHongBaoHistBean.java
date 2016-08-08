package com.dudu.duduhelper.bean;

import java.util.List;

public class GetHongBaoHistBean 
{
	private String status;
	private String info;
	private List<GetHongBaoHistDataBean> data;
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
	public List<GetHongBaoHistDataBean> getData() {
		return data;
	}
	public void setData(List<GetHongBaoHistDataBean> data) {
		this.data = data;
	}
	
	

}
