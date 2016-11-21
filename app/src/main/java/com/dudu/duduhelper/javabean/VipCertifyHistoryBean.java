package com.dudu.duduhelper.javabean;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/11/3
 */

public class VipCertifyHistoryBean {

	private String code;
	private String msg;
	private List<ListBean> list;

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

	public List<ListBean> getList() {
		return list;
	}

	public void setList(List<ListBean> list) {
		this.list = list;
	}

	public static class ListBean {
		private String name;
		private String image;
		private String used_time;
		private String used_user;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getUsed_time() {
			return used_time;
		}

		public void setUsed_time(String used_time) {
			this.used_time = used_time;
		}

		public String getUsed_user() {
			return used_user;
		}

		public void setUsed_user(String used_user) {
			this.used_user = used_user;
		}
	}
}
