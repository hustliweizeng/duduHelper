package com.dudu.duduhelper;

import org.apache.http.Header;

import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.bean.ProvienceBean;
import com.dudu.duduhelper.bean.ResponsBean;
import com.dudu.duduhelper.bean.UserBean;
import com.dudu.duduhelper.common.Util;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.dudu.duduhelper.widget.WaveHelper;
import com.dudu.duduhelper.widget.WaveView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

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
		animation.setDuration(1000); //设置持续时间5秒  
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
				animation.setDuration(1000); //设置持续时间5秒  
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
       // popupWindow.showAtLocation(ShopUserBankInfoActivity.this.findViewById(R.id.userbankinfolinearlayout), Gravity.BOTTOM, 0, 0);
        
        //ImageView closeImageButton=(ImageView) view.findViewById(R.id.closeImageButton);
//        closeImageButton.setOnClickListener(new OnClickListener() 
//        {
//			@Override
//			public void onClick(View arg0) 
//			{
//				// TODO Auto-generated method stub
//				popupWindow.dismiss();
//				AlphaAnimation animation = new AlphaAnimation((float)1, (float)0);
//				animation.setDuration(1000); //设置持续时间5秒  
//				animation.setAnimationListener(new AnimationListener() 
//				{
//					
//					@Override
//					public void onAnimationStart(Animation animation) 
//					{
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onAnimationRepeat(Animation animation) 
//					{
//						// TODO Auto-generated method stub
//						
//					}
//					
//					@Override
//					public void onAnimationEnd(Animation animation) 
//					{
//						// TODO Auto-generated method stub
//					}
//				});
//			}
//		});
	}
	
//	@Override
//	public void LeftButtonClick() 
//	{
//		// TODO Auto-generated method stub
//		if(!(userBankNameEditText.getText().toString().trim()).equals(share.getString("truename", ""))||
//				!(userBankNumEditText.getText().toString().trim()).equals(share.getString("bankno", ""))||
//				!(userBankNameTextView.getText().toString().trim()).equals(share.getString("bankname", ""))||
//				!(provienceTextView.getText().toString().trim()).equals(share.getString("province", ""))||
//				!(userBankCityTextView.getText().toString().trim()).equals(share.getString("city", ""))||
//				!(userBankSonNameEditText.getText().toString().trim()).equals(share.getString("moreinfo", "")))
//		{
//			MyDialog.showDialog(UserBankInfoActivity.this, "您的银行卡信息已发生变更，是否保存更改", true, true, "取消","保存",new OnClickListener()
//			{
//				
//				@Override
//				public void onClick(View v) 
//				{
//					// TODO Auto-generated method stub
//					MyDialog.cancel();
//					finish();
//				}
//			}, new OnClickListener() 
//			{
//				
//				@Override
//				public void onClick(View v) 
//				{
//					// TODO Auto-generated method stub
//					MyDialog.cancel();
//					SaveData();
//				}
//			});
//		}
//		else
//		{
//			super.LeftButtonClick();
//		}
//				
//	}

//	  @Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) 
//	  {
//		  if(data!=null)
//		  {
//			  
//	          switch(requestCode)
//	          {
//	              case 1:
//	              //来自按钮1的请求，作相应业务处理
//	        	  if(!TextUtils.isEmpty(data.getStringExtra("bank")))
//	        	  {
//	        		  userBankNameTextView.setText(data.getStringExtra("bank"));
//	        	  }
//	        	  break;
//	              case 2:
//	              //来自按钮2的请求，作相应业务处理
//	              {
//	            	  provienceCode=((ProvienceBean)data.getSerializableExtra("province")).getId();
//	            	  provienceTextView.setText(((ProvienceBean)data.getSerializableExtra("province")).getName());
//	            	  userBankCityTextView.setText(null);
//	              }
//	              break;
//	              case 3:
//	              //来自按钮2的请求，作相应业务处理
//	              {
//	            	  cityCode=((ProvienceBean)data.getSerializableExtra("city")).getId();
//	            	  userBankCityTextView.setText(((ProvienceBean)data.getSerializableExtra("city")).getName());
//	              }
//	              break;
//	           }
//		  }
//     }

}
