package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.adapter.GuestListCheckAdapter;
import com.dudu.duduhelper.adapter.RedbagMsgListAdapter;

/**
 * @author
 * @version 1.0
 * @date 2016/9/23
 */

public class CreateRedbagmsgActivity extends BaseActivity {
	private TextView tv_send_num;
	private TextView tv_create_money;
	private RecyclerView recycleview_msg;
	private SwipeRefreshLayout refresh_msg;
	private Button submitbtn;
	private RedbagMsgListAdapter adapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_create_redbag_msg);
		initHeadView("红包通知",true,false,0);
		adapter = new RedbagMsgListAdapter(context);
		initView();
		initData();
	}

	private void initData() {
		recycleview_msg.setLayoutManager(new LinearLayoutManager(this));
		recycleview_msg.setAdapter(adapter);
		adapter.setOnItemClickListner(new GuestListCheckAdapter.OnItemClickListner() {
			@Override
			public void onItemClick(View view, int position) {
				startActivity(new Intent(context,RedbagMsgDetail.class));
			}
		});
		
	}

	private void initView() {
		tv_send_num = (TextView) findViewById(R.id.tv_send_num);
		tv_create_money = (TextView) findViewById(R.id.tv_create_money);
		recycleview_msg = (RecyclerView) findViewById(R.id.recycleview_msg);
		submitbtn = (Button) findViewById(R.id.submitbtn);
		refresh_msg = (SwipeRefreshLayout) findViewById(R.id.refresh_msg);
		refresh_msg.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				initData();
				refresh_msg.setRefreshing(false);
			}
		});
		
		submitbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context,NewRedbagMsgActivity.class));
			}
		});
	}
}
