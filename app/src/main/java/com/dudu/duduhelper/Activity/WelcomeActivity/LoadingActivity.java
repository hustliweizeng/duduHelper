package com.dudu.duduhelper.Activity.WelcomeActivity;

import android.content.Intent;
import android.os.Bundle;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Activity.MainActivity;
import com.dudu.duduhelper.R;

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
