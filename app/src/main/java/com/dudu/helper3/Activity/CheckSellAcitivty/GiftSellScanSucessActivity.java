package com.dudu.helper3.Activity.CheckSellAcitivty;


import org.apache.http.Header;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.Activity.WelcomeActivity.LoginActivity;
import com.dudu.duduhelper.R;
import com.dudu.helper3.application.DuduHelperApplication;
import com.dudu.helper3.bean.CashSellBean;
import com.dudu.helper3.bean.ResponsBean;
import com.dudu.helper3.Utils.Util;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.widget.ColorDialog;
import com.dudu.helper3.widget.MyDialog;
import com.example.qr_codescan.MipcaActivityCapture;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GiftSellScanSucessActivity extends BaseActivity 
{
	private TextView ScanSellActissonTextView;
	private TextView ScanSellNameTextView;
	private TextView ScanSellDataTextView;
	private Button enterSellbutton;
	private Button enterScanbutton;
	private LinearLayout scanSellSucesslin;
	private RelativeLayout scanFailRelLin;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cash_sell_scan_sucess);
		initHeadView("礼品核销", true, false,R.drawable.icon_historical);
		DuduHelperApplication.getInstance().addActivity(this);
		initView();
		initData();
	}
	private void initView() 
	{
		// TODO Auto-generated method stub
		scanFailRelLin=(RelativeLayout) this.findViewById(R.id.scanFailRelLin);
		scanSellSucesslin=(LinearLayout) this.findViewById(R.id.scanSellSucesslin);
		ScanSellActissonTextView=(TextView) this.findViewById(R.id.ScanSellActissonTextView);
		ScanSellNameTextView=(TextView) this.findViewById(R.id.ScanSellNameTextView);
		ScanSellDataTextView=(TextView) this.findViewById(R.id.ScanSellDataTextView);
		
		enterSellbutton=(Button) this.findViewById(R.id.enterSellbutton);
		
		enterScanbutton=(Button) this.findViewById(R.id.enterScanbutton);
		enterScanbutton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(GiftSellScanSucessActivity.this, MipcaActivityCapture.class);
				intent.putExtra("action", "gift");
				startActivity(intent);
				finish();
			}
		});
		enterSellbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				confirmCashSell();
			}
		});
	}
	//确认核销该优惠券
	private void confirmCashSell() 
	{
		// TODO Auto-generated method stub
		ColorDialog.showRoundProcessDialog(GiftSellScanSucessActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("no", getIntent().getStringExtra("result"));
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(GiftSellScanSucessActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.post(ConstantParamPhone.IP+ConstantParamPhone.GET_GIFT_CARD, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(GiftSellScanSucessActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				//Toast.makeText(GiftSellScanSucessActivity.this, arg2, Toast.LENGTH_LONG).show();
				ResponsBean cashSellBean=new Gson().fromJson(arg2, ResponsBean.class);
				if(cashSellBean.getStatus().equals("-1006"))
				{
					//Toast.makeText(ProductListActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
					//Toast.makeText(getActivity(), "出错啦！", Toast.LENGTH_SHORT).show();
					MyDialog.showDialog(GiftSellScanSucessActivity.this, "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(GiftSellScanSucessActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
//				if(cashSellBean.getStatus().equals("-4000"))
//				{
//					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
//					//执行失败触发的事件
//					return;
//				}
//				if(cashSellBean.getStatus().equals("-4201"))
//				{
//					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
//					//执行失败触发的事件
//					return;
//				}
//				if(cashSellBean.getStatus().equals("-4202"))
//				{
//					Toast.makeText(GiftSellScanSucessActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
//					//执行失败触发的事件
//					
//					return;
//				}
//				if(cashSellBean.getStatus().equals("-4203"))
//				{
//					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
//					//执行失败触发的事件
//					return;
//				}
//				if(cashSellBean.getStatus().equals("-4004"))
//				{
//					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
//					//执行失败触发的事件
//				}
//				if(cashSellBean.getStatus().equals("-4003"))
//				{
//					Toast.makeText(GiftSellScanSucessActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
//					//执行失败触发的事件
//					return;
//				}
				//网络访问方法成功后执行的事件，initData；
				if(cashSellBean.getStatus().equals("1"))
				{
					Toast.makeText(GiftSellScanSucessActivity.this, "礼品卡核销成功啦！", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(GiftSellScanSucessActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					return;
				}
				//网络访问方法成功后执行的事件，initData；
				
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				ColorDialog.dissmissProcessDialog();
			}
		});
	}
	//验证核销信息
	private void initData() 
	{
		// TODO Auto-generated method stub
		ColorDialog.showRoundProcessDialog(GiftSellScanSucessActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("no", getIntent().getStringExtra("result"));
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(GiftSellScanSucessActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.GET_GIFT_CARD, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(GiftSellScanSucessActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				CashSellBean cashSellBean=new Gson().fromJson(arg2, CashSellBean.class);
				if(cashSellBean.getStatus().equals("-1006"))
				{
					//Toast.makeText(ProductListActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
					//Toast.makeText(getActivity(), "出错啦！", Toast.LENGTH_SHORT).show();
					MyDialog.showDialog(GiftSellScanSucessActivity.this, "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(GiftSellScanSucessActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				if(cashSellBean.getStatus().equals("-4000"))
				{
					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					ScanSellActissonTextView.setText(cashSellBean.getInfo());
					scanFailRelLin.setVisibility(View.VISIBLE);
					return;
				}
				if(cashSellBean.getStatus().equals("-4201"))
				{
					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					ScanSellActissonTextView.setText(cashSellBean.getInfo());
					scanFailRelLin.setVisibility(View.VISIBLE);
					return;
				}
				if(cashSellBean.getStatus().equals("-4202"))
				{
					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					
					ScanSellActissonTextView.setText(cashSellBean.getInfo());
					scanFailRelLin.setVisibility(View.VISIBLE);
					return;
				}
				if(cashSellBean.getStatus().equals("-4203"))
				{
					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					ScanSellActissonTextView.setText(cashSellBean.getInfo());
					scanFailRelLin.setVisibility(View.VISIBLE);
					return;
				}
				if(cashSellBean.getStatus().equals("-4004"))
				{
					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					ScanSellActissonTextView.setText(cashSellBean.getInfo());
					scanFailRelLin.setVisibility(View.VISIBLE);
					return;
				}
				//网络访问方法成功后执行的事件，initData；
				if(cashSellBean.getStatus().equals("1"))
				{
					scanSellSucesslin.setVisibility(View.VISIBLE);
					ScanSellActissonTextView.setText("成功识别该卡券");
					ScanSellNameTextView.setText(cashSellBean.getData().getSubject());
					ScanSellDataTextView.setText("有效期至："+ Util.DataConVert(cashSellBean.getData().getExptime()));
				}
				else
				{
					ScanSellActissonTextView.setText(cashSellBean.getInfo());
					scanFailRelLin.setVisibility(View.VISIBLE);
					return;
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
}
