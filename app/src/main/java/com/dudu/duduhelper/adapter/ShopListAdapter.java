package com.dudu.duduhelper.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.bean.GetHongBaoHistDataBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopListAdapter extends BaseAdapter 
{
	private Context context;
    private ViewHolder viewHolder;
    List<GetHongBaoHistDataBean> list=new ArrayList<GetHongBaoHistDataBean>();
    public ShopListAdapter(Context context)
	{
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return list.size();
	}
	public void clear()
    {
    	list.clear();
    	notifyDataSetChanged();
    }
    public void addAll(List<GetHongBaoHistDataBean> list)
    {
    	this.list.addAll(this.list.size(), list);
    	notifyDataSetChanged();
    }

	@Override
	public GetHongBaoHistDataBean getItem(int arg0) 
	{
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) 
	{
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
			convertView = LayoutInflater.from(context).inflate(R.layout.shop_list_item, null);
//			viewHolder.moneyText=(TextView) convertView.findViewById(R.id.moneyText);
//			viewHolder.titleText=(TextView) convertView.findViewById(R.id.titleText);
//			viewHolder.getNameText=(TextView) convertView.findViewById(R.id.getNameText);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(list.size()!=0)
		{
//			
		}
		return convertView;
	}
	private class ViewHolder
	{
		private ImageView shopimage;
		private TextView shopnametext;
		private TextView shoplocationtext;
		
	}
}
