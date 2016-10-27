package com.dudu.helper3.Activity.BigBandActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.dudu.helper3.R;
import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.adapter.BigBugShopSelectAdapter;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.CheckableShopbean;
import com.dudu.helper3.javabean.ShopListBean;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/10/26
 */

public class ShopSelectActivity extends BaseActivity implements View.OnClickListener {


	private ImageButton backButton;
	private ListView lv;
	private SwipeRefreshLayout swiperefresh;
	private ImageView lv_all;
	private Button btn_confirm;
	private LinearLayout ll_edit;
	private BigBugShopSelectAdapter adapter;
	private boolean isAll =false;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_select_shop);
		adapter = new BigBugShopSelectAdapter(context);
		
		initView();
		initData();
	}

	private void initCheckedIds() {
		ArrayList<String> data = getIntent().getStringArrayListExtra("data");
		adapter.setCheckedIds(data);
		
	}

	private void initData() {
		swiperefresh.setRefreshing(true);
		
		
		HttpUtils.getConnection(context, null, ConstantParamPhone.GET_SHOPABLE, "get", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				LogUtil.d("res",s);

				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						CheckableShopbean shopListBean = new Gson().fromJson(s, CheckableShopbean.class);
						if (shopListBean.getData()!=null){
							adapter.addAll(shopListBean.getData());
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
				initCheckedIds();//加载完成后，设置选中的条目
				
			}
		});
	}

	private void initView() {
		backButton = (ImageButton) findViewById(R.id.backButton);
		lv = (ListView) findViewById(R.id.lv);
		swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
		lv_all = (ImageView) findViewById(R.id.lv_all);
		lv_all.setOnClickListener(this);
		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		ll_edit = (LinearLayout) findViewById(R.id.ll_edit);

		backButton.setOnClickListener(this);
		btn_confirm.setOnClickListener(this);
		swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				initData();
			}
		});
		lv.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.backButton:
					finish();
				break;
			case R.id.btn_confirm:
				ArrayList<String> checkedList = adapter.getCheckedList();
				Intent intent = new Intent(context,ShopProductAddActivity.class);
				intent.putStringArrayListExtra("ids",checkedList);
				setResult(RESULT_OK,intent);
				finish();
				break;
			case R.id.lv_all:
				if (!isAll){
					adapter.checkAllIds();
					lv_all.setImageResource(R.drawable.select_check);
					isAll =!isAll;
				}else {
					adapter.removeAll();
					lv_all.setImageResource(R.drawable.select_empty);
					isAll =!isAll;
					
				}
				break;
		}
	}
}
