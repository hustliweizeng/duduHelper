package com.dudu.helper3.Activity.EmployeeManageActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.dudu.helper3.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.adapter.ShopListSelectAdapter;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.ShopListBean;
import com.dudu.helper3.javabean.ShopUserBean;
import com.dudu.helper3.javabean.ShopUserDetaiBean;
import com.dudu.helper3.widget.ColorDialog;
import com.dudu.helper3.widget.MyAlertDailog;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ShopMemberAddActivity extends BaseActivity 
{
	private EditText membername;
	private EditText membercount;
	private EditText memberpassword;
	private Button editMemberbutton;
	private String title="";
	private ShopUserBean.DataBean memberDataBean;
	private TextView memberShopTextView;
	private String url;
	private String shop_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_member_add);
		memberDataBean=(ShopUserBean.DataBean) getIntent().getSerializableExtra("detail");
		initView();
		
		if(memberDataBean!=null)
		{
			title="修改店员账号";
			url = ConstantParamPhone.GET_USER_DETAIL+memberDataBean.getId();
			initData();
		}
		else
		{
			title="添加店员账号";
			url = ConstantParamPhone.ADD_USER;
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
		ColorDialog.showRoundProcessDialog(ShopMemberAddActivity.this,R.layout.loading_process_dialog_color);
        HttpUtils.getConnection(context,null,url, "get",new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(ShopMemberAddActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						ShopUserDetaiBean detail = new Gson().fromJson(arg2, ShopUserDetaiBean.class);
						ShopUserDetaiBean.DataBean dataBean = detail.getData();
						if(dataBean!=null)
						{
							if(!TextUtils.isEmpty(dataBean.getNickname()))
							{
								membername.setText(dataBean.getNickname());
							}
							if(!TextUtils.isEmpty(dataBean.getName()))
							{
								membercount.setText(dataBean.getName());
							}
							//密码马赛克
							memberpassword.setText("****");
							memberShopTextView.setText(dataBean.getShop_name());
						}
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
	//修改数据
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
		//如果是修改界面
		if(memberDataBean!=null)
		{
			//如果修改了所属店铺
			if (shop_id!=null){
				params.add("shop_id",shop_id);
			}else {
				//没修改还用之前数据
				params.add("shop_id",memberDataBean.getId());
			}
			//新增页面
		}else {
			params.add("name",membername.getText().toString());
			params.add("shop_id",shop_id);
		}
		params.add("plaintextPassword",memberpassword.getText().toString().trim());
		params.add("nickname",membername.getText().toString().trim());
        HttpUtils.getConnection(context,params,url, "post",new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(ShopMemberAddActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						Toast.makeText(context,"修改成功",Toast.LENGTH_SHORT).show();
						finish();
						//数据请求成功
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
	
	//店铺列表弹窗
	private void showShopListPopWindow() 
	{
		//请求网络数据获取店铺信息/api/app/shop_user/shops
		HttpUtils.getConnection(context, null, ConstantParamPhone.GET_SHOPABLE, "GET", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context, "网络异常，稍后再试", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				try {
					JSONObject object = new JSONObject(s);
					String code = object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)) {
						//数据请求成功
						ShopListBean data = new Gson().fromJson(s, ShopListBean.class);
						showCategorySelctor(data.getData(), "选择店铺");
					} else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
	}
				//显示行业选择框
	private void showCategorySelctor(final List<ShopListBean.DataBean> category, final String title) {
		ShopListSelectAdapter adapter = new ShopListSelectAdapter(context,R.layout.item_circle_select);
		adapter.addAll(category);
		LogUtil.d("adapter",adapter.getCount()+"");
		MyAlertDailog.show(context,title,adapter );
		//通过接口回调，确认选择的条目，并展示出来
		MyAlertDailog.setOnItemClickListentner(new MyAlertDailog.OnItemClickListentner() {
			@Override
			public void Onclick(int poistion) {
				//设置选中店铺
				memberShopTextView.setText(category.get(poistion).getName());
				//设置选中的行业id
				shop_id = Integer.parseInt(category.get(poistion).getId())+"";
			}
		});

	}
	
}
