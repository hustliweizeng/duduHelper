package com.dudu.duduhelper.Activity.MyInfoActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Activity.WelcomeActivity.LoginActivity;
import com.dudu.duduhelper.Activity.MainActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.bean.ResponsBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

public class ShopRebindPhoneSteptwoActivity extends BaseActivity 
{

	private EditText userPhoneEditText;
	private EditText messageCodeEditText;
	private Button btnGetmess;
	private Button SubmitPhonebutton;
	private TimeCount time;
	private String userphone;
	private String oldCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_rebind_phone_steptwo);
		time = new TimeCount(60000, 1000);//构造CountDownTimer对象
		initHeadView("绑定手机号", true, false, 0);
		oldCode=getIntent().getStringExtra("code");
		initView();
	}

	private void initView() 
	{
		// TODO Auto-generated method stub
		userPhoneEditText=(EditText) this.findViewById(R.id.phoneNumEditText);
		messageCodeEditText=(EditText) this.findViewById(R.id.phoneCodeEditText);
		btnGetmess=(Button)this.findViewById(R.id.btnGetmess);
		SubmitPhonebutton=(Button)this.findViewById(R.id.SubmitPhonebutton);
		btnGetmess.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(userPhoneEditText.getText().toString().trim()))
				{
					Toast.makeText(ShopRebindPhoneSteptwoActivity.this, "请输入手机号",Toast.LENGTH_SHORT).show();
					return;
				}
				if(userPhoneEditText.getText().toString().trim().length()!=11)
				{
					Toast.makeText(ShopRebindPhoneSteptwoActivity.this, "请输入正确的手机号",Toast.LENGTH_SHORT).show();
					return;
				}
				userphone=userPhoneEditText.getText().toString().trim();
				getMessage();
			}
		});
		
		SubmitPhonebutton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(userPhoneEditText.getText().toString().trim()))
				{
					Toast.makeText(ShopRebindPhoneSteptwoActivity.this, "请输入手机号",Toast.LENGTH_SHORT).show();
					return;
				}
				if(userPhoneEditText.getText().toString().trim().length()!=11)
				{
					Toast.makeText(ShopRebindPhoneSteptwoActivity.this, "请输入正确的手机号",Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(messageCodeEditText.getText().toString().trim()))
				{
					Toast.makeText(ShopRebindPhoneSteptwoActivity.this, "请输入验证码",Toast.LENGTH_SHORT).show();
					return;
				}
				if(!userPhoneEditText.getText().toString().trim().equals(userphone))
				{
					Toast.makeText(ShopRebindPhoneSteptwoActivity.this, "请保持手机号一致",Toast.LENGTH_SHORT).show();
					return;
				}
				ColorDialog.showRoundProcessDialog(ShopRebindPhoneSteptwoActivity.this,R.layout.loading_process_dialog_color);
				RequestParams params = new RequestParams();
				params.add("token", share.getString("token", ""));
				params.add("mobile", userPhoneEditText.getText().toString().trim());
				params.add("code", messageCodeEditText.getText().toString().trim());
				params.add("version", ConstantParamPhone.VERSION);
				params.add("oldcode", oldCode);
				params.setContentEncoding("UTF-8");
				AsyncHttpClient client = new AsyncHttpClient();
				//保存cookie，自动保存到了shareprefercece  
		        PersistentCookieStore myCookieStore = new PersistentCookieStore(ShopRebindPhoneSteptwoActivity.this);    
		        client.setCookieStore(myCookieStore); 
		        client.post(ConstantParamPhone.IP+ConstantParamPhone.CHANGE_PHONE, params,new TextHttpResponseHandler(){

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
					{
						Toast.makeText(ShopRebindPhoneSteptwoActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) 
					{
						//获取短信发送状态
						ResponsBean responsBean=new Gson().fromJson(arg2, ResponsBean.class);
						if(responsBean.getStatus().equals("-1006"))
						{
							//Toast.makeText(ProductListActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
							//Toast.makeText(getActivity(), "出错啦！", Toast.LENGTH_SHORT).show();
							MyDialog.showDialog(ShopRebindPhoneSteptwoActivity.this, "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									MyDialog.cancel();
								}
							}, new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent=new Intent(ShopRebindPhoneSteptwoActivity.this,LoginActivity.class);
									startActivity(intent);
								}
							});
						}
						if(responsBean.getStatus().equals("-1103"))
						{
							Toast.makeText(ShopRebindPhoneSteptwoActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
							return;
						}
						if(responsBean.getStatus().equals("-1101"))
						{
							Toast.makeText(ShopRebindPhoneSteptwoActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
							return;
						}
						if(responsBean.getStatus().equals("-1102"))
						{
							Toast.makeText(ShopRebindPhoneSteptwoActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
							return;
						}
						if(responsBean.getStatus().equals("-1104"))
						{
							Toast.makeText(ShopRebindPhoneSteptwoActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
							return;
						}
						if(responsBean.getStatus().equals("-1105"))
						{
							Toast.makeText(ShopRebindPhoneSteptwoActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
							return;
						}
						if(responsBean.getStatus().equals("1"))
						{
							Toast.makeText(ShopRebindPhoneSteptwoActivity.this, "绑定成功啦", Toast.LENGTH_LONG).show();
							//保存用户信息
							SharedPreferences.Editor edit = share.edit(); //编辑文件 
							edit.putString("mobile", userphone);
					    	edit.commit();//保存数据信息 
					    	Intent intent=new Intent(ShopRebindPhoneSteptwoActivity.this,MainActivity.class);
							startActivity(intent);
							finish();
						}
						else
						{
							Toast.makeText(ShopRebindPhoneSteptwoActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
							return;
						}
					}
					@Override
					public void onFinish() 
					{
						// TODO Auto-generated method stub
						ColorDialog.dissmissProcessDialog();
					}
				});
				
			}
		});
	}
	private void getMessage() 
	{
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("mobile", userPhoneEditText.getText().toString().trim());
		params.add("type", "bind");
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(ShopRebindPhoneSteptwoActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.post(ConstantParamPhone.IP+ConstantParamPhone.SEND_SMS, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(ShopRebindPhoneSteptwoActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				//获取短信发送状态
				ResponsBean responsBean=new Gson().fromJson(arg2, ResponsBean.class);
				if(responsBean.getStatus().equals("-1103"))
				{
					Toast.makeText(ShopRebindPhoneSteptwoActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
					return;
				}
				if(responsBean.getStatus().equals("-3000"))
				{
					Toast.makeText(ShopRebindPhoneSteptwoActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
					return;
				}
				if(responsBean.getStatus().equals("1"))
				{
					time.start();
				}
				//开始计时
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
			}
		});
	}
	class TimeCount extends CountDownTimer 
	{
		public TimeCount(long millisInFuture, long countDownInterval) 
		{
		    super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		@Override
		public void onFinish() 
		{
			//计时完毕时触发
			btnGetmess.setTextColor(btnGetmess.getResources().getColor(R.color.text_color_red));
			btnGetmess.setText("获取验证码");
			btnGetmess.setClickable(true);
			btnGetmess.setPressed(false);
		}
		@Override
		public void onTick(long millisUntilFinished)
		{//计时过程显示
			btnGetmess.setTextColor(btnGetmess.getResources().getColor(R.color.title_color));
			btnGetmess.setClickable(false);
			btnGetmess.setPressed(true);
			btnGetmess.setText(millisUntilFinished /1000+"秒");
		}
	}

}
