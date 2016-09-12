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
    private int[] positions;
    //默认开始的组id为第一组
    private int groudid = 0;

    public MoneyHistoryAdapter(Context context) {
        this.context = context;
        notifyDataSetChanged();
    }

    public void addAll(List<CashHistoryBean.DataBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        positions = new int[list.size()];
        positions[0] = 0;
        if (list!=null && list.size()!=0){
            int count = 0;
            for (int i = 0; i< list.size(); i++){
                count += list.get(i).getOrder().size();
                //记录二级目录的位置
                if (i < list.size()-1){
                    positions[i+1] = count;
                }
                orders.addAll(list.get(i).getOrder());
            }
            return  count;
        }else {
            //Toast.makeText(context,"当前没有要显示的数据",Toast.LENGTH_SHORT).show();
            return 0;
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
        }
        holder = (ViewHolder) convertView.getTag();
        
        //设置数据,根据日期动态显示列表头
        //1.第三层的第一个条目显示日期
        //如果是这个组的第一条，那么显示日期出来
        //设置第一个条目if
        for (int i :positions){
            if (position == i){
                holder.tv_date_item_money_history.setVisibility(View.VISIBLE);
                holder.tv_date_item_money_history.setText(orders.get(position).getId().substring(0,4)+"-"
                        +orders.get(position).getId().substring(4,6)+"-"+orders.get(position).getId().substring(6,8));
            }else {
                holder.tv_date_item_money_history.setVisibility(View.GONE);

            }
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
