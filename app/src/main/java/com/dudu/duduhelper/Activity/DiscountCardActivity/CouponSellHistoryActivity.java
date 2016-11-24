package com.dudu.duduhelper.Activity.DiscountCardActivity;

import org.apache.http.Header;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Activity.WelcomeActivity.LoginActivity;
import com.dudu.duduhelper.Activity.OrderActivity.SearchActivity;
import com.dudu.duduhelper.adapter.CouponSellHistoryAdapter;
import com.dudu.duduhelper.bean.GetCouponSellBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import com.dudu.duduhelper.R;
public class CouponSellHistoryActivity extends BaseActivity 
{
	private SwipeRefreshLayout couponSellHistorySwipeLayout;
	private ListView couponSellHistoryListView;
	private Button reloadButton;
	private View footView;
    private ProgressBar loading_progressBar;
    private TextView loading_text;
    private int page=1;
    private int lastItemIndex;
    private boolean reffinish=false;
    private CouponSellHistoryAdapter getCashAdapter;
    private String id;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon_sell_history);
		initHeadView("领取记录", true, true, R.drawable.icon_mysearch);
		initView();
		ColorDialog.showRoundProcessDialog(CouponSellHistoryActivity.this,R.layout.loading_process_dialog_color);
		initData();
	}

	@SuppressLint("ResourceAsColor") 
	private void initView() 
	{
		// TODO Auto-generated method stub
		selectClickButton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(CouponSellHistoryActivity.this,SearchActivity.class);
				intent.putExtra("id", id);
				startActivity(intent);
			}
		});
		id=getIntent().getStringExtra("id");
		couponSellHistorySwipeLayout=(SwipeRefreshLayout) this.findViewById(R.id.CouponSellHistorySwipeLayout);
		couponSellHistoryListView=(ListView) this.findViewById(R.id.CouponSellHistoryListView);
		reloadButton=(Button) this.findViewById(R.id.reloadButton);
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
				initData();
			}
		});
		getCashAdapter=new CouponSellHistoryAdapter(this);
		couponSellHistorySwipeLayout=(SwipeRefreshLayout) this.findViewById(R.id.CouponSellHistorySwipeLayout);
		couponSellHistorySwipeLayout.setColorSchemeResources(R.color.text_color);
		couponSellHistorySwipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		couponSellHistorySwipeLayout.setProgressBackgroundColor(R.color.bg_color);
		couponSellHistoryListView=(ListView) this.findViewById(R.id.CouponSellHistoryListView);
		couponSellHistoryListView.setAdapter(getCashAdapter);
		couponSellHistoryListView.addFooterView(footView,null,false);
		couponSellHistoryListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(CouponSellHistoryActivity.this,CouponSellDetailActivity.class);
				intent.putExtra("id", getCashAdapter.getItem(arg2).getId());
				startActivity(intent);
			}
		});
		//下拉刷新事件
		couponSellHistorySwipeLayout.setOnRefreshListener(new OnRefreshListener() 
		{
			
			@Override
			public void onRefresh() 
			{
				// TODO Auto-generated method stub
				getCashAdapter.clear();
				initData();
			}
		});
	}

	private void initData() 
	{
		// TODO Auto-generated method stub
		loading_progressBar.setVisibility(View.VISIBLE);
		loading_text.setText("加载中...");
		RequestParams params = new RequestParams();
		params.add("token", this.share.getString("token", ""));
//		params.add("page",String.valueOf(page));
//		params.add("pagesize","10");
		params.add("id",id);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(CouponSellHistoryActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.GET_COUPON_RECORD_LIST, params,new TextHttpResponseHandler()
		{

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				reloadButton.setVisibility(View.VISIBLE);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				GetCouponSellBean getCouponSellBean=new Gson().fromJson(arg2,GetCouponSellBean.class);
				if(!getCouponSellBean.getStatus().equals("1"))
				{
					MyDialog.showDialog(CouponSellHistoryActivity.this, "该账号已在其他手机登录，是否重新登录", false, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(CouponSellHistoryActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				else
				{
					if(getCouponSellBean.getData()!=null&&getCouponSellBean.getData().size()!=0)
					{
						getCashAdapter.addAll(getCouponSellBean.getData());
						reloadButton.setVisibility(View.GONE);
						loading_progressBar.setVisibility(View.GONE);
						loading_text.setText("加载完啦！");
						
					}
					else
					{
							loading_progressBar.setVisibility(View.GONE);
							loading_text.setText("啥也没有！");
							reloadButton.setVisibility(View.VISIBLE);
					}
				}
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				couponSellHistorySwipeLayout.setRefreshing(false);
				ColorDialog.dissmissProcessDialog();
			}
		});
	}
}
