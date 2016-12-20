package com.dudu.duduhelper.javabean;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/22
 */

public class ShopListBean implements Serializable{


	private String code;
	private String msg;
	private List<DataBean> data;

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

	public List<DataBean> getData() {
		return data;
	}

	public void setData(List<DataBean> data) {
		this.data = data;
	}

	public static class DataBean implements Serializable{
		private String id;
		private String name;
		private String address;
		private String trade_history;
		private String income_sum;
		private String uv_history;
		private String order_num;
		private String trade_month;
		private String income_month;
		private String user_num;
		private List<String> images;
		private String status;
		private String logo;

		public String getLogo() {
			return logo;
		}

		public void setLogo(String logo) {
			this.logo = logo;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getTrade_history() {
			return trade_history;
		}

		public void setTrade_history(String trade_history) {
			this.trade_history = trade_history;
		}

		public String getIncome_sum() {
			return income_sum;
		}

		public void setIncome_sum(String income_sum) {
			this.income_sum = income_sum;
		}

		public String getUv_history() {
			return uv_history;
		}

		public void setUv_history(String uv_history) {
			this.uv_history = uv_history;
		}

		public String getOrder_num() {
			return order_num;
		}

		public void setOrder_num(String order_num) {
			this.order_num = order_num;
		}

		public String getTrade_month() {
			return trade_month;
		}

		public void setTrade_month(String trade_month) {
			this.trade_month = trade_month;
		}

		public String getIncome_month() {
			return income_month;
		}

		public void setIncome_month(String income_month) {
			this.income_month = income_month;
		}

		public String getUser_num() {
			return user_num;
		}

		public void setUser_num(String user_num) {
			this.user_num = user_num;
		}

		public List<String> getImages() {
			return images;
		}

		public void setImages(List<String> images) {
			this.images = images;
		}
	}
}
