package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.javabean.ShopListBean;

import java.util.ArrayList;
import java.util.List;
import com.dudu.duduhelper.R;
/**
 * @author
 * @version 1.0
 * @date 2016/9/26
 */

public class ShopListSelectAdapter extends  BaseAdapter {
	private Context context;
	private  int layout;
	private List<ShopListBean.DataBean> list = new ArrayList<>();
	private List<String> checkedList = new ArrayList<>();//已经选中的位置集合
	private List<String> checkedIds  = new ArrayList<>();//已经选中的位置集合
	private boolean isFromId;//是否根据id去判断是否选中条目

	public ShopListSelectAdapter(Context context, int layout)
	{
		this.context = context;
		this.layout = layout;
	}
	public void addAll(List<ShopListBean.DataBean> list)
	{
		if (list!=null && ! list.toString().equals(this.list.toString())){
			this.list.addAll(list);
			notifyDataSetChanged();
		}
	}
	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public ShopListBean.DataBean getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Long.parseLong(list.get(position).getId());
	}

	/**
	 * 返回id在集合中的位置
	 * @param id
	 * @return
	 */
	public int getItemPos(String id){
		for (ShopListBean.DataBean item:list){
			if (item.getId().equals(id)){
				return list.indexOf(id);
			}else {
				return -1;
			}
		}
		return  -1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		if (convertView == null){
			convertView = View.inflate(context, layout,null);
		}
		TextView tv_title = (TextView) convertView.findViewById(R.id.tv_title_category);
		ImageView iv_check  = (ImageView) convertView.findViewById(R.id.iv_check);
		//手动设置条目高度，否则无效
		tv_title.setHeight(Util.dip2px(context,50));
		tv_title.setText(list.get(position).getName());
		/**
		 * 判断复选框
		 */
		if(isFromId){
			String id = list.get(position).getId();
			//通过id判断复选框
			if (checkedIds.contains(id)){
				iv_check.setImageResource(R.drawable.icon_xuanze_sel);
				LogUtil.d("init","ok");
			}else {
				iv_check.setImageResource(R.drawable.icon_xuanze);
				LogUtil.d("init","fail");
			}
			
		}else {
			//通过位置判断复选框
			if (checkedList.contains(position+"")){
				iv_check.setImageResource(R.drawable.icon_xuanze_sel);
				LogUtil.d("init","ok");
			}else {
				iv_check.setImageResource(R.drawable.icon_xuanze);
				LogUtil.d("init","fail");
			}
		}
		
		return convertView;
	}
	public void clear() {
		list.clear();
		notifyDataSetChanged();
	}

	/**
	 * 设置选中的位置
	 * @param position
	 */
	public void setcheckedId(String position) {
		if (checkedList.contains(position)){
			checkedList.remove(position);
		}else {
			checkedList.add(position);
		}
		notifyDataSetChanged();
	}

	public void addCheckedIds(List<ShopListBean.DataBean> list ,List<String> checkedIds,boolean isFromId) {
		addAll(list);
		this.checkedIds = checkedIds;
		this.isFromId = isFromId;
		notifyDataSetChanged();
	}
}
