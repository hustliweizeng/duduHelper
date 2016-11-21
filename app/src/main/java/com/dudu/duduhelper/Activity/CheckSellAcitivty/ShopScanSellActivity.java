package com.dudu.duduhelper.Activity.CheckSellAcitivty;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.widget.ConfirmView;
import com.dudu.duduhelper.widget.DilatingDotsProgressBar;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 优惠券核销页面
 */
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
		initHeadView("券码核销", true, false, 0);
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
