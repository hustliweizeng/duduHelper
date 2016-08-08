package com.dudu.duduhelper;

import android.os.Bundle;
import android.text.InputType;
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
		if(!TextUtils.isEmpty(share.getString("mobile", "")))
		{
			bindPhoneText.setText(share.getString("mobile", ""));
		}
		else
		{
			bindPhoneText.setText("请先绑定手机号");
		}
		passWordEdit = (EditText) this.findViewById(R.id.passWordEdit);
		showPasswordImageBtn = (ImageView) this.findViewById(R.id.showPasswordImageBtn);
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
		phoneBindButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				phoneLayout.setVisibility(View.VISIBLE);
				passwordLayout.setVisibility(View.GONE);
				phoneBindButton.setBackgroundColor(phoneBindButton.getResources().getColor(R.color.bg_white_color));
				phoneBindButton.setTextColor(phoneBindButton.getResources().getColor(R.color.text_dark_color));
				passwordBindButton.setBackgroundColor(passwordBindButton.getResources().getColor(R.color.bg_color));
				passwordBindButton.setTextColor(passwordBindButton.getResources().getColor(R.color.text_middledark_color));
			}
		});
		passwordBindButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				passwordLayout.setVisibility(View.VISIBLE);
				phoneLayout.setVisibility(View.GONE);
				passwordBindButton.setBackgroundColor(phoneBindButton.getResources().getColor(R.color.bg_white_color));
				passwordBindButton.setTextColor(phoneBindButton.getResources().getColor(R.color.text_dark_color));
				phoneBindButton.setBackgroundColor(passwordBindButton.getResources().getColor(R.color.bg_color));
				phoneBindButton.setTextColor(passwordBindButton.getResources().getColor(R.color.text_middledark_color));
			}
		});
	}

}
