package com.dudu.duduhelper.bean;

//用户登录信息
public class UserDataBean 
{
	
	private String id;
	private String username;
	private String shopname;
	private String shoplogo;
	private String mobile;
	private String money;
	private String token;
	private UserToddystatBean todaystat; 
	private UserToddystatBean totalstat;
	private UserBankBean bank;
	private String agentname;
	
	public String getAgentname() {
		return agentname;
	}
	public void setAgentname(String agentname) {
		this.agentname = agentname;
	}
	public String getId() {
		return id;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getShopname() {
		return shopname;
	}
	public void setShopname(String shopname) {
		this.shopname = shopname;
	}
	public String getShoplogo() {
		return shoplogo;
	}
	public void setShoplogo(String shoplogo) {
		this.shoplogo = shoplogo;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public UserToddystatBean getTodaystat() {
		return todaystat;
	}
	public void setTodaystat(UserToddystatBean todaystat) {
		this.todaystat = todaystat;
	}
	public UserToddystatBean getTotalstat() {
		return totalstat;
	}
	public void setTotalstat(UserToddystatBean totalstat) {
		this.totalstat = totalstat;
	}
	public UserBankBean getBank() {
		return bank;
	}
	public void setBank(UserBankBean bank) {
		this.bank = bank;
	}
}
