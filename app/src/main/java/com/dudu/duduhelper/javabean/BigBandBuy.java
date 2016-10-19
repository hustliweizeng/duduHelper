package com.dudu.duduhelper.javabean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lwz on 2016/8/31.
 */
public class BigBandBuy implements Serializable{

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

    public static class DataBean implements  Serializable{
        private String id;
        private String status;
        private String is_on_sale;
        private String invitation;
        private String name;
        private String thumbnail;
        private Object show_img;
        private String delivery;
        private Object address;
        private String shop_id;
        private String agent_id;
        private String price;
        private String current_price;
        private String rule;
        private String stock;
        private String amount;
        private String saled_count;
        private String clicked_count;
        private String validation_count;
        private String views;
        private String explain;
        private String created_at;
        private String updated_at;
        private Object deleted_at;
        private List<String> apply_shops;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getIs_on_sale() {
            return is_on_sale;
        }

        public void setIs_on_sale(String is_on_sale) {
            this.is_on_sale = is_on_sale;
        }

        public String getInvitation() {
            return invitation;
        }

        public void setInvitation(String invitation) {
            this.invitation = invitation;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public Object getShow_img() {
            return show_img;
        }

        public void setShow_img(Object show_img) {
            this.show_img = show_img;
        }

        public String getDelivery() {
            return delivery;
        }

        public void setDelivery(String delivery) {
            this.delivery = delivery;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getAgent_id() {
            return agent_id;
        }

        public void setAgent_id(String agent_id) {
            this.agent_id = agent_id;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(String current_price) {
            this.current_price = current_price;
        }

        public String getRule() {
            return rule;
        }

        public void setRule(String rule) {
            this.rule = rule;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getSaled_count() {
            return saled_count;
        }

        public void setSaled_count(String saled_count) {
            this.saled_count = saled_count;
        }

        public String getClicked_count() {
            return clicked_count;
        }

        public void setClicked_count(String clicked_count) {
            this.clicked_count = clicked_count;
        }

        public String getValidation_count() {
            return validation_count;
        }

        public void setValidation_count(String validation_count) {
            this.validation_count = validation_count;
        }

        public String getViews() {
            return views;
        }

        public void setViews(String views) {
            this.views = views;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public Object getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(Object deleted_at) {
            this.deleted_at = deleted_at;
        }

        public List<String> getApply_shops() {
            return apply_shops;
        }

        public void setApply_shops(List<String> apply_shops) {
            this.apply_shops = apply_shops;
        }
    }
}
