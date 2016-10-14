package com.dudu.duduhelper.javabean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/10/13
 */

public class RedbagMsgListBean implements Serializable{


	private String total_red_packet;
	private String total_promote_consumer;
	private PageInfoBean page_info;
	private String code;
	private String msg;
	private ArrayList<ListBean> list;

	public String getTotal_red_packet() {
		return total_red_packet;
	}

	public void setTotal_red_packet(String total_red_packet) {
		this.total_red_packet = total_red_packet;
	}

	public String getTotal_promote_consumer() {
		return total_promote_consumer;
	}

	public void setTotal_promote_consumer(String total_promote_consumer) {
		this.total_promote_consumer = total_promote_consumer;
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

	public ArrayList<ListBean> getList() {
		return list;
	}

	public void setList(ArrayList<ListBean> list) {
		this.list = list;
	}

	public static class PageInfoBean implements  Serializable{
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

	public static class ListBean implements  Serializable {
		private String status;
		private String created_at;
		private String send_num;
		private String succeed_num;
		private String used_num;
		private String promote_consumer;
		private String red_packet_id;
		private String end_time;
		private RedPacketInfoBean red_packet_info;

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

		public String getUsed_num() {
			return used_num;
		}

		public void setUsed_num(String used_num) {
			this.used_num = used_num;
		}

		public String getPromote_consumer() {
			return promote_consumer;
		}

		public void setPromote_consumer(String promote_consumer) {
			this.promote_consumer = promote_consumer;
		}

		public String getRed_packet_id() {
			return red_packet_id;
		}

		public void setRed_packet_id(String red_packet_id) {
			this.red_packet_id = red_packet_id;
		}

		public String getEnd_time() {
			return end_time;
		}

		public void setEnd_time(String end_time) {
			this.end_time = end_time;
		}

		public RedPacketInfoBean getRed_packet_info() {
			return red_packet_info;
		}

		public void setRed_packet_info(RedPacketInfoBean red_packet_info) {
			this.red_packet_info = red_packet_info;
		}

		public static class RedPacketInfoBean implements  Serializable{
			private String money;
			private String limit;
			private String life;

			public String getMoney() {
				return money;
			}

			public void setMoney(String money) {
				this.money = money;
			}

			public String getLimit() {
				return limit;
			}

			public void setLimit(String limit) {
				this.limit = limit;
			}

			public String getLife() {
				return life;
			}

			public void setLife(String life) {
				this.life = life;
			}
		}
	}
}
