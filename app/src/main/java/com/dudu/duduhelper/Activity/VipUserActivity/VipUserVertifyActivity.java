package com.dudu.duduhelper.Activity.VipUserActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.VipDetailBean;
import com.dudu.duduhelper.widget.MyKeyBoard;
import com.example.qr_codescan.MipcaActivityCapture;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author
 * @version 1.0
 * @date 2016/11/2
 */

public class VipUserVertifyActivity extends BaseActivity {
	private TextView tv_card_no;
	private ImageView btn_del;
	private LinearLayout ll_scan;
	private Button submitbtn;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_vip_vertify);
		initView();
		initHeadView("会员卡验证",true,true,R.drawable.icon_historical);
	}

	@Override
	public void RightButtonClick() {
		super.RightButtonClick();
		startActivity(new Intent(context,VipVertifyHistoryActivity.class));
	}

	private void initView() {
		tv_card_no = (TextView) findViewById(R.id.tv_card_no);
		btn_del = (ImageView) findViewById(R.id.btn_del);
		ll_scan = (LinearLayout) findViewById(R.id.ll_scan);
		submitbtn = (Button) findViewById(R.id.submitbtn);
		submitbtn.setText("提交");
		btn_del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				tv_card_no.setText("");
			}
		});
		ll_scan.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(context, MipcaActivityCapture.class);
				intent.putExtra("action","vip");
				startActivity(intent);
			}
		});
		//创建键盘监听事件
		//设置键盘输入的监听事件
		MyKeyBoard keyBoard = new MyKeyBoard(this);
		keyBoard.setOnKeyBoardClickListener(new MyKeyBoard.OnKeyBoardClickListener()
		{
			@Override
			//判断如果是提交按钮
			public void onSubmit()
			{
				requestVertify();
			
			}
			@Override
			public void onDelect()//删除按钮
			{
				if(!TextUtils.isEmpty(tv_card_no.getText()))//判断输入是否为空
				{
					//删掉一个数字
					tv_card_no.setText(tv_card_no.getText().toString().substring(0, tv_card_no.getText().toString().length()-1));
					if(TextUtils.isEmpty(tv_card_no.getText()))
					{
						btn_del.setVisibility(View.GONE);//删除按钮不可见
					}
				}
				else
				{
					btn_del.setVisibility(View.GONE);
				}
			}

			@Override
			public void onClick(String content)
			{
				btn_del.setVisibility(View.VISIBLE);
				tv_card_no.setText(tv_card_no.getText()+content);//追加数据显示出来
			}
		});
	}

	/**
	 * 请求验证会员卡
	 */
	private void requestVertify() {
		final String number = tv_card_no.getText().toString().trim();
		if (TextUtils.isEmpty(number)){
			Toast.makeText(context,"卡号不能为空",Toast.LENGTH_SHORT).show();
			return;
		}

		HttpUtils.getConnection(context, null, ConstantParamPhone.GET_VIP_INFO+number, "get", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {

				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					Intent intent = new Intent(context,VipUserVertifyResActivity.class);
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						VipDetailBean detail = new Gson().fromJson(s, VipDetailBean.class);
						//获取验证结果，跳转到相应页面
						intent.putExtra("res",0);//成功
						intent.putExtra("detail",detail);//详情
						intent.putExtra("id",number);
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						intent.putExtra("res",1);//失败
						intent.putExtra("msg",msg);//错误信息
					}
					startActivity(intent);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
