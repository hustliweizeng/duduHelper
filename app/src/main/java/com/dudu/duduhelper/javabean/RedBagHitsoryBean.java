package com.dudu.duduhelper.javabean;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/10/11
 */

public class RedBagHitsoryBean {


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
		private String red_packet_title;
		private String member_nickname;
		private String money;
		private String used;
		private String expire_time;
		private String created_at;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getRed_packet_title() {
			return red_packet_title;
		}

		public void setRed_packet_title(String red_packet_title) {
			this.red_packet_title = red_packet_title;
		}

		public String getMember_nickname() {
			return member_nickname;
		}

		public void setMember_nickname(String member_nickname) {
			this.member_nickname = member_nickname;
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

		public String getExpire_time() {
			return expire_time;
		}

		public void setExpire_time(String expire_time) {
			this.expire_time = expire_time;
		}

		public String getCreated_at() {
			return created_at;
		}

		public void setCreated_at(String created_at) {
			this.created_at = created_at;
		}
	}
}
