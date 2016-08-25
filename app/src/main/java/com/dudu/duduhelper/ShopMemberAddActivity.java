package com.dudu.duduhelper;

import org.apache.http.Header;

import com.dudu.duduhelper.bean.MemberDataBean;
import com.dudu.duduhelper.bean.MemberDetailBean;
import com.dudu.duduhelper.bean.ResponsBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.dudu.duduhelper.widget.PopWindowList;
import com.dudu.duduhelper.widget.PopWindowList.OnItemClickLinster;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ShopMemberAddActivity extends BaseActivity 
{
	private EditText membername;
	private EditText membercount;
	private EditText memberpassword;
	private Button editMemberbutton;
	private String title="";
	private MemberDataBean memberDataBean;
	private String methord="";
	private TextView memberShopTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_member_add);
		memberDataBean=(MemberDataBean) getIntent().getSerializableExtra("account");
		initView();
		if(memberDataBean!=null)
		{
			methord=ConstantParamPhone.EDIT_MEMBER;;
			title="修改店员账号";
			initData();
		}
		else
		{
			methord=ConstantParamPhone.ADD_MEMBER;
			title="添加店员账号";
		}
		initHeadView(title, true, false, 0);
	}

	

	private void initView() 
	{
		// TODO Auto-generated method stub
		memberShopTextView = (TextView) this.findViewById(R.id.memberShopTextView);
		memberShopTextView.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//弹出店铺列表
				showShopListPopWindow();
				
			}

		});
		membername=(EditText) this.findViewById(R.id.membername);
		membercount=(EditText) this.findViewById(R.id.membercount);
		memberpassword=(EditText) this.findViewById(R.id.memberpassword);
		editMemberbutton=(Button) this.findViewById(R.id.editMemberbutton);
		editMemberbutton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				upDateData();
			}
		});
	}
	private void initData() 
	{
		// TODO Auto-generated method stub

		ColorDialog.showRoundProcessDialog(ShopMemberAddActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		//params.add("category",category);
		params.add("id",memberDataBean.getId());
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(ShopMemberAddActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.MEMBER_DETAIL, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(ShopMemberAddActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				MemberDetailBean productDetailBean=new Gson().fromJson(arg2,MemberDetailBean.class);
				if(productDetailBean.getStatus().equals("-1006"))
				{
					//Toast.makeText(ProductListActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
					//Toast.makeText(getActivity(), "出错啦！", Toast.LENGTH_SHORT).show();
					MyDialog.showDialog(ShopMemberAddActivity.this, "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(ShopMemberAddActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				if(!productDetailBean.getStatus().equals("1"))
				{
					Toast.makeText(ShopMemberAddActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
				}
				else
				{
					if(productDetailBean.getData()!=null)
					{
						if(!TextUtils.isEmpty(productDetailBean.getData().getNickname()))
						{
							membername.setText(productDetailBean.getData().getNickname());
						}
						if(!TextUtils.isEmpty(productDetailBean.getData().getUsername()))
						{
							membercount.setText(productDetailBean.getData().getUsername());
						}
						if(!TextUtils.isEmpty(productDetailBean.getData().getPassword()))
						{
							memberpassword.setText(productDetailBean.getData().getPassword());
						}
					}
					else
					{
						Toast.makeText(ShopMemberAddActivity.this, "暂无数据！", Toast.LENGTH_SHORT).show();
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
	private void upDateData() 
	{
		// TODO Auto-generated method stub
		if(TextUtils.isEmpty(membername.getText().toString()))
		{
			Toast.makeText(ShopMemberAddActivity.this, "请输入用户姓名", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(membercount.getText().toString()))
		{
			Toast.makeText(ShopMemberAddActivity.this, "请输入用户账号", Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(memberpassword.getText().toString()))
		{
			Toast.makeText(ShopMemberAddActivity.this, "请输入用户密码", Toast.LENGTH_SHORT).show();
			return;
		}
		ColorDialog.showRoundProcessDialog(ShopMemberAddActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		if(memberDataBean!=null)
		{
			params.add("id",memberDataBean.getId());
		}
		params.add("username",membercount.getText().toString().trim());
		params.add("password",memberpassword.getText().toString().trim());
		params.add("nickname",membername.getText().toString().trim());
		params.add("version", ConstantParamPhone.VERSION);
		//params.add("description",productDetaliTextView.getText().toString().trim());
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(ShopMemberAddActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.post(ConstantParamPhone.IP+methord, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(ShopMemberAddActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				ResponsBean responsBean=new Gson().fromJson(arg2,ResponsBean.class);
				if(!responsBean.getStatus().equals("1"))
				{
					Toast.makeText(ShopMemberAddActivity.this, responsBean.getInfo(), Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(ShopMemberAddActivity.this, "操作成功啦", Toast.LENGTH_SHORT).show();
					Intent intent=new Intent();  
	                setResult(RESULT_OK, intent);  
	                finish();
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
	
	//创建店铺列表弹窗
	private void showShopListPopWindow() 
	{
		//今天该写店铺选择弹框列表啦
		final PopWindowList popListVindow = new PopWindowList(ShopMemberAddActivity.this);
		popListVindow.setOnItemClickLinster(new OnItemClickLinster() 
		{
			@Override
			public void onClick(String shopName) 
			{
				memberShopTextView.setText(shopName);
				popListVindow.dismiss();
			}
		});
	}
	
}
