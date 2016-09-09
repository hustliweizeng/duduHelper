package com.dudu.duduhelper.Activity.SummaryActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.widget.WheelIndicatorItem;
import com.dudu.duduhelper.widget.WheelIndicatorTongjiView;

public class ShopAccountWatchActivity extends BaseActivity 
{
	private WheelIndicatorTongjiView wheelIndicatorView;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_account_watch);
		initHeadView("数据统计", true, false, 0);
		wheelIndicatorView = (WheelIndicatorTongjiView) findViewById(R.id.wheel_indicator_view);
		wheelIndicatorView.setItemsLineWidth(Util.dip2px(this, 2));
        //设置使用金额
        WheelIndicatorItem bikeActivityIndicatorItem = new WheelIndicatorItem(Float.parseFloat("40")/Float.parseFloat("100"), Color.parseColor("#ff5000"),Util.dip2px(this, 4));
        WheelIndicatorItem bikeActivityIndicatorItem1 = new WheelIndicatorItem(Float.parseFloat("40")/Float.parseFloat("100"), Color.parseColor("#2c4660"),Util.dip2px(this, 2));
        wheelIndicatorView.addWheelIndicatorItem(bikeActivityIndicatorItem1);
        wheelIndicatorView.addWheelIndicatorItem(bikeActivityIndicatorItem);
        wheelIndicatorView.startItemsAnimation(); 
	}
}
