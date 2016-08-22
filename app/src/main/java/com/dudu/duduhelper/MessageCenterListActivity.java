package com.dudu.duduhelper;

import android.content.Intent;
import android.os.Bundle;

public class MessageCenterListActivity extends BaseActivity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_message_center_list);
		Intent intent = getIntent();
		int type = intent.getIntExtra("type",-1);
		switch (type){
			case 1:
				initHeadView("系统公告",true,false,0);
				break;
			case 2:
				break;
			case 3:
				break;
			case 4:
				break;


		}
		
	}

}
