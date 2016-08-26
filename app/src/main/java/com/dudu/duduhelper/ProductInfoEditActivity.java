package com.dudu.duduhelper;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProductInfoEditActivity extends BaseActivity 
{
	private EditText editContentEditTextView;
	private Button saveProductbutton;
	private String content;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_info_edit);
		initHeadView("商品描述",true, false, 0);
		content=getIntent().getStringExtra("content");
		initView();
	}

	private void initView() 
	{
		// TODO Auto-generated method stub
		editContentEditTextView=(EditText) this.findViewById(R.id.editContentEditTextView);
		editContentEditTextView.setText(content);
		saveProductbutton=(Button) this.findViewById(R.id.saveProductbutton);
		saveProductbutton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(editContentEditTextView.getText().toString().trim()))
				{
					Toast.makeText(ProductInfoEditActivity.this, "请输入商品描述", Toast.LENGTH_SHORT).show();
					return;
			
				}
				if(editContentEditTextView.getText().toString().length()>200)
				{
					Toast.makeText(ProductInfoEditActivity.this, "您输入的太多啦，超过200字啦！", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent=new Intent(); 
				intent.putExtra("descrip", editContentEditTextView.getText().toString());
                setResult(RESULT_OK, intent);  
                finish();
			}
		});
	}

}
