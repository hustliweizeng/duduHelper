package com.dudu.duduhelper.Activity.MyInfoActivity;


import org.apache.http.Header;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Activity.WelcomeActivity.LoginActivity;
import com.dudu.duduhelper.Activity.WelcomeActivity.LoginBindPhoneActivity;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.bean.ResponsBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.dudu.duduhelper.R;
public class GetCashActivity extends BaseActivity implements TextWatcher
{
	private EditText getCashEditText;
	private Button getCashbutton;
	private RelativeLayout userBankRelLine;
	private TextView cardBankName;
	private TextView cardNumTextView;
	private TimeCount time;
	private Button btnGetmess;
	private EditText messageCodeEditText;
	private TextView getTotalCash;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_cash);
		initHeadView("我要提现", true, true, R.drawable.icon_historical);
		DuduHelperApplication.getInstance().addActivity(this);
		time = new TimeCount(60000, 1000);//构造CountDownTimer对象
		if(TextUtils.isEmpty(share.getString("mobile", "")))
		{
			MyDialog.showDialog(GetCashActivity.this, "尚未绑定手机号，是否绑定手机号", true, true, "确定", "取消", new OnClickListener() 
	    	{
				
				@Override
				public void onClick(View arg0) 
				{
					// TODO Auto-generated method stub
					Intent intent=new Intent(GetCashActivity.this,LoginBindPhoneActivity.class);
					startActivity(intent);
				}
			}, new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					finish();
				}
			});
		}
		initView();
	}
	
	private void initView() 
	{
		// TODO Auto-generated method stub
		messageCodeEditText=(EditText) this.findViewById(R.id.messageCodeEditText);
		getTotalCash=(TextView) this.findViewById(R.id.getTotalCash);
		getTotalCash.setText("￥"+" "+this.share.getString("money", ""));
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
		cardBankName=(TextView) this.findViewById(R.id.cardBankName);
		cardBankName.setText(share.getString("bankname", ""));
		cardNumTextView=(TextView) this.findViewById(R.id.cardNumTextView);
		if(!TextUtils.isEmpty(share.getString("bankno", "")))
		{
			cardNumTextView.setText("尾号"+share.getString("bankno", "").substring(share.getString("bankno", "").length()-4, share.getString("bankno", "").length()));
			//cardNumTextView.setText(share.getString("bankno", ""));
		}
		else
		{
			cardNumTextView.setText("点击绑定银行卡");
		}
		userBankRelLine=(RelativeLayout) this.findViewById(R.id.userBankRelLine);
		getCashEditText=(EditText) this.findViewById(R.id.getCashEditText);
		getCashbutton=(Button) this.findViewById(R.id.getCashbutton);
		//输入文本监听事件
		getCashEditText.addTextChangedListener(this);
		//银行卡点击跳转
		userBankRelLine.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(GetCashActivity.this,ShopUserBankInfoEditActivity.class);
				startActivityForResult(intent, 1);
			}
		});
		getCashbutton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(share.getString("bankno", "")))
				{
					MyDialog.showDialog(GetCashActivity.this, "尚未绑定银行卡，请绑定", false, true, "取消", "确定",new OnClickListener() 
					{
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) 
						{
							// TODO Auto-generated method stub
							Intent intent=new Intent(GetCashActivity.this,ShopUserBankInfoEditActivity.class);
							startActivityForResult(intent, 1);
							MyDialog.cancel();
						}
					});
					return;
				}
				if(Double.parseDouble(getCashEditText.getText().toString().trim())>Double.parseDouble(GetCashActivity.this.share.getString("money", "")))
				{
					MyDialog.showDialog(GetCashActivity.this, "当前提现金额不足，请重新输入", false, true, "取消", "确定",new OnClickListener() 
					{
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) 
						{
							// TODO Auto-generated method stub
							getCashEditText.setText("");
							MyDialog.cancel();
						}
					});
					return;
				}
				if(TextUtils.isEmpty(messageCodeEditText.getText().toString().trim()))
				{
					Toast.makeText(GetCashActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
					return;
				}
				//提现方法
				// TODO Auto-generated method stub
				ColorDialog.showRoundProcessDialog(GetCashActivity.this,R.layout.loading_process_dialog_color);
				RequestParams params = new RequestParams();
				params.add("token", share.getString("token", ""));
				params.add("money", getCashEditText.getText().toString().trim());
				params.add("code", messageCodeEditText.getText().toString().trim());
				params.add("version", ConstantParamPhone.VERSION);
				params.setContentEncoding("UTF-8");
				AsyncHttpClient client = new AsyncHttpClient();
				//保存cookie，自动保存到了shareprefercece  
		        PersistentCookieStore myCookieStore = new PersistentCookieStore(GetCashActivity.this);    
		        client.setCookieStore(myCookieStore); 
		        client.post(ConstantParamPhone.IP+ConstantParamPhone.OUT_USER_MONEY, params,new TextHttpResponseHandler(){

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
					{
						Toast.makeText(GetCashActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
					}
					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) 
					{
						ResponsBean responsBean=new Gson().fromJson(arg2,ResponsBean.class);
						if(responsBean.getStatus().equals("-1006"))
						{
							MyDialog.showDialog(GetCashActivity.this, "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() 
							{
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									MyDialog.cancel();
								}
							}, new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Intent intent=new Intent(GetCashActivity.this,LoginActivity.class);
									startActivity(intent);
								}
							});
						}
						if(responsBean.getStatus().equals("-1020"))
						{
							Toast.makeText(GetCashActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
							return;
							//保存用户信息
							
						}
						if(responsBean.getStatus().equals("-1021"))
						{
							Toast.makeText(GetCashActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
							return;
							//保存用户信息
						}
						if(responsBean.getStatus().equals("1"))
						{
							MyDialog.showDialog(GetCashActivity.this, "您的提现申请已经提交，请您耐心等待运营商处理！", false, true, "","确定", null, new OnClickListener()
							{
								
								@Override
								public void onClick(View v) 
								{
									// TODO Auto-generated method stub
									MyDialog.cancel();
									GetCashActivity.this.finish();
								}
							});
						}
						else
						{
							Toast.makeText(GetCashActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
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
	@Override
	public void RightButtonClick() 
	{
		// TODO Auto-generated method stub
		super.RightButtonClick();
		Intent intent=new Intent(GetCashActivity.this,GetCashHistoryListActivity.class);
		startActivity(intent);
	}
    //文本框监听，判断文本框内容是否为空值，改变button状态
	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
	}

	@Override
	public void afterTextChanged(Editable s) {
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		if(TextUtils.isEmpty(getCashEditText.getText().toString().trim()))
		{
			getCashbutton.setEnabled(false);
			getCashbutton.setBackgroundResource(R.drawable.btn_nopass_bg);
		}
		else
		{
			getCashbutton.setEnabled(true);
			getCashbutton.setBackgroundResource(R.drawable.btn_larg_bg);
		}
	}
	private void getMessage() 
	{
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("mobile",share.getString("mobile", ""));
		params.add("type", "outmoney");
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(GetCashActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.post(ConstantParamPhone.IP+ConstantParamPhone.SEND_SMS, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(GetCashActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				//获取短信发送状态
				ResponsBean responsBean=new Gson().fromJson(arg2, ResponsBean.class);
				if(responsBean.getStatus().equals("-1103"))
				{
					Toast.makeText(GetCashActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
					return;
				}
				if(responsBean.getStatus().equals("-3000"))
				{
					Toast.makeText(GetCashActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
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
	@Override
	public void onActivityResult(int arg0, int arg1, Intent arg2) 
	{
		// TODO Auto-generated method stub
		if(!TextUtils.isEmpty(share.getString("bankno", "")))
		{
			cardNumTextView.setText("尾号"+share.getString("bankno", "").substring(share.getString("bankno", "").length()-4, share.getString("bankno", "").length()));
		}
		if(!TextUtils.isEmpty(share.getString("bankname", "")))
		{
			cardBankName.setText(share.getString("bankname", ""));
		}
	}

}
