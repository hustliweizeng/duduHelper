package com.dudu.duduhelper.javabean;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/27
 */

public class RedBagListBean {


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

	public static class DataBean {
		private String id;
		private String title;
		private String num;
		private String used_num;
		private String total;
		private String logo;

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
	}
}
