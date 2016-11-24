package com.dudu.duduhelper.Activity.WelcomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dudu.duduhelper.Activity.MainActivity;
import com.dudu.duduhelper.Activity.MyInfoActivity.ShopSettingActivity;
import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.widget.ColorDialog;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginBindPhoneActivity extends BaseActivity 
{
	private EditText userPhoneEditText;
	private EditText messageCodeEditText;
	private Button btnGetmess;
	private Button SubmitPhonebutton;
	private String userphone;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_bind_phone);
		initHeadView("绑定手机号", true, false, 0);
		initView();
	}

	private void initView() {
		
		userPhoneEditText = (EditText) this.findViewById(R.id.userPhoneEditText);
		messageCodeEditText = (EditText) this.findViewById(R.id.messageCodeEditText);
		btnGetmess = (Button) this.findViewById(R.id.btnGetmess);
		SubmitPhonebutton = (Button) this.findViewById(R.id.SubmitPhonebutton);
		//发送验证码
		btnGetmess.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (TextUtils.isEmpty(userPhoneEditText.getText().toString().trim())) {
					Toast.makeText(LoginBindPhoneActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
					return;
				}
				if (userPhoneEditText.getText().toString().trim().length() != 11) {
					Toast.makeText(LoginBindPhoneActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
					return;
				}
				userphone = userPhoneEditText.getText().toString().trim();
				sendMsg();
			}
		});
		//提交请求
		SubmitPhonebutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (TextUtils.isEmpty(userPhoneEditText.getText().toString().trim())) {
					Toast.makeText(LoginBindPhoneActivity.this, "请输入手机号", Toast.LENGTH_SHORT).show();
					return;
				}
				if (userPhoneEditText.getText().toString().trim().length() != 11) {
					Toast.makeText(LoginBindPhoneActivity.this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
					return;
				}
				if (TextUtils.isEmpty(messageCodeEditText.getText().toString().trim())) {
					Toast.makeText(LoginBindPhoneActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
					return;
				}
				if (!userPhoneEditText.getText().toString().trim().equals(userphone)) {
					Toast.makeText(LoginBindPhoneActivity.this, "请保持手机号一致", Toast.LENGTH_SHORT).show();
					return;
				}
				ColorDialog.showRoundProcessDialog(LoginBindPhoneActivity.this, R.layout.loading_process_dialog_color);
				RequestParams params = new RequestParams();
				params.add("code", messageCodeEditText.getText().toString().trim());
				params.add("mobile",userPhoneEditText.getText().toString().trim());
				HttpUtils.getConnection(context,params, ConstantParamPhone.BIND_PHONE, "post", new TextHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
						Toast.makeText(LoginBindPhoneActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						try {
							JSONObject object = new JSONObject(arg2);
							String code =  object.getString("code");
							if ("SUCCESS".equalsIgnoreCase(code)){
								//数据请求成功
								Toast.makeText(context,"手机绑定成功",Toast.LENGTH_SHORT).show();
								//startActivity(new Intent(context,MainActivity.class));//绑定成功以后 结束当前页面
								String type = getIntent().getStringExtra("type");
								if ("info".equals(type)){
									//跳转到主页
									startActivity(new Intent(context,ShopSettingActivity.class));
								}else {
									startActivity(new Intent(context,MainActivity.class));
								}
								sp.edit().putString("mobile",userPhoneEditText.getText().toString().trim()).commit();//每次保存完手机号就保存到本地
								finish();
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
						ColorDialog.dissmissProcessDialog();
					}
				});
			}
		});
	}
	//发送验证码
	private void sendMsg() {
		RequestParams params = new RequestParams();
		params.put("mobile",userphone);
		params.put("type","bind");
		HttpUtils.getConnection(context, params, ConstantParamPhone.GET_SMS_CONFIRM, "get", new TextHttpResponseHandler() {
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
						showResidueSeconds();
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * 显示剩余秒数
	 */
	private void showResidueSeconds() {
		//显示倒计时按钮
		new CountDownTimer(60*1000,1000){

			@Override
			public void onTick(long lastTime) {
				//倒计时执行的方法
				btnGetmess.setClickable(false);
				btnGetmess.setFocusable(false);
				btnGetmess.setText(lastTime/1000+"秒后重发");
				btnGetmess.setTextColor(getResources().getColor(R.color.text_hint_color));
				btnGetmess.setBackgroundResource(R.drawable.btn_bg_hint);
				LogUtil.d("lasttime","剩余时间:"+lastTime/1000);
			}

			@Override
			public void onFinish() {
				btnGetmess.setClickable(true);
				btnGetmess.setFocusable(true);
				btnGetmess.setText("重新获取");
			}
		}.start();
	}


}