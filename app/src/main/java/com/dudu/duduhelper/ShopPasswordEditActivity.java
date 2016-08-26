package com.dudu.duduhelper;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

public class ShopPasswordEditActivity extends BaseActivity 
{
    private LinearLayout passwordLayout;
    private LinearLayout phoneLayout;
    private Button phoneBindButton;
    private Button passwordBindButton;
    private TextView textView;
    private TextView textView1;
    private ImageView showPasswordImageBtn;
    private EditText passWordEdit;
    private TextView bindPhoneText;
    private boolean showPass = false;
	private Button btnGetmess;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_password_edit);
		initHeadView("密码管理", true, false, 0);
		initView();
	}

	private void initView() 
	{
		bindPhoneText = (TextView) this.findViewById(R.id.bindPhoneText);
		//获取本地保存的电话号码
		String mobile = sp.getString("mobile","");
		if(!TextUtils.isEmpty(mobile))
		{
			bindPhoneText.setText(mobile);
		}
		else
		{
			bindPhoneText.setText("请先绑定手机号");
			//绑定手机号页面
			bindPhoneText.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					startActivity(new Intent(context,ShopRebindPhoneSteponeActivity.class));
				}
			});
		}
		passWordEdit = (EditText) this.findViewById(R.id.passWordEdit);
		showPasswordImageBtn = (ImageView) this.findViewById(R.id.showPasswordImageBtn);
		//是否隐藏密码
		showPasswordImageBtn.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(showPass)
				{
					//现在按钮是打开状态，密码可见,点击需要隐藏
					showPasswordImageBtn.setImageResource(R.drawable.icon_yanjing);
					passWordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					showPass = false;
					
				}
				else
				{
					//现在是关闭状态，密码不可见
					showPasswordImageBtn.setImageResource(R.drawable.icon_biyan);
					passWordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
					showPass = true;
				}
			}
		});
		//设置TextView显示不同的颜色
		textView = (TextView) this.findViewById(R.id.textView);
		textView1 = (TextView) this.findViewById(R.id.textView1);
		SpannableStringBuilder builder = new SpannableStringBuilder(textView.getText().toString());  
		ForegroundColorSpan greenSpan = new ForegroundColorSpan(0xFF3DD6BC);
		builder.setSpan(greenSpan, textView.getText().toString().length()-12, textView.getText().toString().length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
		textView.setText(builder);  
		textView1.setText(builder);  
		
		passwordLayout = (LinearLayout) this.findViewById(R.id.passwordLayout);
		phoneLayout = (LinearLayout) this.findViewById(R.id.phoneLayout);
		phoneBindButton = (Button) this.findViewById(R.id.phoneBindButton);
		passwordBindButton = (Button) this.findViewById(R.id.passwordBindButton);
		btnGetmess = (Button) findViewById(R.id.btnGetmess);
		//发送短信验证
		btnGetmess.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				RequestParams params = new RequestParams();
				params.put("mobile",sp.getString("mobile","18937228893"));
				String url = ConstantParamPhone.BASE_URL+ConstantParamPhone.GET_SMS_CONFIRM;

				HttpUtils.getConnection(context, params, url, "get", new TextHttpResponseHandler() {
					@Override
					public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
						Toast.makeText(context,"短信发送失败",Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(int i, Header[] headers, String s) {
						//显示剩余有效时间
						showResidueSeconds();

					}
				});
			}
		});
		//手机绑定界面
		phoneBindButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				phoneLayout.setVisibility(View.VISIBLE);
				passwordLayout.setVisibility(View.GONE);
				phoneBindButton.setBackgroundColor(phoneBindButton.getResources().getColor(R.color.bg_white_color));
				phoneBindButton.setTextColor(phoneBindButton.getResources().getColor(R.color.text_dark_color));
				passwordBindButton.setBackgroundColor(passwordBindButton.getResources().getColor(R.color.bg_color));
				passwordBindButton.setTextColor(passwordBindButton.getResources().getColor(R.color.text_middledark_color));
			}
		});
		//密码验证界面
		passwordBindButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				passwordLayout.setVisibility(View.VISIBLE);
				phoneLayout.setVisibility(View.GONE);
				passwordBindButton.setBackgroundColor(phoneBindButton.getResources().getColor(R.color.bg_white_color));
				passwordBindButton.setTextColor(phoneBindButton.getResources().getColor(R.color.text_dark_color));
				phoneBindButton.setBackgroundColor(passwordBindButton.getResources().getColor(R.color.bg_color));
				phoneBindButton.setTextColor(passwordBindButton.getResources().getColor(R.color.text_middledark_color));
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
                LogUtil.d("lasttime","剩余时间:"+lastTime/1000);
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
