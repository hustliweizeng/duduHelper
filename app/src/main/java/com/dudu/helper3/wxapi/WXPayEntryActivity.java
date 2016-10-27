package com.dudu.helper3.wxapi;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
public class WXPayEntryActivity extends BaseActivity implements IWXAPIEventHandler {


	private IWXAPI api;
	private TextView tv_res;


	@Override public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LogUtil.d("jieguo","ol");

       setContentView(R.layout.activity_pay_result);
		tv_res = (TextView) findViewById(R.id.tv_res);
		initHeadView("支付结果",true,false,0);
		api = WXAPIFactory.createWXAPI(this, "wxa43dd59c979e6ab7");
		api.handleIntent(getIntent(), this);
	}


	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
		api.handleIntent(intent, this);
	}


	@Override
	public void onReq(BaseReq req) {

	}


	@Override
	public void onResp(BaseResp resp) {
		int errCode = resp.errCode;
		LogUtil.d("resp",resp.errCode+"");

		if (errCode == 0) {
			// 0成功 展示成功页面
			tv_res.setText("支付成功");
		}
		else if (errCode == -1) {
			//-1 错误 可能的原因：签名错误、未注册APPID、项目设置APPID不正确、注册的APPID与设置的不匹配、其他异常等。 
			tv_res.setText("支付失败:"+resp.errStr);
			finish();
		}
		else if (errCode == -2) {
			//-2 用户取消 无需处理。发生场景：用户不支付了，点击取消，返回APP。  
			finish();
		}
	}


}  