package com.dudu.duduhelper;

import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.widget.MyDialog;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShopSettingActivity extends BaseActivity implements OnClickListener
{
	private RelativeLayout changePhoneNumRel;
	private TextView phoneNumText;
	private RelativeLayout shopEditRel;
	private RelativeLayout passwordEditRel;
	private RelativeLayout mendianRel;
	private RelativeLayout memberRel;
	private Button logoutButton;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_setting);
		initHeadView("设置", true, false, 0);
		initView();
		initData();
	}
    
	//加载视图
	private void initView() 
	{
		logoutButton = (Button) this.findViewById(R.id.logoutButton);
		logoutButton.setOnClickListener(this);
		mendianRel = (RelativeLayout) this.findViewById(R.id.mendianRel);
		mendianRel.setOnClickListener(this); 
		memberRel = (RelativeLayout) this.findViewById(R.id.memberRel);
		memberRel.setOnClickListener(this);
		changePhoneNumRel = (RelativeLayout) this.findViewById(R.id.changePhoneNumRel);
		changePhoneNumRel.setOnClickListener(this);
		phoneNumText = (TextView) this.findViewById(R.id.phoneNumText);
		shopEditRel = (RelativeLayout) this.findViewById(R.id.shopEditRel);
		shopEditRel.setOnClickListener(this);
		passwordEditRel = (RelativeLayout) this.findViewById(R.id.passwordEditRel);
		passwordEditRel.setOnClickListener(this);
	}
	
	//设置数据
	private void initData() 
	{
		// TODO Auto-generated method stub
		if(!TextUtils.isEmpty(share.getString("mobile", "")))
		{
		   phoneNumText.setText(share.getString("mobile", ""));
		}
		else
		{
			phoneNumText.setText("请先绑定手机号");
		}
	}

	@Override
	public void onClick(View v) 
	{
		Intent intent = null;
		switch (v.getId())
		{
			case R.id.changePhoneNumRel:
				intent=new Intent(ShopSettingActivity.this,ShopRebindPhoneSteponeActivity.class);
				break;
			case R.id.shopEditRel:
				intent=new Intent(ShopSettingActivity.this,ShopInfoEditActivity.class);
				break;
			case R.id.passwordEditRel:
				intent=new Intent(ShopSettingActivity.this,ShopPasswordEditActivity.class);
				break;
			case R.id.mendianRel:
				intent=new Intent(ShopSettingActivity.this,ShopListManagerActivity.class);
				break;
			case R.id.memberRel:
				intent=new Intent(ShopSettingActivity.this,ShopMemberListActivity.class);
				break;
			case R.id.logoutButton:
                MyDialog.showDialog(ShopSettingActivity.this, "  退出登录将清空用户信息，是否退出",true, true, "取消","确定", new OnClickListener() 
                {
					@Override
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub
						MyDialog.cancel();
					}
				}, 
				new OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub
						share.edit().clear().commit();
						DuduHelperApplication.getInstance().exit();
					}
				});
                return;
			default:
				break;
		}
		startActivity(intent);
	}
}
