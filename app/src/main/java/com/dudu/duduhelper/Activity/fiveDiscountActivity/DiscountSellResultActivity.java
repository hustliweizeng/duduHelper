package com.dudu.duduhelper.Activity.fiveDiscountActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.FiveDiscountBean;
import com.dudu.duduhelper.widget.MyDialog;
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
import com.dudu.duduhelper.R;
public class DiscountSellResultActivity extends BaseActivity 
{
	private FiveDiscountBean dataBean;
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
	private FiveDiscountBean data;
	private String id;
	//private String status;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_discount_sell_result);
		initHeadView("五折卡验证", true, false, 0);
		data = (FiveDiscountBean) getIntent().getSerializableExtra("data");
		id = getIntent().getStringExtra("id");
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
		if (data.getCode().equalsIgnoreCase("SUCCESS")){

			discountSellStatus.setText("验证通过");
			discountSellresultText.setText("会员身份确认，请放心使用");
			dataBean=(FiveDiscountBean) getIntent().getSerializableExtra("data");
			initData();
		}else
		{
			discountSellStatus.setText("验证失败");
			discountSubmitbutton.setText("重新验证");
			memberinfoline.setVisibility(View.GONE);
			memberinfoContentline.setVisibility(View.GONE);
			discountSubmitbutton.setText("重新输入");
			/*if(!TextUtils.isEmpty(getIntent().getStringExtra("info")))
			{
				discountSellresultText.setText(getIntent().getStringExtra("info").split(" ")[1]);
			}*/
		}
		discountSubmitbutton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				if(data.getCode().equalsIgnoreCase("SUCCESS"))
				{
					MyDialog.showDialog(DiscountSellResultActivity.this,
							"每张“五折卡”只可在指定商家单日、单次使用，当日不可重读使用。确定要用五折卡么？", true, true, "取消", "使用", new OnClickListener()
					{
						@Override
						public void onClick(View v) 
						{
							MyDialog.cancel();
						}
					},
					new OnClickListener() 
					{
						
						@Override
						public void onClick(View v) 
						{
							submitData();//可以使用时，提交使用
						}
					});
				}
				else
				{
					DiscountSellResultActivity.this.finish();//不能使用时结束该页面
				}
			}

		});
	}
	
	private void submitData() 
	{
		RequestParams params = new RequestParams();
		params.add("code",id);
        HttpUtils.getConnection(context,params, ConstantParamPhone.USE_CARD, "post",new TextHttpResponseHandler()
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
		});
	}
	

	private void initData() 
	{

		if(!TextUtils.isEmpty(id))//卡号
		{
			discountNumTextView.setText(id);
		}
		if(!TextUtils.isEmpty(dataBean.getData().getTruename()))
		{
			discountNameTextView.setText(dataBean.getData().getTruename());
		}
		if(!TextUtils.isEmpty(dataBean.getData().getSex()))
		{
			   discountSexTextView.setText(dataBean.getData().getSex());
		}
		if(!TextUtils.isEmpty(dataBean.getData().getBirth()))
		{
			discountBirthTextView.setText(dataBean.getData().getBirth());
		}
		if(!TextUtils.isEmpty(dataBean.getData().getCity()))
		{
			discountCityTextView.setText(dataBean.getData().getCity());
		}
		if(!TextUtils.isEmpty(dataBean.getData().getMobile()))
		{
			discountPhoneTextView.setText(dataBean.getData().getMobile());
		}
		if(!TextUtils.isEmpty(dataBean.getData().getExp_time()))
		{
			discountVIPExperTimeTextView.setText((dataBean.getData().getExp_time()));
		}
	}

}
