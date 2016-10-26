package com.dudu.helper3.Activity.MyInfoActivity;

import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.helper3.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.helper3.application.DuduHelperApplication;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.BankCardListBean;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopUserBankInfoActivity extends BaseActivity 
{
	private LinearLayout kaihuBanklin;
	private LinearLayout kaihuPriviencelin;
	private LinearLayout userBankCityLin;
	private TextView userBankNameTextView;
	private TextView provienceTextView;
	private TextView userBankCityTextView;
	private String provienceCode;
	private String cityCode;
	private TextView userBankOpenNameTextView;
	private TextView userBankNumTextView;
	private TextView userBankSonNameEditText;
	private TextView unbindButton;
	 private PopupWindow popupWindow;
	//编辑按钮
	private Button editButton;
	private BankCardListBean.DataBean comeinData;
	private Button btnGetmess;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_user_bank_info);
		initHeadView("我的银行卡", true, false, 0);

		//获取传递过来的集合数据
		comeinData = (BankCardListBean.DataBean)(getIntent().getSerializableExtra("cardInfo"));


		editButton=(Button) this.findViewById(R.id.selectTextClickButton);
		editButton.setText("编辑");
		editButton.setVisibility(View.VISIBLE);
		DuduHelperApplication.getInstance().addActivity(this);
		initView();
		initData();
	}
	private void initView() 
	{
		unbindButton = (TextView) this.findViewById(R.id.unbindButton);
		userBankSonNameEditText=(TextView) this.findViewById(R.id.userBankSonNameTextView);
		userBankNumTextView=(TextView) this.findViewById(R.id.userBankNumTextView);
		userBankNameTextView=(TextView) this.findViewById(R.id.userBankNameTextView);
		userBankCityTextView=(TextView) this.findViewById(R.id.userBankCityTextView);
		provienceTextView=(TextView) this.findViewById(R.id.provienceTextView);
		userBankOpenNameTextView=(TextView) this.findViewById(R.id.userBankOpenNameTextView);
		kaihuBanklin=(LinearLayout) this.findViewById(R.id.kaihuBanklin);
		kaihuPriviencelin=(LinearLayout) this.findViewById(R.id.kaihuPriviencelin);
		userBankCityLin=(LinearLayout) this.findViewById(R.id.userBankCityLin);
		//编辑当前银行卡按钮的点击事件

		editButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				Intent intent = new Intent(ShopUserBankInfoActivity.this,ShopUserBankInfoEditActivity.class);
				intent.putExtra("info",comeinData);
				startActivity(intent);
			}
		});
		unbindButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				//弹出解除绑定窗口
				popUbingPhone();
			}
			
		});

	}

	//设置银行卡信息
	/*"id" => "银行卡ID 修改时和提现时用"
			"name" => "账户姓名"
			"bank_key" => "银行名称"
			"bank_name" => "支行信息"
			"card_number" => "卡号"
			"province_id" => "省份ID"
			"city_id" => "城市ID"*/
	private void initData() 
	{
		//用户名
		if(!TextUtils.isEmpty(comeinData.getName()))
		{
			userBankNameTextView.setText(comeinData.getName());
		}
		//
		//卡号
		if(!TextUtils.isEmpty(comeinData.getCard_number()))
		{
			userBankNumTextView.setText(comeinData.getCard_number());
		}
		//银行名称
		if(!TextUtils.isEmpty(comeinData.getBank_key()))
		{
			userBankOpenNameTextView.setText(comeinData.getBank_key());
		}
		//省份
		if(!TextUtils.isEmpty(comeinData.getProvince_name()))
		{
			provienceTextView.setText(comeinData.getProvince_name());
		}
		//城市
		if(!TextUtils.isEmpty(comeinData.getCity_name()))
		{
			userBankCityTextView.setText(comeinData.getCity_name());
		}
		//支行名称
		if(!TextUtils.isEmpty(comeinData.getBank_name()))
		{
			userBankSonNameEditText.setText(comeinData.getBank_name());
		}
	}
	//解除当前银行卡绑定
	private void popUbingPhone()
	{
		AlphaAnimation animation = new AlphaAnimation((float)0, (float)1.0);
		animation.setDuration(500); //设置持续时间5秒
		LayoutInflater layoutInflater = (LayoutInflater)ShopUserBankInfoActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);  
        View view = layoutInflater.inflate(R.layout.shop_unbind_phone_pop_window, null);
		//验证码按钮,请求网络获取验证码
		btnGetmess = (Button) view.findViewById(R.id.btnGetmess);
		btnGetmess.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				RequestParams params = new RequestParams();
				params.put("mobile",sp.getString("mobile","当前没有绑定的手机号！"));
				params.put("type","unbindbank");
				HttpUtils.getConnection(context, params, ConstantParamPhone.GET_SMS_CONFIRM, "GET", new TextHttpResponseHandler() {
					@Override
					public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
						Toast.makeText(context,"网络异常，请稍后再试",Toast.LENGTH_LONG).show();
					}
					@Override
					public void onSuccess(int i, Header[] headers, String s) {
						//显示倒计时
						showResidueSeconds();
					}
				});
			}
		});
		TextView bindPhoneText = (TextView) view.findViewById(R.id.bindPhoneText);
		bindPhoneText.setText(sp.getString("mobile","请先绑定手机号"));
		final EditText messageCodeEditText = (EditText) view.findViewById(R.id.messageCodeEditText);
        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,  LayoutParams.MATCH_PARENT);
		//提交确认解除绑定
		Button submitPhoneBtn = (Button) view.findViewById(R.id.submitPhoneBtn);
		submitPhoneBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String code = messageCodeEditText.getText().toString().trim();
				if (TextUtils.isEmpty(code)){
					Toast.makeText(context,"验证码输入为空",Toast.LENGTH_LONG).show();
				}
				AsyncHttpClient client = new AsyncHttpClient();
				//保存cookie，自动保存到了shareprefercece,自动保存，自动使用
				PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
				client.setCookieStore(myCookieStore);
				RequestParams params = new RequestParams();
				params.put("code",code);
				client.delete(context,ConstantParamPhone.DEL_BANKCARD+comeinData.getId(),null, params, new TextHttpResponseHandler() {
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
								Toast.makeText(context,"解绑成功",Toast.LENGTH_LONG).show();
								finish();
								//回到银行卡列表
								startActivity(new Intent(context,ShopBankListActivity.class));
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


		});




        popupWindow.setFocusable(true);  
        popupWindow.setOutsideTouchable(true);  
        //这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景  
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000)); 
        //设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAsDropDown(ShopUserBankInfoActivity.this.findViewById(R.id.head));
        LinearLayout dismissLayout = (LinearLayout) view.findViewById(R.id.dismissLayout);
        dismissLayout.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View v)
			{
				popupWindow.dismiss();
				AlphaAnimation animation = new AlphaAnimation((float)1, (float)0);
				animation.setDuration(500); //设置持续时间5秒
				animation.setAnimationListener(new AnimationListener() 
				{
					
					@Override
					public void onAnimationStart(Animation animation) 
					{
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) 
					{
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) 
					{
						// TODO Auto-generated method stub
					}
				});
			}
		});

	}
	/**
	 * 显示剩余秒数
	 */
	private void showResidueSeconds() {
		//显示倒计时按钮
		new CountDownTimer(60*1000,1000){

			@Override
			public void onTick(long lastTime) {
				//倒计时执行的方法
				btnGetmess.setClickable(false);
				btnGetmess.setFocusable(false);
				btnGetmess.setText(lastTime/1000+"秒后重发");
				btnGetmess.setTextColor(getResources().getColor(R.color.text_hint_color));
				btnGetmess.setBackgroundResource(R.drawable.btn_bg_hint);
				// LogUtil.d("lasttime","剩余时间:"+lastTime/1000);
			}

			@Override
			public void onFinish() {
				btnGetmess.setClickable(true);
				btnGetmess.setFocusable(true);
				btnGetmess.setText("重新获取");

			}
		}.start();
	}

}
