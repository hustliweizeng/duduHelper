package com.dudu.helper3.Activity.VipUserActivity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.Utils.Util;
import com.dudu.helper3.adapter.VertifyVipHistoryAdapter;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.VipCertifyHistoryBean;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
