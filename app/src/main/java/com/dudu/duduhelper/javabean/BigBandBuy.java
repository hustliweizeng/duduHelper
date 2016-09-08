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
        private String name;
        private String thumbnail;
        private String[] show_img;
        private String shop_id;
        private String price;
        private String current_price;
        private String rule;
        private String stock;
        private String amount;
        private String saled_count;
        private String validation_count;
        private String explain;
        private String upshelf;
        private String downshelf;

        public String getSold() {
            return sold;
        }

        public void setSold(String sold) {
            this.sold = sold;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String[] getPics() {
            return pics;
        }

        public void setPics(String[] pics) {
            this.pics = pics;
        }

        public String getAgent_id() {
            return agent_id;
        }

        public void setAgent_id(String agent_id) {
            this.agent_id = agent_id;
        }

        public String getExpiry() {
            return expiry;
        }

        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }

        private String sold;
        private String category_id;
        private String[] pics;
        private String agent_id;
        private String expiry;

        public String getDownshelf() {
            return downshelf;
        }

        public void setDownshelf(String downshelf) {
            this.downshelf = downshelf;
        }

        public String getUpshelf() {
            return upshelf;
        }

        public void setUpshelf(String upshelf) {
            this.upshelf = upshelf;
        }

        

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

        public String[] getShow_img() {
            return show_img;
        }

        public void setShow_img(String[] show_img) {
            this.show_img = show_img;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
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

        public String getValidation_count() {
            return validation_count;
        }

        public void setValidation_count(String validation_count) {
            this.validation_count = validation_count;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }
    }
}
