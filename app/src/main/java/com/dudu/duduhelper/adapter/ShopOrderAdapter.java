package com.dudu.duduhelper.adapter;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.javabean.OrderListBean;
import com.dudu.duduhelper.javabean.OrderStatusBean;
import com.dudu.duduhelper.javabean.SelectorBean;

import java.util.ArrayList;
import java.util.List;

public class ShopOrderAdapter extends BaseAdapter 
{
    private Context context;
    private ViewHolder viewHolder;
    List<OrderListBean.ListBean> list=new ArrayList<>();
	public ShopOrderAdapter(Context context)
	{
		this.context=context;
	}
	public void clear()
    {
    	list.clear();
    }
    public void addAll(List<OrderListBean.ListBean> list)
    {
    	this.list.addAll(this.list.size(), list);
    	notifyDataSetChanged();
    }
	public  String getLastId(){
		return list.get(list.size()-1).getId();
	}
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public OrderListBean.ListBean getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(list.get(list.size()-1).getId());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.shop_order_all_item, null);
			//订单号
			viewHolder.orderNumTextView=(TextView) convertView.findViewById(R.id.orderNumTextView);
			//订单状态
			viewHolder.orderActionTextView=(TextView)convertView.findViewById(R.id.orderActionTextView);
			//订单类型
			viewHolder.orderTypeTextView=(TextView)convertView.findViewById(R.id.orderTypeTextView);
			//订单时间
			viewHolder.orderTimeTextView=(TextView)convertView.findViewById(R.id.orderTimeTextView);
			//订单金额
			viewHolder.orderPriceTextView=(TextView)convertView.findViewById(R.id.orderPriceTextView);
			//订单来源
			viewHolder.orderSourceText=(TextView)convertView.findViewById(R.id.orderSourceText);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(list.size()!=0)
		{
			//设置订单号
			viewHolder.orderNumTextView.setText(list.get(position).getId());
			//设置订单状态
			int status = Integer.parseInt(list.get(position).getStatus());
			int color = R.color.text_green_color;
			String content = "";
			switch (status){
				case 0 :
					content = "已取消";
					color = R.color.text_gray_color;
					break;
				case 1 :
					content = "待支付";
					color = R.color.text_color_yellow;
				case 2 :
					content = "已支付";
					color = R.color.text_green_color;
					break;
				case 3 :
					content = "已完成";
					color = R.color.text_gray_color;
					break;
				case 4 :
					content = "待发货";
					break;
				case 5 :
					content = "待收货";
					break;
				case 6 :
					content = "待核销";
					break;
				case 7 :
					content = "待评价";
					break;
				case -1 :
					content = "已过期";
					color = R.color.text_gray_color;
					break;
				case -2 :
					content = "已退款";
					break;
			}
			//设置内容
			viewHolder.orderActionTextView.setText(content);
			//设置颜色
			viewHolder.orderActionTextView.setTextColor(viewHolder.orderActionTextView.getResources().getColor(color));
			
			//设置订单类型
			viewHolder.orderTypeTextView.setText(list.get(position).getSubject());
			//设置订单时间
			viewHolder.orderTimeTextView.setText(Util.DataConVertMint(list.get(position).getTime()));
			//设置价格
			viewHolder.orderPriceTextView.setText("￥"+list.get(position).getFee());
			//设置来源
			//遍历集合
			for(SelectorBean bean: (new OrderStatusBean().getAllOrderSource())){
				if (bean.id == Integer.parseInt(list.get(position).getFrom())){
					viewHolder.orderSourceText.setText(bean.name);
				}
			}
			Log.i("info", String.valueOf(position));
		}
		return convertView;
	}
	private class ViewHolder
	{
		TextView orderSourceText;
		TextView orderNumTextView;
		TextView orderActionTextView;
		TextView orderTypeTextView;
		TextView orderTimeTextView;
		TextView orderPriceTextView;
	}

}
