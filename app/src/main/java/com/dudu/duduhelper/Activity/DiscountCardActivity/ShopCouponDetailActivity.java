package com.dudu.duduhelper.Activity.DiscountCardActivity;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
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
//折扣详情页面
public class ShopCouponDetailActivity extends BaseActivity
{
	private Button editCouponButton;
	//商品id
	private ImageView couponImage;
	private TextView couponName;
	private TextView couponTime;
	private RiseNumberTextView couponSold;
	private RiseNumberTextView couponVerify;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private RiseNumberTextView leftNumText;
	private WheelIndicatorTongjiNoXuxianView wheelIndicatorTongjiNoXuxianView;
	private BigBandBuy.DataBean coupon;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_coupon_detail);
		initHeadView("优惠券",true, true, R.drawable.icon_historical);//优惠券历史
		initView();
		initData();
	}

	@Override
	public void RightButtonClick()
	{
		/*Intent intent=new Intent(ShopCouponDetailActivity.this,CouponSellHistoryActivity.class);
		intent.putExtra("id", "");
		startActivity(intent);*/
	}

	private void initData() {
		String id = getIntent().getLongExtra("id",0)+"";
		//获取列表中的数据
		coupon = (BigBandBuy.DataBean) getIntent().getSerializableExtra("productinfo");
		ImageAware imageAware = new ImageViewAware(couponImage, false);
		imageLoader.displayImage(coupon.getThumbnail(), imageAware);
		couponName.setText(coupon.getName());
		couponTime.setText("开始时间:"+coupon.getUpshelf() + "\n" + "结束时间:"+coupon.getDownshelf());
		couponSold.setText(coupon.getSaled_count());
		couponVerify.setText(coupon.getValidation_count());

		//领取数量  
		couponSold.withNumber(Float.parseFloat(coupon.getSold()));
		//核销数量
		couponVerify.withNumber(Float.parseFloat(coupon.getValidation_count()));
		leftNumText.withNumber(Float.parseFloat(String.valueOf(Integer.parseInt(coupon.getSold()) -Integer.parseInt(coupon.getValidation_count()) )));
		

		couponSold.setDuration(1200);
		couponVerify.setDuration(1200);
		leftNumText.setDuration(1200);

		//开始播放动画  
		couponSold.start();
		couponVerify.start();
		leftNumText.start();
		WheelIndicatorItem bikeActivityIndicatorItem = new WheelIndicatorItem(Float.parseFloat(coupon.getValidation_count()) / Float.parseFloat(coupon.getSold()), Color.parseColor("#ff5000"), Util.dip2px(this, 4));
		WheelIndicatorItem bikeActivityIndicatorItem1 = new WheelIndicatorItem(Float.parseFloat(coupon.getValidation_count()) / Float.parseFloat(coupon.getSold()), Color.parseColor("#ffffff"), Util.dip2px(this, 2));
		wheelIndicatorTongjiNoXuxianView.addWheelIndicatorItem(bikeActivityIndicatorItem1);
		wheelIndicatorTongjiNoXuxianView.addWheelIndicatorItem(bikeActivityIndicatorItem);
		wheelIndicatorTongjiNoXuxianView.startItemsAnimation();


		editCouponButton = (Button) this.findViewById(R.id.editCouponButton);
		editCouponButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context,ShopDiscoutAddActivity.class);
				//把数据传递到编辑页面
				intent.putExtra("productinfo", coupon);
				startActivity(intent);
			}
		});
	}

	private void initView()
	{
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
