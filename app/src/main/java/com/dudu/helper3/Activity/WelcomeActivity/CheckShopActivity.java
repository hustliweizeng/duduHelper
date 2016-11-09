package com.dudu.helper3.Activity.WelcomeActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.dudu.helper3.Activity.MainActivity;
import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.adapter.SelectShopAdapter;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.InfoBean;
import com.dudu.helper3.javabean.ShopCheckListBean;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author
 * @version 1.0
 * @date 2016/11/9
 */
public class CheckShopActivity extends BaseActivity implements View.OnClickListener {

	private ImageButton backButton;
	private ListView listview;
	private SwipeRefreshLayout siwpeRefresh;
	private SelectShopAdapter adapter;
	private boolean shopIsoPen;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		adapter = new SelectShopAdapter(this);
		setContentView(R.layout.activity_check_shop);
		initView();
		initData();
	}

	private void initData() {//显示列表信息
		ShopCheckListBean data = (ShopCheckListBean) getIntent().getSerializableExtra("data");
		if (data!=null && data.getList()!=null &&data.getList().size()>0){
			adapter.addall(data.getList());
		}
	}

	private void initView() {
		backButton = (ImageButton) findViewById(R.id.backButton);
		listview = (ListView) findViewById(R.id.listview);
		siwpeRefresh = (SwipeRefreshLayout) findViewById(R.id.siwpeRefresh);
		backButton.setOnClickListener(this);
		listview.setAdapter(adapter);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {//条目点击事件。直接切换相应的店铺
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				long itemId = adapter.getItemId(position);
				//根据店铺id进入不同的店铺，加载店铺数据
				checkShop(itemId+"");
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.backButton:
				finish();
				break;
		}
	}
	private void checkShop(String id)
	{
		RequestParams params = new RequestParams();
		//请求网络连接之前，设置保存cookie，
		HttpUtils.getConnection(context, params, ConstantParamPhone.CHECK_SHOP+id, "get", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络故障，请重试",Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						InfoBean infoBean = new Gson().fromJson(s, InfoBean.class);
						
						String isshopuser = infoBean.getUser().getIsshopuser();
						boolean isManager = false;
						if ("1".equals(isshopuser)){
							isManager = false;
							LogUtil.d("manager","false");
						}
						if ("0".equals(isshopuser)) {
							isManager = true;
							LogUtil.d("manager","true");
						}
						//保存用户信息
						//1.通过sp保存用户信息
						SharedPreferences.Editor edit = sp.edit();
						
						edit.putString("username",infoBean.getUser().getName())
								.putString("nickename",infoBean.getUser().getNickname())//手动添加
								.putString("shopid",infoBean.getShop().getId())		//店铺id        
								//2.存储商店信息
								.putString("id",infoBean.getShop().getId())
								.putString("shopLogo",infoBean.getShop().getLogo())
								.putString("shopName",infoBean.getShop().getName())
								//3.储存今日状态
								.putString("todayIncome",infoBean.getTodaystat().getIncome())
								//4.存储总计状态
								.putString("frozenMoney",infoBean.getTotalstat().getFreezemoney())
								.putString("useableMoney",infoBean.getTotalstat().getUsablemoney())
								//访客信息
								.putString("totalVistor",infoBean.getTotalstat().getVisitor())
								.putString("totalBuyer",infoBean.getTotalstat().getBuyer())
								.putString("totalOrder",infoBean.getTotalstat().getOrder())
								.putString("totalIncome",infoBean.getTotalstat().getIncome())
								.putString("totalTrade",infoBean.getTotalstat().getTrade())
								.putBoolean("isManager",isManager)
								//在后台处理
								.apply();
						LogUtil.d("welcome",s);
						//判断有没有绑定手机
						if (TextUtils.isEmpty(infoBean.getUser().getMobile())){
							Toast.makeText(context,"您还没有绑定手机号!",Toast.LENGTH_SHORT).show();
							Intent intent = new Intent(context, LoginBindPhoneActivity.class);//失败的话到绑定手机页面
							intent.putExtra("type","login");
							startActivity(intent);
							finish();
						}else {
							edit.putString("mobile",infoBean.getUser().getMobile()).commit();//
							startActivity(new Intent(context,MainActivity.class));//成功以后直接到主页
						}
						finish();
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
						//跳转到登陆页面
						startActivity(new Intent(context,LoginActivity.class));
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	

}
