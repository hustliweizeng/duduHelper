package com.dudu.helper3.bean;

public class UserBean 
{
	//用户登录
	private String status;
	//sucess,fail
	private String info;
	private UserDataBean data;
	public UserDataBean getData() {
		return data;
	}
	public void setData(UserDataBean data) {
		this.data = data;
	}
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

}
