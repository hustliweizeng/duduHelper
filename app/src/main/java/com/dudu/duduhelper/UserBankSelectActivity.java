package com.dudu.duduhelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.dudu.duduhelper.adapter.BankAreAdapter;
import com.dudu.duduhelper.adapter.ProductTypeAdapter;
import com.dudu.duduhelper.bean.ProvienceBean;
import com.dudu.duduhelper.bean.UserBankListBean;
import com.dudu.duduhelper.bean.UserBankProvienceBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

public class UserBankSelectActivity extends BaseActivity 
{
	private ListView cuserbankListView;
	private SwipeRefreshLayout userbankswipeLayout;
	private ProductTypeAdapter productTypeAdapter;
	private BankAreAdapter bankAreAdapter;
	private String provienceCode;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_user_bank_select);
		initHeadView("选择开户行", true, false, 0);
		productTypeAdapter=new ProductTypeAdapter(this);
		bankAreAdapter=new BankAreAdapter(this);
		if(getIntent().getStringExtra("action").equals("bank"));
		{
			initBankData();
		}
		if(getIntent().getStringExtra("action").equals("province"))
		{
			initProvince();
		}
		if(getIntent().getStringExtra("action").equals("city"))
		{
			provienceCode=getIntent().getStringExtra("provienceCode");
			initCity();
		}
		initView();
	}

	private void initCity() 
	{
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("provinceid",provienceCode);
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		new AsyncHttpClient().get(ConstantParamPhone.IP+ConstantParamPhone.GET_USER_BANK_CITY, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(UserBankSelectActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				UserBankProvienceBean userBankProvienceBean=new Gson().fromJson(arg2,UserBankProvienceBean.class);
				if(userBankProvienceBean.getStatus().equals("-1006"))
				{
					//Toast.makeText(UserBankSelectActivity.this, "出错啦！", Toast.LENGTH_LONG).show();
					//Toast.makeText(getActivity(), "出错啦！", Toast.LENGTH_SHORT).show();
					MyDialog.showDialog(UserBankSelectActivity.this, "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(UserBankSelectActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				if(userBankProvienceBean.getStatus().equals("1"))
				{
					if(userBankProvienceBean.getData()!=null)
					{
						bankAreAdapter.addAll(userBankProvienceBean.getData(),"");
					}
					else
					{
						Toast.makeText(UserBankSelectActivity.this, "暂无数据！", Toast.LENGTH_LONG).show();
					}
				}
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				
			}
		});
	}

	private void initProvince() 
	{
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		new AsyncHttpClient().get(ConstantParamPhone.IP+ConstantParamPhone.GET_USER_BANK_PROVINCE, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(UserBankSelectActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				UserBankProvienceBean userBankProvienceBean=new Gson().fromJson(arg2,UserBankProvienceBean.class);
				if(!userBankProvienceBean.getStatus().equals("1"))
				{
					Toast.makeText(UserBankSelectActivity.this, "出错啦！", Toast.LENGTH_LONG).show();
				}
				else
				{
					userBankProvienceBean.getData();
					bankAreAdapter.addAll(userBankProvienceBean.getData(),"");
				}
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				
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
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				
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
		cuserbankListView=(ListView) this.findViewById(R.id.cuserbankListView);
		if(getIntent().getStringExtra("action").equals("bank"))
		{
			cuserbankListView.setAdapter(productTypeAdapter);
		}
		else
		{
			cuserbankListView.setAdapter(bankAreAdapter);
		
		}
		userbankswipeLayout.setOnRefreshListener(new OnRefreshListener() 
		{
			
			@Override
			public void onRefresh() 
			{
				// TODO Auto-generated method stub
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
					 intent.putExtra("province", (ProvienceBean)bankAreAdapter.getItem(position));
				 }
				 if(UserBankSelectActivity.this.getIntent().getStringExtra("action").equals("city"))
				 {
					 intent.putExtra("city", (ProvienceBean)bankAreAdapter.getItem(position));
				 }
                 setResult(RESULT_OK, intent);  
                 finish();  
			}
		});
		
	}

}
