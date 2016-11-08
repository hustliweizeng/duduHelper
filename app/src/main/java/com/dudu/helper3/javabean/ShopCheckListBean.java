package com.dudu.helper3.javabean;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/11/8
 */

public class ShopCheckListBean {

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
		private String id;
		private String name;
		private String logo;
		private String trade_history;
		private String trade_month;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getLogo() {
			return logo;
		}

		public void setLogo(String logo) {
			this.logo = logo;
		}

		public String getTrade_history() {
			return trade_history;
		}

		public void setTrade_history(String trade_history) {
			this.trade_history = trade_history;
		}

		public String getTrade_month() {
			return trade_month;
		}

		public void setTrade_month(String trade_month) {
			this.trade_month = trade_month;
		}
	}
}
