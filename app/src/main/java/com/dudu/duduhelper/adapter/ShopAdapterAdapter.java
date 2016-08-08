package com.dudu.duduhelper.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.bean.GetCashDataBean;
import com.dudu.duduhelper.bean.GetHongBaoHistDataBean;
import com.dudu.duduhelper.bean.OrderDataBean;
import com.dudu.duduhelper.common.Util;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopAdapterAdapter extends BaseAdapter 
{
	private Context context;
    private ViewHolder viewHolder;
    List<GetHongBaoHistDataBean> list=new ArrayList<GetHongBaoHistDataBean>();
    public ShopAdapterAdapter(Context context)
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
			convertView = LayoutInflater.from(context).inflate(R.layout.shop_manager_item, null);
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
//			if((System.currentTimeMillis()-Long.parseLong(list.get(position).getExpire_time())*1000)>0)
//			{
//				viewHolder.moneyIcon.setTextColor(viewHolder.moneyIcon.getResources().getColor(R.color.text_color_light));
//				viewHolder.moneyAction.setTextColor(viewHolder.moneyAction.getResources().getColor(R.color.text_color_light));
//				viewHolder.moneyText.setTextColor(viewHolder.moneyText.getResources().getColor(R.color.text_color_light));
//				viewHolder.moneyAction.setText("已过期");
//				if(!TextUtils.isEmpty(list.get(position).getExpire_time()))
//				{
//					viewHolder.getDataText.setText("过期时间:"+Util.DataConVertMint(list.get(position).getExpire_time()));
//				}
//			}
//			else 
//			{
//				if(list.get(position).getUsed().equals("0"))
//				{
//					viewHolder.moneyIcon.setTextColor(viewHolder.moneyIcon.getResources().getColor(R.color.text_color_red));
//					viewHolder.moneyAction.setTextColor(viewHolder.moneyAction.getResources().getColor(R.color.text_color_red));
//					viewHolder.moneyText.setTextColor(viewHolder.moneyText.getResources().getColor(R.color.text_color_red));
//					viewHolder.moneyAction.setText("未使用");
//					if(!TextUtils.isEmpty(list.get(position).getExpire_time()))
//					{
//						viewHolder.getDataText.setText("过期时间:"+Util.DataConVertMint(list.get(position).getExpire_time()));
//					}
//				}
//				else
//				{
//					viewHolder.moneyIcon.setTextColor(viewHolder.moneyIcon.getResources().getColor(R.color.text_color));
//					viewHolder.moneyAction.setTextColor(viewHolder.moneyAction.getResources().getColor(R.color.text_color));
//					viewHolder.moneyText.setTextColor(viewHolder.moneyText.getResources().getColor(R.color.text_color));
//					viewHolder.moneyAction.setText("已使用");
//					if(!TextUtils.isEmpty(list.get(position).getUsed_time()))
//					{
//						viewHolder.getDataText.setText("使用时间:"+Util.DataConVertMint(list.get(position).getUsed_time()));
//					}
//				}
//			}
////			else if(list.get(position).getUsed().equals("1"))
////			{
////				
////			}
//			if(!TextUtils.isEmpty(list.get(position).getMoney()))
//			{
//				viewHolder.moneyText.setText(list.get(position).getMoney());
//			}
//			if(!TextUtils.isEmpty(list.get(position).getTitle()))
//			{
//				viewHolder.titleText.setText(list.get(position).getTitle());
//			}
//			if(!TextUtils.isEmpty(list.get(position).getNickname()))
//			{
//				viewHolder.getNameText.setText("领取人:"+list.get(position).getNickname());
//			}
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
