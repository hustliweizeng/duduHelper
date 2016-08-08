package com.dudu.duduhelper.adapter;


import java.util.ArrayList;
import java.util.List;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.bean.OrderBean;
import com.dudu.duduhelper.bean.OrderDataBean;
import com.dudu.duduhelper.bean.ProductListBean;
import com.dudu.duduhelper.common.Util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopOrderAdapter extends BaseAdapter 
{
    private Context context;
    private ViewHolder viewHolder;
    List<OrderDataBean> list=new ArrayList<OrderDataBean>();
	public ShopOrderAdapter(Context context)
	{
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	public void clear()
    {
    	list.clear();
    }
    public void addAll(List<OrderDataBean> list)
    {
    	this.list.addAll(this.list.size(), list);
    	notifyDataSetChanged();
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public OrderDataBean getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		if(convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.shop_order_all_item, null);
			viewHolder.orderNumTextView=(TextView) convertView.findViewById(R.id.orderNumTextView);
			viewHolder.orderActionTextView=(TextView)convertView.findViewById(R.id.orderActionTextView);
			viewHolder.orderTypeTextView=(TextView)convertView.findViewById(R.id.orderTypeTextView);
			viewHolder.orderTimeTextView=(TextView)convertView.findViewById(R.id.orderTimeTextView);
//			viewHolder.orderNameTextView=(TextView)convertView.findViewById(R.id.orderNameTextView);
//			viewHolder.orderPhoneTextView=(TextView)convertView.findViewById(R.id.orderPhoneTextView);
			viewHolder.orderPriceTextView=(TextView)convertView.findViewById(R.id.orderPriceTextView);
			viewHolder.orderSourceText=(TextView)convertView.findViewById(R.id.orderSourceText);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(list.size()!=0)
		{
			viewHolder.orderNumTextView.setText(list.get(position).getNo());
			if(list.get(position).getStatus().equals("-1"))
			{
				viewHolder.orderActionTextView.setText("已过期");
				viewHolder.orderActionTextView.setTextColor(viewHolder.orderActionTextView.getResources().getColor(R.color.text_gray_color));
			}
			if(list.get(position).getStatus().equals("0"))
			{
				viewHolder.orderActionTextView.setText("已取消");
				viewHolder.orderActionTextView.setTextColor(viewHolder.orderActionTextView.getResources().getColor(R.color.text_gray_color));
			}
			if(list.get(position).getStatus().equals("1"))
			{
				viewHolder.orderActionTextView.setText("未支付");
				viewHolder.orderActionTextView.setTextColor(viewHolder.orderActionTextView.getResources().getColor(R.color.text_color_yellow));
			}
			if(list.get(position).getStatus().equals("2"))
			{
				viewHolder.orderActionTextView.setText("已支付");
				viewHolder.orderActionTextView.setTextColor(viewHolder.orderActionTextView.getResources().getColor(R.color.text_green_color));
			}
			if(list.get(position).getStatus().equals("3"))
			{
				viewHolder.orderActionTextView.setText("已完成");
				viewHolder.orderActionTextView.setTextColor(viewHolder.orderActionTextView.getResources().getColor(R.color.text_gray_color));
			}
			viewHolder.orderTypeTextView.setText(list.get(position).getSubject());
			viewHolder.orderTimeTextView.setText(Util.DataConVertMint(list.get(position).getTime()));
//			if(!TextUtils.isEmpty(list.get(position).getName()))
//			{
//			    viewHolder.orderNameTextView.setText(list.get(position).getName());
//			}
//			else
//			{
//				viewHolder.orderNameTextView.setText("未填写");
//				//viewHolder.orderNameTextView.setVisibility(View.GONE);
//				//viewHolder.icon_man.setVisibility(View.GONE);
//			}
//			if(!TextUtils.isEmpty(list.get(position).getMobile()))
//			{
//			    viewHolder.orderPhoneTextView.setText(list.get(position).getMobile());
//			}
//			else
//			{
//				viewHolder.orderPhoneTextView.setText("未填写");
//				 //viewHolder.orderPhoneTextView.setVisibility(View.GONE);
//				 //viewHolder.icon_phone.setVisibility(View.GONE);
//			}
			viewHolder.orderPriceTextView.setText("￥"+list.get(position).getFee());
			viewHolder.orderSourceText.setText(list.get(position).getSource().getName());
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
