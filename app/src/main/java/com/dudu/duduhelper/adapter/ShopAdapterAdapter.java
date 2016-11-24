package com.dudu.duduhelper.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dudu.duduhelper.javabean.ShopListBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.dudu.duduhelper.R;
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
			convertView = View.inflate(context, R.layout.shop_manager_item, null);
			viewHolder.shopimage = (ImageView) convertView.findViewById(R.id.shopimage);
			viewHolder.name = (TextView) convertView.findViewById(R.id.name);
			viewHolder.totalTrade  = (TextView) convertView.findViewById(R.id.total_trade);
			viewHolder.month_trade  = (TextView) convertView.findViewById(R.id.month_trade);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//设置数据
		ShopListBean.DataBean dataBean = list.get(position);
		viewHolder.totalTrade.setText(" ￥"+dataBean.getTrade_history());
		viewHolder.month_trade.setText(" ￥"+dataBean.getTrade_month());
		viewHolder.name.setText(dataBean.getName());
		String imgUrl;
		if (dataBean.getImages()!=null && dataBean.getImages().size()>0){
			imgUrl = dataBean.getImages().get(0);//获取之前一顶要做非空判断
			viewHolder.shopimage.setTag(imgUrl);
			//显示图片之前检查大小
			if (viewHolder.shopimage.getTag()!=null && viewHolder.shopimage.getTag().equals(imgUrl)){
				ImageLoader.getInstance().displayImage(dataBean.getImages().get(0),viewHolder.shopimage);
			}
		}
		return convertView;
	}
	private class ViewHolder
	{
		private ImageView shopimage;
		private TextView name;
		private TextView totalTrade;
		public TextView month_trade;
	}
}
