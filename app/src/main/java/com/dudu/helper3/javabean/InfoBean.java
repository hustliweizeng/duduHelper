package com.dudu.helper3.javabean;

/**
 * @author
 * @version 1.0
 * @date 2016/9/8
 */
public class InfoBean {

    private String code;
    private String msg;
    private ShopBean shop;
    private TodaystatBean todaystat;
    private TotalstatBean totalstat;
    private UserBean user;

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

    public ShopBean getShop() {
        return shop;
    }

    public void setShop(ShopBean shop) {
        this.shop = shop;
    }

    public TodaystatBean getTodaystat() {
        return todaystat;
    }

    public void setTodaystat(TodaystatBean todaystat) {
        this.todaystat = todaystat;
    }

    public TotalstatBean getTotalstat() {
        return totalstat;
    }

    public void setTotalstat(TotalstatBean totalstat) {
        this.totalstat = totalstat;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class ShopBean {
        private String id;
        private String logo;
        private String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public static class TodaystatBean {
        private String buyer;
        private String income;
        private String order;
        private String trade;
        private String visitor;

        public String getBuyer() {
            return buyer;
        }

        public void setBuyer(String buyer) {
            this.buyer = buyer;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public String getTrade() {
            return trade;
        }

        public void setTrade(String trade) {
            this.trade = trade;
        }

        public String getVisitor() {
            return visitor;
        }

        public void setVisitor(String visitor) {
            this.visitor = visitor;
        }
    }

    public static class TotalstatBean {
        private String buyer;
        private String freezemoney;
        private String income;
        private String order;
        private String outmoney;
        private String trade;
        private String usablemoney;
        private String visitor;

        public String getUnverificationmoney() {
            return unverificationmoney;
        }

        public void setUnverificationmoney(String unverificationmoney) {
            this.unverificationmoney = unverificationmoney;
        }

        private String unverificationmoney;

        public String getBuyer() {
            return buyer;
        }

        public void setBuyer(String buyer) {
            this.buyer = buyer;
        }

        public String getFreezemoney() {
            return freezemoney;
        }

        public void setFreezemoney(String freezemoney) {
            this.freezemoney = freezemoney;
        }

        public String getIncome() {
            return income;
        }

        public void setIncome(String income) {
            this.income = income;
        }

        public String getOrder() {
            return order;
        }

        public void setOrder(String order) {
            this.order = order;
        }

        public String getOutmoney() {
            return outmoney;
        }

        public void setOutmoney(String outmoney) {
            this.outmoney = outmoney;
        }

        public String getTrade() {
            return trade;
        }

        public void setTrade(String trade) {
            this.trade = trade;
        }

        public String getUsablemoney() {
            return usablemoney;
        }

        public void setUsablemoney(String usablemoney) {
            this.usablemoney = usablemoney;
        }

        public String getVisitor() {
            return visitor;
        }

        public void setVisitor(String visitor) {
            this.visitor = visitor;
        }
    }

    public static class UserBean {
        private String mobile;
        private String name;
        private String nickname;
        private String isshopuser;

        public String getIsshopuser() {
            return isshopuser;
        }

        public void setIsshopuser(String isshopuser) {
            this.isshopuser = isshopuser;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }
}
