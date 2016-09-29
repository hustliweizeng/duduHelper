package com.dudu.duduhelper.javabean;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/27
 */

public class RedbagDetailBean implements Serializable {

	private DataBean data;
	private String code;
	private String msg;

	public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
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

	public static class DataBean implements  Serializable{
		private String id;
		private String title;
		private String num;
		private String total;
		private String logo;
		private String lower_money;
		private String upper_money;
		private String life;
		private String image;
		private String time_start;
		private String time_end;
		private String send_num;
		private String used_num;
		private String send_money;
		private String used_money;
		private List<String> range;
		private List<LimitBean> limit;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getNum() {
			return num;
		}

		public void setNum(String num) {
			this.num = num;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}

		public String getLogo() {
			return logo;
		}

		public void setLogo(String logo) {
			this.logo = logo;
		}

		public String getLower_money() {
			return lower_money;
		}

		public void setLower_money(String lower_money) {
			this.lower_money = lower_money;
		}

		public String getUpper_money() {
			return upper_money;
		}

		public void setUpper_money(String upper_money) {
			this.upper_money = upper_money;
		}

		public String getLife() {
			return life;
		}

		public void setLife(String life) {
			this.life = life;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
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

		public String getSend_num() {
			return send_num;
		}

		public void setSend_num(String send_num) {
			this.send_num = send_num;
		}

		public String getUsed_num() {
			return used_num;
		}

		public void setUsed_num(String used_num) {
			this.used_num = used_num;
		}

		public String getSend_money() {
			return send_money;
		}

		public void setSend_money(String send_money) {
			this.send_money = send_money;
		}

		public String getUsed_money() {
			return used_money;
		}

		public void setUsed_money(String used_money) {
			this.used_money = used_money;
		}

		public List<String> getRange() {
			return range;
		}

		public void setRange(List<String> range) {
			this.range = range;
		}

		public List<LimitBean> getLimit() {
			return limit;
		}

		public void setLimit(List<LimitBean> limit) {
			this.limit = limit;
		}

		public static class LimitBean implements Serializable{
			private String price;
			private String usable;
			public String getPrice() {
				return price;
			}

			public void setPrice(String price) {
				this.price = price;
			}

			public String getUsable() {
				return usable;
			}

			public void setUsable(String usable) {
				this.usable = usable;
			}
		}
	}
}
