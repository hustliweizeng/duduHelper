package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.javabean.BankCardListBean;
import com.dudu.duduhelper.widget.CircleImageView;

import java.util.ArrayList;
import java.util.List;

public class BankListAdapter extends BaseAdapter 
{
	private Context context;
    private ViewHolder viewHolder;
    List<BankCardListBean.DataBean> list=new ArrayList<>();
    //private OnClick listener=null;
    
    public BankListAdapter(Context context)
	{
		this.context=context;
	}
	@Override
	public int getCount() 
	{
		return list.size();
	}
	public void clear()
    {
    	list.clear();
    	notifyDataSetChanged();
    }
    public void addAll(List<BankCardListBean.DataBean> list)
    {
    	this.list.addAll(this.list.size(), list);
    	notifyDataSetChanged();
    }
	//返回某个条目的银行卡号
	public  String getCardNo(int position){
		return  list.get(position).getId();
	}
	@Override
	public BankCardListBean.DataBean getItem(int arg0)
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
		if(convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.shop_bank_item, null);
			viewHolder.bankNameText = (TextView) convertView.findViewById(R.id.bankNameText);
			viewHolder.bankCardNum = (TextView) convertView.findViewById(R.id.bankCardNum);
			viewHolder.bankCardUsername = (TextView) convertView.findViewById(R.id.bankCardUsername);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		BankCardListBean.DataBean data = list.get(position);
		//设置数据非空判断
		if(list.size()!=0)
		{	//设置银行名称
			if(!TextUtils.isEmpty(data.getBank_name()))
			{
				viewHolder.bankNameText.setText(data.getBank_name());
			}
			//设置银行卡图标


			//设置银行卡号
			if(!TextUtils.isEmpty(data.getCard_number()))
			{
				//只显示末尾4个
				viewHolder.bankCardNum.setText(data.getCard_number());//substring(list.size()-4,list.size()-1));
			}
			//设置姓名
			if(!TextUtils.isEmpty(list.get(position).getName()))
			{
				viewHolder.bankCardUsername.setText(data.getName());
			}
			//
		}
		return convertView;
	}
	private class ViewHolder {
		TextView bankNameText;
		TextView bankCardNum;
		TextView bankCardUsername;
		CircleImageView banklogoImg;
	}



}
