package com.dudu.duduhelper;

import com.dudu.duduhelper.bean.ProductListBean;
import com.dudu.duduhelper.common.Util;
import com.dudu.duduhelper.widget.DrawcircleView;
import com.dudu.duduhelper.widget.WheelIndicatorItem;
import com.dudu.duduhelper.widget.WheelIndicatorTongjiNoXuxianView;
import com.dudu.duduhelper.widget.risenumbertextview.RiseNumberTextView;
import com.nineoldandroids.util.IntProperty;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShopProductDetailActivity extends BaseActivity 
{
	private Button editCouponButton;
	private RiseNumberTextView totalNumText;
	private RiseNumberTextView sellNumText;
	private RiseNumberTextView leftNumText;
	//商品id
	private ProductListBean coupon;
	private ImageView couponImage;
	private TextView couponName;
	private TextView couponTime;
	private TextView soldPerTextView;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private float sold;
	private float soldPer;
	//商品分类
    public String category;
	private WheelIndicatorTongjiNoXuxianView wheelIndicatorTongjiNoXuxianView;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_product_detail);
		initHeadView("大牌抢购",true, false, 0);
		category=getIntent().getStringExtra("category");
		coupon=(ProductListBean) getIntent().getSerializableExtra("coupon");
		initView();
		initData();
	}

	private void initData() 
	{
		// TODO Auto-generated method stub
		
		//设置使用金额
		
		 //设置数据  
		totalNumText.withNumber(Float.parseFloat(coupon.getStock()));  
		sellNumText.withNumber(Float.parseFloat(coupon.getSold()));  
		leftNumText.withNumber(Float.parseFloat(String.valueOf(Integer.parseInt(coupon.getStock())-Integer.parseInt(coupon.getSold()))));  
		
		totalNumText.setDuration(1200);
		sellNumText.setDuration(1200);
		leftNumText.setDuration(1200);
		
		 //开始播放动画  
		totalNumText.start();  
		sellNumText.start();  
		leftNumText.start();  
		
//		totalNumText.setText(coupon.getStock());
//		sellNumText.setText(coupon.getSold());
//		leftNumText.setText(String.valueOf(Integer.parseInt(coupon.getStock())-Integer.parseInt(coupon.getSold())));
		
        WheelIndicatorItem bikeActivityIndicatorItem = new WheelIndicatorItem(Float.parseFloat(coupon.getSold())/Float.parseFloat(coupon.getStock()), Color.parseColor("#ff5000"),Util.dip2px(this, 4));
        WheelIndicatorItem bikeActivityIndicatorItem1 = new WheelIndicatorItem(Float.parseFloat(coupon.getSold())/Float.parseFloat(coupon.getStock()), Color.parseColor("#ffffff"),Util.dip2px(this, 2));
        wheelIndicatorTongjiNoXuxianView.addWheelIndicatorItem(bikeActivityIndicatorItem1);
        wheelIndicatorTongjiNoXuxianView.addWheelIndicatorItem(bikeActivityIndicatorItem);
        wheelIndicatorTongjiNoXuxianView.startItemsAnimation(); 
		
		ImageAware imageAware = new ImageViewAware(couponImage, false);
		imageLoader.displayImage(coupon.getPic(), imageAware);
		couponName.setText(coupon.getSubject());
		couponTime.setText(Util.DataConVert2(coupon.getTime_start())+"-"+Util.DataConVert2(coupon.getTime_end()));
		sold=Float.parseFloat(coupon.getSold());
        
		editCouponButton=(Button) this.findViewById(R.id.editCouponButton);
		editCouponButton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(ShopProductDetailActivity.this,ShopProductAddActivity.class);
				intent.putExtra("id", coupon.getId());
				intent.putExtra("category", category);
				startActivity(intent);
			}
		});
		
//		historyCouponButton.setOnClickListener(new OnClickListener() 
//		{	
//			@Override
//			public void onClick(View v) 
//			{
//				// TODO Auto-generated method stub
//				Intent intent=new Intent(ShopProductDetailActivity.this,CouponSellHistoryActivity.class);
//				intent.putExtra("id", coupon.getId());
//				startActivity(intent);
//			}
//		});
	}

	private void initView() 
	{
		// TODO Auto-generated method stub
		leftNumText = (RiseNumberTextView) this.findViewById(R.id.leftNumText);
		totalNumText = (RiseNumberTextView) this.findViewById(R.id.totalNumText);
		sellNumText = (RiseNumberTextView) this.findViewById(R.id.sellNumText);
		couponImage=(ImageView) this.findViewById(R.id.couponImage);
		couponName=(TextView) this.findViewById(R.id.couponName);
		couponTime=(TextView) this.findViewById(R.id.couponTime);
		wheelIndicatorTongjiNoXuxianView = (WheelIndicatorTongjiNoXuxianView) findViewById(R.id.wheel_indicator_view);
		wheelIndicatorTongjiNoXuxianView.setItemsLineWidth(Util.dip2px(this, 2));
        
	}
}
