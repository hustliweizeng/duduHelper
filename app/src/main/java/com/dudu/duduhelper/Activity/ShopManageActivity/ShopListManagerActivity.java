package com.dudu.duduhelper.Activity.ShopManageActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.adapter.ShopAdapterAdapter;
import com.dudu.duduhelper.bean.GetHongBaoHistBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.ShopListBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopListManagerActivity extends BaseActivity 
{
	private ListView lv_shop_list;
	private SwipeRefreshLayout refresh_shop_list;
	//判断数据是否加载完成
    private ShopAdapterAdapter adapter;
	private ShopListBean data;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_list_manager);
		initHeadView("门店管理", true, true, R.drawable.icon_tianjia);
		initView();
		initData();
	}
	
	@Override
	public void RightButtonClick() 
	{
		Intent intent = new Intent(ShopListManagerActivity.this,ShopAddActivity.class);
		intent.putExtra("source","add");
		startActivity(intent);
	}
	
	@SuppressLint("ResourceAsColor") 
	private void initView() 
	{
		
		
		//listview设置
		adapter=new ShopAdapterAdapter(this);
		lv_shop_list=(ListView) this.findViewById(R.id.lv_shop_list);
		lv_shop_list.setAdapter(adapter);
		//设置下拉更新，上啦加载
		refresh_shop_list = (SwipeRefreshLayout)this.findViewById(R.id.refresh_shop_list);
		refresh_shop_list.setColorSchemeResources(R.color.text_color);
		refresh_shop_list.setSize(SwipeRefreshLayout.DEFAULT);
		refresh_shop_list.setProgressBackgroundColor(R.color.bg_color);
		refresh_shop_list.setOnRefreshListener(new OnRefreshListener() 
		{
			
			@Override
			public void onRefresh() 
			{
				//下拉刷新的时候更新数据
				adapter.clear();
				initData();
			}
		});
		
		//条目点击监听
		lv_shop_list.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				Intent intent = new Intent(ShopListManagerActivity.this,ShopAddActivity.class);
				intent.putExtra("shopId", data.getData().get(position).getId());
				intent.putExtra("source","detail");
				startActivity(intent);
			}
		});
	}
	
	//获取数据
	private void initData() 
	{
		HttpUtils.getConnection(context,null,ConstantParamPhone.GET_SHOP_LIST, "GET",new TextHttpResponseHandler()
		{

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(context, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						data = new Gson().fromJson(arg2, ShopListBean.class);
						//防止多次加入数据，先清空原有数据
						adapter.clear();
						adapter.addAll(data.getData());
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
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				refresh_shop_list.setRefreshing(false);
				ColorDialog.dissmissProcessDialog();
			}
		});
	}
	//页面返回后，重新加载数据
	@Override
	public void onResume() {
		super.onResume();
		initData();
	}
}
