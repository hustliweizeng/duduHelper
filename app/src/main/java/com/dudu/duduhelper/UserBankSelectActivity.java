package com.dudu.duduhelper;

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

import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.adapter.BankAreAdapter;
import com.dudu.duduhelper.adapter.BankCityListAdapter;
import com.dudu.duduhelper.adapter.ProductTypeAdapter;
import com.dudu.duduhelper.bean.UserBankListBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.CityClistBean;
import com.dudu.duduhelper.javabean.ProvinceListBean;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

public class UserBankSelectActivity extends BaseActivity 
{
	private ListView cuserbankListView;
	private SwipeRefreshLayout userbankswipeLayout;
	//银行列表
	private ProductTypeAdapter productTypeAdapter;
	//省份列表
	private BankAreAdapter bankAreAdapter;
	private String provienceCode;
	//城市列表
	private BankCityListAdapter cityListAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_bank_select);

		String title = null;
		productTypeAdapter=new ProductTypeAdapter(this);
		bankAreAdapter=new BankAreAdapter(this);
		cityListAdapter = new BankCityListAdapter(this);
		//根据不同来源初始化数据
		if(getIntent().getStringExtra("action").equals("bank"));
		{
			title = "我的银行卡";
			initBankData();
		}
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
		//初始化其他信息
		initView();
	}

	//初始化城市列表
	private void initCity() 
	{
		RequestParams params = new RequestParams();
		params.add("id",provienceCode);
		HttpUtils.getConnection(context,params,ConstantParamPhone.GET_CITY_LIST, "GET",new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(UserBankSelectActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				CityClistBean city = new Gson().fromJson(arg2,CityClistBean.class);
				if (!"SUCCESS".equalsIgnoreCase(city.getCode())){
					Toast.makeText(UserBankSelectActivity.this, "请稍后再试", Toast.LENGTH_LONG).show();
				}else {
					//请求数据成功
					if (city!= null){
						LogUtil.d("city==",city.getData().size()+"");
						cityListAdapter.addAll(city.getData(),"");
						LogUtil.d("size",cityListAdapter.getCount()+"");
						cityListAdapter.notifyDataSetChanged();
					}
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
				ProvinceListBean province = new Gson().fromJson(arg2, ProvinceListBean.class);
				if(!province.getCode().equals("SUCCESS"))
				{
					Toast.makeText(UserBankSelectActivity.this, "请稍后再试", Toast.LENGTH_LONG).show();
				}
				else
				{
					//请求数据成功,初始化时没有条目被选中
					bankAreAdapter.addAll(province.getData(),"");
				}
			}

		});
	}

	private void initBankData() 
	{
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		new AsyncHttpClient().get(ConstantParamPhone.IP+ConstantParamPhone.GET_USER_BANK, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(UserBankSelectActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				UserBankListBean userBankListBean=new Gson().fromJson(arg2,UserBankListBean.class);
				if(!userBankListBean.getStatus().equals("1"))
				{
					Toast.makeText(UserBankSelectActivity.this, "出错啦！", Toast.LENGTH_LONG).show();
				}
				else
				{
					userBankListBean.getData();
					productTypeAdapter.addAll(userBankListBean.getData());
				}
			}
		});
	}

	@SuppressLint("ResourceAsColor") 
	private void initView() 
	{
		// TODO Auto-generated method stub
		userbankswipeLayout=(SwipeRefreshLayout) this.findViewById(R.id.userbankswipeLayout);
		userbankswipeLayout.setColorSchemeResources(R.color.text_color);
		userbankswipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		userbankswipeLayout.setProgressBackgroundColor(R.color.bg_color);
		//listview设置条目点击事件
		cuserbankListView=(ListView) this.findViewById(R.id.cuserbankListView);
		//银行列表
		if(getIntent().getStringExtra("action").equals("bank"))
		{
			cuserbankListView.setAdapter(productTypeAdapter);
		}
		if(getIntent().getStringExtra("action").equals("province"))
		{
			//省市列表
			cuserbankListView.setAdapter(bankAreAdapter);
		}
		if (getIntent().getStringExtra("action").equals("city")){
			cuserbankListView.setAdapter(cityListAdapter);
		}
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
				// TODO Auto-generated method stub
				 Intent intent=new Intent();  
				 if(UserBankSelectActivity.this.getIntent().getStringExtra("action").equals("bank"))
				 {
					 intent.putExtra("bank", (String)productTypeAdapter.getItem(position));  
				 }
				 if(UserBankSelectActivity.this.getIntent().getStringExtra("action").equals("province"))
				 {
					 intent.putExtra("province", (ProvinceListBean.DataBean)bankAreAdapter.getItem(position));
				 }
				 if(UserBankSelectActivity.this.getIntent().getStringExtra("action").equals("city"))
				 {
					 intent.putExtra("city", (CityClistBean.DataBean)cityListAdapter.getItem(position));
				 }
                 setResult(RESULT_OK, intent);  
                 finish();  
			}
		});
		
	}

}
