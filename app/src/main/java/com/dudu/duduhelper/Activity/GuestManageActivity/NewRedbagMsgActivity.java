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
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
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

public class NewRedbagMsgActivity extends BaseActivity implements View.OnClickListener {
	private TextView tv_guest_num;
	private LinearLayout ll_choose_guest;
	private EditText ed_price;
	private EditText ed_requirement;
	private EditText ed_expireday;
	private Button submitbtn;
	private ArrayList<CharSequence> checkedList;
	private ArrayList<CharSequence> checkedIDs =new ArrayList<>();

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
				startActivityForResult(intent,4);
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
			Toast.makeText(this, "请输入使用条件", Toast.LENGTH_SHORT).show();
			return;
		}

		String expireday = ed_expireday.getText().toString().trim();
		if (TextUtils.isEmpty(expireday)) {
			Toast.makeText(this, "请输入有效天数", Toast.LENGTH_SHORT).show();
			return;
		}
	
		//封装用户id
		String members = "";
		if (checkedIDs!=null&& checkedIDs.size()>0){
			LogUtil.d("update",checkedIDs.toString());
			members = checkedIDs.toString();
		}else {
			Toast.makeText(context,"选择的用户数不能为0!",Toast.LENGTH_SHORT).show();
			return;
		}
		//封装内容
		String detail ="";
		try {
			JSONObject object = new JSONObject();
			object.put("money",price);
			object.put("min_use_money",requirement);
			object.put("life",expireday);
			detail = object.toString();
			LogUtil.d("detail",detail);

		}catch (Exception e){
			e.printStackTrace();
		}
		RequestParams params = new RequestParams();
		params.put("type_id","2");
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
		if (requestCode ==4){
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
