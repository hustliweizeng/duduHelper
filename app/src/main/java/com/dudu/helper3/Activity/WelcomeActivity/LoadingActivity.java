package com.dudu.helper3.Activity.WelcomeActivity;

import android.content.Intent;
import android.os.Bundle;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.Activity.MainActivity;
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
