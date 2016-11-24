package com.dudu.duduhelper.Activity.MyInfoActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Activity.EmployeeManageActivity.ShopMemberListActivity;
import com.dudu.duduhelper.Activity.ShopManageActivity.ShopListManagerActivity;
import com.dudu.duduhelper.Utils.CleanAppCache;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.widget.MyDialog;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import com.dudu.duduhelper.R;
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
	private boolean shopIsoPen;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_setting);
		initHeadView("店铺设置", true, false, 0);
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
				startActivity(new Intent(context,ChangeShopActivity.class));
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
