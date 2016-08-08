package com.dudu.duduhelper;

import com.dudu.duduhelper.application.DuduHelperApplication;
import com.example.qr_codescan.MipcaActivityCapture;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShopGetCashCodeActivity extends BaseActivity 
{
    private String imageUrl;
    private String money;
    private ImageView ImageCode;
    private TextView cashMoneyText;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private LinearLayout getCashButton;
    private TextView getCashInputText;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_get_cash_code);
		initHeadView("收款", true,true, R.drawable.icon_bangzhutouming);
		DuduHelperApplication.getInstance().addActivity(this);
		imageUrl=getIntent().getStringExtra("qrcode");
		money=getIntent().getStringExtra("money");
		initView();
		
	}

	private void initView() 
	{
		// TODO Auto-generated method stub
		getCashInputText = (TextView) this.findViewById(R.id.getCashInputText);
		getCashButton = (LinearLayout) this.findViewById(R.id.getCashButton);
		ImageCode=(ImageView) this.findViewById(R.id.imageCashCodeImg);
		ImageAware imageAware = new ImageViewAware(ImageCode, false);
		imageLoader.displayImage(imageUrl, imageAware);
		cashMoneyText = (TextView) this.findViewById(R.id.cashMoneyText);
		cashMoneyText.setText("¥ "+money);
		getCashButton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShopGetCashCodeActivity.this, MipcaActivityCapture.class);
				intent.putExtra("action", "income");
				intent.putExtra("fee", money);
				intent.putExtra("body", "商家收款-扫码支付");
				startActivity(intent);
			}
		});
		getCashInputText.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(ShopGetCashCodeActivity.this,ShopGetInComeCashActivity.class);
				intent.putExtra("action", "getcashcode");
				intent.putExtra("fee", money);
				startActivity(intent);
			}
		});
	}

}
