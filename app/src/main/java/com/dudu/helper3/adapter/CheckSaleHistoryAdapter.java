package com.dudu.helper3.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dudu.helper3.R;
import com.dudu.helper3.Utils.Util;
import com.dudu.helper3.javabean.CheckSaleHistoryBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/11/2
 */
public class CheckSaleHistoryAdapter extends BaseAdapter {


	private final Context context;
	private List<CheckSaleHistoryBean.DataBean.OrderBean> list = new ArrayList<>();
	private List<CheckSaleHistoryBean.DataBean> data;

	public CheckSaleHistoryAdapter(Context context) {
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
		if (convertView == null){
			convertView = View.inflate(context, R.layout.item_checkhistroy, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.checkhistory_title.setText(data.get(0).getDate());//显示日期
		holder.checkhistory_name.setText(list.get(position).getSubject());
		holder.checkhistory_id.setText("核销码: "+list.get(position).getCode());
		holder.checkhistory_date.setText("核销时间: "+Util.DateForomate3(list.get(position).getUsed_time()));//unix转换日期

		String tag = (String) holder.checkhistory_img.getTag();
		String imgPath = list.get(position).getThumb();
		if (imgPath!=null && !imgPath.equals(tag)){//不是同一个位置需要显示出来图片
			holder.checkhistory_img.setTag(imgPath);//重新设置tag
			ImageLoader.getInstance().displayImage(list.get(position).getThumb(),holder.checkhistory_img);//显示图片
		}
		
		
		return convertView;
	}

	public void add(List<CheckSaleHistoryBean.DataBean> data) {
		this.data = data;
		if (data != null && data.size() > 0) {
			//循环加入数据到列表
			for (CheckSaleHistoryBean.DataBean item : data) {
				for (CheckSaleHistoryBean.DataBean.OrderBean item1 : item.getOrder()) {
					list.add(item1);

				}
			}

		}

	}

	public static class ViewHolder {
		public View rootView;
		public TextView checkhistory_title;
		public ImageView checkhistory_img;
		public TextView checkhistory_name;
		public TextView checkhistory_id;
		public TextView checkhistory_date;

		public ViewHolder(View rootView) {
			this.rootView = rootView;
			this.checkhistory_title = (TextView) rootView.findViewById(R.id.checkhistory_title);
			this.checkhistory_img = (ImageView) rootView.findViewById(R.id.checkhistory_img);
			this.checkhistory_name = (TextView) rootView.findViewById(R.id.checkhistory_name);
			this.checkhistory_id = (TextView) rootView.findViewById(R.id.checkhistory_id);
			this.checkhistory_date = (TextView) rootView.findViewById(R.id.checkhistory_date);
		}

	}
}
