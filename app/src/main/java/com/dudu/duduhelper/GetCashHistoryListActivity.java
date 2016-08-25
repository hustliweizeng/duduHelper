package com.dudu.duduhelper;

import org.apache.http.Header;

import com.dudu.duduhelper.adapter.GetCashHistoryAdapter;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.bean.CashHistoryBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

public class GetCashHistoryListActivity extends BaseActivity {
	private GetCashHistoryAdapter getCashHistoryAdapter;
	private ListView cashHistoryListView;
	private SwipeRefreshLayout cashHistoryswipeLayout;
	private int page=1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_cash_history_list);
		initHeadView("提现记录", true, false, 0);
		getCashHistoryAdapter = new GetCashHistoryAdapter(this);
		DuduHelperApplication.getInstance().addActivity(this);
		initView();
		initData(page);
	}

	@SuppressLint("ResourceAsColor") 
	private void initView() 
	{
		// TODO Auto-generated method stub
		cashHistoryListView=(ListView) this.findViewById(R.id.cashHistoryListView);
		cashHistoryListView.setAdapter(getCashHistoryAdapter);
		cashHistoryswipeLayout = (SwipeRefreshLayout)this.findViewById(R.id.cashHistoryswipeLayout);
		cashHistoryswipeLayout.setColorSchemeResources(R.color.text_color);
		cashHistoryswipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		cashHistoryswipeLayout.setProgressBackgroundColor(R.color.bg_color);
		cashHistoryswipeLayout.setOnRefreshListener(new OnRefreshListener() 
		{
			
			@Override
			public void onRefresh() 
			{
				// TODO Auto-generated method stub
				page++;
				initData(page);
			}
		});
	}

	private void initData(final int page) 
	{
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("page", String.valueOf(page));
		params.add("pagesize","10");
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(GetCashHistoryListActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.GET_CASH_HISTORY, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(GetCashHistoryListActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				CashHistoryBean cashHistoryBean=new Gson().fromJson(arg2,CashHistoryBean.class);
				if(cashHistoryBean.getStatus().equals("-1006"))
				{
					//Toast.makeText(ProductListActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
					//Toast.makeText(getActivity(), "出错啦！", Toast.LENGTH_SHORT).show();
					MyDialog.showDialog(GetCashHistoryListActivity.this, "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(GetCashHistoryListActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				if(!cashHistoryBean.getStatus().equals("1"))
				{
					Toast.makeText(GetCashHistoryListActivity.this, "出错啦！", Toast.LENGTH_LONG).show();
				}
				else
				{
					if(cashHistoryBean.getData()!=null&&cashHistoryBean.getData().size()!=0)
					{
						getCashHistoryAdapter.addAll(cashHistoryBean.getData());
					}
					else
					{
						if(page==1)
						{
							Toast.makeText(GetCashHistoryListActivity.this, "暂无数据！", Toast.LENGTH_LONG).show();
						}
						else
						{
							Toast.makeText(GetCashHistoryListActivity.this, "加载完啦", Toast.LENGTH_LONG).show();
						}
					}
				}
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				//ColorDialog.dissmissProcessDialog();
				cashHistoryswipeLayout.setRefreshing(false);
			}
		});
	}

}
