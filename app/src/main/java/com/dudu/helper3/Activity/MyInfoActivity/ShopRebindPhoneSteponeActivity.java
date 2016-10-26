package com.dudu.helper3.Activity.MyInfoActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.helper3.Activity.WelcomeActivity.LoginActivity;
import com.dudu.helper3.Activity.WelcomeActivity.LoginBindPhoneActivity;
import com.dudu.helper3.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.application.DuduHelperApplication;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopRebindPhoneSteponeActivity extends BaseActivity {

	private Button getPermessionbutton;
	private Button btnGetmess;
	private EditText messageCodeEditText;
	private TextView phoneNumText;
	private boolean isSubmit;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_rebind_phone_stepone);
		DuduHelperApplication.getInstance().addActivity(this);
		initHeadView("重新绑定", true, false, 0);
		initView();
	}

	private void initView() 
	{
		phoneNumText = (TextView) this.findViewById(R.id.phoneNumText);
		String mobile = sp.getString("mobile","");
		if(!TextUtils.isEmpty(mobile))
		{
			phoneNumText.setText(mobile);
		}
		else
		{
			//一般在登陆的时候判断是否绑定手机，如果进入到了个人中心页面，说明肯定已经绑定过了
			//或者是重新绑定时，已经解绑但是还没有绑定上，会出现这种问题,比如当前页面已经解绑，但是进入下个绑定
			//页面没有绑定成功时，会返回到当前页面
			phoneNumText.setText("请先绑定手机号");
			DuduHelperApplication application = (DuduHelperApplication)getApplication();
			application.exit();
			startActivity(new Intent(context, LoginActivity.class));
		}
		messageCodeEditText=(EditText) this.findViewById(R.id.messageCodeEditText);
		getPermessionbutton=(Button) this.findViewById(R.id.getPermessionbutton);
		btnGetmess=(Button) this.findViewById(R.id.btnGetmess);
		//获取验证码
		btnGetmess.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				getMessage(ConstantParamPhone.GET_SMS_CONFIRM,"get");
			}
		});
		//下一步按钮，请求网络确认验证码
		getPermessionbutton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//非空判断
				if(TextUtils.isEmpty(messageCodeEditText.getText().toString().trim()))
				{
					Toast.makeText(ShopRebindPhoneSteponeActivity.this,"请输入验证码", Toast.LENGTH_SHORT).show();
					return;
				}
				
				//提交服务器解绑
				getMessage(ConstantParamPhone.UNBIND_PHONE,"POST");
			}
		});
	}
	private void getMessage(final String url, final String method) 
	{
		RequestParams params = new RequestParams();
		
		//获取短信验证
		if (method.equalsIgnoreCase("get")){
			params.put("type", "unbind");
			params.put("mobile",sp.getString("mobile", ""));
		}else {
			//解绑手机
			params.put("code",messageCodeEditText.getText().toString().trim());
		}
		HttpUtils.getConnection(context,params,url, method,new TextHttpResponseHandler(){
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3)
			{
				Toast.makeText(ShopRebindPhoneSteponeActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2)
			{
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						if(method.equalsIgnoreCase("get")){
							//如果是get请求方式，开始计时
							showResidueSeconds();
							return;
						}else {
							Toast.makeText(context,"原手机已解绑，重新输入新手机号",Toast.LENGTH_LONG).show();
							startActivity(new Intent(context,LoginBindPhoneActivity.class));
							finish();
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
