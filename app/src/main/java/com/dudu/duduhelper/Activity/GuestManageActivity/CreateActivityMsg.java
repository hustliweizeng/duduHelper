package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.adapter.ActivityMsGListAdapter;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author
 * @version 1.0
 * @date 2016/9/23
 */

public class CreateActivityMsg extends BaseActivity implements View.OnClickListener {
	private RecyclerView recycleview_list;
	private SwipeRefreshLayout swiperefresh;
	private Button submitbtn;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_create_activity_msg);
		initView();
		initHeadView("活动通知",true,false,0);
		initData();

	}

	private void initData() {
		recycleview_list.setLayoutManager(new LinearLayoutManager(this));
		recycleview_list.setAdapter(new ActivityMsGListAdapter(context));
		swiperefresh.setRefreshing(false);
		HttpUtils.getConnection(context, null, ConstantParamPhone.GET_SEND_RECORD+"1", "GET", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功


					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
		
	}

	private void initView() {
		recycleview_list = (RecyclerView) findViewById(R.id.recycleview_list);
		swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
		submitbtn = (Button) findViewById(R.id.submitbtn);
		submitbtn.setOnClickListener(this);
		swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				initData();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.submitbtn:
				startActivity(new Intent(context,NewActivityMsgActivity.class));

				break;
		}
	}
}
