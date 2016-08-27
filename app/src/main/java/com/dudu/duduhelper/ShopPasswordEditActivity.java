package com.dudu.duduhelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

public class ShopPasswordEditActivity extends BaseActivity
{
    private LinearLayout passwordLayout;
    private LinearLayout phoneLayout;
    private Button phoneBindButton;
    private Button passwordBindButton;
    private TextView textView;
    private TextView textView1;
    private ImageView showPasswordImageBtn;
    private EditText passWordEdit;
    private TextView bindPhoneText;
    private boolean showPass = false;
	private Button btnGetmess;
	private LinearLayout ll_finishi_change_password;
	private RelativeLayout rl_oldpassword_change_pwd;
	private Boolean isFromSms  = false;
	private EditText messageCodeEditText;
	private String confirmCode;
	private EditText textxinmimaconf;
	private EditText textxinmimaconf2;
	private String temp1;
	private EditText ed_oldpwd_change_pwd;
	private String oldPwd;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_password_edit);
		initHeadView("密码管理", true, false, 0);
		initView();
	}

	private void initView()
	{
		bindPhoneText = (TextView) this.findViewById(R.id.bindPhoneText);
		//获取本地保存的电话号码
		String mobile = sp.getString("mobile","");
		if(!TextUtils.isEmpty(mobile))
		{
			bindPhoneText.setText(mobile);
		}
		else
		{
			bindPhoneText.setText("请先绑定手机号");
			//绑定手机号页面
			bindPhoneText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					startActivity(new Intent(context,ShopRebindPhoneSteponeActivity.class));
				}
			});
		}
		passWordEdit = (EditText) this.findViewById(R.id.passWordEdit);
		showPasswordImageBtn = (ImageView) this.findViewById(R.id.showPasswordImageBtn);
		//是否隐藏密码
		showPasswordImageBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if(showPass)
				{
					//现在按钮是打开状态，密码可见,点击需要隐藏
					showPasswordImageBtn.setImageResource(R.drawable.icon_yanjing);
					passWordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					showPass = false;

				}
				else
				{
					//现在是关闭状态，密码不可见
					showPasswordImageBtn.setImageResource(R.drawable.icon_biyan);
					passWordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
					showPass = true;
				}
			}
		});
		//设置TextView显示不同的颜色
		textView = (TextView) this.findViewById(R.id.textView);
		textView1 = (TextView) this.findViewById(R.id.textView1);
		SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());
		ForegroundColorSpan greenSpan = new ForegroundColorSpan(0xFF3DD6BC);
		builder.setSpan(greenSpan, textView.getText().toString().length()-12, textView.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		textView.setText(builder);
		textView1.setText(builder);

		passwordLayout = (LinearLayout) this.findViewById(R.id.passwordLayout);
		ll_finishi_change_password = (LinearLayout) findViewById(R.id.ll_finishi_change_password);
		phoneLayout = (LinearLayout) this.findViewById(R.id.phoneLayout);
		phoneBindButton = (Button) this.findViewById(R.id.phoneBindButton);
		rl_oldpassword_change_pwd = (RelativeLayout) findViewById(R.id.rl_oldpassword_change_pwd);
		passwordBindButton = (Button) this.findViewById(R.id.passwordBindButton);
		messageCodeEditText = (EditText) findViewById(R.id.messageCodeEditText);
		Button submitPhoneBtn = (Button) findViewById(R.id.submitPhoneBtn);
		//显示输入密码页面，隐藏当前页面
		submitPhoneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				//从验证码方式修改
				confirmCode = messageCodeEditText.getText().toString().trim();
				if (TextUtils.isEmpty(confirmCode)){
					Toast.makeText(context,"验证码不能为空",Toast.LENGTH_LONG).show();
				}else {
					//验证码不是空的时候，才切换页面
					phoneLayout.setVisibility(View.GONE);
					passwordLayout.setVisibility(View.VISIBLE);
					rl_oldpassword_change_pwd.setVisibility(View.GONE);
					isFromSms = true;
				}
			}
		});
		btnGetmess = (Button) findViewById(R.id.btnGetmess);
		//发送短信验证
		btnGetmess.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				RequestParams params = new RequestParams();
				//测试用直接发
				params.put("mobile","18937228893");
				params.put("type","password");
				//params.setContentEncoding("UTF-8");
				String url = ConstantParamPhone.GET_SMS_CONFIRM;

				HttpUtils.getConnection(context, params, url, "get", new TextHttpResponseHandler() {
					@Override
					public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
						Toast.makeText(context,"短信发送失败",Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(int i, Header[] headers, String s) {
						//显示剩余有效时间
						LogUtil.d("success",s);
						showResidueSeconds();

					}
				});
			}
		});
		//手机绑定界面
		phoneBindButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				phoneLayout.setVisibility(View.VISIBLE);
				passwordLayout.setVisibility(View.GONE);
				phoneBindButton.setBackgroundColor(phoneBindButton.getResources().getColor(R.color.bg_white_color));
				phoneBindButton.setTextColor(phoneBindButton.getResources().getColor(R.color.text_dark_color));
				passwordBindButton.setBackgroundColor(passwordBindButton.getResources().getColor(R.color.bg_color));
				passwordBindButton.setTextColor(passwordBindButton.getResources().getColor(R.color.text_middledark_color));
			}
		});
		//密码验证界面
		passwordBindButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				passwordLayout.setVisibility(View.VISIBLE);
				phoneLayout.setVisibility(View.GONE);
				rl_oldpassword_change_pwd.setVisibility(View.VISIBLE);
				passwordBindButton.setBackgroundColor(phoneBindButton.getResources().getColor(R.color.bg_white_color));
				passwordBindButton.setTextColor(phoneBindButton.getResources().getColor(R.color.text_dark_color));
				phoneBindButton.setBackgroundColor(passwordBindButton.getResources().getColor(R.color.bg_color));
				phoneBindButton.setTextColor(passwordBindButton.getResources().getColor(R.color.text_middledark_color));
			}
		});
		//老密码
		ed_oldpwd_change_pwd = (EditText) findViewById(R.id.ed_oldpwd_change_pwd);
		//新密码
		passWordEdit = (EditText) findViewById(R.id.passWordEdit);
		//新密码确认
		textxinmimaconf2 = (EditText) findViewById(R.id.textxinmimaconf2);

		//提交按钮
		Button loginbutton = (Button) findViewById(R.id.loginbutton);
		loginbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				oldPwd = ed_oldpwd_change_pwd.getText().toString().trim();
				temp1 = passWordEdit.getText().toString().trim();
				String temp2 = textxinmimaconf2.getText().toString().trim();
				//如果同旧密码，需要验证旧密码是否为空
				if (!isFromSms){
					if (TextUtils.isEmpty(oldPwd)){
						Toast.makeText(context,"密码不能为空",Toast.LENGTH_SHORT).show();
						return;
					}
				}
				if (TextUtils.isEmpty(temp1) ){
					Toast.makeText(context,"密码不能为空",Toast.LENGTH_SHORT).show();
				}else {
					//验证2次密码是否一致
					if (temp1.equals(temp2)){
						//请求网络连接
						requestHttpConnection();
					}else {
						Toast.makeText(context,"两次密码不一致",Toast.LENGTH_SHORT).show();

					}
				}

			}
		});
	}

	private void requestHttpConnection() {
		RequestParams params = new RequestParams();
		params.put("password",temp1);
		String url = null;

		//判断是短信验证
		if (isFromSms){
			//通过验证码修改0
			url = ConstantParamPhone.CHANGE_PWD_BYSMS;
			params.put("code",confirmCode);
			HttpUtils.getConnection(context, params, url, "POST", handler);

		}else {

			//通过老密码修改
			url = ConstantParamPhone.CHANGE_PWD_BYPWD;
			params.put("oldpassword",oldPwd);
			HttpUtils.getConnection(context,params,url,"post",handler);
		}
	}
	//请求网络的响应
	TextHttpResponseHandler handler = new TextHttpResponseHandler() {
		@Override
		public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
			Toast.makeText(context,"密码修改失败，请稍后再试",Toast.LENGTH_LONG).show();
		}

		@Override
		public void onSuccess(int i, Header[] headers, String s) {
			Toast.makeText(context,"修改成功，请重新登陆",Toast.LENGTH_LONG).show();
			startActivity(new Intent(context,LoginActivity.class));
			//退出所有界面
			DuduHelperApplication application = (DuduHelperApplication)getApplication();
			application.exit();
		}
	};
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
               // LogUtil.d("lasttime","剩余时间:"+lastTime/1000);
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
