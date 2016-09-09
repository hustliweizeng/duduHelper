package com.dudu.duduhelper.Activity.ShopManageActivity;

import android.os.Bundle;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;

public class ShopAddActivity extends BaseActivity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_add);
		initHeadView("门店设置", true, false, 0);
	}

}
