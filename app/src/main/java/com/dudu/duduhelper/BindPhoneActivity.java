package com.dudu.duduhelper;

import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.widget.MyDialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class BindPhoneActivity extends BaseActivity {

	private Button editPhonebutton;
	private TextView phoneNumTextView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bind_phone);
		initHeadView("绑定手机号", true, false, 0);
		DuduHelperApplication.getInstance().addActivity(this);
		if(TextUtils.isEmpty(share.getString("mobile", "")))
		{
			MyDialog.showDialog(BindPhoneActivity.this, "尚未绑定手机号，是否绑定手机号", true, true, "确定", "取消", new OnClickListener() 
	    	{
				
				@Override
				public void onClick(View arg0) 
				{
					// TODO Auto-generated method stub
					Intent intent=new Intent(BindPhoneActivity.this,LoginBindPhoneActivity.class);
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
		phoneNumTextView=(TextView) this.findViewById(R.id.phoneNumTextView);
		phoneNumTextView.setText(share.getString("mobile", ""));
		editPhonebutton=(Button) this.findViewById(R.id.editPhonebutton);
		editPhonebutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(BindPhoneActivity.this,ShopRebindPhoneSteponeActivity.class);
				startActivity(intent);
				
			}
		});
	}

	

}
