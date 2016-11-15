package com.example.qr_codescan;

import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.helper3.Activity.CashHistoryActivity.ShopMoneyRecordListActivity;
import com.dudu.helper3.Activity.CheckSellAcitivty.CheckSaleDetailActivity;
import com.dudu.helper3.Activity.CheckSellAcitivty.CheckSaleHistoryActivity;
import com.dudu.helper3.Activity.GetMoneyActivity.ShopDiscountScanSucessActivity;
import com.dudu.helper3.Activity.GetMoneyActivity.ShopGetCashCodeActivity;
import com.dudu.helper3.Activity.GetMoneyActivity.ShopGetInComeCashActivity;
import com.dudu.helper3.Activity.VipUserActivity.VipUserVertifyActivity;
import com.dudu.helper3.Activity.VipUserActivity.VipUserVertifyResActivity;
import com.dudu.helper3.Activity.VipUserActivity.VipVertifyHistoryActivity;
import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.VipDetailBean;
import com.dudu.helper3.widget.ColorDialog;
import com.google.gson.Gson;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.decoding.CaptureActivityHandler;
import com.mining.app.zxing.decoding.InactivityTimer;
import com.mining.app.zxing.view.ViewfinderView;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Vector;

/**
 * Initial the camera
 * 扫二维码收款页面
 * @author Ryan.Tang
 */
public class MipcaActivityCapture extends BaseActivity implements Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private boolean hasSurface;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	private String action;
	private String orderName;
	private String orderCash;
	private TextView moneyText;
	
	private TextView scanText;
	private TextView btnText;
	private ImageView btnIconImg;
	private LinearLayout getCashButton;
	private TextView inputcodeBtn;
	private String orderId;
	private String price;
	//private LinearLayout scanmycodebtn;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_capture);
		//通过其他界面跳转过来携带的参数
		price = getIntent().getStringExtra("price");
		action = getIntent().getStringExtra("action");

		initHeadView();
	}

	/**
	 * 请求支付订单
	 */
	private void requestOrder(final  String restring)
	{
		
		//请求网络数据
		ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("fee",price);
		params.add("body",sp.getString("shopName",""));
		HttpUtils.getConnection(context,params, ConstantParamPhone.CREATE_PAYMENT_ORDER, "post",new TextHttpResponseHandler()
		{

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3)
			{
				Toast.makeText(context, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2)
			{
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						orderId = object.getString("id");//生成订单号
						//进入（扫码收款页面）
						Intent intent = null;
						intent = new Intent(MipcaActivityCapture.this,ShopDiscountScanSucessActivity.class);
						intent.putExtra("price", price);
						intent.putExtra("id",orderId);
						intent.putExtra("result", restring);
						startActivity(intent);
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
				ColorDialog.dissmissProcessDialog();
			}
		});
	}

	private void initHeadView() {
		if ("income".equals(action))
		{
			initHeadView("收款", true, true, R.drawable.icon_historical);
		}
		else
		{
			if ("vip".equals(action)){
				initHeadView("会员卡验证",true,true,R.drawable.icon_historical);
			}else {

				initHeadView("核销", true, true, R.drawable.icon_historical);
			}
		}
		// ViewUtil.addTopView(getApplicationContext(), this,
		// R.string.scan_card);
		CameraManager.init(getApplication());
		//扫码窗口谷歌zxing扫码工具
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
		moneyText = (TextView) this.findViewById(R.id.moneyText);
		btnText = (TextView) this.findViewById(R.id.btnText);
		scanText = (TextView) this.findViewById(R.id.scanText);
		inputcodeBtn = (TextView) this.findViewById(R.id.inputcodeBtn);
		btnIconImg=(ImageView) this.findViewById(R.id.btnIconImg);
		//二维码收款页面
		getCashButton= (LinearLayout) this.findViewById(R.id.getCashButton);
		getCashButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
			if("income".equals(action))
			{
				Intent intent = new Intent(context, ShopGetCashCodeActivity.class);
				intent.putExtra("price",price);
				startActivity(intent);
			}
			else
			{
				if ("vip".equals(action)){
					startActivity(new Intent(context, VipUserVertifyActivity.class));
					
				}else {
					Intent intent = new Intent(MipcaActivityCapture.this,ShopGetInComeCashActivity.class);
					intent.putExtra("action", "hexiao");
					startActivity(intent);
				}
			}
			}
		});
		inputcodeBtn.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
			Intent intent = new Intent(MipcaActivityCapture.this,ShopGetInComeCashActivity.class);
			intent.putExtra("action", "getcashcode");
			intent.putExtra("fee", price);
			//intent.putExtra("body", body);
			startActivity(intent);
			finish();
			}
		});
		if (action.equals("income"))
		{
			moneyText.setText("￥ "+price);
		}
		else
		{
			if ("vip".equals(action)){
				scanText.setText("请将用户出示的个人微信电子会员卡的二维码放入框内即可完成核销");
				btnText.setText("输入会员卡卡号核销");
				moneyText.setVisibility(View.INVISIBLE);
			}else {

				moneyText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 0);
				scanText.setText("请将用户提供的兑换码置入框内即可完成核销");
				btnText.setText("兑换码核销");
				inputcodeBtn.setVisibility(View.GONE);
				btnIconImg.setImageResource(R.drawable.icon_tiaoxingma);
			}
		}
	}

	@Override
	public void RightButtonClick() {
		super.RightButtonClick();
		if(action.equals("income")){
			startActivity(new Intent(context, ShopMoneyRecordListActivity.class));
		}else {
			if ("vip".equals(action)) {
				startActivity(new Intent(context,VipVertifyHistoryActivity.class));
			}else {
				//进入核销历史
				startActivity(new Intent(context, CheckSaleHistoryActivity.class));
			}
			
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;

	}

	@Override
	public void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	/**
	 * 处理二维码扫描结果，应该把识别出来的信息，请求网络服务器进行核对，
	 * @param result
	 * @param barcode
	 */
	public void handleDecode(Result result, Bitmap barcode) 
	{
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		final String resultString = result.getText();
		LogUtil.d("success",resultString+"");
		if (resultString.equals("")) {
			Toast.makeText(MipcaActivityCapture.this, "扫描失败！",
					Toast.LENGTH_SHORT).show();
			finish();
		} else {
			//扫描成功
			Intent intent = null;
			if (action.equals("income")) //收款页面
			{
				requestOrder(resultString);//获取订单号并把扫描结果传递给服务器
				return;
			}else {
				if ("vip".equals(action)){
					//进入核销结果页面-请求网络处理核销结果
					/**
					 * 请求验证会员卡
					 */
						String number = resultString;
						if (TextUtils.isEmpty(number)){
							Toast.makeText(context,"卡号不能为空",Toast.LENGTH_SHORT).show();
							return;
						}
						HttpUtils.getConnection(context, null, ConstantParamPhone.GET_VIP_INFO+number, "get", new TextHttpResponseHandler() {
							@Override
							public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
								Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
							}
							@Override
							public void onSuccess(int i, Header[] headers, String s) {

								try {
									JSONObject object = new JSONObject(s);
									String code =  object.getString("code");
									Intent intent = new Intent(context,VipUserVertifyResActivity.class);
									if ("SUCCESS".equalsIgnoreCase(code)){
										//数据请求成功
										VipDetailBean detail = new Gson().fromJson(s, VipDetailBean.class);
										//获取验证结果，跳转到相应页面
										intent.putExtra("res",0);//成功
										intent.putExtra("detail",detail);//详情
										intent.putExtra("id",resultString);
									}else {
										//数据请求失败
										String msg = object.getString("msg");
										intent.putExtra("res",1);//失败
										intent.putExtra("msg",msg);//错误信息
									}
									startActivity(intent);
									finish();
								} catch (JSONException e) {
									e.printStackTrace();
								}
							}
						});
					
				}else {

					//进入核销结果页面
					intent = new Intent(context, CheckSaleDetailActivity.class);
					intent.putExtra("result", resultString);
				}
			}

			startActivity(intent);
		}
		MipcaActivityCapture.this.finish();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			AssetFileDescriptor file = getResources().openRawResourceFd(
					R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(),
						file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}