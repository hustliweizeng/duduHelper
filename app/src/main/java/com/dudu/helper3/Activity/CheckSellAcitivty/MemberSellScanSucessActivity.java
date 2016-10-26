package com.dudu.helper3.Activity.CheckSellAcitivty;


import org.apache.http.Header;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.Activity.WelcomeActivity.LoginActivity;
import com.dudu.duduhelper.R;
import com.dudu.helper3.application.DuduHelperApplication;
import com.dudu.helper3.bean.MemberSellBean;
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

public class MemberSellScanSucessActivity extends BaseActivity 
{
	private TextView ScanSellActissonTextView;
	private TextView ScanSellNameTextView;
	private TextView ScanSellDataTextView;
	private Button enterSellbutton;
	private Button enterScanbutton;
	private LinearLayout scanSellSucesslin;
	private RelativeLayout scanFailRelLin;
	private String orderName;
	private String orderCash;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cash_sell_scan_sucess);
		initHeadView("会员卡核销", true, false,R.drawable.icon_historical);
		orderName=getIntent().getStringExtra("orderName");
		orderCash=getIntent().getStringExtra("orderCash");
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
				Intent intent = new Intent(MemberSellScanSucessActivity.this, MipcaActivityCapture.class);
				intent.putExtra("action", "member");
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
		ColorDialog.showRoundProcessDialog(MemberSellScanSucessActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("no", getIntent().getStringExtra("result"));
		params.add("title",orderName);
		params.add("money",orderCash);
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(MemberSellScanSucessActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.post(ConstantParamPhone.IP+ConstantParamPhone.GET_MEMBER_CARD, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(MemberSellScanSucessActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				ResponsBean cashSellBean=new Gson().fromJson(arg2, ResponsBean.class);
				if(cashSellBean.getStatus().equals("-1006"))
				{
					//Toast.makeText(ProductListActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
					//Toast.makeText(getActivity(), "出错啦！", Toast.LENGTH_SHORT).show();
					MyDialog.showDialog(MemberSellScanSucessActivity.this, "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(MemberSellScanSucessActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				if(cashSellBean.getStatus().equals("-4000"))
				{
					Toast.makeText(MemberSellScanSucessActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					return;
				}
				if(cashSellBean.getStatus().equals("-4001"))
				{
					Toast.makeText(MemberSellScanSucessActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					return;
				}
				if(cashSellBean.getStatus().equals("-4002"))
				{
					Toast.makeText(MemberSellScanSucessActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					
					return;
				}
				if(cashSellBean.getStatus().equals("-4003"))
				{
					Toast.makeText(MemberSellScanSucessActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					return;
				}
				if(cashSellBean.getStatus().equals("-4004"))
				{
					
					Toast.makeText(MemberSellScanSucessActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					return;
					//执行失败触发的事件
				}
				if(cashSellBean.getStatus().equals("1"))
				{
					Toast.makeText(MemberSellScanSucessActivity.this, "验证成功啦", Toast.LENGTH_LONG).show();
					MemberSellScanSucessActivity.this.finish();
				}
				else
				{
					Toast.makeText(MemberSellScanSucessActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
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
	//验证核销信息
	private void initData() 
	{
		// TODO Auto-generated method stub
		ColorDialog.showRoundProcessDialog(MemberSellScanSucessActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("no", getIntent().getStringExtra("result"));
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(MemberSellScanSucessActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.GET_MEMBER_CARD, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(MemberSellScanSucessActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				MemberSellBean memberSellBean=new Gson().fromJson(arg2, MemberSellBean.class);
				if(memberSellBean.getStatus().equals("-4000"))
				{
					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					ScanSellActissonTextView.setText(memberSellBean.getInfo());
					scanFailRelLin.setVisibility(View.VISIBLE);
					return;
				}
				if(memberSellBean.getStatus().equals("-4101"))
				{
					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					ScanSellActissonTextView.setText(memberSellBean.getInfo());
					scanFailRelLin.setVisibility(View.VISIBLE);
					return;
				}
				if(memberSellBean.getStatus().equals("-4102"))
				{
					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					
					ScanSellActissonTextView.setText(memberSellBean.getInfo());
					scanFailRelLin.setVisibility(View.VISIBLE);
					return;
				}
				if(memberSellBean.getStatus().equals("-4103"))
				{
					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					ScanSellActissonTextView.setText(memberSellBean.getInfo());
					scanFailRelLin.setVisibility(View.VISIBLE);
					return;
				}
//				if(cashSellBean.getStatus().equals("-4004"))
//				{
//					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
//					//执行失败触发的事件
//					ScanSellActissonTextView.setText(cashSellBean.getInfo());
//					scanFailRelLin.setVisibility(View.VISIBLE);
//					return;
//				}
				//网络访问方法成功后执行的事件，initData；
				scanSellSucesslin.setVisibility(View.VISIBLE);
				ScanSellActissonTextView.setText("成功识别该卡券");
				ScanSellNameTextView.setText(memberSellBean.getData().getSub_title());
				ScanSellDataTextView.setText("有效期："+Util.DataConVert(memberSellBean.getData().getBegin_date())+"-"+Util.DataConVert(memberSellBean.getData().getEnd_date()));
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
