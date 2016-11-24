package com.dudu.duduhelper.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dudu.duduhelper.bean.GetCashDataBean;
import com.dudu.duduhelper.Utils.Util;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.dudu.duduhelper.R;
public class GetCashAdapter extends BaseAdapter 
{
	private Context context;
    private ViewHolder viewHolder;
    List<GetCashDataBean> list=new ArrayList<GetCashDataBean>();
    public GetCashAdapter(Context context)
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
    public void addAll(List<GetCashDataBean> list)
    {
    	this.list.addAll(this.list.size(), list);
    	notifyDataSetChanged();
    }

	@Override
	public GetCashDataBean getItem(int arg0) 
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
			convertView = LayoutInflater.from(context).inflate(R.layout.fragment_getcash_item, null);
			viewHolder.getcashtype=(TextView) convertView.findViewById(R.id.getcashtype);
			viewHolder.getcashaction=(TextView) convertView.findViewById(R.id.getcashaction);
			viewHolder.getcashtime=(TextView) convertView.findViewById(R.id.getcashtime);
			viewHolder.getcashmoney=(TextView) convertView.findViewById(R.id.getcashmoney);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(list.size()!=0)
		{
			if(!TextUtils.isEmpty(list.get(position).getSubject()))
			{
				viewHolder.getcashtype.setText(list.get(position).getSubject());
			}
			if(!TextUtils.isEmpty(list.get(position).getFee()))
			{
				viewHolder.getcashmoney.setText(list.get(position).getFee());
			}
			if(!TextUtils.isEmpty(list.get(position).getTime()))
			{
				viewHolder.getcashtime.setText(Util.DataConVertMint(list.get(position).getTime()));
			}
			if(list.get(position).getStatus().equals("1"))
			{
				viewHolder.getcashaction.setText("未支付");
				viewHolder.getcashaction.setBackgroundResource(R.drawable.btn_yellowboardline_bg);
				viewHolder.getcashaction.setTextColor(viewHolder.getcashaction.getResources().getColor(R.color.text_color_yellow));
			}
			if(list.get(position).getStatus().equals("2"))
			{
				viewHolder.getcashaction.setText("已支付");
				viewHolder.getcashaction.setBackgroundResource(R.drawable.btn_greenboardline_bg);
				viewHolder.getcashaction.setTextColor(viewHolder.getcashaction.getResources().getColor(R.color.text_color));
			}
		}
		return convertView;
	}
	private class ViewHolder
	{
		TextView getcashtype;
		TextView getcashaction;
		TextView getcashtime;
		TextView getcashmoney;
	}
}
