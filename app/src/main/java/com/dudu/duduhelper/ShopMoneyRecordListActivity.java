package com.dudu.duduhelper;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;

public class ShopMoneyRecordListActivity extends BaseActivity 
{
	//编辑按钮
	private Button editButton;
	private PopupWindow popupWindow;

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
	}

	private void getDataPopWindow()
	{
		LayoutInflater layoutInflater = (LayoutInflater)ShopMoneyRecordListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.shop_data_popwindow, null);
		popupWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT,  ActionBar.LayoutParams.MATCH_PARENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		popupWindow.showAsDropDown(relayout_mytitle);

	}

}
