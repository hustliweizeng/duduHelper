package com.dudu.duduhelper.fragment;

import java.util.List;

import org.apache.http.Header;

import com.dudu.duduhelper.LoginActivity;
import com.dudu.duduhelper.MainActivity;
import com.dudu.duduhelper.ShopOrderDetailActivity;
import com.dudu.duduhelper.shopProductListActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.adapter.ShopOrderAdapter;
import com.dudu.duduhelper.bean.OrderBean;
import com.dudu.duduhelper.bean.OrderDataBean;
import com.dudu.duduhelper.bean.ProductBean;
import com.dudu.duduhelper.bean.ProductListBean;
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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
public class AllOrderFragment extends Fragment 
{
	private View AllOrderFragmentView;
	private ListView allOrderListView;
	private ShopOrderAdapter orderAdapter;
	private int page=1;
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
    
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		AllOrderFragmentView = inflater.inflate(R.layout.fragment_order_all, null);
		return AllOrderFragmentView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		orderAdapter=new ShopOrderAdapter(getActivity());
		initFragmentView();
		//ColorDialog.showRoundProcessDialog(getActivity(),R.layout.loading_process_dialog_color);
		//initData();
	}
	@Override
	public void onResume() 
	{
		// TODO Auto-generated method stub
		super.onResume();
		page=1;
		orderAdapter.clear();
		ColorDialog.showRoundProcessDialog(getActivity(),R.layout.loading_process_dialog_color);
		initData();
	}
	private void initData() 
	{
		loading_progressBar.setVisibility(View.VISIBLE);
		loading_text.setText("加载中...");
		
		// TODO Auto-generated method stub
		allOrderListView.setAdapter(orderAdapter);
		RequestParams params = new RequestParams();
		params.add("token", ((MainActivity)getActivity()).share.getString("token", ""));
		params.add("page",String.valueOf(page));
		params.add("pagesize","10");
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(getActivity());    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.GET_ALLORDER_LIST, params,new TextHttpResponseHandler()
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
				OrderBean orderBean=new Gson().fromJson(arg2,OrderBean.class);
				if(!orderBean.getStatus().equals("1"))
				{
					MyDialog.showDialog(getActivity(), "该账号已在其他手机登录，是否重新登录", false, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(getActivity(),LoginActivity.class);
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
						
						if(page==1&&orderBean.getData().size()<10)
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
							Toast.makeText(getActivity(), "加载完啦", Toast.LENGTH_SHORT).show();
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
				orderallswipeLayout.setRefreshing(false);
				ColorDialog.dissmissProcessDialog();
			}
		});
	}

	@SuppressLint("ResourceAsColor") 
	private void initFragmentView() 
	{
		
		footView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_listview_foot, null);
		loading_progressBar=(ProgressBar) footView.findViewById(R.id.loading_progressBar);
		loading_text=(TextView) footView.findViewById(R.id.loading_text);
		
		// TODO Auto-generated method stub
		reloadButton=(Button) AllOrderFragmentView.findViewById(R.id.reloadButton);
		//数据重载按钮
		reloadButton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				ColorDialog.showRoundProcessDialog(getActivity(),R.layout.loading_process_dialog_color);
				initData();
			}
		});
		allOrderListView=(ListView) AllOrderFragmentView.findViewById(R.id.allOrderListView);
		allOrderListView.addFooterView(footView,null,false);
		allOrderListView.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(), ShopOrderDetailActivity.class);
				intent.putExtra("no", orderAdapter.getItem(position).getNo());
				intent.putExtra("status", orderAdapter.getItem(position).getStatus());
				startActivityForResult(intent, 1);
			}
		});
		allOrderListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) 
			{
				// TODO Auto-generated method stub
				//
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE&&lastItemIndex == orderAdapter.getCount()) 
				{  
                    //Log.i(TAG, "onScrollStateChanged");  
                    //加载数据代码，此处省略了  
					page++;
					//设置刷新方式
					reftype=2;
					if(!reffinish)
					{
						initData();
					}
					allOrderListView.setSelection(lastItemIndex-1);
					//Toast.makeText(ProductListActivity.this, "加载中",  Toast.LENGTH_SHORT).show();
                }  
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) 
			{
				// TODO Auto-generated method stub
				lastItemIndex = firstVisibleItem + visibleItemCount -1; 
			}
		});
		orderallswipeLayout = (SwipeRefreshLayout)AllOrderFragmentView.findViewById(R.id.orderallswipeLayout);
		orderallswipeLayout.setColorSchemeResources(R.color.text_color);
		orderallswipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		orderallswipeLayout.setProgressBackgroundColor(R.color.bg_color);
		orderallswipeLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() 
			{
				// TODO Auto-generated method stub
				page=1;
				reftype=1;
				orderAdapter.clear();
				initData();
			}
		});

	}
//	@Override
//	public void onActivityResult(int requestCode, int resultCode, Intent data) 
//	{
//		// TODO Auto-generated method stub
//		super.onActivityResult(requestCode, resultCode, data); 
//		page=1;
//		orderAdapter.clear();
//		initData();
//	}
	

}
