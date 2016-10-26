package com.dudu.helper3.javabean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/1
 */
public class ProductStatus{
    
    
    
    public List<SelectorBean>  getProductStatus(){
        List<SelectorBean> datas = new ArrayList<>();
        SelectorBean DataBean6=new SelectorBean();
        DataBean6.id = 0;
        DataBean6.name =("所有");
        datas.add(DataBean6);
        SelectorBean DataBean4=new SelectorBean();
        DataBean4.id = 1;
        DataBean4.name =("未上架");
        datas.add(DataBean4);
        SelectorBean DataBean5=new SelectorBean();
        DataBean5.id = 2;
        DataBean5.name =("已上架");
        datas.add(DataBean5);
        return datas;
        
    }
    public List<SelectorBean> getProductORderBy(){
        //商品列表第一项
        List<SelectorBean> datas = new ArrayList<>();
        SelectorBean DataBean=new SelectorBean();
        DataBean.id = 1;
        DataBean.name = ("默认排序");
        datas.add(DataBean);
        SelectorBean DataBean1=new SelectorBean();
        DataBean1.id = 2;
        DataBean1.name = ("销量最低");
        datas.add(DataBean1);
        SelectorBean DataBean2=new SelectorBean();
        DataBean2.id = 3;
        DataBean2.name = ("销量最高");
        datas.add(DataBean2);
        SelectorBean DataBean3=new SelectorBean();
        DataBean3.id = 4;
        DataBean3.name = ("人气最低");
        datas.add(DataBean3);
        SelectorBean DataBean4=new SelectorBean();
        DataBean4.id = 5;
        DataBean4.name = ("人气最高");
        datas.add(DataBean4);
        return datas;
        
    }
}
