package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.WxUtil;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.widget.MyAlertDailog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.Header;
import org.json.JSONObject;

/**
 * @author
 * @version 1.0
 * @date 2016/9/23
 */

public class StoreMoneyActivity extends BaseActivity implements View.OnClickListener ,IWXAPIEventHandler{
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
		//1.注册appid
		api = WXAPIFactory.createWXAPI(this, "wxb4ba3c02aa476ea1");
		//2.将该app注册到微信
		api.registerApp("wxb4ba3c02aa476ea1");
		//3.预支付请求的服务器地址
		String url = "http://wxpay.weixin.qq.com/pub_v2/app/app_pay.php?plat=android";
		//Toast.makeText(this, "获取订单中...", Toast.LENGTH_SHORT).show();
		//我将后端反给我的信息放到了WeiXinPay中，这步是获取数据
		AsyncHttpClient client = new AsyncHttpClient();
		PersistentCookieStore myCookieStore = new PersistentCookieStore(context);
		client.setCookieStore(myCookieStore);
		//获取本地user_agent;
		String defaultUserAgent = WebSettings.getDefaultUserAgent(context);
		if (isWifiAvailable(context)){
			client.addHeader("NETTYPE","WIFI");
			LogUtil.d("WIFI","ON");
		}else {
			client.addHeader("NETTYPE","3G+");
			LogUtil.d("WIFI","OFF");
		}
		client.setUserAgent(defaultUserAgent);
		//请求网络
		
		client.get( url, null, new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				LogUtil.d("fail","dssd");
				throwable.printStackTrace();

			}
			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				LogUtil.d("res",s);
				try {
					JSONObject json = new JSONObject(s);
					if(null != json ){
						PayReq req = new PayReq();
						//req.appId = "wxf8b4f85f3a794e77";  // 测试用appId
						req.appId			= json.getString("appid");
						req.partnerId		= json.getString("partnerid");
						req.prepayId		= json.getString("prepayid");
						req.nonceStr		= json.getString("noncestr");
						req.timeStamp		= json.getString("timestamp");
						req.packageValue	= json.getString("package");
						req.sign			= json.getString("sign");
						req.extData			= "app data"; // optional
						//Toast.makeText(context, "正常调起支付", Toast.LENGTH_SHORT).show();
						// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
						api.sendReq(req);
					}else{
						Log.d("PAY_GET", "返回错误"+json.getString("retmsg"));
						Toast.makeText(context, "返回错误"+json.getString("retmsg"), Toast.LENGTH_SHORT).show();
					}
			}catch(Exception e){
				Log.e("PAY_GET", "异常："+e.getMessage());
				Toast.makeText(context, "异常："+e.getMessage(), Toast.LENGTH_SHORT).show();
			}
		}
		});
		
		
		
	/*	msgApi.registerApp("你在微信开放平台创建的app的APPID");
		PayReq request = new PayReq();
		request.appId = "wxd930ea5d5a258f4f";//应用ID
		request.partnerId = "1900000109";//商户id
		request.prepayId= "1101000000140415649af9fc314aa427";//预支付交易会话ID
		request.packageValue = "Sign=WXPay";//随机字符串，不长于32位。推荐随机数生成算法
		request.nonceStr= "1101000000140429eb40476f8896f4c9";
		request.timeStamp= "1398746574";//时间戳，请见接口规则-参数规定
		request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";//签名，详见签名生成算法
		api.sendReq(request);*/
		
		
		
		
		
		/*
		startActivity(new Intent(context,PayResultActivity.class));
		finish();*/
	}
	private IWXAPI api;
	//支付成功以后回调
	@Override
	public void onReq(BaseReq baseReq) {
	}

	@Override
	public void onResp(BaseResp baseResp) {
		Log.d("res", "onPayFinish, errCode = " + baseResp.errCode);
		if(baseResp.getType()== ConstantsAPI.COMMAND_PAY_BY_WX){
			Log.d("tag","onPayFinish,errCode="+baseResp.toString());
			AlertDialog.Builder builder=new AlertDialog.Builder(this);
			builder.setTitle("支付成功");
			builder.show();
		}
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}
	/**
	 * 判断wifi连接状态
	 *
	 * @param ctx
	 * @return
	 */
	public static  boolean isWifiAvailable(Context ctx) {
		ConnectivityManager conMan = (ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo.State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (NetworkInfo.State.CONNECTED == wifi) {
			return true;
		} else {
			return false;
		}
	}


}
