package com.dudu.helper3.Activity.GuestManageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.Utils.Util;
import com.dudu.helper3.adapter.GuestManageAdapter;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.GuestListBean;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

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
	int page = 1;
	int num = 10;
	public GuestListBean guestList;
	private GuestManageAdapter adapter;
	private int lastVisibleItem;
	private int maxPage;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_guest_manager);
		initHeadView("客户管理",true,false,0);
		adapter = new GuestManageAdapter(context);
		initView();
		
		refreshLayout.setProgressViewOffset(false, 0, Util.dip2px(context, 24));//第一次启动时刷新
		refreshLayout.setRefreshing(true);
	}

	@Override
	public void onResume() {
		super.onResume();
		adapter.clear();
		initData(1);
	}

	/**
	 * 请求列表信息
	 */
	private void initData(int pageNum) {
		user_list.setLayoutManager(new LinearLayoutManager(this));
		user_list.setAdapter(adapter);
		RequestParams params = new RequestParams();
		params.put("page",pageNum);
		params.put("size",num);
		HttpUtils.getConnection(context, params, ConstantParamPhone.GET_GUEST_LIST, "POST", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				LogUtil.d("res",page+"=="+s);
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						guestList = new Gson().fromJson(s, GuestListBean.class);
						//設置頁面
						total_guest.setText(guestList.getCount().getTotal());
						new_guest.setText(guestList.getCount().getNow());
						if (guestList.getList()!= null &&guestList.getList().size()>0){
							adapter.addAll(guestList.getList());
						}
						maxPage = Integer.parseInt(guestList.getPage_info().getCount_page());
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFinish() {
				super.onFinish();
				refreshLayout.setRefreshing(false);
			}
		});
		
		
	}

	private void initView() {
		new_guest = (TextView) findViewById(R.id.new_guest);
		total_guest = (TextView) findViewById(R.id.total_guest);
		send_message_guest_manage = (Button) findViewById(R.id.send_message_guest_manage);
		user_list = (RecyclerView) findViewById(R.id.user_list);
		refreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_guest_magager);
		send_message_guest_manage.setOnClickListener(this);
		user_list.setLayoutManager(new LinearLayoutManager(this));
		//上啦刷新按钮
		refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				adapter.clear();
				initData(1);
			}
		});
		/**
		 * 下拉加载更多
		 */
		user_list.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				if (newState == RecyclerView.SCROLL_STATE_IDLE  &&lastVisibleItem ==adapter.getItemCount()-1){//当滑动停止的时候
					if (page <maxPage){
						page++;
						initData(page);
						user_list.scrollToPosition(0);
						LogUtil.d("load",page+"");
					}
				}
			}
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {//动态刷新最后一个可见的条目
				super.onScrolled(recyclerView, dx, dy);
				RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
				LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;//转换为线性布局
				lastVisibleItem = linearManager.findLastVisibleItemPosition();
				LogUtil.d("pos",lastVisibleItem+"");
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
