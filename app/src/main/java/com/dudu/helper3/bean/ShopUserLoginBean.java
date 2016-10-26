package com.dudu.helper3.bean;

public class ShopUserLoginBean 
{
	private ShopUserBean user;
	private ShopBean shop;
	private ShopTotalstat totalstat;
	private ShopTodaystat todaystat;
	private ShopBank bank;
	private String code;
	private String msg;
	public ShopUserBean getUser() {
		return user;
	}
	public void setUser(ShopUserBean user) {
		this.user = user;
	}
	public ShopBean getShop() {
		return shop;
	}
	public void setShop(ShopBean shop) {
		this.shop = shop;
	}
	public ShopTotalstat getTotalstat() {
		return totalstat;
	}
	public void setTotalstat(ShopTotalstat totalstat) {
		this.totalstat = totalstat;
	}
	public ShopTodaystat getTodaystat() {
		return todaystat;
	}
	public void setTodaystat(ShopTodaystat todaystat) {
		this.todaystat = todaystat;
	}
	public ShopBank getBank() {
		return bank;
	}
	public void setBank(ShopBank bank) {
		this.bank = bank;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
