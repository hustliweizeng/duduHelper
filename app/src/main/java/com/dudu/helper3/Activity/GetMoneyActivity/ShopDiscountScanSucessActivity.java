package com.dudu.helper3.Activity.GetMoneyActivity;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.lang.ArrayUtils;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.Activity.MainActivity;
import com.dudu.helper3.Activity.PrinterActivity.ShopSearchBlueToothActivity;
import com.dudu.duduhelper.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.widget.ColorDialog;
import com.dudu.helper3.widget.ConfirmView;
import com.dudu.helper3.widget.DilatingDotsProgressBar;
import com.example.qr_codescan.MipcaActivityCapture;
import com.gprinter.command.EscCommand;
import com.gprinter.command.EscCommand.ENABLE;
import com.gprinter.command.EscCommand.FONT;
import com.gprinter.command.EscCommand.JUSTIFICATION;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.RotateAnimation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShopDiscountScanSucessActivity extends BaseActivity 
{
	private ConfirmView confirmView;
	private DilatingDotsProgressBar mDilatingDotsProgressBar;
	private TextView result;
	private ImageView showContentImgBtn;
	private RelativeLayout descriptionRelayout;
	private TextView discountScanDiscriptText;
	int maxDescripLine = 3; //TextView默认最大展示行数
	private boolean isExpand=false;//是否已展开的状态
	private int firstValue;
	private String auth_code;
	private String price;
	private String id;
	private TextView discountScanNum;
	private String ordernum;
	private Button printButton;
	private TimeCount time;
	private TextView discountScanTime;
	private TextView discountScanMoney;
	private static BluetoothSocket bluetoothSocket = null;   
	private static OutputStream outputStream = null;  
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothDevice device;
	private boolean isConnection = false;
	private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	
	private Button goHomeBtn;
	private Button goCashutton;
	private ImageView imageCashCodeImg;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_discount_scan_sucess);
		initHeadView("扫码收款", true, false, 0);
		time = new TimeCount(30000, 5000);//构造CountDownTimer对象
		initFilter();
		initView();
		initViewData();
	}
	private void initFilter() 
	{
		// 设置广播信息过滤      
		IntentFilter intentFilter = new IntentFilter();     
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//开关状态
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//设备开始绑定
		// 注册广播接收器，接收并处理搜索结果      
		registerReceiver(receiver, intentFilter);     
	}

	private void initViewData() 
	{
		//初始化价格
		if(!TextUtils.isEmpty(getIntent().getStringExtra("price")))
		{
		   price = getIntent().getStringExtra("price");
		   discountScanMoney.setText(price+"元");
		}
		else
		{
			setFailResult("扫码失败"); 
			return;
		}
		//初始化描述信息
		if(!TextUtils.isEmpty(getIntent().getStringExtra("id")))
		{
			id=getIntent().getStringExtra("id");
		}
		else
		{
			setFailResult("扫码失败"); 
			return;
		}
		//初始化结果码
		if(!TextUtils.isEmpty(getIntent().getStringExtra("result")))
		{
			auth_code=getIntent().getStringExtra("result");
		}
		else
		{
			setFailResult("扫码失败"); 
			return;
		}
		getInComeResult();
	}
 
	private void initView() 
	{
		//返回首页
		goHomeBtn = (Button) this.findViewById(R.id.goHomeButton);
		//继续收银
		goCashutton = (Button) this.findViewById(R.id.goCashutton);
		goHomeBtn.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View v) {
			Intent intent = new Intent(ShopDiscountScanSucessActivity.this,MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			}
		});
		goCashutton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
			Intent intent = new Intent(ShopDiscountScanSucessActivity.this,ShopGetInComeCashActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);
			finish();
			}
		});
		//收款金额
		discountScanMoney=(TextView) this.findViewById(R.id.discountScanMoney);
		//收款时间
		discountScanTime=(TextView) this.findViewById(R.id.discountScanTime);
		//打印按钮
		printButton=(Button) this.findViewById(R.id.printButton);
		//显示结果
		imageCashCodeImg = (ImageView) findViewById(R.id.imageCashCodeImg);
		
		discountScanNum=(TextView) this.findViewById(R.id.discountScanNum);
		descriptionRelayout=(RelativeLayout) this.findViewById(R.id.descriptionRelayout);
		discountScanDiscriptText=(TextView) this.findViewById(R.id.discountScanDiscriptText);
		//OnCreate方法中定义设置的textView不会马上渲染并显示，所以textview的getLineCount()获取到的值一般都为零，因此使用post会在其绘制完成后来对ImageView进行显示控制。 
		discountScanDiscriptText.post(new Runnable() 
		{
			
			@Override
			public void run() 
			{
				// TODO Auto-generated method stub
				firstValue=discountScanDiscriptText.getHeight();
//				if(discountScanDiscriptText.getLineCount()>1)
//				{
//					showContentImgBtn.setVisibility(View.VISIBLE);
//				}
			}
		});
		showContentImgBtn=(ImageView) this.findViewById(R.id.showContentImgBtn);
		mDilatingDotsProgressBar = (DilatingDotsProgressBar) findViewById(R.id.progress);
        // show progress bar and start animating
        mDilatingDotsProgressBar.showNow();
		confirmView = (ConfirmView) findViewById(R.id.confirm_view);
		confirmView.animatedWithState(ConfirmView.State.Progressing);
		//付款结果
		result=(TextView) findViewById(R.id.result);
		descriptionRelayout.setOnClickListener(new View.OnClickListener() 
		{

            @Override
            public void onClick(View v) 
            {
                discountScanDiscriptText.clearAnimation();//清楚动画效果
                final int deltaValue;//默认高度，即前边由maxLine确定的高度
                final int startValue = discountScanDiscriptText.getHeight();//起始高度
                int durationMillis = 350;//动画持续时间
                if (!isExpand) 
                {
                	/**
                     * 展开动画
                     * 从起始高度增长至实际高度
                     */
                	isExpand=true;
                	deltaValue = discountScanDiscriptText.getLineHeight() * maxDescripLine - startValue;
                    RotateAnimation animation = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                    animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    showContentImgBtn.startAnimation(animation);
                } 
                else 
                {
                	/**
//                   * 折叠动画
//                   * 从实际高度缩回起始高度
//                   */
                	isExpand=false;
                	deltaValue =firstValue-startValue;
                	RotateAnimation animation = new RotateAnimation(90, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                	animation.setDuration(durationMillis);
                    animation.setFillAfter(true);
                    showContentImgBtn.startAnimation(animation);
                }
                Animation animation = new Animation() 
                {
                	@Override
                	protected void applyTransformation(float interpolatedTime,Transformation t) 
                	{
                		// TODO Auto-generated method stub
                		//根据ImageView旋转动画的百分比来显示textview高度，达到动画效果
//                		if(isExpand)
//                		{
                			discountScanDiscriptText.setHeight((int) (startValue + deltaValue * interpolatedTime));
//                		}
//                		else
//                		{
//                			discountScanDiscriptText.setHeight((int)(startValue-(startValue-57)* interpolatedTime));
//                		}
                	}
                };
                animation.setDuration(durationMillis);
                animation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						if(isExpand)
						{
							discountScanDiscriptText.setLines(3);
						}
						else 
						{
							discountScanDiscriptText.setLines(1);
						}
					}
				});
                discountScanDiscriptText.startAnimation(animation);
                //descriptionRelayout.startAnimation(animation);
            }
        });
	}
	
	//请求扫码支付接口
	private void getInComeResult() 
	{
		ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.put("id",id);
		params.put("code",auth_code);
		LogUtil.d("ss","id="+id+"//code="+auth_code);
		HttpUtils.getConnection(context, params, ConstantParamPhone.PAY_BY_CAMERA, "post", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						LogUtil.d("success",s);
						//设置页面信息
						result.setText("收款成功！");
						//设置成功的图片
						imageCashCodeImg.setImageResource(R.drawable.icon_pay_sucess);
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						LogUtil.d("fail",s);
						Toast.makeText(context,s,Toast.LENGTH_LONG).show();
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
	}
	//查询订单
	private void selectOrderStatus() 
	{
		// TODO Auto-generated method stub
		result.setText("正在查询支付结果");
		RequestParams params = new RequestParams();
		params.add("id",id );
		params.add("code", this.share.getString("token", ""));
        HttpUtils.getConnection(context,params,ConstantParamPhone.GET_SCAN_ORDER, "get",new TextHttpResponseHandler()
		{

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(ShopDiscountScanSucessActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
			}
			
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
			}
		});
	}
	//设置失败结果
	private void setFailResult(String string) 
	{
		// TODO Auto-generated method stub
		mDilatingDotsProgressBar.hideNow();
		result.setText(string);
		confirmView.animatedWithState(ConfirmView.State.Fail);
		printButton.setVisibility(View.VISIBLE);
		printButton.setText("重新扫描");
		printButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(price)&&!TextUtils.isEmpty(id))
				{
					Intent intent=new Intent(ShopDiscountScanSucessActivity.this,MipcaActivityCapture.class);
					intent.putExtra("action", "income");
					intent.putExtra("fee", price);
					intent.putExtra("body", id);
					startActivity(intent);
					ShopDiscountScanSucessActivity.this.finish();
				}
				else
				{
					Intent intent=new Intent(ShopDiscountScanSucessActivity.this,ShopGetInComeCashActivity.class);
					startActivity(intent);
					ShopDiscountScanSucessActivity.this.finish();
				}
			}
		});
		
	}
	//设置成功结果
	private void setSucessResult() 
	{
		// TODO Auto-generated method stub
		mDilatingDotsProgressBar.hideNow();
		result.setText("收款完成");
		confirmView.animatedWithState(ConfirmView.State.Success);
		printButton.setVisibility(View.VISIBLE);
		printButton.setText("打印凭据");
		printButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				// TODO Auto-generated method stub
				bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
				if(bluetoothAdapter==null)
				{
					Toast.makeText(ShopDiscountScanSucessActivity.this,"该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
					return;
				}
				
				else if(!bluetoothAdapter.isEnabled())//isEnabled enable是激活,开启蓝牙
				{
					//开启
					ColorDialog.showRoundProcessDialog(ShopDiscountScanSucessActivity.this,R.layout.loading_process_dialog_color);
					Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);     
					// 设置蓝牙可见性，最多300秒      
					intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3000);     
					startActivityForResult(intent, 1); 
				}
				else
				{
					SharedPreferences sharePrint= getSharedPreferences("printinfo", MODE_PRIVATE);
					if(!TextUtils.isEmpty(sharePrint.getString("blueAddress", "")))
					{
						
						device=bluetoothAdapter.getRemoteDevice(sharePrint.getString("blueAddress", ""));
						if(device.getBondState() == BluetoothDevice.BOND_NONE)
						{
							try 
							{
								Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
								createBondMethod.invoke(device);
							}
							catch (Exception e) 
							{
								// TODO Auto-generated catch block
								e.printStackTrace();
								Toast.makeText(ShopDiscountScanSucessActivity.this, "配对失败！", Toast.LENGTH_SHORT).show(); 
							}  
						}
						else
						{
							getConnect();
						}
					}
					else
					{
						Intent intent=new Intent(ShopDiscountScanSucessActivity.this,ShopSearchBlueToothActivity.class);
						startActivity(intent);
					}
				}
			}
		});
	}
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
			//selectOrderStatus();
			setFailResult("支付超时，请稍后查询收款状态");
		}
		@Override
		public void onTick(long millisUntilFinished)
		{
			//计时过程显示
			selectOrderStatus();
		}
	}
	
	private BroadcastReceiver receiver=new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			// TODO Auto-generated method stub
			String action = intent.getAction(); 
			if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
			{
				if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON)
				{
					Toast.makeText(ShopDiscountScanSucessActivity.this,"打开成功", Toast.LENGTH_SHORT).show();
					ColorDialog.dissmissProcessDialog();
				}
				else
				{
					Toast.makeText(ShopDiscountScanSucessActivity.this,"打开蓝牙失败", Toast.LENGTH_SHORT).show();
					ColorDialog.dissmissProcessDialog();
				}
			}
			if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action))
			{
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);  
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) 
                {    
                    //绑定过的设备    
                	Toast.makeText(ShopDiscountScanSucessActivity.this,"配对成功", Toast.LENGTH_SHORT).show();
                } 
                if (device.getBondState() == BluetoothDevice.BOND_NONE)  
                {    
                    //未绑定的设备
                	Toast.makeText(ShopDiscountScanSucessActivity.this,"配对失败", Toast.LENGTH_SHORT).show();
                }    
			}
		}
	};
  //获取连接
  	private void getConnect()
  	{
  		if (!this.isConnection) 
  		{    
              try
              {
                  bluetoothSocket = this.device.createRfcommSocketToServiceRecord(uuid);    
                  bluetoothSocket.connect();    
                  outputStream = bluetoothSocket.getOutputStream();    
                  this.isConnection = true;    
                  if (this.bluetoothAdapter.isDiscovering()) 
                  {    
                  	//取消搜索
                  }    
              } 
              catch (Exception e) 
              {    
                  Toast.makeText(this, "连接失败,请检查打印机是否开启！", Toast.LENGTH_LONG).show(); 
                  return;
              }    
              Toast.makeText(this, this.device.getName() + "连接成功！", Toast.LENGTH_SHORT).show();   
          } 
          sendPrint();
  	}
  //发送打印
  	private void sendPrint() 
  	{
  		if (this.isConnection) 
  		{    
              System.out.println("开始打印！！");    
              try 
              { 
                  EscCommand esc = new EscCommand();
                  //esc.addPrintAndFeedLines((byte) 3);
                  //esc.addSelectCodePage(EscCommand.CODEPAGE.UYGUR);
                  //设置汉子无效
                  //esc.addCancelKanjiMode();
                  esc.addPrintAndLineFeed();
                  esc.addSelectKanjiMode();
                  //esc.addSelectJustification(JUSTIFICATION.LEFT);//设置打印左对齐
                  esc.addSelectJustification(JUSTIFICATION.CENTER);//设置打印居中
                  esc.addSelectPrintModes(FONT.FONTA, ENABLE.OFF, ENABLE.ON, ENABLE.OFF, ENABLE.OFF);//设置为倍高倍宽
                  esc.addText("欢迎光临\n");   //  打印文字
                  esc.addText(share.getString("shopname", "本店铺")+"\n\n");
                  esc.addSelectPrintModes(FONT.FONTA, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF);//设置为倍高倍宽
                  //esc.addSetCharcterSize(WIDTH_ZOOM.MUL_3, HEIGHT_ZOOM.MUL_3);//设置字符尺寸
                  esc.addSelectJustification(JUSTIFICATION.LEFT);//设置打印左对齐
                  esc.addText("时间：        "+discountScanTime.getText().toString()+"\n");   //  打印文字    
                  esc.addText("订单号：      "+ordernum+"\n");   //  打印文字
                  esc.addText("--------------------------------\n");   //  打印文字
                  esc.addText("名称");   //  打印文字
                  
                  esc.addSetAbsolutePrintPosition((short) 310);
                  esc.addText("金额\n");
                  esc.addText("商家收款");
                  esc.addSetAbsolutePrintPosition((short) 300);
              	  esc.addText(price+"\n");
              	
//                  esc.addText("\n\n商家优惠");   //  打印文字
//                  esc.addSetAbsolutePrintPosition((short) 300);
//                  esc.addText(getCashDataBean.getData().getDiscount_shop_fee()+"\n");
//                  
//                  esc.addText("红包减免");
//                  esc.addSetAbsolutePrintPosition((short) 300);
//                  esc.addText(getCashDataBean.getData().getDiscount_activity_fee()+"\n");
                  
                  esc.addText("--------------------------------\n");   //  打印文字
                  
                  esc.addTurnEmphasizedModeOnOrOff(ENABLE.ON);//设置加粗
                  esc.addText("实收金额");
                  esc.addSetAbsolutePrintPosition((short) 300);
                  esc.addText(price+"\n\n");   //  打印文字
                  
                  esc.addSelectJustification(JUSTIFICATION.CENTER);//设置打印居中
                  esc.addTurnEmphasizedModeOnOrOff(ENABLE.OFF);
                  //打印图片
//  		          Bitmap b1 = BitmapFactory.decodeResource(getResources(),R.drawable.qcoerd);
//  		          esc.addRastBitImage(b1, 200, 0);   //打印图片
//  		          //设置二维码内容
//                  esc.addStoreQRCodeData(qrcodeContent);
//                  //设置二维码尺寸
//                  esc.addSelectSizeOfModuleForQRCode((byte)6);
//                  esc.addPrintQRCode();
//                  esc.addText("\n使用微信扫描该二维码即可完成本次支付\n\n");
                  esc.addText("\n"+share.getString("getagentname", "")+"竭诚为您服务\n\n"); 
                  esc.addText("*******************************\n\n\n\n\n\n\n");
                  Vector<Byte> datas = esc.getCommand(); //发送数据
                  Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
                  byte[] bytes = ArrayUtils.toPrimitive(Bytes);
                  outputStream.write(bytes, 0, bytes.length);    
                  outputStream.flush();  
                  Toast.makeText(ShopDiscountScanSucessActivity.this, "打印成功！", Toast.LENGTH_LONG).show();
              }
              catch (Exception e)
              {    
                  Toast.makeText(ShopDiscountScanSucessActivity.this, "打印失败！", Toast.LENGTH_LONG).show();    
              }    
          }
  		else 
  		{
              Toast.makeText(ShopDiscountScanSucessActivity.this, "设备未连接，请重新连接！", Toast.LENGTH_SHORT).show();    
        }    
  	}
  	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) 
		{
			if (resultCode == RESULT_OK) 
			{
				
			} 
			else if (resultCode == RESULT_CANCELED) 
			{
				ColorDialog.dissmissProcessDialog();
				Toast.makeText(ShopDiscountScanSucessActivity.this,"打开蓝牙取消", Toast.LENGTH_SHORT).show();
			}
		}
	}
  	@Override
    protected void onDestroy() 
    {
    	// TODO Auto-generated method stub
    	super.onDestroy();
    	try
    	{
			outputStream.close();
			bluetoothSocket.close();
		} 
    	catch (Exception e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
    }
	
	

}
