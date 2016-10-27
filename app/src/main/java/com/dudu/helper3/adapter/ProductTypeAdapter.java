package com.dudu.helper3.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dudu.helper3.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ProductTypeAdapter extends BaseAdapter {

	private Context context;
	private List<String> list=new ArrayList<String>();
	
	public void addAll(List<String> list)
	{
		this.list.addAll(this.list.size(), list);
    	notifyDataSetChanged();
	}
	public ProductTypeAdapter(Context context)
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
		convertView = LayoutInflater.from(context).inflate(R.layout.activity_product_window_select_item, null);
		TextView textView=(TextView) convertView.findViewById(R.id.selectTypeTextView);
		textView.setText(list.get(position));
		return convertView;
	}

}
