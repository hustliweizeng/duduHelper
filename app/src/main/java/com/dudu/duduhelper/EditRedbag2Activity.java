package com.dudu.duduhelper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.SystemBarTintManager;
import com.umeng.message.PushAgent;

/**
 * Created by lwz on 2016/8/19.
 */
public class EditRedbag2Activity extends Activity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_edit_redbag2);
		initview();

	}

	private void initview() {
		ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
		backButton.setOnClickListener(this);


	}

	public void init(){
		//在所有界面统计app启动次数
		PushAgent.getInstance(this).onAppStart();
		//每个activity界面设置为沉浸式状态栏，android 4.4以上才支持
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
		{
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		//设置通知栏（状态栏）的颜色
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.status_Bar_color_red);//通知栏所需颜色

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

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case  R.id.backButton:
				finish();
				break;
			case R.id.btn_create_redbag2_submit:
				//请求网络
				String url = ConstantParamPhone.BASE_URL+ConstantParamPhone.ADD_REDBAG;
				break;
		}
	}
}
