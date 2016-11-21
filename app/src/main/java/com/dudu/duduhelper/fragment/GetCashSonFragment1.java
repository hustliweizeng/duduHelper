package com.dudu.duduhelper.fragment;

import org.apache.http.Header;

import com.dudu.duduhelper.Activity.CashHistoryActivity.GetInComeActivity;
import com.dudu.duduhelper.Activity.MainActivity;
import com.dudu.duduhelper.Activity.WelcomeActivity.LoginActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.adapter.GetCashAdapter;
import com.dudu.duduhelper.bean.GetCashBean;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class GetCashSonFragment1 extends Fragment 
{
	private String type;
	private View GetCashSonFragmentView;
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
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		GetCashSonFragmentView= inflater.inflate(R.layout.fragment_getcashson1_layout, null);
		return GetCashSonFragmentView;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initFragmentView();
		//ColorDialog.showRoundProcessDialog(getActivity(),R.layout.loading_process_dialog_color);
		initData();
	}
	@SuppressLint("ResourceAsColor") 
	private void initFragmentView() 
	{
		// TODO Auto-generated method stub
		footView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_listview_foot, null);
		loading_progressBar=(ProgressBar) footView.findViewById(R.id.loading_progressBar);
		loading_text=(TextView) footView.findViewById(R.id.loading_text);
		// TODO Auto-generated method stub
		reloadButton=(Button) GetCashSonFragmentView.findViewById(R.id.reloadButton1);
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
		
		getCashAdapter=new GetCashAdapter(getActivity());
		getCashSwipeLayout=(SwipeRefreshLayout) getActivity().findViewById(R.id.getCashSwipeLayout1);
		getCashSwipeLayout.setColorSchemeResources(R.color.text_color);
		getCashSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		getCashSwipeLayout.setProgressBackgroundColor(R.color.bg_color);
		getCashListView=(ListView) getActivity().findViewById(R.id.getCashListView1);
		getCashListView.setAdapter(getCashAdapter);
		getCashListView.addFooterView(footView,null,false);
		getCashListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),GetInComeActivity.class);
				//intent.putExtra("orderno", getCashAdapter.getItem(arg2).getNo());
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
						//
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
		params.add("token", ((MainActivity)getActivity()).share.getString("token", ""));
		params.add("page",String.valueOf(page));
		params.add("pagesize","10");
		params.add("status",type);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(getActivity());    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.GET_CASH_LIST, params,new TextHttpResponseHandler()
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
				GetCashBean getCashBean=new Gson().fromJson(arg2,GetCashBean.class);
				if(!getCashBean.getStatus().equals("1"))
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
				getCashSwipeLayout.setRefreshing(false);
				ColorDialog.dissmissProcessDialog();
			}
		});
	}
}
