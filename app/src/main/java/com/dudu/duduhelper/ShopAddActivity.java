package com.dudu.duduhelper;

import android.os.Bundle;

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
