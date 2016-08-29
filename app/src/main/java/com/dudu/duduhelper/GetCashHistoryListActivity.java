package com.dudu.duduhelper;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.widget.ListView;
import android.widget.Toast;

import com.dudu.duduhelper.adapter.GetCashHistoryAdapter;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.bean.CashHistoryBean;
import com.dudu.duduhelper.bean.CashHistoryDataBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

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
				page++;
				initData(page);
			}
		});
	}

	private void initData(final int page) 
	{
		RequestParams params = new RequestParams();
		params.add("page", String.valueOf(page));
		params.add("pagesize","10");

        HttpUtils.getConnection(context,params,ConstantParamPhone.GET_OUT_MONEY_LIST, "GET",new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(GetCashHistoryListActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {
				CashHistoryBean cashHistoryBean = new Gson().fromJson(arg2, CashHistoryBean.class);
				if ("SUCESS".equalsIgnoreCase(cashHistoryBean.getCode())){
					//请求数据成功
					CashHistoryDataBean bean = new CashHistoryDataBean();
					bean.setMoney("300");
					bean.setTime("2016-08-26");
					bean.setStatus("1");
					//cashHistoryBean.getData().add(bean);
					List<CashHistoryDataBean> hisory = new ArrayList<CashHistoryDataBean>();
					hisory.add(bean);
					getCashHistoryAdapter.addAll(hisory);

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
