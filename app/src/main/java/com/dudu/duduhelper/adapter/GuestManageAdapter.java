package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.javabean.GuestListBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import com.dudu.duduhelper.R;
/**
 * @author
 * @version 1.0
 * @date 2016/9/23
 */

public class GuestManageAdapter extends RecyclerView.Adapter {

	List<GuestListBean.GuestDetails> list = new ArrayList<>();
	Context context ;

	public GuestManageAdapter(Context context ){
		this.context = context;

	}
	public void addAll(List<GuestListBean.GuestDetails> list){
		if (list!=null){
			LogUtil.d("moredata",list.size()+"");
			this.list.addAll(list);
			notifyDataSetChanged();//刷新页面
		}
	}
	@Override
	//把view封装成holder，自动复用
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {//根据不同的类型加载不同的布局
		View view = View.inflate(context, R.layout.item_guest,null);
		MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}
	//填写数据
	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		final  MyViewHolder myholder = (MyViewHolder) holder;
		GuestListBean.GuestDetails data = list.get(position);
		/**
		 * 判断当前图片是否刷新
		 */
		if (!data.getAvatar().equals(myholder.iv_photo)){
			//第一次运行tag肯定为null，所以会执行zheli 
			myholder.iv_photo.setTag(data.getAvatar());
			ImageLoader.getInstance().displayImage(data.getAvatar(),myholder.iv_photo);
		}
		myholder.tv_consume_price.setText(data.getTotal_consumption());
		myholder.tv_consume_conut.setText(data.getTotal_num());
		myholder.tv_date.setText(data.getLast_consumption_time());
		myholder.tv_name.setText(data.getNickname());
		if (position == list.size()-1 && position>5){
			myholder.footview.setVisibility(View.VISIBLE);
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

	public void clear() {
		list.clear();
	}

	//封装holder
	public class MyViewHolder extends  RecyclerView.ViewHolder{
		//通过构造方法绑定findview
		ImageView iv_photo ;
		TextView tv_name;
		TextView tv_consume_price;
		TextView tv_consume_conut;
		TextView tv_date;
		LinearLayout footview;
		TextView loading_text;
		public MyViewHolder(View itemView) {
			super(itemView);
			iv_photo = (ImageView) itemView.findViewById(R.id.iv_item_guest);
			tv_name = (TextView) itemView.findViewById(R.id.tv_title_item_guest);
			tv_consume_price = (TextView) itemView.findViewById(R.id.tv_totalprice_item_guest);
			tv_consume_conut = (TextView) itemView.findViewById(R.id.tv_count_item_guest);
			tv_date = (TextView) itemView.findViewById(R.id.tv_date_item_guest);
			footview = (LinearLayout) itemView.findViewById(R.id.footview);
			loading_text = (TextView) itemView.findViewById(R.id.loading_text);
		}
	}
	
}
