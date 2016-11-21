package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.ShopStatusBean;
import com.dudu.duduhelper.widget.WheelIndicatorItem;
import com.dudu.duduhelper.widget.WheelIndicatorTongjiView;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
	private float active_num;
	private float unactive_num;
	private int activity_time;
	private int redbag_time;
	private float redbag_price;
	private float activity_price;
	private TextView act_content;
	private TextView redbag_content;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_sendmessage);
		initView();
		initHeadView("选择通知类型",true,false,0);
		initData();
	}

	private void initData() {
		HttpUtils.getConnection(context, null, ConstantParamPhone.GET_SHOP_STATUS, "get", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						ShopStatusBean shopStatusBean = new Gson().fromJson(s, ShopStatusBean.class);
						active_num = Float.parseFloat(shopStatusBean.getActive_user());
						unactive_num = Float.parseFloat(shopStatusBean.getInactivity_user());

						active_user_num.setText(active_num+"");
						unactive_user_num.setText(unactive_num+"");
						
						//设置次数
						List<ShopStatusBean.MessageListsBean> message_lists = shopStatusBean.getMessage_lists();
						if (message_lists!=null &&message_lists.size()>0){
							if (message_lists.get(0).getType_id().equals("1")){
								//设置活动次数
								activity_time = Integer.parseInt(message_lists.get(0).getSurplus());
								tv_count_activity.setText(activity_time+"");
								act_content.setText(message_lists.get(0).getDesc());//说明
								//发送活动信息的单价
								activity_price = Float.parseFloat(message_lists.get(0).getPrice());
							} 
							if (message_lists.get(1).getType_id().equals("2")){
								//设置红包剩余次数
								redbag_time = Integer.parseInt(message_lists.get(1).getSurplus());
								LogUtil.d("redbag_time",redbag_time+"");
								tv_count_redbag.setText(redbag_time+"");
								redbag_content.setText(message_lists.get(1).getDesc());//说明
								//红包信息单价
								redbag_price = Float.parseFloat(message_lists.get(1).getPrice());
							}
						}

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
				//初始化图片信息
				initHeadView();

			}
		});
		
		
	}

	@Override
	public void onResume() {
		super.onResume();
		initData();
	}

	private void initHeadView() {
		float total_num = active_num+unactive_num;

		wheelIndicatorView = (WheelIndicatorTongjiView) findViewById(R.id.wheel_indicator_view);
		wheelIndicatorView.setItemsLineWidth(Util.dip2px(this, 2));
		//设置使用金额
		//背景是蓝底，所以未到店的是分子，总数是分母
		WheelIndicatorItem bikeActivityIndicatorItem = new WheelIndicatorItem(unactive_num/total_num, Color.parseColor("#ff5000"), Util.dip2px(this, 4));//非活跃
		WheelIndicatorItem bikeActivityIndicatorItem1 = new WheelIndicatorItem(unactive_num/total_num, Color.parseColor("#2c4660"), Util.dip2px(this, 2));//活跃
		wheelIndicatorView.addWheelIndicatorItem(bikeActivityIndicatorItem);
		wheelIndicatorView.addWheelIndicatorItem(bikeActivityIndicatorItem1);
		wheelIndicatorView.startItemsAnimation();
	}

	private void initView() {
		active_user_num = (TextView) findViewById(R.id.active_user_num);
		unactive_user_num = (TextView) findViewById(R.id.unactive_user_num);
		tv_count_activity = (TextView) findViewById(R.id.tv_count_activity);
		activity_msg = (RelativeLayout) findViewById(R.id.activity_msg);
		tv_count_redbag = (TextView) findViewById(R.id.tv_count_redbag);
		redbage_msg = (RelativeLayout) findViewById(R.id.redbage_msg);
		act_content = (TextView) findViewById(R.id.act_content);
		redbag_content = (TextView) findViewById(R.id.redbag_content);
		redbage_msg.setOnClickListener(this);
		activity_msg.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()){
			//红包消息
			case R.id.redbage_msg:
			//判断当前可用次数是否为0
			if (redbag_time >0){
				intent = new Intent(context, CreateRedbagmsgActivity.class);
			}else {
				intent = new Intent(context,StoreMoneyActivity.class);
				intent.putExtra("style","redbag");
				intent.putExtra("price",redbag_price);
			}
			break;
			//活动消息
			case R.id.activity_msg:
			if (activity_time >0){
				intent = new Intent(context, CreateActivityMsg.class);
			}else {
				intent = new Intent(context,StoreMoneyActivity.class);
				intent.putExtra("style","activity");
				intent.putExtra("price",activity_price);
			}
			break;
		}
		startActivity(intent);
	}
}
