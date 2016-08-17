package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dudu.duduhelper.R;

/**
 * Created by lwz on 2016/8/17.
 */
public class MoneyHistoryAdapter  extends BaseAdapter{
	Context context;
	public MoneyHistoryAdapter(Context context){
		this.context = context;

	}

	@Override
	public int getCount() {
		return 10;
	}

	@Override
	public View getView(int position,View convertView, ViewGroup parent) {
		if (convertView == null){
			convertView = View.inflate(context, R.layout.item_checkhistroy,null);
		}
		Log.d("adapter","已经打印");
		return  convertView;
	}

	@Override
	public long getItemId(int i) {
		return 0;
	}

	@Override
	public Object getItem(int i) {
		return null;
	}
}
