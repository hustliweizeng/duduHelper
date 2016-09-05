package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.javabean.CashHistoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwz on 2016/8/17.
 */
public class MoneyHistoryAdapter extends BaseAdapter {

    public List<CashHistoryBean.DataBean> list = new ArrayList<>();
    Context context;

    public MoneyHistoryAdapter(Context context) {
        this.context = context;
        notifyDataSetChanged();
    }

    public void addAll(List<CashHistoryBean.DataBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list!=null && list.size()!=0){
            int count = 0;
            for (CashHistoryBean.DataBean bean :list){
                //每级条目数相加
                count += bean.getOrder().size();
            }
            return  count;
        }else {
            Toast.makeText(context,"当前没有要显示的数据",Toast.LENGTH_SHORT).show();
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
        Log.d("adapter", "已经打印");
        holder = (ViewHolder) convertView.getTag();
        CashHistoryBean.DataBean dataBean = list.get(0);
        //设置数据,根据日期动态显示列表头
        //1.第三层的第一个条目显示日期
        List<CashHistoryBean.DataBean.OrderBean> orderBeen = dataBean.getOrder();
        if (position == 0){
            holder.tv_date_item_money_history.setVisibility(View.VISIBLE);
            holder.tv_date_item_money_history.setText(dataBean.getDate().substring(0,4)+"-"
                    +dataBean.getDate().substring(4,6)+"-"+dataBean.getDate().substring(6));
        }else {
            holder.tv_date_item_money_history.setVisibility(View.GONE);
        }
        //2.设置其他内容
        holder.tv_subject_money_history.setText(orderBeen.get(position).getSubject());
        holder.tv_body_money_history.setText("-"+orderBeen.get(position).getBody());
        holder.tv_price_item_money_hishory.setText("+￥"+orderBeen.get(position).getFee());
        holder.tv_time_money_history.setText(Util.TimeConVert(orderBeen.get(position).getTime()));
        return convertView;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    public static class ViewHolder {
        public TextView tv_date_item_money_history;
        public TextView tv_price_item_money_hishory;
        public TextView tv_time_money_history;
        public TextView tv_subject_money_history;
        public TextView tv_body_money_history;

    }
}
