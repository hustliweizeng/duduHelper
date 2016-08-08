package com.dudu.duduhelper;

import com.dudu.duduhelper.widget.ConfirmView;
import com.dudu.duduhelper.widget.DilatingDotsProgressBar;
import com.dudu.duduhelper.widget.MyKeyBoard;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class ShopScanSellActivity extends BaseActivity 
{
	private ConfirmView confirmView;
	private DilatingDotsProgressBar mDilatingDotsProgressBar;
	private Button submitButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_scan_sell);
		initHeadView("该优惠券可核销", true, false, 0);
		initView();
	}

	private void initView() 
	{
		submitButton = (Button) this.findViewById(R.id.submitButton);
		confirmView = (ConfirmView) findViewById(R.id.confirm_view);
		confirmView.animatedWithState(ConfirmView.State.Progressing);
		mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progress);
        mDilatingDotsProgressBar.showNow();
        submitButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				mDilatingDotsProgressBar.hideNow();
				confirmView.animatedWithState(ConfirmView.State.Success);
			}
		});
        
//        button1.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) 
//			{
//				// TODO Auto-generated method stub
//				mDilatingDotsProgressBar.hideNow();
//				confirmView.animatedWithState(ConfirmView.State.Fail);
//			}
//		});
	}

	
}
