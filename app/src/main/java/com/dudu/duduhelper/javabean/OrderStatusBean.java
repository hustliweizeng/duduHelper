package com.dudu.duduhelper.javabean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwz on 2016/8/30.
 */
public class OrderStatusBean {
    public   class  OrderStatus{

        public int id;
        public String name;
    }
    public   class  OrderSource{

        public int id;
        public String name;
    }
    /*"0": "已取消",
            "1": "待支付",
            "2": "已支付",
            "3": "已完成",
            "4": "待发货",
            "5": "待收货",
            "6": "待核销",
            "7": "待评价",
            "-1": "已过期",
            "-2": "已退款"
            */
    //返回订单状态列表
    public   List<OrderStatus>  getAllOrderStatus(){
        OrderStatus status1 = new OrderStatus();
        status1.id = 1;
        status1.name = "待支付";
        OrderStatus status2 = new OrderStatus();
        status2.id = 2;
        status2.name = "已支付";
        OrderStatus status3 = new OrderStatus();
        status3.id = 3;
        status3.name = "已完成";
        OrderStatus status4 = new OrderStatus();
        status4.id = 4;
        status4.name = "待发货";
        OrderStatus status5 = new OrderStatus();
        status5.id = 5;
        status5.name = "待收货";
        OrderStatus status6 = new OrderStatus();
        status6.id = 6;
        status6.name = "待核销";
        OrderStatus status7 = new OrderStatus();
        status7.id = 7;
        status7.name = "待评价";
        OrderStatus status8 = new OrderStatus();
        status8.id = -1;
        status8.name = "已过期";
        OrderStatus status9 = new OrderStatus();
        status8.id = -2;
        status8.name = "已退款";
        List<OrderStatus> list = new ArrayList<>();
        list.add(status1);
        list.add(status2);
        list.add(status3);
        list.add(status4);
        list.add(status5);
        list.add(status6);
        list.add(status7);
        list.add(status8);
        list.add(status9);
        return  list;
    }
    //返回订单来源列表
    /*"1": "大牌抢购",
            "6": "商家收款",
            "7": "优惠券",
            "8": "五折卡",
            "9": "刷卡支付",
            "10": "扫码支付",
            "11": "周边活动"*/
    public   List<OrderSource>  getAllOrderSource(){
        OrderSource source1 = new OrderSource();
        source1.id = 1;
        source1.name = "大牌抢购";
        OrderSource source2 = new OrderSource();
        source2.id = 6;
        source2.name = "商家收款";
        OrderSource source3 = new OrderSource();
        source3.id = 7;
        source3.name = "优惠券";
        OrderSource source4 = new OrderSource();
        source4.id = 8;
        source4.name = "五折卡";
        OrderSource source5 = new OrderSource();
        source5.id = 9;
        source5.name = "刷卡支付";
        OrderSource source6 = new OrderSource();
        source6.id = 10;
        source6.name = "扫码支付";
        OrderSource source7 = new OrderSource();
        source6.id = 11;
        source6.name = "周边活动";

        ArrayList<OrderSource> list = new ArrayList<>();
        list.add(source1);
        list.add(source2);
        list.add(source3);
        list.add(source4);
        list.add(source5);
        list.add(source6);
        list.add(source7);
        return list;
    }


}
