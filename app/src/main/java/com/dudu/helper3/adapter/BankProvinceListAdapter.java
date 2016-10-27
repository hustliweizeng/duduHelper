package com.dudu.helper3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.javabean.ProvinceListBean;

import java.util.ArrayList;
import java.util.List;

public class BankProvinceListAdapter extends BaseAdapter {

	private Context context;
	private List<ProvinceListBean.DataBean> list = new ArrayList<>();
	//设置选中的条目
	private String select;

	public void addAll(List<ProvinceListBean.DataBean> list, String select)
	{
		//在指定位置加入集合
		this.list.addAll(list);
		this.select = select;
		//更新数据
    	notifyDataSetChanged();
	}
	public BankProvinceListAdapter(Context context)
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
		return Long.parseLong(list.get(position).getId());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = LayoutInflater.from(context).inflate(R.layout.activity_product_window_select_item, null);
		TextView textView=(TextView) convertView.findViewById(R.id.selectTypeTextView);
		textView.setText(list.get(position).getName());
		//设置选中颜色
		if(list.get(position).getName().equals(select))
		{
			textView.setTextColor(textView.getResources().getColor(R.color.text_green_color));
			textView.setBackgroundColor(0xfff4f4f4);
		}
		LogUtil.d("city_getview",position+"");
		return convertView;
	}
}
