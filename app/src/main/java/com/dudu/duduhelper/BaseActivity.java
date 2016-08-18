package com.dudu.duduhelper;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
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
import android.widget.Toast;

import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.bean.UpdateBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.service.UpdateService;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.dudu.duduhelper.widget.SystemBarTintManager;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;
import com.umeng.analytics.onlineconfig.UmengOnlineConfigureListener;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengDialogButtonListener;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UpdateStatus;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.json.JSONObject;

public class BaseActivity extends FragmentActivity 
{
	private ImageButton backButton;
	public ImageButton selectClickButton;
	public TextView headtitle;
	public SharedPreferences share;

	public String FORCE_UPDATE=""; 
	public RelativeLayout relayout_mytitle;

	@Override
	protected void onCreate(Bundle arg0) 
	{
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
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.status_Bar_color);//通知栏所需颜色
		share = getSharedPreferences("userinfo", MODE_PRIVATE);
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
     * @param Imageid 右侧要替换的图片id
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
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}
	//更新版本信息
	private void updateCheck() 
	{
		try 
		{
			// TODO Auto-generated method stub
			PackageManager manager = this.getPackageManager(); 
			PackageInfo info;
			info = manager.getPackageInfo(this.getPackageName(), 0);
	        final String appVersion = info.versionName; // 版本名  
	        //String currentVersionCode = info.versionCode; // 版本号  
	        //if(!appVersion.equals(RespBen.getApk_version()))//
	        RequestParams params = new RequestParams();
			params.setContentEncoding("UTF-8");
			AsyncHttpClient client = new AsyncHttpClient();
			//保存cookie，自动保存到了shareprefercece  
	        PersistentCookieStore myCookieStore = new PersistentCookieStore(BaseActivity.this);    
	        client.setCookieStore(myCookieStore); 
	        client.get(ConstantParamPhone.IP+ConstantParamPhone.GET_VERSION, params,new TextHttpResponseHandler()
			{
	
				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
				{
					Toast.makeText(BaseActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
				}
				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2) 
				{
					final UpdateBean updateBean=new Gson().fromJson(arg2,UpdateBean.class);
					if(!updateBean.getStatus().equals("1"))
					{
						Toast.makeText(BaseActivity.this, updateBean.getInfo(), Toast.LENGTH_LONG).show();
						//保存用户信息
					}
					else
					{
						if(!appVersion.equals(updateBean.getData().getVersion()))
						{
							MyDialog.showDialog(BaseActivity.this, "版本更新", true, true, "别烦我", "立即更新", new OnClickListener() 
							{
								
								@Override
								public void onClick(View v) 
								{
									// TODO Auto-generated method stub
									MyDialog.cancel();
								}
							}, new OnClickListener() {
								
								@Override
								public void onClick(View v) 
								{
									// TODO Auto-generated method stub
									Intent intent=new Intent(BaseActivity.this,UpdateService.class);
									intent.putExtra("apkUrl", updateBean.getData().getUrl());
							        startService(intent);
							        MyDialog.cancel();
								}
							});
						}
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
		catch (NameNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
//	        
	}
}
