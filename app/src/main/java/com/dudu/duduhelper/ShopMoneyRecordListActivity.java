package com.dudu.duduhelper;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dudu.duduhelper.adapter.MoneyHistoryAdapter;
import com.dudu.duduhelper.widget.CalendarView;
import com.dudu.duduhelper.widget.ColorDialog;
import com.loopj.android.http.RequestParams;

import java.util.Date;

public class ShopMoneyRecordListActivity extends BaseActivity
{
	//编辑按钮
	private Button editButton;
	private PopupWindow popupWindow;
	private TextView calendarCenter;
	private CalendarView calener;
	private ListView lv_checckmoneyhisory;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_money_record_list);
		initHeadView("收银流水", true, false, 0);
		initView();

	}

	private void initView()
	{
		editButton=(Button) this.findViewById(R.id.selectTextClickButton);
		editButton.setVisibility(View.VISIBLE);
		editButton.setText("日历");
		editButton.setOnClickListener(new View.OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				getDataPopWindow();
			}
		});
		//刚开始listview不可见
		lv_checckmoneyhisory = (ListView) findViewById(R.id.lv_checckmoneyhisory);
		//lv_checckmoneyhisory.setVisibility(View.GONE);

	}

	/**
	 * 下拉弹出
	 */
	private void getDataPopWindow()
	{
		LayoutInflater layoutInflater = (LayoutInflater)ShopMoneyRecordListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.shop_data_popwindow, null);
		calendarCenter = (TextView) view.findViewById(R.id.calendarCenter);
		calener = (CalendarView) view.findViewById(R.id.calendar);

		popupWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT,  ActionBar.LayoutParams.MATCH_PARENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		//设置打开方式，在标题的下方
		popupWindow.showAsDropDown(relayout_mytitle);

		//初始化自定义日历calender
		final CalendarView calendar = (CalendarView) view.findViewById(R.id.calendar);
		calendar.setOnItemClickListener(new CalendarView.OnItemClickListener() {
			@Override
			public void OnItemClick(Date selectedStartDate, Date selectedEndDate, Date downDate) {
				//获取view中的按下日期
				String downDate1 = calendar.getFormateDownDate();
				//Log.d("账单流水",downDate1);
				calendarCenter.setText(downDate1);
				//弹窗收回，请求数据
				popupWindow.dismiss();
				ColorDialog.showRoundProcessDialog(ShopMoneyRecordListActivity.this, R.layout.loading_process_dialog_color);
				getData();

			}
		});
	}

	/**
	 * 异步请求数据
	 */
	private void getData() {
		RequestParams param =  new RequestParams();
		param.add("token",share.getString("token",""));
		//String url = ConstantParamPhone.IP+;
		//HttpUtils.getConnection(this,param,);
		//数据请求成功后弹窗消失
		//SystemClock.sleep(1000);
		ColorDialog.dissmissProcessDialog();

		lv_checckmoneyhisory.setVisibility(View.VISIBLE);
		lv_checckmoneyhisory.setAdapter(new MoneyHistoryAdapter(this));
	}

}
