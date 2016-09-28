package com.dudu.duduhelper.Activity.RedBagActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.dudu.duduhelper.Activity.BigBandActivity.shopProductListActivity;
import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.adapter.RedBagCheckAdapter;
import com.dudu.duduhelper.adapter.RedBagListAdapter;
import com.dudu.duduhelper.adapter.RedbagMsgListAdapter;
import com.dudu.duduhelper.javabean.RedBagListBean;

import java.util.ArrayList;
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
	private ListView lv_redbag;
	private SwipeRefreshLayout swipe_product_list;
	private TextView tv_check_all;
	private ImageView iv_del;
	private LinearLayout ll_check;
	private ImageView iv_source;
	private ImageView iv_status;
	private LinearLayout selectLine;
	private RedBagListAdapter adapter;
	private RedBagCheckAdapter<String> arrayAdapter1;
	private RedBagListBean list;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_redbag_list);
		adapter = new RedBagListAdapter(context);
		initHeadView("商家红包",true,false,0);
		initView();
		initData();
	}

	private void initData() {
		list = (RedBagListBean) getIntent().getSerializableExtra("data");
		if (list.getData()!=null && list.getData().size()>0){
			//每次刷新数据之前清空数据
			adapter.clear();
			adapter.AddAll(list.getData());
			adapter.notifyDataSetChanged();
			swipe_product_list.setRefreshing(false);
		}
	}

	private void initView() {
		tv_source = (TextView) findViewById(R.id.tv_source);
		tv_status = (TextView) findViewById(R.id.tv_status);
		reloadButton = (Button) findViewById(R.id.reloadButton);
		lv_redbag = (ListView) findViewById(R.id.lv_redbag);
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
		lv_redbag.setAdapter(adapter);
		swipe_product_list.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				swipe_product_list.setRefreshing(true);
				initData();
			}
		});
		lv_redbag.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				Intent intent = new Intent(context, ShopHongBaoDetailActivity.class);
				intent.putExtra("id",list.getData().get(position).getId());
				startActivity(intent);
			}
		});

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.reloadButton:

				break;
			case R.id.tv_source:
				tv_source.setTextColor(getResources().getColor(R.color.text_green_color));
				iv_source.setImageResource(R.drawable.icon_jiantou_shang);
				tv_status.setTextColor(getResources().getColor(R.color.text_color_gray));
				iv_status.setImageResource(R.drawable.icon_jiantou_xia);
				showPopwindow("left");
				break;
			case R.id.tv_status:
				tv_source.setTextColor(getResources().getColor(R.color.text_color_gray));
				iv_source.setImageResource(R.drawable.icon_jiantou_xia);
				tv_status.setTextColor(getResources().getColor(R.color.text_green_color));
				iv_status.setImageResource(R.drawable.icon_jiantou_shang);
				showPopwindow("right");
				break;
		}
	}
	public  void showPopwindow(final String side){

		View view = View.inflate(context,R.layout.activity_product_window_select, null);
		final PopupWindow popupWindow = new PopupWindow(view, ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		//在哪里显示
		popupWindow.showAsDropDown(selectLine);
		arrayAdapter1 = null;
		ListView selectList = (ListView) view.findViewById(R.id.productSelectList);
		ImageView closeImageButton = (ImageView) view.findViewById(R.id.closeImageButton);
		closeImageButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		if ("left".equals(side)){
			List<String> s = new ArrayList<>();
			s.add("默认所有");
			s.add("商家发放");
			s.add("免费领取");
			arrayAdapter1 = new RedBagCheckAdapter<>(context);
			arrayAdapter1.AddAll(s);
			selectList.setAdapter(arrayAdapter1);
		}else if ("right".equals(side)){
			LogUtil.d("right","222");
			List<String> s1 = new ArrayList<>();
			s1.add("全部");
			s1.add("已领取");
			s1.add("已消费");
			s1.add("已过期");
			arrayAdapter1 = new RedBagCheckAdapter<>(context);
			arrayAdapter1.AddAll(s1);
			selectList.setAdapter(arrayAdapter1);
		}
		//筛选的条目点击事件
		selectList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			if ("left".equals(side)){
				tv_source.setText((arrayAdapter1.getItem(position)));
			}else {
				tv_status.setText((arrayAdapter1.getItem(position)));
			}
			popupWindow.dismiss();
			initData();
			}
		});
		popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener()
		{

			@Override
			public void onDismiss()
			{
				//重置所有按钮
				tv_source.setTextColor(context.getResources().getColor(R.color.text_color_gray));
				iv_source.setImageResource(R.drawable.icon_jiantou_xia);
				tv_status.setTextColor(context.getResources().getColor(R.color.text_color_gray));
				iv_status.setImageResource(R.drawable.icon_jiantou_xia);
			}
		});
		
		

	}
}
