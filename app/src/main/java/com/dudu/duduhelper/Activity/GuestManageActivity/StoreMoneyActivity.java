package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.widget.MyAlertDailog;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @author
 * @version 1.0
 * @date 2016/9/23
 */

public class StoreMoneyActivity extends BaseActivity implements View.OnClickListener  {
	private TextView redbage_check;
	private TextView activity_check;
	private EditText ed_num;
	private TextView tv_price;
	private Button btn_subimit;
	//默认选择红包
	private boolean isRedbag = true;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_store_money);
		initHeadView("充值",true,false,0);
		initView();
	}

	private void initView() {
		redbage_check = (TextView) findViewById(R.id.redbage_check);
		activity_check = (TextView) findViewById(R.id.activity_check);
		ed_num = (EditText) findViewById(R.id.ed_num);
		tv_price = (TextView) findViewById(R.id.tv_price);
		btn_subimit = (Button) findViewById(R.id.btn_subimit);
		redbage_check.setOnClickListener(this);
		activity_check.setOnClickListener(this);
		btn_subimit.setOnClickListener(this);

		String style = getIntent().getStringExtra("style");
		//初始化界面按钮
		if (style.equals("redbag")){
			redbage_check.setTextColor(getResources().getColor(R.color.text_green_color));
			activity_check.setTextColor(getResources().getColor(R.color.text_dark_color));
			isRedbag = true;
		}else if (style.equals("activity")){
			redbage_check.setTextColor(getResources().getColor(R.color.text_dark_color));
			activity_check.setTextColor(getResources().getColor(R.color.text_green_color));
			isRedbag =false;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_subimit:
				submit();
			case R.id.redbage_check	:
				redbage_check.setTextColor(getResources().getColor(R.color.text_green_color));
				activity_check.setTextColor(getResources().getColor(R.color.text_dark_color));
				ed_num.setText("");
				isRedbag = true;
				break;
			case R.id.activity_check:
				redbage_check.setTextColor(getResources().getColor(R.color.text_dark_color));
				activity_check.setTextColor(getResources().getColor(R.color.text_green_color));
				ed_num.setText("");
				isRedbag =false;
				break;
		}
	}

	private void submit() {
		// validate
	/*	String num = ed_num.getText().toString().trim();
		if (TextUtils.isEmpty(num)) {
			Toast.makeText(this, "num不能为空", Toast.LENGTH_SHORT).show();
			AlertDialog dailog = new  AlertDialog.Builder(context,R.style.AppTheme_Dialog).create();
			dailog.show();
			//获取弹窗界面
			Window window = dailog.getWindow();
			//获取屏幕的宽度
			WindowManager windowManager = getWindowManager();
			Display display = windowManager.getDefaultDisplay();
			WindowManager.LayoutParams lp = dailog.getWindow().getAttributes();
			lp.width = (int)(display.getWidth()); //设置宽度    
			dailog.getWindow().setAttributes(lp);
			//设置布局
			window.setContentView(R.layout.dailog_confirm_order);


			return;
		}*/
		//直接跳转到支付结果页面
		final IWXAPI msgApi = WXAPIFactory.createWXAPI(context, null);
		// 将该app注册到微信
		msgApi.registerApp("wxd930ea5d5a258f4f");
		//调用预支付
		IWXAPI api = WXAPIFactory.createWXAPI(this, "你在微信开放平台创建的app的APPID");
		//我将后端反给我的信息放到了WeiXinPay中，这步是获取数据
		msgApi.registerApp("你在微信开放平台创建的app的APPID");
		PayReq request = new PayReq();
		request.appId = "wxd930ea5d5a258f4f";//应用ID
		request.partnerId = "1900000109";//商户id
		request.prepayId= "1101000000140415649af9fc314aa427";//预支付交易会话ID
		request.packageValue = "Sign=WXPay";//随机字符串，不长于32位。推荐随机数生成算法
		request.nonceStr= "1101000000140429eb40476f8896f4c9";
		request.timeStamp= "1398746574";//时间戳，请见接口规则-参数规定
		request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";//签名，详见签名生成算法
		api.sendReq(request);
		
		
		
		
		
		
		startActivity(new Intent(context,PayResultActivity.class));
		finish();
	}

	//支付结果回掉
	public void onResp(BaseResp resp){
		if(resp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
			Log.d("tag","onPayFinish,errCode="+resp.errCode);
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setTitle("ss");
		}
	}

}
