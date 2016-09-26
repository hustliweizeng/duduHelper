package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.dudu.duduhelper.R;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/23
 */

public class RedbagMsgListAdapter extends RecyclerView.Adapter {
	Context context;
	public RedbagMsgListAdapter(Context context){
		this.context = context;
	}
	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = View.inflate(context, R.layout.item_redbag_msg_detail,null);
		MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

	}

	@Override
	public int getItemCount() {
		return 10;
	}
	
	class MyViewHolder extends RecyclerView.ViewHolder{

		public MyViewHolder(final View itemView) {
			super(itemView);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if(mListener!=null){
						mListener.onItemClick(itemView,getLayoutPosition());
					}
				}
			});
		}
	}
	
	GuestListCheckAdapter.OnItemClickListner mListener;
	//为adapter编写条目点击事件
	public interface OnItemClickListner{
		public void onItemClick(View view ,int position);
	}
	//回调方法
	public void setOnItemClickListner(GuestListCheckAdapter.OnItemClickListner listner){
		mListener = listner;
	}
	
}
