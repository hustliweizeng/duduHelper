package com.dudu.helper3.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dudu.duduhelper.R;
import com.dudu.helper3.bean.GetCouponSellDataBean;
import com.dudu.helper3.Utils.Util;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CouponSellHistoryAdapter extends BaseAdapter 
{
	private Context context;
    private ViewHolder viewHolder;
    List<GetCouponSellDataBean> list=new ArrayList<GetCouponSellDataBean>();
    public CouponSellHistoryAdapter(Context context)
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
    public void addAll(List<GetCouponSellDataBean> list)
    {
    	this.list.addAll(this.list.size(), list);
    	notifyDataSetChanged();
    }

	@Override
	public GetCouponSellDataBean getItem(int arg0) 
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
			convertView = LayoutInflater.from(context).inflate(R.layout.activity_getcoupon_item, null);
			viewHolder.getcashtype=(TextView) convertView.findViewById(R.id.getcashtype);
			viewHolder.getcashaction=(TextView) convertView.findViewById(R.id.getcashaction);
			viewHolder.getcashtime=(TextView) convertView.findViewById(R.id.getcashtime);
			viewHolder.getcashsellname=(TextView) convertView.findViewById(R.id.getcashsellname);
			viewHolder.getcashsellnum=(TextView) convertView.findViewById(R.id.getcashsellnum);
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
			if(!TextUtils.isEmpty(list.get(position).getNickname()))
			{
				viewHolder.getcashsellname.setText("领取人："+list.get(position).getNickname());
			}
			if(!TextUtils.isEmpty(list.get(position).getVerifytime()))
			{
				viewHolder.getcashtime.setText("过期时间："+ Util.DataConVert2(list.get(position).getExptime()));
			}
			if(!TextUtils.isEmpty(list.get(position).getSn()))
			{
				viewHolder.getcashsellnum.setText("劵号："+list.get(position).getSn());
			}
			if(!TextUtils.isEmpty(list.get(position).getVerifytime()))
			{
				if(list.get(position).getVerifytime().equals("0"))
				{
					if(!TextUtils.isEmpty(list.get(position).getExptime()))
					{
						if((System.currentTimeMillis()-(Long.parseLong(list.get(position).getExptime())*1000))>0)
						{
							viewHolder.getcashaction.setText("已过期");
							viewHolder.getcashaction.setTextColor(viewHolder.getcashaction.getResources().getColor(R.color.text_color_light));
						}
						else
						{
							viewHolder.getcashaction.setText("未验证");
							viewHolder.getcashaction.setTextColor(viewHolder.getcashaction.getResources().getColor(R.color.text_color_yellow));
						}
					}
					
				}
				if(!list.get(position).getVerifytime().equals("0"))
				{
					viewHolder.getcashaction.setText("已验证");
					viewHolder.getcashaction.setTextColor(viewHolder.getcashaction.getResources().getColor(R.color.text_color));
				}
			}
		}
		return convertView;
	}
	private class ViewHolder
	{
		TextView getcashtype;
		TextView getcashaction;
		TextView getcashtime;
		TextView getcashsellname;
		TextView getcashsellnum;
	}
}
