package com.dudu.duduhelper.Activity.SummaryActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.adapter.ModuelSummoryAdapter;
import com.dudu.duduhelper.adapter.SummoryModuleAdapter;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.Modules;
import com.dudu.duduhelper.javabean.SummoryDetaiBean;
import com.dudu.duduhelper.widget.WheelIndicatorItem;
import com.dudu.duduhelper.widget.WheelIndicatorTongjiView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.jar.Attributes;

public class ShopAccountWatchActivity extends BaseActivity 
{
	private WheelIndicatorTongjiView wheelIndicatorView;
	private TextView tv_start;
	private TextView tv_end;
	private TextView tv_rangeday;
	private TextView tv_total_trade;
	private TextView tv_total_income;
	private TextView fangkeNumText;
	private TextView buyerNumText;
	private TextView orderNumText;
	private SummoryModuleAdapter adapter;
	private float totalIncome;
	private float totalTrade;
	private ListView lv_datas;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_account_watch);
		initHeadView("自定义统计", true, false, 0);
		adapter = new SummoryModuleAdapter(context);
		initView();
		initData();
		
	}

	private void initData() {
		RequestParams params = new RequestParams();
		params.put("start",getIntent().getStringExtra("start"));
		params.put("end",getIntent().getStringExtra("end"));
		tv_start.setText(getIntent().getStringExtra("start"));
		tv_end.setText(getIntent().getStringExtra("end"));
		
		LogUtil.d("start",getIntent().getStringExtra("start"));
		LogUtil.d("end",getIntent().getStringExtra("end"));
		HttpUtils.getConnection(context, params, ConstantParamPhone.GET_SUM, "get", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				LogUtil.d("res",s);
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						SummoryDetaiBean data = new Gson().fromJson(s, SummoryDetaiBean.class);
						tv_rangeday.setText(data.getDaynum()+"天");
						tv_total_income.setText(data.getIncome());
						tv_total_trade.setText(data.getTrade());
						fangkeNumText.setText(data.getVisiter());
						buyerNumText.setText(data.getBuyer());
						orderNumText.setText(data.getOrder());
						//总交易额
						totalIncome = Float.parseFloat(data.getIncome());
						totalTrade = Float.parseFloat(data.getTrade());
						setCricle();

						//获取列表信息
						List<SummoryDetaiBean.TradeModulesBean> trade_modules = data.getTrade_modules();
						//把数据放到适配器
						adapter.addAll(trade_modules);
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e)
				{
					e.printStackTrace();
				}
			}
		});

	}

	private void initView() {

		tv_start = (TextView) findViewById(R.id.tv_start);
		tv_end = (TextView) findViewById(R.id.tv_end);
		tv_rangeday = (TextView) findViewById(R.id.tv_rangeday);
		tv_total_trade = (TextView) findViewById(R.id.tv_total_trade);
		tv_total_income = (TextView) findViewById(R.id.tv_total_income);

		fangkeNumText = (TextView) findViewById(R.id.fangkeNumText);
		buyerNumText = (TextView) findViewById(R.id.buyerNumText);
		orderNumText = (TextView) findViewById(R.id.orderNumText);
		lv_datas = (ListView) findViewById(R.id.lv_datas);
		lv_datas.setAdapter(adapter);
	}
	public  void setCricle(){
		wheelIndicatorView = (WheelIndicatorTongjiView) findViewById(R.id.wheel_indicator_view);
		wheelIndicatorView.setItemsLineWidth(Util.dip2px(this, 2));
		//设置使用金额
		WheelIndicatorItem bikeActivityIndicatorItem = new WheelIndicatorItem
				(totalIncome/totalTrade, Color.parseColor("#ff5000"),Util.dip2px(this, 4));//外圈
		WheelIndicatorItem bikeActivityIndicatorItem1 = new WheelIndicatorItem
				(totalIncome/totalTrade, Color.parseColor("#2c4660"),Util.dip2px(this, 2));//内圈
		wheelIndicatorView.addWheelIndicatorItem(bikeActivityIndicatorItem1);
		wheelIndicatorView.addWheelIndicatorItem(bikeActivityIndicatorItem);
		wheelIndicatorView.startItemsAnimation();
	}
}
