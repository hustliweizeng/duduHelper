package com.dudu.helper3.Activity.GuestManageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.dudu.helper3.BaseActivity;
import com.dudu.duduhelper.R;

/**
 * @author
 * @version 1.0
 * @date 2016/10/12
 */

public class PayResultActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_pay_result);
		initHeadView("支付结果",true,false,0);
		Button btn_back = (Button) findViewById(R.id.btn_back);
		btn_back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context,SendMessageActivity.class));
				finish();
			}
		});
	}
}
