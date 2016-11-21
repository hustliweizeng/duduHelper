package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.javabean.CheckableShopbean;
import com.dudu.duduhelper.javabean.ShopListBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import retrofit2.http.POST;

/**
 * @author
 * @version 1.0
 * @date 2016/10/26
 */
public class BigBugShopSelectAdapter extends BaseAdapter {
	List<CheckableShopbean.DataBean> list = new ArrayList<>();
	ArrayList<String> checkedIds = new ArrayList<>();
	List<String> checkedPos = new ArrayList<>();
	public BigBugShopSelectAdapter(Context context){
		this.context = context;
	}
	public void addAll(List<CheckableShopbean.DataBean>  list){
		if (list!=null &&list.size()>0){
			this.list = list;
			LogUtil.d("list",list.size()+"");
			notifyDataSetChanged();
		}
	}
	private Context context;

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
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = View.inflate(context, R.layout.item_select_shop, null);
			holder = new ViewHolder(convertView);
			convertView.setTag(holder);
		}else {
			holder = (ViewHolder) convertView.getTag();
		}
		LogUtil.d("new","ssssa");
		CheckableShopbean.DataBean dataBean = list.get(position);
		holder.name.setText(dataBean.getName());
		holder.address.setText(dataBean.getAddress());
		String image = dataBean.getCover();
		if (image!=null ){
			if (holder.shopimage.getTag()!=null){
				if ((int)holder.shopimage.getTag() != position){//条目复用时，核对tag不一致说明是不同的条码，就可以设置图片了
					holder.shopimage.setTag(position);
					ImageLoader.getInstance().displayImage(image,holder.shopimage);
				}
			}
		}
		holder.ll_check.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//条目点击后需要获取他的id
				String itemId = getItemId(position)+"";
				if (!checkedIds.contains(itemId)){
					checkedPos.add(position+"");
					checkedIds.add(itemId);
					LogUtil.d("add",position+"");
				}else {
					checkedPos.remove(position+"");
					checkedIds.remove(itemId);
					LogUtil.d("remove",position+"");
				}
				notifyDataSetChanged();//选择完毕后刷新数据
			}
		});
		//根据集合中的数据刷新复选框、
		if(checkedPos.contains(position+"")){
			holder.iv_check.setImageResource(R.drawable.select_check);
		}else {
			holder.iv_check.setImageResource(R.drawable.select_empty);
		}
		return convertView;
	}

	public ArrayList<String> getCheckedList() {
		return checkedIds;
	}

	public void checkAllIds() {
		checkedPos.clear();
		checkedIds.clear();
		int i =0;
		for (CheckableShopbean.DataBean item: list){
			checkedIds.add(item.getId());//添加id
			checkedPos.add(i+"");//添加位置
			i++;
		}
		notifyDataSetChanged();
	}

	public void removeAll() {
		checkedIds.clear();
		checkedPos.clear();
		notifyDataSetChanged();
	}

	public void setCheckedIds(ArrayList<String> data) {
		if(data!=null &&data.size()>0){
			checkedIds = data;
			for (int i = 0;i<list.size() ;i++){
				if (data.contains(list.get(i).getId())){
					checkedPos.add(i+"");
				}
			}
			notifyDataSetChanged();
		}
	}

	public static class ViewHolder {
		public View rootView;
		public ImageView iv_check;
		public ImageView shopimage;
		public TextView name;
		public TextView address;
		public LinearLayout ll_check;

		public ViewHolder(View rootView) {
			this.rootView = rootView;
			this.iv_check = (ImageView) rootView.findViewById(R.id.iv_check);
			this.shopimage = (ImageView) rootView.findViewById(R.id.shopimage);
			this.name = (TextView) rootView.findViewById(R.id.name);
			this.address = (TextView) rootView.findViewById(R.id.address);
			this.ll_check = (LinearLayout) rootView.findViewById(R.id.ll_check);
			
		}

	}
}
