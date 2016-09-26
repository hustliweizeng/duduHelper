package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.widget.WheelIndicatorItem;
import com.dudu.duduhelper.widget.WheelIndicatorTongjiView;

/**
 * @author
 * @version 1.0
 * @date 2016/9/23
 */

public class SendMessageActivity extends BaseActivity implements View.OnClickListener {
	WheelIndicatorTongjiView wheelIndicatorView;
	private TextView active_user_num;
	private TextView unactive_user_num;
	private TextView tv_count_activity;
	private RelativeLayout activity_msg;
	private TextView tv_count_redbag;
	private RelativeLayout redbage_msg;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_sendmessage);
		initView();
		initHeadView("选择通知类型",true,false,0);
		initHeadView();
	}

	private void initHeadView() {
		wheelIndicatorView = (WheelIndicatorTongjiView) findViewById(R.id.wheel_indicator_view);
		wheelIndicatorView.setItemsLineWidth(Util.dip2px(this, 2));
		//设置使用金额
		WheelIndicatorItem bikeActivityIndicatorItem = new WheelIndicatorItem(Float.parseFloat("40") / Float.parseFloat("100"), Color.parseColor("#ff5000"), Util.dip2px(this, 4));
		WheelIndicatorItem bikeActivityIndicatorItem1 = new WheelIndicatorItem(Float.parseFloat("40") / Float.parseFloat("100"), Color.parseColor("#2c4660"), Util.dip2px(this, 2));
		wheelIndicatorView.addWheelIndicatorItem(bikeActivityIndicatorItem1);
		wheelIndicatorView.addWheelIndicatorItem(bikeActivityIndicatorItem);
		wheelIndicatorView.startItemsAnimation();
	}

	private void initView() {
		active_user_num = (TextView) findViewById(R.id.active_user_num);
		unactive_user_num = (TextView) findViewById(R.id.unactive_user_num);
		tv_count_activity = (TextView) findViewById(R.id.tv_count_activity);
		activity_msg = (RelativeLayout) findViewById(R.id.activity_msg);
		tv_count_redbag = (TextView) findViewById(R.id.tv_count_redbag);
		redbage_msg = (RelativeLayout) findViewById(R.id.redbage_msg);
		redbage_msg.setOnClickListener(this);
		activity_msg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		int count = 0;
		Intent intent = null;
		switch (v.getId()){
			//红包消息
			case R.id.redbage_msg:
			//判断当前可用次数是否为0
				count = 1;
			if (count >0){
				intent = new Intent(context, CreateRedbagmsgActivity.class);
			}else {
				intent = new Intent(context,StoreMoneyActivity.class);
			}
			break;
			//活动消息
			case R.id.activity_msg:
				count =0;
			if (count >0){
				intent = new Intent(context, CreateActivityMsg.class);
			}else {
				intent = new Intent(context,StoreMoneyActivity.class);
			}
			break;
		}
		startActivity(intent);
	}
}
