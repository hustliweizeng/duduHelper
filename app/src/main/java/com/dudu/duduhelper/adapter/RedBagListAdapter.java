package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.javabean.RedBagListBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/28
 */

public class RedBagListAdapter extends BaseAdapter {


	private final Context context;
	private List<RedBagListBean.DataBean> list;

	public  RedBagListAdapter(Context context) {
		this.context = context;
	}


	public void AddAll(List<RedBagListBean.DataBean> list) {
		if (list != null & list.size() > 0)
			this.list = list;
	}
	public void clear(){
		list =null;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;

		if (convertView == null){
			convertView = View.inflate(context, R.layout.item_redbag_list, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
			

		}else{
			holder = (ViewHolder) convertView.getTag();

		}
		RedBagListBean.DataBean data = list.get(position);
		if(data.getLogo()!=null)
		ImageLoader.getInstance().displayImage(data.getLogo(),holder.iv_logo);
		holder.tv_sold.setText(data.getUsed_num());
		holder.tv_name.setText(data.getTitle());
		holder.tv_price.setText(data.getTotal());
		holder.tv_status.setText(data.getNum());
		

		return null;
	}

	public static class ViewHolder {
		public View rootView;
		public ImageView iv_logo;
		public TextView tv_name;
		public TextView tv_stock;
		public TextView tv_sold;
		public TextView tv_price;
		public TextView tv_status;

		public ViewHolder(View rootView) {
			this.rootView = rootView;
			this.iv_logo = (ImageView) rootView.findViewById(R.id.iv_logo);
			this.tv_name = (TextView) rootView.findViewById(R.id.tv_name);
			this.tv_stock = (TextView) rootView.findViewById(R.id.tv_stock);
			this.tv_sold = (TextView) rootView.findViewById(R.id.tv_sold);
			this.tv_price = (TextView) rootView.findViewById(R.id.tv_price);
			this.tv_status = (TextView) rootView.findViewById(R.id.tv_status);
		}

	}
}
