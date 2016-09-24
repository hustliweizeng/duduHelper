package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;

/**
 * @author
 * @version 1.0
 * @date 2016/9/24
 */

public class NewRedbagMsgActivity extends BaseActivity implements View.OnClickListener {
	private TextView tv_guest_num;
	private LinearLayout ll_choose_guest;
	private EditText ed_price;
	private EditText ed_requirement;
	private EditText ed_expireday;
	private Button submitbtn;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_new_redbag_msg);
		initView();
		initHeadView("新建红包通知", true, false, 0);
	}

	private void initView() {
		tv_guest_num = (TextView) findViewById(R.id.tv_guest_num);
		ll_choose_guest = (LinearLayout) findViewById(R.id.ll_choose_guest);
		ll_choose_guest.setOnClickListener(this);
		ed_price = (EditText) findViewById(R.id.ed_price);
		ed_requirement = (EditText) findViewById(R.id.ed_requirement);
		ed_expireday = (EditText) findViewById(R.id.ed_expireday);
		submitbtn = (Button) findViewById(R.id.submitbtn);

		submitbtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.submitbtn:

				break;
			case R.id.ll_choose_guest:
				startActivity(new Intent(context,GuestSelectActivity.class));
				break;
		}
	}

	private void submit() {
		// validate
		String price = ed_price.getText().toString().trim();
		if (TextUtils.isEmpty(price)) {
			Toast.makeText(this, "请输入金额", Toast.LENGTH_SHORT).show();
			return;
		}

		String requirement = ed_requirement.getText().toString().trim();
		if (TextUtils.isEmpty(requirement)) {
			Toast.makeText(this, "满20元可用", Toast.LENGTH_SHORT).show();
			return;
		}

		String expireday = ed_expireday.getText().toString().trim();
		if (TextUtils.isEmpty(expireday)) {
			Toast.makeText(this, "21", Toast.LENGTH_SHORT).show();
			return;
		}

		// TODO validate success, do something


	}
}
