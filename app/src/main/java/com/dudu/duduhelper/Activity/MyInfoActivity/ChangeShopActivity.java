package com.dudu.duduhelper.Activity.MyInfoActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.Activity.MainActivity;
import com.dudu.duduhelper.Activity.WelcomeActivity.CheckShopActivity;
import com.dudu.duduhelper.Activity.WelcomeActivity.LoginActivity;
import com.dudu.duduhelper.Activity.WelcomeActivity.LoginBindPhoneActivity;
import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.adapter.CheckShopAdapter;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.LoginBean;
import com.dudu.duduhelper.javabean.ShopCheckListBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.dudu.duduhelper.R;
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
	private boolean shopIsoPen;
	private TextView btn_confirm;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_change_shop);
		adapter = new CheckShopAdapter(context);
		initView();
		initData();
	}

	/**
	 * 获取店铺列表信息
	 */
	private void initData() {

		RequestParams params = new RequestParams();
		String umeng_token = sp.getString("umeng_token", "");
		params.add("umeng_token",umeng_token);
		params.add("username",sp.getString("loginname",""));
		params.add("password",sp.getString("password",""));

		String url = ConstantParamPhone.USER_LOGIN;//调用切换门店信息
		HttpUtils.getConnection(context, params,url,"POST",new TextHttpResponseHandler()
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
				ColorDialog.dissmissProcessDialog();
				LogUtil.d("ss",arg2);
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						ShopCheckListBean data = new Gson().fromJson(arg2, ShopCheckListBean.class);
						if (data!=null && data.getList()!=null){
							adapter.addAll(data.getList());
						}
						siwpeRefresh.setRefreshing(false);
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
		listview.setAdapter(adapter);
		//确定按钮
		btn_confirm = (TextView) findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {//返回的时候确认选择
				String checkedId = adapter.getCheckedId();
				if (!TextUtils.isEmpty(checkedId)){//非空判断
					switchShop(checkedId);
				}else {
					finish();//空的时候结束当前页面
				}
			}
		});
		siwpeRefresh = (SwipeRefreshLayout) findViewById(R.id.siwpeRefresh);
		siwpeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				initData();
			}
		});
		backButton = (ImageButton) findViewById(R.id.backButton);
		backButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				adapter.setCheckedShop(position);//设置选中条目
			}
		});
	}


	/**
	 * 请求网路切换门店
	 * @param checkedId
	 */
	private void switchShop(String checkedId) {
		if(TextUtils.isEmpty(checkedId)){
			Toast.makeText(context,"您没有选择店铺！",Toast.LENGTH_SHORT).show();
			return; 
		}
		ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
		String url = ConstantParamPhone.SWITCH_SHOP;//调用切换门店信息
		String umeng_token = sp.getString("umeng_token", "");
		HttpUtils.getConnection(context, null,url+checkedId+"?umeng_token="+umeng_token,"get",new TextHttpResponseHandler()
		{
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3)
			{
				arg3.printStackTrace();
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

					//判断是否主店铺权限
					String mainId = sp.getString("mainId","");//保存主店铺信息
					String id = loginBean.getShop().getId();
					LogUtil.d("mainid ",mainId);
					LogUtil.d("id ",id);
					if (mainId.equals(id)){
						sp.edit().putBoolean("isMainShop",true).commit();
					}else {
						sp.edit().putBoolean("isMainShop",false).commit();
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
							.putString("uncheckPrice",loginBean.getTotalstat().getUnverificationmoney())
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
					startActivity(new Intent(context, LoginActivity.class));
					finish();
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
