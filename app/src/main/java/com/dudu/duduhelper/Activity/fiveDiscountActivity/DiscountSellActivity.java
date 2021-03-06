package com.dudu.duduhelper.Activity.fiveDiscountActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.FiveDiscountBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.dudu.duduhelper.R;
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
		
		discountNumEditText=(EditText) this.findViewById(R.id.discountNumEditText);
		discountSellbutton=(Button) this.findViewById(R.id.discountSellbutton);
		discountFirstNumTextView=(TextView) this.findViewById(R.id.discountFirstNumEditText);
		if(!TextUtils.isEmpty(getIntent().getStringExtra("first")))
		{
			discountFirstNumTextView.setText(getIntent().getStringExtra("first"));
		}
		discountSellbutton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				if(TextUtils.isEmpty(discountNumEditText.getText().toString())||discountNumEditText.getText().toString().trim().length()<8)
				{
					Toast.makeText(DiscountSellActivity.this, "请输入卡号后八位数字", Toast.LENGTH_SHORT).show();
					return;
				}
				initData();

			}
		});
	}
	private void initData() 
	{
		RequestParams params = new RequestParams();
		params.add("code", discountNumEditText.getText().toString().trim());
        HttpUtils.getConnection(context,params,ConstantParamPhone.VERTIFY_CARD, "post",new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(DiscountSellActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						FiveDiscountBean fiveDiscountBean = new Gson().fromJson(arg2, FiveDiscountBean.class);

						//数据请求成功,验证成功以后使用五折卡页面
						//Toast.makeText(context,"验证成功",Toast.LENGTH_SHORT).show();
						Intent intent = new Intent(context, DiscountSellResultActivity.class);
						intent.putExtra("data",fiveDiscountBean);
						intent.putExtra("id",discountNumEditText.getText().toString().trim());
						startActivity(intent);
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFinish() 
			{
				ColorDialog.dissmissProcessDialog();
			}
		});
		
		

	}

	
}
