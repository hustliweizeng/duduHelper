package com.dudu.helper3.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dudu.duduhelper.R;
import com.dudu.helper3.bean.OrderGoods;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OrderDetailAdapter extends BaseAdapter {

	private Context context;
	private List<OrderGoods> list=new ArrayList<OrderGoods>();
	
	public void addAll(List<OrderGoods> list)
	{
		this.list.addAll(this.list.size(), list);
    	notifyDataSetChanged();
	}
	public OrderDetailAdapter(Context context)
	{
		this.context=context;
	}
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
			
			return list.size();
	}

	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
			return list.get(position);
		
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(R.layout.activity_order_detail_item, null);
		TextView goodsNameTextView=(TextView) convertView.findViewById(R.id.goodsNameTextView);
		TextView goodsNamePriceTextView=(TextView) convertView.findViewById(R.id.goodsNamePriceTextView);
		goodsNameTextView.setText(list.get(position).getName());
		goodsNamePriceTextView.setText("￥"+list.get(position).getFee()+"x"+list.get(position).getNum());
		return convertView;
	}

}
