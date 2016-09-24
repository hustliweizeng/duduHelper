package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.adapter.GuestListCheckAdapter;

/**
 * @author
 * @version 1.0
 * @date 2016/9/24
 */

public class GuestSelectActivity extends BaseActivity implements View.OnClickListener {
	private RecyclerView recycleview_list;
	private TextView tv_select_all;
	private TextView tv_submit;
	private GuestListCheckAdapter adapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_guest_select);
		initHeadView("选择客户", true, false, 0);
		adapter = new GuestListCheckAdapter(this);
		initView();
		initData();
	}

	private void initData() {
		//设置店铺点击事件
		adapter.setOnItemClickListner(new GuestListCheckAdapter.OnItemClickListner() {
			@Override
			public void onItemClick(View view, int position) {
				//点击
				Toast.makeText(context,"点击了"+position,Toast.LENGTH_SHORT).show();
			}
		});
		recycleview_list.setLayoutManager(new LinearLayoutManager(this));
		recycleview_list.setAdapter(adapter);
		


	}

	private void initView() {
		recycleview_list = (RecyclerView) findViewById(R.id.recycleview_list);
		tv_select_all = (TextView) findViewById(R.id.tv_select_all);
		tv_select_all.setOnClickListener(this);
		tv_submit = (TextView) findViewById(R.id.tv_submit);
		tv_submit.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case  R.id.recycleview_list:
				break;
			case  R.id.tv_select_all:
				adapter.addAll();
				break;
			case  R.id.tv_submit:
				break;

		}
	}
}
