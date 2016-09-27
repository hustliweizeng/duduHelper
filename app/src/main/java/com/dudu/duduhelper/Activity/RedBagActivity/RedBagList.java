package com.dudu.duduhelper.Activity.RedBagActivity;

import android.app.ActionBar;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dudu.duduhelper.Activity.BigBandActivity.shopProductListActivity;
import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.javabean.RedBagListBean;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/27
 */

public class RedBagList extends BaseActivity implements View.OnClickListener {
	private TextView tv_source;
	private TextView tv_status;
	private Button reloadButton;
	private ListView productListView;
	private SwipeRefreshLayout swipe_product_list;
	private TextView tv_check_all;
	private ImageView iv_del;
	private LinearLayout ll_check;
	private ImageView iv_source;
	private ImageView iv_status;
	private LinearLayout selectLine;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_redbag_list);
		initView();
		initData();
	}

	private void initData() {
		List<RedBagListBean.DataBean> list = (List<RedBagListBean.DataBean>) getIntent().getSerializableExtra("data");
		
		
	}

	private void initView() {
		tv_source = (TextView) findViewById(R.id.tv_source);
		tv_status = (TextView) findViewById(R.id.tv_status);
		reloadButton = (Button) findViewById(R.id.reloadButton);
		productListView = (ListView) findViewById(R.id.productListView);
		swipe_product_list = (SwipeRefreshLayout) findViewById(R.id.swipe_product_list);
		tv_check_all = (TextView) findViewById(R.id.tv_check_all);
		iv_del = (ImageView) findViewById(R.id.iv_del);
		ll_check = (LinearLayout) findViewById(R.id.ll_check);
		selectLine = (LinearLayout) this.findViewById(R.id.selectLine);
		iv_source = (ImageView) findViewById(R.id.iv_source);
		iv_status = (ImageView) findViewById(R.id.iv_status);
		
		tv_source.setOnClickListener(this);
		tv_status.setOnClickListener(this);
		reloadButton.setOnClickListener(this);



	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.reloadButton:

				break;
			case R.id.tv_source:
				tv_source.setTextColor(getResources().getColor(R.color.text_green_color));
				iv_source.setImageResource(R.drawable.icon_jiantou_shang);
				tv_source.setTextColor(getResources().getColor(R.color.text_color_gray));
				iv_status.setImageResource(R.drawable.icon_jiantou_xia);
				showPopwindow("order");
				break;
			case R.id.tv_status:
				break;
		}
	}
	public  void showPopwindow(String side){

		View view = View.inflate(context,R.layout.activity_product_window_select, null);
		PopupWindow popupWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		//在哪里显示
		popupWindow.showAsDropDown(selectLine);
		ListView productSelectList = (ListView) view.findViewById(R.id.productSelectList);
		
		

	}
}
