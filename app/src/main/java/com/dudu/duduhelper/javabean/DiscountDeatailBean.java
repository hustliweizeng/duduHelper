package com.dudu.duduhelper.javabean;

import java.io.Serializable;

/**
 * @author
 * @version 1.0
 * @date 2016/9/7
 */
public class DiscountDeatailBean implements  Serializable{

    private  String code ;
    private  String msg;
    private Data data;
    public static  class Data implements Serializable{
        public String[] pics;
        public String agent_id;
        public String amout;
        public String category_id;
        public String created_at;
        public String current_price;
        public String deleted_at;
        public String downshelf;
        public String expiry;
        public String explain;
        public String id;
        public String name;
        public String price;
        public String shop_id;
        public String sold;
        public String status;
        public String stock;
        public String thumbnail;
        public String upshelf;
        public String validation_count;

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

        public String getAmout() {
            return amout;
        }

        public void setAmout(String amout) {
            this.amout = amout;
        }

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getCurrent_price() {
            return current_price;
        }

        public void setCurrent_price(String current_price) {
            this.current_price = current_price;
        }

        public String getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(String deleted_at) {
            this.deleted_at = deleted_at;
        }

        public String getDownshelf() {
            return downshelf;
        }

        public void setDownshelf(String downshelf) {
            this.downshelf = downshelf;
        }

        public String getExpiry() {
            return expiry;
        }

        public void setExpiry(String expiry) {
            this.expiry = expiry;
        }

        public String getExplain() {
            return explain;
        }

        public void setExplain(String explain) {
            this.explain = explain;
        }

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

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getShop_id() {
            return shop_id;
        }

        public void setShop_id(String shop_id) {
            this.shop_id = shop_id;
        }

        public String getSold() {
            return sold;
        }

        public void setSold(String sold) {
            this.sold = sold;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStock() {
            return stock;
        }

        public void setStock(String stock) {
            this.stock = stock;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public String getUpshelf() {
            return upshelf;
        }

        public void setUpshelf(String upshelf) {
            this.upshelf = upshelf;
        }

        public String getValidation_count() {
            return validation_count;
        }

        public void setValidation_count(String validation_count) {
            this.validation_count = validation_count;
        }
        
    }
    public String getMsg() {
        return msg;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public void setMsg(String msg) {
        this.msg = msg;

    }
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
