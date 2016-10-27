package com.dudu.helper3.Activity.OrderActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.Activity.DiscountCardActivity.CouponSellDetailActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.adapter.CouponSellHistoryAdapter;
import com.dudu.helper3.adapter.ShopOrderAdapter;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.OrderListBean;
import com.dudu.helper3.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

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
		orderAdapter = new ShopOrderAdapter(this);
		//keyword=getIntent().getStringExtra("keyword");
		initView();
		
	}
	
	private void initData() 
	{
		loading_progressBar.setVisibility(View.VISIBLE);
		loading_text.setText("加载中...");
		
		RequestParams params = new RequestParams();
		params.add("id",keyword);
        HttpUtils.getConnection(context,params,ConstantParamPhone.SEARCH_ORDER, "get",new TextHttpResponseHandler()
		{
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(SearchActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
				reloadButton.setVisibility(View.VISIBLE);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) {

				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						OrderListBean data = new Gson().fromJson(arg2, OrderListBean.class);
						orderAdapter.addAll(data.getList());

					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				loading_progressBar.setVisibility(View.GONE);
				allOrderListView.setAdapter(orderAdapter);
			}
		});
	}

	@SuppressLint("ResourceAsColor") 
	private void initView() 
	{
		backButton=(ImageButton) this.findViewById(R.id.backButton);
		searchBarEdit=(EditText) this.findViewById(R.id.searchBarEdit);
		selectClickButton=(Button) this.findViewById(R.id.selectClickButton);
		//返回键
		backButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		if(!TextUtils.isEmpty(keyword))
		{
			searchBarEdit.setText(keyword);
		}
		//搜索键
		selectClickButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				keyword=searchBarEdit.getText().toString().trim();
				if (TextUtils.isEmpty(keyword)){
					Toast.makeText(context,"输入不能为空",Toast.LENGTH_SHORT).show();
				}else {
					orderAdapter.clear();
					initData();
					InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(searchBarEdit.getWindowToken(),0);
				}
			}
		});
		//搜索框监听
		searchBarEdit.setOnEditorActionListener(new OnEditorActionListener() 
		{

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) 
			{
				// TODO Auto-generated method stub
				if(arg1 ==EditorInfo.IME_ACTION_SEARCH)
				{
					keyword=searchBarEdit.getText().toString().trim();
					orderAdapter.clear();
					initData();
					//隐藏软键盘
					InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(searchBarEdit.getWindowToken(),0);
	               return true;
	            }
				return false;
			}
		});
		footView = LayoutInflater.from(this).inflate(R.layout.activity_listview_foot, null);
		loading_progressBar=(ProgressBar) footView.findViewById(R.id.loading_progressBar);
		loading_text=(TextView) footView.findViewById(R.id.loading_text);
		reloadButton=(Button) this.findViewById(R.id.reloadButton);
		//数据重载按钮
		reloadButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				ColorDialog.showRoundProcessDialog(SearchActivity.this,R.layout.loading_process_dialog_color);
				orderAdapter.clear();
				initData();
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
					Intent intent=new Intent(SearchActivity.this, ShopOrderDetailActivity.class);
					intent.putExtra("info", orderAdapter.getInfo().get(position));
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
				reftype=1;
				orderAdapter.clear();
				initData();
				ColorDialog.dissmissProcessDialog();
			}
		});
	}

}
