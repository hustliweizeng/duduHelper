package com.dudu.duduhelper.bean;

import java.io.Serializable;

public class GetHongBaoHistDataBean implements Serializable
{
	private String id;
	private String red_packet_id;
	private String user_id;
	private String log_time;
	private String used_time;
	private String money;
	private String used;
	private String order_id;
	private String from_order_id;
	private String nickname;
	private String title;
	private String life;
	private String expire_time;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRed_packet_id() {
		return red_packet_id;
	}
	public void setRed_packet_id(String red_packet_id) {
		this.red_packet_id = red_packet_id;
	}
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getLog_time() {
		return log_time;
	}
	public void setLog_time(String log_time) {
		this.log_time = log_time;
	}
	public String getUsed_time() {
		return used_time;
	}
	public void setUsed_time(String used_time) {
		this.used_time = used_time;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getUsed() {
		return used;
	}
	public void setUsed(String used) {
		this.used = used;
	}
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getFrom_order_id() {
		return from_order_id;
	}
	public void setFrom_order_id(String from_order_id) {
		this.from_order_id = from_order_id;
	}
	public String getNickname() {
		return nickname;
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getLife() {
		return life;
	}
	public void setLife(String life) {
		this.life = life;
	}
	public String getExpire_time() {
		return expire_time;
	}
	public void setExpire_time(String expire_time) {
		this.expire_time = expire_time;
	}
	
	

}
