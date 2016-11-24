package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.MD5Util;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.Header;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import com.dudu.duduhelper.R;
/**
 * @author
 * @version 1.0
 * @date 2016/9/23
 */

public class StoreMoneyActivity extends BaseActivity implements View.OnClickListener {
	private TextView redbage_check;
	private TextView activity_check;
	private EditText ed_num;
	private TextView tv_price;
	private Button btn_subimit;
	//默认选择红包
	private boolean isRedbag = true;
	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	private GoogleApiClient client2;
	private float price;
	private TextView tv_single_price;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_store_money);
		initHeadView("充值", true, false, 0);
		initView();
		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client2 = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
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
		tv_single_price = (TextView) findViewById(R.id.tv_single_price);

		String style = getIntent().getStringExtra("style");
		price = getIntent().getFloatExtra("price",50);
		tv_single_price.setText(price+"元");//设置信息单价
		//初始化界面按钮
		if (style.equals("redbag")) {
			redbage_check.setTextColor(getResources().getColor(R.color.text_green_color));
			activity_check.setTextColor(getResources().getColor(R.color.text_dark_color));
			isRedbag = true;
		} else if (style.equals("activity")) {
			redbage_check.setTextColor(getResources().getColor(R.color.text_dark_color));
			activity_check.setTextColor(getResources().getColor(R.color.text_green_color));
			isRedbag = false;
		}
		//输入次数的监听，合计金额自动变化
		ed_num.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				int num = 0;
				if (!TextUtils.isEmpty(s.toString())){//非空判断
					num = Integer.parseInt(s.toString().trim());
				}
				float total_price = num * price;
				BigDecimal b   =   new   BigDecimal(total_price);
				float   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).floatValue();
				//   b.setScale(2,   BigDecimal.ROUND_HALF_UP)   表明四舍五入，保留两位小数
				tv_price.setText(f1+"");
			}
		});
			
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_subimit:
				submit();
				break;
			case R.id.redbage_check:
				redbage_check.setTextColor(getResources().getColor(R.color.text_green_color));
				activity_check.setTextColor(getResources().getColor(R.color.text_dark_color));
				isRedbag = true;
				break;
			case R.id.activity_check:
				redbage_check.setTextColor(getResources().getColor(R.color.text_dark_color));
				activity_check.setTextColor(getResources().getColor(R.color.text_green_color));
				isRedbag = false;
				break;
		}
	}

	private void submit() {
		String num = ed_num.getText().toString().trim();
		if (TextUtils.isEmpty(num)){
			Toast.makeText(context,"请输入充值次数",Toast.LENGTH_SHORT).show();
			return;
		}

		//1.注册appid
		api = WXAPIFactory.createWXAPI(this, null);
		//2.将该app注册到微信
		api.registerApp("wxcb7744abfa329214");
		//我将后端反给我的信息放到了WeiXinPay中，这步是获取数据

		/**
		 * 请求预支付订单
		 */
		
		//请求网络
		RequestParams params = new RequestParams();
		//type_id = 1活动通知;2是红包通知
		String type_id = "";
		if (isRedbag){
			type_id = "2";
		}else {
			type_id = "1";
		}
		params.put("type_id",type_id);
		params.put("version","2");
		params.put("num",num);
		HttpUtils.getConnection(context, params, ConstantParamPhone.GET_PAYORDER,"post",new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				LogUtil.d("fail", "fff");
				Toast.makeText(context,"服务器异常，请稍后再试",Toast.LENGTH_SHORT).show();
				throwable.printStackTrace();
			}
			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				LogUtil.d("res", s);
				try {
					JSONObject json = new JSONObject(s);
					if (null != json) {

						/**
						 * 本地根据key生成签名sign
						 */
						String randomString = getRandomStringByLength(12);//生成唯一字随机字符串
						String time = System.currentTimeMillis()+"";//生成时间戳
						String pre_id = json.getString("prepay_id");
						SortedMap<Object,Object> parameters = new TreeMap<Object,Object>();
						parameters.put("appid", "wxcb7744abfa329214");
						parameters.put("partnerid", "1409893702");
						parameters.put("prepayid", pre_id);
						parameters.put("package", "Sign=WXPay");
						parameters.put("noncestr", randomString);
						parameters.put("timestamp",time );
						String signed = createSign("UTF_8", parameters);
						/**
						 * 封装好请求参数
						 */
						
						PayReq req = new PayReq();
						req.appId = "wxcb7744abfa329214";
						req.partnerId = "1409893702";
						req.prepayId = pre_id;
						req.nonceStr = randomString;
						req.timeStamp = time;
						req.packageValue = "Sign=WXPay";
						req.sign =signed;
						LogUtil.d("pre_id",pre_id.toString());

						// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
						api.sendReq(req);
					} else {
						Log.d("PAY_GET", "返回错误" + json.getString("retmsg"));
						Toast.makeText(context, "返回错误" + json.getString("retmsg"), Toast.LENGTH_SHORT).show();
					}
				} catch (Exception e) {
					Log.e("PAY_GET", "异常：" + e.getMessage());
					Toast.makeText(context, "异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				Toast.makeText(context,"充值成功",Toast.LENGTH_SHORT);
			}
		});


	}

	/**
	 * 生成随机字符串
	 * @param length
	 * @return
	 */
	public static String getRandomStringByLength(int length) {
		String base = "abcdefghijklmnopqrstuvwxyz0123456789";
		Random random = new Random();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < length; i++) {
			int number = random.nextInt(base.length());
			sb.append(base.charAt(number));
		}
		return sb.toString();
	}

	private IWXAPI api;

	



	/**
	 * ATTENTION: This was auto-generated to implement the App Indexing API.
	 * See https://g.co/AppIndexing/AndroidStudio for more information.
	 */
	public Action getIndexApiAction() {
		Thing object = new Thing.Builder()
				.setName("StoreMoney Page") // TODO: Define a title for the content shown.
				// TODO: Make sure this auto-generated URL is correct.
				.setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
				.build();
		return new Action.Builder(Action.TYPE_VIEW)
				.setObject(object)
				.setActionStatus(Action.STATUS_TYPE_COMPLETED)
				.build();
	}

	@Override
	public void onStart() {
		super.onStart();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		client2.connect();
		AppIndex.AppIndexApi.start(client2, getIndexApiAction());
	}

	@Override
	public void onStop() {
		super.onStop();

		// ATTENTION: This was auto-generated to implement the App Indexing API.
		// See https://g.co/AppIndexing/AndroidStudio for more information.
		AppIndex.AppIndexApi.end(client2, getIndexApiAction());
		client2.disconnect();
	}
	
	
	
	
	
	private static String Key = "A4AD3F2D84FC97FDA07D8B446D15EA28";//微信前面的key
	/**
	 * 微信支付签名算法sign 
	 * @param characterEncoding
	 * @param parameters
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static String createSign(String characterEncoding,SortedMap<Object,Object> parameters){
		StringBuffer sb = new StringBuffer();
		Set es = parameters.entrySet();//1.所有参与传参的参数按照accsii排序（升序）  
		Iterator it = es.iterator();
		while(it.hasNext()) {//2.生成字符串
			Map.Entry entry = (Map.Entry)it.next();
			String k = (String)entry.getKey();
			Object v = entry.getValue();
			if(null != v && !"".equals(v)
					&& !"sign".equals(k) && !"key".equals(k)) {
				sb.append(k + "=" + v + "&");
			}
		}
		sb.append("key=" + Key);//3.把key拼接进去
		String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();//md5加密
		return sign;
	}


}
