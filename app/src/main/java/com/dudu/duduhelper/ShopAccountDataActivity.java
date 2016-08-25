package com.dudu.duduhelper;

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

import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.bean.AccountBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.dudu.duduhelper.widget.WaveHelper;
import com.dudu.duduhelper.widget.WaveView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

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
	private TextView allIncomeTextView;
	private TextView fangkeNumText;
	private TextView buyerNumText;
	private TextView orderNumText;
	private TextView allFeeTextView;
	//波浪助手
	private WaveHelper mWaveHelper;
	private WaveView waveView;
	
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
		initData("",data);	
	}
	//加载数据
	private void initData(String stardata,String finishdata) 
	{
		// TODO Auto-generated method stub
		ColorDialog.showRoundProcessDialog(ShopAccountDataActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token",share.getString("token", ""));
		params.add("start", stardata);
		params.add("end", finishdata);
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(ShopAccountDataActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.ACCOUNT_DATA, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(ShopAccountDataActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				AccountBean accountBean=new Gson().fromJson(arg2,AccountBean.class);
				if(accountBean.getStatus().equals("-1006"))
				{
					MyDialog.showDialog(ShopAccountDataActivity.this, "该账号已在其他手机登录，是否重新登录", false, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(ShopAccountDataActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
					return;
				}
				if(!accountBean.getStatus().equals("1"))
				{
					Toast.makeText(ShopAccountDataActivity.this, "出错啦", Toast.LENGTH_LONG).show();
				}
				else
				{
					if(!TextUtils.isEmpty(accountBean.getData().getTrade()))
					{
						allFeeTextView.setText("¥"+accountBean.getData().getTrade());
					}
					
					if(!TextUtils.isEmpty("¥"+accountBean.getData().getIncome()))
					{
						allIncomeTextView.setText(accountBean.getData().getIncome());
					}
					if(!TextUtils.isEmpty(accountBean.getData().getVisiter()))
					{
						fangkeNumText.setText(accountBean.getData().getVisiter());
					}
					if(!TextUtils.isEmpty(accountBean.getData().getBuyer()))
					{
						buyerNumText.setText(accountBean.getData().getBuyer());
					}
					if(!TextUtils.isEmpty(accountBean.getData().getOrder()))
					{
						orderNumText.setText(accountBean.getData().getOrder());
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
	private void initView() 
	{
		waveView = (WaveView) findViewById(R.id.wave);
		//waveView.setBorder(10, Color.parseColor("#44FFFFFF"));
		mWaveHelper = new WaveHelper(waveView,5000,0.2f);
		waveView.setShapeType(WaveView.ShapeType.SQUARE);
		waveView.setWaveColor(Color.parseColor("#03ffffff"),Color.parseColor("#07ffffff"));
		 
		
		allFeeTextView=(TextView) this.findViewById(R.id.allFeeTextView);
		orderNumText=(TextView) this.findViewById(R.id.orderNumText);
		fangkeNumText=(TextView) this.findViewById(R.id.fangkeNumText);
		buyerNumText=(TextView) this.findViewById(R.id.buyerNumText);
		allIncomeTextView=(TextView) this.findViewById(R.id.allIncomeTextView);
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
				// TODO Auto-generated method stub
//				if(TextUtils.isEmpty(startTimeTextView.getText().toString().trim()))
//				{
//					Toast.makeText(ShopAccountDataActivity.this, "请选择统计起始日期", Toast.LENGTH_SHORT).show();
//					return;
//				}
//				if(TextUtils.isEmpty(finishTimeTextView.getText().toString().trim()))
//				{
//					Toast.makeText(ShopAccountDataActivity.this, "请选择统计结束日期", Toast.LENGTH_SHORT).show();
//					return;
//				}
//				if(!Util.compareDate(startTimeTextView.getText().toString().trim(), finishTimeTextView.getText().toString().trim()))
//				{
//					Toast.makeText(ShopAccountDataActivity.this, "起始日期不能大于结束日期", Toast.LENGTH_SHORT).show();
//					return;
//				}
//				fangkeNumText.setTextColor(fangkeNumText.getResources().getColor(R.color.text_color));
//				buyerNumText.setTextColor(fangkeNumText.getResources().getColor(R.color.text_color));
//				orderNumText.setTextColor(fangkeNumText.getResources().getColor(R.color.text_color));
//				initData(startTimeTextView.getText().toString().trim(),finishTimeTextView.getText().toString().trim());
				Intent intent = new Intent(ShopAccountDataActivity.this,ShopAccountWatchActivity.class);
				startActivity(intent);
			}
		});
		startTimeRel.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				showDataDialog(startTimeTextView);
			}
		});
		finishTimeRel.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				showDataDialog(finishTimeTextView);
			}
		});
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
                 // TODO Auto-generated method stub
                 //Toast.makeText(RegisterActivity.this, year+"year "+(monthOfYear+1)+"month "+dayOfMonth+"day", Toast.LENGTH_SHORT).show();
            	 textView.setText( year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
             }
         },mycalendar.get(Calendar.YEAR),mycalendar.get(Calendar.MONTH), mycalendar.get(Calendar.DAY_OF_MONTH));
         datePicker.show();
    }
    
    @Override
	public void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }

    @Override
	public void onResume() {
        super.onResume();
        mWaveHelper.start();
    }

}
