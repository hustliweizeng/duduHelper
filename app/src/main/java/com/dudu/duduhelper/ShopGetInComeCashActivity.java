package com.dudu.duduhelper;


import org.apache.http.Header;

import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.bean.GetCashBean;
import com.dudu.duduhelper.bean.GetInComeCashBean;
import com.dudu.duduhelper.common.Util;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.dudu.duduhelper.widget.MyKeyBoard;
import com.dudu.duduhelper.widget.MyKeyBoard.OnKeyBoardClickListener;
import com.dudu.duduhelper.wxapi.WXEntryActivity;
import com.example.qr_codescan.MipcaActivityCapture;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShopGetInComeCashActivity extends BaseActivity 
{

	private TextView getcashmoneyedit;
	private ImageView getcashmoneyDelectIconBtn;
	private TextView getcashmoneyicon;
	private TextView getCashText;
	private String action="";
	private Button submitbtn;
	private LinearLayout scanHexiaoButton;
	private RelativeLayout hexiaoRelLayout;
	private LinearLayout wuzhelin;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_get_in_come_cash);
		initHeadView("收款", true, true, R.drawable.icon_bangzhutouming);
		DuduHelperApplication.getInstance().addActivity(this);
		if(getIntent().getStringExtra("action")!=null)
		{
			action = getIntent().getStringExtra("action");
		}
		initView();
	}
	
	@Override
	public void RightButtonClick() 
	{
		// TODO Auto-generated method stub
		Intent intent = new Intent(this,ShopGetCashhistoryListActivity.class);
		startActivity(intent);
	}

	private void initView() 
	{
		//qrcodeButton=(Button) this.findViewById(R.id.qrcodeButton);
		
		wuzhelin = (LinearLayout) this.findViewById(R.id.wuzhelin);
		
		hexiaoRelLayout = (RelativeLayout) this.findViewById(R.id.hexiaoRelLayout);
		scanHexiaoButton = (LinearLayout) this.findViewById(R.id.scanHexiaoButton);
		submitbtn = (Button) this.findViewById(R.id.submitbtn);
		getcashmoneyicon = (TextView) this.findViewById(R.id.getcashmoneyicon);
		getCashText = (TextView) this.findViewById(R.id.getCashText);
		getcashmoneyDelectIconBtn=(ImageView) this.findViewById(R.id.getcashmoneyDelectIconBtn);
		getcashmoneyedit=(TextView) this.findViewById(R.id.getcashmoneyedit);
		if(action.equals("getcashcode"))
		{
			getCashText.setText("请输入用户的付款码编号");
			getcashmoneyicon.setVisibility(View.GONE);
			submitbtn.setText("提交");
			getcashmoneyedit.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
		}
		if(action.equals("hexiao"))
		{
			getCashText.setText("请输入用户的核销编号");
			getcashmoneyicon.setVisibility(View.GONE);
			scanHexiaoButton.setVisibility(View.VISIBLE);
			hexiaoRelLayout.setBackgroundResource(R.drawable.shop_btn_hexiaolin);
			submitbtn.setText("提交");
			submitbtn.setBackgroundResource(R.drawable.shop_keyboard_hexiao_text_select);
			getcashmoneyedit.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
		}
		if(action.equals("wuzhe"))
		{
			getCashText.setText("请输入用户的五折卡编号");
			getcashmoneyicon.setVisibility(View.GONE);
			wuzhelin.setVisibility(View.VISIBLE);
			hexiaoRelLayout.setBackgroundResource(R.drawable.shop_btn_hexiaolin);
			submitbtn.setText("验证");
			submitbtn.setBackgroundResource(R.drawable.shop_keyboard_hexiao_text_select);
			getcashmoneyedit.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
		}
		scanHexiaoButton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
//				Intent intent = new Intent(ShopGetInComeCashActivity.this,MipcaActivityCapture.class);
//				intent.putExtra("action", "hexiao");
//				startActivity(intent);
				finish();
			}
		});
		getcashmoneyedit.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(ShopGetInComeCashActivity.this.findViewById(R.id.keyboard).getVisibility()==View.GONE)
				{
					ShopGetInComeCashActivity.this.findViewById(R.id.keyboard).setVisibility(View.VISIBLE);
				}
			}
		});
		getcashmoneyDelectIconBtn.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				getcashmoneyedit.setText("");
				getcashmoneyDelectIconBtn.setVisibility(View.INVISIBLE);
			}
		});
		MyKeyBoard myKeyBoard = new MyKeyBoard(this);
		myKeyBoard.setOnKeyBoardClickListener(new OnKeyBoardClickListener() 
		{
			
			@Override
			public void onSubmit() 
			{
				// TODO Auto-generated method stub
				if(action.equals("getcashcode"))
				{
					if(TextUtils.isEmpty(getcashmoneyedit.getText().toString()))
					{
						Toast.makeText(ShopGetInComeCashActivity.this, "请填写用户付款码编号", Toast.LENGTH_SHORT).show();
						return;
					}
					Intent intent = new Intent(ShopGetInComeCashActivity.this,ShopDiscountScanSucessActivity.class);
					intent.putExtra("fee", getIntent().getStringExtra("fee"));
					intent.putExtra("result",getcashmoneyedit.getText());
					intent.putExtra("body", "商家收款-扫码支付");
					startActivity(intent);
					finish();
				}
				else if(action.equals("hexiao"))
				{
					if(TextUtils.isEmpty(getcashmoneyedit.getText().toString()))
					{
						Toast.makeText(ShopGetInComeCashActivity.this, "请输入用户的核销编号", Toast.LENGTH_SHORT).show();
						return;
					}
					Intent intent = new Intent(ShopGetInComeCashActivity.this,ShopScanSellActivity.class);
					intent.putExtra("result",getcashmoneyedit.getText());
					startActivity(intent);
					finish();
				}
				else
				{
				  initData();
				}
			}
			
			@Override
			public void onDelect() 
			{
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(getcashmoneyedit.getText()))
				{
					getcashmoneyedit.setText(getcashmoneyedit.getText().toString().substring(0, getcashmoneyedit.getText().toString().length()-1));
					if(TextUtils.isEmpty(getcashmoneyedit.getText()))
					{
						getcashmoneyDelectIconBtn.setVisibility(View.INVISIBLE);
					}
				}
				else
				{
					getcashmoneyDelectIconBtn.setVisibility(View.INVISIBLE);
				}
			}
			
			@Override
			public void onClick(String content) 
			{
				// TODO Auto-generated method stub
				getcashmoneyDelectIconBtn.setVisibility(View.VISIBLE);
				getcashmoneyedit.setText(getcashmoneyedit.getText()+content);
			}
		});
	}
	private void initData() 
	{
		// TODO Auto-generated method stub
		if(TextUtils.isEmpty(getcashmoneyedit.getText().toString()))
		{
			Toast.makeText(ShopGetInComeCashActivity.this, "请填写收款金额", Toast.LENGTH_SHORT).show();
			return;
		}
		if(Double.parseDouble(getcashmoneyedit.getText().toString())<0.01)
		{
			Toast.makeText(ShopGetInComeCashActivity.this, "收款金额必须大于0.01元", Toast.LENGTH_SHORT).show();
			return;
		}
		if(Double.parseDouble(getcashmoneyedit.getText().toString())>100000)
		{
			Toast.makeText(ShopGetInComeCashActivity.this, "单笔金额不能超过100000", Toast.LENGTH_SHORT).show();
			return;
		}
		
		ColorDialog.showRoundProcessDialog(ShopGetInComeCashActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", this.share.getString("token", ""));
		params.add("fee",getcashmoneyedit.getText().toString());
		params.add("body","暂无描述");
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(ShopGetInComeCashActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.post(ConstantParamPhone.IP+ConstantParamPhone.GET_CASH_LIST, params,new TextHttpResponseHandler()
		{

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(ShopGetInComeCashActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				GetInComeCashBean getCashBean=new Gson().fromJson(arg2,GetInComeCashBean.class);
				if(getCashBean.getStatus().equals("-1006"))
				{
					MyDialog.showDialog(ShopGetInComeCashActivity.this, "该账号已在其他手机登录，是否重新登录", false, true, "取消", "确定",new OnClickListener() 
					{
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					},new OnClickListener() {
						
						@Override
						public void onClick(View v) 
						{
							// TODO Auto-generated method stub
							Intent intent=new Intent(ShopGetInComeCashActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				else
				{
					if(getCashBean.getStatus().equals("1"))
					{
						Intent intent=new Intent(ShopGetInComeCashActivity.this,ShopGetCashCodeActivity.class);
						intent.putExtra("qrcode", getCashBean.getData().getQrcode());
						intent.putExtra("qrcodeContent", getCashBean.getData().getUrl());
						intent.putExtra("money", getcashmoneyedit.getText().toString());
						intent.putExtra("no", getCashBean.getData().getId());
						intent.putExtra("time", getCashBean.getData().getTime_create());
						startActivity(intent);
					}
					else
					{
						Toast.makeText(ShopGetInComeCashActivity.this, getCashBean.getInfo(), Toast.LENGTH_SHORT).show();
						return;
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

}
