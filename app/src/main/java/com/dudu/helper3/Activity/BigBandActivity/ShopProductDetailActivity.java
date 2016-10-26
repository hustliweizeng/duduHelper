package com.dudu.helper3.Activity.BigBandActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dudu.helper3.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.helper3.Utils.Util;
import com.dudu.helper3.javabean.BigBandBuy;
import com.dudu.helper3.widget.WheelIndicatorItem;
import com.dudu.helper3.widget.WheelIndicatorTongjiNoXuxianView;
import com.dudu.helper3.widget.risenumbertextview.RiseNumberTextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.json.JSONArray;
import org.json.JSONObject;

public class ShopProductDetailActivity extends BaseActivity
{
	private Button editproductinfoButton;
	private RiseNumberTextView totalNumText;
	private RiseNumberTextView sellNumText;
	private RiseNumberTextView leftNumText;
	//商品id
	private BigBandBuy.DataBean productinfo;
	private ImageView productinfoImage;
	private TextView productinfoName;
	private TextView productinfoTime;
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
		//获取传递过来的商品详情
		productinfo =(BigBandBuy.DataBean) getIntent().getSerializableExtra("productinfo");
		initView();
		initData();
	}

	private void initData()
	{

		//设置使用金额

		//设置数据  
		totalNumText.withNumber(Float.parseFloat(productinfo.getStock()));
		sellNumText.withNumber(Float.parseFloat(productinfo.getSaled_count()));
		leftNumText.withNumber(Float.parseFloat(String.valueOf(Integer.parseInt(productinfo.getStock())-Integer.parseInt(productinfo.getSaled_count()))));

		totalNumText.setDuration(1200);
		sellNumText.setDuration(1200);
		leftNumText.setDuration(1200);

		//开始播放动画  
		totalNumText.start();
		sellNumText.start();
		leftNumText.start();


		WheelIndicatorItem bikeActivityIndicatorItem = new WheelIndicatorItem(Float.parseFloat(productinfo.getSaled_count())/Float.parseFloat(productinfo.getStock()), Color.parseColor("#ff5000"), Util.dip2px(this, 4));
		WheelIndicatorItem bikeActivityIndicatorItem1 = new WheelIndicatorItem(Float.parseFloat(productinfo.getSaled_count())/Float.parseFloat(productinfo.getStock()), Color.parseColor("#ffffff"),Util.dip2px(this, 2));
		wheelIndicatorTongjiNoXuxianView.addWheelIndicatorItem(bikeActivityIndicatorItem1);
		wheelIndicatorTongjiNoXuxianView.addWheelIndicatorItem(bikeActivityIndicatorItem);
		wheelIndicatorTongjiNoXuxianView.startItemsAnimation();

		ImageAware imageAware = new ImageViewAware(productinfoImage, false);
		imageLoader.displayImage(productinfo.getThumbnail(), imageAware);
		productinfoName.setText(productinfo.getName());
		String open_time = productinfo.getRule();
		//设置开始结束时间
		
		try {
			JSONArray array = new JSONArray(productinfo.getRule());
			JSONObject time = array.getJSONObject(0);
			String begin = time.getString("begin");
			String end = time.getString("end");
			productinfoTime.setText("开始时间:"+begin+"\n"+"结束时间:"+end);
		}catch (Exception e){
			System.out.print(e);
		}
		sold=Float.parseFloat(productinfo.getSaled_count());
		
		//编辑按钮,进入商品编辑页面
		editproductinfoButton=(Button) this.findViewById(R.id.editCouponButton);
		editproductinfoButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				//进入商品编辑页面
				Intent intent=new Intent(ShopProductDetailActivity.this,ShopProductAddActivity.class);
				intent.putExtra("productinfo", productinfo);
				intent.putExtra("category", category);
				startActivity(intent);
			}
		});

	}

	private void initView()
	{
		leftNumText = (RiseNumberTextView) this.findViewById(R.id.leftNumText);
		totalNumText = (RiseNumberTextView) this.findViewById(R.id.totalNumText);
		sellNumText = (RiseNumberTextView) this.findViewById(R.id.sellNumText);
		productinfoImage=(ImageView) this.findViewById(R.id.couponImage);
		productinfoName=(TextView) this.findViewById(R.id.couponName);
		productinfoTime=(TextView) this.findViewById(R.id.couponTime);
		wheelIndicatorTongjiNoXuxianView = (WheelIndicatorTongjiNoXuxianView) findViewById(R.id.wheel_indicator_view);
		wheelIndicatorTongjiNoXuxianView.setItemsLineWidth(Util.dip2px(this, 2));
		

	}
}
