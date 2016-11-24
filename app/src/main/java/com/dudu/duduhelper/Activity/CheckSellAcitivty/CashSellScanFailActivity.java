package com.dudu.duduhelper.Activity.CheckSellAcitivty;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.dudu.duduhelper.BaseActivity;
import com.example.qr_codescan.MipcaActivityCapture;
import com.dudu.duduhelper.R;
public class CashSellScanFailActivity extends BaseActivity 
{
	private Button enterSellbutton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cash_sell_scan_fail);
		initHeadView("优惠券核销", true, false,R.drawable.icon_historical);
		initView();
	}

	private void initView() 
	{
		// TODO Auto-generated method stub
		enterSellbutton=(Button) this.findViewById(R.id.enterSellbutton);
		enterSellbutton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(CashSellScanFailActivity.this, MipcaActivityCapture.class);
				startActivity(intent);
				finish();
			}
		});
	}

}
