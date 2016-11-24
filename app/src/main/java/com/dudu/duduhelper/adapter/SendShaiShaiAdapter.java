package com.dudu.duduhelper.adapter;

import java.util.ArrayList;
import java.util.List;

import org.fireking.app.imagelib.entity.ImageBean;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import com.dudu.duduhelper.R;
public class SendShaiShaiAdapter extends BaseAdapter {
	private Context context;
	private ViewHolder viewHolder;
	private List<ImageBean> imagelist = new ArrayList<ImageBean>();
	private DisplayImageOptions options;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	public SendShaiShaiAdapter(Context context) 
	{
		this.context = context;
		options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.icon_head)
				.showImageForEmptyUri(R.drawable.icon_head)
				.showImageOnFail(R.drawable.icon_head).cacheInMemory(false)
				.cacheOnDisc(false).considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(100)).build();
	}

	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		if((this.imagelist.size() + 1)>=3)
		{
			return 3;
		}
		else
		{
			return (this.imagelist.size() + 1);
		}
		//return this.imagelist.size();
	}
	
	public List<ImageBean> getList()
	{
		return imagelist;
	}

	public void addAll(List<ImageBean> list) {
		this.imagelist.addAll(this.imagelist.size(), list);
		notifyDataSetChanged();
	}
	
	public void add(ImageBean item) {
		this.imagelist.add(item);
		notifyDataSetChanged();
	}
	
	public void clear() 
	{
		this.imagelist.clear();
		//notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<ImageBean> getAllItem() {
		// TODO Auto-generated method stub
		return imagelist;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		viewHolder = new ViewHolder();
		convertView = LayoutInflater.from(context).inflate(R.layout.activity_send_shai_shai_gallery_item, null);
		viewHolder.imageView = (ImageView) convertView.findViewById(R.id.shaishaiimageview);
		if (position == this.imagelist.size()) 
		{
			viewHolder.imageView.setImageResource(R.drawable.icon_cammer);
		}
		else 
		{
			imageLoader.displayImage("file://" + imagelist.get(position).path,viewHolder.imageView);
		}
		imageLoader.displayImage(imagelist.get(position).path,viewHolder.imageView);
		return convertView;
	}

	private class ViewHolder 
	{
		ImageView imageView;
	}

}
