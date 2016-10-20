package com.dudu.duduhelper.adapter;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.bean.HongbaoListBean;
import com.dudu.duduhelper.bean.ResponsBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.BigBandBuy;
import com.dudu.duduhelper.Activity.BigBandActivity.shopProductListActivity;
import com.dudu.duduhelper.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ProductAdapter extends BaseAdapter 
{
	private Context context;
    private ViewHolder viewHolder;
    private DisplayImageOptions options;
    private boolean isHongbao=false;//是否是红包
    private boolean isMulChoice;//是否显示批量删除
	private boolean isDisCount=false;//是

    public  HashMap<Integer, Integer> visiblecheck ;//是否显示复选框
    public  HashMap<Integer, Boolean> ischeck;//复选框是否选中

    public List<String> selectid = new ArrayList<String>();//保存选中条目的id
    public List<BigBandBuy.DataBean> list=new ArrayList<>();//大牌产品列表
    public List<HongbaoListBean> hongBaolist=new ArrayList<HongbaoListBean>();//红包列表
    protected ImageLoader imageLoader = ImageLoader.getInstance();
	//是否全选
	public boolean isAllSelect;
	//是否显示复选框
	public  boolean isShowCheckBox;
	private AlertDialog dailog;


	//初始化listview的checkbox
    public ProductAdapter(Context context,boolean isMulChoice,boolean isDisCount,boolean isHongbao)
    {
    	this.isDisCount=isDisCount;
    	this.isHongbao=isHongbao;
    	this.context=context;
		//设置图片加载的信息
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
    public void addAll(List<BigBandBuy.DataBean> list,boolean isAllSelect)
    {
	    //非空判断
	    if (list==null){
		    return;
	    }

    	this.list.addAll(this.list.size(), list);
		this.isAllSelect = isAllSelect;
		//是否全选状态
    	if(isAllSelect)
    	{
    		//设置所有checkbox可见
            for(int i=0;i<getCount();i++)
            {
            	if(ischeck.size()-1>i)
            	{
            		if(ischeck.get(i))
                	{
						//所有条目设置为选中
                		ischeck.put(i, true);
                	}
            	}
            	else
            	{
            		ischeck.put(i, false);
            	}
            	//设置所有复选框可见
                visiblecheck.put(i, CheckBox.VISIBLE);
            }
        }
		//不是全选状态
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
		if(isHongbao)
		{
			return Long.parseLong(hongBaolist.get(position).getId());
		}
		else
		{
			return Long.parseLong(list.get(position).getId());
		}
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		if(convertView==null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.shop_fragment_product_item, null);
			//复选框
			viewHolder.productCheckImg=(ImageView) convertView.findViewById(R.id.productCheckImg);
			viewHolder.productRelPriceTextView=(TextView) convertView.findViewById(R.id.productRelPriceTextView);
			//设置横线
			viewHolder.productRelPriceTextView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG );
			viewHolder.productName=(TextView) convertView.findViewById(R.id.productName);
			viewHolder.producthaveNum=(TextView) convertView.findViewById(R.id.producthaveNum);
			viewHolder.productSellNum=(TextView) convertView.findViewById(R.id.productSellNum);
			viewHolder.productPrice=(TextView) convertView.findViewById(R.id.productPrice);
			viewHolder.productAction=(TextView) convertView.findViewById(R.id.productAction);
			viewHolder.productImage=(ImageView) convertView.findViewById(R.id.productImage);
			viewHolder.downButton=(ImageView) convertView.findViewById(R.id.downButton);
			viewHolder.productGetNum=(TextView) convertView.findViewById(R.id.productGetNum);
			viewHolder.tv_status = (TextView)convertView.findViewById(R.id.tv_status);
			
			//红包页面的时候，有几个控件设置不可见
			if(isHongbao) {
				viewHolder.downButton.setVisibility(View.GONE);
				viewHolder.productGetNum.setVisibility(View.VISIBLE);
				viewHolder.productRelPriceTextView.setVisibility(View.GONE);
				viewHolder.productPrice.setVisibility(View.GONE);
			}
			//设置tag复用
			convertView.setTag(viewHolder);
		}
		//开始复用
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		/***************************************************************************
		 *	设置每个条目的数据和事件
		 */
		//设置上下架按钮的监听事件
		final String status = list.get(position).getStatus();
		final String is_on_sale = list.get(position).getIs_on_sale();
		if ("0".equals(status)){
			//审核中不能点击
			viewHolder.downButton.setEnabled(false);
		}else {
			viewHolder.downButton.setEnabled(true);
			viewHolder.downButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Toast.makeText(context,"点击了"+position,Toast.LENGTH_SHORT).show();
					if ("2".equals(status)||"0".equals(is_on_sale)){
						//审核不通过，点击重新审核,新的界面
						AlertDialog dailog =  new AlertDialog.Builder(context).create();
						dailog.show();
						//获取window之前必须先show
						Window window = dailog.getWindow();
						window.setContentView(R.layout.alertdailog_subimt);
						//设置提交成功以后的状态
						dailog.dismiss();
						SwitchStatus(position);
						
					}else {
						dailog = new AlertDialog.Builder(context).create();
						dailog.show();
						//获取window之前必须先show
						Window window = dailog.getWindow();
						window.setContentView(R.layout.alertdailg_bigband);
						Button confirm = (Button) window.findViewById(R.id.confirm);
						Button canle = (Button) window.findViewById(R.id.cancel);
						canle.setTextColor(context.getResources().getColor(R.color.btn_normal_color));
						TextView dis = (TextView) window.findViewById(R.id.disc);
						 if ("1".equals(is_on_sale)){
							//已上架
							 dis.setText("下架后需运营商重新审核，方能上架出售");
						}
						//确定按钮
						confirm.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dailog.dismiss();
								SwitchStatus(position);
							}
						});
						//取消按钮
						canle.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								dailog.dismiss();
							}
						});
					}
					
				}
			});
		}
		
		
		//根据传递过来的布尔值
		if (isShowCheckBox){
			//是否显示多选框
			viewHolder.productCheckImg.setVisibility(View.VISIBLE);
		}else {
			viewHolder.productCheckImg.setVisibility(View.GONE);
		}

		//如果是红包页面
		if(isHongbao)
		{

			if(!TextUtils.isEmpty(hongBaolist.get(position).getId()))
			{
				//
			}
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
			//根据之前复选框信息，设置当前条目样式
			if(ischeck.get(position))
			{
				viewHolder.productCheckImg.setImageResource(R.drawable.icon_xuanze_sel);
			}
			else
			{
				viewHolder.productCheckImg.setImageResource(R.drawable.icon_xuanze);
			}
			//复选框的监听
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
			//viewHolder.productCheckImg.setVisibility(visiblecheck.get(position));
		}
		//如果是商品列表
		else
		{
			//商品原价
			if(!TextUtils.isEmpty(list.get(position).getPrice()))
			{
				viewHolder.productRelPriceTextView.setText(list.get(position).getPrice());
			}
			//商品价格
			if(!TextUtils.isEmpty(list.get(position).getCurrent_price()))
			{
				viewHolder.productPrice.setText("￥"+list.get(position).getCurrent_price());
			}
			//产品名称
			if(!TextUtils.isEmpty(list.get(position).getName()))
			{
				viewHolder.productName.setText(list.get(position).getName());
			}
			//库存
			if(!TextUtils.isEmpty(list.get(position).getStock()))
			{
				viewHolder.producthaveNum.setText("库存:"+list.get(position).getStock());
			}
			//已售出
			if(!TextUtils.isEmpty(list.get(position).getSaled_count()))
			{
				viewHolder.productSellNum.setText("已售:"+list.get(position).getSaled_count());
			}
			
			//上架状态
			if(list.get(position).getStatus().equals("0"))
			{
				//viewHolder.productAction.setText("正在审核");
				viewHolder.downButton.setImageResource(R.drawable.icon_shenhe);
				viewHolder.tv_status.setText("正在审核");
			}
			else if(list.get(position).getStatus().equals("2"))
			{
				//viewHolder.productAction.setText("审核中");
				viewHolder.tv_status.setText("审核未通过");
				viewHolder.downButton.setImageResource(R.drawable.icon_fail);
			//审核通过状态
			}else  if (list.get(position).getStatus().equals("1")){
				if (list.get(position).getIs_on_sale().equals("0")){
					//未上架
					viewHolder.tv_status.setText("未上架");
					viewHolder.downButton.setImageResource(R.drawable.icon_down);

				}else {
					//已上架
					viewHolder.tv_status.setText("已上架");
					viewHolder.downButton.setImageResource(R.drawable.icon_up);

				}
				
			}
			//商品图片
			if(!TextUtils.isEmpty(list.get(position).getThumbnail()))
			{
				String imagepath = list.get(position).getThumbnail();
				if(!imagepath.equals(viewHolder.productImage.getTag()))
				{
					//设置tag，这样图片加载时就不会跳了
					viewHolder.productImage.setTag(imagepath);
					imageLoader.displayImage(list.get(position).getThumbnail(),viewHolder.productImage, options);
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
						LogUtil.d("item_remove",list.get(position).getId());

					}
					else
					{
						viewHolder.productCheckImg.setImageResource(R.drawable.icon_guanyu);

						ischeck.put(position,true);

						//记录是否需要删除的数据id
						selectid.add(list.get(position).getId());
						LogUtil.d("item_add",list.get(position).getId());
						
					}
					notifyDataSetChanged();
				}
			});
			//迭代设置所有checkbox的可见性
			//viewHolder.productCheckImg.setVisibility(visiblecheck.get(position));
		}
		return convertView;
	}

	private void SwitchStatus(int position) {
		RequestParams params  =new RequestParams();
		String id = list.get(position).getId();
		LogUtil.d("switch",id);
		params.add("id",id);
		HttpUtils.getConnection(context, params, ConstantParamPhone.SWITCH_STATUS, "post", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						dailog.dismiss();
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
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
		ImageButton delectButton;
		ImageView downButton;
		TextView productGetNum;
		TextView tv_status;
	}
	//商品删除事件
	private class OnClick implements OnClickListener
	{
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

}
