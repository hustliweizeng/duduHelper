package com.dudu.helper3.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dudu.helper3.R;
import com.dudu.helper3.javabean.RedBagHitsoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/10/11
 */

public class RedBagHistoryAdapter extends BaseAdapter {

	private final Context mContext;
	public List<RedBagHitsoryBean.DataBean> list = new ArrayList<>();

	public RedBagHistoryAdapter(Context context) {
		mContext = context;
	}

	public void addAll(List<RedBagHitsoryBean.DataBean> list) {
		if (list != null && list.size() > 0) {
			this.list = list;
		}
		notifyDataSetChanged();
	}
	

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public RedBagHitsoryBean.DataBean getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(mContext, R.layout.item_redbag_history, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		RedBagHitsoryBean.DataBean data = list.get(position);
		holder.tv_price_hist.setText(data.getMoney());
		holder.tv_title_his.setText(data.getRed_packet_title());
		holder.tv_name_his.setText(data.getMember_nickname());
		holder.tv_expireTime_his.setText(data.getExpire_time());
		
		if (data.getUsed().equals("0")){
			holder.tv_status_his.setText("未使用");
			holder.tv_status_his.setTextColor(mContext.getResources().getColor(R.color.text_red_color));

		}else if (data.getUsed().equals("1")){
			holder.tv_status_his.setText("已使用");
			holder.tv_status_his.setTextColor(mContext.getResources().getColor(R.color.text_color));


		}else{
			holder.tv_status_his.setText("已过期");
			holder.tv_status_his.setTextColor(mContext.getResources().getColor(R.color.text_dark_color));
		}

		return convertView;
	}

	public static class ViewHolder {
		public View rootView;
		public TextView tv_price_hist;
		public TextView tv_status_his;
		public TextView tv_title_his;
		public TextView tv_name_his;
		public TextView tv_expireTime_his;

		public ViewHolder(View rootView) {
			this.rootView = rootView;
			this.tv_price_hist = (TextView) rootView.findViewById(R.id.tv_price_hist);
			this.tv_status_his = (TextView) rootView.findViewById(R.id.tv_status_his);
			this.tv_title_his = (TextView) rootView.findViewById(R.id.tv_title_his);
			this.tv_name_his = (TextView) rootView.findViewById(R.id.tv_name_his);
			this.tv_expireTime_his = (TextView) rootView.findViewById(R.id.tv_expireTime_his);
		}

	}
}
