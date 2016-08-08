package com.dudu.duduhelper;

import com.dudu.duduhelper.common.Util;
import com.dudu.duduhelper.widget.WaveHelper;
import com.dudu.duduhelper.widget.WaveView;
import com.dudu.duduhelper.widget.WheelIndicatorItem;
import com.dudu.duduhelper.widget.WheelIndicatorTongjiView;
import com.dudu.duduhelper.widget.WheelIndicatorView;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

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
