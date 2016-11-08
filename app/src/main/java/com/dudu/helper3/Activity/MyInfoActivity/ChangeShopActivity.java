package com.dudu.helper3.Activity.MyInfoActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.dudu.helper3.Activity.MainActivity;
import com.dudu.helper3.Activity.WelcomeActivity.LoginActivity;
import com.dudu.helper3.Activity.WelcomeActivity.LoginBindPhoneActivity;
import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.Utils.Util;
import com.dudu.helper3.adapter.CheckShopAdapter;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.LoginBean;
import com.dudu.helper3.javabean.ShopListBean;
import com.dudu.helper3.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author
 * @version 1.0
 * @date 2016/11/8
 */

public class ChangeShopActivity extends BaseActivity {
	private ListView listview;
	private SwipeRefreshLayout siwpeRefresh;
	private CheckShopAdapter adapter;
	private ImageButton backButton;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_change_shop);
		adapter = new CheckShopAdapter(context);
		
		
		initView();
		initHeadView("门店切换",true,false,0);
		siwpeRefresh.setProgressViewOffset(false, 0, Util.dip2px(context, 24));//第一次启动时刷新
		siwpeRefresh.setRefreshing(true);
		initData();
		

	}

	private void initData() {
		HttpUtils.getConnection(context, null, ConstantParamPhone.GET_SHOPABLE, "get", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int i, Header[] headers, String s) {

				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						ShopListBean data = new Gson().fromJson(s, ShopListBean.class);
						adapter.addAll(data.getData());
						

					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	private void initView() {
		listview = (ListView) findViewById(R.id.listview);
		siwpeRefresh = (SwipeRefreshLayout) findViewById(R.id.siwpeRefresh);
		backButton = (ImageButton) findViewById(R.id.backButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String checkedId = adapter.getCheckedId();
				switchShop(checkedId);
			}
		});
				
	}

	private void switchShop(String checkedId) {
		ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
		String url = ConstantParamPhone.SWITCH_SHOP;//调用切换门店信息
		HttpUtils.getConnection(context, null,url+checkedId,"get",new TextHttpResponseHandler()
		{
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3)
			{
				arg3.printStackTrace();
				Toast.makeText(context, arg2, Toast.LENGTH_LONG).show();

			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2)
			{
				LogUtil.d("login",arg2);

				LoginBean loginBean = new Gson().fromJson(arg2,LoginBean.class);
				//判断返回状态是否成功
				if (ConstantParamPhone.SUCCESS.equalsIgnoreCase(loginBean.getCode())){

					//判断手机绑定没有
					if (TextUtils.isEmpty(loginBean.getUser().getMobile())){
						Toast.makeText(context,"当前账号未绑定，请先绑定手机号",Toast.LENGTH_LONG).show();
						startActivity(new Intent(context,LoginBindPhoneActivity.class));
						finish();
						return;//不能进入了
					}
					//对bean的数据做非空判断
					String isshopuser = loginBean.getUser().getIsshopuser();
					boolean isManager = true;
					if ("1".equals(isshopuser)){
						isManager = false;
						LogUtil.d("manager","false");
					}
					if ("0".equals(isshopuser)) {
						isManager = true;
					}

					//1.通过sp保存用户信息
					SharedPreferences.Editor edit = sp.edit();
					edit.putString("username", loginBean.getUser().getName())
							.putString("nickename", loginBean.getUser().getNickname())//手动添加
							.putString("mobile", loginBean.getUser().getMobile())
							.putString("shopid",loginBean.getShop().getId())		//店铺id
							//2.存储商店信息
							.putString("id", loginBean.getShop().getId())
							.putString("shopLogo", loginBean.getShop().getLogo())
							.putString("shopName", loginBean.getShop().getName())
							//3.储存今日状态
							.putString("todayIncome", loginBean.getTodaystat().getIncome())
							//4.存储总计状态
							.putString("frozenMoney", loginBean.getTotalstat().getFreezemoney())
							.putString("useableMoney", loginBean.getTotalstat().getUsablemoney())
							//储存店员状态
							.putBoolean("isManager", isManager)
							//储存统计信息
							.putString("totalVistor",loginBean.getTotalstat().getVisitor())
							.putString("totalBuyer",loginBean.getTotalstat().getBuyer())
							.putString("totalOrder",loginBean.getTotalstat().getOrder())
							.putString("totalIncome",loginBean.getTotalstat().getIncome())
							.putString("totalTrade",loginBean.getTotalstat().getTrade())
							.putBoolean("isManager",isManager)
							//在后台处理
							.apply();
					//跳转到主页


					startActivity(new Intent(context, MainActivity.class));
					finish();

				}else if (ConstantParamPhone.FAIL.equalsIgnoreCase(loginBean.getCode())){
					Toast.makeText(context,"用户名或者密码不正确",Toast.LENGTH_LONG).show();
				}

			}
			@Override
			public void onFinish()
			{
				ColorDialog.dissmissProcessDialog();
			}
		});
	}
	
}
