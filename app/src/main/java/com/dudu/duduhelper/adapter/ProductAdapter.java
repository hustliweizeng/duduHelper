package com.dudu.duduhelper.adapter;

import com.dudu.duduhelper.R;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.bean.HongbaoListBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.BigBandBuy;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import org.apache.http.Header;
import org.json.JSONArray;
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
	private AlertDialog dailog1;
	private String urlL;
	private String url;


	//初始化listview的checkbox
    public ProductAdapter(Context context,boolean isMulChoice,boolean isDisCount,boolean isHongbao)
    {
    	this.isDisCount=isDisCount;
    	this.isHongbao=isHongbao;
    	this.context=context;
		//设置图片加载的信息
    	options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_defalut)
		.showImageForEmptyUri(R.drawable.ic_defalut)
		.showImageOnFail(R.drawable.ic_defalut)
		.cacheInMemory(true).cacheOnDisc(true)
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
			viewHolder.vip_price = (TextView) convertView.findViewById(R.id.vip_price);
			//设置tag复用
			convertView.setTag(viewHolder);
			//LogUtil.d("new","new");
		}
		//开始复用
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
			//LogUtil.d("old","复用");
		}
		/***************************************************************************
		 *	设置每个条目的数据和事件
		 */
		//设置上下架按钮的监听事件
		if ("0".equals(list.get(position).getStatus())){
			//审核中不能点击
			viewHolder.downButton.setEnabled(false);
			viewHolder.downButton.setFocusable(false);
		}else if ("1".equals(list.get(position).getStatus())){
		//审核通过状态，只有上下架状态
			viewHolder.downButton.setEnabled(true);
			//上下架切换按钮
			viewHolder.downButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//Toast.makeText(context,"pos="+position,Toast.LENGTH_SHORT).show();
					LogUtil.d("is_on_sale",list.get(position).getIs_on_sale());
					if ("1".equals(list.get(position).getIs_on_sale())){
						//如果是已上架状态直接切换为下架状态
						LogUtil.d("up","上架");
						list.get(position).setIs_on_sale("0");//设置数据源为下架
						SwitchStatus(position);
					}else{
						/**
                         * 刚开始写的不是else一直不能正常点击我擦啊！
						 */
						LogUtil.d("down","下架");
						list.get(position).setIs_on_sale("1");//设置数据源为上架
						SwitchStatus(position);
					}
					notifyDataSetChanged();
				}
			});
		}else {
			viewHolder.downButton.setEnabled(true);
			viewHolder.downButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					//Toast.makeText(context,"pos="+position,Toast.LENGTH_SHORT).show();

					//审核不通过，点击重新审核,新的界面
					dailog1 = new AlertDialog.Builder(context).create();
					dailog1.show();
					//获取window之前必须先show
					Window window = dailog1.getWindow();
					window.setContentView(R.layout.alertdailog_subimt);
					//不需要按钮的状态

					list.get(position).setStatus("0");//设置数据源为正在审核
					notifyDataSetChanged();
					SwitchStatus(position);
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
		//商品原价
		if(!TextUtils.isEmpty(list.get(position).getPrice()))
		{
			viewHolder.productRelPriceTextView.setText(list.get(position).getPrice());
		}
		if (!isDisCount){

			//商品价格和vip价格
			if(!TextUtils.isEmpty(list.get(position).getRule()))
			{

				try {
					JSONArray rule = new JSONArray(list.get(position).getRule());
					JSONObject time = rule.getJSONObject(0);//只获取第一个规则
					String price = time.getString("price");
					viewHolder.productPrice.setText("￥"+price);//现价
					String vipPrice = time.getString("vip_price");
					if ("1".equals(list.get(position).getIs_vip_price())){//是否有vip价格
						viewHolder.vip_price.setVisibility(View.VISIBLE);
						viewHolder.vip_price.setText("VIP会员价:￥"+vipPrice);//vip价格
					}else {
						viewHolder.vip_price.setVisibility(View.GONE);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		}else {
			//Toast.makeText(context,"优惠券vip",Toast.LENGTH_LONG).show();
			//优惠券页面
			String discountPrice = list.get(position).getIs_vip_price();
			String disPrice = list.get(position).getVip_price();
			LogUtil.d("isvip",discountPrice);
			if("1".equals(discountPrice)){
				viewHolder.vip_price.setVisibility(View.VISIBLE);
				viewHolder.vip_price.setText("VIP会员价:￥"+disPrice);//vip价格
			}else{
				viewHolder.vip_price.setVisibility(View.GONE);
			}
			viewHolder.productPrice.setText("￥"+list.get(position).getCurrent_price());//现价
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
		//已售出-大牌抢购
		if(!TextUtils.isEmpty(list.get(position).getSaled_count()))
		{
			viewHolder.productSellNum.setText("已售:"+list.get(position).getSaled_count());
		}
		//已售出-优惠券
		if(!TextUtils.isEmpty(list.get(position).getSold()))
		{
			viewHolder.productSellNum.setText("已售:"+list.get(position).getSold());
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
		String imagepath = list.get(position).getThumbnail();
		viewHolder.productImage.setTag(imagepath);
		String tag = (String) viewHolder.productImage.getTag();
		//商品图片
		if(!TextUtils.isEmpty(imagepath))
		{
			if(tag!=null && imagepath.equals(tag))
			{
				//设置tag，这样图片加载时就不会跳了
				imageLoader.displayImage(list.get(position).getThumbnail(),viewHolder.productImage, options);
			}
		}else {
			viewHolder.productImage.setImageResource(R.drawable.ic_defalut);
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
		
		return convertView;
	}

	/**
	 * 切换商品上下架状态
	 * @param position
	 */
	private void SwitchStatus(int position) {
		if (isDisCount){
			urlL = ConstantParamPhone.SWITCH_DIS_STATUS;
		}else {
			urlL = ConstantParamPhone.SWITCH_STATUS;
		}
		RequestParams params  =new RequestParams();
		String id = list.get(position).getId();
		params.add("id",id);
		HttpUtils.getConnection(context, params, urlL, "POST", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				LogUtil.d("res",s);
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						if (dailog1!=null){
							dailog1.dismiss();
						}
						//notifyDataSetChanged();

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

	private static class ViewHolder
	{
		ImageView productCheckImg;
		ImageView productImage;
		TextView productRelPriceTextView;//tv.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG ); 真实价格
		TextView productName;//名称
		TextView producthaveNum;//库存
		TextView productSellNum;//销售量
		TextView productPrice;//售价
		TextView productAction;//商品状态
		ImageView downButton;
		TextView productGetNum;
		TextView tv_status;
		TextView vip_price;
		
	}
	

}
