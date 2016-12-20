package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.widget.SmoothCheckBox;
import com.dudu.duduhelper.widget.SmoothCheckBox.OnCheckedChangeListener;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.dudu.duduhelper.R;
public class ShopImageAdapter extends BaseAdapter
{
	
    private Context context;
    //默认不显示
    private boolean isVisable=false;
    private OnSelectImageClickListener onSelectImageClickListener;
	//数据源集合,包含所有网址的集合
    private ArrayList<String> imageList = new ArrayList< >();
	//获取UIL实例
    protected ImageLoader imageLoader = ImageLoader.getInstance();
	//保存要删除的条目位置
    public List<Integer> listDelect = new ArrayList<Integer>();//checkbox选中状态
	public ShopImageAdapter(Context context) 
	{
		this.context = context;
	}

	public ArrayList<String> getImageList(){
		return imageList;
	}
	@Override
	public int getCount() 
	{
		// 多出来一张照片，最后一张是点击用的照片，不必上传
		return imageList.size()+1;
	}

	@Override
	public Object getItem(int position) 
	{
		return null;
	}

	@Override
	public long getItemId(int position) 
	{
		return 0;
	}
	

	public void addAll(List<String> imageList)
	{
		this.imageList.addAll(this.imageList.size(), imageList);
	}
	
	public void add(String image)
	{
		this.imageList.add(image);
		notifyDataSetChanged();
	}
	public void delectSelectImageList()
	{
		/**
		 * 遍历选中的要删除的条目，按照降序重新排列以后再删除
		 */
		Collections.reverse(listDelect);//降序排列后不会报错
		for (Integer postion : listDelect) 
		{
		LogUtil.d("DEL","item="+postion);
			//删除指定位置的条目，每次删除以后，位置会发生变化，但是删除集合中存的位置是无序的
			imageList.remove((int) postion);
		}
		//清空复选框集合
		listDelect.clear();
		notifyDataSetChanged();//刷新数据
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
				if(isChecked)
				{
					if (!listDelect.contains(Integer.valueOf(position))){
						//获取被选中的位置
						listDelect.add(Integer.valueOf(position));
						LogUtil.d("checkbox+",position+"");
					}
				}
				else
				{
					if (listDelect.contains(Integer.valueOf(position))){
						//移除当前条目
						listDelect.remove(Integer.valueOf(position));
						LogUtil.d("checkbox-",position+"");
					}

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
			String path = this.imageList.get(position);
			//判断路径是否为空
			if (!TextUtils.isEmpty(path)){

				if(path.startsWith("http")){
					//如果是网络图片用imageloader加载
					imageLoader.displayImage(path,imagephoto);
				}else {
					//如果是本地图片，直接加载
					LogUtil.d("adapter",path);
					imagephoto.setImageBitmap(BitmapFactory.decodeFile(path));
					//通过uri设置图片
				}
			}

			//判断复选框是否可见
			if(isVisable)
			{
				photocheckbox.setVisibility(View.VISIBLE);
			}
			else
			{
				photocheckbox.setVisibility(View.GONE);
			}
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

