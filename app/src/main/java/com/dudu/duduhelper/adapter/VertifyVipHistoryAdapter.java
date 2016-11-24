package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.javabean.VipCertifyHistoryBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;
import com.dudu.duduhelper.R;
/**
 * @author
 * @version 1.0
 * @date 2016/11/3
 */

public class VertifyVipHistoryAdapter extends BaseAdapter {
	private Context context;
	private List<VipCertifyHistoryBean.ListBean> list = new ArrayList<>();

	public VertifyVipHistoryAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_virtify_history, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else {
			
			holder = (ViewHolder) convertView.getTag();
		}
		VipCertifyHistoryBean.ListBean listBean = list.get(position);
		
		holder.title.setText(listBean.getName());
		holder.date.setText(listBean.getUsed_time().substring(0,10));
		holder.name.setText("验证者: "+listBean.getUsed_user());


		/**
		 * 使用tag防止跳动
		 */
		String tag = (String) holder.iv_pic.getTag();
		if (listBean.getImage()!=null){
			if (!listBean.getImage().equals(tag)){//当标签不一致时，说明位置不同，可以加载数据
				holder.iv_pic.setTag(listBean.getImage());
				//图片设置为圆角
				ImageLoader.getInstance().displayImage(listBean.getImage(),holder.iv_pic, new DisplayImageOptions.Builder()
						.displayer(new RoundedBitmapDisplayer(Util.dip2px(context,80))).build());
			}
		}
		  
		return convertView;
	}

	public void addAll(List<VipCertifyHistoryBean.ListBean> list) {
		if (list != null && list.size() > 0) {
			this.list = list;
			notifyDataSetChanged();
		}
	}

	public static class ViewHolder {
		private TextView  date;
		public View rootView;
		public ImageView iv_pic;
		public TextView title;
		public ImageView iv_logo;
		public TextView name;

		public ViewHolder(View rootView) {
			this.rootView = rootView;
			this.iv_pic = (ImageView) rootView.findViewById(R.id.iv_pic);
			this.title = (TextView) rootView.findViewById(R.id.title);
			this.iv_logo = (ImageView) rootView.findViewById(R.id.iv_logo);
			this.name = (TextView) rootView.findViewById(R.id.name);
			this.date = (TextView) rootView.findViewById(R.id.tv_date);
		}

	}
}
