package com.dudu.helper3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dudu.helper3.R;
import com.dudu.helper3.Utils.Util;
import com.dudu.helper3.bean.CashHistoryDataBean;

import java.util.ArrayList;
import java.util.List;

public class GetCashHistoryAdapter extends BaseAdapter 
{
	private ViewHolder viewHolder;
	private Context context;
	private List<CashHistoryDataBean> list=new ArrayList<CashHistoryDataBean>();
	
	public GetCashHistoryAdapter(Context context) 
	{
		this.context=context;
	}
    public void addAll(List<CashHistoryDataBean> list)
    {
    	this.list.addAll(this.list.size(), list);
    	notifyDataSetChanged();
    }
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(convertView==null)
		{
			viewHolder=new ViewHolder();
			convertView=LayoutInflater.from(context).inflate(R.layout.activity_get_cash_history_list_item, null);
			viewHolder.getCashActionTextView=(TextView) convertView.findViewById(R.id.getCashActionTextView);
			viewHolder.getCashMoneyTextView=(TextView) convertView.findViewById(R.id.getCashMoneyTextView);
			viewHolder.getCashTiemTextView=(TextView) convertView.findViewById(R.id.getCashTiemTextView);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(list.get(position).getStatus().equals("1"))
		{
			viewHolder.getCashActionTextView.setText("提现成功");
			viewHolder.getCashMoneyTextView.setText("-"+list.get(position).getMoney());
		}
		if(list.get(position).getStatus().equals("0"))
		{
			viewHolder.getCashActionTextView.setText("审核中");
			viewHolder.getCashMoneyTextView.setText("-"+list.get(position).getMoney());
			viewHolder.getCashActionTextView.setTextColor(viewHolder.getCashActionTextView.getResources().getColor(R.color.text_color));
		}
		if(list.get(position).getStatus().equals("-1"))
		{
			viewHolder.getCashActionTextView.setText("提现失败");
			viewHolder.getCashMoneyTextView.setText("+"+list.get(position).getMoney());
			viewHolder.getCashActionTextView.setTextColor(viewHolder.getCashActionTextView.getResources().getColor(R.color.text_color_yellow));
		}
		viewHolder.getCashTiemTextView.setText(Util.DataConVert(list.get(position).getTime()));
		return convertView;
	}
	private class ViewHolder
	{
		TextView getCashActionTextView;
		TextView getCashMoneyTextView;
		TextView getCashTiemTextView;
	}

}
