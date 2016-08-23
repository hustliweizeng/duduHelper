package com.dudu.duduhelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.bean.SalerBean;
import com.dudu.duduhelper.bean.UserBean;
import com.dudu.duduhelper.common.Util;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
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
//		MobclickAgent.setAutoLocation(true);
//		MobclickAgent.setSessionContinueMillis(1000);
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
		// TODO Auto-generated method stub
		userDelectIconBtn=(ImageView) this.findViewById(R.id.userDelectIconBtn);
		mimaDelectIconBtn=(ImageView) this.findViewById(R.id.mimaDelectIconBtn);
		userDelectIconBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
			    username.setText("");
			}
		});
		mimaDelectIconBtn.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				// TODO Auto-generated method stub

			}
		});
		//姓名输入监听
		username.addTextChangedListener(new TextWatcher()
		{
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub

			}

			@Override
			public void afterTextChanged(Editable s)
			{
				// TODO Auto-generated method stub

			}
		});

		judgeUserTextView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				//请求联网时，主线程显示进度条
				ColorDialog.showRoundProcessDialog(LoginActivity.this,R.layout.loading_process_dialog_color);
				RequestParams params = new RequestParams();
				params.add("username", username.getText().toString().trim());
				params.add("password", Util.md5(password.getText().toString().trim()));
				//获取本地token参数
				String umeng_token = getSharedPreferences("umeng_token",MODE_PRIVATE).getString("token","");
				//params.add("version", ConstantParamPhone.VERSION);//老接口的参数
				params.add("umeng_token",umeng_token);
				//设置编码方式
				params.setContentEncoding("UTF-8");
				AsyncHttpClient client = new AsyncHttpClient();
				//保存cookie，自动保存到了shareprefercece
		        PersistentCookieStore myCookieStore = new PersistentCookieStore(LoginActivity.this);
		        client.setCookieStore(myCookieStore);
		        client.get(ConstantParamPhone.IP+methord, params,new TextHttpResponseHandler()
				{
					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3)
					{
						Toast.makeText(LoginActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
						//临时跳转到主页，方便genymotion调试
//						Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//						startActivity(intent);
						finish();

					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2)
					{
						//输出返回数据
						Log.d("gsons",arg2);
						System.out.print(arg2);
						if(userType.equals("dianzhang"))
						{
							UserBean userBean =new Gson().fromJson(arg2,UserBean.class);
							if(userBean.getStatus().equals("1"))
							{
								//保存用户信息
								SharedPreferences.Editor edit = share.edit(); //编辑文件
								edit.putString("usertype","dianzhang");
								if(!TextUtils.isEmpty(userBean.getData().getId()))
								{
									edit.putString("userid",userBean.getData().getId());
								}
								if(!TextUtils.isEmpty(userBean.getData().getUsername()))
								{
									edit.putString("username",userBean.getData().getUsername());
								}
								if(!TextUtils.isEmpty(password.getText().toString()))
								{
									edit.putString("password",Util.md5(password.getText().toString().trim()));
								}
								if(!TextUtils.isEmpty(userBean.getData().getShopname()))
								{
									edit.putString("shopname",userBean.getData().getShopname());
								}
								if(!TextUtils.isEmpty(userBean.getData().getShoplogo()))
								{
									edit.putString("shoplogo",userBean.getData().getShoplogo());
								}
								if(!TextUtils.isEmpty(userBean.getData().getToken()))
								{
									edit.putString("token", userBean.getData().getToken());
								}
								if(!TextUtils.isEmpty(userBean.getData().getMoney()))
								{
									edit.putString("money", userBean.getData().getMoney());
								}
								if(!TextUtils.isEmpty(userBean.getData().getAgentname()))
								{
									edit.putString("getagentname", userBean.getData().getAgentname());
								}
								if(!(userBean.getData().getTodaystat()==null))
								{
									edit.putString("todaystatvisitor", userBean.getData().getTodaystat().getVisiter());
									edit.putString("todaystatbuyer", userBean.getData().getTodaystat().getBuyer());
									edit.putString("todaystatorder", userBean.getData().getTodaystat().getOrder());
									edit.putString("todaystatincome", userBean.getData().getTodaystat().getIncome());
								}
								else
								{
									edit.putString("todaystatvisitor", "0");
									edit.putString("todaystatbuyer", "0");
									edit.putString("todaystatorder", "0");
									edit.putString("todaystatincome", "0");
								}
								if(!(userBean.getData().getTotalstat()==null))
								{
									edit.putString("totalstatvisitor", userBean.getData().getTotalstat().getVisiter());
									edit.putString("totalstatbuyer", userBean.getData().getTotalstat().getBuyer());
									edit.putString("totalstatorder", userBean.getData().getTotalstat().getOrder());
									edit.putString("totalstatincome", userBean.getData().getTotalstat().getIncome());
								}
								else
								{
									edit.putString("totalstatvisitor", "0");
									edit.putString("totalstatbuyer", "0");
									edit.putString("totalstatorder", "0");
									edit.putString("totalstatincome", "0");
								}
								if(!(userBean.getData().getBank()==null))
								{
									edit.putString("bankname", userBean.getData().getBank().getBankname());
									edit.putString("bankno", userBean.getData().getBank().getBankno());
									edit.putString("truename", userBean.getData().getBank().getTruename());
									edit.putString("province", userBean.getData().getBank().getProvince());
									edit.putString("city", userBean.getData().getBank().getCity());
									edit.putString("moreinfo", userBean.getData().getBank().getMoreinfo());
								}
								else
								{
									edit.putString("bankname", "");
									edit.putString("bankno", "");
									edit.putString("truename", "");
									edit.putString("province", "");
									edit.putString("city", "");
									edit.putString("moreinfo", "");
								}
							    if(TextUtils.isEmpty(userBean.getData().getMobile()))
							    {
							    	edit.commit();//保存数据信息
							    	MyDialog.showDialog(LoginActivity.this, "尚未绑定手机号，是否绑定手机号", true, true, "确定", "取消", new OnClickListener()
							    	{

										@Override
										public void onClick(View arg0)
										{
											// TODO Auto-generated method stub
											Intent intent=new Intent(LoginActivity.this,LoginBindPhoneActivity.class);
											startActivity(intent);
											finish();
										}
									}, new OnClickListener() {

										@Override
										public void onClick(View arg0) {
											// TODO Auto-generated method stub
											Intent intent=new Intent(LoginActivity.this,MainActivity.class);
											startActivity(intent);
											finish();
										}
									});

							    }
							    else
							    {
							    	edit.putString("mobile", userBean.getData().getMobile());
							    	edit.commit();//保存数据信息
									Intent intent=new Intent(LoginActivity.this,MainActivity.class);
									startActivity(intent);
									finish();
							    }
							    edit.commit();//保存数据信息

							}
							else
							{
								Toast.makeText(LoginActivity.this, userBean.getInfo(), Toast.LENGTH_LONG).show();
								//保存用户信息
								return;
							}
						}
						else
						{
							SalerBean salerBean =new Gson().fromJson(arg2,SalerBean.class);
							if(salerBean.getStatus().equals("1"))
							{
								//保存用户信息
								SharedPreferences.Editor edit = share.edit(); //编辑文件
								if(!TextUtils.isEmpty(salerBean.getData().getId()))
								{
									edit.putString("userid",salerBean.getData().getId());
								}
								if(!TextUtils.isEmpty(salerBean.getData().getUsername()))
								{
									edit.putString("username",salerBean.getData().getUsername());
								}
								if(!TextUtils.isEmpty(salerBean.getData().getToken()))
								{
									edit.putString("token", salerBean.getData().getToken());
								}
								edit.commit();//保存数据信息
								Intent intent=new Intent(LoginActivity.this,MainActivity.class);
								startActivity(intent);
								finish();
							}
							else
							{
								Toast.makeText(LoginActivity.this, salerBean.getInfo(), Toast.LENGTH_LONG).show();
								//保存用户信息
								return;
							}
						}




						/**
						 * 新版本的方法
						 */
//							ShopUserLoginBean shopUserLoginBean =new Gson().fromJson(arg2,ShopUserLoginBean.class);
//							if(shopUserLoginBean.getCode().equals(ConstantParamPhone.SUCCESS))
//							{
//								//保存用户信息
//								SharedPreferences.Editor edit = share.edit(); //编辑文件
//								edit.putString("usertype","dianzhang");
//								if(!TextUtils.isEmpty(userBean.getData().getId()))
//								{
//									edit.putString("userid",userBean.getData().getId());
//								}
//								if(!TextUtils.isEmpty(userBean.getData().getUsername()))
//								{
//									edit.putString("username",userBean.getData().getUsername());
//								}
//								if(!TextUtils.isEmpty(password.getText().toString()))
//								{
//									edit.putString("password",Util.md5(password.getText().toString().trim()));
//								}
//								if(!TextUtils.isEmpty(userBean.getData().getShopname()))
//								{
//									edit.putString("shopname",userBean.getData().getShopname());
//								}
//								if(!TextUtils.isEmpty(userBean.getData().getShoplogo()))
//								{
//									edit.putString("shoplogo",userBean.getData().getShoplogo());
//								}
//								if(!TextUtils.isEmpty(userBean.getData().getToken()))
//								{
//									edit.putString("token", userBean.getData().getToken());
//								}
//								if(!TextUtils.isEmpty(userBean.getData().getMoney()))
//								{
//									edit.putString("money", userBean.getData().getMoney());
//								}
//								if(!TextUtils.isEmpty(userBean.getData().getAgentname()))
//								{
//									edit.putString("getagentname", userBean.getData().getAgentname());
//								}
//								if(!(userBean.getData().getTodaystat()==null))
//								{
//									edit.putString("todaystatvisitor", userBean.getData().getTodaystat().getVisiter());
//									edit.putString("todaystatbuyer", userBean.getData().getTodaystat().getBuyer());
//									edit.putString("todaystatorder", userBean.getData().getTodaystat().getOrder());
//									edit.putString("todaystatincome", userBean.getData().getTodaystat().getIncome());
//								}
//								else
//								{
//									edit.putString("todaystatvisitor", "0");
//									edit.putString("todaystatbuyer", "0");
//									edit.putString("todaystatorder", "0");
//									edit.putString("todaystatincome", "0");
//								}
//								if(!(userBean.getData().getTotalstat()==null))
//								{
//									edit.putString("totalstatvisitor", userBean.getData().getTotalstat().getVisiter());
//									edit.putString("totalstatbuyer", userBean.getData().getTotalstat().getBuyer());
//									edit.putString("totalstatorder", userBean.getData().getTotalstat().getOrder());
//									edit.putString("totalstatincome", userBean.getData().getTotalstat().getIncome());
//								}
//								else
//								{
//									edit.putString("totalstatvisitor", "0");
//									edit.putString("totalstatbuyer", "0");
//									edit.putString("totalstatorder", "0");
//									edit.putString("totalstatincome", "0");
//								}
//								if(!(userBean.getData().getBank()==null))
//								{
//									edit.putString("bankname", userBean.getData().getBank().getBankname());
//									edit.putString("bankno", userBean.getData().getBank().getBankno());
//									edit.putString("truename", userBean.getData().getBank().getTruename());
//									edit.putString("province", userBean.getData().getBank().getProvince());
//									edit.putString("city", userBean.getData().getBank().getCity());
//									edit.putString("moreinfo", userBean.getData().getBank().getMoreinfo());
//								}
//								else
//								{
//									edit.putString("bankname", "");
//									edit.putString("bankno", "");
//									edit.putString("truename", "");
//									edit.putString("province", "");
//									edit.putString("city", "");
//									edit.putString("moreinfo", "");
//								}
//							    if(TextUtils.isEmpty(shopUserLoginBean.getUser().getMobile()))
//							    {
//							    	edit.commit();//保存数据信息
//							    	MyDialog.showDialog(LoginActivity.this, "尚未绑定手机号，是否绑定手机号", true, true, "确定", "取消", new OnClickListener()
//							    	{
//
//										@Override
//										public void onClick(View arg0)
//										{
//											// TODO Auto-generated method stub
//											Intent intent=new Intent(LoginActivity.this,LoginBindPhoneActivity.class);
//											startActivity(intent);
//											finish();
//										}
//									}, new OnClickListener() {
//
//										@Override
//										public void onClick(View arg0) {
//											// TODO Auto-generated method stub
//											Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//											startActivity(intent);
//											finish();
//										}
//									});
//
//							    }
//							    else
//							    {
//							    	edit.putString("mobile",shopUserLoginBean.getUser().getMobile());
//							    	edit.commit();//保存数据信息
//									Intent intent=new Intent(LoginActivity.this,MainActivity.class);
//									startActivity(intent);
//									finish();
//							    }
//							    edit.commit();//保存数据信息
//
//							}
//							else
//							{
//								Toast.makeText(LoginActivity.this, shopUserLoginBean.getMsg(), Toast.LENGTH_LONG).show();
//								//保存用户信息
//								return;
//							}
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
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		// TODO Auto-generated method stub
		if(keyCode == KeyEvent.KEYCODE_BACK)
		{
			DuduHelperApplication.getInstance().exit();
		}
		return super.onKeyDown(keyCode, event);
	}
}
