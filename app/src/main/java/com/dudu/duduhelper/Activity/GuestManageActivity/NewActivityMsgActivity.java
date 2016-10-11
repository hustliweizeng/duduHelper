package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;

import java.util.ArrayList;

/**
 * @author
 * @version 1.0
 * @date 2016/9/24
 */

public class NewActivityMsgActivity extends BaseActivity implements View.OnClickListener {
	private TextView tv_guest_num;
	private LinearLayout ll_choose_guest;
	private EditText ed_title;
	private EditText ed_content;
	private Button submitbtn;
	private ArrayList<CharSequence> checkedList;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_new_activity_msg);
		initView();
		initHeadView("新建活动通知", true, false, 0);
	}

	private void initView() {
		tv_guest_num = (TextView) findViewById(R.id.tv_guest_num);
		ll_choose_guest = (LinearLayout) findViewById(R.id.ll_choose_guest);
		ll_choose_guest.setOnClickListener(this);
		ed_title = (EditText) findViewById(R.id.ed_title);
		ed_content = (EditText) findViewById(R.id.ed_content);
		submitbtn = (Button) findViewById(R.id.submitbtn);

		submitbtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.submitbtn:

				break;
			case R.id.ll_choose_guest:
				Intent intent = new Intent(context, GuestSelectActivity.class);
				if (checkedList!=null){
					intent.putCharSequenceArrayListExtra("checked",checkedList);
				}
				startActivityForResult(intent,2);
				break;
		}
	}

	private void submit() {
		// validate
		String title = ed_title.getText().toString().trim();
		if (TextUtils.isEmpty(title)) {
			Toast.makeText(this, "请编辑标题", Toast.LENGTH_SHORT).show();
			return;
		}

		String content = ed_content.getText().toString().trim();
		if (TextUtils.isEmpty(content)) {
			Toast.makeText(this, "content不能为空", Toast.LENGTH_SHORT).show();
			return;
		}

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode ==2){
			ArrayList<CharSequence> list = data.getCharSequenceArrayListExtra("list");
			if (list!=null ){
				tv_guest_num.setText(list.size()+"人");
				checkedList = list;
			}
		}
	}
}
