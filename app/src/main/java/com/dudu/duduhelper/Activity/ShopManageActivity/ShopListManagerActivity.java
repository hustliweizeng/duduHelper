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
import com.dudu.duduhelper.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

public class ShopListManagerActivity extends BaseActivity 
{
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
    private ShopAdapterAdapter hongbaoHistoryAdapter;
    private String hongbaoId;


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
		startActivity(intent);
	}
	
	@SuppressLint("ResourceAsColor") 
	private void initView() 
	{
		//listview添加脚布局
		hongbaoId="90";
		footView = LayoutInflater.from(this).inflate(R.layout.activity_listview_foot, null);
		loading_progressBar=(ProgressBar) footView.findViewById(R.id.loading_progressBar);
		loading_text=(TextView) footView.findViewById(R.id.loading_text);

		//点击重试按钮
		reloadButton=(Button) this.findViewById(R.id.reloadButton);
		reloadButton.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v)
			{
				ColorDialog.showRoundProcessDialog(ShopListManagerActivity.this,R.layout.loading_process_dialog_color);
				initData();
			}
		});
		//listview设置
		hongbaoHistoryAdapter=new ShopAdapterAdapter(this);
		hongbaoHistoryListView=(ListView) this.findViewById(R.id.hongbaoHistoryListView);
		hongbaoHistoryListView.setAdapter(hongbaoHistoryAdapter);
		hongbaoHistoryListView.addFooterView(footView,null,false);
		//设置下拉更新，上啦加载
		hongbaoHistoryswipeLayout = (SwipeRefreshLayout)this.findViewById(R.id.hongbaoHistoryswipeLayout);
		hongbaoHistoryswipeLayout.setColorSchemeResources(R.color.text_color);
		hongbaoHistoryswipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		hongbaoHistoryswipeLayout.setProgressBackgroundColor(R.color.bg_color);
		hongbaoHistoryswipeLayout.setOnRefreshListener(new OnRefreshListener() 
		{
			
			@Override
			public void onRefresh() 
			{
				//下拉刷新的时候更新数据
				page=1;
				hongbaoHistoryAdapter.clear();
				initData();
			}
		});
		//listview的滚动监听
		hongbaoHistoryListView.setOnScrollListener(new OnScrollListener() 
		{
			@Override
			//如果滚动到最后一行，请求网络加载数据
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE&&lastItemIndex == hongbaoHistoryAdapter.getCount()) // productAdapter.getCount()记录的是数据的长度
				{  
					//请求第二页的数据
					page++;
					//设置刷新方式
					if(!reffinish)
					{
						initData();
					}
					//把条目定位到最后一个
					hongbaoHistoryListView.setSelection(lastItemIndex-1);
                }
			}
			
			@Override
			//滚动中的监听，设置最后一个条目
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) //totalItemCount记录的是整个listView的长度
			{
				lastItemIndex = firstVisibleItem + visibleItemCount -1;
			}
		});
		//条目点击监听
		hongbaoHistoryListView.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				Intent intent = new Intent(ShopListManagerActivity.this,ShopAddActivity.class);
				intent.putExtra("hongbao", hongbaoHistoryAdapter.getItem(position));
				intent.putExtra("source","mendian");
				startActivity(intent);
			}
		});
	}
	//获取数据
	private void initData() 
	{
		//显示view
		loading_progressBar.setVisibility(View.VISIBLE);
		loading_text.setText("加载中...");

		RequestParams params = new RequestParams();
		params.add("token", ShopListManagerActivity.this.share.getString("token", ""));
		params.add("page",String.valueOf(page));
		params.add("pagesize","10");
		params.add("id",hongbaoId);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(ShopListManagerActivity.this);    
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
							Toast.makeText(ShopListManagerActivity.this, "加载完啦", Toast.LENGTH_SHORT).show();
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
