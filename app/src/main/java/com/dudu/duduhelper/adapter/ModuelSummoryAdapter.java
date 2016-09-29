package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.javabean.Modules;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/29
 */

public class ModuelSummoryAdapter extends BaseAdapter {


	private final Context context;
	List<Modules> list = new ArrayList<>();
	public ModuelSummoryAdapter(Context context){
		this.context = context;
				
	}
	public  void addAll(List<Modules> list ){
		if (list!= null &&list.size()>0){

			this.list = list;
		}
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		convertView = View.inflate(context, R.layout.item_summury,null);
		TextView tv_source = (TextView) convertView.findViewById(R.id.tv_source);
		TextView tv_price = (TextView) convertView.findViewById(R.id.tv_price);
		
		tv_source.setText(list.get(position).name);
		tv_price.setText(list.get(position).sum);


		return convertView;
	}
	
	
	
	
}
