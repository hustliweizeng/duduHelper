package com.dudu.helper3.Activity.RedBagActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.R;

/**
 * Created by lwz on 2016/8/18.
 */
public class CreateRedBagActivity extends BaseActivity implements View.OnClickListener {

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_new_redbag);
		initview();


	}

	private void initview() {
		Button btn_create_redbag = (Button) findViewById(R.id.btn_create_redbag);
		ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
		backButton.setOnClickListener(this);
		btn_create_redbag.setOnClickListener(this);

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()){
			case R.id.btn_create_redbag:
				startActivity(new Intent(this,EditRedbag1Activity.class));
				break;
			case R.id.backButton:
				finish();
				break;



		}

	}
}
