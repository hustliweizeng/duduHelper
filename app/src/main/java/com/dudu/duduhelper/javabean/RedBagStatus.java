package com.dudu.duduhelper.javabean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/1
 */
public class RedBagStatus {
    
    public List<SelectorBean> getRedBagStatus(){
        List<SelectorBean> datas = new ArrayList<>();
        SelectorBean data6=new SelectorBean();
       // data6.id = ("all");
        data6.name = ("所有");
        datas.add(data6);
        SelectorBean data4=new SelectorBean();
       // data4.id = ("ended");
        data4.name = ("已截止");
        datas.add(data4);
        SelectorBean data5=new SelectorBean();
       // data5.id = ("releasing");
        data5.name = ("发放中");
        datas.add(data5);
        
        return datas;
    }

    public List<SelectorBean> getRedBagOrderby(){
        List<SelectorBean> datas = new ArrayList<>();
        SelectorBean data=new SelectorBean();
       // data.id = ("getmore");
        data.name = ("默认排序");
        datas.add(data);
        SelectorBean data1=new SelectorBean();
       // data1.id = ("getmore");
        data1.name = ("领取最多");
        datas.add(data1);
        SelectorBean data2=new SelectorBean();
       // data2.id = ("getless");
        data2.name = ("领取最少");
        datas.add(data2);
        SelectorBean data3=new SelectorBean();
        //data3.id = ("moremoney");
        data3.name = ("金额最多");
        datas.add(data3);
        SelectorBean data4=new SelectorBean();
        //data4.id = ("lessmoney");
        data4.name = ("金额最少");
        datas.add(data4);
        return datas;
    }
}
