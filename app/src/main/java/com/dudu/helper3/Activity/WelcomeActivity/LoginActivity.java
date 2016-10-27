package com.dudu.helper3.Activity.WelcomeActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.Activity.MainActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.application.DuduHelperApplication;
import com.dudu.helper3.Utils.Util;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.LoginBean;
import com.dudu.helper3.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

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
	private TextView findPasswordTextView;
	private boolean shopIsoPen;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		DuduHelperApplication.getInstance().addActivity(this);
		initView();
		mContext = this;
		MobclickAgent.openActivityDurationTrack(false);
		//MobclickAgent.updateOnlineConfig(this);
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
		//忘记密码
		findPasswordTextView = (TextView) findViewById(R.id.findPasswordTextView);
		findPasswordTextView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			//进入忘记密码页面
			startActivity(new Intent(context,ForgetPwdCertifyMobileActivity.class));
			}
		});
		userDelectIconBtn.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
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
				//保存用户名和密码到本地
				sp.edit().putString("loginname",username.getText().toString().trim())
				.putString("password",Util.md5(password.getText().toString().trim()))		
				.commit();		
				
				String url = ConstantParamPhone.USER_LOGIN;
		        HttpUtils.getConnection(context, params,url,"POST",new TextHttpResponseHandler()
				{
					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3)
					{
						arg3.printStackTrace();

						Toast.makeText(LoginActivity.this, arg2, Toast.LENGTH_LONG).show();

					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2)
					{
						LogUtil.d("login",arg2);

						LoginBean loginBean = new Gson().fromJson(arg2,LoginBean.class);
						//判断返回状态是否成功
						if (ConstantParamPhone.SUCCESS.equalsIgnoreCase(loginBean.getCode())){
							
							//判断手机绑定没有
							if (TextUtils.isEmpty(loginBean.getUser().getMobile())){
								Toast.makeText(context,"当前账号未绑定，请先绑定手机号",Toast.LENGTH_LONG).show();
								startActivity(new Intent(context,LoginBindPhoneActivity.class));
								finish();
								return;//不能进入了
							}
							requestStatus();//请求店铺状态
							//对bean的数据做非空判断
							String isshopuser = loginBean.getUser().getIsshopuser();
							boolean isManager = true;
							if ("1".equals(isshopuser)){
								isManager = false;
								LogUtil.d("manager","false");
							}
							if ("0".equals(isshopuser)) {
								isManager = true;
							}
								
							//1.通过sp保存用户信息
							SharedPreferences.Editor edit = sp.edit();
							edit.putString("username", loginBean.getUser().getName())
							.putString("nickename", loginBean.getUser().getNickname())//手动添加
							.putString("mobile", loginBean.getUser().getMobile())
							//2.存储商店信息
							.putString("id", loginBean.getShop().getId())
							.putString("shopLogo", loginBean.getShop().getLogo())
							.putString("shopName", loginBean.getShop().getName())
							//3.储存今日状态
							.putString("todayIncome", loginBean.getTodaystat().getIncome())
							//4.存储总计状态
							.putString("frozenMoney", loginBean.getTotalstat().getFreezemoney())
							.putString("useableMoney", loginBean.getTotalstat().getUsablemoney())
							//储存店员状态
							.putBoolean("isManager", isManager)
							//在后台处理
							.apply();
							//跳转到主页
							

							startActivity(new Intent(context, MainActivity.class));
							finish();

						}else if (ConstantParamPhone.FAIL.equalsIgnoreCase(loginBean.getCode())){
							Toast.makeText(LoginActivity.this,"用户名或者密码不正确",Toast.LENGTH_LONG).show();
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
	/**
	 * 判断店员管理是否可用
	 */
	public void requestStatus(){
		HttpUtils.getConnection(context, null, ConstantParamPhone.GET_GUEST_ISOPEN, "GET", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				LogUtil.d("res",s);
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						if (object.getString("status").equals("1")){
							shopIsoPen = true;
						}else{
							shopIsoPen = false;
							
						}
						sp.edit().putBoolean("shopstatus",shopIsoPen).commit();
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
}
