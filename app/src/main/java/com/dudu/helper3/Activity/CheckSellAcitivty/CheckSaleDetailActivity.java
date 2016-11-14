package com.dudu.helper3.Activity.CheckSellAcitivty;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.Utils.Util;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.CheckTicketBean;
import com.dudu.helper3.widget.ColorDialog;
import com.example.qr_codescan.MipcaActivityCapture;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author
 * @version 1.0
 * @date 2016/9/12
 */
public class CheckSaleDetailActivity extends BaseActivity implements View.OnClickListener {

	private ImageButton backButton;
	private TextView tv_name_check_sale;
	private TextView tv_old_price_check_sale;
	private TextView tv_now_price_check_sale;
	private TextView tv_stock_check_sale;
	private TextView tv_from_check_sale;
	private TextView tv_user_check_sale;
	private TextView tv_mobile_check_sale;
	private TextView tv_gettime_check_sale;
	private Button btn_subimit;
	private TextView tv_username_check_sale;
	
	private TextView tv_check_status_check_sale;
	private ImageView iv_check_status_check_sale;
	private LinearLayout ll_content_check_sale;
	private String vertifyCode;
	private CheckTicketBean info;
	private LinearLayout ll_all;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activiy_check_sale);
		initHeadView("券码核销",true,false,0);
		initView();
		initData();
	}

	private void initData() {
		ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
		vertifyCode = getIntent().getStringExtra("result");
		HttpUtils.getConnection(context, null, ConstantParamPhone.GET_CHECK_TICKET+vertifyCode, "GET", new TextHttpResponseHandler() {
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
						//数据请求成功,肯定有info数据
						CheckTicketBean  data = new Gson().fromJson(s,CheckTicketBean.class);
						//显示数据
						info = data;
						certifyTicket(info);

					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
						//核销吗不存在
						ticketStatus =4;
						btn_subimit.setText("重新输入");
						iv_check_status_check_sale.setImageResource(R.drawable.icon_fail);
						tv_check_status_check_sale.setText(msg);
						tv_check_status_check_sale.setTextColor(getResources().getColor(R.color.erro_status));
						ll_all.setVisibility(View.VISIBLE);
						//隐藏下面的所有内容
						ll_content_check_sale.setVisibility(View.GONE);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				ColorDialog.dissmissProcessDialog();
			}
		});
	}

	private void fillPage(int ticketStatus) {
		
		switch (ticketStatus){
			case CHECKABLE:
				LogUtil.d("status","可以核销");
				if (info!=null){
					tv_name_check_sale.setText(info.getSubject());
					tv_old_price_check_sale.setText(info.getPrice());
					tv_now_price_check_sale.setText(info.getCurrent_price());
					tv_stock_check_sale.setText(info.getNum());
					tv_from_check_sale.setText(info.getFrom());
					tv_user_check_sale.setText(info.getMember_name());
					tv_mobile_check_sale.setText(info.getMobile());
					tv_username_check_sale.setText(info.getName());
					if (!TextUtils.isEmpty(info.getCreated_time())){
						tv_gettime_check_sale.setText(Util.DateForomate3(info.getCreated_time()));
					}
					btn_subimit.setText("确认核销");
					tv_check_status_check_sale.setText("该优惠券可核销");
					tv_check_status_check_sale.setTextColor(getResources().getColor(R.color.text_black_color));
				}
				break;
			case CHECK_SUCCESS:
				LogUtil.d("status","核销成功");
				btn_subimit.setText("继续核销");
				Toast.makeText(context,"核销成功!",Toast.LENGTH_SHORT).show();
				tv_check_status_check_sale.setText("该优惠券可核销");
				tv_check_status_check_sale.setTextColor(getResources().getColor(R.color.text_remind));
				finish();//结束当前y
				
				break;
			case CHECKED:
				LogUtil.d("status","已核销");
				tv_name_check_sale.setText(info.getSubject());
				tv_old_price_check_sale.setText(info.getPrice());
				tv_now_price_check_sale.setText(info.getCurrent_price());
				tv_stock_check_sale.setText(info.getNum());
				tv_from_check_sale.setText(info.getFrom());
				tv_user_check_sale.setText(info.getMember_name());
				tv_mobile_check_sale.setText(info.getMobile());
				tv_username_check_sale.setText(info.getName());
				tv_gettime_check_sale.setText(info.getCreated_time());
				btn_subimit.setText("重新输入");
				iv_check_status_check_sale.setImageResource(R.drawable.icon_checked);
				tv_check_status_check_sale.setText("该优惠券已使用");
				tv_check_status_check_sale.setTextColor(getResources().getColor(R.color.erro_status));
				break;

			case NOT_EXIST:
				LogUtil.d("status","不存在");
				btn_subimit.setText("重新输入");
				iv_check_status_check_sale.setImageResource(R.drawable.icon_fail);
				tv_check_status_check_sale.setText("该核销码不存在");
				tv_check_status_check_sale.setTextColor(getResources().getColor(R.color.erro_status));
				//隐藏下面的所有内容
				ll_content_check_sale.setVisibility(View.GONE);
				LogUtil.d("no","hide");
				break;
		}
		//确定状态后，显示页面
		ll_all.setVisibility(View.VISIBLE);
	}

	//核销状态
	private int ticketStatus =-1;

	//验证该优惠券是否可用
	public static final int CHECKABLE = 1;//可以核销
	public static final int CHECK_SUCCESS = 2;//成功核销
	public static final int CHECKED = 3;//已经核销过了
	public static final int NOT_EXIST = 4;//不存在

	private void certifyTicket(CheckTicketBean data) {
		long expiredTime = 0;
		if (data.getExpired_time()!=null){
			expiredTime= Long.parseLong(data.getExpired_time());
		}
		String usedTime = data.getUsed_time();
		//判断是否过期
		if (expiredTime !=0){
			if (System.currentTimeMillis() > expiredTime*1000){//
				Toast.makeText(context,"该优惠券已过期",Toast.LENGTH_SHORT).show();
			}
		}
		//判断是否用过
		if(usedTime!=null){
			if (!"0".equals(usedTime)){
				Toast.makeText(context,"优惠券已经使用过了",Toast.LENGTH_SHORT).show();
				ticketStatus =3;
			}else {
				ticketStatus = 1;//可以使用
			}
		}else {
			ticketStatus = 1;//可以使用,数据为null说明没用过
		}

		//根据页面状态显示不同页面
		fillPage(ticketStatus);
		
	}

	private void initView() {
		backButton = (ImageButton) findViewById(R.id.backButton);
		tv_name_check_sale = (TextView) findViewById(R.id.tv_name_check_sale);
		tv_old_price_check_sale = (TextView) findViewById(R.id.tv_old_price_check_sale);
		tv_now_price_check_sale = (TextView) findViewById(R.id.tv_now_price_check_sale);
		tv_stock_check_sale = (TextView) findViewById(R.id.tv_stock_check_sale);
		tv_from_check_sale = (TextView) findViewById(R.id.tv_from_check_sale);
		tv_user_check_sale = (TextView) findViewById(R.id.tv_user_check_sale);
		tv_mobile_check_sale = (TextView) findViewById(R.id.tv_mobile_check_sale);
		tv_gettime_check_sale = (TextView) findViewById(R.id.tv_gettime_check_sale);
		tv_username_check_sale = (TextView) findViewById(R.id.tv_username_check_sale);
		tv_check_status_check_sale = (TextView) findViewById(R.id.tv_check_status_check_sale);
		iv_check_status_check_sale = (ImageView) findViewById(R.id.iv_check_status_check_sale);
		ll_content_check_sale = (LinearLayout) findViewById(R.id.ll_content_check_sale);
		ll_all = (LinearLayout) findViewById(R.id.ll_all);
		//在未知状态时隐藏
		ll_all.setVisibility(View.GONE);
		btn_subimit = (Button) findViewById(R.id.btn_subimit);
		backButton.setOnClickListener(this);
		btn_subimit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.backButton:
				finish();
				break;
			case R.id.btn_subimit:
				//根据页面不同状态，显示不同的提交按钮
				switch (ticketStatus){
					case CHECKABLE:
						//确认核销
						HttpUtils.getConnection(context, null, ConstantParamPhone.VERTIFY_TICKET + vertifyCode, "post", new TextHttpResponseHandler() {
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
										ticketStatus = 2;
										//刷新页面
										fillPage(ticketStatus);
									}else {
										//数据请求失败
										String msg = object.getString("msg");
										Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
										ticketStatus =4;
										fillPage(ticketStatus);
									}
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
						break;
					//这三种情况都进入到核销页面
					case CHECK_SUCCESS:
					case CHECKED:
					case NOT_EXIST:
						Intent intent = new Intent(context, MipcaActivityCapture.class);
						intent.putExtra("action","hexiao");
						startActivity(intent);
						finish();
						break;
				}
				
				break;
		}
	}
}
