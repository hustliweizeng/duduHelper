package com.dudu.helper3.javabean;

/**
 * @author
 * @version 1.0
 * @date 2016/9/12
 */
public class CheckTicketBean {
	private String code;
	private String	msg;
	private  CheckData data;

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

	public CheckData getData() {
		return data;
	}

	public void setData(CheckData data) {
		this.data = data;
	}

	public class CheckData {
		private String id;
		private String subject;
		private String price;
		private String current_price;
		private String from;
		private String num;
		private String member_name;
		private String name;
		private String mobile;
		private String address;
		private String created_time;
		private String expired_time;
		private String used_time;
		private String order_id;

		public String getOrder_id() {
			return order_id;
		}

		public void setOrder_id(String order_id) {
			this.order_id = order_id;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getSubject() {
			return subject;
		}

		public void setSubject(String subject) {
			this.subject = subject;
		}

		public String getPrice() {
			return price;
		}

		public void setPrice(String price) {
			this.price = price;
		}

		public String getCurrent_price() {
			return current_price;
		}

		public void setCurrent_price(String current_price) {
			this.current_price = current_price;
		}

		public String getFrom() {
			return from;
		}

		public void setFrom(String from) {
			this.from = from;
		}

		public String getNum() {
			return num;
		}

		public void setNum(String num) {
			this.num = num;
		}

		public String getMember_name() {
			return member_name;
		}

		public void setMember_name(String member_name) {
			this.member_name = member_name;
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

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getCreated_time() {
			return created_time;
		}

		public void setCreated_time(String created_time) {
			this.created_time = created_time;
		}

		public String getExpired_time() {
			return expired_time;
		}

		public void setExpired_time(String expired_time) {
			this.expired_time = expired_time;
		}

		public String getUsed_time() {
			return used_time;
		}

		public void setUsed_time(String used_time) {
			this.used_time = used_time;
		}
	}
}
