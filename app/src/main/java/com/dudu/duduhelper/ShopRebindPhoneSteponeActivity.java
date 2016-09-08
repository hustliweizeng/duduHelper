package com.dudu.duduhelper;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
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
	private boolean isUnbind = true;
	private String type;

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
			phoneNumText.setText("请先绑定手机号");
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
				//如果是解绑手机
				if (isUnbind){
					type = "unbind";
					getMessage(ConstantParamPhone.GET_SMS_CONFIRM,"get");
				}else {
					type = "bind";
					getMessage(ConstantParamPhone.GET_SMS_CONFIRM,"GET");
				}
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
				else
				{
					//请求网络解绑手机、
					getMessage(ConstantParamPhone.UNBIND_PHONE,"POST");
				}
				//如果是点击了绑定提交按钮
				if (isSubmit){
					getMessage(ConstantParamPhone.BIND_PHONE,"post");
				}
			}
		});
	}
	private void getMessage(final String url, final String method) 
	{
		RequestParams params = new RequestParams();
		
		//获取短信验证
		if (method.equalsIgnoreCase("get")){
			params.put("type", type);
			params.put("mobile",sp.getString("mobile", ""));
		}else {
			//解绑手机
			if (url.equalsIgnoreCase(ConstantParamPhone.UNBIND_PHONE)){
				params.put("code",messageCodeEditText.getText().toString().trim());
			}else {
				//绑定手机
				params.put("code",messageCodeEditText.getText().toString().trim());
				params.put("mobile",sp.getString("mobile", ""));
			}
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
						}
						//如果解绑短信验证成功
						if (url.equals(ConstantParamPhone.UNBIND_PHONE)){
							//当前页面刷新UI
							phoneNumText.setHint("请输入要绑定的手机号");
							btnGetmess.setText("提交");
							//设置当前页面状态为提交
							isSubmit = true;
						}
						//如果请求是绑定手机
						if (url.equalsIgnoreCase(ConstantParamPhone.BIND_PHONE)){
							Toast.makeText(context,"绑定成功,请重新登陆",Toast.LENGTH_SHORT).show();
							DuduHelperApplication application = (DuduHelperApplication)getApplication();
							application.exit();
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
