package com.dudu.helper3.adapter;


import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dudu.helper3.R;
import com.dudu.helper3.Utils.Util;
import com.dudu.helper3.javabean.ShopCategoryBean;

import java.util.ArrayList;
import java.util.List;

public class ShopCategoryAdapter extends BaseAdapter

{
	private Context context;
	private TextView textView;
	private  int layout;
	private List<ShopCategoryBean.DataBean> list = new ArrayList<>();
    public ShopCategoryAdapter(Context context, int layout)
    {
    	this.context = context;
		this.layout = layout;
    }
    public void addAll(List<ShopCategoryBean.DataBean> list)
    {
	    if(list!=null){
		    this.list.addAll(list);
		    notifyDataSetChanged();
	    }
		    
    }
	@Override
	public int getCount() 
	{
		return list.size();
	}

	@Override
	public ShopCategoryBean.DataBean getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(list.get(position).getId());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if (convertView == null){
			convertView = View.inflate(context, layout,null);
		}
		TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title_category);
		//手动设置条目高度，否则无效
		tv_title.setHeight(Util.dip2px(context,50));
		tv_title.setText(list.get(position).getName());

		return convertView;
	}
	public void clear() {
		list.clear();
		notifyDataSetChanged();
	}

}
