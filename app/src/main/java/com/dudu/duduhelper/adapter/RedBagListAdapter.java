package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.javabean.RedBagListBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/28
 */

public class RedBagListAdapter extends BaseAdapter {


	private final Context context;
	private List<RedBagListBean.DataBean> list  = new ArrayList<>();
	private HashMap< Integer,Boolean> delList = new HashMap<>();
	//存储要删除的列表
	private List<String> delIds = new ArrayList<>();
	private boolean isDel =false;
	private ViewHolder holder;
	private RedBagListBean.DataBean data;

	public  RedBagListAdapter(Context context) {
		this.context = context;
	}
	
	public void AddAll(List<RedBagListBean.DataBean> list) {
		if (list != null & list.size() > 0){
			this.list = list;
			for (int i= 0; i<list.size() ;i++){
				delList.put(i,false);
			}
			LogUtil.d("","data="+list.size());
		}
	}
	public void setIsDel(){
		isDel = !isDel;
		//状态改变后刷新页面
		notifyDataSetChanged();
	}
	public List<String> getDelList(){
		return delIds;
	}
	public void clear(){
		list =new ArrayList<>();
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(list.get(position).getId());
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		holder = null;
		if (convertView == null){
			convertView = View.inflate(context, R.layout.item_redbag_list, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
			LogUtil.d("position","新建了");

		}else{
			holder = (ViewHolder) convertView.getTag();
			LogUtil.d("position","复用了");
		}
		data = list.get(position);
		if(data.getLogo()!=null && data.getLogo().startsWith("http")){
			ImageLoader.getInstance().displayImage(data.getLogo(), holder.iv_logo);
		}
		holder.tv_sold.setText("已领:"+ data.getUsed_num());
		holder.tv_stock.setText("红包:"+ data.getNum());
		holder.tv_name.setText(data.getTitle());
		holder.tv_price.setText(data.getTotal());
		
		//设置红包状态
		long time = Util.Data2Unix(data.getTime_end());
		if (time <System.currentTimeMillis()){
			//已过期
			holder.tv_status.setText("已截至");
		}else {
			holder.tv_status.setText("发放中");
			holder.tv_status.setTextColor(context.getResources().getColor(R.color.text_color_yellow));
		}
		//复选框是否显示(因为holder被复用了，所以这里要对位置也进行判断)
		if (isDel){
			holder.btn_check.setVisibility(View.VISIBLE);
			if (delList.get(position)){
				holder.btn_check.setImageResource(R.drawable.icon_xuanze_sel);
			}else {
				holder.btn_check.setImageResource(R.drawable.icon_xuanze);
			}
		}else {
			holder.btn_check.setVisibility(View.GONE);
		}
		//复选框的监听事件
		holder.btn_check.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//判断集合中是否包含该条目
				if (delList.get(position)){
					delList.put(position,false);
					delIds.remove(data.getId());
					holder.btn_check.setImageResource(R.drawable.icon_xuanze);
				}else {
					delList.put(position,true);
					delIds.add(data.getId());
					holder.btn_check.setImageResource(R.drawable.icon_xuanze_sel);
				}
				notifyDataSetChanged();
			}
		});
		
		
		return convertView;
	}

	public static class ViewHolder {
		public View rootView;
		public ImageView iv_logo;
		public TextView tv_name;
		public TextView tv_stock;
		public TextView tv_sold;
		public TextView tv_price;
		public TextView tv_status;
		private ImageButton btn_check;

		public ViewHolder(View rootView) {
			this.rootView = rootView;
			this.iv_logo = (ImageView) rootView.findViewById(R.id.iv_logo);
			this.tv_name = (TextView) rootView.findViewById(R.id.tv_name);
			this.tv_stock = (TextView) rootView.findViewById(R.id.tv_stock);
			this.tv_sold = (TextView) rootView.findViewById(R.id.tv_sold);
			this.tv_price = (TextView) rootView.findViewById(R.id.tv_price);
			this.tv_status = (TextView) rootView.findViewById(R.id.tv_status);
			this.btn_check = (ImageButton) rootView.findViewById(R.id.btn_check);
		}

	}
}
