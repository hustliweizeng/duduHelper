package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.adapter.ActivityMsGListAdapter;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.ActivityMsgBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.dudu.duduhelper.R.id.loading_progressBar;

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
	private  int sise = 10;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_create_activity_msg);
		adapter = new ActivityMsGListAdapter(context);
		initView();
		initHeadView("活动通知",true,false,0);
		initData();

	}

	private void initData() {

		//ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
		swiperefresh.setRefreshing(true);
		RequestParams params = new RequestParams();
		params.put("type_id","1");//红包的typeid是2
		params.put("page",page);
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
						ActivityMsgBean activityMsgBean = new Gson().fromJson(s, ActivityMsgBean.class);
						List<ActivityMsgBean.ListBean> list = activityMsgBean.getList();
						if (list!=null &&list.size()>0){
							adapter.addAll(list);
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
				ColorDialog.dissmissProcessDialog();
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
		recycleview_list.setLayoutManager(new LinearLayoutManager(this));
		recycleview_list.setAdapter(adapter);
		recycleview_list.setOnScrollListener(new RecyclerView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				super.onScrollStateChanged(recyclerView, newState);
				//滑动停止以后
				if (newState == RecyclerView.SCROLL_STATE_IDLE){
					//获取是否在底部
					boolean isBottom = recyclerView.canScrollVertically(1);

					//说明滑到底部
					if (isBottom){
						page++;
						initData();
					}
				}
				
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
