package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.javabean.ActivityMsgBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/24
 */

public class ActivityMsGListAdapter extends RecyclerView.Adapter {
	Context mContext;
	private List<ActivityMsgBean.ListBean> list =new ArrayList<>();

	public ActivityMsGListAdapter(Context context) {
		mContext = context;
	}

	public void addAll(List<ActivityMsgBean.ListBean> list) {
		if (list!=null &&list.size()>0){
			this.list = list;
			notifyDataSetChanged();
		}
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = View.inflate(mContext, R.layout.item_activity_msg, null);
		MyHolder holder = new MyHolder(view);

		return holder;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		MyHolder myholder = (MyHolder) holder;

		ActivityMsgBean.ListBean listBean = list.get(position);
		myholder.title.setText(listBean.getTitle());
		myholder.discription.setText(listBean.getDesc());
		myholder.msg_send.setText("/"+listBean.getSend_num());
		myholder.msg_receive.setText(listBean.getSucceed_num());
		int fail = Integer.parseInt(listBean.getSend_num()) - Integer.parseInt(listBean.getSucceed_num());
		myholder.msg_fail.setText(fail+"");
		
	}

	@Override
	public int getItemCount() {
		return list.size();
	}



	class MyHolder extends RecyclerView.ViewHolder {
		private TextView title;
		private TextView discription;
		private TextView msg_receive;
		private TextView msg_send;
		private TextView msg_fail;

		public MyHolder(View itemView) {
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.title);
			discription = (TextView) itemView.findViewById(R.id.discription);
			msg_receive = (TextView) itemView.findViewById(R.id.msg_receive);
			msg_send = (TextView) itemView.findViewById(R.id.msg_send);
			msg_fail = (TextView) itemView.findViewById(R.id.msg_fail);
		}
	}
}
