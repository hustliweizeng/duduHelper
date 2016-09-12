package com.dudu.duduhelper.Activity.CashHistoryActivity;

import android.app.ActionBar;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.adapter.MoneyHistoryAdapter;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.CashHistoryBean;
import com.dudu.duduhelper.widget.CalendarView;
import com.dudu.duduhelper.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.squareup.picasso.Downloader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShopMoneyRecordListActivity extends BaseActivity {
	//编辑按钮
	private Button editButton;
	private PopupWindow popupWindow;
	private TextView calendarCenter;
	private CalendarView calener;
	private ListView lv_checckmoneyhisory;
	private MoneyHistoryAdapter adapter;
	private String downDate1;
	private SimpleDateFormat format;
	private CalendarView calendar;
	private TextView tv_msg;
	private int titleHight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//手动测量并记录标题栏的高度
		measureTitleHight();
		setContentView(R.layout.shop_money_record_list);
		adapter = new MoneyHistoryAdapter(this);
		//初始化当天日期
		format = new SimpleDateFormat("yyyy-MM-dd");
		downDate1 = format.format(new Date());
		downDate1 = "";
		LogUtil.d("收银流水", downDate1);
		initHeadView("收银流水", true, false, 0);
		initView();
		getData();

	}

	//在界面可见之前测量控件的高度
	private void measureTitleHight() {
		int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
		relayout_mytitle.measure(0, height);
		titleHight = relayout_mytitle.getHeight();
		LogUtil.d("height", titleHight + "'");
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

	}

	/**
	 * 下拉弹出
	 */
	private void getDataPopWindow() {
		LayoutInflater layoutInflater = (LayoutInflater) ShopMoneyRecordListActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.shop_data_popwindow, null);

		if (popupWindow == null) {
			//每次都new的话一些数据会遗失
			popupWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
			//当前日期,第一次初始化
			calendarCenter = (TextView) view.findViewById(R.id.calendarCenter);
			calendarCenter.setText(new SimpleDateFormat("yyyy年MM月dd日").format(new Date()));
			calener = (CalendarView) view.findViewById(R.id.calendar);
		}
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
				//更新标题的日期
				calendarCenter.setText(new SimpleDateFormat("yyyy年MM月dd日").format(downDate));
				//弹窗收回，请求数据
				popupWindow.dismiss();

			}
		});
	}


	/**
	 * 异步请求数据
	 */
	private void getData() {
		ColorDialog.showRoundProcessDialog(context, R.layout.loading_process_dialog_color);
		RequestParams param = new RequestParams();
		param.add("date", downDate1);
		param.add("lastid", "0");
		HttpUtils.getConnection(context, param, ConstantParamPhone.GET_CASH_HISTORY, "GET", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
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
						adapter.addAll(historyBean.getData());
						tv_msg.setVisibility(View.GONE);
					} else {
						//数据请求失败
						String msg = object.getString("msg");
						tv_msg.setVisibility(View.VISIBLE);
						//Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				//数据请求成功后弹窗消失
				ColorDialog.dissmissProcessDialog();
				lv_checckmoneyhisory.setVisibility(View.VISIBLE);
				//数据解析成功后，展示列表
				lv_checckmoneyhisory.setAdapter(adapter);

			}
		});
	}


}
