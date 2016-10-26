package com.dudu.helper3.Activity.DiscountCardActivity;

import org.apache.http.Header;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.Activity.WelcomeActivity.LoginActivity;
import com.dudu.duduhelper.R;
import com.dudu.helper3.bean.CouponHistoryBean;
import com.dudu.helper3.Utils.Util;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.widget.ColorDialog;
import com.dudu.helper3.widget.MyDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class CouponSellDetailActivity extends BaseActivity 
{
	private TextView couponname;
	private TextView couponnum;
	private TextView couponnickname;
	private TextView couponstatus;
	private TextView coupontime;
	private TextView couponendtime;
	private TextView couponexpertime;
	private String id;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon_sell_detail);
		initHeadView("使用详情", true, true, R.id.icon_card);
		initView();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		id=getIntent().getStringExtra("id");
		couponname=(TextView) this.findViewById(R.id.couponname);
		couponnum=(TextView) this.findViewById(R.id.couponnum);
		couponnickname=(TextView) this.findViewById(R.id.couponnickname);
		couponstatus=(TextView) this.findViewById(R.id.couponstatus);
		coupontime=(TextView) this.findViewById(R.id.coupontime);
		couponendtime=(TextView) this.findViewById(R.id.couponendtime);
		couponexpertime=(TextView) this.findViewById(R.id.couponexpertime);
	}
	
	private void initData() 
	{
		// TODO Auto-generated method stub
		ColorDialog.showRoundProcessDialog(this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		//params.add("category",category);
		params.add("id",id);
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(CouponSellDetailActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.GET_COUPON_RECORD_INFO, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(CouponSellDetailActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				CouponHistoryBean productDetailBean=new Gson().fromJson(arg2,CouponHistoryBean.class);
				if(productDetailBean.getStatus().equals("-1006"))
				{
					MyDialog.showDialog(CouponSellDetailActivity.this, "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() 
					{
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(CouponSellDetailActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				if(!productDetailBean.getStatus().equals("1"))
				{
					Toast.makeText(CouponSellDetailActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
				}
				else
				{
					if(productDetailBean.getData()!=null)
					{
//						couponPriceEditText.setText(productDetailBean.getData().getFee());
						couponname.setText(productDetailBean.getData().getSubject());
						couponnum.setText(productDetailBean.getData().getSn());
						couponnickname.setText(productDetailBean.getData().getNickname());
						coupontime.setText(Util.DataConVert2(productDetailBean.getData().getGettime()));//领取时间
						couponendtime.setText(Util.DataConVert2(productDetailBean.getData().getExptime()));//过期时间
						couponexpertime.setText(Util.DataConVert2(productDetailBean.getData().getVerifytime()));//验证时间
						if(!TextUtils.isEmpty(productDetailBean.getData().getVerifytime()))
						{
							if(productDetailBean.getData().getVerifytime().equals("0"))
							{
								if(!TextUtils.isEmpty(productDetailBean.getData().getExptime()))
								{
									if((System.currentTimeMillis()-Long.parseLong(productDetailBean.getData().getExptime()))>0)
									{
										couponstatus.setText("已过期");
										couponstatus.setTextColor(couponstatus.getResources().getColor(R.color.text_color_light));
										couponexpertime.setText("已过期");
									}
									else
									{
										couponstatus.setText("未验证");
										couponstatus.setTextColor(couponstatus.getResources().getColor(R.color.text_color_yellow));
										couponexpertime.setText("已过期");
									}
								}
								
							}
							if(!productDetailBean.getData().getVerifytime().equals("0"))
							{
								couponstatus.setText("已验证");
								couponstatus.setTextColor(couponstatus.getResources().getColor(R.color.text_color));
							}
						}
					}
					else
					{
						Toast.makeText(CouponSellDetailActivity.this, "暂无数据！", Toast.LENGTH_SHORT).show();
					}
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
}
