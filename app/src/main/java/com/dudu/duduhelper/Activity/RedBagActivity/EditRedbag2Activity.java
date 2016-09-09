package com.dudu.duduhelper.Activity.RedBagActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.SystemBarTintManager;
import com.umeng.message.PushAgent;

import java.util.LinkedList;

/**
 * Created by lwz on 2016/8/19.
 */
public class EditRedbag2Activity extends Activity implements View.OnClickListener {

	private LinearLayout ll_content_edit_redab2;
	private LinearLayout ll_condition_edit_redbag2;
	//红包使用条件的默认加入位置
	private  int position = 8;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_edit_redbag2);
		initview();

	}

	private void initview() {
		ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
		//红包内容的总布局
		ll_content_edit_redab2 = (LinearLayout) findViewById(R.id.ll_content_edit_redab2);
		//红包使用限制的布局view
		ll_condition_edit_redbag2 = (LinearLayout) findViewById(R.id.ll_condition_edit_redbag2);
		ImageView iv_add_edit_redbag2= (ImageView) findViewById(R.id.iv_add_edit_redbag2);
		//添加条目设置点击事件
		iv_add_edit_redbag2.setOnClickListener(this);

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
			//添加红包使用条件
			case R.id.iv_add_edit_redbag2:
				createConditon();

				break;
		}
	}

	private void createConditon() {
		//载入红包条件的布局
		//1创建集合管理红包条件
		LinkedList<LinearLayout> conditions = new LinkedList<>();

		LinearLayout item_condition_edit_redbag = (LinearLayout) View.inflate(this,R.layout.item_condition_edit_redbag,null);
		ll_content_edit_redab2.addView(item_condition_edit_redbag,position);
		//再添加的话，位置会变动
		position++;
		Log.d("condition","创建了条件"+position);


	}
}
