package com.dudu.duduhelper.javabean;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/10/12
 */

public class ShopStatusBean {


	private String active_user;
	private String inactivity_user;
	private String code;
	private String msg;
	private List<MessageListsBean> message_lists;

	public String getActive_user() {
		return active_user;
	}

	public void setActive_user(String active_user) {
		this.active_user = active_user;
	}

	public String getInactivity_user() {
		return inactivity_user;
	}

	public void setInactivity_user(String inactivity_user) {
		this.inactivity_user = inactivity_user;
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

	public List<MessageListsBean> getMessage_lists() {
		return message_lists;
	}

	public void setMessage_lists(List<MessageListsBean> message_lists) {
		this.message_lists = message_lists;
	}

	public static class MessageListsBean {
		private String id;
		private String type_id;
		private String month_free_num;
		private String name;
		private String image;
		private String desc;
		private String surplus;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getType_id() {
			return type_id;
		}

		public void setType_id(String type_id) {
			this.type_id = type_id;
		}

		public String getMonth_free_num() {
			return month_free_num;
		}

		public void setMonth_free_num(String month_free_num) {
			this.month_free_num = month_free_num;
		}

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

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public String getSurplus() {
			return surplus;
		}

		public void setSurplus(String surplus) {
			this.surplus = surplus;
		}
	}
}
