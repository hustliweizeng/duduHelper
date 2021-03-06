package com.dudu.duduhelper.Activity.RedBagActivity;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.RedbagDetailBean;
import com.dudu.duduhelper.widget.WheelIndicatorItem;
import com.dudu.duduhelper.widget.WheelIndicatorView;
import com.dudu.duduhelper.widget.risenumbertextview.RiseNumberTextView;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.dudu.duduhelper.R;
public class ShopHongBaoDetailActivity extends BaseActivity 
{
	private WheelIndicatorView wheelIndicatorView;
	private WheelIndicatorView wheelIndicatorView2;
	private RiseNumberTextView sendMoneyText;
	private RiseNumberTextView getMoneyText;
	private RiseNumberTextView useMoneyText;
	private RiseNumberTextView totalHongbaoText;
	private RiseNumberTextView getHongbaoText;
	private RiseNumberTextView useHongbaoText;
	private Button editHongbaoBtn;
	private RedbagDetailBean.DataBean hongbaoBean;
	private TextView hongbaoStartTimeTextView;
	private ImageView hongbaoImage;
	private TextView hongbaoName;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;
	private RedbagDetailBean data;
	private boolean isMainShop;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_hong_bao_detail);
		isMainShop = sp.getBoolean("isMainShop",false);//是否主店铺
		initView();
		initData();
	}
	@Override
	public void RightButtonClick() 
	{
		// 红包领取历史记录
		super.RightButtonClick();
		Intent intent = new Intent(ShopHongBaoDetailActivity.this,ShopHongbaoHistoryListActivity.class);
		intent.putExtra("id", data.getData().getId());
		startActivity(intent);
	}

	private void initView() 
	{
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.ic_defalut)
		.showImageForEmptyUri(R.drawable.ic_defalut)
		.showImageOnFail(R.drawable.ic_defalut)
		.cacheInMemory(true).considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(100)).build();
		
		initHeadView("商家红包", true, true, R.drawable.icon_historical);
		hongbaoName=(TextView) this.findViewById(R.id.hongbaoName);
		hongbaoImage=(ImageView) this.findViewById(R.id.hongbaoImage);
		
		hongbaoStartTimeTextView=(TextView) this.findViewById(R.id.hongbaoStartTimeTextView);
		wheelIndicatorView = (WheelIndicatorView) findViewById(R.id.wheel_indicator_view);
		wheelIndicatorView2 = (WheelIndicatorView) findViewById(R.id.wheel_indicator_view2);
		sendMoneyText=(RiseNumberTextView) this.findViewById(R.id.sendMoneyText);
		getMoneyText=(RiseNumberTextView) this.findViewById(R.id.getMoneyText);
		useMoneyText=(RiseNumberTextView) this.findViewById(R.id.useMoneyText);
		totalHongbaoText=(RiseNumberTextView) this.findViewById(R.id.totalHongbaoText);
		getHongbaoText=(RiseNumberTextView) this.findViewById(R.id.getHongbaoText);
		useHongbaoText=(RiseNumberTextView) this.findViewById(R.id.useHongbaoText);
		editHongbaoBtn=(Button) this.findViewById(R.id.editHongbaoBtn);
		//进入红包编辑页面
		editHongbaoBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent=new Intent(ShopHongBaoDetailActivity.this,ShopHongBaoAddActivity.class);
				LogUtil.d("edit","edtred");
				intent.putExtra("data",data);
				startActivityForResult(intent, 1);
			}
		});
		boolean isManager = sp.getBoolean("isManager", false);
		boolean isMainShop = sp.getBoolean("isMainShop",false);
		LogUtil.d("isManager",isManager+"");
		LogUtil.d("isMainShop",isMainShop+"");
		if (isManager ){//满足任何一个即可
			//再判断是不是分店
			if (isMainShop){
				editHongbaoBtn.setVisibility(View.VISIBLE);
			}else {
				editHongbaoBtn.setVisibility(View.GONE);
			}
		}else {
			editHongbaoBtn.setVisibility(View.GONE);
		}
		
	}
	
	private void initData() 
	{
		String id = getIntent().getStringExtra("id");
		LogUtil.d("incomeId","id="+id);
		HttpUtils.getConnection(context, null, ConstantParamPhone.GET_REDBAG_DETAIL + id, "get", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				try {
					LogUtil.d("suc",s);
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						data = new Gson().fromJson(s, RedbagDetailBean.class);
						hongbaoBean= data.getData();
						if (hongbaoBean == null)
							return;
						//设置数据
						hongbaoName.setText(hongbaoBean.getTitle());
						hongbaoStartTimeTextView.setText(hongbaoBean.getTime_start()+" \n"+hongbaoBean.getTime_end());
						imageLoader.displayImage(hongbaoBean.getLogo(),hongbaoImage, options);
						
						/**
						 * 金额类统计
						 */
						//设置使用金额
						wheelIndicatorView.setItemsLineWidth(Util.dip2px(context, 2));
						WheelIndicatorItem bikeActivityIndicatorItem = new WheelIndicatorItem
								(Float.parseFloat(hongbaoBean.getUsed_money())/Float.parseFloat(hongbaoBean.getSend_money()), Color.parseColor("#ff5400"),Util.dip2px(context, 4));
						//设置领取金额
						WheelIndicatorItem walkingActivityIndicatorItem = new WheelIndicatorItem
								(Float.parseFloat(hongbaoBean.getSend_money())/Float.parseFloat(hongbaoBean.getTotal()), Color.parseColor("#3dd6bc"),Util.dip2px(context, 4));
						wheelIndicatorView.addWheelIndicatorItem(bikeActivityIndicatorItem);
						wheelIndicatorView.addWheelIndicatorItem(walkingActivityIndicatorItem);
						wheelIndicatorView.startItemsAnimation();

						//金额  
						sendMoneyText.withNumber(Float.parseFloat(hongbaoBean.getTotal()));
						getMoneyText.withNumber(Float.parseFloat(hongbaoBean.getSend_money()));
						useMoneyText.withNumber(Float.parseFloat(hongbaoBean.getUsed_money()));

						/**
						 * 数量类统计
						 */
						//设置红包金额
						wheelIndicatorView2.setItemsLineWidth(Util.dip2px(context, 2));
						//设置红包使用数
						WheelIndicatorItem hongbaoGetIndicatorItem = new WheelIndicatorItem
								(Float.parseFloat(hongbaoBean.getUsed_num())/Float.parseFloat(hongbaoBean.getSend_num()), Color.parseColor("#ff5400"),Util.dip2px(context, 4));
						//设置红包领取数
						WheelIndicatorItem hongbaoUseIndicatorItem = new WheelIndicatorItem
								(Float.parseFloat(hongbaoBean.getSend_num())/Float.parseFloat(hongbaoBean.getNum()), Color.parseColor("#3dd6bc"),Util.dip2px(context, 4));
						wheelIndicatorView2.addWheelIndicatorItem(hongbaoGetIndicatorItem);
						wheelIndicatorView2.addWheelIndicatorItem(hongbaoUseIndicatorItem);
						wheelIndicatorView2.startItemsAnimation(); // Animate!
						//数量
						totalHongbaoText.withNumber(Float.parseFloat(hongbaoBean.getNum()));
						getHongbaoText.withNumber(Float.parseFloat(hongbaoBean.getSend_num()));
						useHongbaoText.withNumber(Float.parseFloat(hongbaoBean.getUsed_num()));

						
						//设置动画播放时间  
						sendMoneyText.setDuration(1200);
						getMoneyText.setDuration(1200);
						useMoneyText.setDuration(1200);
						totalHongbaoText.setDuration(1200);
						getHongbaoText.setDuration(1200);
						useHongbaoText.setDuration(1200);

						//开始播放动画  
						sendMoneyText.start();
						getMoneyText.start();
						useMoneyText.start();
						totalHongbaoText.start();
						getHongbaoText.start();
						useHongbaoText.start();
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
}
