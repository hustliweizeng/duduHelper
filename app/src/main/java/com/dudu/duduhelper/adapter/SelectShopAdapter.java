package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dudu.duduhelper.javabean.ShopCheckListBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;
import com.dudu.duduhelper.R;
/**
 * @author
 * @version 1.0
 * @date 2016/11/9
 */
public class SelectShopAdapter extends BaseAdapter {
	private final Context context;
	List<ShopCheckListBean.ListBean> list = new ArrayList<>();

	public SelectShopAdapter(Context context) {
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
		return Long.parseLong(list.get(position).getId());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.shop_manager_item, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		

		ShopCheckListBean.ListBean dataBean = list.get(position);
		holder.name.setText(dataBean.getName());
		holder.month_trade.setText(dataBean.getTrade_month());
		holder.total_trade.setText(dataBean.getTrade_history());
		/**
		 * 给图片设置tag显示
		 */
		if (dataBean.getLogo()!=null ){//图片做非空判断
			if ( !dataBean.getLogo().equals(holder.shopimage.getTag())){//只要tag不一致就说明是不同的位置
				holder.shopimage.setTag(dataBean.getLogo());//保存第一张图片地址作为tag标记
				ImageLoader.getInstance().displayImage(dataBean.getLogo(),holder.shopimage);
			}
		}
		
		return convertView;
	}

	public void addall(List<ShopCheckListBean.ListBean> list) {
		if (list!=null){
			this.list = list;
			notifyDataSetChanged();
		}
	}

	public static class ViewHolder {
		public View rootView;
		public ImageView shopimage;
		public TextView name;
		public TextView total_trade;
		public TextView month_trade;

		public ViewHolder(View rootView) {
			this.rootView = rootView;
			this.shopimage = (ImageView) rootView.findViewById(R.id.shopimage);
			this.name = (TextView) rootView.findViewById(R.id.name);
			this.total_trade = (TextView) rootView.findViewById(R.id.total_trade);
			this.month_trade = (TextView) rootView.findViewById(R.id.month_trade);
		}

	}
}
