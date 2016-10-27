package com.dudu.helper3.Activity.MyInfoActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.adapter.BankProvinceListAdapter;
import com.dudu.helper3.adapter.BankCityListAdapter;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.CityClistBean;
import com.dudu.helper3.javabean.ProvinceListBean;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class UserBankSelectActivity extends BaseActivity 
{
	private ListView cuserbankListView;
	private SwipeRefreshLayout userbankswipeLayout;
	//省份列表
	private BankProvinceListAdapter provinceListAdapter;
	private String provienceCode;
	//城市列表
	private BankCityListAdapter cityListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_bank_select);
		provinceListAdapter = new BankProvinceListAdapter(context);
		cityListAdapter = new BankCityListAdapter(context);

		String title = null;
		cityListAdapter = new BankCityListAdapter(this);
		//初始化其他信息
		initView();
		//根据不同来源初始化数据
		if(getIntent().getStringExtra("action").equals("province"))
		{
			title ="选择省份";
			initProvince();
		}
		if(getIntent().getStringExtra("action").equals("city"))
		{
			provienceCode = getIntent().getStringExtra("provienceCode");
			title ="选择城市";
			initCity();
		}
		initHeadView(title, true, false, 0);

	}

	//初始化城市列表
	private void initCity() 
	{
		RequestParams params = new RequestParams();
		params.add("id",provienceCode);
		HttpUtils.getConnection(context,params, ConstantParamPhone.GET_CITY_LIST, "GET",new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(UserBankSelectActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				LogUtil.d("city",arg2);
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						CityClistBean city = new Gson().fromJson(arg2,CityClistBean.class);
						cityListAdapter.addAll(city.getData(),"");
						cuserbankListView.setAdapter(cityListAdapter);
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});
	}
	//直接联网加载省份信息
	private void initProvince() 
	{
		RequestParams params = new RequestParams();
		HttpUtils.getConnection(context,params,ConstantParamPhone.GET_PROVINCE_LIST, "GET",new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(UserBankSelectActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				LogUtil.d("province",arg2);
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						ProvinceListBean province = new Gson().fromJson(arg2, ProvinceListBean.class);
						provinceListAdapter.addAll(province.getData(),"");
						cuserbankListView.setAdapter(provinceListAdapter);
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				
			}

		});
	}

	@SuppressLint("ResourceAsColor") 
	private void initView() 
	{
		userbankswipeLayout=(SwipeRefreshLayout) this.findViewById(R.id.userbankswipeLayout);
		userbankswipeLayout.setColorSchemeResources(R.color.text_color);
		userbankswipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		userbankswipeLayout.setProgressBackgroundColor(R.color.bg_color);
		
		//listview设置条目点击事件
		cuserbankListView=(ListView) this.findViewById(R.id.cuserbankListView);
		userbankswipeLayout.setOnRefreshListener(new OnRefreshListener() 
		{
			
			@Override
			public void onRefresh() 
			{
				userbankswipeLayout.setRefreshing(false);
			}
		});
		cuserbankListView.setOnItemClickListener(new OnItemClickListener() 
        {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				//设置返回的数据
				 Intent intent=new Intent();  
				 if(getIntent().getStringExtra("action").equals("province"))
				 {
					 intent.putExtra("province", (ProvinceListBean.DataBean)provinceListAdapter.getItem(position));
				 }
				 if(getIntent().getStringExtra("action").equals("city"))
				 {
					 intent.putExtra("city", (CityClistBean.DataBean)cityListAdapter.getItem(position));
				 }
                 setResult(RESULT_OK, intent);  
                 finish();  
			}
		});
		
	}

}
