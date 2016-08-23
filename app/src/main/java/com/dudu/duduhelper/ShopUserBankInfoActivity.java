package com.dudu.duduhelper;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dudu.duduhelper.application.DuduHelperApplication;

public class ShopUserBankInfoActivity extends BaseActivity 
{
	private LinearLayout kaihuBanklin;
	private LinearLayout kaihuPriviencelin;
	private LinearLayout userBankCityLin;
	private TextView userBankNameTextView;
	private TextView provienceTextView;
	private TextView userBankCityTextView;
	private String provienceCode;
	private String cityCode;
	private TextView userBankOpenNameTextView;
	private TextView userBankNumTextView;
	private TextView userBankSonNameEditText;
	private TextView unbindButton;
	 private PopupWindow popupWindow;
	//编辑按钮
	private Button editButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_user_bank_info);
		initHeadView("我的银行卡", true, false, 0);
		editButton=(Button) this.findViewById(R.id.selectTextClickButton);
		editButton.setText("编辑");
		editButton.setVisibility(View.VISIBLE);
		DuduHelperApplication.getInstance().addActivity(this);
		initView();
		initData();
	}
	private void initView() 
	{
		// TODO Auto-generated method stub
		unbindButton = (TextView) this.findViewById(R.id.unbindButton);
		userBankSonNameEditText=(TextView) this.findViewById(R.id.userBankSonNameTextView);
		userBankNumTextView=(TextView) this.findViewById(R.id.userBankNumTextView);
		userBankNameTextView=(TextView) this.findViewById(R.id.userBankNameTextView);
		userBankCityTextView=(TextView) this.findViewById(R.id.userBankCityTextView);
		provienceTextView=(TextView) this.findViewById(R.id.provienceTextView);
		userBankOpenNameTextView=(TextView) this.findViewById(R.id.userBankOpenNameTextView);
		kaihuBanklin=(LinearLayout) this.findViewById(R.id.kaihuBanklin);
		kaihuPriviencelin=(LinearLayout) this.findViewById(R.id.kaihuPriviencelin);
		userBankCityLin=(LinearLayout) this.findViewById(R.id.userBankCityLin);
		editButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(ShopUserBankInfoActivity.this,ShopUserBankInfoEditActivity.class);
				startActivity(intent);
			}
		});
		unbindButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				//弹出解除绑定窗口
				popUbingPhone();
			}
			
		});
	}
	private void initData() 
	{
		// TODO Auto-generated method stub
		if(!TextUtils.isEmpty(share.getString("truename", "")))
		{
			userBankNameTextView.setText("*"+share.getString("truename", "").substring(1,share.getString("truename", "").length()));
		}
		if(!TextUtils.isEmpty(share.getString("bankno", "")))
		{
			userBankNumTextView.setText("尾号："+share.getString("bankno", "").substring(share.getString("bankno", "").length()-4,share.getString("bankno", "").length()));
		}
		if(!TextUtils.isEmpty(share.getString("bankname", "")))
		{
			userBankNameTextView.setText(share.getString("bankname", ""));
		}
		if(!TextUtils.isEmpty(share.getString("province", "")))
		{
			provienceTextView.setText(share.getString("province", ""));
		}
		if(!TextUtils.isEmpty(share.getString("city", "")))
		{
			userBankCityTextView.setText(share.getString("city", ""));
		}
		if(!TextUtils.isEmpty(share.getString("moreinfo", "")))
		{
			userBankSonNameEditText.setText(share.getString("moreinfo", ""));
		}
	}
	
	private void popUbingPhone()
	{
		AlphaAnimation animation = new AlphaAnimation((float)0, (float)1.0);
		animation.setDuration(500); //设置持续时间5秒
		LayoutInflater layoutInflater = (LayoutInflater)ShopUserBankInfoActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        View view = layoutInflater.inflate(R.layout.shop_unbind_phone_pop_window, null);  
        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,  LayoutParams.MATCH_PARENT);  
        popupWindow.setFocusable(true);  
        popupWindow.setOutsideTouchable(true);  
        //这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景  
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000)); 
        //设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAsDropDown(ShopUserBankInfoActivity.this.findViewById(R.id.head));
        LinearLayout dismissLayout = (LinearLayout) view.findViewById(R.id.dismissLayout);
        dismissLayout.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v)
			{
				popupWindow.dismiss();
				AlphaAnimation animation = new AlphaAnimation((float)1, (float)0);
				animation.setDuration(500); //设置持续时间5秒
				animation.setAnimationListener(new AnimationListener() 
				{
					
					@Override
					public void onAnimationStart(Animation animation) 
					{
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) 
					{
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) 
					{
						// TODO Auto-generated method stub
					}
				});
			}
		});

	}

}
