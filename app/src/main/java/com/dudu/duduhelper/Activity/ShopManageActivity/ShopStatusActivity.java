package com.dudu.duduhelper.Activity.ShopManageActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.Activity.SummaryActivity.ShopAccountDataActivity;
import com.dudu.duduhelper.Activity.SummaryActivity.ShopAccountWatchActivity;
import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.javabean.ShopListBean;
import com.dudu.duduhelper.widget.WaveHelper;
import com.dudu.duduhelper.widget.WaveView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author
 * @version 1.0
 * @date 2016/11/1
 */

public class ShopStatusActivity extends BaseActivity implements View.OnClickListener {
	private ViewPager vp_content;
	private TextView fangkeNumText;
	private TextView buyerNumText;
	private TextView orderNumText;
	private TextView startTimeTextView;
	private TextView finishTimeTextView;
	private Button accountDatabutton;
	private LinearLayout startTimeRel;
	private LinearLayout finishTimeRel;

	private WaveHelper mWaveHelper;
	private WaveView waveView;//波浪显示
	private List<View> viewList;
	private WaveView waveView2;
	private WaveHelper mWaveHelper2;
	private ImageView dot1;
	private ImageView dot2;
	private TextView month_trade;
	private TextView month_income;
	private TextView total_trade;
	private TextView total_income;
	private ShopListBean.DataBean detail;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_shop_status);
		initView();
		initHeadView("数据统计",true,true,R.drawable.icon_settings);
		initData();
	}

	private void initData() {
		detail = (ShopListBean.DataBean) getIntent().getSerializableExtra("detail");
		if (detail == null){
			return;
		}
		fangkeNumText.setText(detail.getUv_history());
		buyerNumText.setText(detail.getUser_num());
		orderNumText.setText(detail.getOrder_num());
		month_trade.setText(detail.getTrade_month());
		month_income.setText(detail.getIncome_month());
		total_trade.setText(detail.getTrade_history());
		total_income.setText(detail.getIncome_sum());
	}

	@Override
	public void RightButtonClick() {//到编辑页面
		super.RightButtonClick();
		Intent intent = new Intent(this,ShopAddActivity.class);
		intent.putExtra("source","detail");//进入详情页面
		intent.putExtra("shopId",detail.getId());//传递店铺id过去
		startActivity(intent);
	}

	private void initView() {
		
		dot1 = (ImageView) findViewById(R.id.dot1);
		dot2 = (ImageView) findViewById(R.id.dot2);
		vp_content = (ViewPager) findViewById(R.id.vp_content);
		fangkeNumText = (TextView) findViewById(R.id.fangkeNumText);
		buyerNumText = (TextView) findViewById(R.id.buyerNumText);
		orderNumText = (TextView) findViewById(R.id.orderNumText);
		startTimeTextView = (TextView) findViewById(R.id.startTimeTextView);
		finishTimeTextView = (TextView) findViewById(R.id.finishTimeTextView);
		accountDatabutton = (Button) findViewById(R.id.accountDatabutton);
		accountDatabutton.setOnClickListener(this);
		startTimeRel = (LinearLayout) findViewById(R.id.startTimeRel);
		startTimeRel.setOnClickListener(this);
		finishTimeRel = (LinearLayout) findViewById(R.id.finishTimeRel);
		finishTimeRel.setOnClickListener(this);
		
		viewList = new ArrayList<View>();
		//加载view  
		View view1= LayoutInflater.from(this).inflate(R.layout.item_vp1, null);
		View view2=LayoutInflater.from(this).inflate(R.layout.item_vp2, null);
		
		//统计数据
		month_trade = (TextView) view1.findViewById(R.id.month_trade);
		month_income = (TextView) view1.findViewById(R.id.month_income);

		total_trade = (TextView) view2.findViewById(R.id.total_trade);
		total_income = (TextView) view2.findViewById(R.id.total_income);
		
		waveView = (WaveView) view1.findViewById(R.id.wave1);
		mWaveHelper = new WaveHelper(waveView,5000,0.2f);
		waveView.setShapeType(WaveView.ShapeType.SQUARE);
		waveView.setWaveColor(Color.parseColor("#03ffffff"),Color.parseColor("#07ffffff"));

	    waveView2 = (WaveView) view2.findViewById(R.id.wave2);
		mWaveHelper2 = new WaveHelper(waveView2,5000,0.2f);
		waveView2.setShapeType(WaveView.ShapeType.SQUARE);
		waveView2.setWaveColor(Color.parseColor("#03ffffff"),Color.parseColor("#07ffffff"));
		
		
		viewList.add(view1);
		viewList.add(view2);
		vp_content.setAdapter(new PagerAdapter() {
			@Override
			public int getCount() {
				return viewList.size();
			}

			@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				return arg0 == arg1;//官方提示这样写  
			}

			@Override
			public void destroyItem(ViewGroup container, int position, Object object) {
				container.removeView(viewList.get(position));//删除页卡  
			}

			@Override
			public Object instantiateItem(ViewGroup container, int position) {
				container.addView(viewList.get(position));//添加页卡  
				return viewList.get(position);
			}
		});
		//viewpager和原点联动
		vp_content.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				
			}

			@Override
			public void onPageSelected(int position) {
				if (position == 0){
					dot1.setImageResource(R.drawable.yuandian_sel);
					dot2.setImageResource(R.drawable.yuandian);
				}else {
					dot1.setImageResource(R.drawable.yuandian);
					dot2.setImageResource(R.drawable.yuandian_sel);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.accountDatabutton://提交
				Intent intent = new Intent(this,ShopAccountWatchActivity.class);//进入到统计页面
				String start = startTimeTextView.getText().toString().trim();
				String end = finishTimeTextView.getText().toString().trim();

				if (TextUtils.isEmpty(start) ||TextUtils.isEmpty(end)){
					Toast.makeText(context,"请选择开始和结束日期",Toast.LENGTH_SHORT).show();
					return;
				}
				intent.putExtra("start",start);
				intent.putExtra("end",end);
				startActivity(intent);
				break;
			case R.id.startTimeRel://开始时间
				showDataDialog(startTimeTextView);
				break;
			case R.id.finishTimeRel://结束时间
				showDataDialog(finishTimeTextView);
				break;
		}
	}
	//选择日期,调用系统主题
	@SuppressLint("InlinedApi")
	private void showDataDialog(final TextView textView)
	{
		Calendar mycalendar=Calendar.getInstance(Locale.CHINA);
		Date mydate=new Date(); //获取当前日期Date对象
		mycalendar.setTime(mydate);////为Calendar对象设置时间为当前日期
		//日期选择器，系统自带日期选择对话框
		DatePickerDialog datePicker=new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, new DatePickerDialog.OnDateSetListener()
		{
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
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
		mWaveHelper2.cancel();
	}
	@Override
	public void onResume() {
		super.onResume();
		mWaveHelper.start();
		mWaveHelper2.start();
	}
}
