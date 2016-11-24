package com.dudu.duduhelper.Activity.CheckSellAcitivty;

import org.apache.http.Header;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Activity.WelcomeActivity.LoginActivity;
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
import com.dudu.duduhelper.R;
public class GiftSellActivity extends BaseActivity 
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
		setContentView(R.layout.activity_cashgift_sell);
		initHeadView("礼品核销", true, false,R.drawable.icon_historical);
		DuduHelperApplication.getInstance().addActivity(this);
		initView();
		
	}

	private void initView() {
		// TODO Auto-generated method stub
		cashSellName=(TextView) this.findViewById(R.id.giftcashSellName);
		cashSellDataTime=(TextView) this.findViewById(R.id.giftcashSellDataTime);
		step1lin=(LinearLayout) this.findViewById(R.id.giftstep1lin);
		step2lin=(LinearLayout) this.findViewById(R.id.giftstep2lin);
		step3lin=(LinearLayout) this.findViewById(R.id.giftstep3lin);
		scanbutton=(Button) this.findViewById(R.id.giftscanbutton);
		stepSucessLin=(LinearLayout) this.findViewById(R.id.giftstepSucessLin);
		enterSellbutton=(Button) this.findViewById(R.id.giftenterSellbutton);
		passwordEditText=(EditText) this.findViewById(R.id.giftpasswordEditText);
		confirmSellPasswordBtn=(Button) this.findViewById(R.id.giftconfirmSellPasswordBtn);
		showMessageTextView=(TextView) this.findViewById(R.id.giftshowMessageTextView);
		enterSellbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(passwordEditText.getText().toString().trim()))
				{
					Toast.makeText(GiftSellActivity.this, "请输入核销密码", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(GiftSellActivity.this, "请输入核销密码", Toast.LENGTH_SHORT).show();
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
				Intent intent = new Intent(GiftSellActivity.this, MipcaActivityCapture.class);
				intent.putExtra("action", "gift");
				startActivity(intent);
			}
		});
	}
	
	//确认核销该优惠券
	private void confirmCashSell() 
	{
		// TODO Auto-generated method stub
		ColorDialog.showRoundProcessDialog(GiftSellActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("no", passwordEditText.getText().toString().trim());
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(GiftSellActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.post(ConstantParamPhone.IP+ConstantParamPhone.GET_GIFT_CARD, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(GiftSellActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				ResponsBean cashSellBean=new Gson().fromJson(arg2, ResponsBean.class);
				if(cashSellBean.getStatus().equals("-1006"))
				{
					//Toast.makeText(ProductListActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
					//Toast.makeText(getActivity(), "出错啦！", Toast.LENGTH_SHORT).show();
					MyDialog.showDialog(GiftSellActivity.this, "该账号已在其他手机登录，是否重新登录", false, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(GiftSellActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
					return;
				}
				if(cashSellBean.getStatus().equals("-4000"))
				{
					Toast.makeText(GiftSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					return;
				}
				if(cashSellBean.getStatus().equals("-4003"))
				{
					Toast.makeText(GiftSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					return;
				}
				if(cashSellBean.getStatus().equals("-4201"))
				{
					Toast.makeText(GiftSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					return;
				}
//				if(cashSellBean.getStatus().equals("-4202"))
//				{
//					Toast.makeText(GiftSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
//					//执行失败触发的事件
//					
//					return;
//				}
				if(cashSellBean.getStatus().equals("-4203"))
				{
					Toast.makeText(GiftSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					//执行失败触发的事件
					return;
				}
				//网络访问方法成功后执行的事件，initData；
				if(cashSellBean.getStatus().equals("1"))
				{
					Toast.makeText(GiftSellActivity.this, "礼品卡核销成功啦！", Toast.LENGTH_LONG).show();
				}
				else
				{
					Toast.makeText(GiftSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
					return;
				}
				//网络访问方法成功后执行的事件，initData；
				
			}
			@Override
			public void onFinish() 
			{
				ColorDialog.dissmissProcessDialog();
			}
		});
	}
	
	//验证核销信息
	private void initData() 
	{
		// TODO Auto-generated method stub
		ColorDialog.showRoundProcessDialog(GiftSellActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("no", passwordEditText.getText().toString().trim());
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(GiftSellActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.GET_GIFT_CARD, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(GiftSellActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				CashSellBean cashSellBean=new Gson().fromJson(arg2, CashSellBean.class);
				if(cashSellBean.getStatus().equals("-1006"))
				{
					//Toast.makeText(ProductListActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
					//Toast.makeText(getActivity(), "出错啦！", Toast.LENGTH_SHORT).show();
					MyDialog.showDialog(GiftSellActivity.this, "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() {
						
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
							Intent intent=new Intent(GiftSellActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
					return;
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
				if(cashSellBean.getStatus().equals("-4201"))
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
				if(cashSellBean.getStatus().equals("-4202"))
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
				if(cashSellBean.getStatus().equals("-4203"))
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
//				if(cashSellBean.getStatus().equals("-4003"))
//				{
//					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
//					//执行失败触发的事件
//					showMessageTextView.setVisibility(View.VISIBLE);
//					scanbutton.setVisibility(View.VISIBLE);
//					showMessageTextView.setText(cashSellBean.getInfo());
//					step1lin.setVisibility(View.GONE);
//					step2lin.setVisibility(View.GONE);
//					step3lin.setVisibility(View.GONE);
//					stepSucessLin.setVisibility(View.GONE);
//					enterSellbutton.setVisibility(View.GONE);
//					return;
//				}
//				if(cashSellBean.getStatus().equals("-4004"))
//				{
//					//Toast.makeText(CashSellActivity.this, cashSellBean.getInfo(), Toast.LENGTH_LONG).show();
//					//执行失败触发的事件
//					showMessageTextView.setVisibility(View.VISIBLE);
//					scanbutton.setVisibility(View.VISIBLE);
//					showMessageTextView.setText(cashSellBean.getInfo());
//					step1lin.setVisibility(View.GONE);
//					step2lin.setVisibility(View.GONE);
//					step3lin.setVisibility(View.GONE);
//					stepSucessLin.setVisibility(View.GONE);
//					enterSellbutton.setVisibility(View.GONE);
//					return;
//				}
				//网络访问方法成功后执行的事件，initData；
				step1lin.setVisibility(View.GONE);
				step2lin.setVisibility(View.GONE);
				step3lin.setVisibility(View.GONE);
				scanbutton.setVisibility(View.GONE);
				showMessageTextView.setVisibility(View.GONE);
				stepSucessLin.setVisibility(View.VISIBLE);
				enterSellbutton.setVisibility(View.VISIBLE);
				cashSellName.setText(cashSellBean.getData().getSubject());
				//+Util.DataConVert(cashSellBean.getData().getTime())+"—"
				cashSellDataTime.setText("有效期至："+ Util.DataConVert(cashSellBean.getData().getExptime()));
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				ColorDialog.dissmissProcessDialog();
			}
		});
	}
	
//	@Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode) {
//		case SCANNIN_GREQUEST_CODE:
//			if(resultCode == RESULT_OK){
//				Bundle bundle = data.getExtras();
//				Toast.makeText(CashSellActivity.this, bundle.getString("result"), Toast.LENGTH_LONG).show();
//			}
//			break;
//		}
//    }	
}
