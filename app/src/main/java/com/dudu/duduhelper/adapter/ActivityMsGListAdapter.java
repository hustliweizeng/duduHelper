package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dudu.duduhelper.R;

/**
 * @author
 * @version 1.0
 * @date 2016/9/24
 */

public class ActivityMsGListAdapter extends RecyclerView.Adapter {
	Context mContext;
	

	public ActivityMsGListAdapter(Context context) {
		mContext = context;
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = View.inflate(mContext, R.layout.item_activity_msg, null);
		MyHolder holder = new MyHolder(view);

		return holder;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		
	}

	@Override
	public int getItemCount() {
		return 10;
	}

	

	class MyHolder extends RecyclerView.ViewHolder{
		private TextView title;
		private TextView discription;
		private TextView msg_receive;
		private TextView msg_send;
		private TextView msg_fail;

		public MyHolder(View itemView) {
			super(itemView);
			
			
			
		}
	}
}
