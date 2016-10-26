package com.dudu.helper3.bean;

import java.io.Serializable;
import java.util.List;

public class MemberBean implements Serializable
{
	private String status;
	private String info;
	private List<MemberDataBean> data;
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
	public List<MemberDataBean> getData() {
		return data;
	}
	public void setData(List<MemberDataBean> data) {
		this.data = data;
	}
	

}
