package com.dudu.duduhelper.Activity.EmployeeManageActivity;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.adapter.ShopListSelectAdapter;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.ShopListBean;
import com.dudu.duduhelper.javabean.ShopUserBean;
import com.dudu.duduhelper.javabean.ShopUserDetaiBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.AlertDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import com.dudu.duduhelper.R;
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
	private ShopListSelectAdapter adapter;
	private ShopListBean data;
	private ImageView iv_cancle;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_member_add);
		memberDataBean=(ShopUserBean.DataBean) getIntent().getSerializableExtra("detail");
		requestShopList();//请求店铺列表
		initView();

		if(memberDataBean!=null)
		{
			title="修改店员账号";
			url = ConstantParamPhone.GET_USER_DETAIL+memberDataBean.getId();//修改店员
			initData();//请求详情
		}
		else
		{
			title="添加店员账号";
			url = ConstantParamPhone.ADD_USER;//新增店员
		}
		initHeadView(title, true, false, 0);
	}

	

	private void initView() 
	{
		
		adapter = new ShopListSelectAdapter(context, R.layout.item_circle_multi_select);
		memberShopTextView = (TextView) this.findViewById(R.id.memberShopTextView);
		memberShopTextView.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//弹出店铺列表
				if(data!=null){
					showCategorySelctor(data.getData(),"选择店铺（可多选）");
				}else {
					Toast.makeText(context,"没有请求到店铺数据",Toast.LENGTH_SHORT).show();
				}
			}

		});
		membername=(EditText) this.findViewById(R.id.membername);
		membercount=(EditText) this.findViewById(R.id.membercount);
		if(memberDataBean!=null){
			membercount.setFocusable(false);
			membercount.setEnabled(false);
		}else {
			membercount.setFocusable(true);
			membercount.setEnabled(true);
		}
		memberpassword=(EditText) this.findViewById(R.id.memberpassword);
		editMemberbutton=(Button) this.findViewById(R.id.editMemberbutton);
		editMemberbutton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				upDateData();//提交信息
			}
		});
	}
	List<String> shopIds = new ArrayList<>();

	/**
	 * 获取员工详情
	 */
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
							memberpassword.setText("******");
							memberShopTextView.setText(dataBean.getShop_name());
							//选择的店铺id
							if (dataBean.getShops()!=null && dataBean.getShops().size()>0){
								for ( ShopUserDetaiBean.DataBean.ShopsBean item : dataBean.getShops()){
									shopIds.add(item.getId());
								}
								checkedIds = shopIds;//设置已选中id
							}
						}
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						LogUtil.d("msg",msg.toString());
						Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFinish() 
			{
				ColorDialog.dissmissProcessDialog();
			}
		});
	}
	//修改数据
	private void upDateData() 
	{
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
			//没修改还用之前数据
			if (checkedIds!=null &&checkedIds.size()>0){
				params.add("shop_id",checkedIds.toString());//已修改数据
			}else {
				params.add("shop_id",shopIds.toString());//没有修改数据
				LogUtil.d("id_old",shopIds.toString());
			}
			
			//新增页面
		}else {
			params.add("name",membercount.getText().toString());//账号名称
			params.add("shop_id",checkedIds.toString());
			LogUtil.d("id_new",checkedIds.toString());
		}
		//当密码修改过时才上传
		if (!"******".equals(memberpassword.getText().toString().trim())){
			params.add("plaintextPassword",memberpassword.getText().toString().trim());//密码
		}
		params.add("nickname",membername.getText().toString().trim());//账号
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
						Toast.makeText(context,"提交成功",Toast.LENGTH_SHORT).show();
						finish();
						//数据请求成功
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						LogUtil.d("msg",msg);
						if(msg.equals("名称 已经存在。")){
							Toast.makeText(context,"该账号已存在",Toast.LENGTH_LONG).show();
						}else {
							Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
						}
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFinish() 
			{
				ColorDialog.dissmissProcessDialog();
			}
		});
	}
	
	//店铺列表弹窗
	private void requestShopList() 
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
						data = new Gson().fromJson(s, ShopListBean.class);
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
	List<String> checkedIds = new ArrayList<>();
	List<String> checkedNames = new ArrayList<>();
	//显示可选店铺
	private void showCategorySelctor(final List<ShopListBean.DataBean> category, final String title) {
		if (shopIds!=null &&shopIds.size()>0){
			adapter.addCheckedIds(category,shopIds,true);//把所有店铺列表，已选中列表传递过去
		}{
			adapter.addAll(category);
		}
		final AlertDialog dailog = new AlertDialog.Builder(context).create();
		dailog.show();
		//获取window之前必须先show
		Window window = dailog.getWindow();
		window.setContentView(R.layout.alertdailog_multi_choose);
		iv_cancle = (ImageView) window.findViewById(R.id.iv_cancle);
		TextView tv_title_alertdailog = (TextView) window.findViewById(R.id.tv_title_alertdailog);
		ListView lv_alertdailog = (ListView) window.findViewById(R.id.lv_alertdailog);
		Button iv_canle_alertdailog = (Button) window.findViewById(R.id.iv_canle_alertdailog);

		tv_title_alertdailog.setText(title);
		lv_alertdailog.setAdapter(adapter);
		
		LogUtil.d("adapter", adapter.getCount()+"");
		//通过接口回调，确认选择的条目，并展示出来
		lv_alertdailog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String itemId = adapter.getItemId(position)+"";
				String name = category.get(position).getName();
				if (checkedIds.contains(itemId)){
					checkedIds.remove(itemId);
					checkedNames.remove(name);
				}else {
					checkedIds.add(itemId);
					checkedNames.add(name);
				}
				//设置选中店铺
				if (checkedIds!=null){
					memberShopTextView.setText(checkedNames.toString());
					LogUtil.d("ids",checkedIds.toString()+"=="+checkedNames.toString());
				}else {
					memberShopTextView.setHint("请选择所属店铺");
				}
				adapter.setcheckedId(position+"");
			}

		});
		iv_canle_alertdailog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dailog.dismiss();
			}
		});
		iv_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dailog.dismiss();
			}
		});
	}
	
}
