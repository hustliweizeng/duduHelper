package com.dudu.duduhelper.Activity.SummaryActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.widget.WaveHelper;
import com.dudu.duduhelper.widget.WaveView;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ShopAccountDataActivity extends BaseActivity 
{
	private LinearLayout startTimeRel;
	private LinearLayout finishTimeRel;
	private TextView startTimeTextView;
	private TextView finishTimeTextView;
	private Button accountDatabutton;
	private TextView fangkeNumText;
	private TextView buyerNumText;
	private TextView orderNumText;
	//波浪助手
	private WaveHelper mWaveHelper;
	private WaveView waveView;
	private TextView all_trade;
	private TextView all_income;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_account_data);
		initHeadView("数据统计", true, false, 0);
		initView();
		DuduHelperApplication.getInstance().addActivity(this);
		Calendar c = Calendar.getInstance();
		String year = String.valueOf(c.get(Calendar.YEAR));
		String month =String.valueOf(c.get(Calendar.MONTH)+1);
		String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		String data=year+"-"+month+"-"+day;
	}
	
	private void initView() 
	{
		waveView = (WaveView) findViewById(R.id.wave);
		mWaveHelper = new WaveHelper(waveView,5000,0.2f);
		waveView.setShapeType(WaveView.ShapeType.SQUARE);
		waveView.setWaveColor(Color.parseColor("#03ffffff"),Color.parseColor("#07ffffff"));

		//总交易额
		all_trade = (TextView) findViewById(R.id.allFeeTextView);
		//总收入
		all_income = (TextView) findViewById(R.id.allIncomeTextView);
		
		
		orderNumText=(TextView) this.findViewById(R.id.orderNumText);
		fangkeNumText=(TextView) this.findViewById(R.id.fangkeNumText);
		buyerNumText=(TextView) this.findViewById(R.id.buyerNumText);
		accountDatabutton=(Button) this.findViewById(R.id.accountDatabutton);
		startTimeRel=(LinearLayout) this.findViewById(R.id.startTimeRel);
		finishTimeRel=(LinearLayout) this.findViewById(R.id.finishTimeRel);
		startTimeTextView=(TextView) this.findViewById(R.id.startTimeTextView);
		finishTimeTextView=(TextView) this.findViewById(R.id.finishTimeTextView);
		accountDatabutton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(ShopAccountDataActivity.this,ShopAccountWatchActivity.class);
				String start = startTimeTextView.getText().toString().trim();
				String end = finishTimeTextView.getText().toString().trim();
				if (TextUtils.isEmpty(start) ||TextUtils.isEmpty(end)){
					Toast.makeText(context,"请选择开始和结束日期",Toast.LENGTH_SHORT).show();
					return;
				}
				intent.putExtra("start",start);
				intent.putExtra("end",end);
				startActivity(intent);
			}
		});
		startTimeRel.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				showDataDialog(startTimeTextView);
			}
		});
		finishTimeRel.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				showDataDialog(finishTimeTextView);
			}
		});
		//设置统计数据
		fangkeNumText.setText(sp.getString("totalVistor","0"));//访客
		buyerNumText.setText(sp.getString("totalBuyer","0"));//买家
		orderNumText.setText(sp.getString("totalOrder","0"));//订单

		all_trade.setText(sp.getString("totalTrade",""));//总交易额
		all_income.setText(sp.getString("totalIncome",""));//总收入
	}
	//选择日期,调用系统主题
    @SuppressLint("InlinedApi") 
    private void showDataDialog(final TextView textView)
    {
    	 Calendar mycalendar=Calendar.getInstance(Locale.CHINA);
         Date mydate=new Date(); //获取当前日期Date对象
         mycalendar.setTime(mydate);////为Calendar对象设置时间为当前日期
		//日期选择器，系统自带日期选择对话框
    	 DatePickerDialog datePicker=new DatePickerDialog(ShopAccountDataActivity.this,AlertDialog.THEME_HOLO_LIGHT, new OnDateSetListener() 
    	 {
             @Override
             public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) 
             {
            	 textView.setText( year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
             }
         },mycalendar.get(Calendar.YEAR),mycalendar.get(Calendar.MONTH), mycalendar.get(Calendar.DAY_OF_MONTH));
         datePicker.show();
    }
    
    @Override
	public void onPause() {
	    super.onPause();
	    //波浪效果
	    mWaveHelper.cancel();
    }

    @Override
	public void onResume() {
        super.onResume();
        mWaveHelper.start();
    }

}
