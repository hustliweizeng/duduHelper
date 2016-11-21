package com.dudu.duduhelper.Activity.RedBagActivity;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.Activity.WelcomeActivity.LoginActivity;
import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.adapter.RedBagCheckAdapter;
import com.dudu.duduhelper.adapter.RedBagListAdapter;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.RedBagListBean;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

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
	private Button btn_new_redbag;
	private boolean isDel =false;
	private ImageButton btn_check;
	private int count =0;
	private Button btn_edit;
	private ImageButton backButton;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_redbag_list);
		adapter = new RedBagListAdapter(context);
		initView();
		swipe_product_list.setProgressViewOffset(false, 0, Util.dip2px(context, 24));//第一次启动时刷新
		swipe_product_list.setRefreshing(true);
	}

	

	private void initView() {
		backButton = (ImageButton) findViewById(R.id.backButton);
		btn_edit = (Button) findViewById(R.id.btn_edit);
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
		btn_check = (ImageButton) findViewById(R.id.btn_check);
		btn_new_redbag = (Button) findViewById(R.id.btn_new_redbag);
		btn_new_redbag.setOnClickListener(this);
		tv_source.setOnClickListener(this);
		tv_status.setOnClickListener(this);
		reloadButton.setOnClickListener(this);
		lv_redbag.setAdapter(adapter);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		swipe_product_list.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				swipe_product_list.setRefreshing(true);
				requestRedbagStatus();
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
		//删除选中条目
		iv_del.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				List<String> delList = adapter.getDelList();
				if (delList== null ||delList.size()==0){
					Toast.makeText(context,"当前没有选中的条目",Toast.LENGTH_SHORT).show();
					return;
				}
				for (String item :delList){
					LogUtil.d("load","正在删除"+item);
					count++;
					delItem(item);
				}

			}
		});
		btn_edit.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!isDel) {
					ll_check.setVisibility(View.VISIBLE);
					btn_new_redbag.setVisibility(View.GONE);
					adapter.setIsDel();
					btn_edit.setText("取消");
					isDel = !isDel;
				} else {
					ll_check.setVisibility(View.GONE);
					btn_new_redbag.setVisibility(View.VISIBLE);
					adapter.setIsDel();
					btn_edit.setText("编辑");
					isDel = !isDel;
				}
			}
		});

	}
	//删除指定id
	private void delItem(String id) {
		LogUtil.d("res","id="+id);
		AsyncHttpClient client = new AsyncHttpClient();
		PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
		client.setCookieStore(myCookieStore);
		String defaultUserAgent = null;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1) {
			defaultUserAgent = WebSettings.getDefaultUserAgent(context);
		}else {
			WebView view = new WebView(this);
			defaultUserAgent = view.getSettings().getUserAgentString();
		}

		if (isWifiAvailable(context)){
			client.addHeader("NETTYPE","WIFI");
		}else {
			client.addHeader("NETTYPE","3G+");
		}
		client.setUserAgent(defaultUserAgent);
		client.delete(ConstantParamPhone.BASE_URL+ConstantParamPhone.DEL_REDBAG+id, null,new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				throwable.printStackTrace();
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
						count--;
						LogUtil.d("count:",+count+"");
						if (count ==0){
							Toast.makeText(context,"所选红包已经删除",Toast.LENGTH_SHORT).show();
							//全部删除后，刷新页面
							requestRedbagStatus();
							//每次删除完毕后清空集合
							adapter.delIds.clear();
						}
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
						startActivity(new Intent(context, LoginActivity.class));
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * 判断wifi连接状态
	 *
	 * @param ctx
	 * @return
	 */
	public  boolean isWifiAvailable(Context ctx) {
		ConnectivityManager conMan = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (NetworkInfo.State.CONNECTED == wifi) {
			return true;
		} else {
			return false;
		}
	}


	@Override
	public void onResume() {
		super.onResume();
		//重新请求数据
		requestRedbagStatus();
	}

	/**
	 * 请求红包状态
	 */
	private void requestRedbagStatus() {
		//ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
		//弹对话框
		String url = ConstantParamPhone.GET_REDBAG_LIST;
		//请求结果处理
		HttpUtils.getConnection(context, null, url, "get", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				//设置有无红包的状态,解析处理json数据
				Log.d("redbag",s);
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						list= new Gson().fromJson(s, RedBagListBean.class);
						if (list.getData()!=null && list.getData().size()>0){
							//每次刷新数据之前清空数据
							adapter.clear();
							adapter.AddAll(list.getData());
							adapter.notifyDataSetChanged();
							swipe_product_list.setRefreshing(false);
						}

					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
						startActivity(new Intent(context, LoginActivity.class));
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				//默认设置没有红包
			}

			@Override
			public void onFinish() {
				super.onFinish();
				swipe_product_list.setRefreshing(false);
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
			case R.id.btn_new_redbag:
				startActivity(new Intent(context,CreateRedBagActivity.class));
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
			requestRedbagStatus();
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
