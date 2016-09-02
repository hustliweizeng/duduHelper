package com.dudu.duduhelper;

import com.dudu.duduhelper.bean.ProductListBean;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.javabean.BigBandBuy;
import com.dudu.duduhelper.widget.WheelIndicatorItem;
import com.dudu.duduhelper.widget.WheelIndicatorTongjiNoXuxianView;
import com.dudu.duduhelper.widget.risenumbertextview.RiseNumberTextView;
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
import android.widget.TextView;

public class ShopCouponDetailActivity extends BaseActivity 
{
	private Button editCouponButton;
	//private Button historyCouponButton;
	//商品id
	private BigBandBuy.DataBean coupon;
	private ImageView couponImage;
	private TextView couponName;
	private TextView couponTime;
	private RiseNumberTextView couponSold;
	private RiseNumberTextView couponVerify;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private RiseNumberTextView leftNumText;
	private WheelIndicatorTongjiNoXuxianView wheelIndicatorTongjiNoXuxianView;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_coupon_detail);
		initHeadView("优惠券",true, true, R.drawable.icon_historical);
		initView();
		initData();
	}
	
	@Override
	public void RightButtonClick() 
	{
		// TODO Auto-generated method stub
		Intent intent=new Intent(ShopCouponDetailActivity.this,CouponSellHistoryActivity.class);
		intent.putExtra("id", coupon.getId());
		startActivity(intent);
	}

	private void initData() {
		// TODO Auto-generated method stub
		coupon = (BigBandBuy.DataBean) getIntent().getSerializableExtra("coupon");
		ImageAware imageAware = new ImageViewAware(couponImage, false);
		imageLoader.displayImage(coupon.getThumbnail(), imageAware);
		couponName.setText(coupon.getName());
		couponTime.setText(coupon.getUpshelf() + "至\n" + coupon.getDownshelf());
		couponSold.setText(coupon.getSaled_count());
		couponVerify.setText(coupon.getValidation_count());

		//设置数据  
		couponSold.withNumber(Float.parseFloat(coupon.getSaled_count()));
		couponVerify.withNumber(Float.parseFloat(coupon.getValidation_count()));
		leftNumText.withNumber(Float.parseFloat(String.valueOf(Integer.parseInt(coupon.getSaled_count()) - Integer.parseInt(coupon.getValidation_count()))));

		couponSold.setDuration(1200);
		couponVerify.setDuration(1200);
		leftNumText.setDuration(1200);

		//开始播放动画  
		couponSold.start();
		couponVerify.start();
		leftNumText.start();
		WheelIndicatorItem bikeActivityIndicatorItem = new WheelIndicatorItem(Float.parseFloat(coupon.getSaled_count()) / Float.parseFloat(coupon.getStock()), Color.parseColor("#ff5000"), Util.dip2px(this, 4));
		WheelIndicatorItem bikeActivityIndicatorItem1 = new WheelIndicatorItem(Float.parseFloat(coupon.getSaled_count()) / Float.parseFloat(coupon.getStock()), Color.parseColor("#ffffff"), Util.dip2px(this, 2));
		wheelIndicatorTongjiNoXuxianView.addWheelIndicatorItem(bikeActivityIndicatorItem1);
		wheelIndicatorTongjiNoXuxianView.addWheelIndicatorItem(bikeActivityIndicatorItem);
		wheelIndicatorTongjiNoXuxianView.startItemsAnimation();


		editCouponButton = (Button) this.findViewById(R.id.editCouponButton);
		//historyCouponButton=(Button) this.findViewById(R.id.historyCouponButton);
		editCouponButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(context,ShopProductAddActivity.class);
				//把数据传递到编辑页面
				intent.putExtra("productinfo", coupon);
				intent.putExtra("category","discount");
				startActivity(intent);
			}
		});
	}

	private void initView() 
	{
		// TODO Auto-generated method stub
		leftNumText=(RiseNumberTextView) this.findViewById(R.id.leftNumText);
		couponImage=(ImageView) this.findViewById(R.id.couponImage);
		couponName=(TextView) this.findViewById(R.id.couponName);
		couponTime=(TextView) this.findViewById(R.id.couponTime);
		couponSold=(RiseNumberTextView) this.findViewById(R.id.couponSold);
		couponVerify=(RiseNumberTextView) this.findViewById(R.id.couponVerify);
		wheelIndicatorTongjiNoXuxianView = (WheelIndicatorTongjiNoXuxianView) findViewById(R.id.wheel_indicator_view);
		wheelIndicatorTongjiNoXuxianView.setItemsLineWidth(Util.dip2px(this, 2));
		
	}
}
