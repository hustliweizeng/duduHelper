package com.dudu.duduhelper.adapter;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.Header;

import com.dudu.duduhelper.shopProductListActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.bean.HongbaoListBean;
import com.dudu.duduhelper.bean.ProductListBean;
import com.dudu.duduhelper.bean.ResponsBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProductAdapter extends BaseAdapter 
{
	private Context context;
    private ViewHolder viewHolder;
    private DisplayImageOptions options;
    private boolean isHongbao=false;//是否是红包
    private boolean isMulChoice;//是否显示批量删除
    public  HashMap<Integer, Integer> visiblecheck ;//用来记录是否显示checkBox
    public  HashMap<Integer, Boolean> ischeck;//checkbox选中状态
    public List<String> selectid = new ArrayList<String>();//保存选中的
    public List<ProductListBean> list=new ArrayList<ProductListBean>();
    public List<HongbaoListBean> hongBaolist=new ArrayList<HongbaoListBean>();
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private boolean isCash=false;
    //初始化listview的checkbox
    public ProductAdapter(Context context,boolean isMulChoice,boolean isCash,boolean isHongbao)
    {
    	this.isCash=isCash;
    	this.isHongbao=isHongbao;
    	this.context=context;
    	options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.icon_head)
		.showImageForEmptyUri(R.drawable.icon_head)
		.showImageOnFail(R.drawable.icon_head)
		.cacheInMemory(true).considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(100)).build();
    	this.isMulChoice=isMulChoice;
    	visiblecheck = new HashMap<Integer, Integer>();
    	ischeck = new HashMap<Integer, Boolean>();
    	
    }
    
    public void clear()
    {
    	if(isHongbao)
    	{
    		hongBaolist.clear();
    	}
    	else
    	{
    		list.clear();
    	}
    	
    }
    
//    public void removeAll(List<Integer> ids)
//    {
//    	list.removeAll(ids);
//    	notifyDataSetChanged();
//    }
    
    public void addAll(List<ProductListBean> list,boolean isChoose)
    {
    	this.list.addAll(this.list.size(), list);
    	if(isChoose)
    	{
    		//设置所有checkbox可见
            for(int i=0;i<getCount();i++)
            {
            	if(ischeck.size()-1>i)
            	{
            		if(ischeck.get(i))
                	{
                		ischeck.put(i, true);
                	}
            	}
            	else
            	{
            		ischeck.put(i, false);
            	}
            	
                visiblecheck.put(i, CheckBox.VISIBLE);
            }
        }
        else
        {
            for(int i=0;i<getCount();i++)
            {
            	//设置所有checkbox隐藏
                ischeck.put(i, false);
                visiblecheck.put(i, CheckBox.GONE);
            }
        }
    	notifyDataSetChanged();
    }
    
    public void addHongbaoAll(List<HongbaoListBean> list,boolean isChoose)
    {
    	this.hongBaolist.addAll(this.hongBaolist.size(), list);
    	if(isChoose)
    	{
    		//设置所有checkbox可见
            for(int i=0;i<getCount();i++)
            {
            	if(ischeck.size()-1>i)
            	{
            		if(ischeck.get(i))
                	{
                		ischeck.put(i, true);
                	}
            	}
            	else
            	{
            		ischeck.put(i, false);
            	}
            	
                visiblecheck.put(i, CheckBox.VISIBLE);
            }
        }
        else
        {
            for(int i=0;i<getCount();i++)
            {
            	//设置所有checkbox隐藏
                ischeck.put(i, false);
                visiblecheck.put(i, CheckBox.GONE);
            }
        }
    	notifyDataSetChanged();
    }
    
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		if(isHongbao)
		{
			return hongBaolist.size();
		}
		else
		{
			return list.size();
		}
		
	}

	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		if(isHongbao)
		{
			return hongBaolist.get(position);
		}
		else
		{
			return list.get(position);
		}
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		if(isHongbao)
		{
			return hongBaolist.size();
		}
		else
		{
			return list.size();
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		OnClick listener=null;
		OnClick2 listener2=null;
		if(convertView==null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.shop_fragment_product_item, null);
			viewHolder.productCheckImg=(ImageView) convertView.findViewById(R.id.productCheckImg);
			viewHolder.productRelPriceTextView=(TextView) convertView.findViewById(R.id.productRelPriceTextView);
			viewHolder.productRelPriceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
			viewHolder.productName=(TextView) convertView.findViewById(R.id.productName);
			viewHolder.producthaveNum=(TextView) convertView.findViewById(R.id.producthaveNum);
			viewHolder.productSellNum=(TextView) convertView.findViewById(R.id.productSellNum);
			viewHolder.productPrice=(TextView) convertView.findViewById(R.id.productPrice);
			viewHolder.productAction=(TextView) convertView.findViewById(R.id.productAction);
			viewHolder.productImage=(ImageView) convertView.findViewById(R.id.productImage);
			viewHolder.downButton=(ImageView) convertView.findViewById(R.id.downButton);
			viewHolder.productGetNum=(TextView) convertView.findViewById(R.id.productGetNum);
			if(isHongbao)
			{
				viewHolder.downButton.setVisibility(View.GONE);
				viewHolder.productGetNum.setVisibility(View.VISIBLE);
				viewHolder.productRelPriceTextView.setVisibility(View.GONE);
				viewHolder.productPrice.setVisibility(View.GONE);
			}
			viewHolder.delectButton=(Button) convertView.findViewById(R.id.delectButton);
			listener=new OnClick();
			listener2=new OnClick2();
			viewHolder.delectButton.setOnClickListener(listener);
			viewHolder.downButton.setOnClickListener(listener2);
			convertView.setTag(viewHolder.delectButton.getId(), listener);
			convertView.setTag(viewHolder.downButton.getId(), listener2);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
			listener=(OnClick) convertView.getTag(viewHolder.delectButton.getId());
			listener2=(OnClick2) convertView.getTag(viewHolder.downButton.getId());
		}
		if(isHongbao)
		{
			if(hongBaolist.size()!=0)
			{
				if(!TextUtils.isEmpty(hongBaolist.get(position).getId()))
				{
					listener.setpositionAndId(hongBaolist.get(position).getId(),position,ConstantParamPhone.DELECT_HONGBAO);
					//listener2.setpositionAndId(hongBaolist.get(position).getId(),hongBaolist.get(position).getStatus(),position,ConstantParamPhone.EDIT_COUPON_INFO);
				}
//				if(!TextUtils.isEmpty(hongBaolist.get(position).getPrice2()))
//				{
//					viewHolder.productRelPriceTextView.setText(hongBaolist.get(position).getPrice2());
//				}
//				if(!TextUtils.isEmpty(hongBaolist.get(position).getPrice1()))
//				{
//					viewHolder.productPrice.setText("￥"+hongBaolist.get(position).getPrice1());
//				}
				if(!TextUtils.isEmpty(hongBaolist.get(position).getSend_num())&&!TextUtils.isEmpty(hongBaolist.get(position).getNum()))
				{
					viewHolder.productGetNum.setText("已领："+hongBaolist.get(position).getSend_num()+"/"+hongBaolist.get(position).getNum());
				}
				if(!TextUtils.isEmpty(hongBaolist.get(position).getTitle()))
				{
					viewHolder.productName.setText(hongBaolist.get(position).getTitle());
				}
				if(!TextUtils.isEmpty(hongBaolist.get(position).getTotal()))
				{
					viewHolder.producthaveNum.setText("总额:"+hongBaolist.get(position).getTotal());
				}
				if(!TextUtils.isEmpty(hongBaolist.get(position).getTotal())&&!TextUtils.isEmpty(hongBaolist.get(position).getSend_money()))
				{
					viewHolder.productSellNum.setText("余额:"+(Double.parseDouble(hongBaolist.get(position).getTotal())-Double.parseDouble(hongBaolist.get(position).getSend_money())));
				}
				if(!TextUtils.isEmpty(hongBaolist.get(position).getTime_end()))
				{
					if(System.currentTimeMillis()>Long.parseLong(hongBaolist.get(position).getTime_end())*1000)
					{
					   viewHolder.productAction.setText("已截止");
					   viewHolder.productAction.setTextColor(viewHolder.productAction.getResources().getColor(R.color.text_color_light));
					}
					else
					{
						viewHolder.productAction.setText("发放中");
					}
				}
				if(!TextUtils.isEmpty(hongBaolist.get(position).getLogo()))
				{
					String imagepath = hongBaolist.get(position).getLogo();
					if(!imagepath.equals(viewHolder.productImage.getTag()))
					{ 
						viewHolder.productImage.setTag(imagepath); 
						imageLoader.displayImage(hongBaolist.get(position).getLogo(),viewHolder.productImage, options);
					}
				}
				
				//迭代设置所有checkbox是否选中，做全选，清空重置使用
				/*
				 * 在复用时对其进行判断，根据其状态来显示相应的内容，这样在滑动时条目就不会再错乱了
				 */
				if(ischeck.get(position))
				{
					viewHolder.productCheckImg.setImageResource(R.drawable.icon_xuanze_sel);
				}
				else 
				{
					viewHolder.productCheckImg.setImageResource(R.drawable.icon_xuanze);
				}
				viewHolder.productCheckImg.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub
		                if(ischeck.get(position))
		                {
		                	viewHolder.productCheckImg.setImageResource(R.drawable.icon_dingdan);
		                	//设置点击后其选中状态，不选中
		                	ischeck.put(position,false);
		                	//记录是否需要删除的数据id
		                	selectid.remove(hongBaolist.get(position).getId());
		                	
		                	
		                }
		                else
		                {
		                	viewHolder.productCheckImg.setImageResource(R.drawable.icon_guanyu);
		                	
		                	ischeck.put(position,true);
		                	
		                	//记录是否需要删除的数据id
		                	selectid.add(hongBaolist.get(position).getId());
		                }
		                notifyDataSetChanged();
					}
				});
				//迭代设置所有checkbox的可见性
				viewHolder.productCheckImg.setVisibility(visiblecheck.get(position));
			}
		}
		else
		{
			if(list.size()!=0)
			{
				if(!TextUtils.isEmpty(list.get(position).getId()))
				{
					if(isCash)
					{
						listener.setpositionAndId(list.get(position).getId(),position,ConstantParamPhone.DELECT_COUPON);
						listener2.setpositionAndId(list.get(position).getId(),list.get(position).getStatus(),position,ConstantParamPhone.EDIT_COUPON_INFO);
					}
					else
					{
						listener.setpositionAndId(list.get(position).getId(),position,ConstantParamPhone.DELECT_PRODUCT);
						listener2.setpositionAndId(list.get(position).getId(),list.get(position).getStatus(),position,ConstantParamPhone.EDIT_PRODUCT_INFO);
					}
					
				}
				if(!TextUtils.isEmpty(list.get(position).getPrice2()))
				{
					viewHolder.productRelPriceTextView.setText(list.get(position).getPrice2());
				}
				if(!TextUtils.isEmpty(list.get(position).getPrice1()))
				{
					viewHolder.productPrice.setText("￥"+list.get(position).getPrice1());
				}
				if(!TextUtils.isEmpty(list.get(position).getSubject()))
				{
					viewHolder.productName.setText(list.get(position).getSubject());
				}
				if(!TextUtils.isEmpty(list.get(position).getStock()))
				{
					viewHolder.producthaveNum.setText("库存:"+list.get(position).getStock());
				}
				if(!TextUtils.isEmpty(list.get(position).getSold()))
				{
					viewHolder.productSellNum.setText("已售:"+list.get(position).getSold());
				}
				if(list.get(position).getStatus().equals("1"))
				{
	//				if(isCash)
	//				{
	//					viewHolder.downButton.setVisibility(View.GONE);
	//				}
					viewHolder.productAction.setText("未上架");
					viewHolder.downButton.setImageResource(R.drawable.icon_shangjia);
				}
				if(list.get(position).getStatus().equals("2"))
				{
	//				if(isCash)
	//				{
	//					viewHolder.downButton.setVisibility(View.GONE);
	//				}
					viewHolder.productAction.setText("已上架");
					viewHolder.downButton.setImageResource(R.drawable.icon_xiajia);
					
				}
				if(!TextUtils.isEmpty(list.get(position).getPic()))
				{
					String imagepath = list.get(position).getPic();
					if(!imagepath.equals(viewHolder.productImage.getTag()))
					{ 
						viewHolder.productImage.setTag(imagepath); 
						imageLoader.displayImage(list.get(position).getPic(),viewHolder.productImage, options);
					}
				}
				
				
				
				//迭代设置所有checkbox是否选中，做全选，清空重置使用
				/*
				 * 在复用时对其进行判断，根据其状态来显示相应的内容，这样在滑动时条目就不会再错乱了
				 */
				if(ischeck.get(position))
				{
					viewHolder.productCheckImg.setImageResource(R.drawable.icon_xuanze_sel);
				}
				else 
				{
					viewHolder.productCheckImg.setImageResource(R.drawable.icon_xuanze);
				}
				viewHolder.productCheckImg.setOnClickListener(new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub
		                if(ischeck.get(position))
		                {
		                	viewHolder.productCheckImg.setImageResource(R.drawable.icon_dingdan);
		                	//设置点击后其选中状态，不选中
		                	ischeck.put(position,false);
		                	//记录是否需要删除的数据id
		                	selectid.remove(list.get(position).getId());
		                	
		                	
		                }
		                else
		                {
		                	viewHolder.productCheckImg.setImageResource(R.drawable.icon_guanyu);
		                	
		                	ischeck.put(position,true);
		                	
		                	//记录是否需要删除的数据id
		                	selectid.add(list.get(position).getId());
		                }
		                notifyDataSetChanged();
					}
				});
				//迭代设置所有checkbox的可见性
				viewHolder.productCheckImg.setVisibility(visiblecheck.get(position));
			}
		}
		return convertView;
	}
	private class ViewHolder
	{
		ImageView productCheckImg;
		ImageView productImage;
		TextView productRelPriceTextView;//tv.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); 真实价格
		TextView productName;//名称
		TextView producthaveNum;//库存
		TextView productSellNum;//销售量
		TextView productPrice;//售价
		TextView productAction;//商品状态
		Button delectButton;
		ImageView downButton;
		TextView productGetNum;
	}
	//商品删除事件
	private class OnClick implements OnClickListener
	{
		//ConstantParamPhone.DELECT_PRODUCT
        String id;
        int postion;
        String methord;
        public void setpositionAndId(String id,int postion,String methord)
        {
        	this.id=id;
        	this.postion=postion;
        	this.methord=methord;
        }
		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			
			ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
			RequestParams params = new RequestParams();
			params.add("token", ((shopProductListActivity)context).share.getString("token", ""));
			params.add("category",((shopProductListActivity)context).category);
			params.add("id",id);
			params.add("version", ConstantParamPhone.VERSION);
			params.setContentEncoding("UTF-8");
			AsyncHttpClient client = new AsyncHttpClient();
			//保存cookie，自动保存到了shareprefercece  
	        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);    
	        client.setCookieStore(myCookieStore); 
	        if(isHongbao)
			{
	        	client.post(ConstantParamPhone.IP+methord, params,new TextHttpResponseHandler()
				{

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
					{
						Toast.makeText(context, "网络不给力呀", Toast.LENGTH_LONG).show();
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) 
					{
						ResponsBean responsBean=new Gson().fromJson(arg2,ResponsBean.class);
						if(!responsBean.getStatus().equals("0"))
						{
							Toast.makeText(context, "出错啦！", Toast.LENGTH_LONG).show();
						}
						else
						{
							Toast.makeText(context, "删除成功啦！", Toast.LENGTH_LONG).show();
						}
					}
					@Override
					public void onFinish() 
					{
						// TODO Auto-generated method stub
						ColorDialog.dissmissProcessDialog();
						if(isHongbao)
						{
						   hongBaolist.remove(postion);
						}
						else
						{
						   list.remove(postion);
						}
						notifyDataSetChanged();
					}
				});
			}
	        else
	        {
	        	client.get(ConstantParamPhone.IP+methord, params,new TextHttpResponseHandler()
				{

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
					{
						Toast.makeText(context, "网络不给力呀", Toast.LENGTH_LONG).show();
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) 
					{
						ResponsBean responsBean=new Gson().fromJson(arg2,ResponsBean.class);
						if(!responsBean.getStatus().equals("1"))
						{
							Toast.makeText(context, "出错啦！", Toast.LENGTH_LONG).show();
						}
						else
						{
							Toast.makeText(context, "删除成功啦！", Toast.LENGTH_LONG).show();
							
						}
					}
					@Override
					public void onFinish() 
					{
						// TODO Auto-generated method stub
						ColorDialog.dissmissProcessDialog();
						if(isHongbao)
						{
						   hongBaolist.remove(postion);
						}
						else
						{
						   list.remove(postion);
						}
						notifyDataSetChanged();
					}
				});
	        }
	        
		}
	}
	//商品上架
		private class OnClick2 implements OnClickListener
		{
			//ConstantParamPhone.EDIT_PRODUCT_INFO
	        String id;
	        String status;
	        int postion;
	        String methord;
	        public void setpositionAndId(String id,String status,int postion,String methord)
	        {
	        	this.id=id;
	        	this.postion=postion;
	        	this.status=status;
	        	this.methord=methord;
	        }
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
				RequestParams params = new RequestParams();
				params.add("token", ((shopProductListActivity)context).share.getString("token", ""));
				params.add("category",((shopProductListActivity)context).category);
				params.add("id",id);
				params.add("version", ConstantParamPhone.VERSION);
				if(status.equals("1"))
				{
					params.add("status","2");
				}
				if(status.equals("2"))
				{
					params.add("status","1");
				}
				params.setContentEncoding("UTF-8");
				AsyncHttpClient client = new AsyncHttpClient();
				//保存cookie，自动保存到了shareprefercece  
		        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);    
		        client.setCookieStore(myCookieStore); 
		        client.post(ConstantParamPhone.IP+methord, params,new TextHttpResponseHandler()
				{

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
					{
						Toast.makeText(context, "网络不给力呀", Toast.LENGTH_SHORT).show();
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) 
					{
						ResponsBean responsBean=new Gson().fromJson(arg2,ResponsBean.class);
						if(!responsBean.getStatus().equals("1"))
						{
							Toast.makeText(context, "出错啦！", Toast.LENGTH_SHORT).show();
						}
						else
						{
							Toast.makeText(context, "修改成功啦！", Toast.LENGTH_SHORT).show();
						}
					}
					@Override
					public void onFinish() 
					{
						// TODO Auto-generated method stub
						ColorDialog.dissmissProcessDialog();
						if(status.equals("1"))
						{
							list.get(postion).setStatus("2");
						}
						if(status.equals("2"))
						{
							list.get(postion).setStatus("1");
						}
						notifyDataSetChanged();
					}
				});
			}
		}
}
