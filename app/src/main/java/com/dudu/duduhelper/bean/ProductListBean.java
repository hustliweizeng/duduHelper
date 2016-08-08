package com.dudu.duduhelper.bean;

import java.io.Serializable;

public class ProductListBean implements Serializable
{
	private String id;
	private String subject;
	private String stock;
	private String price1;
	private String price2;
	private String sold;
	private String status;
	private String pic;
	private String time_start;
	private String time_end;
	private String verify;
	
	public String getVerify() {
		return verify;
	}
	public void setVerify(String verify) {
		this.verify = verify;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getStock() {
		return stock;
	}
	public void setStock(String stock) {
		this.stock = stock;
	}
	public String getPrice1() {
		return price1;
	}
	public void setPrice1(String price1) {
		this.price1 = price1;
	}
	public String getPrice2() {
		return price2;
	}
	public void setPrice2(String price2) {
		this.price2 = price2;
	}
	public String getSold() {
		return sold;
	}
	public void setSold(String sold) {
		this.sold = sold;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getPic() {
		return pic;
	}
	public void setPic(String pic) {
		this.pic = pic;
	}
	public String getTime_start() {
		return time_start;
	}
	public void setTime_start(String time_start) {
		this.time_start = time_start;
	}
	public String getTime_end() {
		return time_end;
	}
	public void setTime_end(String time_end) {
		this.time_end = time_end;
	}
	

}
