package com.dudu.helper3.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.dudu.duduhelper.R;

/**
 * Created by lwz on 2016/8/22.
 */
public class MsgOperationAdapter  extends BaseAdapter{

	private final Context context;

	public  MsgOperationAdapter(Context context){
		this.context = context;
	}
	@Override
	public int getCount() {
		return 3;
	}
	@Override
	public View getView(int i, View converView, ViewGroup parent) {
		if (converView == null){
			converView = View.inflate(context,R.layout.item_msglist,null);

		}

		return converView;
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
