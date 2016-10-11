package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.widget.MyAlertDailog;

/**
 * @author
 * @version 1.0
 * @date 2016/9/23
 */

public class StoreMoneyActivity extends BaseActivity implements View.OnClickListener {
	private TextView redbage_check;
	private TextView activity_check;
	private EditText ed_num;
	private TextView tv_price;
	private Button btn_subimit;
	//默认选择红包
	private boolean isRedbag = true;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_store_money);
		initHeadView("充值",true,false,0);
		initView();
	}

	private void initView() {
		redbage_check = (TextView) findViewById(R.id.redbage_check);
		activity_check = (TextView) findViewById(R.id.activity_check);
		ed_num = (EditText) findViewById(R.id.ed_num);
		tv_price = (TextView) findViewById(R.id.tv_price);
		btn_subimit = (Button) findViewById(R.id.btn_subimit);
		redbage_check.setOnClickListener(this);
		activity_check.setOnClickListener(this);
		btn_subimit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_subimit:
				submit();
			case R.id.redbage_check	:
				redbage_check.setTextColor(getResources().getColor(R.color.text_green_color));
				activity_check.setTextColor(getResources().getColor(R.color.text_dark_color));
				isRedbag = true;
				break;
			case R.id.activity_check:
				redbage_check.setTextColor(getResources().getColor(R.color.text_dark_color));
				activity_check.setTextColor(getResources().getColor(R.color.text_green_color));
				isRedbag =false;
				break;
		}
	}

	private void submit() {
		// validate
		String num = ed_num.getText().toString().trim();
		if (TextUtils.isEmpty(num)) {
			Toast.makeText(this, "num不能为空", Toast.LENGTH_SHORT).show();
			AlertDialog dailog = new  AlertDialog.Builder(context,R.style.AppTheme_Dialog).create();
			dailog.show();
			//获取弹窗界面
			Window window = dailog.getWindow();
			//获取屏幕的宽度
			WindowManager windowManager = getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			WindowManager.LayoutParams lp = dailog.getWindow().getAttributes();
			lp.width = (int)(display.getWidth()); //设置宽度    
			dailog.getWindow().setAttributes(lp);
			//设置布局
			window.setContentView(R.layout.dailog_confirm_order);


			return;
		}



	}
}
