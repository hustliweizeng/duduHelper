package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.dudu.duduhelper.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/23
 */

public class GuestManageAdapter extends RecyclerView.Adapter {

	List<Object> list = new ArrayList<>();
	Context context ;

	public GuestManageAdapter(Context context, List<Object> t ){
		this.context = context;
		list = t;
		
	}
	@Override
	//把view封装成holder，自动复用
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = View.inflate(context, R.layout.item_guest,null);
		MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}
	//填写数据
	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		
		MyViewHolder myholder = (MyViewHolder) holder;
		//Object o = list.get(position);
		//ImageLoader.getInstance().displayImage();
		
		
	}

	@Override
	public int getItemCount() {
		return 3;
	}
	//封装holder
	public class MyViewHolder extends  RecyclerView.ViewHolder{
		//通过构造方法绑定findview
		ImageView iv_photo ;
		TextView tv_name;
		TextView tv_consume_price;
		TextView tv_consume_conut;
		TextView tv_date;
		
		public MyViewHolder(View itemView) {
			super(itemView);
			iv_photo = (ImageView) itemView.findViewById(R.id.iv_item_guest);
			tv_name = (TextView) itemView.findViewById(R.id.tv_title_item_guest);
			tv_consume_price = (TextView) itemView.findViewById(R.id.tv_totalprice_item_guest);
			tv_consume_conut = (TextView) itemView.findViewById(R.id.tv_count_item_guest);
			tv_date = (TextView) itemView.findViewById(R.id.tv_date_item_guest);
		}
	}
	
	
	
}
