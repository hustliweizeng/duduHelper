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
    private boolean reffinish=false;
    private View footView;
    private ProgressBar loading_progressBar;
    private TextView loading_text;
    private Button reloadButton;
    private int page=1;
    private int lastItemIndex;
    private ShopAdapterAdapter adapter;


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
		adapter=new ShopAdapterAdapter(this);
		lv_shop_list=(ListView) this.findViewById(R.id.lv_shop_list);
		lv_shop_list.setAdapter(adapter);
		lv_shop_list.addFooterView(footView,null,false);
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
				page=1;
				adapter.clear();
				initData();
			}
		});
		//listview的滚动监听
		lv_shop_list.setOnScrollListener(new OnScrollListener() 
		{
			@Override
			//如果滚动到最后一行，请求网络加载数据
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE&&lastItemIndex == adapter.getCount()) // productAdapter.getCount()记录的是数据的长度
				{  
					//请求第二页的数据
					page++;
					//设置刷新方式
					if(!reffinish)
					{
						initData();
					}
					//把条目定位到最后一个
					lv_shop_list.setSelection(lastItemIndex-1);
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
		lv_shop_list.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				Intent intent = new Intent(ShopListManagerActivity.this,ShopAddActivity.class);
				//intent.putExtra("hongbao", adapter.getItem(position));
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
						ShopListBean data = new Gson().fromJson(arg2, ShopListBean.class);
						adapter.addAll(data.getData());
						


					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
						reloadButton.setVisibility(View.VISIBLE);
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
}
