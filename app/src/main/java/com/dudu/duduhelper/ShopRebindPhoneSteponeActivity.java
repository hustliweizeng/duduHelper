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

public class ShopRebindPhoneSteponeActivity extends BaseActivity {

	private Button getPermessionbutton;
	private Button btnGetmess;
	private EditText messageCodeEditText;
	private TextView phoneNumText;
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
		// TODO Auto-generated method stub
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

				getMessage();
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
					//ColorDialog.showRoundProcessDialog(ShopRebindPhoneSteponeActivity.this,R.layout.loading_process_dialog_color);
					//RequestParams params = new RequestParams();
					//请求网络验证短信，绑定成功
					Toast.makeText(context,"绑定成功",Toast.LENGTH_LONG).show();
					//修改sp中的mobie值
					finish();
				}
			}
		});
	}
	private void getMessage() 
	{
		RequestParams params = new RequestParams();
		params.add("type", "bind");
		params.add("mobile",sp.getString("mobile", ""));
		params.setContentEncoding("UTF-8");
		HttpUtils.getConnection(context,params,ConstantParamPhone.GET_SMS_CONFIRM, "get",new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3)
			{
				Toast.makeText(ShopRebindPhoneSteponeActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2)
			{

				//开始计时
				showResidueSeconds();
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
