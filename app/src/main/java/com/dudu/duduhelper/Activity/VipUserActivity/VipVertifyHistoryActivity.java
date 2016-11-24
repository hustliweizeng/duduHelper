package com.dudu.duduhelper.Activity.VipUserActivity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.adapter.VertifyVipHistoryAdapter;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.VipCertifyHistoryBean;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;
import com.dudu.duduhelper.R;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author
 * @version 1.0
 * @date 2016/11/3
 */

public class VipVertifyHistoryActivity extends BaseActivity  {
	private ListView listview;
	private SwipeRefreshLayout siwpeRefresh;
	private VertifyVipHistoryAdapter adapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activty_virtify_history);
		initHeadView("验证记录", true, false, 0);
		adapter = new VertifyVipHistoryAdapter(this);
		initView();
		siwpeRefresh.setProgressViewOffset(false, 0, Util.dip2px(context, 24));//第一次启动时刷新
		siwpeRefresh.setRefreshing(true);
		initData();
	}

	private void initData() {
		HttpUtils.getConnection(context, null, ConstantParamPhone.GET_VIP_HISTORY, "get", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						VipCertifyHistoryBean data = new Gson().fromJson(s, VipCertifyHistoryBean.class);
						adapter.addAll(data.getList());

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
				siwpeRefresh.setRefreshing(false);
			}
		});
		
		
		
	}

	private void initView() {
		listview = (ListView) findViewById(R.id.listview);
		listview.setAdapter(adapter);
		siwpeRefresh = (SwipeRefreshLayout) findViewById(R.id.siwpeRefresh);
		siwpeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				initData();
			}
		});
	}

}
