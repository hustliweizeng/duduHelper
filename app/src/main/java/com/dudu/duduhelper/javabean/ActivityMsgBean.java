package com.dudu.duduhelper.javabean;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/10/13
 */

public class ActivityMsgBean {


	private PageInfoBean page_info;
	private String code;
	private String msg;
	private List<ListBean> list;

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

	public List<ListBean> getList() {
		return list;
	}

	public void setList(List<ListBean> list) {
		this.list = list;
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

	public static class ListBean {
		private String status;
		private String created_at;
		private String send_num;
		private String succeed_num;
		private String title;
		private String desc;

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getCreated_at() {
			return created_at;
		}

		public void setCreated_at(String created_at) {
			this.created_at = created_at;
		}

		public String getSend_num() {
			return send_num;
		}

		public void setSend_num(String send_num) {
			this.send_num = send_num;
		}

		public String getSucceed_num() {
			return succeed_num;
		}

		public void setSucceed_num(String succeed_num) {
			this.succeed_num = succeed_num;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}
}
