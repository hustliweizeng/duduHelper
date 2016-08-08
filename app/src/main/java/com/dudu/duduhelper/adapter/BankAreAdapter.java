package com.dudu.duduhelper.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.bean.ProvienceBean;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class BankAreAdapter extends BaseAdapter {

	private Context context;
	private List<ProvienceBean> list=new ArrayList<ProvienceBean>();
	private String select;
	
	public void addAll(List<ProvienceBean> list,String select)
	{
		this.list.addAll(this.list.size(), list);
		this.select=select;
    	notifyDataSetChanged();
	}
	public BankAreAdapter(Context context)
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

	@SuppressLint("ResourceAsColor") @Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(R.layout.activity_product_window_select_item, null);
		TextView textView=(TextView) convertView.findViewById(R.id.selectTypeTextView);
		textView.setText(list.get(position).getName());
		if(list.get(position).getName().equals(select))
		{
			textView.setTextColor(textView.getResources().getColor(R.color.text_green_color));
			textView.setBackgroundColor(0xfff4f4f4);
		}
		return convertView;
	}

}
