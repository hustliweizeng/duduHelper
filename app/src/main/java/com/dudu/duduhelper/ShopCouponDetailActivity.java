package com.dudu.duduhelper;

import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.bean.ProductListBean;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.BigBandBuy;
import com.dudu.duduhelper.javabean.DiscountDeatailBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.WheelIndicatorItem;
import com.dudu.duduhelper.widget.WheelIndicatorTongjiNoXuxianView;
import com.dudu.duduhelper.widget.risenumbertextview.RiseNumberTextView;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
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
import android.widget.Toast;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

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
	private DiscountDeatailBean.Data coupon;

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
		Intent intent=new Intent(ShopCouponDetailActivity.this,CouponSellHistoryActivity.class);
		intent.putExtra("id", "");
		startActivity(intent);
	}

	private void initData() {
		ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
		String id = getIntent().getLongExtra("id",0)+"";
		RequestParams params = new RequestParams();
		params.put("id",id);
		params.put("op","info");
		HttpUtils.getConnection(context, params, ConstantParamPhone.GET_DISCOUT_DETAIL, "post", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				//数据请求成功
				LogUtil.d("discout",s);
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//DiscountDeatailBean bean = new Gson().fromJson(s, DiscountDeatailBean.class);
						JSONObject data = object.getJSONObject("data");
						LogUtil.d("name",data.getString("name"));
						coupon = new DiscountDeatailBean.Data();
						coupon.setThumbnail(data.getString("thumbnail"));
						coupon.setUpshelf(data.getString("upshelf"));
						coupon.setDownshelf(data.getString("downshelf"));
						coupon.setSold(data.getString("sold"));
						coupon.setValidation_count(data.getString("validation_count"));
						coupon.setStock(data.getString("stock"));
						coupon.setCurrent_price(data.getString("current_price"));

					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFinish() {
				super.onFinish();
				ColorDialog.dissmissProcessDialog();
				if (coupon ==null){
					LogUtil.d("erro","数据为空");
					return;
				}
				fillData();
			}
		});
		
		
	}

	private void fillData() {
		ImageAware imageAware = new ImageViewAware(couponImage, false);
		imageLoader.displayImage(coupon.getThumbnail(), imageAware);
		couponName.setText(coupon.getName());
		couponTime.setText(coupon.getUpshelf() + "\n" + coupon.getDownshelf());
		couponSold.setText(coupon.getSold());
		couponVerify.setText(coupon.getValidation_count());

		//设置数据  
		couponSold.withNumber(Float.parseFloat(coupon.getSold()));
		couponVerify.withNumber(Float.parseFloat(coupon.getValidation_count()));
		leftNumText.withNumber(Float.parseFloat(String.valueOf(Integer.parseInt(coupon.getSold()) - Integer.parseInt(coupon.getValidation_count()))));

		couponSold.setDuration(1200);
		couponVerify.setDuration(1200);
		leftNumText.setDuration(1200);

		//开始播放动画  
		couponSold.start();
		couponVerify.start();
		leftNumText.start();
		WheelIndicatorItem bikeActivityIndicatorItem = new WheelIndicatorItem(Float.parseFloat(coupon.getSold()) / Float.parseFloat(coupon.getStock()), Color.parseColor("#ff5000"), Util.dip2px(this, 4));
		WheelIndicatorItem bikeActivityIndicatorItem1 = new WheelIndicatorItem(Float.parseFloat(coupon.getSold()) / Float.parseFloat(coupon.getStock()), Color.parseColor("#ffffff"), Util.dip2px(this, 2));
		wheelIndicatorTongjiNoXuxianView.addWheelIndicatorItem(bikeActivityIndicatorItem1);
		wheelIndicatorTongjiNoXuxianView.addWheelIndicatorItem(bikeActivityIndicatorItem);
		wheelIndicatorTongjiNoXuxianView.startItemsAnimation();


		editCouponButton = (Button) this.findViewById(R.id.editCouponButton);
		//historyCouponButton=(Button) this.findViewById(R.id.historyCouponButton);
		editCouponButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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
