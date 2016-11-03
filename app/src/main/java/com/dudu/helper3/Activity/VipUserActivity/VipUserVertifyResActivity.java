package com.dudu.helper3.Activity.VipUserActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.VipDetailBean;
import com.dudu.helper3.widget.ColorDialog;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author
 * @version 1.0
 * @date 2016/11/3
 */

public class VipUserVertifyResActivity extends BaseActivity implements View.OnClickListener {
	private static final int VERTIFY_OK = 0;//验证成功
	private static final int VERTIFY_FAIL = 1;//验证失败
	private static final int USE_SUCCESS = 2;//使用成功
	private int res;
	private ImageView iv_res;
	private TextView tv_res;
	private TextView tv_name;
	private TextView tv_sex;
	private TextView tv_birth;
	private TextView tv_city;
	private TextView tv_phone;
	private TextView tv_expireTime;
	private TextView tv_discount;
	private Button btn_resubimit;
	private Button btn_use;
	private ImageView iv_res_other;
	private TextView tv_res_other;
	private TextView tv_content;
	private Button btn_sub;


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		res = getIntent().getIntExtra("res", -1);
		initLayout();//根据res结果加载不同布局
		initHeadView("会员卡验证", true, false, 0);
		initView();
		initData();
	}

	private void initLayout() {
		//加载不同的布局
		switch (res) {
			case VERTIFY_OK:
				setContentView(R.layout.activity_vip_vertify_res);
				break;
			case VERTIFY_FAIL:
			case USE_SUCCESS:
				setContentView(R.layout.activity_vip_vertify_res_fail);
				break;
			default:
				Toast.makeText(context, "服务器返回异常，请稍后再试", Toast.LENGTH_SHORT).show();
				break;
		}
	}

	private void initData() {
		switch (res){
			case VERTIFY_OK:
				//获取详情数据
				VipDetailBean data = (VipDetailBean) getIntent().getSerializableExtra("detail");
				if (data!=null){
					iv_res.setImageResource(R.drawable.icon_success);
					tv_res.setText("验证成功");
					tv_name.setText(data.getName());
					String sex = data.getSex();
					String sex1 = "";
					if ("1".equals(sex)){
						sex1 = "男";
					}else if ("2".equals(sex)){
						sex1 = "女";
					}else {
						sex1 = "未知";
					}
					tv_sex.setText(sex1);
					tv_birth.setText(data.getBirthday());
					tv_city.setText(data.getCity());
					tv_phone.setText(data.getMobile());
					tv_expireTime.setText(data.getEnd_time());
					tv_discount	.setText(data.getDiscount()+"折");
				}
				
				break;
			case VERTIFY_FAIL:
				String msg = getIntent().getStringExtra("msg");
				LogUtil.d("msg",msg);
				if ("该会员今日已在本店使用过".equals(msg)){
					tv_content.setText(msg);
					tv_res_other.setText("重复使用");
					iv_res_other.setImageResource(R.drawable.ic_rig);
				}else {
					tv_content.setText(msg);
					tv_res_other.setText("验证失败");
					iv_res_other.setImageResource(R.drawable.ic_wro);
				}
				break;
			case USE_SUCCESS:
				tv_content.setText("该会员卡当日将不可在本店继续使用");
				iv_res_other.setImageResource(R.drawable.icon_success);
				iv_res_other.setImageResource(R.drawable.icon_success);
				break;
		}
		
		
	}

	private void initView() {
		
		if (res == VERTIFY_OK){
			/**
			 * 验证成功界面
			 */
			iv_res = (ImageView) findViewById(R.id.iv_res);
			tv_res = (TextView) findViewById(R.id.tv_res);
			tv_name = (TextView) findViewById(R.id.tv_name);
			tv_sex = (TextView) findViewById(R.id.tv_sex);
			tv_birth = (TextView) findViewById(R.id.tv_birth);
			tv_city = (TextView) findViewById(R.id.tv_city);
			tv_phone = (TextView) findViewById(R.id.tv_phone);
			tv_expireTime = (TextView) findViewById(R.id.tv_expireTime);
			tv_discount = (TextView) findViewById(R.id.tv_discount);
			btn_resubimit = (Button) findViewById(R.id.btn_resubimit);
			btn_resubimit.setOnClickListener(this);
			btn_use = (Button) findViewById(R.id.btn_use);
			btn_use.setOnClickListener(this);

		}else {
			/**
			 * 其他界面
			 */
			iv_res_other = (ImageView) findViewById(R.id.iv_res_other);
			tv_res_other = (TextView) findViewById(R.id.tv_res_other);
			tv_content = (TextView) findViewById(R.id.tv_content);
			btn_sub = (Button) findViewById(R.id.btn_sub);
			btn_sub.setOnClickListener(this);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_resubimit://重新输入
				startActivity(new Intent(context,VipUserVertifyActivity.class));
				finish();
				break;
			case R.id.btn_use://请求使用该会员卡
				ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
				String id = getIntent().getStringExtra("id");
				LogUtil.d("id",id);
				HttpUtils.getConnection(context, null, ConstantParamPhone.USE_VIP+id+"/use", "post", new TextHttpResponseHandler() {
					@Override
					public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
						Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
						throwable.printStackTrace();
					}

					@Override
					public void onSuccess(int i, Header[] headers, String s) {

						try {
							JSONObject object = new JSONObject(s);
							String code =  object.getString("code");
							if ("SUCCESS".equalsIgnoreCase(code)){
								//使用成功
								res = USE_SUCCESS;
								initLayout();//重新加载布局
								initView();
								initData();//重新设置数据

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
					public void onFinish() {
						super.onFinish();
						ColorDialog.dissmissProcessDialog();
					}
				});

				break;
			case R.id.btn_sub://重新输入
				startActivity(new Intent(context,VipUserVertifyActivity.class));
				finish();
				break;
		}
	}
}
