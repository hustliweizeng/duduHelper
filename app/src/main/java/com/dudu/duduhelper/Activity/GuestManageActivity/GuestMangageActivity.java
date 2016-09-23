package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.adapter.GuestManageAdapter;

/**
 * @author
 * @version 1.0
 * @date 2016/9/22
 */

public class GuestMangageActivity extends BaseActivity implements View.OnClickListener {

	private TextView new_guest;
	private TextView total_guest;
	private Button send_message_guest_manage;
	private RecyclerView user_list;
	private SwipeRefreshLayout refreshLayout;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_guest_manager);
		initHeadView("会员管理",true,false,0);
		initView();
		initData();


	}

	private void initData() {
		
		user_list.setLayoutManager(new LinearLayoutManager(this));
		user_list.setAdapter(new GuestManageAdapter(context,null));
		Toast.makeText(context,"加载成功",Toast.LENGTH_SHORT).show();
		refreshLayout.setRefreshing(false);
	}

	private void initView() {
		new_guest = (TextView) findViewById(R.id.new_guest);
		total_guest = (TextView) findViewById(R.id.total_guest);
		send_message_guest_manage = (Button) findViewById(R.id.send_message_guest_manage);
		user_list = (RecyclerView) findViewById(R.id.user_list);
		refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_guest_magager);
		send_message_guest_manage.setOnClickListener(this);
		
		//上啦刷新按钮
		refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				initData();
				
			}
			
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.send_message_guest_manage:
				startActivity(new Intent(context,SendMessageActivity.class));
				break;
		}
	}
	
	
}
