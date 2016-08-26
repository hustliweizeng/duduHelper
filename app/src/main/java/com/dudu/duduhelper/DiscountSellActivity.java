package com.dudu.duduhelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.bean.DiscountBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

public class DiscountSellActivity extends BaseActivity 
{
	private Button sellHistoryBtn;
	private EditText discountNumEditText;
	private Button discountSellbutton;
	private TextView discountFirstNumTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discount_sell);
		initHeadView("五折卡验证", true, false, 0);
		initView();
		
	}
    

	private void initView() 
	{
		// TODO Auto-generated method stub
		
		discountNumEditText=(EditText) this.findViewById(R.id.discountNumEditText);
		discountSellbutton=(Button) this.findViewById(R.id.discountSellbutton);
		discountFirstNumTextView=(TextView) this.findViewById(R.id.discountFirstNumEditText);
		if(!TextUtils.isEmpty(getIntent().getStringExtra("first")))
		{
			discountFirstNumTextView.setText(getIntent().getStringExtra("first"));
		}
//		sellHistoryBtn=(Button) this.findViewById(R.id.selectTextClickButton);
//		sellHistoryBtn.setText("验证记录");
//		sellHistoryBtn.setVisibility(View.VISIBLE);
//		sellHistoryBtn.setOnClickListener(new OnClickListener() 
//		{
//			
//			@Override
//			public void onClick(View arg0) 
//			{
//				// TODO Auto-generated method stub
//			    Toast.makeText(DiscountSellActivity.this, "哈哈哈", Toast.LENGTH_SHORT).show();
//			}
//		});
		discountSellbutton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(discountNumEditText.getText().toString())||discountNumEditText.getText().toString().trim().length()<8)
				{
					Toast.makeText(DiscountSellActivity.this, "请输入卡号后八位数字", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(discountFirstNumTextView.getText().toString()))
				{
					Toast.makeText(DiscountSellActivity.this, "请获取卡号前缀", Toast.LENGTH_SHORT).show();
					return;
				}
				
				initData();

			}
		});
	}
	private void initData() 
	{
		// TODO Auto-generated method stub
		ColorDialog.showRoundProcessDialog(DiscountSellActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("no", (discountFirstNumTextView.getText().toString().trim()+discountNumEditText.getText().toString().trim()));
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(DiscountSellActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.post(ConstantParamPhone.IP+ConstantParamPhone.GET_SELL_DISCOUNT, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(DiscountSellActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				DiscountBean discountBean=new Gson().fromJson(arg2, DiscountBean.class);
				if(discountBean.getStatus().equals("-1006"))
				{
					MyDialog.showDialog(DiscountSellActivity.this, "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(DiscountSellActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				if(discountBean.getStatus().equals("0"))
				{
					Intent intent=new Intent(DiscountSellActivity.this,DiscountSellResultActivity.class);
					intent.putExtra("status", 0);
					intent.putExtra("data", discountBean.getData());
					startActivity(intent);		
				}
				if(discountBean.getStatus().equals("-1"))
				{
					Intent intent=new Intent(DiscountSellActivity.this,DiscountSellResultActivity.class);
					intent.putExtra("status", -1);
					intent.putExtra("info",discountBean.getInfo());
					startActivity(intent);
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
