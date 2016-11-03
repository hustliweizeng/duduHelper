package com.dudu.helper3.javabean;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/11/2
 */

public class CheckSaleHistoryBean {

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
		private String date;
		private List<OrderBean> order;

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public List<OrderBean> getOrder() {
			return order;
		}

		public void setOrder(List<OrderBean> order) {
			this.order = order;
		}

		public static class OrderBean {
			private String id;
			private String code;
			private String used_time;
			private String thumb;
			private String subject;

			public String getId() {
				return id;
			}

			public void setId(String id) {
				this.id = id;
			}

			public String getCode() {
				return code;
			}

			public void setCode(String code) {
				this.code = code;
			}

			public String getUsed_time() {
				return used_time;
			}

			public void setUsed_time(String used_time) {
				this.used_time = used_time;
			}

			public String getThumb() {
				return thumb;
			}

			public void setThumb(String thumb) {
				this.thumb = thumb;
			}

			public String getSubject() {
				return subject;
			}

			public void setSubject(String subject) {
				this.subject = subject;
			}
		}
	}
}
