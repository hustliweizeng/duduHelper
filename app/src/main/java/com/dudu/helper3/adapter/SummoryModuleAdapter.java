package com.dudu.helper3.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dudu.helper3.R;
import com.dudu.helper3.javabean.SummoryDetaiBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/10/17
 */

public class SummoryModuleAdapter extends BaseAdapter {
	private final Context context;
	List<SummoryDetaiBean.TradeModulesBean> list =new ArrayList<>();

	public SummoryModuleAdapter(Context context) {
		this.context = context;
	}

	public void addAll(List<SummoryDetaiBean.TradeModulesBean> list) {
		if (list != null && list.size() > 0) {
			this.list = list;
			notifyDataSetChanged();
		}
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
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_sum, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);

		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		String module_id = list.get(position).getModule_id();
		String name ="";
		switch (module_id){
			case "1":
				name ="大牌抢购";
				break;
			case "6":
				name ="商家收款";
				break;
			case "7":
				name ="优惠券";
				break;
			case "8":
				name ="五折卡";
				break;
			case "9":
				name ="刷卡支付";
				break;
			case "10":
				name ="扫码支付";
				break;
			case "11":
				name ="周边活动";
				break;
		}
		holder.tv_name_other.setText(name);
		holder.tv_price_other.setText(list.get(position).getFee());
		return convertView;
	}

	public static class ViewHolder {
		public View rootView;
		public TextView tv_name_other;
		public TextView tv_price_other;

		public ViewHolder(View rootView) {
			this.rootView = rootView;
			this.tv_name_other = (TextView) rootView.findViewById(R.id.tv_name_other);
			this.tv_price_other = (TextView) rootView.findViewById(R.id.tv_price_other);
		}

	}
}
