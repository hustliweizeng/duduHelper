package com.dudu.duduhelper.Activity.CheckSellAcitivty;

import org.apache.http.Header;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Activity.WelcomeActivity.LoginActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.bean.CashSellBean;
import com.dudu.duduhelper.bean.ResponsBean;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.example.qr_codescan.MipcaActivityCapture;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class CashSellActivity extends BaseActivity 
{
	private Button scanbutton;
	private Button confirmSellPasswordBtn;
	private EditText passwordEditText;
	private LinearLayout step1lin;
	private LinearLayout step2lin;
	private LinearLayout step3lin;
	private LinearLayout stepSucessLin;
	private Button enterSellbutton;
	private TextView showMessageTextView;
	private final static int SCANNIN_GREQUEST_CODE = 1;
	private TextView cashSellName;
	private TextView cashSellDataTime;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cashyouhui_sell);
		initHeadView("优惠券核销", true, false,R.drawable.icon_historical);
		initView();
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		cashSellName=(TextView) this.findViewById(R.id.cashSellName);
		cashSellDataTime=(TextView) this.findViewById(R.id.cashSellDataTime);
		step1lin=(LinearLayout) this.findViewById(R.id.step1lin);
		step2lin=(LinearLayout) this.findViewById(R.id.step2lin);
		step3lin=(LinearLayout) this.findViewById(R.id.step3lin);
		scanbutton=(Button) this.findViewById(R.id.scanbutton);
		stepSucessLin=(LinearLayout) this.findViewById(R.id.stepSucessLin);
		enterSellbutton=(Button) this.findViewById(R.id.enterSellbutton);
		passwordEditText=(EditText) this.findViewById(R.id.passwordEditText);
		confirmSellPasswordBtn=(Button) this.findViewById(R.id.confirmSellPasswordBtn);
		showMessageTextView=(TextView) this.findViewById(R.id.showMessageTextView);
		enterSellbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(passwordEditText.getText().toString().trim()))
				{
					Toast.makeText(CashSellActivity.this, "请输入核销密码", Toast.LENGTH_SHORT).show();
					return;
				}
				confirmCashSell();
			}
		});
		confirmSellPasswordBtn.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(passwordEditText.getText().toString().trim()))
				{
					Toast.makeText(CashSellActivity.this, "请输入核销密码", Toast.LENGTH_SHORT).show();
					return;
				}
				initData();
				

			}

		});
		//开启二维码扫描
		scanbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(CashSellActivity.this, MipcaActivityCapture.class);
				intent.putExtra("action", "cash");
				startActivity(intent);
			}
		});
	}
	
	//确认核销该优惠券
	private void confirmCashSell() 
	{
		// TODO Auto-generated method stub
		ColorDialog.showRoundProcessDialog(CashSellActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("no", passwordEditText.getText().toString().trim());
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(CashSellActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.post(ConstantParamPhone.IP+ConstantParamPhone.GET_SELL_CARD, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(CashSellActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				ResponsBean cashSellBean=new Gson().fromJson(arg2, ResponsBean.class);
				if(cashSellBean.getStatus().equals("-1006"))
				{
					//Toast.makeText(ProductListActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
					//Toast.makeText(getActivity(), "出错啦！", Toast.LENGTH_SHORT).show();
					MyDialog.showDialog(CashSellActivity.this, "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
							return;
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(CashSellActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				if(cashSellBean.getStatus().equals("-4000"))
				{
					Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					return;
				}
				if(cashSellBean.getStatus().equals("-4001"))
				{
					Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					return;
				}
				if(cashSellBean.getStatus().equals("-4002"))
				{
					Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					
					return;
				}
				if(cashSellBean.getStatus().equals("-4003"))
				{
					Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					return;
				}
				if(cashSellBean.getStatus().equals("-4004"))
				{
					Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					return;
					//执行失败触发的事件
				}
				if(cashSellBean.getStatus().equals("-4300"))
				{
					Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					return;
					//执行失败触发的事件
				}
				//网络访问方法成功后执行的事件，initData；
				if(cashSellBean.getStatus().equals("1"))
				{
					Toast.makeText(CashSellActivity.this, "验证成功啦！", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
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
		ColorDialog.showRoundProcessDialog(CashSellActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("no", passwordEditText.getText().toString().trim());
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(CashSellActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.GET_SELL_CARD, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(CashSellActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				CashSellBean cashSellBean=new Gson().fromJson(arg2, CashSellBean.class);
				if(cashSellBean.getStatus().equals("-1006"))
				{
					//Toast.makeText(ProductListActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
					//Toast.makeText(getActivity(), "出错啦！", Toast.LENGTH_SHORT).show();
					MyDialog.showDialog(CashSellActivity.this, "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(CashSellActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				if(cashSellBean.getStatus().equals("-4000"))
				{
					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					showMessageTextView.setVisibility(View.VISIBLE);
					scanbutton.setVisibility(View.VISIBLE);
					showMessageTextView.setText(cashSellBean.getInfo());
					step1lin.setVisibility(View.GONE);
					step2lin.setVisibility(View.GONE);
					step3lin.setVisibility(View.GONE);
					stepSucessLin.setVisibility(View.GONE);
					enterSellbutton.setVisibility(View.GONE);
					return;
				}
				if(cashSellBean.getStatus().equals("-4001"))
				{
					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					showMessageTextView.setVisibility(View.VISIBLE);
					scanbutton.setVisibility(View.VISIBLE);
					showMessageTextView.setText(cashSellBean.getInfo());
					step1lin.setVisibility(View.GONE);
					step2lin.setVisibility(View.GONE);
					step3lin.setVisibility(View.GONE);
					stepSucessLin.setVisibility(View.GONE);
					enterSellbutton.setVisibility(View.GONE);
					return;
				}
				if(cashSellBean.getStatus().equals("-4002"))
				{
					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					
					showMessageTextView.setVisibility(View.VISIBLE);
					scanbutton.setVisibility(View.VISIBLE);
					showMessageTextView.setText(cashSellBean.getInfo());
					step1lin.setVisibility(View.GONE);
					step2lin.setVisibility(View.GONE);
					step3lin.setVisibility(View.GONE);
					stepSucessLin.setVisibility(View.GONE);
					enterSellbutton.setVisibility(View.GONE);
					return;
				}
				if(cashSellBean.getStatus().equals("-4003"))
				{
					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					showMessageTextView.setVisibility(View.VISIBLE);
					scanbutton.setVisibility(View.VISIBLE);
					showMessageTextView.setText(cashSellBean.getInfo());
					step1lin.setVisibility(View.GONE);
					step2lin.setVisibility(View.GONE);
					step3lin.setVisibility(View.GONE);
					stepSucessLin.setVisibility(View.GONE);
					enterSellbutton.setVisibility(View.GONE);
					return;
				}
				if(cashSellBean.getStatus().equals("-4004"))
				{
					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					showMessageTextView.setVisibility(View.VISIBLE);
					scanbutton.setVisibility(View.VISIBLE);
					showMessageTextView.setText(cashSellBean.getInfo());
					step1lin.setVisibility(View.GONE);
					step2lin.setVisibility(View.GONE);
					step3lin.setVisibility(View.GONE);
					stepSucessLin.setVisibility(View.GONE);
					enterSellbutton.setVisibility(View.GONE);
					return;
				}
				if(cashSellBean.getStatus().equals("1"))
				{
					//网络访问方法成功后执行的事件，initData；
					step1lin.setVisibility(View.GONE);
					step2lin.setVisibility(View.GONE);
					step3lin.setVisibility(View.GONE);
					scanbutton.setVisibility(View.GONE);
					showMessageTextView.setVisibility(View.GONE);
					stepSucessLin.setVisibility(View.VISIBLE);
					enterSellbutton.setVisibility(View.VISIBLE);
					cashSellName.setText(cashSellBean.getData().getSubject());
					cashSellDataTime.setText("有效期至："+Util.DataConVert(cashSellBean.getData().getExptime()));//"+Util.DataConVert(cashSellBean.getData().getTime())+"-
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
