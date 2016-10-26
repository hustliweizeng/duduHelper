package com.dudu.helper3.bean;

public class AccountDataResponBean 
{
	private String visiter;//访客数
	private String buyer;//顾客数
	private String order;//订单数
	private String income;//收入
	private String trade;//交易总额
	
	
	public String getTrade() {
		return trade;
	}
	public void setTrade(String trade) {
		this.trade = trade;
	}
	public String getVisiter() {
		return visiter;
	}
	public void setVisiter(String visiter) {
		this.visiter = visiter;
	}
	public String getBuyer() {
		return buyer;
	}
	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getIncome() {
		return income;
	}
	public void setIncome(String income) {
		this.income = income;
	}
	

}
