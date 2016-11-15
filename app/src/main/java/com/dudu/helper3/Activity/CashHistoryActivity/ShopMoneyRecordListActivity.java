package com.dudu.helper3.Activity.CashHistoryActivity;

import android.app.ActionBar;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.Utils.Util;
import com.dudu.helper3.adapter.MoneyHistoryAdapter;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.CashHistoryBean;
import com.dudu.helper3.widget.CalendarView;
import com.dudu.helper3.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ShopMoneyRecordListActivity extends BaseActivity {
	//编辑按钮
	private Button editButton;
	private PopupWindow popupWindow;
	private TextView calendarCenter;
	private ListView lv_checckmoneyhisory;
	private MoneyHistoryAdapter adapter;
	private String downDate1;
	private SimpleDateFormat format;
	private CalendarView calendar;
	private TextView tv_msg;
	private int titleHight;
	private SwipeRefreshLayout swiperefresh;
	private Date currentDate;
	private Calendar calenderUtil;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//手动测量并记录标题栏的高度
		setContentView(R.layout.shop_money_record_list);
		currentDate = new Date(System.currentTimeMillis());
		calenderUtil = Calendar.getInstance();
		adapter = new MoneyHistoryAdapter(this);
		//初始化当天日期
		format = new SimpleDateFormat("yyyy-MM-dd");
		downDate1 = format.format(new Date());
		downDate1 = "";
		LogUtil.d("收银流水", downDate1);
		initHeadView("收银流水", true, false, 0);
		initView();
		swiperefresh.setProgressViewOffset(false, 0, Util.dip2px(context, 24));//第一次启动时刷新
		swiperefresh.setRefreshing(true);
		getData(); 
	}
	private void initView() {
		tv_msg = (TextView) findViewById(R.id.tv_msg);
		editButton = (Button) this.findViewById(R.id.selectTextClickButton);
		editButton.setVisibility(View.VISIBLE);
		editButton.setText("日历");
		editButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				getDataPopWindow();
			}
		});
		lv_checckmoneyhisory = (ListView) findViewById(R.id.lv_checckmoneyhisory);
		swiperefresh = (SwipeRefreshLayout) findViewById(R.id.swiperefresh);
		swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				getData();
			}
		});
	}

	/**
	 * 下拉弹出
	 */
	
	private void getDataPopWindow() {
		LayoutInflater layoutInflater = (LayoutInflater) ShopMoneyRecordListActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.shop_data_popwindow, null);
		if (popupWindow == null) {//初始化日期显示
			//每次都new的话一些数据会遗失
			popupWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
			//当前日期,第一次初始化
			calendarCenter = (TextView) view.findViewById(R.id.calendarCenter);//日期的对象
			calendarCenter.setText(new SimpleDateFormat("yyyy年MM月").format(new Date()));
		}
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		//设置打开方式，在标题的下方
		popupWindow.showAsDropDown(relayout_mytitle);

		//初始化自定义日历calender
		calendar = (CalendarView) view.findViewById(R.id.calendar);
		calendar.setOnItemClickListener(new CalendarView.OnItemClickListener() {
			@Override
			public void OnItemClick(Date selectedStartDate, Date selectedEndDate, Date downDate) {
				//获取view中的按下日期，准备请求数据
				downDate1 = format.format(downDate);
				//清空集合
				getData();
				LogUtil.d("收银流水", downDate1);
				//弹窗收回，请求数据
				popupWindow.dismiss();

			}
		});
		/**
		 * 切换月份
		 */
		
		final ImageButton calendarLeft = (ImageButton) view.findViewById(R.id.calendarLeft);
		calendarLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				calendar.clickLeftMonth();
				calenderUtil.setTime(currentDate);
				calenderUtil.add(Calendar.MONTH, -1);
				currentDate = calenderUtil.getTime();
				calendarCenter.setText(new SimpleDateFormat("yyyy年MM月").format(currentDate));
			}
		});
		ImageButton calendarRight = (ImageButton) view.findViewById(R.id.calendarRight);
		calendarRight.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				calenderUtil.setTime(currentDate);
				calenderUtil.add(Calendar.MONTH, 1);
				currentDate = calenderUtil.getTime();
				calendarCenter.setText(new SimpleDateFormat("yyyy年MM月").format(currentDate));
				calendar.clickRightMonth();
			}
		});
	}


	/**
	 * 异步请求数据
	 */
	private void getData() {
		RequestParams param = new RequestParams();
		param.add("date", downDate1);
		LogUtil.d("selcetDATA",downDate1);
		param.add("lastid", "0");
		HttpUtils.getConnection(context, param, ConstantParamPhone.GET_CASH_HISTORY, "GET", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				LogUtil.d("list", s);
				try {
					JSONObject object = new JSONObject(s);
					String code = object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)) {
						//数据请求成功
						CashHistoryBean historyBean = new Gson().fromJson(s, CashHistoryBean.class);
						//插入数据之前，清空之前的adapter的集合，否则数据会重复
						adapter.orders.clear();
						adapter.list.clear();
						//获取数据非空判断
						if (historyBean.getData()!=null &&historyBean.getData().size()>0){
							adapter.addAll(historyBean.getData());
							tv_msg.setVisibility(View.GONE);
						}else {
							tv_msg.setVisibility(View.VISIBLE);
						}
						//刷新页面
						adapter.notifyDataSetChanged();

					} else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				swiperefresh.setRefreshing(false);
				//数据请求成功后弹窗消失
				lv_checckmoneyhisory.setVisibility(View.VISIBLE);
				//数据解析成功后，展示列表
				lv_checckmoneyhisory.setAdapter(adapter);

			}
		});
	}


}
