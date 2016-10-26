package com.dudu.helper3;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dudu.duduhelper.R;
import com.dudu.helper3.application.DuduHelperApplication;
import com.dudu.helper3.widget.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;

public class BaseActivity extends FragmentActivity 
{
	private ImageButton backButton;
	public ImageButton selectClickButton;
	public TextView headtitle;
	public SharedPreferences share;
	public SharedPreferences sp;
	public Context context;

	public String FORCE_UPDATE="";
	public String url ="";
	public RelativeLayout relayout_mytitle;
	public static String  umeng_token = "dudu";

	@Override
	protected void onCreate(Bundle arg0) 
	{
		context = this;
		//把所有页面加到application
		DuduHelperApplication.getInstance().addActivity(this);
		//在所有界面统计app启动次数
		PushAgent.getInstance(this).onAppStart();
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		//每个activity界面设置为沉浸式状态栏，android 4.4以上才支持
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) 
		{
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);

		//设置通知栏（状态栏）的颜色

		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.status_Bar_color);//通知栏所需颜色

		//每个activity都可以获取到sp中保存的用户信息
		share = getSharedPreferences("userinfo", MODE_PRIVATE);
		sp = getSharedPreferences("userconig",Context.MODE_PRIVATE);

		//umeng_token = getSharedPreferences("umengtoken",MODE_PRIVATE).getString("token","");
		DuduHelperApplication.getInstance().addActivity(this);
		MobclickAgent.updateOnlineConfig(this);//获取强制更新在线参数
		FORCE_UPDATE=MobclickAgent.getConfigParams(this, "force_update" );
		MobclickAgent.setOnlineConfigureListener(new UmengOnlineConfigureListener(){
		  @Override
		  public void onDataReceived(JSONObject data)
		  {
			  JSONObject haha=data;
			  System.out.println(haha);
		  }
		});
		/**
		 * 设置友盟更新
		 */
		UmengUpdateAgent.setUpdateOnlyWifi(false);
		UmengUpdateAgent.update(this);
		
		
		
		if(StringUtils.isEmpty(FORCE_UPDATE)) 
		{    
            return;    
        }    
		UmengUpdateAgent.setDialogListener(new UmengDialogButtonListener() 
		{
			
			@Override
			public void onClick(int arg0) 
			{
				// TODO Auto-generated method stub
				 switch (arg0) 
				 {
			        case UpdateStatus.Update:
			            break;
			        default:  
		        	if(!FORCE_UPDATE.equals("0"))
		        	{
		        		DuduHelperApplication.getInstance().exit();
		        	}
		        }
		    }
		});
	}
	

	//做版本兼容，
	@TargetApi(19) 
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
	}
	//添加友盟统计
	public void onResume() {
	    super.onResume();
	    MobclickAgent.onResume(this);       //统计时长
	}
	public void onPause() {
	    super.onPause();
	    MobclickAgent.onPause(this);
	}

	// true就是可见的

	/**
	 * 初始化每个activity的头布局
	 * @param title 当前activity的名字
	 * @param LeftButtonVisable 左边button是否可见
	 * @param RightButtonVisable	右边button是否可见
     * @param Imageid 右侧要替换的图片id,当没有时，填0
	 *
     */
	@SuppressLint("ResourceAsColor") 
	public void initHeadView(String title, boolean LeftButtonVisable,boolean RightButtonVisable, int Imageid) 
	{
		// TODO Auto-generated method stub
		relayout_mytitle= (RelativeLayout) this.findViewById(R.id.relayout_mytitle);
		headtitle = (TextView) this.findViewById(R.id.title);
		headtitle.setText(title);
		backButton = (ImageButton) this.findViewById(R.id.backButton);
		selectClickButton = (ImageButton) this.findViewById(R.id.selectClickButton);
		if (!LeftButtonVisable) 
		{
			backButton.setVisibility(View.GONE);
		} else {
			backButton.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					LeftButtonClick();
				}
			});
		}
		if (!RightButtonVisable) 
		{
			selectClickButton.setVisibility(View.GONE);
		}
		else 
		{
			selectClickButton.setVisibility(View.VISIBLE);
			selectClickButton.setImageResource(Imageid);
			selectClickButton.setOnClickListener(new OnClickListener() 
			{
				@Override
				public void onClick(View v) 
				{
					// TODO Auto-generated method stub
					RightButtonClick();
				}

			});
		}
	}

	public void LeftButtonClick() {
		finish();
	}
	//当右边按钮更改时，重写该监听方法
	public void RightButtonClick() {

	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
	}
	
}
