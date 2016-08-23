package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.bean.ImageBean;
import com.dudu.duduhelper.widget.SmoothCheckBox;
import com.dudu.duduhelper.widget.SmoothCheckBox.OnCheckedChangeListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

public class ShopImageAdapter extends BaseAdapter
{
	
    private Context context;
    //默认不显示
    private boolean isVisable=false;
    private OnSelectImageClickListener onSelectImageClickListener;
	//数据源集合
    private List<ImageBean> imageList = new ArrayList<ImageBean>();
	//获取UIL实例
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    public List<Integer> listDelect = new ArrayList<Integer>();//checkbox选中状态
    //private List<HashMap<, boolean> listDelect = new ArrayList<Integer>();
	public ShopImageAdapter(Context context) 
	{
		this.context = context;
	}

	public List<ImageBean> getImageList(){
		return imageList;
	}
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return imageList.size()+1;
	}

	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return 0;
	}
	

	public void addAll(List<ImageBean> imageList) 
	{
		this.imageList.addAll(this.imageList.size(), imageList);
	}
	
	public void add(ImageBean image) 
	{
		this.imageList.add(image);
		notifyDataSetChanged();
	}
	public void delectSelectImageList()
	{
		for (Integer postion : listDelect) 
		{
			imageList.remove((int)postion);
		}
		listDelect.clear();
		notifyDataSetChanged();
	}
	public void setCheckVisableOffOn()
	{
		if(isVisable)
		{
			isVisable = false;
		}
		else
		{
			isVisable = true;
		}
		notifyDataSetChanged();
	}
	
	
	@Override
	//显示图片通过UIL加载
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		//加载图片显示item
		convertView = LayoutInflater.from(context).inflate(R.layout.shop_imagebrower_item, null);
		ImageView imagephoto = (ImageView) convertView.findViewById(R.id.imagephoto);
		SmoothCheckBox photocheckbox = (SmoothCheckBox) convertView.findViewById(R.id.photocheckbox);
		//复选框设置监听
		photocheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() 
		{
			@Override
			public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) 
			{
				// TODO Auto-generated method stub
				if(isChecked)
				{
					listDelect.add(Integer.valueOf(position));
				}
				else
				{
					listDelect.remove(Integer.valueOf(position));
				}
			}
		});
		//最后一张图片设置成相机
		if (position == this.imageList.size()) 
		{
			//设置照相机的监听事件，通过接口回调
			imagephoto.setImageResource(R.drawable.icon_cammer);
			imagephoto.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					// 触发自定义的监听事件，需要重写该接口
					onSelectImageClickListener.onSelectClick();
				}
			});
		}
		else
		{
			//设置编辑状态
			if(isVisable)
			{
				photocheckbox.setVisibility(View.VISIBLE);
			}
			else
			{
				photocheckbox.setVisibility(View.GONE);
			}
			//判断是本地图片还是网络图片
			if(!TextUtils.isEmpty(imageList.get(position).getPath()))
			{
				//如果是本地图片，直接加载
				imageLoader.displayImage(imageList.get(position).getPath(),imagephoto);
			}
			else
			{
				//如果是网络图片，

				imagephoto.setImageURI(imageList.get(position).getImageUri());
			}
			//为图片设置点击事件
			imagephoto.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					
				}
			});
		}
		return convertView;
	}
	public void setOnSelectImageClickListener(OnSelectImageClickListener onSelectImageClickListener)
	{
		this.onSelectImageClickListener = onSelectImageClickListener;
	}
	
	public interface OnSelectImageClickListener
	{
		void onSelectClick();
	}
}

