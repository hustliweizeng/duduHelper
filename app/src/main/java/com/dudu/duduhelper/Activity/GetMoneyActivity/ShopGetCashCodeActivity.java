package com.dudu.duduhelper.Activity.GetMoneyActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.Activity.MainActivity;
import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Activity.CashHistoryActivity.ShopMoneyRecordListActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.CreateCashPic;
import com.dudu.duduhelper.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopGetCashCodeActivity extends BaseActivity 
{
    private String money;
    private ImageView ImageCode;
    private TextView cashMoneyText;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private LinearLayout getCashButton;
    private TextView getCashInputText;
	private CreateCashPic data;
	private TimeCount time;
	private TextView tv_content;
	private TextView tv_btn;
	private ImageView iv_btn;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		time = new TimeCount(60000,3000);
		setContentView(R.layout.shop_get_cash_code);
		initHeadView("收款", true,true, R.drawable.icon_historical);
		//获取传递过来的二维码和价格
		money = getIntent().getStringExtra("price");
		initData();
		
	}

	@Override
	public void RightButtonClick() {
		//进入收银历史
		Intent intent = new Intent(this,ShopMoneyRecordListActivity.class);
		startActivity(intent);
	}

	private void initData() {
		
		if (TextUtils.isEmpty(money)){
			Toast.makeText(context,"输入金额不能为空",Toast.LENGTH_SHORT).show();
			return;
		}
		ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.put("fee",money);
		params.put("body",sp.getString("shopName","商品描述"));
		HttpUtils.getConnection(context, params, ConstantParamPhone.CREATE_PAY_PIC, "post", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				LogUtil.d("result",s);
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						data = new Gson().fromJson(s,CreateCashPic.class);
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
				//数据请求成功后展示数据
				setData();
			}
		});
		
	}

	private void setData() {
		tv_content = (TextView) findViewById(R.id.tv_content);
		//按钮文字
		tv_btn = (TextView) findViewById(R.id.tv_btn);
		//按钮图片
		iv_btn = (ImageView) findViewById(R.id.iv_btn);
		
		getCashButton = (LinearLayout) this.findViewById(R.id.getCashButton);//刷卡收款按钮
		ImageCode = (ImageView) this.findViewById(R.id.imageCashCodeImg);//二维码图片
		//对二维码图片做非空判断
		if (data !=null && !TextUtils.isEmpty(data.getQrcode())){
			ImageAware imageAware = new ImageViewAware(ImageCode, false);
			imageLoader.displayImage(data.getQrcode(), imageAware);//用imageloader设置二维码图片
		}
		cashMoneyText = (TextView) this.findViewById(R.id.cashMoneyText);
		cashMoneyText.setText("￥ " + money);//设置收款金额
		getCashButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();//返回上一页
			}
		});
		//扫码收款页面跳转(因为当前页面入口只有扫码付款，所以，返回上一个界面就行)
		time.start();//开始计时
	}

	/**
	 * 计时器
	 */
	class TimeCount extends CountDownTimer
	{
		public TimeCount(long millisInFuture, long countDownInterval)
		{
			super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		@Override
		public void onFinish()
		{
			//计时完毕时触发
			//Toast.makeText(context,"付款超时",Toast.LENGTH_SHORT).show();
			tv_content.setText("付款超时!");
		}
		@Override
		public void onTick(long millisUntilFinished)
		{
			//计时过程显示
			if (data!=null){//当获取到结果时再开始请求结果
				requetResult();
			}
			//Toast.makeText(context,"正在等待付款结果",Toast.LENGTH_SHORT).show();
			tv_content.setText("正在等待付款结果...");
		}
	}

	private void requetResult() {
		RequestParams params = new RequestParams();
		params.add("id",data.getId() );
		HttpUtils.getConnection(context,params,ConstantParamPhone.GET_PAYMENT_RESULT, "get",new TextHttpResponseHandler()
		{
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3)
			{
				Toast.makeText(context, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2)
			{
				LogUtil.d("res",arg2);
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						tv_content.setText("付款成功！");
						time.cancel();//取消及时
						tv_btn.setText("回到首页");
						iv_btn.setVisibility(View.GONE);
						getCashButton.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								finish();
								startActivity(new Intent(context, MainActivity.class));
							}
						});
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						//Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

			}

			@Override
			public void onFinish()
			{
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		time.cancel();
	}
}
