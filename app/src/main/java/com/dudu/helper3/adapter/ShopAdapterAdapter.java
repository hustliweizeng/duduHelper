package com.dudu.helper3.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.javabean.ShopListBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ShopAdapterAdapter extends BaseAdapter 
{
	private Context context;
    private ViewHolder viewHolder;
    List<ShopListBean.DataBean> list=new ArrayList<>();
    public ShopAdapterAdapter(Context context)
	{
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return list.size();
	}
	public void clear()
    {
    	list.clear();
    	notifyDataSetChanged();
    }
    public void addAll(List<ShopListBean.DataBean> list)
    {
	    if (list!=null){
		    this.list.addAll(this.list.size(), list);
		    notifyDataSetChanged();
	    }
    }

	@Override
	public ShopListBean.DataBean getItem(int arg0) 
	{
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		if(convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = View.inflate(context,R.layout.shop_manager_item, null);
			viewHolder.shopimage = (ImageView) convertView.findViewById(R.id.shopimage);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.address = (TextView) convertView.findViewById(R.id.shoplocationtext);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		
		//设置数据
		ShopListBean.DataBean dataBean = list.get(position);
		LogUtil.d("daa",dataBean.getName());
		viewHolder.address.setText(dataBean.getAddress());
		viewHolder.name.setText(dataBean.getName());
		if (dataBean.getImages()!=null && dataBean.getImages().length>0){
			//显示图片之前检查大小
			ImageLoader.getInstance().displayImage(dataBean.getImages()[0],viewHolder.shopimage);
		}
		return convertView;
	}
	private class ViewHolder
	{
		private ImageView shopimage;
		private TextView address;
		private TextView name;
		
	}
}
