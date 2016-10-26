package com.dudu.helper3.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dudu.duduhelper.R;
import com.dudu.helper3.javabean.SelectorBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 订单筛选列表选择器
 */
public class OrderSelectorAdapter extends BaseAdapter {

	private Context context;
	private List<SelectorBean> list=new ArrayList<>();
	//设置选中的条目
	public String select ="";

	public void addAll(List< ?extends  SelectorBean> list, String select)
	{
		this.list.addAll(this.list.size(), list);
		this.select = select;
		notifyDataSetChanged();
	}

	public OrderSelectorAdapter(Context context)
	{
		this.context=context;
	}
	@Override
	public int getCount()
	{
		return list.size();
	}
	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}
	@Override
	public long getItemId(int position)
	{
		return list.get(position).id;
	}

	@SuppressLint("ResourceAsColor") @Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		convertView = LayoutInflater.from(context).inflate(R.layout.activity_product_window_select_item, null);
		TextView textView=(TextView) convertView.findViewById(R.id.selectTypeTextView);
		textView.setText(list.get(position).name);
		//设置选中条目的颜色
		
		if(!TextUtils.isEmpty(select)){
			//如果是设置颜色和背景
			if(select.equals(list.get(position).name))
			{
				textView.setTextColor(textView.getResources().getColor(R.color.text_green_color));
				textView.setBackgroundColor(0xfff4f4f4);
				//如果不是还原设置颜色和背景-------
				//-------坑了我好几次了---------
			}else {
				textView.setTextColor(textView.getResources().getColor(R.color.text_color_gray));
				textView.setBackgroundColor(0xfff);
			}
		}
		return convertView;
	}

}
