package com.dudu.helper3.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.helper3.R;
import com.dudu.helper3.javabean.ShopListBean;
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

	List<ShopListBean.DataBean> list = new ArrayList<>();
	HashMap<Integer,Boolean> checkList = new HashMap<>();
	


	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public ShopListBean.DataBean getItem(int position) {
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
		ShopListBean.DataBean dataBean = list.get(position);
		holder.name.setText(dataBean.getName());
		holder.month_trade.setText(dataBean.getTrade_month());
		holder.total_trade.setText(dataBean.getTrade_history());
		/**
		 * 给图片设置tag显示
		 */
		if (dataBean.getImages()!=null && dataBean.getImages().size()>0){//图片做非空判断
			if ( !holder.shopimage.getTag().equals(dataBean.getImages().get(0))){//只要tag不一致就说明是不同的位置
				holder.shopimage.setTag(dataBean.getImages().get(0));//保存第一张图片地址作为tag标记
				ImageLoader.getInstance().displayImage(dataBean.getImages().get(0),holder.shopimage);
			}
			
		}
		//设定条目点击时间
		holder.ll_check.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (checkList.size()>0 ){
					if (checkList.containsKey(position)){//修改状态
						checkList.put(position,false);
					}else {//切换门店
						checkList.clear();//每次选择前清空数据
						checkList.put(position,true);//把该位置添加到集合中
					}
				}else {
					checkList.put(position,true);//把该位置添加到集合中
				}
				notifyDataSetChanged();
			}
		});
		/**
		 * 设置按钮
		 */
		if (checkList.get(position)){
			holder.iv_check.setImageResource(R.drawable.icon_xuanze_sel);
		}else {
			holder.iv_check.setImageResource(R.drawable.icon_xuanze);
		}
		
		


		return null;
	}

	public void addAll(List<ShopListBean.DataBean> data) {
		if (data!=null &&data.size()>0){
			list = data;
			notifyDataSetChanged();
		}
		Toast.makeText(context,"没有门店信息",Toast.LENGTH_SHORT).show();
		
	}

	public String getCheckedId() {
		String itemId = null;
		for (Integer pos :checkList.keySet()){
			itemId = list.get(pos).getId();
			if (!TextUtils.isEmpty(itemId)) {
				break;
			}
		}
		return  itemId;	
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
