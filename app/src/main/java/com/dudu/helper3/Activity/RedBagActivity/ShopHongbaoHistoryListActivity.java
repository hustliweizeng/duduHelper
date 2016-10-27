package com.dudu.helper3.Activity.RedBagActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.adapter.RedBagHistoryAdapter;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.RedBagHitsoryBean;
import com.dudu.helper3.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopHongbaoHistoryListActivity extends BaseActivity {
	private ListView hongbaoHistoryListView;
	private SwipeRefreshLayout hongbaoHistoryswipeLayout;
	//判断数据是否加载完成
	private View footView;
	private ProgressBar loading_progressBar;
	private TextView loading_text;
	private RedBagHistoryAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_hongbao_history_list_layout);
		initHeadView("领取记录", true, false, 0);
		initView();
		initData();
	}

	@SuppressLint("ResourceAsColor")
	private void initView()
	{
		// TODO Auto-generated method stub
		footView = LayoutInflater.from(this).inflate(R.layout.activity_listview_foot, null);
		loading_progressBar=(ProgressBar) footView.findViewById(R.id.loading_progressBar);
		loading_text=(TextView) footView.findViewById(R.id.loading_text);
		adapter=new RedBagHistoryAdapter(context);
		hongbaoHistoryListView=(ListView) this.findViewById(R.id.hongbaoHistoryListView);
		hongbaoHistoryListView.setAdapter(adapter);
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
				hongbaoHistoryswipeLayout.setRefreshing(true);
				initData();
			}
		});

		hongbaoHistoryListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent(ShopHongbaoHistoryListActivity.this,ShopHongbaoHistoryDetailActivity.class);
				//intent.putExtra("hongbao", adapter.getItem(position));
				startActivity(intent);
			}
		});
	}
	private void initData()
	{
		String id = getIntent().getStringExtra("id");
		loading_progressBar.setVisibility(View.VISIBLE);
		loading_text.setVisibility(View.VISIBLE);
		loading_text.setText("加载中...");
		HttpUtils.getConnection(context,null, ConstantParamPhone.REDBAG_HISTORY+id+"/logs", "get",new TextHttpResponseHandler()
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
						RedBagHitsoryBean bean = new Gson().fromJson(arg2, RedBagHitsoryBean.class);
						if (bean.getData()!=null && bean.getData().size()>0){
							//添加数据之前清空
							adapter.list.clear();
							adapter.addAll(bean.getData());
							//显示正常时，隐藏提示
							footView.setVisibility(View.GONE);
						}else {
							//没有数据
							loading_text.setVisibility(View.VISIBLE);
							loading_text.setText("当前没有要显示的数据");
							loading_progressBar.setVisibility(View.GONE);
						}

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
			public void onFinish()
			{
				// TODO Auto-generated method stub
				hongbaoHistoryswipeLayout.setRefreshing(false);
				ColorDialog.dissmissProcessDialog();
				
			}
		});
	}
}