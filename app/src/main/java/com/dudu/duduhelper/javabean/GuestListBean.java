package com.dudu.duduhelper.javabean;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/10/12
 */

public class GuestListBean {


	private CountBean count;
	private List<GuestDetails> list;
	private PageInfoBean page_info;
	private String code;
	private String msg;
	
	public  class GuestDetails{
		private  String member_id;
		private  String	avatar;
		private  String	nickname; 
		private  String	total_num;
		private  String	total_consumption;
		private  String	last_consumption_time;
		private  String	subscribe;

		public String getMember_id() {
			return member_id;
		}

		public void setMember_id(String member_id) {
			this.member_id = member_id;
		}

		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public String getTotal_num() {
			return total_num;
		}

		public void setTotal_num(String total_num) {
			this.total_num = total_num;
		}

		public String getTotal_consumption() {
			return total_consumption;
		}

		public void setTotal_consumption(String total_consumption) {
			this.total_consumption = total_consumption;
		}

		public String getLast_consumption_time() {
			return last_consumption_time;
		}

		public void setLast_consumption_time(String last_consumption_time) {
			this.last_consumption_time = last_consumption_time;
		}

		public String getSubscribe() {
			return subscribe;
		}

		public void setSubscribe(String subscribe) {
			this.subscribe = subscribe;
		}
	}

	public CountBean getCount() {
		return count;
	}

	public void setCount(CountBean count) {
		this.count = count;
	}

	public List<GuestDetails> getList() {
		return list;
	}

	public void setList(List<GuestDetails> list) {
		this.list = list;
	}

	public PageInfoBean getPage_info() {
		return page_info;
	}

	public void setPage_info(PageInfoBean page_info) {
		this.page_info = page_info;
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

	public static class CountBean {
		private String now;
		private String total;

		public String getNow() {
			return now;
		}

		public void setNow(String now) {
			this.now = now;
		}

		public String getTotal() {
			return total;
		}

		public void setTotal(String total) {
			this.total = total;
		}
	}

	public static class PageInfoBean {
		private String page;
		private String size;
		private String count;
		private String count_page;

		public String getPage() {
			return page;
		}

		public void setPage(String page) {
			this.page = page;
		}

		public String getSize() {
			return size;
		}

		public void setSize(String size) {
			this.size = size;
		}

		public String getCount() {
			return count;
		}

		public void setCount(String count) {
			this.count = count;
		}

		public String getCount_page() {
			return count_page;
		}

		public void setCount_page(String count_page) {
			this.count_page = count_page;
		}
	}
}
