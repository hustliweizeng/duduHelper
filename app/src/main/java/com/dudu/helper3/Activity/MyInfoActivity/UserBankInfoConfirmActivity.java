package com.dudu.helper3.Activity.MyInfoActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.Activity.WelcomeActivity.LoginActivity;
import com.dudu.duduhelper.R;
import com.dudu.helper3.bean.ResponsBean;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.widget.ColorDialog;
import com.dudu.helper3.widget.MyDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

public class UserBankInfoConfirmActivity extends BaseActivity {

	private Button getPermessionbutton;
	private Button btnGetmess;
	private EditText messageCodeEditText;
	private TimeCount time;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_userbank_info_confirm);
		initHeadView("信息验证", true, false, 0);
		time = new TimeCount(60000, 1000);//构造CountDownTimer对象
		initView();
	}

	private void initView() 
	{
		// TODO Auto-generated method stub
		messageCodeEditText=(EditText) this.findViewById(R.id.messageCodeEditText);
		getPermessionbutton=(Button) this.findViewById(R.id.getPermessionbutton);
		btnGetmess=(Button) this.findViewById(R.id.btnGetmess);
		btnGetmess.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				
				// TODO Auto-generated method stub
				getMessage();
			}
		});
		getPermessionbutton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(messageCodeEditText.getText().toString().trim()))
				{
					Toast.makeText(UserBankInfoConfirmActivity.this,"请输入验证码", Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{
					ColorDialog.showRoundProcessDialog(UserBankInfoConfirmActivity.this,R.layout.loading_process_dialog_color);
					RequestParams params = new RequestParams();
					params.add("token", share.getString("token", ""));
					//params.add("mobile",share.getString("mobile", ""));
					params.add("code", messageCodeEditText.getText().toString().trim());
					params.add("version", ConstantParamPhone.VERSION);
					params.setContentEncoding("UTF-8");
					new AsyncHttpClient().get(ConstantParamPhone.IP+ConstantParamPhone.BIND_PHONE, params,new TextHttpResponseHandler(){

						@Override
						public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
						{
							Toast.makeText(UserBankInfoConfirmActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
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
								MyDialog.showDialog(UserBankInfoConfirmActivity.this, "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										MyDialog.cancel();
									}
								}, new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										// TODO Auto-generated method stub
										Intent intent=new Intent(UserBankInfoConfirmActivity.this,LoginActivity.class);
										startActivity(intent);
									}
								});
							}
							if(responsBean.getStatus().equals("-1103"))
							{
								Toast.makeText(UserBankInfoConfirmActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
								return;
							}
							if(responsBean.getStatus().equals("-1102"))
							{
								Toast.makeText(UserBankInfoConfirmActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
								return;
							}
							if(responsBean.getStatus().equals("-1104"))
							{
								Toast.makeText(UserBankInfoConfirmActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
								return;
							}
							if(responsBean.getStatus().equals("-1105"))
							{
								Toast.makeText(UserBankInfoConfirmActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
								return;
							}
							if(responsBean.getStatus().equals("-1101"))
							{
								Toast.makeText(UserBankInfoConfirmActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
								return;
							}
							if(responsBean.getStatus().equals("1"))
							{
								Toast.makeText(UserBankInfoConfirmActivity.this, "验证成功啦", Toast.LENGTH_LONG).show();
//								SharedPreferences.Editor edit = share.edit(); //编辑文件 
//								edit.putString("mobile", "");
//						    	edit.commit();//保存数据信息
								Intent intent=new Intent(UserBankInfoConfirmActivity.this,ShopUserBankInfoEditActivity.class);
								//intent.putExtra("code", messageCodeEditText.getText().toString().trim());
								startActivity(intent);
								finish();
							}
							else
							{
								Toast.makeText(UserBankInfoConfirmActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
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
					//保存用户信息
//					SharedPreferences.Editor edit = share.edit(); //编辑文件 
//					edit.putString("mobile", userphone);
//			    	edit.commit();//保存数据信息 
//					Intent intent=new Intent(RebindPhoneSteponeActivity.this,RebindPhoneSteptwoActivity.class);
//					startActivity(intent);
				}
			}
		});
	}
	private void getMessage() 
	{
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("mobile",share.getString("mobile", ""));
		params.add("type", "bind");
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		new AsyncHttpClient().post(ConstantParamPhone.IP+ConstantParamPhone.SEND_SMS, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(UserBankInfoConfirmActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				//获取短信发送状态
				ResponsBean responsBean=new Gson().fromJson(arg2, ResponsBean.class);
				if(responsBean.getStatus().equals("-1103"))
				{
					Toast.makeText(UserBankInfoConfirmActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
					return;
				}
				if(responsBean.getStatus().equals("-3000"))
				{
					Toast.makeText(UserBankInfoConfirmActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
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
