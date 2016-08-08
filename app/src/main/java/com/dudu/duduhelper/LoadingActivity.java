package com.dudu.duduhelper;

import org.apache.http.Header;

import com.dudu.duduhelper.bean.UserBean;
import com.dudu.duduhelper.common.Util;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class LoadingActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		Intent intent = new Intent(LoadingActivity.this, MainActivity.class);
		startActivity(intent);
	}

}
