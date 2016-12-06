package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dudu.duduhelper.javabean.BankCardListBean;

import java.util.ArrayList;
import java.util.List;
import com.dudu.duduhelper.R;
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
			viewHolder.bankLogo = (ImageView) convertView.findViewById(R.id.banklogoImg);
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
			if(!TextUtils.isEmpty(data.getBank_key()))
			{
				viewHolder.bankNameText.setText(data.getBank_key());
			}
			//设置银行卡号
			if(!TextUtils.isEmpty(data.getCard_number()))
			{
				//只显示末尾4个
				if (data.getCard_number().length()>4){
					viewHolder.bankCardNum.setText(data.getCard_number().substring(data.getCard_number().length()-4));
				}else {
					viewHolder.bankCardNum.setText("****");
				}
			}
			//设置姓名
			if(!TextUtils.isEmpty(list.get(position).getName()))
			{
				viewHolder.bankCardUsername.setText(data.getName());
			}
			//设置银行图标
			if(!TextUtils.isEmpty(list.get(position).getBank_key()))
			{
				int resource = R.drawable.defaultbank;
				switch (list.get(position).getBank_key()){
					case "广发银行":
						resource = R.drawable.gfbank;
						break;
					case "华夏银行":
						resource = R.drawable.hxbank;
						break;
					case "交通银行":
						resource = R.drawable.jtbank;
						break;
					case "浦东银行":
						resource = R.drawable.pdbank;
						break;
					case "兴业银行":
						resource = R.drawable.xybank;
						break;
					case "招商银行":
						resource = R.drawable.zsbank;
						break;
					case "中国工商银行":
						resource = R.drawable.gsbank;
						break;
					case "中国光大银行":
						resource = R.drawable.gdbank;
						break;
					case "中国建设银行":
						resource = R.drawable.jsbank;
						break;
					case "中国民生银行":
						resource = R.drawable.msbank;
						break;
					case "中国农业银行":
						resource = R.drawable.nybank;
						break;
					case "中国信合":
						resource = R.drawable.xhbank;
						break;
					case "中国银行":
						resource = R.drawable.zgbank;
						break;
					case "中国邮政":
						resource = R.drawable.yzbank;
						break;
				}
				viewHolder.bankLogo.setImageResource(resource);
			}
			
		}
		return convertView;
	}
	private class ViewHolder {
		TextView bankNameText;
		TextView bankCardNum;
		TextView bankCardUsername;
		ImageView bankLogo;
	}



}
