package com.dudu.helper3.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
			this.list.addAll(list);
			LogUtil.d("change",this.list.size()+"");
			notifyDataSetChanged();
		}
	}
	public  void clear(){
		list.clear();
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = View.inflate(mContext, R.layout.item_activity_msg, null);
		MyHolder holder = new MyHolder(view);
		return holder;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		final  MyHolder myholder = (MyHolder) holder;
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
		if (position == list.size()-1 && position>10){
			myholder.footview.setVisibility(View.VISIBLE);
			LogUtil.d("foot","true");
			CountDownTimer timer = new CountDownTimer(2000, 2000) {
				@Override
				public void onTick(long millisUntilFinished) {
				}
				@Override
				public void onFinish() {
					myholder.footview.setVisibility(View.GONE);
				}
			};
			timer.start();
		}else {
			myholder.footview.setVisibility(View.GONE);
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
		private LinearLayout footview;

		public MyHolder(View itemView) {
			super(itemView);
			title = (TextView) itemView.findViewById(R.id.title);
			discription = (TextView) itemView.findViewById(R.id.discription);
			msg_receive = (TextView) itemView.findViewById(R.id.msg_receive);
			msg_send = (TextView) itemView.findViewById(R.id.msg_send);
			msg_fail = (TextView) itemView.findViewById(R.id.msg_fail);
			tv_date = (TextView) itemView.findViewById(R.id.tv_date);
			footview = (LinearLayout) itemView.findViewById(R.id.footview);
		}
	}
}
