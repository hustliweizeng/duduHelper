package com.dudu.duduhelper.Activity.MessageCentreActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.adapter.MsgOperationAdapter;

public class MessageCenterListActivity extends BaseActivity 
{

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_message_center_list);
		initview();

		
	}

	private void initview() {
		ListView lv_msglist = (ListView) findViewById(R.id.lv_msglist);

		Intent intent = getIntent();
		int type = intent.getIntExtra("type",-1);
		switch (type){
			case 1:
				initHeadView("系统公告",true,false,0);

				break;
			case 2:
				initHeadView("运营公告",true,false,0);
				break;
			case 3:
				initHeadView("操作提醒",true,false,0);
				lv_msglist.setAdapter(new MsgOperationAdapter(this));
				break;
			case 4:
				initHeadView("订单提醒",true,false,0);
				break;


		}
	}

}
