package com.dudu.duduhelper.javabean;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/29
 */

public class SummoryDetaiBean {


	private String daynum;
	private String trade;
	private String income;
	private String visiter;
	private String buyer;
	private String order;
	private String code;
	private String msg;
	private List<TradeModulesBean> trade_modules;

	public String getDaynum() {
		return daynum;
	}

	public void setDaynum(String daynum) {
		this.daynum = daynum;
	}

	public String getTrade() {
		return trade;
	}

	public void setTrade(String trade) {
		this.trade = trade;
	}

	public String getIncome() {
		return income;
	}

	public void setIncome(String income) {
		this.income = income;
	}

	public String getVisiter() {
		return visiter;
	}

	public void setVisiter(String visiter) {
		this.visiter = visiter;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
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

	public List<TradeModulesBean> getTrade_modules() {
		return trade_modules;
	}

	public void setTrade_modules(List<TradeModulesBean> trade_modules) {
		this.trade_modules = trade_modules;
	}

	public static class TradeModulesBean {
		private String module_id;
		private String num;
		private String fee;

		public String getModule_id() {
			return module_id;
		}

		public void setModule_id(String module_id) {
			this.module_id = module_id;
		}

		public String getNum() {
			return num;
		}

		public void setNum(String num) {
			this.num = num;
		}

		public String getFee() {
			return fee;
		}

		public void setFee(String fee) {
			this.fee = fee;
		}
	}
}
