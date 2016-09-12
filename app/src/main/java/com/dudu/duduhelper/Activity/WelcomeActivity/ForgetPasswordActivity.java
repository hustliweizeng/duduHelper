package com.dudu.duduhelper.Activity.WelcomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author
 * @version 1.0
 * @date 2016/9/11
 */
public class ForgetPasswordActivity extends BaseActivity implements View.OnClickListener {
	private EditText ed_create_password_first;
	private EditText ed_create_password_second;
	private Button bt_submit_pwd;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_forget_password);
		initHeadView("重新设置密码", true, false, 0);
		initView();
	}

	private void initView() {

		ed_create_password_first = (EditText) findViewById(R.id.ed_create_password_first);
		ed_create_password_first.setOnClickListener(this);
		ed_create_password_second = (EditText) findViewById(R.id.ed_create_password_second);
		ed_create_password_second.setOnClickListener(this);
		bt_submit_pwd = (Button) findViewById(R.id.bt_submit_pwd);
		bt_submit_pwd.setOnClickListener(this);
	}


	private void initData() {
		String code = getIntent().getStringExtra("code");
		String mobile = getIntent().getStringExtra("mobile");
		RequestParams params = new RequestParams();
		params.put("code", code);
		params.put("mobile", mobile);
		HttpUtils.getConnection(context, params, ConstantParamPhone.BIND_PHONE, "post", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context, "网络异常，稍后再试", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				try {
					JSONObject object = new JSONObject(s);
					String code = object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)) {
						//数据请求成功,重新登陆
						Toast.makeText(context, "密码设置成功，请重新登陆", Toast.LENGTH_SHORT).show();
						startActivity(new Intent(context, LoginActivity.class));
						finish();
					} else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.bt_submit_pwd:
				submit();
				initData();

				break;
		}
	}

	private void submit() {
		// validate
		String first = ed_create_password_first.getText().toString().trim();
		if (TextUtils.isEmpty(first)) {
			Toast.makeText(this, "请输入新密码", Toast.LENGTH_SHORT).show();
			return;
		}

		String second = ed_create_password_second.getText().toString().trim();
		if (TextUtils.isEmpty(second)) {
			Toast.makeText(this, "再次输入新密码", Toast.LENGTH_SHORT).show();
			return;
		}
		if (first.equalsIgnoreCase(second)){
			Toast.makeText(this, "两次输入密码不一致", Toast.LENGTH_SHORT).show();
			return;
		}

	}
}
