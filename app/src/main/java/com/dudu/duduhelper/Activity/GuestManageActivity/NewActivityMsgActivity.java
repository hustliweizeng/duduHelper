package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

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
	private TextView tv_num_remind;
	int max_num = 300;
	private ArrayList<CharSequence> checkedIDs;

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
		//字体输入监听
		ed_content.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				int letf_num = max_num-s.length();
				tv_num_remind.setText("剩余"+letf_num+"字");
			}
		});
		tv_num_remind = (TextView) findViewById(R.id.tv_num_remind);
		submitbtn = (Button) findViewById(R.id.submitbtn);
		submitbtn.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.submitbtn:
				submit();
				break;
			case R.id.ll_choose_guest:
				Intent intent = new Intent(context, GuestSelectActivity.class);
				if (checkedList!=null){
					intent.putCharSequenceArrayListExtra("checked",checkedList);
				}
				if (checkedIDs!=null){
					intent.putCharSequenceArrayListExtra("checked_id",checkedIDs);
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
		String members = "";
		if (checkedIDs!=null& checkedIDs.size()>0){
			LogUtil.d("update",checkedIDs.toString());
			members = checkedIDs.toString();
		}else {
			Toast.makeText(context,"选择的用户数不能为0!",Toast.LENGTH_SHORT).show();
			return;
		}
		String detail ="";
		try {
			JSONObject object = new JSONObject();
			object.put("title",title);
			object.put("desc",content);
			detail = object.toString();
			LogUtil.d("detail",detail);

		}catch (Exception e){
			e.printStackTrace();
		}

		RequestParams params = new RequestParams();
		params.put("type_id","1");
		params.put("members",members);
		params.put("params",detail);
		HttpUtils.getConnection(context, params, ConstantParamPhone.SEND_MESSAGE, "post", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				LogUtil.d("res",s);
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						Toast.makeText(context,"提交成功",Toast.LENGTH_SHORT).show();
						finish();
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,"发送失败,错误代码:"+msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
		


	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode ==2){
			ArrayList<CharSequence> list = data.getCharSequenceArrayListExtra("list");
			ArrayList<CharSequence> ids = data.getCharSequenceArrayListExtra("ids");

			if (list!=null ){
				tv_guest_num.setText(list.size()+"人");
				checkedList = list;
			}
			if (ids!=null){
				checkedIDs = ids;
				LogUtil.d("ids",ids.size()+"");
			}
		}
	}
}
