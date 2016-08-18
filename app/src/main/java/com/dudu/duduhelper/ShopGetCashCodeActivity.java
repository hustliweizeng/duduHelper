package com.dudu.duduhelper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dudu.duduhelper.application.DuduHelperApplication;
import com.example.qr_codescan.MipcaActivityCapture;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

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
		//获取传递过来的二维码和价格
		imageUrl=getIntent().getStringExtra("qrcode");
		money=getIntent().getStringExtra("money");
		initView();
		
	}

	private void initView() 
	{
		// TODO Auto-generated method stub
		//付款码收款
		getCashInputText = (TextView) this.findViewById(R.id.getCashInputText);
		//扫码收款
		getCashButton = (LinearLayout) this.findViewById(R.id.getCashButton);
		ImageCode=(ImageView) this.findViewById(R.id.imageCashCodeImg);
		ImageAware imageAware = new ImageViewAware(ImageCode, false);
		//用imageloader设置二维码图片
		imageLoader.displayImage(imageUrl, imageAware);
		cashMoneyText = (TextView) this.findViewById(R.id.cashMoneyText);
		//设置收款金额
		cashMoneyText.setText("¥ "+money);
		//扫码收款页面跳转
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
		//付款码收款页面跳转
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
