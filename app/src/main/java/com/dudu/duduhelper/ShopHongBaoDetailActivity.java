package com.dudu.duduhelper;

import com.dudu.duduhelper.bean.HongbaoListBean;
import com.dudu.duduhelper.bean.SubmitHongbaoBean;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.widget.WheelIndicatorItem;
import com.dudu.duduhelper.widget.WheelIndicatorView;
import com.dudu.duduhelper.widget.risenumbertextview.RiseNumberTextView;
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
	private HongbaoListBean hongbaoBean;
	private TextView hongbaoStartTimeTextView;
	private ImageView hongbaoImage;
	private TextView hongbaoName;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_hong_bao_detail);
		initView();
		initData();
	}
	@Override
	public void RightButtonClick() 
	{
		// TODO Auto-generated method stub
		super.RightButtonClick();
		Intent intent = new Intent(ShopHongBaoDetailActivity.this,ShopHongbaoHistoryListActivity.class);
		intent.putExtra("hongbaoId", hongbaoBean.getId());
		startActivity(intent);
	}

	private void initView() 
	{
		// TODO Auto-generated method stub
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.icon_head)
		.showImageForEmptyUri(R.drawable.icon_head)
		.showImageOnFail(R.drawable.icon_head)
		.cacheInMemory(true).considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(100)).build();
		
		initHeadView("商家红包", true, true, R.drawable.icon_historical);
		hongbaoName=(TextView) this.findViewById(R.id.hongbaoName);
		hongbaoImage=(ImageView) this.findViewById(R.id.hongbaoImage);
		
		hongbaoBean=(HongbaoListBean) getIntent().getSerializableExtra("hongbao");
		
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
		editHongbaoBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(ShopHongBaoDetailActivity.this,ShopHongBaoAddActivity.class);
				intent.putExtra("hongbao", hongbaoBean);
				startActivityForResult(intent, 1);
			}
		});
	}
	
	private void initData() 
	{
		// TODO Auto-generated method stub
		
		
		hongbaoName.setText(hongbaoBean.getTitle());
		hongbaoStartTimeTextView.setText(Util.DataConVertMint(hongbaoBean.getTime_start())+" — "+Util.DataConVertMint(hongbaoBean.getTime_end()));
		imageLoader.displayImage(hongbaoBean.getLogo(),hongbaoImage, options);
		//设置圆圈填充百分比
        //float dailyKmsTarget = 4.0f; // 4.0Km is the user target, for example
        //float totalKmsDone = 3.0f; // User has done 3 Km
        //int percentageOfExerciseDone = (int) (totalKmsDone/dailyKmsTarget * 100); //
        //wheelIndicatorView.setFilledPercent(percentageOfExerciseDone);
		
		wheelIndicatorView.setItemsLineWidth(Util.dip2px(this, 2));
        //设置使用金额
        WheelIndicatorItem bikeActivityIndicatorItem = new WheelIndicatorItem(Float.parseFloat(hongbaoBean.getUsed_money())/Float.parseFloat(hongbaoBean.getTotal()), Color.parseColor("#ff5400"),Util.dip2px(this, 6));
        //设置领取金额
        WheelIndicatorItem walkingActivityIndicatorItem = new WheelIndicatorItem(Float.parseFloat(hongbaoBean.getSend_money())/Float.parseFloat(hongbaoBean.getTotal()), Color.parseColor("#3dd6bc"),Util.dip2px(this, 4));
        wheelIndicatorView.addWheelIndicatorItem(bikeActivityIndicatorItem);
        wheelIndicatorView.addWheelIndicatorItem(walkingActivityIndicatorItem);
        //Or you can add it as
        //wheelIndicatorView.setWheelIndicatorItems(Arrays.asList(runningActivityIndicatorItem,walkingActivityIndicatorItem,bikeActivityIndicatorItem));
        wheelIndicatorView.startItemsAnimation(); 
        
        
        //设置红包金额
        wheelIndicatorView2.setItemsLineWidth(Util.dip2px(this, 2));
        //设置红包使用数
        WheelIndicatorItem hongbaoGetIndicatorItem = new WheelIndicatorItem(Float.parseFloat(hongbaoBean.getUsed_num())/Float.parseFloat(hongbaoBean.getNum()), Color.parseColor("#ff5400"),Util.dip2px(this, 6));
        //设置红包领取数
        WheelIndicatorItem hongbaoUseIndicatorItem = new WheelIndicatorItem(Float.parseFloat(hongbaoBean.getSend_num())/Float.parseFloat(hongbaoBean.getNum()), Color.parseColor("#3dd6bc"),Util.dip2px(this, 4));
        wheelIndicatorView2.addWheelIndicatorItem(hongbaoGetIndicatorItem);
        wheelIndicatorView2.addWheelIndicatorItem(hongbaoUseIndicatorItem);
        //Or you can add it as
        //wheelIndicatorView.setWheelIndicatorItems(Arrays.asList(runningActivityIndicatorItem,walkingActivityIndicatorItem,bikeActivityIndicatorItem));
        wheelIndicatorView2.startItemsAnimation(); // Animate!
        
        //设置数据  
 		sendMoneyText.withNumber(Float.parseFloat(hongbaoBean.getTotal()));  
 		getMoneyText.withNumber(Float.parseFloat(hongbaoBean.getSend_money()));  
 		useMoneyText.withNumber(Float.parseFloat(hongbaoBean.getUsed_money())); 
 		
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
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		//requestCode判断来源是哪个activity resultCode判断来源是activity的哪一个方法
		if (requestCode == 1 && resultCode == RESULT_OK) 
		{
			SubmitHongbaoBean submitHongbaoBean=(SubmitHongbaoBean) data.getSerializableExtra("hongbaobean");
			hongbaoBean.setTitle(submitHongbaoBean.getTitle());
			hongbaoBean.setTotal(submitHongbaoBean.getTotal());
			hongbaoBean.setNum(submitHongbaoBean.getNum());
			hongbaoBean.setLower_money(submitHongbaoBean.getLower_money());
			hongbaoBean.setUpper_money(submitHongbaoBean.getUpper_money());
			hongbaoBean.setTime_start(submitHongbaoBean.getTime_start());
			hongbaoBean.setTime_end(submitHongbaoBean.getTime_end());
			hongbaoBean.setLife(submitHongbaoBean.getLife());
			hongbaoBean.setLimit(submitHongbaoBean.getLimit());
			initData();
		}
	}
}
