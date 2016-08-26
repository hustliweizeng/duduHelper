package com.dudu.duduhelper;

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

import com.dudu.duduhelper.adapter.HongbaoHistoryAdapter;
import com.dudu.duduhelper.bean.GetHongBaoHistBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

public class ShopHongbaoHistoryListActivity extends BaseActivity {
	private ListView hongbaoHistoryListView;
	private SwipeRefreshLayout hongbaoHistoryswipeLayout;
	//判断数据是否加载完成
    private boolean reffinish=false;
    private View footView;
    private ProgressBar loading_progressBar;
    private TextView loading_text;
    private Button reloadButton;
    private int page=1;
    private int lastItemIndex;
    private HongbaoHistoryAdapter hongbaoHistoryAdapter;
    private String hongbaoId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_hongbao_history_list_layout);
		initHeadView("领取记录", true, false, 0);
		initData();
	}

	@SuppressLint("ResourceAsColor") 
	private void initView() 
	{
		// TODO Auto-generated method stub
		hongbaoId=getIntent().getStringExtra("hongbaoId");
		footView = LayoutInflater.from(this).inflate(R.layout.activity_listview_foot, null);
		loading_progressBar=(ProgressBar) footView.findViewById(R.id.loading_progressBar);
		loading_text=(TextView) footView.findViewById(R.id.loading_text);
		// TODO Auto-generated method stub
		reloadButton=(Button) this.findViewById(R.id.reloadButton);
		//数据重载按钮
		reloadButton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				ColorDialog.showRoundProcessDialog(ShopHongbaoHistoryListActivity.this,R.layout.loading_process_dialog_color);
				initData();
			}
		});
		hongbaoHistoryAdapter=new HongbaoHistoryAdapter(this);
		hongbaoHistoryListView=(ListView) this.findViewById(R.id.hongbaoHistoryListView);
		hongbaoHistoryListView.setAdapter(hongbaoHistoryAdapter);
		hongbaoHistoryListView.addFooterView(footView,null,false);
		hongbaoHistoryswipeLayout = (SwipeRefreshLayout)this.findViewById(R.id.hongbaoHistoryswipeLayout);
		hongbaoHistoryswipeLayout.setColorSchemeResources(R.color.text_color);
		hongbaoHistoryswipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		hongbaoHistoryswipeLayout.setProgressBackgroundColor(R.color.bg_color);
		hongbaoHistoryswipeLayout.setOnRefreshListener(new OnRefreshListener() 
		{
			
			@Override
			public void onRefresh() 
			{
				// TODO Auto-generated method stub
				page=1;
				hongbaoHistoryAdapter.clear();
				initData();
			}
		});
		hongbaoHistoryListView.setOnScrollListener(new OnScrollListener() 
		{
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) 
			{
				// TODO Auto-generated method stub
				//
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE&&lastItemIndex == hongbaoHistoryAdapter.getCount()) // productAdapter.getCount()记录的是数据的长度
				{  
                    //Log.i(TAG, "onScrollStateChanged");  
                    //加载数据代码，此处省略了  
					page++;
					//设置刷新方式
					if(!reffinish)
					{
						initData();
					}
					hongbaoHistoryListView.setSelection(lastItemIndex-1);
					//Toast.makeText(ProductListActivity.this, "加载中",  Toast.LENGTH_SHORT).show();
                }  
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) //totalItemCount记录的是整个listView的长度
			{
				// TODO Auto-generated method stub
				lastItemIndex = firstVisibleItem + visibleItemCount -1; 
			}
		});
		hongbaoHistoryListView.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShopHongbaoHistoryListActivity.this,ShopHongbaoHistoryDetailActivity.class);
				intent.putExtra("hongbao", hongbaoHistoryAdapter.getItem(position));
				startActivity(intent);
			}
		});
	}
	private void initData() 
	{
		// TODO Auto-generated method stub
		loading_progressBar.setVisibility(View.VISIBLE);
		loading_text.setText("加载中...");
		RequestParams params = new RequestParams();
		params.add("token", ShopHongbaoHistoryListActivity.this.share.getString("token", ""));
		params.add("page",String.valueOf(page));
		params.add("pagesize","10");
		params.add("id",hongbaoId);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(ShopHongbaoHistoryListActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.post(ConstantParamPhone.IP+ConstantParamPhone.GET_HONGBAOHIST_LIST, params,new TextHttpResponseHandler()
		{

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				//Toast.makeText(getActivity(), "网络不给力呀", Toast.LENGTH_SHORT).show();
				if(page==1)
				{
					reloadButton.setVisibility(View.VISIBLE);
				}
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				GetHongBaoHistBean getHongBaoHistBean=new Gson().fromJson(arg2,GetHongBaoHistBean.class);
				if(!getHongBaoHistBean.getStatus().equals("0"))
				{
//					MyDialog.showDialog(HongbaoHistoryListActivity.this, "该账号已在其他手机登录，是否重新登录", false, true, "取消", "确定",new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							MyDialog.cancel();
//						}
//					}, new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							Intent intent=new Intent(HongbaoHistoryListActivity.this,LoginActivity.class);
//							startActivity(intent);
//						}
//					});
					//Toast.makeText(getActivity(), "啥也没有！", Toast.LENGTH_SHORT).show();
					loading_progressBar.setVisibility(View.GONE);
					loading_text.setText("啥也没有！");
					reloadButton.setVisibility(View.VISIBLE);
				}
				else
				{
					if(getHongBaoHistBean.getData()!=null&&getHongBaoHistBean.getData().size()!=0)
					{
						hongbaoHistoryAdapter.addAll(getHongBaoHistBean.getData());
						reloadButton.setVisibility(View.GONE);
						if(page==1&&getHongBaoHistBean.getData().size()<10)
						{
							loading_progressBar.setVisibility(View.GONE);
							loading_text.setText("加载完啦！");
						}
						
					}
					else
					{
						if(page==1)
						{
							//Toast.makeText(getActivity(), "啥也没有！", Toast.LENGTH_SHORT).show();
							loading_progressBar.setVisibility(View.GONE);
							loading_text.setText("啥也没有！");
							reloadButton.setVisibility(View.VISIBLE);
						}
						else
						{
							Toast.makeText(ShopHongbaoHistoryListActivity.this, "加载完啦", Toast.LENGTH_SHORT).show();
							loading_progressBar.setVisibility(View.GONE);
							loading_text.setText("加载完啦！");
							reffinish=true;
						}
					}
				}
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				hongbaoHistoryswipeLayout.setRefreshing(false);
				ColorDialog.dissmissProcessDialog();
			}
		});
	}
}