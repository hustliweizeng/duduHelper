package com.dudu.helper3.Activity.GetMoneyActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.Activity.CashHistoryActivity.ShopMoneyRecordListActivity;
import com.dudu.duduhelper.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.CreateCashPic;
import com.dudu.helper3.widget.ColorDialog;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
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
				initView();
			}
		});
		
	}

	private void initView() {
		
		getCashButton = (LinearLayout) this.findViewById(R.id.getCashButton);//扫码收款
		ImageCode = (ImageView) this.findViewById(R.id.imageCashCodeImg);
		ImageAware imageAware = new ImageViewAware(ImageCode, false);
		imageLoader.displayImage(data.getQrcode(), imageAware);//用imageloader设置二维码图片
		cashMoneyText = (TextView) this.findViewById(R.id.cashMoneyText);
		cashMoneyText.setText("￥ " + money);//设置收款金额
		getCashButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});//扫码收款页面跳转(因为当前页面入口只有扫码付款，所以，返回上一个界面就行)
	}

}
