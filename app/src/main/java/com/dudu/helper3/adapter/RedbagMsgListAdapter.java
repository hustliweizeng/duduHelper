package com.dudu.helper3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.javabean.RedbagMsgListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/23
 */

public class RedbagMsgListAdapter extends RecyclerView.Adapter {
	Context context;
	List<RedbagMsgListBean.ListBean> list = new ArrayList<>();


	public RedbagMsgListAdapter(Context context) {
		this.context = context;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = View.inflate(context, R.layout.item_redbag_msg_detail, null);
		MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}

	public void addAll(List<RedbagMsgListBean.ListBean> list) {
		if (list!=null && list.size()>0){
			this.list.addAll(list);
			LogUtil.d("ok","条目数:"+list.size());
			notifyDataSetChanged();
		}
	}


	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		MyViewHolder holder1 = (MyViewHolder) holder;
		RedbagMsgListBean.ListBean listBean = list.get(position);
		if (listBean!=null){
			holder1.tv_date.setText(listBean.getCreated_at());
			holder1.msg_receive.setText(listBean.getSucceed_num());
			holder1.msg_send.setText("/"+listBean.getSend_num());
			int fail = Integer.parseInt(listBean.getSend_num()) - Integer.parseInt(listBean.getSucceed_num());
			holder1.msg_fail.setText(fail+"");
			holder1.msg_use.setText(listBean.getUsed_num());
			holder1.promote_price.setText(listBean.getPromote_consumer());
			long endTime = Long.parseLong(listBean.getEnd_time());
			//判断红包是否过期
			if (System.currentTimeMillis() > endTime){
				holder1.tv_status.setText("已结束");
				holder1.tv_status.setVisibility(View.VISIBLE);
			}else {
				holder1.tv_status.setVisibility(View.GONE);
			}
			//如果是最后一条
			if (position == list.size()-1 &&position >10){
				//添加脚布局
				holder1.footview.setVisibility(View.VISIBLE);
			}else {
				holder1.footview.setVisibility(View.GONE);
			}
			
		}
	}

	@Override
	public int getItemCount() {
		return list.size();
	}

	class MyViewHolder extends RecyclerView.ViewHolder {
		private TextView tv_date;
		private TextView tv_status;
		private TextView msg_receive;
		private TextView msg_send;
		private TextView msg_fail;
		private TextView msg_use;
		private TextView promote_price;
		private LinearLayout footview;

		public MyViewHolder(final View itemView) {
			super(itemView);
			tv_date = (TextView) itemView.findViewById(R.id.tv_date);
			tv_status = (TextView) itemView.findViewById(R.id.tv_status);
			msg_receive = (TextView) itemView.findViewById(R.id.msg_receive);
			msg_send = (TextView) itemView.findViewById(R.id.msg_send);
			msg_fail = (TextView) itemView.findViewById(R.id.msg_fail);
			msg_use = (TextView) itemView.findViewById(R.id.msg_use);
			promote_price = (TextView) itemView.findViewById(R.id.promote_price);
			footview = (LinearLayout) itemView.findViewById(R.id.footview);
			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mListener != null) {
						mListener.onItemClick(itemView, getLayoutPosition());
					}
				}
			});
		}
	}

	GuestListCheckAdapter.OnItemClickListner mListener;

	//为adapter编写条目点击事件
	public interface OnItemClickListner {
		public void onItemClick(View view, int position);
	}

	//回调方法
	public void setOnItemClickListner(GuestListCheckAdapter.OnItemClickListner listner) {
		mListener = listner;
	}

}
