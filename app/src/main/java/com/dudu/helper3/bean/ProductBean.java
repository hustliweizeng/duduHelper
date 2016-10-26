package com.dudu.helper3.bean;

import java.util.List;

public class ProductBean 
{
	private String status;
	private String info;
	private List<ProductListBean> data;
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
	public List<ProductListBean> getData() {
		return data;
	}
	public void setData(List<ProductListBean> data) {
		this.data = data;
	}

}
