package com.dudu.helper3.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dudu.helper3.Activity.GuestManageActivity.CreateActivityMsg;
import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.javabean.ActivityMsgBean;

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
		//设置监听回调,每次加载完数据该接口被调用
	}
	public void addAll(List<ActivityMsgBean.ListBean> list) {
		if (list!=null &&list.size()>0){
			this.list .addAll(list);
			LogUtil.d("change",list.size()+"");
			notifyDataSetChanged();
		}
	}
	public static final int ITEM_TYPE_CONTENT = 1;
	public static final int ITEM_TYPE_BOTTOM = 2;

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		if (viewType == ITEM_TYPE_BOTTOM){
			View view = View.inflate(mContext, R.layout.activity_listview_foot, null);
			final  FootHolder footHolder  = new FootHolder(view);
			CreateActivityMsg msg = (CreateActivityMsg) mContext;
			msg.setOnLoadFinishi(new CreateActivityMsg.OnLoadFinishi() {
				@Override
				public void LoadfFinish(boolean isEmpty) {
					if (isEmpty){
						footHolder.loading_text.setText("已经到底了");
					}else {
						footHolder.loading_text.setText("加载完毕");
					}
				}
			});
			return footHolder;
		}else {
			View view = View.inflate(mContext, R.layout.item_activity_msg, null);
			MyHolder holder = new MyHolder(view);
			return holder;
		}
	}
	public  class  FootHolder extends  RecyclerView.ViewHolder{
		private  TextView loading_text ;
		private ProgressBar progressBar;

		public FootHolder(View itemView) {
			super(itemView);
			loading_text = (TextView) itemView.findViewById(R.id.loading_text);
			progressBar = (ProgressBar) itemView.findViewById(R.id.loading_progressBar);
		}
	}

	@Override
	public int getItemViewType(int position) {
		if (position == getItemCount()){
			return ITEM_TYPE_BOTTOM;
		}else {
			return ITEM_TYPE_CONTENT;
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		if (position == getItemCount()){
			return;
		}
		MyHolder myholder = (MyHolder) holder;
		ActivityMsgBean.ListBean listBean = list.get(position);
		myholder.title.setText(listBean.getTitle());
		myholder.discription.setText(listBean.getDesc());
		myholder.msg_send.setText("/"+listBean.getSend_num());
		myholder.msg_receive.setText(listBean.getSucceed_num());
		int fail = Integer.parseInt(listBean.getSend_num()) - Integer.parseInt(listBean.getSucceed_num());
		myholder.msg_fail.setText(fail+"");//失败人数
		if(!TextUtils.isEmpty(listBean.getCreated_at())){
			myholder.tv_date.setText(listBean.getCreated_at());
		}else {
			myholder.tv_date.setText("未知时间");
		}
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
		private TextView tv_date;

		public MyHolder(View itemView) {
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.title);
			discription = (TextView) itemView.findViewById(R.id.discription);
			msg_receive = (TextView) itemView.findViewById(R.id.msg_receive);
			msg_send = (TextView) itemView.findViewById(R.id.msg_send);
			msg_fail = (TextView) itemView.findViewById(R.id.msg_fail);
			tv_date = (TextView) itemView.findViewById(R.id.tv_date);
		}
	}
}
