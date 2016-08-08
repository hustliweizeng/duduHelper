package com.dudu.duduhelper.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


import com.dudu.duduhelper.R;
import com.dudu.duduhelper.bean.ImageBean;
import com.dudu.duduhelper.widget.SmoothCheckBox;
import com.dudu.duduhelper.widget.SmoothCheckBox.OnCheckedChangeListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

public class ShopImageAdapter extends BaseAdapter
{
	
    private Context context;
    //默认不显示
    private boolean isVisable=false;
    private OnSelectImageClickListener onSelectImageClickListener;
    private List<ImageBean> imageList = new ArrayList<ImageBean>();
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    public List<Integer> listDelect = new ArrayList<Integer>();//checkbox选中状态
    //private List<HashMap<, boolean> listDelect = new ArrayList<Integer>();
	public ShopImageAdapter(Context context) 
	{
		this.context = context;
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
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(R.layout.shop_imagebrower_item, null);
		ImageView imagephoto = (ImageView) convertView.findViewById(R.id.imagephoto);
		SmoothCheckBox photocheckbox = (SmoothCheckBox) convertView.findViewById(R.id.photocheckbox);
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
		if (position == this.imageList.size()) 
		{
			imagephoto.setImageResource(R.drawable.icon_cammer);
			imagephoto.setOnClickListener(new OnClickListener() 
			{
				
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					onSelectImageClickListener.onSelectClick();
				}
			});
		}
		else
		{
			if(isVisable)
			{
				photocheckbox.setVisibility(View.VISIBLE);
			}
			else
			{
				photocheckbox.setVisibility(View.GONE);
			}
			if(!TextUtils.isEmpty(imageList.get(position).getPath()))
			{
				imageLoader.displayImage(imageList.get(position).getPath(),imagephoto);
			}
			else
			{
				imagephoto.setImageURI(imageList.get(position).getImageUri());
			}
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

