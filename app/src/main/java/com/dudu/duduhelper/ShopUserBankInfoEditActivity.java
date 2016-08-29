package com.dudu.duduhelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.BankCardListBean;
import com.dudu.duduhelper.javabean.ProvinceListBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

public class ShopUserBankInfoEditActivity extends BaseActivity 
{
	private LinearLayout kaihuBanklin;
	private LinearLayout kaihuPriviencelin;
	private LinearLayout userBankCityLin;
	private TextView userBankNameTextView;
	private TextView provienceTextView;
	private TextView userBankCityTextView;
	private String provienceCode;
	private String cityCode;
	private Button savebutton;
	private EditText userBankNameEditText;
	private EditText userBankNumEditText;
	private EditText userBankSonNameEditText;
	private Button btnGetmess;
	private EditText messageCodeEditText;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_user_bank_info_edit);
		initHeadView("我的银行卡", true, false, 0);
		initView();
		initData();
		
	}

	//提交信息
	private void SaveData() 
	{
		if(TextUtils.isEmpty(userBankNameEditText.getText().toString().trim()))
		{
			Toast.makeText(ShopUserBankInfoEditActivity.this, "请输入开户姓名", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(userBankNumEditText.getText().toString().trim()))
		{
			Toast.makeText(ShopUserBankInfoEditActivity.this, "请输入银行卡号", Toast.LENGTH_SHORT).show();
			return;
		}
		
		if(TextUtils.isEmpty(userBankNameTextView.getText().toString().trim()))
		{
			Toast.makeText(ShopUserBankInfoEditActivity.this, "请输选择开户银行", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(provienceTextView.getText().toString().trim()))
		{
			Toast.makeText(ShopUserBankInfoEditActivity.this, "请选择开户银行所在省份", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(userBankCityTextView.getText().toString().trim()))
		{
			Toast.makeText(ShopUserBankInfoEditActivity.this, "请选择开户行所在城市", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(userBankSonNameEditText.getText().toString().trim()))
		{
			Toast.makeText(ShopUserBankInfoEditActivity.this, "请选择开户行所在城市", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(messageCodeEditText.getText().toString().trim()))
		{
			Toast.makeText(ShopUserBankInfoEditActivity.this, "请输入验证码", Toast.LENGTH_SHORT).show();
			return;
		}
		/*array:7 [▼
		"bank_key" => "银行名称"
		"bank_name" => "分行信息"
		"card_number" => "银行卡号"
		"province_id" => "省份ID"
		"city_id" => "城市ID"
		"name" => "持卡人姓名"
		"code" => "验证码"
		]*/
		ColorDialog.showRoundProcessDialog(ShopUserBankInfoEditActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("name",userBankNameEditText.getText().toString().trim());
		params.add("bank_key",userBankNameTextView.getText().toString().trim());
		params.add("card_number",userBankNumEditText.getText().toString().trim());
		params.add("province_id",provienceTextView.getText().toString().trim());
		params.add("city_id",userBankCityTextView.getText().toString().trim());
		params.add("bank_name",userBankSonNameEditText.getText().toString().trim());
		params.add("code",messageCodeEditText.getText().toString().trim());
		HttpUtils.getConnection(context,params,ConstantParamPhone.CHANGE_BANKCARD_INFO, "POST",new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(ShopUserBankInfoEditActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				Toast.makeText(context,"修改银行卡成功",Toast.LENGTH_LONG).show();
				finish();

			}
			@Override
			public void onFinish() 
			{
				ColorDialog.dissmissProcessDialog();
			}
		});
	}

	private void initData()
	{
		//从编辑要么传递过来的数据
		BankCardListBean.DataBean info = (BankCardListBean.DataBean) getIntent().getSerializableExtra("info");
		if (info != null){
			//显示默认数据
			//用户名
			if(!TextUtils.isEmpty(info.getBank_name()))
			{
				userBankNameEditText.setHint(info.getName());
			}
			//银行卡号
			if(!TextUtils.isEmpty(info.getCard_number()))
			{
				userBankNumEditText.setHint(info.getCard_number());
			}
			//银行名称
			if(!TextUtils.isEmpty(info.getBank_name()))
			{
				userBankNameTextView.setText(info.getBank_name());
			}
			//省份
			if(!TextUtils.isEmpty(info.getProvince_id()))
			{
				provienceTextView.setText(info.getProvince_id());
			}
			//城市
			if(!TextUtils.isEmpty(info.getCity_id()))
			{
				userBankCityTextView.setText(info.getCity_id());
			}
			//支行名称
			if(!TextUtils.isEmpty(info.getBank_name()))
			{
				userBankSonNameEditText.setText(share.getString("moreinfo", ""));
			}
		}
	}
	
	@Override
	//点击左侧退出的时候，弹出对话框
	public void LeftButtonClick() 
	{
		//任意条目不为空时
		if(!(TextUtils.isEmpty(userBankNameEditText.getText().toString().trim()))||
				!(TextUtils.isEmpty(userBankNumEditText.getText().toString().trim()))||
				!(userBankNameTextView.getText().toString().trim()).equals(share.getString("bankname", ""))||
				!(provienceTextView.getText().toString().trim()).equals(share.getString("province", ""))||
				!(userBankCityTextView.getText().toString().trim()).equals(share.getString("city", ""))||
				!(userBankSonNameEditText.getText().toString().trim()).equals(share.getString("moreinfo", "")))
		{
			if(!(userBankNameEditText.getText().toString().trim()).equals(share.getString("truename", ""))||!(userBankNumEditText.getText().toString().trim()).equals(share.getString("bankno", "")))
			{
				MyDialog.showDialog(ShopUserBankInfoEditActivity.this, "您的银行卡信息已发生变更，是否保存更改", true, true, "取消","保存",new OnClickListener()
				{
					
					@Override
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub
						MyDialog.cancel();
						finish();
					}
				}, new OnClickListener() 
				{
					
					@Override
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub
						MyDialog.cancel();
						SaveData();
					}
				});
			}
			else
			{
				super.LeftButtonClick();
			}
		}
		else
		{
			super.LeftButtonClick();
		}
				
	}

	private void initView() 
	{
		// TODO Auto-generated method stub
		messageCodeEditText=(EditText) this.findViewById(R.id.messageCodeEditText);

		btnGetmess=(Button) this.findViewById(R.id.btnGetmess);
		btnGetmess.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// 获取验证码
				getMessage();
				//showResidueSeconds();
			}
		});
		userBankSonNameEditText=(EditText) this.findViewById(R.id.userBankSonNameEditText);
		userBankNumEditText=(EditText) this.findViewById(R.id.userBankNumEditText);
		userBankNameEditText=(EditText) this.findViewById(R.id.userBankNameEditText);
		savebutton=(Button) this.findViewById(R.id.savebutton);
		userBankCityTextView=(TextView) this.findViewById(R.id.userBankCityTextView);
		provienceTextView=(TextView) this.findViewById(R.id.provienceTextView);
		userBankNameTextView=(TextView) this.findViewById(R.id.userBankNameTextView);
		kaihuBanklin=(LinearLayout) this.findViewById(R.id.kaihuBanklin);
		kaihuPriviencelin=(LinearLayout) this.findViewById(R.id.kaihuPriviencelin);
		userBankCityLin=(LinearLayout) this.findViewById(R.id.userBankCityLin);
		TextView myphonenum = (TextView) findViewById(R.id.myphonenum);
		myphonenum.setText(sp.getString("mobile","请先绑定手机"));
		//提交编辑后的信息
		savebutton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				//保存用户信息
				SaveData();
			}
		});
		//选择城市
		userBankCityLin.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(provienceCode))
				{
					Toast.makeText(ShopUserBankInfoEditActivity.this, "请先选择开户行省份",  Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent=new Intent(ShopUserBankInfoEditActivity.this,UserBankSelectActivity.class);
				intent.putExtra("action", "city");
				intent.putExtra("provienceCode", provienceCode);
				startActivityForResult(intent, 3);
			}
		});
		//选择省份
		kaihuPriviencelin.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(userBankNameTextView.getText().toString().trim()))
				{
					Toast.makeText(ShopUserBankInfoEditActivity.this, "请先选择开户行", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent=new Intent(context,UserBankSelectActivity.class);
				intent.putExtra("action", "province");
				startActivityForResult(intent, 2);
			}
		});
		kaihuBanklin.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(ShopUserBankInfoEditActivity.this,UserBankSelectActivity.class);
				intent.putExtra("action", "bank");
				startActivityForResult(intent, 1);
			}
		});
	}
	  @Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	  {
		  if(data!=null)
		  {
			  
	          switch(requestCode)
	          {
	              case 1:
	              //来自按钮1的请求，作相应业务处理
	        	  if(!TextUtils.isEmpty(data.getStringExtra("bank")))
	        	  {
	        		  userBankNameTextView.setText(data.getStringExtra("bank"));
	        	  }
	        	  break;
	              case 2:
	              //来自按钮2的请求，作相应业务处理
	              {
					  //省份列表
					  provienceCode = ((ProvinceListBean.DataBean)data.getSerializableExtra("province")).getCode();
	            	  provienceTextView.setText(((ProvinceListBean.DataBean)data.getSerializableExtra("province")).getName());
	            	  userBankCityTextView.setText(null);
	              }
	              break;
	              case 3:
	              //来自按钮2的请求，作相应业务处理
	              {
	            	  cityCode=((ProvinceListBean.DataBean)data.getSerializableExtra("city")).getId();
	            	  userBankCityTextView.setText(((ProvinceListBean.DataBean)data.getSerializableExtra("city")).getName());
	              }
	              break;
	           }
		  }
     }
	//获取短信验证码
	  private void getMessage() 
		{
			RequestParams params = new RequestParams();
			params.add("mobile",sp.getString("mobile", ""));
			params.add("type", "changebank");
			HttpUtils.getConnection(context,params,ConstantParamPhone.GET_SMS_CONFIRM, "GET",new TextHttpResponseHandler(){

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
				{
					Toast.makeText(ShopUserBankInfoEditActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
					//showResidueSeconds();
				}
				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2) 
				{
					Toast.makeText(ShopUserBankInfoEditActivity.this, "已发送短信验证，15分钟内有效", Toast.LENGTH_LONG).show();
					showResidueSeconds();
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
