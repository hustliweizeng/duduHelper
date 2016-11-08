package com.dudu.helper3.Activity.MyInfoActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.Activity.EmployeeManageActivity.ShopMemberListActivity;
import com.dudu.helper3.Activity.ShopManageActivity.ShopListManagerActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.Utils.CleanAppCache;
import com.dudu.helper3.application.DuduHelperApplication;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.widget.MyDialog;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopSettingActivity extends BaseActivity implements OnClickListener
{
	private RelativeLayout changePhoneNumRel;
	private TextView phoneNumText;
	private RelativeLayout shopEditRel;
	private RelativeLayout passwordEditRel;
	private RelativeLayout mendianRel;
	private RelativeLayout memberRel;
	private Button logoutButton;
	private RelativeLayout select_shop;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_setting);
		initHeadView("设置", true, false, 0);
		initView();
		initData();
	}
    
	//加载视图
	private void initView() 
	{
		logoutButton = (Button) this.findViewById(R.id.logoutButton);
		logoutButton.setOnClickListener(this);
		mendianRel = (RelativeLayout) this.findViewById(R.id.mendianRel);
		mendianRel.setOnClickListener(this); 
		memberRel = (RelativeLayout) this.findViewById(R.id.memberRel);
		memberRel.setOnClickListener(this);
		changePhoneNumRel = (RelativeLayout) this.findViewById(R.id.changePhoneNumRel);
		changePhoneNumRel.setOnClickListener(this);
		phoneNumText = (TextView) this.findViewById(R.id.phoneNumText);
		shopEditRel = (RelativeLayout) this.findViewById(R.id.shopEditRel);
		shopEditRel.setOnClickListener(this);
		passwordEditRel = (RelativeLayout) this.findViewById(R.id.passwordEditRel);
		passwordEditRel.setOnClickListener(this);
		select_shop = (RelativeLayout) findViewById(R.id.select_shop);
		select_shop.setOnClickListener(this);
	}
	
	//设置数据
	private void initData() 
	{
		String mobile = sp.getString("mobile","");
		if(!TextUtils.isEmpty(mobile))
		{
		   phoneNumText.setText(mobile);
		}
		else
		{
			phoneNumText.setText("请先绑定手机号");
		}
	}

	@Override
	public void onClick(View v) 
	{
		boolean isManager = sp.getBoolean("isManager", false);
		Intent intent = null;
		switch (v.getId())
		{
			case R.id.select_shop://选择分店店铺
				startActivityForResult(new Intent(context,ChangeShopActivity.class),20);
				return;
			case R.id.changePhoneNumRel:
				//绑定手机号页面
				intent=new Intent(ShopSettingActivity.this,ShopRebindPhoneSteponeActivity.class);
				break;
			case R.id.shopEditRel:
				intent=new Intent(ShopSettingActivity.this,ShopInfoEditActivity.class);
				break;
			case R.id.passwordEditRel:
				intent=new Intent(ShopSettingActivity.this,ShopPasswordEditActivity.class);
				break;
			case R.id.mendianRel:
				if (isManager){
					intent =new Intent(context,ShopListManagerActivity.class);
				}else {
					Toast.makeText(context,"您没有管理权限",Toast.LENGTH_SHORT).show();
					return;
				}
				break;
			case R.id.memberRel:
				if (isManager){
					intent=new Intent(ShopSettingActivity.this,ShopMemberListActivity.class);
				}else {
					Toast.makeText(context,"您没有管理权限",Toast.LENGTH_SHORT).show();
					return;
				}
				break;
			case R.id.logoutButton:
                MyDialog.showDialog(ShopSettingActivity.this, "  退出登录将清空用户信息，是否退出",true, true, "取消","确定", new OnClickListener() 
                {
					@Override
					//取消退出
					public void onClick(View v) 
					{
						MyDialog.cancel();
					}
				}, 
				new OnClickListener() 
				{
					@Override
					//确认退出
					public void onClick(View v) 
					{
						sp.edit().clear().commit();
						CleanAppCache.cleanApplicationData(context);
						DuduHelperApplication.getInstance().exit();
						requestLogOut();

					}
				});
                return;
			default:
				break;
		}
		startActivity(intent);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 20){
			String id = data.getStringExtra("id");
			if (!TextUtils.isEmpty(id)){//当返回数据不为空时，请求切换页面
				HttpUtils.getConnection(context, null, ConstantParamPhone.SWITCH_SHOP, "get", new TextHttpResponseHandler() {
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
		}
		
		
	}

	/**
	 * 联网请求退出
	 */
	private void requestLogOut() {
		RequestParams params = new RequestParams();

		HttpUtils.getConnection(context, params, ConstantParamPhone.LOG_OUT, "post", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				Toast.makeText(context,"已经退出当前账户，请重新登陆",Toast.LENGTH_LONG).show();
			}
		});
	}
}
