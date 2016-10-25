package com.dudu.duduhelper.Activity.RedBagActivity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.widget.SystemBarTintManager;
import com.umeng.message.PushAgent;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by lwz on 2016/8/19.
 */
public class EditRedbag1Activity extends Activity implements View.OnClickListener {
	public SharedPreferences share;
	private EditText ed_edit_redbag_title;
	private LinearLayout startTimeRel;
	private LinearLayout finishTimeRel;
	private TextView startTimeTextView;
	private TextView finishTimeTextView;
	private int flag;
	private int flag1;
	private String startTime;
	private String endTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_edit_redbag1);
		initview();

	}

	private void initview() {
		Button btn_create_redbag1 = (Button) findViewById(R.id.btn_create_redbag1);
		ed_edit_redbag_title = (EditText) findViewById(R.id.ed_edit_redbag_title);
		startTimeRel = (LinearLayout) this.findViewById(R.id.startTimeRel);
		finishTimeRel = (LinearLayout) this.findViewById(R.id.finishTimeRel);
		startTimeTextView = (TextView) this.findViewById(R.id.startTimeTextView);
		finishTimeTextView = (TextView) this.findViewById(R.id.finishTimeTextView);

		ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		btn_create_redbag1.setOnClickListener(this);

		//设置起始和终止时间
		startTimeRel.setOnClickListener(new View.OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				showDataDialog(startTimeTextView);
			}
		});
		finishTimeRel.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				showDataDialog(finishTimeTextView);
			}
		});
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
			case R.id.btn_create_redbag1:
				//传递参数到下一页
				String title = ed_edit_redbag_title.getText().toString().trim();
				if (TextUtils.isEmpty(title) ||TextUtils.isEmpty(startTime) ||TextUtils.isEmpty(endTime)){
					Toast.makeText(this,"输入不完整",Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent(this,EditRedbag2Activity.class);
				intent.putExtra("starTime", startTime);
				intent.putExtra("endTime", endTime);
				intent.putExtra("title",title);
				startActivity(intent);

				break;
			case R.id.backButton:
				finish();
				break;
		}
	}
	
	// 选择日期,调用系统主题
	@SuppressLint("InlinedApi")
	private void showDataDialog(final TextView textView) {
		flag = 0;
		flag1 = 0;
		datastr.delete(0, datastr.length());
		Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
		Date mydate = new Date(); // 获取当前日期Date对象
		mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期
		DatePickerDialog datePicker = new DatePickerDialog(
				EditRedbag1Activity.this, AlertDialog.THEME_HOLO_LIGHT,
				new DatePickerDialog.OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
					                      int monthOfYear, int dayOfMonth) {
						if (flag == 0) {
							flag = 1;
							datastr.append(year + "-" + (monthOfYear + 1) + "-"
									+ dayOfMonth + " ");
							textView.setText(datastr);//只显示日期即可
							showTimePickerDialog(textView);
						}
					}
				}, mycalendar.get(Calendar.YEAR),
				mycalendar.get(Calendar.MONTH),
				mycalendar.get(Calendar.DAY_OF_MONTH));
		datePicker.show();
	}
	// 用来拼接日期和时间，最终用来显示的
	private StringBuilder datastr = new StringBuilder("");
	@SuppressLint("InlinedApi")
	public void showTimePickerDialog(final TextView textView) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		TimePickerDialog dialog = new TimePickerDialog(this,
				AlertDialog.THEME_HOLO_LIGHT, new TimePickerDialog.OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker arg0, int hour, int minute) {
				if (flag1 == 0) {
					flag1 = 1;
					datastr.append(hour + ":" + minute + ":00");
					if(textView.equals(startTimeTextView)){
						startTime = datastr.toString();
					}else {
						endTime = datastr.toString();
					}
				}
			}

		}, hour, minute, true);
		dialog.show();
	}
}
