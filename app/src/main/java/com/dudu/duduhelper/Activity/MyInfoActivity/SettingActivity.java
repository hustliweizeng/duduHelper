package com.dudu.duduhelper.Activity.MyInfoActivity;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.Activity.WelcomeActivity.ForgetPwdCertifyMobileActivity;
import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Utils.CleanAppCache;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.widget.MyDialog;
import com.dudu.duduhelper.wxapi.WXEntryActivity;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import com.dudu.duduhelper.R;
public class SettingActivity extends BaseActivity 
{
	private RelativeLayout bind_phone_line;
	private TextView phoneNumTextView;
	private Button logoutButton;
	private RelativeLayout shopecodeimageRel;
	private RelativeLayout paycodeimageRel;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initHeadView("设置", true, false, 0);
		initView();
	}
	private void initView()
	{
		// TODO Auto-generated method stub
		shopecodeimageRel=(RelativeLayout) this.findViewById(R.id.shopecodeimageRel);
		paycodeimageRel=(RelativeLayout) this.findViewById(R.id.paycodeimageRel);
		logoutButton=(Button) this.findViewById(R.id.logoutButton);
		phoneNumTextView=(TextView) this.findViewById(R.id.phoneNumTextView);
		phoneNumTextView.setText(share.getString("mobile", ""));
		bind_phone_line=(RelativeLayout) this.findViewById(R.id.bind_phone_line);
		shopecodeimageRel.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(SettingActivity.this,WXEntryActivity.class);
				intent.putExtra("type", ConstantParamPhone.GET_SHOPE_CODE);
				startActivity(intent);
			}
		});
		paycodeimageRel.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(SettingActivity.this,WXEntryActivity.class);
				intent.putExtra("type", ConstantParamPhone.GET_CASHIER_CODE);
				startActivity(intent);
			}
		});
		bind_phone_line.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(SettingActivity.this,ForgetPwdCertifyMobileActivity.class);
				startActivity(intent);
			}
		});
		logoutButton.setOnClickListener(new OnClickListener() 
		{

			@Override
			public void onClick(View v) 
			{

				MyDialog.showDialog(SettingActivity.this, "  退出登录将清空用户信息，是否退出",true, true, "取消","确定", new OnClickListener() {
					
					@Override
					public void onClick(View v) 
					{
						LogUtil.d("clear","取消清除sp");
						MyDialog.cancel();

					}
				}, 
				new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{

						sp.edit().clear().commit();
						LogUtil.d("clear","清除sp");
						CleanAppCache.cleanApplicationData(context);
						DuduHelperApplication.getInstance().exit();
						//请求登出
						requestLogOut();
					}
				});
				
			}
		});

		  

	}

	private void requestLogOut() {
		RequestParams params = new RequestParams();

		HttpUtils.getConnection(context, params, ConstantParamPhone.LOG_OUT, "post", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				Toast.makeText(context,"已经退出当前账户，请重新登陆",Toast.LENGTH_LONG).show();
			}
		});
	}

}
