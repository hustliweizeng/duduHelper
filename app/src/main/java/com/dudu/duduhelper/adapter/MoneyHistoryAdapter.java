package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.javabean.CashHistoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwz on 2016/8/17.
 */
public class MoneyHistoryAdapter extends BaseAdapter {

    //统计带标题的位置
    public List<CashHistoryBean.DataBean> list = new ArrayList<>();
    //所有的集合列表
    public List<CashHistoryBean.DataBean.OrderBean> orders = new ArrayList<>();
    Context context;
    List<String> dates = new ArrayList<>();

    public MoneyHistoryAdapter(Context context) {
        this.context = context;
        notifyDataSetChanged();
    }

    public void addAll(List<CashHistoryBean.DataBean> list) {
        if (list!=null){
            this.list = list;
        }else {
            Toast.makeText(context,"当前没有要显示的数据",Toast.LENGTH_SHORT).show();
        }
        //重置日期
        dates.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        if (list ==null ||list.size() ==0){
            return 0;
        }else {
            int count = 0;
            for (int i = 0; i< list.size(); i++){
                count += list.get(i).getOrder().size();
                orders.addAll(list.get(i).getOrder());
            }
            return  count;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_money_history, null);
            holder = new ViewHolder();
            holder.tv_date_item_money_history = (TextView) convertView.findViewById(R.id.tv_date_item_money_history);
            holder.tv_subject_money_history = (TextView) convertView.findViewById(R.id.tv_subject_money_history);
            holder.tv_body_money_history = (TextView) convertView.findViewById(R.id.tv_body_money_history);
            holder.tv_time_money_history = (TextView) convertView.findViewById(R.id.tv_time_money_history);
            holder.tv_price_item_money_hishory = (TextView) convertView.findViewById(R.id.tv_price_item_money_hishory);
            convertView.setTag(holder);
            LogUtil.d("item","新建了");

        }{
            holder = (ViewHolder) convertView.getTag();
            LogUtil.d("item","复用了");
        }
        
        //如果当前时间戳没有被显示过，那么显示出来，否则隐藏
        //1.获取当前条目的日期
        String tempDate  = orders.get(position).getId().substring(0,4)+"-"
                +orders.get(position).getId().substring(4,6)+"-"+orders.get(position).getId().substring(6,8);
        //2.当前日期和上个条目的日期做对比，如果重复则不显示
        if (dates.contains(tempDate)){
            holder.tv_date_item_money_history.setText(tempDate);
            holder.tv_date_item_money_history.setVisibility(View.GONE);
            //3.如果日期和上个条目日期不一致，那么重置该日期并显示出来    
        }else {
            //如果集合中不包括该日期，加进去
            dates.add(tempDate);
            holder.tv_date_item_money_history.setText(tempDate);
            holder.tv_date_item_money_history.setVisibility(View.VISIBLE);
        }
        if (position ==0){
            holder.tv_date_item_money_history.setText(tempDate);
            holder.tv_date_item_money_history.setVisibility(View.VISIBLE);
        }

        //2.设置其他内容（positoin是绝对位置，但是这里需要在集合中的相对位置）=转换为相对位置
        holder.tv_subject_money_history.setText(orders.get(position).getSubject());
        holder.tv_body_money_history.setText("-"+orders.get(position).getBody());
        holder.tv_price_item_money_hishory.setText("+￥"+orders.get(position).getFee());
        holder.tv_time_money_history.setText(Util.TimeConVert(orders.get(position).getTime()));
        return convertView;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public CashHistoryBean.DataBean.OrderBean getItem(int i) {
        return orders.get(i);
    }

    public static class ViewHolder {
        public TextView tv_date_item_money_history;
        public TextView tv_price_item_money_hishory;
        public TextView tv_time_money_history;
        public TextView tv_subject_money_history;
        public TextView tv_body_money_history;

    }
}
