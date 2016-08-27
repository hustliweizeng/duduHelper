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

import com.dudu.duduhelper.bean.ProvienceBean;
import com.dudu.duduhelper.bean.ResponsBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
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
	private TimeCount time;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_user_bank_info_edit);
		initHeadView("我的银行卡", true, false, 0);
		time = new TimeCount(60000, 1000);//构造CountDownTimer对象
		initView();
		initData();
		
	}
	//短信验证倒计时
	class TimeCount extends CountDownTimer 
	{
		public TimeCount(long millisInFuture, long countDownInterval) 
		{
		    super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		@Override
		public void onFinish() 
		{
			//计时完毕时触发
			btnGetmess.setTextColor(btnGetmess.getResources().getColor(R.color.text_color_red));
			btnGetmess.setText("重新获取");
			btnGetmess.setClickable(true);
			btnGetmess.setPressed(false);
		}
		@Override
		public void onTick(long millisUntilFinished)
		{//计时过程显示
			btnGetmess.setTextColor(btnGetmess.getResources().getColor(R.color.title_color));
			btnGetmess.setClickable(false);
			btnGetmess.setPressed(true);
			btnGetmess.setText(millisUntilFinished /1000+"秒");
		}
	}
	//修改银行卡
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
		params.setContentEncoding("UTF-8");
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
		// TODO Auto-generated method stub
		if(!TextUtils.isEmpty(share.getString("truename", "")))
		{
			userBankNameEditText.setHint("*"+share.getString("truename", "").substring(1,share.getString("truename", "").length()));
		}
		if(!TextUtils.isEmpty(share.getString("bankno", "")))
		{
			userBankNumEditText.setHint("尾号:"+share.getString("bankno", "").substring(share.getString("bankno", "").length()-4, share.getString("bankno", "").length()));
		}
		if(!TextUtils.isEmpty(share.getString("bankname", "")))
		{
			userBankNameTextView.setText(share.getString("bankname", ""));
		}
		if(!TextUtils.isEmpty(share.getString("province", "")))
		{
			provienceTextView.setText(share.getString("province", ""));
		}
		if(!TextUtils.isEmpty(share.getString("city", "")))
		{
			userBankCityTextView.setText(share.getString("city", ""));
		}
		if(!TextUtils.isEmpty(share.getString("moreinfo", "")))
		{
			userBankSonNameEditText.setText(share.getString("moreinfo", ""));
		}
	}
	
	@Override
	public void LeftButtonClick() 
	{
		// TODO Auto-generated method stub

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
		savebutton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				//保存用户信息
				SaveData();
			}
		});

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
				Intent intent=new Intent(ShopUserBankInfoEditActivity.this,UserBankSelectActivity.class);
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
	            	  provienceCode=((ProvienceBean)data.getSerializableExtra("province")).getId();
	            	  provienceTextView.setText(((ProvienceBean)data.getSerializableExtra("province")).getName());
	            	  userBankCityTextView.setText(null);
	              }
	              break;
	              case 3:
	              //来自按钮2的请求，作相应业务处理
	              {
	            	  cityCode=((ProvienceBean)data.getSerializableExtra("city")).getId();
	            	  userBankCityTextView.setText(((ProvienceBean)data.getSerializableExtra("city")).getName());
	              }
	              break;
	           }
		  }
     }
	  private void getMessage() 
		{
			// TODO Auto-generated method stub
			// TODO Auto-generated method stub
			RequestParams params = new RequestParams();
			params.add("token", share.getString("token", ""));
			params.add("mobile",share.getString("mobile", ""));
			params.add("type", "changebank");
			params.add("version", ConstantParamPhone.VERSION);
			params.setContentEncoding("UTF-8");
			new AsyncHttpClient().post(ConstantParamPhone.IP+ConstantParamPhone.SEND_SMS, params,new TextHttpResponseHandler(){

				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
				{
					Toast.makeText(ShopUserBankInfoEditActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
				}
				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2) 
				{
					//获取短信发送状态
					ResponsBean responsBean=new Gson().fromJson(arg2, ResponsBean.class);
					if(responsBean.getStatus().equals("-1103"))
					{
						Toast.makeText(ShopUserBankInfoEditActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
						return;
					}
					if(responsBean.getStatus().equals("-3000"))
					{
						Toast.makeText(ShopUserBankInfoEditActivity.this, responsBean.getInfo(), Toast.LENGTH_LONG).show();
						return;
					}
					if(responsBean.getStatus().equals("1"))
					{
						time.start();
					}
					//开始计时
				}
				@Override
				public void onFinish() 
				{
					// TODO Auto-generated method stub
				}
			});
		}

}
