package com.dudu.helper3.Activity.CheckSellAcitivty;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dudu.helper3.BaseActivity;
import com.dudu.duduhelper.R;

public class MemberSellActivity extends BaseActivity 
{
	private EditText orderNameEditText;
	private EditText orderCashEditText;
	private Button nextStepbutton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_sell);
		initHeadView("会员卡核销", true, false,R.drawable.icon_historical);
		initView();
	}

	private void initView() 
	{
		// TODO Auto-generated method stub
		orderNameEditText=(EditText) this.findViewById(R.id.orderNameEditText);
		orderCashEditText=(EditText) this.findViewById(R.id.orderCashEditText);
		nextStepbutton=(Button) this.findViewById(R.id.nextStepbutton);
		nextStepbutton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(orderNameEditText.getText().toString().trim()))
				{
					Toast.makeText(MemberSellActivity.this, "请填写订单名称",Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(orderCashEditText.getText().toString().trim()))
				{
					Toast.makeText(MemberSellActivity.this, "请填写订单金额",Toast.LENGTH_SHORT).show();
					return;
				}
				//验证通过
				Intent intent=new Intent(MemberSellActivity.this,MemberSellStepTwoActivity.class);
				intent.putExtra("orderName", orderNameEditText.getText().toString().trim());
				intent.putExtra("orderCash", orderCashEditText.getText().toString().trim());
				startActivity(intent);
			}
		});
	}

	
}
