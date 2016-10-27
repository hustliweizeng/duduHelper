package com.dudu.helper3.Activity.fiveDiscountActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.application.DuduHelperApplication;
import com.dudu.helper3.bean.DiscountDataBean;
import com.dudu.helper3.Utils.Util;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.widget.ColorDialog;
import com.dudu.helper3.widget.MyDialog;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DiscountSellResultActivity extends BaseActivity 
{
	private DiscountDataBean dataBean;
	private TextView discountSellStatus;
	private TextView discountSellresultText;
	private TextView discountNumTextView;
	private TextView discountNameTextView;
	private TextView discountSexTextView;
	private TextView discountBirthTextView;
	private TextView discountCityTextView;
	private TextView discountPhoneTextView;
	private TextView discountVIPExperTimeTextView;
	private Button discountSubmitbutton;
	private LinearLayout memberinfoline;
	private LinearLayout memberinfoContentline;
	//private String status;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discount_sell_result);
		initHeadView("五折卡验证", true, false, 0);
		DuduHelperApplication.getInstance().addActivity(this);
		initView();
	}

	private void initView() 
	{
		memberinfoline=(LinearLayout) this.findViewById(R.id.memberinfoline);
		memberinfoContentline=(LinearLayout) this.findViewById(R.id.memberinfoContentline);
		discountSubmitbutton=(Button) this.findViewById(R.id.discountSubmitbutton);
		discountSellStatus=(TextView) this.findViewById(R.id.discountSellStatus);
		discountSellresultText=(TextView) this.findViewById(R.id.discountSellresultText);
		discountNumTextView=(TextView) this.findViewById(R.id.discountNumTextView);
		discountNameTextView=(TextView) this.findViewById(R.id.discountNameTextView);
		discountSexTextView=(TextView) this.findViewById(R.id.discountSexTextView);
		discountBirthTextView=(TextView) this.findViewById(R.id.discountBirthTextView);
		discountCityTextView=(TextView) this.findViewById(R.id.discountCityTextView);
		discountPhoneTextView=(TextView) this.findViewById(R.id.discountPhoneTextView);
		discountVIPExperTimeTextView=(TextView) this.findViewById(R.id.discountVIPExperTimeTextView);
		// TODO Auto-generated method stub
		if(getIntent().getIntExtra("status",1)==0)
		{
			discountSellStatus.setText("验证通过");
			discountSellresultText.setText("会员身份确认，请放心使用");
			dataBean=(DiscountDataBean) getIntent().getSerializableExtra("data");
			initData();
		}
		else
		{
			discountSellStatus.setText("验证失败");
			discountSubmitbutton.setText("重新验证");
			memberinfoline.setVisibility(View.GONE);
			memberinfoContentline.setVisibility(View.GONE);
			if(!TextUtils.isEmpty(getIntent().getStringExtra("info")))
			{
				discountSellresultText.setText(getIntent().getStringExtra("info").split(" ")[1]);
			}
		}
		discountSubmitbutton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				if(getIntent().getIntExtra("status",1)==0)
				{
				// TODO Auto-generated method stub
					MyDialog.showDialog(DiscountSellResultActivity.this, "每张“五折卡”只可在指定商家单日、单次使用，当日不可重读使用。确定要用五折卡么？", true, true, "取消", "使用", new OnClickListener() 
					{
						
						@Override
						public void onClick(View v) 
						{
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					},
					new OnClickListener() 
					{
						
						@Override
						public void onClick(View v) 
						{
							// TODO Auto-generated method stub
							
							submitData();
						}
					});
				}
				else
				{
					DiscountSellResultActivity.this.finish();
				}

			}

		});
	}
	
	private void submitData() 
	{
		ColorDialog.showRoundProcessDialog(DiscountSellResultActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
        HttpUtils.getConnection(context,params, ConstantParamPhone.USE_CARD, "PSOT",new TextHttpResponseHandler()
        {
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(DiscountSellResultActivity.this, "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						Toast.makeText(DiscountSellResultActivity.this, "使用成功", Toast.LENGTH_LONG).show();
						finish();
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				ColorDialog.dissmissProcessDialog();
			}
		});
	}
	

	private void initData() 
	{
		// TODO Auto-generated method stub
		if(!TextUtils.isEmpty(dataBean.getCode()))
		{
			discountNumTextView.setText(dataBean.getCode());
		}
		if(!TextUtils.isEmpty(dataBean.getNickname()))
		{
			discountNameTextView.setText(dataBean.getNickname());
		}
		if(!TextUtils.isEmpty(dataBean.getSex()))
		{
			if(dataBean.getSex().equals("1"))
			{
			   discountSexTextView.setText("男");
			}
			if(dataBean.getSex().equals("2"))
			{
			   discountSexTextView.setText("女");
			}
		}
		if(!TextUtils.isEmpty(dataBean.getBirthday()))
		{
			discountBirthTextView.setText(dataBean.getBirthday());
		}
		if(!TextUtils.isEmpty(dataBean.getCity()))
		{
			discountCityTextView.setText(dataBean.getCity());
		}
		if(!TextUtils.isEmpty(dataBean.getMobile()))
		{
			discountPhoneTextView.setText(dataBean.getMobile());
		}
		if(!TextUtils.isEmpty(dataBean.getCard_exp_time()))
		{
			discountVIPExperTimeTextView.setText(Util.DataConVertMint(dataBean.getCard_exp_time()));
		}
	}

}
