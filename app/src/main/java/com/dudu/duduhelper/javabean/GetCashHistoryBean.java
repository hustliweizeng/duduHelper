package com.dudu.duduhelper.javabean;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/10/28
 */

public class GetCashHistoryBean {

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
		private String money;
		private String time;
		private Object agent_time;
		private String status;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getMoney() {
			return money;
		}

		public void setMoney(String money) {
			this.money = money;
		}

		public String getTime() {
			return time;
		}

		public void setTime(String time) {
			this.time = time;
		}

		public Object getAgent_time() {
			return agent_time;
		}

		public void setAgent_time(Object agent_time) {
			this.agent_time = agent_time;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}
	}
}
