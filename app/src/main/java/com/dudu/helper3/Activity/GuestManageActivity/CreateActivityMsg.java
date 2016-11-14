package com.dudu.helper3.Activity.GuestManageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.Utils.Util;
import com.dudu.helper3.adapter.ActivityMsGListAdapter;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.ActivityMsgBean;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/23
 */

public class CreateActivityMsg extends BaseActivity implements View.OnClickListener {
	private RecyclerView recycleview_list;
	private SwipeRefreshLayout swiperefresh;
	private Button submitbtn;
	private ActivityMsGListAdapter adapter;
	private  int page = 1;
	private  int sise = 2;
	private int lastVisibleItemPosition;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_create_activity_msg);
		adapter = new ActivityMsGListAdapter(context);
		initView();
		initHeadView("活动通知",true,false,0);
		swiperefresh.setProgressViewOffset(false, 0, Util.dip2px(context, 24));//第一次启动时刷新
		swiperefresh.setRefreshing(true);
	}
	@Override
	public void onResume() {
		super.onResume();
		initData(1);
	}
	private void initData(int pageNum) {
		RequestParams params = new RequestParams();
		params.put("type_id","1");//红包的typeid是2
		params.put("page",pageNum);
		params.put("size",sise);
		HttpUtils.getConnection(context, params, ConstantParamPhone.GET_SEND_RECORD, "post", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				LogUtil.d("acti",s);
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						boolean isEmpty;
						ActivityMsgBean activityMsgBean = new Gson().fromJson(s, ActivityMsgBean.class);
						List<ActivityMsgBean.ListBean> list = activityMsgBean.getList();
						if (list!=null &&list.size()>0){
							adapter.addAll(list);
							recycleview_list.setAdapter(adapter);//每次请求数据之后设置
							isEmpty = false;
						}else {
							isEmpty = true;
						}
						if(onLoadFinishi!=null){
							onLoadFinishi.LoadfFinish(isEmpty);
						}
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
				swiperefresh.setRefreshing(false);
			}
		});
		
	}

	/**
	 * 加载完成的回调监听
	 */
	public  interface OnLoadFinishi{
		void LoadfFinish(boolean isEmpty);
	};
	public  OnLoadFinishi onLoadFinishi;
	public void  setOnLoadFinishi(OnLoadFinishi onLoadFinishi){
		this.onLoadFinishi = onLoadFinishi;
	}

	private void initView() {
		recycleview_list = (RecyclerView) findViewById(R.id.recycleview_list);
		swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
		submitbtn = (Button) findViewById(R.id.submitbtn);
		submitbtn.setOnClickListener(this);
		swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				initData(1);
			}
		});
		recycleview_list.setLayoutManager(new LinearLayoutManager(this));
		recycleview_list.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				//滑动停止以后
				if (newState == RecyclerView.SCROLL_STATE_IDLE && lastVisibleItemPosition+1 == recyclerView.getChildCount()){
					page++;
					initData(page);
				}
			}
			@Override
			public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
				super.onScrolled(recyclerView, dx, dy);
				RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
				LinearLayoutManager manager= (LinearLayoutManager)layoutManager;
				lastVisibleItemPosition = manager.findLastVisibleItemPosition();
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
