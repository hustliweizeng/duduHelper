package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.javabean.ShopCheckListBean;
import com.dudu.duduhelper.javabean.ShopListBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * @author
 * @version 1.0
 * @date 2016/11/8
 */

public class CheckShopAdapter extends BaseAdapter {
	Context context;

	public CheckShopAdapter(Context context) {
		this.context = context;
	}
	int chechedPos = -1;
	List<ShopCheckListBean.ListBean> list = new ArrayList<>();

	public void addAll(List<ShopCheckListBean.ListBean> data) {
		if (data!=null &&data.size()>0){
			list = data;
			notifyDataSetChanged();
		}else {
			Toast.makeText(context,"没有门店信息",Toast.LENGTH_SHORT).show();
		}

	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public ShopCheckListBean.ListBean getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.shop_manager_item_check, null);
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
		if (dataBean.getLogo()!=null){//图片做非空判断
			if ( !dataBean.getLogo().equals(holder.shopimage.getTag())){//只要tag不一致就说明是不同的位置
				holder.shopimage.setTag(dataBean.getLogo());//保存第一张图片地址作为tag标记
				ImageLoader.getInstance().displayImage(dataBean.getLogo(),holder.shopimage);
			}
		}
		
		/**
		 * 设置按钮状态
		 */
		if (position == chechedPos){
			holder.iv_check.setImageResource(R.drawable.icon_xuanze_sel);
		}
		else {
			holder.iv_check.setImageResource(R.drawable.icon_xuanze);
		}
		return convertView;
	}

	/**
	 * 设置选中的条目
	 * @param position
	 */
	public void setCheckedShop(int position){
		if (position != chechedPos){
			chechedPos = position;//新选择的条目
		}else {
			//修改的条目
			chechedPos = -1;//设置为不选中
		}
		notifyDataSetChanged();
	}
	public String getCheckedId() {
		if (chechedPos != -1){
			return  list.get(chechedPos).getId();	//返回选中的id
		}else {
			return  null;
		}

		
	}

	public static class ViewHolder {
		public View rootView;
		public ImageView iv_check;
		public ImageView shopimage;
		public TextView name;
		public TextView total_trade;
		public TextView month_trade;
		private LinearLayout ll_check;

		public ViewHolder(View rootView) {
			this.rootView = rootView;
			this.iv_check = (ImageView) rootView.findViewById(R.id.iv_check);
			this.shopimage = (ImageView) rootView.findViewById(R.id.shopimage);
			this.name = (TextView) rootView.findViewById(R.id.name);
			this.total_trade = (TextView) rootView.findViewById(R.id.total_trade);
			this.month_trade = (TextView) rootView.findViewById(R.id.month_trade);
			this.ll_check = (LinearLayout) rootView.findViewById(R.id.ll_check);
			
		}

	}
}
