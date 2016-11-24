package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/28
 */

public class RedBagCheckAdapter<T> extends BaseAdapter {

	List<T> list;
	private final Context context;
	
	public RedBagCheckAdapter(Context context) {
		this.context = context;
	}
	
	public void AddAll(List<T> list) {
		this.list = list;
	}

	@Override

	public int getCount() {
		return list.size();
	}

	@Override
	public T getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null){
			convertView = View.inflate(context, R.layout.activity_product_window_select_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.selectTypeTextView.setText((String)list.get(position));
		
		return convertView;
	}

	public static class ViewHolder {
		public View rootView;
		public TextView selectTypeTextView;

		public ViewHolder(View rootView) {
			this.rootView = rootView;
			this.selectTypeTextView = (TextView) rootView.findViewById(R.id.selectTypeTextView);
		}

	}
}
