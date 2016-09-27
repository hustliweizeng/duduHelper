package com.dudu.duduhelper.javabean;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/27
 */

public class RedbagDetailBean {


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

	public static class DataBean {
		private String id;
		private String title;
		private String num;
		private String used_num;
		private String total;
		private String logo;
		private String lower_money;
		private String upper_money;
		private String life;
		private String image;
		private LimitBean limit;
		private List<String> range;

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

		public String getUsed_num() {
			return used_num;
		}

		public void setUsed_num(String used_num) {
			this.used_num = used_num;
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

		public LimitBean getLimit() {
			return limit;
		}

		public void setLimit(LimitBean limit) {
			this.limit = limit;
		}

		public List<String> getRange() {
			return range;
		}

		public void setRange(List<String> range) {
			this.range = range;
		}

		public static class LimitBean {
			private String key;
			private String value;
			public LimitBean getLimitBean() {
				return new LimitBean();
			}

			public void setLimitBean(String key,String value) {
				this.key = key;
				this.value = value;
			}
		}
	}
}
