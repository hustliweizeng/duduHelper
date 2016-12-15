package com.dudu.duduhelper.javabean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwz on 2016/8/30.
 */
public class OrderStatusBean {

    public   class SelectorBeanStatus extends SelectorBean {
    }
    public   class SelectorBeanSource extends SelectorBean {
    }
    public  class SelectorBeanType extends SelectorBean {
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
    public   List<SelectorBean>  getAllOrderStatus(){
        SelectorBeanStatus status0 = new SelectorBeanStatus();
        status0.id = -100;
        status0.name = "全部";
        SelectorBeanStatus status1 = new SelectorBeanStatus();
        status1.id = 1;
        status1.name = "待支付";
        //SelectorBeanStatus status2 = new SelectorBeanStatus();
//        status2.id = 2;
//        status2.name = "已支付";
        SelectorBeanStatus status3 = new SelectorBeanStatus();
        status3.id = 3;
        status3.name = "已完成";
        SelectorBeanStatus status4 = new SelectorBeanStatus();
        status4.id = 4;
        status4.name = "待发货";
        SelectorBeanStatus status5 = new SelectorBeanStatus();
        status5.id = 5;
        status5.name = "待收货";
        SelectorBeanStatus status6 = new SelectorBeanStatus();
        status6.id = 6;
        status6.name = "待核销";
        SelectorBeanStatus status7 = new SelectorBeanStatus();
        status7.id = 7;
        status7.name = "待评价";
        SelectorBeanStatus status8 = new SelectorBeanStatus();
        status8.id = -1;
        status8.name = "已过期";
        SelectorBeanStatus status9 = new SelectorBeanStatus();
        status9.id = -2;
        status9.name = "已退款";
        SelectorBeanStatus status10 = new SelectorBeanStatus();
        status10.id = -3;
        status10.name = "已取消";
        List<SelectorBean> list = new ArrayList<>();
       list.add(status0);
        list.add(status1);
        //list.add(status2);
        list.add(status3);
        list.add(status4);
        list.add(status5);
        list.add(status6);
        list.add(status7);
        list.add(status10);
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
    public   List<SelectorBean>  getAllOrderSource(){
        SelectorBeanSource source0 = new SelectorBeanSource();
        source0.id = -100;
        source0.name = "全部";
        SelectorBeanSource source1 = new SelectorBeanSource();
        source1.id = 1;
        source1.name = "大牌抢购";
        SelectorBeanSource source2 = new SelectorBeanSource();
        source2.id = 6;
        source2.name = "收银台";
        SelectorBeanSource source3 = new SelectorBeanSource();
        source3.id = 7;
        source3.name = "优惠券";
        SelectorBeanSource source4 = new SelectorBeanSource();
        source4.id = 8;
        source4.name = "五折卡";
        SelectorBeanSource source5 = new SelectorBeanSource();
        source5.id = 9;
        source5.name = "刷卡支付";
        SelectorBeanSource source6 = new SelectorBeanSource();
        source6.id = 10;
        source6.name = "扫码支付";
        SelectorBeanSource source7 = new SelectorBeanSource();
        source7.id = 11;
        source7.name = "周边活动";

        ArrayList<SelectorBean> list = new ArrayList<>();
        list.add(source0);
        list.add(source2);
        list.add(source5);
        list.add(source6);
        list.add(source1);
        list.add(source3);
        //list.add(source4);
       // list.add(source7);
        return list;
    }
    public List<SelectorBean> getAllOrderType(){
        SelectorBeanType type0 = new SelectorBeanType();
        type0.id = -100;
        type0.name = "全部";
        SelectorBeanType type1 = new SelectorBeanType();
        type1.id = 1;
        type1.name = "已支付";
        SelectorBeanType type2 = new SelectorBeanType();
        type2.id = 0;
        type2.name = "未支付";
        ArrayList<SelectorBean> list = new ArrayList<>();
        list.add(type0);
        list.add(type1);
        list.add(type2);
        return list;
    }


}
