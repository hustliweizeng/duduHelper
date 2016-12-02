package com.dudu.duduhelper.javabean;

import java.io.Serializable;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/7
 */
public class DiscountDeatailBean implements  Serializable{

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
        private String status;
        private String name;
        private String category_id;
        private String thumbnail;
        private String[] pics;
        private String shop_id;
        private String agent_id;
        private String price;
        private String current_price;
        private String upshelf;
        private String downshelf;
        private String expiry;
        private String stock;
        private String amount;
        private String sold;
        private String clicked_count;
        private String validation_count;
        private String description;
        private String explain;
        private String created_at;
        private String updated_at;
        private String deleted_at;
        private List<String> apply_shops;
        private String is_vip_price;
        private  String vip_price;

        public String getIs_vip_price() {
            return is_vip_price;
        }

        public void setIs_vip_price(String is_vip_price) {
            this.is_vip_price = is_vip_price;
        }

        public String getVip_price() {
            return vip_price;
        }

        public void setVip_price(String vip_price) {
            this.vip_price = vip_price;
        }

        public List<String> getApply_shops() {
            return apply_shops;
        }

        public void setApply_shops(List<String> apply_shops) {
            this.apply_shops = apply_shops;
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

        public String getCategory_id() {
            return category_id;
        }

        public void setCategory_id(String category_id) {
            this.category_id = category_id;
        }

        public String getThumbnail() {
            return thumbnail;
        }

        public void setThumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
        }

        public Object getPics() {
            return pics;
        }

        public void setPics(String[] pics) {
            this.pics = pics;
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

        public String getUpshelf() {
            return upshelf;
        }

        public void setUpshelf(String upshelf) {
            this.upshelf = upshelf;
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

        public String getSold() {
            return sold;
        }

        public void setSold(String sold) {
            this.sold = sold;
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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
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

        public String getDeleted_at() {
            return deleted_at;
        }

        public void setDeleted_at(String deleted_at) {
            this.deleted_at = deleted_at;
        }
    }
}
