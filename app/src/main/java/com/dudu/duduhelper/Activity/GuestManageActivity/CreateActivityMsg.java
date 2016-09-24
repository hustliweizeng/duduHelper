package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.adapter.ActivityMsGListAdapter;

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
