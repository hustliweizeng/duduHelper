package com.dudu.duduhelper.javabean;

import java.util.List;

/**
 * Created by lwz on 2016/8/29.
 */
/*[
        {"code" :"SUCCESS"
        "msg" :"OK"
        "data" : [{
        "id" : "银行卡ID 修改时和提现时用"
        "name" : "账户姓名"
        "bank_key" : "银行名称"
        "bank_name" : "支行信息"
        "card_number" : "卡号"
        "province_id" : "省份ID"
        "city_id" : "城市ID"
        }]
        }*/
public class BankCardListBean {

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

    public static class DataBean {
        private String id;
        private String name;
        private String bank_key;
        private String bank_name;
        private String card_number;
        private String province_id;
        private String city_id;

        @Override
        public String toString() {
            return "DataBean{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    ", bank_key='" + bank_key + '\'' +
                    ", bank_name='" + bank_name + '\'' +
                    ", card_number='" + card_number + '\'' +
                    ", province_id='" + province_id + '\'' +
                    ", city_id='" + city_id + '\'' +
                    '}';
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

        public String getBank_key() {
            return bank_key;
        }

        public void setBank_key(String bank_key) {
            this.bank_key = bank_key;
        }

        public String getBank_name() {
            return bank_name;
        }

        public void setBank_name(String bank_name) {
            this.bank_name = bank_name;
        }

        public String getCard_number() {
            return card_number;
        }

        public void setCard_number(String card_number) {
            this.card_number = card_number;
        }

        public String getProvince_id() {
            return province_id;
        }

        public void setProvince_id(String province_id) {
            this.province_id = province_id;
        }

        public String getCity_id() {
            return city_id;
        }

        public void setCity_id(String city_id) {
            this.city_id = city_id;
        }
    }

    @Override
    public String toString() {
        return "BankCardListBean{" +
                "code='" + code + '\'' +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
