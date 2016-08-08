package com.dudu.duduhelper;

import org.apache.http.Header;

import com.dudu.duduhelper.adapter.CouponSellHistoryAdapter;
import com.dudu.duduhelper.adapter.ShopOrderAdapter;
import com.dudu.duduhelper.bean.GetCouponSellBean;
import com.dudu.duduhelper.bean.OrderBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;

public class SearchActivity extends BaseActivity 
{
	private ListView allOrderListView;
	private ShopOrderAdapter orderAdapter;
	private CouponSellHistoryAdapter getCashAdapter;
	//private int page=1;
	private SwipeRefreshLayout orderallswipeLayout;
	private Button reloadButton;
	private int lastItemIndex;
    //private View loadMoreView; 
    //判断是上拉还是下拉
    private int reftype;
    //判断数据是否加载完成
    private boolean reffinish=false;
    private View footView;
    private ProgressBar loading_progressBar;
    private TextView loading_text;
    private String keyword;
    private EditText searchBarEdit;
    private ImageButton backButton;
    private Button selectClickButton;
    private String id="";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		keyword=getIntent().getStringExtra("keyword");
		id=getIntent().getStringExtra("id");
		initView();
		ColorDialog.showRoundProcessDialog(this,R.layout.loading_process_dialog_color);
		if(TextUtils.isEmpty(id))
		{
			orderAdapter=new ShopOrderAdapter(this);
			initData();
		}
		else
		{
			getCashAdapter=new CouponSellHistoryAdapter(this);
			allOrderListView.setAdapter(getCashAdapter);
			initData1();
		}
		
	}
	private void initData1() 
	{
		// TODO Auto-generated method stub
		loading_progressBar.setVisibility(View.VISIBLE);
		loading_text.setText("加载中...");
		RequestParams params = new RequestParams();
		params.add("token", this.share.getString("token", ""));
		params.add("id",id);
		params.add("sn",keyword);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(SearchActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.COUPON_RECORD_SEARCH, params,new TextHttpResponseHandler()
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
					MyDialog.showDialog(SearchActivity.this, "该账号已在其他手机登录，是否重新登录", false, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(SearchActivity.this,LoginActivity.class);
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
				orderallswipeLayout.setRefreshing(false);
				ColorDialog.dissmissProcessDialog();
			}
		});
	}
	private void initData() 
	{
		loading_progressBar.setVisibility(View.VISIBLE);
		loading_text.setText("加载中...");
		// TODO Auto-generated method stub
		allOrderListView.setAdapter(orderAdapter);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("no", keyword);
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(SearchActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.ORDER_SEARCH, params,new TextHttpResponseHandler()
		{

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(SearchActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
				reloadButton.setVisibility(View.VISIBLE);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				OrderBean orderBean=new Gson().fromJson(arg2,OrderBean.class);
				if(!orderBean.getStatus().equals("1"))
				{
					MyDialog.showDialog(SearchActivity.this, "该账号已在其他手机登录，是否重新登录", false, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(SearchActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				else
				{
					if(orderBean.getData()!=null&&orderBean.getData().size()!=0)
					{
						orderAdapter.addAll(orderBean.getData());
						reloadButton.setVisibility(View.GONE);
						loading_progressBar.setVisibility(View.GONE);
						loading_text.setText("加载完啦！");
						
					}
					else
					{
						//Toast.makeText(getActivity(), "啥也没有！", Toast.LENGTH_SHORT).show();
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
				orderallswipeLayout.setRefreshing(false);
				ColorDialog.dissmissProcessDialog();
			}
		});
	}

	@SuppressLint("ResourceAsColor") 
	private void initView() 
	{
		// TODO Auto-generated method stub
		backButton=(ImageButton) this.findViewById(R.id.backButton);
		searchBarEdit=(EditText) this.findViewById(R.id.searchBarEdit);
		selectClickButton=(Button) this.findViewById(R.id.selectClickButton);
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		if(!TextUtils.isEmpty(keyword))
		{
			searchBarEdit.setText(keyword);
		}
		selectClickButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				keyword=searchBarEdit.getText().toString().trim();
				if(TextUtils.isEmpty(id))
				{
					orderAdapter.clear();
					initData();
				}
				else
				{
					getCashAdapter.clear();
					initData1();
				}
			}
		});
		searchBarEdit.setOnEditorActionListener(new OnEditorActionListener() 
		{

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) 
			{
				// TODO Auto-generated method stub
				if(arg1 ==EditorInfo.IME_ACTION_SEARCH)
				{
					keyword=searchBarEdit.getText().toString().trim();
					if(TextUtils.isEmpty(id))
					{
						orderAdapter.clear();
						initData();
					}
					else
					{
						getCashAdapter.clear();
						initData1();
					}
	               return true;
	            }
				return false;
			}
		});
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
				ColorDialog.showRoundProcessDialog(SearchActivity.this,R.layout.loading_process_dialog_color);
				if(TextUtils.isEmpty(id))
				{
					orderAdapter.clear();
					initData();
				}
				else
				{
					getCashAdapter.clear();
					initData1();
				}
			}
		});
		allOrderListView=(ListView) this.findViewById(R.id.allOrderListView);
		allOrderListView.addFooterView(footView,null,false);
		allOrderListView.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long arg3) 
			{
				if(TextUtils.isEmpty(id))
				{
					// TODO Auto-generated method stub
					Intent intent=new Intent(SearchActivity.this, ShopOrderDetailActivity.class);
					intent.putExtra("no", orderAdapter.getItem(position).getNo());
					intent.putExtra("status", orderAdapter.getItem(position).getStatus());
					startActivityForResult(intent, 1);
				}
				else
				{
					Intent intent=new Intent(SearchActivity.this,CouponSellDetailActivity.class);
					intent.putExtra("id", getCashAdapter.getItem(position).getId());
					startActivity(intent);
				}
			}
		});
		orderallswipeLayout = (SwipeRefreshLayout)this.findViewById(R.id.orderallswipeLayout);
		orderallswipeLayout.setColorSchemeResources(R.color.text_color);
		orderallswipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		orderallswipeLayout.setProgressBackgroundColor(R.color.bg_color);
		orderallswipeLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() 
			{
				// TODO Auto-generated method stub
				reftype=1;
				if(TextUtils.isEmpty(id))
				{
					orderAdapter.clear();
					initData();
				}
				else
				{
					getCashAdapter.clear();
					initData1();
				}
			}
		});
	}

}
