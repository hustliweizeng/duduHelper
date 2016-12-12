package com.dudu.duduhelper.javabean;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/27
 */

public class RedBagListBean implements  Serializable{


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
		private String time_start;
		private String time_end;
		private String send_num;

		public String getSend_num() {
			return send_num;
		}

		public void setSend_num(String send_num) {
			this.send_num = send_num;
		}

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
	}
}
