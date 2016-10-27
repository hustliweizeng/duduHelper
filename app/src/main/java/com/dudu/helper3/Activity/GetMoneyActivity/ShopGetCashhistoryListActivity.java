package com.dudu.helper3.Activity.GetMoneyActivity;

import org.apache.http.Header;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.Activity.CashHistoryActivity.GetInComeActivity;
import com.dudu.helper3.Activity.WelcomeActivity.LoginActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.adapter.GetCashAdapter;
import com.dudu.helper3.bean.GetCashBean;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.widget.ColorDialog;
import com.dudu.helper3.widget.MyDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;

public class ShopGetCashhistoryListActivity extends BaseActivity 
{
	
	private String type;
	private SwipeRefreshLayout getCashSwipeLayout;
	private ListView getCashListView;
	private GetCashAdapter getCashAdapter;
	
	//判断数据是否加载完成
    private boolean reffinish=false;
    private View footView;
    private ProgressBar loading_progressBar;
    private TextView loading_text;
    private Button reloadButton;
    private int page=1;
    private int lastItemIndex;
	//获取加载数据类型
    @Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_getcashson_layout);
		initHeadView("收款记录", true, false, R.drawable.icon_historical);
		type = "0";
		initFragmentView();
		initData();
	}
	@SuppressLint("ResourceAsColor") 
	private void initFragmentView() 
	{
		// TODO Auto-generated method stub
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
				ColorDialog.showRoundProcessDialog(ShopGetCashhistoryListActivity.this,R.layout.loading_process_dialog_color);
				initData();
			}
		});
		
		getCashAdapter=new GetCashAdapter(this);
		getCashSwipeLayout=(SwipeRefreshLayout) this.findViewById(R.id.getCashSwipeLayout);
		getCashSwipeLayout.setColorSchemeResources(R.color.text_color);
		getCashSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		getCashSwipeLayout.setProgressBackgroundColor(R.color.bg_color);
		getCashListView=(ListView) this.findViewById(R.id.getCashListView);
		getCashListView.setAdapter(getCashAdapter);
		getCashListView.addFooterView(footView,null,false);
		getCashListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(ShopGetCashhistoryListActivity.this,GetInComeActivity.class);
				intent.putExtra("no", getCashAdapter.getItem(arg2).getNo());
				startActivity(intent);
			}
		});
		//下拉刷新事件
		getCashSwipeLayout.setOnRefreshListener(new OnRefreshListener() 
		{
			
			@Override
			public void onRefresh() 
			{
				// TODO Auto-generated method stub
				page=1;
				getCashAdapter.clear();
				initData();
			}
		});
		getCashListView.setOnScrollListener(new OnScrollListener() 
		{
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) 
			{
				// TODO Auto-generated method stub
				//
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE&&lastItemIndex == getCashAdapter.getCount()) // productAdapter.getCount()记录的是数据的长度
				{  
                    //Log.i(TAG, "onScrollStateChanged");  
                    //加载数据代码，此处省略了  
					page++;
					//设置刷新方式
					if(!reffinish)
					{
						initData();
					}
					getCashListView.setSelection(lastItemIndex-1);
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
	}
	private void initData() 
	{
		// TODO Auto-generated method stub
		loading_progressBar.setVisibility(View.VISIBLE);
		loading_text.setText("加载中...");
		RequestParams params = new RequestParams();
		params.add("token",this.share.getString("token", ""));
		params.add("page",String.valueOf(page));
		params.add("pagesize","10");
		params.add("status",type);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.GET_CASH_LIST, params,new TextHttpResponseHandler()
		{

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				//Toast.makeText(this, "网络不给力呀", Toast.LENGTH_SHORT).show();
				if(page==1)
				{
					reloadButton.setVisibility(View.VISIBLE);
				}
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				GetCashBean getCashBean=new Gson().fromJson(arg2,GetCashBean.class);
				if(!getCashBean.getStatus().equals("1"))
				{
					MyDialog.showDialog(ShopGetCashhistoryListActivity.this, "该账号已在其他手机登录，是否重新登录", false, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(ShopGetCashhistoryListActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				else
				{
					if(getCashBean.getData()!=null&&getCashBean.getData().size()!=0)
					{
						getCashAdapter.addAll(getCashBean.getData());
						reloadButton.setVisibility(View.GONE);
						if(page==1&&getCashBean.getData().size()<10)
						{
							loading_progressBar.setVisibility(View.GONE);
							loading_text.setText("加载完啦！");
						}
						
					}
					else
					{
						if(page==1)
						{
							//Toast.makeText(this, "啥也没有！", Toast.LENGTH_SHORT).show();
							loading_progressBar.setVisibility(View.GONE);
							loading_text.setText("啥也没有！");
							reloadButton.setVisibility(View.VISIBLE);
						}
						else
						{
							Toast.makeText(ShopGetCashhistoryListActivity.this, "加载完啦", Toast.LENGTH_SHORT).show();
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
				getCashSwipeLayout.setRefreshing(false);
				ColorDialog.dissmissProcessDialog();
			}
		});
	}

}
