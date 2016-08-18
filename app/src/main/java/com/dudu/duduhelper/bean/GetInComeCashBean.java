package com.dudu.duduhelper.bean;

public class GetInComeCashBean 
{
	/**
	 * id : 20160818164435591
	 * qrcode : http://business.dev.duduapp.net/tool/qrcode?text=weixin%3A%2F%2Fwxpay%2Fbizpayurl%3Fpr%3Dz5Bj3PF
	 * time_create : 1471509875
	 * url : weixin://wxpay/bizpayurl?pr=z5Bj3PF
	 */

	private DataBean data;
	/**
	 * data : {"id":"20160818164435591","qrcode":"http://business.dev.duduapp.net/tool/qrcode?text=weixin%3A%2F%2Fwxpay%2Fbizpayurl%3Fpr%3Dz5Bj3PF","time_create":"1471509875","url":"weixin://wxpay/bizpayurl?pr=z5Bj3PF"}
	 * info : success
	 * status : 1
	 */

	private String info;
	private int status;

	public DataBean getData() {
		return data;
	}

	public void setData(DataBean data) {
		this.data = data;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public static class DataBean {
		private String id;
		private String qrcode;
		private String time_create;
		private String url;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getQrcode() {
			return qrcode;
		}

		public void setQrcode(String qrcode) {
			this.qrcode = qrcode;
		}

		public String getTime_create() {
			return time_create;
		}

		public void setTime_create(String time_create) {
			this.time_create = time_create;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}
	//旧版接口
	/*private String status;
	private String info;
	private GetInComeCashDataBean data;
	
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public GetInComeCashDataBean getData() {
		return data;
	}
	public void setData(GetInComeCashDataBean data) {
		this.data = data;
	}*/
	

}
