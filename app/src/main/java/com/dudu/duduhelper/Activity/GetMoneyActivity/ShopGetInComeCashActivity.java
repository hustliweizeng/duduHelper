package com.dudu.duduhelper.Activity.GetMoneyActivity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.Activity.CheckSellAcitivty.CheckSaleDetailActivity;
import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Activity.CashHistoryActivity.ShopMoneyRecordListActivity;
import com.dudu.duduhelper.widget.MyKeyBoard;
import com.example.qr_codescan.MipcaActivityCapture;
import com.dudu.duduhelper.R;
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
	private String orderId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_get_in_come_cash);
		String name = getIntent().getStringExtra("name");
		if (!TextUtils.isEmpty(name) &&"getMoney".equals(name) ){
			initHeadView("收款", true, true, R.drawable.icon_historical);
		}else {
			initHeadView("核销", true, true, R.drawable.icon_historical);
		}
		if(getIntent().getStringExtra("action")!=null)
		{
			action = getIntent().getStringExtra("action");
		}
		initView();
	}

	@Override
	public void RightButtonClick()
	{
		//进入收款历史纪录
		Intent intent = new Intent(this,ShopMoneyRecordListActivity.class);
		startActivity(intent);
	}

	private void initView()
	{
		//qrcodeButton=(Button) this.findViewById(R.id.qrcodeButton);

		wuzhelin = (LinearLayout) this.findViewById(R.id.wuzhelin);

		hexiaoRelLayout = (RelativeLayout) this.findViewById(R.id.hexiaoRelLayout);
		scanHexiaoButton = (LinearLayout) this.findViewById(R.id.scanHexiaoButton);
		//提交按钮
		submitbtn = (Button) this.findViewById(R.id.submitbtn);
		getcashmoneyicon = (TextView) this.findViewById(R.id.getcashmoneyicon);
		getCashText = (TextView) this.findViewById(R.id.getCashText);
		getcashmoneyDelectIconBtn=(ImageView) this.findViewById(R.id.getcashmoneyDelectIconBtn);
		getcashmoneyedit=(TextView) this.findViewById(R.id.getcashmoneyedit);
		/**
		 * 根据不同的action数据，设置不同的界面按钮
		 */
		if(action.equals("getcashcode"))
		{
			getCashText.setText("请输入用户的付款码编号");
			getcashmoneyicon.setVisibility(View.GONE);
			submitbtn.setText("提交");
			getcashmoneyedit.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
		}
		//从核销页面进入
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
		//从五折界面进入
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
		//创建键盘监听事件
		//设置键盘输入的监听事件
		MyKeyBoard keyBoard = new MyKeyBoard(this);
		keyBoard.setOnKeyBoardClickListener(new MyKeyBoard.OnKeyBoardClickListener()
		{

			@Override
			//判断如果是提交按钮
			public void onSubmit()
			{
				//跳转到其他页面
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
				//跳转到其他页面
				else if(action.equals("hexiao"))
				{
					if(TextUtils.isEmpty(getcashmoneyedit.getText().toString()))
					{
						Toast.makeText(ShopGetInComeCashActivity.this, "请输入用户的核销编号", Toast.LENGTH_SHORT).show();
						return;
					}
					Intent intent = new Intent(context,CheckSaleDetailActivity.class);
					intent.putExtra("result",getcashmoneyedit.getText());
					startActivity(intent);
					finish();
				}
				else
				{
					//请求网络数据，进入收款二维码页面
					enterNextPage();
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

	/**
	 * 价格信息传递到下一页
	 */
	private void enterNextPage() {
		String price = getcashmoneyedit.getText().toString();
		if (TextUtils.isEmpty(price)){
			Toast.makeText(context,"金额输入为空",Toast.LENGTH_SHORT).show();
		}
		Intent intent = new Intent(context,MipcaActivityCapture.class);
		intent.putExtra("price",price);
		intent.putExtra("action","income");
		startActivity(intent);
	}
	//
	

}
