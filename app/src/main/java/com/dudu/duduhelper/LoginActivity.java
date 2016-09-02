package com.dudu.duduhelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.LoginBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;

public class LoginActivity extends BaseActivity
{
	private Button loginbutton;
	private EditText username;
	private EditText password;
	private Context mContext;
	private TextView judgeUserTextView;
	private final  String mPageName = "LoginActivity";
	private String userType="dianzhang";
	private ImageView userDelectIconBtn;
	private ImageView mimaDelectIconBtn;
	private String methord=ConstantParamPhone.USER_LOGIN;
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		DuduHelperApplication.getInstance().addActivity(this);
		initView();
		mContext = this;
		//MobclickAgent.setDebugMode(true);
//      SDK在统计Fragment时，需要关闭Activity自带的页面统计，
//		然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
		MobclickAgent.openActivityDurationTrack(false);
		MobclickAgent.updateOnlineConfig(this);
	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart( mPageName );
		MobclickAgent.onResume(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd( mPageName );
		MobclickAgent.onPause(this);
	}

	private void initView()
	{
		
		userDelectIconBtn=(ImageView) this.findViewById(R.id.userDelectIconBtn);
		mimaDelectIconBtn=(ImageView) this.findViewById(R.id.mimaDelectIconBtn);
		userDelectIconBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				   
			    username.setText("");
			}
		});
		mimaDelectIconBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				password.setText("");
			}
		});
		//通过意图对象传递数据过来
		if(!StringUtils.isEmpty(getIntent().getStringExtra("userType")))
		{
			userType=getIntent().getStringExtra("userType");
		}
		judgeUserTextView=(TextView) this.findViewById(R.id.judgeUserTextView);
		if(userType.equals("dianzhang"))
		{
			judgeUserTextView.setText("我是店员》");
			methord=ConstantParamPhone.USER_LOGIN;
		}
		else
		{
			judgeUserTextView.setText("我是商家》");
			methord=ConstantParamPhone.USER_LOGIN;
		}
		username=(EditText) this.findViewById(R.id.username);
		password=(EditText) this.findViewById(R.id.password);
		loginbutton=(Button) this.findViewById(R.id.loginbutton);
		//密码输入监听
		password.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				if(s.length()>0)
				{
					mimaDelectIconBtn.setVisibility(View.VISIBLE);
				}
				else
				{
					mimaDelectIconBtn.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after)
			{

			}

			@Override
			public void afterTextChanged(Editable s)
			{

			}
		});
		//姓名输入监听
		username.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				if(s.length()>0)
				{
					userDelectIconBtn.setVisibility(View.VISIBLE);
				}
				else
				{
					userDelectIconBtn.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after)
			{

			}

			@Override
			public void afterTextChanged(Editable s)
			{

			}
		});

		judgeUserTextView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				
				Intent intent=new Intent(LoginActivity.this,LoginActivity.class);
				if(userType.equals("dianzhang"))
				{
					intent.putExtra("userType", "dianyuan");
				}
				else
				{
					intent.putExtra("userType", "dianzhang");
				}
				startActivity(intent);
				finish();
			}
		});
		//登陆按钮监听
		loginbutton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(username.getText().toString().trim().equals(""))
				{
					Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_LONG).show();
					return;
				}
				if(password.getText().toString().trim().equals(""))
				{
					Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_LONG).show();
					return;
				}
				/**
				 * 登陆请求，1.参数封装，2.cookie保存，3.成功和失败的回调
				 */
				//请求联网时，主线程显示进度条
				ColorDialog.showRoundProcessDialog(LoginActivity.this,R.layout.loading_process_dialog_color);
				RequestParams params = new RequestParams();
				params.add("username", username.getText().toString().trim());
				params.add("password", Util.md5(password.getText().toString().trim()));
				String umeng_token = getSharedPreferences("umeng_token",MODE_PRIVATE).getString("token","");
				params.add("umeng_token",umeng_token);
				params.setContentEncoding("UTF-8");
				LogUtil.d("welcome","usernmae="+username.getText().toString().trim()+"paswword="+Util.md5(password.getText().toString().trim()));
				String url = ConstantParamPhone.USER_LOGIN;
		        HttpUtils.getConnection(context, params,url,"POST",new TextHttpResponseHandler()
				{
					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3)
					{
						Toast.makeText(LoginActivity.this, "网络不给力呀,请稍后再试", Toast.LENGTH_LONG).show();

					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2)
					{
						LogUtil.d("login",arg2);

						LoginBean loginBean = new Gson().fromJson(arg2,LoginBean.class);
						//判断返回状态是否成功
						if (ConstantParamPhone.SUCCESS.equalsIgnoreCase(loginBean.getCode())){
							
							//对bean的数据做非空判断
							
							/*//1.通过sp保存用户信息
							SharedPreferences.Editor edit = sp.edit();
							edit.putString("username",loginBean.getUser().getName())
							.putString("nickename",loginBean.getUser().getNickname())
									//手动添加
							.putString("mobile","18937228893")//loginBean.getUser().getMobile())
							//2.存储商店信息
							.putString("id",loginBean.getShop().getId())
							.putString("shopLogo",loginBean.getShop().getLogo())
							.putString("shopName",loginBean.getShop().getName())
							//3.储存今日状态
							.putString("todayIncome",loginBean.getTodaystat().getIncome())
							//4.存储总计状态
							.putString("frozenMoney",loginBean.getTotalstat().getFreezemoney())
							.putString("useableMoney",loginBean.getTotalstat().getUsablemoney())
							//在后台处理
							.apply();
							//跳转到主页*/

							Intent intent = new Intent(LoginActivity.this,MainActivity.class);
							//startActivity(new Intent(context,MainActivity.class));
							startActivity(intent);
							finish();

						}else if (ConstantParamPhone.FAIL.equalsIgnoreCase(loginBean.getCode())){
							Toast.makeText(LoginActivity.this,"信息存储有误",Toast.LENGTH_LONG).show();
						}

					}
					@Override
					public void onFinish()
					{
						ColorDialog.dissmissProcessDialog();
					}
				});
			}
		});
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			DuduHelperApplication.getInstance().exit();
		}
		return super.onKeyDown(keyCode, event);
	}
}
