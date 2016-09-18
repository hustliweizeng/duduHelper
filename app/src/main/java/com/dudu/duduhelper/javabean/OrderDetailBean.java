package com.dudu.duduhelper.javabean;

import com.dudu.duduhelper.bean.OrderGoods;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/5
 */
public class OrderDetailBean {
    private DataBean data;
    private String code;
    private String msg;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
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

    public static class DataBean {
        private String id;
        private String subject;
        private String body;
        private String total_fee;
        private String fee;
        private String shop_fee;
        private String discount_agent_fee;
        private String discount_shop_fee;
        private String discount_activity_fee;
        private String from;
        private String ispay;
        private String status;
        private String thumb;
        private String time;
        private String name;
        private String mobile;
        private String address;
        private List<OrderGoods> goods;

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

        public String getBody() {
            return body;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public String getTotal_fee() {
            return total_fee;
        }

        public void setTotal_fee(String total_fee) {
            this.total_fee = total_fee;
        }

        public String getFee() {
            return fee;
        }

        public void setFee(String fee) {
            this.fee = fee;
        }

        public String getShop_fee() {
            return shop_fee;
        }

        public void setShop_fee(String shop_fee) {
            this.shop_fee = shop_fee;
        }

        public String getDiscount_agent_fee() {
            return discount_agent_fee;
        }

        public void setDiscount_agent_fee(String discount_agent_fee) {
            this.discount_agent_fee = discount_agent_fee;
        }

        public String getDiscount_shop_fee() {
            return discount_shop_fee;
        }

        public void setDiscount_shop_fee(String discount_shop_fee) {
            this.discount_shop_fee = discount_shop_fee;
        }

        public String getDiscount_activity_fee() {
            return discount_activity_fee;
        }

        public void setDiscount_activity_fee(String discount_activity_fee) {
            this.discount_activity_fee = discount_activity_fee;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getIspay() {
            return ispay;
        }

        public void setIspay(String ispay) {
            this.ispay = ispay;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getThumb() {
            return thumb;
        }

        public void setThumb(String thumb) {
            this.thumb = thumb;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
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

        public List<OrderGoods> getGoods() {
            return goods;
        }

        public void setGoods(List<OrderGoods> goods) {
            this.goods = goods;
        }
    }
}



