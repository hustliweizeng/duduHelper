package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.adapter.RedbagMsgListAdapter;

/**
 * @author
 * @version 1.0
 * @date 2016/9/23
 */

public class CreateActivityMsg extends BaseActivity {
	private TextView tv_send_num;
	private TextView tv_create_money;
	private RecyclerView recycleview_msg;
	private SwipeRefreshLayout refresh_msg;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_create_redbag_msg);
		initHeadView("活动通知",true,false,0);
		initView();
		initData();
	}

	private void initData() {
		recycleview_msg.setAdapter(new RedbagMsgListAdapter(context));
	}

	private void initView() {
		tv_send_num = (TextView) findViewById(R.id.tv_send_num);
		tv_create_money = (TextView) findViewById(R.id.tv_create_money);
		recycleview_msg = (RecyclerView) findViewById(R.id.recycleview_msg);
		refresh_msg = (SwipeRefreshLayout) findViewById(R.id.refresh_msg);
		refresh_msg.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				initData();
				refresh_msg.setRefreshing(false);
			}
		});
	}
}
