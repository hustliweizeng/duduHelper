package com.dudu.duduhelper;

import android.os.Bundle;

public class ShopInfoEditActivity extends BaseActivity 
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_info_edit);
		initHeadView("设置",true, false, 0);
	}
}
