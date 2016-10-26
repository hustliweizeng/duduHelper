package com.dudu.helper3.bean;

import java.util.List;

public class OrderDetailDataBean 
{
	private String no;
	private String subject;
	private String total_fee;
	private String fee;
	private String thumb;
	private String name;
	private String mobile;
	private String ext;
	private String status;
	private String time;
	private List<OrderGoods> goods;
	private ProvienceBean source;
	private ProvienceBean paytype;
	private String discount_agent_fee;
	private String discount_shop_fee;
	private String discount_activity_fee;
	private String shop_fee;
	private CouponBean coupon;
	
	
	public CouponBean getCoupon() {
		return coupon;
	}
	public void setCoupon(CouponBean coupon) {
		this.coupon = coupon;
	}
	public String getDiscount_agent_fee() {
		return discount_agent_fee;
	}
	public void setDiscount_agent_fee(String discount_agent_fee) {
		this.discount_agent_fee = discount_agent_fee;
	}
	public String getDiscount_shop_fee() {
		return discount_shop_fee;
	}
	public void setDiscount_shop_fee(String discount_shop_fee) {
		this.discount_shop_fee = discount_shop_fee;
	}
	public String getDiscount_activity_fee() {
		return discount_activity_fee;
	}
	public void setDiscount_activity_fee(String discount_activity_fee) {
		this.discount_activity_fee = discount_activity_fee;
	}
	public String getShop_fee() {
		return shop_fee;
	}
	public void setShop_fee(String shop_fee) {
		this.shop_fee = shop_fee;
	}
	public String getNo() {
		return no;
	}
	public void setNo(String no) {
		this.no = no;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String total_fee) {
		this.total_fee = total_fee;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getThumb() {
		return thumb;
	}
	public void setThumb(String thumb) {
		this.thumb = thumb;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getExt() {
		return ext;
	}
	public void setExt(String ext) {
		this.ext = ext;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<OrderGoods> getGoods() {
		return goods;
	}
	public void setGoods(List<OrderGoods> goods) {
		this.goods = goods;
	}
	public ProvienceBean getSource() {
		return source;
	}
	public void setSource(ProvienceBean source) {
		this.source = source;
	}
	public ProvienceBean getPaytype() {
		return paytype;
	}
	public void setPaytype(ProvienceBean paytype) {
		this.paytype = paytype;
	}

}
