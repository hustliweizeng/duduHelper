package com.dudu.duduhelper.Activity.CashHistoryActivity;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.lang.ArrayUtils;
import org.apache.http.Header;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Activity.WelcomeActivity.LoginActivity;
import com.dudu.duduhelper.Activity.ShopSearchBlueToothActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.bean.GetCashDetailBean;
import com.dudu.duduhelper.bean.SelectScanOrderBean;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.gprinter.command.EscCommand;
import com.gprinter.command.EscCommand.ENABLE;
import com.gprinter.command.EscCommand.FONT;
import com.gprinter.command.EscCommand.JUSTIFICATION;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class GetInComeActivity extends BaseActivity 
{
	private TextView payactiontext;
	private TextView getcashdatatext;
	private TextView getcashtypetext;
	private TextView getcashactiontext;
	private TextView getcashmoneydiscriptedit;
	private TextView discountTextView;
	private TextView incomeTextView;
	private GetCashDetailBean getCashDataBean;
	private String no="";
	private Button printButton;
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothDevice device;
	private boolean isConnection = false;
	private static BluetoothSocket bluetoothSocket = null;   
	private static OutputStream outputStream = null;  
	private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private RelativeLayout discountRellayout;
	private SwipeRefreshLayout getDiscountSwipeLayout;
	private TextView referchStatus;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_in_come);
		initHeadView("收款记录", true, false, 0);
		initFilter();
		no=getIntent().getStringExtra("no");
		initView();
		selectOrderStatus(true);
		//initData(true);
	}
	
	

	private void initFilter() 
	{
		// TODO Auto-generated method stub
		// 设置广播信息过滤      
		IntentFilter intentFilter = new IntentFilter();     
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//开关状态
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//设备开始绑定
		// 注册广播接收器，接收并处理搜索结果      
		registerReceiver(receiver, intentFilter);     
	}

	@SuppressLint("ResourceAsColor") 
	@SuppressWarnings("deprecation")
	private void initView() 
	{
		// TODO Auto-generated method stub
		referchStatus=(TextView) this.findViewById(R.id.referchStatus);
		getDiscountSwipeLayout=(SwipeRefreshLayout) this.findViewById(R.id.getDiscountSwipeLayout);
		getDiscountSwipeLayout.setColorSchemeResources(R.color.text_color);
		getDiscountSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		getDiscountSwipeLayout.setProgressBackgroundColor(R.color.bg_color);
		discountRellayout=(RelativeLayout) this.findViewById(R.id.discountRellayout);
		discountTextView=(TextView) this.findViewById(R.id.discountTextView);
		incomeTextView=(TextView) this.findViewById(R.id.incomeTextView);
		payactiontext=(TextView) this.findViewById(R.id.payactiontext);
		getcashdatatext=(TextView) this.findViewById(R.id.getcashdatatext);
		getcashtypetext=(TextView) this.findViewById(R.id.getcashtypetext);
		getcashactiontext=(TextView) this.findViewById(R.id.getcashactiontext);
		getcashmoneydiscriptedit=(TextView) this.findViewById(R.id.getcashmoneydiscriptedit);
		printButton=(Button) this.findViewById(R.id.printButton);
		//下拉刷新事件
		getDiscountSwipeLayout.setOnRefreshListener(new OnRefreshListener() 
		{
			
			@Override
			public void onRefresh() 
			{
				// TODO Auto-generated method stub
				selectOrderStatus(false);
			}
		});
		printButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
				if(bluetoothAdapter==null)
				{
					Toast.makeText(GetInComeActivity.this,"该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
					return;
				}
				
				else if(!bluetoothAdapter.isEnabled())//isEnabled enable是激活,开启蓝牙
				{
					//开启
					ColorDialog.showRoundProcessDialog(GetInComeActivity.this,R.layout.loading_process_dialog_color);
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
								Toast.makeText(GetInComeActivity.this, "配对失败！", Toast.LENGTH_SHORT).show(); 
							}  
						}
						else
						{
							getConnect();
						}
					}
					else
					{
						Intent intent=new Intent(GetInComeActivity.this,ShopSearchBlueToothActivity.class);
						startActivity(intent);
					}
				}
			}
		});
	}
	private void initData(final boolean flag) 
	{
		// TODO Auto-generated method stub
		if(flag)
		{
			ColorDialog.showRoundProcessDialog(this,R.layout.loading_process_dialog_color);
		}
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("no",no);
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(GetInComeActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.GET_CASH_DETAIL, params,new TextHttpResponseHandler()
		{
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(GetInComeActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
				referchStatus.setText("网络不给力呀！");
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				getCashDataBean=new Gson().fromJson(arg2,GetCashDetailBean.class);
				if(getCashDataBean.getStatus().equals("-1006"))
				{
					//Toast.makeText(ProductListActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
					//Toast.makeText(getActivity(), "出错啦！", Toast.LENGTH_SHORT).show();
					MyDialog.showDialog(GetInComeActivity.this, "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(GetInComeActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				if(!getCashDataBean.getStatus().equals("1"))
				{
					Toast.makeText(GetInComeActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
				}
				else
				{
					if(!TextUtils.isEmpty(getCashDataBean.getData().getStatus()))
					{
						if(getCashDataBean.getData().getStatus().equals("1"))
						{
							payactiontext.setText("未支付");
							payactiontext.setTextColor(payactiontext.getResources().getColor(R.color.text_color_yellow));
						}
						if(getCashDataBean.getData().getStatus().equals("2"))
						{
							payactiontext.setText("已支付");
							payactiontext.setTextColor(payactiontext.getResources().getColor(R.color.text_color));
							printButton.setVisibility(View.VISIBLE);
						}
					}
					if(!TextUtils.isEmpty(getCashDataBean.getData().getTime()))
					{
						getcashdatatext.setText(Util.DataConVertMint(getCashDataBean.getData().getTime()));
					}
					if(!TextUtils.isEmpty(getCashDataBean.getData().getSubject()))
					{
						getcashtypetext.setText(getCashDataBean.getData().getSubject());
						if(getCashDataBean.getData().getSubject().equals("商家收款-刷卡支付"))
						{
							discountRellayout.setVisibility(View.GONE);
						}
					}
					if(!TextUtils.isEmpty(getCashDataBean.getData().getFee()))
					{
						getcashactiontext.setText("￥"+getCashDataBean.getData().getFee());
					}
					if(!TextUtils.isEmpty(getCashDataBean.getData().getBody()))
					{
						getcashmoneydiscriptedit.setText(getCashDataBean.getData().getBody());
					}
					if(!TextUtils.isEmpty(getCashDataBean.getData().getDiscount_shop_fee()))
					{
						discountTextView.setText("￥"+getCashDataBean.getData().getDiscount_shop_fee());
					}
					if(!TextUtils.isEmpty(getCashDataBean.getData().getShop_fee()))
					{
						incomeTextView.setText("￥"+getCashDataBean.getData().getShop_fee());
					}
				}
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				if(flag)
				{
					ColorDialog.dissmissProcessDialog();
				}
				else
				{
					getDiscountSwipeLayout.setRefreshing(false);
					referchStatus.setText("下拉更新订单状态");
				}
			}
		});
	}
	
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
                esc.addText("时间：        "+Util.DataConVert2(getCashDataBean.getData().getTime())+"\n");   //  打印文字    
                esc.addText("订单号：      "+getCashDataBean.getData().getNo()+"\n");   //  打印文字
                esc.addText("--------------------------------\n");   //  打印文字
                esc.addText("名称");   //  打印文字
                
                esc.addSetAbsolutePrintPosition((short) 310);
                esc.addText("金额\n");
                esc.addText("商家收款");
                esc.addSetAbsolutePrintPosition((short) 300);
            	esc.addText(getCashDataBean.getData().getFee()+"\n");
            	
                esc.addText("\n\n商家优惠");   //  打印文字
                esc.addSetAbsolutePrintPosition((short) 300);
                esc.addText(getCashDataBean.getData().getDiscount_shop_fee()+"\n");
                
                esc.addText("红包减免");
                esc.addSetAbsolutePrintPosition((short) 300);
                esc.addText(getCashDataBean.getData().getDiscount_activity_fee()+"\n");
                
                esc.addText("--------------------------------\n");   //  打印文字
                
                esc.addTurnEmphasizedModeOnOrOff(ENABLE.ON);//设置加粗
                esc.addText("实收金额");
                esc.addSetAbsolutePrintPosition((short) 300);
                esc.addText(getCashDataBean.getData().getFee()+"\n\n");   //  打印文字
                
                esc.addSelectJustification(JUSTIFICATION.CENTER);//设置打印居中
                esc.addTurnEmphasizedModeOnOrOff(ENABLE.OFF);
                //打印图片
//		          Bitmap b1 = BitmapFactory.decodeResource(getResources(),R.drawable.qcoerd);
//		          esc.addRastBitImage(b1, 200, 0);   //打印图片
//		          //设置二维码内容
//                esc.addStoreQRCodeData(qrcodeContent);
//                //设置二维码尺寸
//                esc.addSelectSizeOfModuleForQRCode((byte)6);
//                esc.addPrintQRCode();
//                esc.addText("\n使用微信扫描该二维码即可完成本次支付\n\n");
                esc.addText("\n"+share.getString("getagentname", "")+"竭诚为您服务\n\n"); 
                esc.addText("*******************************\n\n\n\n\n\n\n");
                Vector<Byte> datas = esc.getCommand(); //发送数据
                Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
                byte[] bytes = ArrayUtils.toPrimitive(Bytes);
                outputStream.write(bytes, 0, bytes.length);    
                outputStream.flush();  
                Toast.makeText(GetInComeActivity.this, "打印成功！", Toast.LENGTH_LONG).show();
            }
            catch (Exception e)
            {    
                Toast.makeText(GetInComeActivity.this, "打印失败！", Toast.LENGTH_LONG).show();    
            }    
        }
		else 
		{
            Toast.makeText(GetInComeActivity.this, "设备未连接，请重新连接！", Toast.LENGTH_SHORT).show();    
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
				Toast.makeText(GetInComeActivity.this,"打开蓝牙取消", Toast.LENGTH_SHORT).show();
			}
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
					Toast.makeText(GetInComeActivity.this,"打开成功", Toast.LENGTH_SHORT).show();
					ColorDialog.dissmissProcessDialog();
				}
				else
				{
					Toast.makeText(GetInComeActivity.this,"打开蓝牙失败", Toast.LENGTH_SHORT).show();
					ColorDialog.dissmissProcessDialog();
				}
			}
			if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action))
			{
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);  
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) 
                {    
                    //绑定过的设备    
                	Toast.makeText(GetInComeActivity.this,"配对成功", Toast.LENGTH_SHORT).show();
                } 
                if (device.getBondState() == BluetoothDevice.BOND_NONE)  
                {    
                    //未绑定的设备
                	Toast.makeText(GetInComeActivity.this,"配对失败", Toast.LENGTH_SHORT).show();
                }    
			}
		}
	};
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
  //查询订单
  	private void selectOrderStatus(final boolean flag) 
  	{
  		// TODO Auto-generated method stub
  		if(!flag)
  		{
  			referchStatus.setText("正在拼命刷新中");
  		}
  		RequestParams params = new RequestParams();
  		params.add("token", this.share.getString("token", ""));
  		params.add("order",no);
  		params.setContentEncoding("UTF-8");
  		AsyncHttpClient client = new AsyncHttpClient();
  		//保存cookie，自动保存到了shareprefercece  
          PersistentCookieStore myCookieStore = new PersistentCookieStore(GetInComeActivity.this);    
          client.setCookieStore(myCookieStore); 
          client.post(ConstantParamPhone.IP+ConstantParamPhone.GET_SCAN_ORDER, params,new TextHttpResponseHandler()
  		{

  			@Override
  			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
  			{
  				Toast.makeText(GetInComeActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
  				referchStatus.setText("下拉更新订单状态");
  			}
  			@Override
  			public void onSuccess(int arg0, Header[] arg1, String arg2) 
  			{
  				SelectScanOrderBean selectScanOrderBean=new Gson().fromJson(arg2,SelectScanOrderBean.class);
  				if(selectScanOrderBean.getReturn_code().equals("FAIL"))
  				{
  					//Toast.makeText(DiscountScanSucessActivity.this, scanInComeBean.getReturn_msg(), Toast.LENGTH_SHORT).show();
  					//setFailResult("scanInComeBean.getReturn_msg()");
  				}
  				if(selectScanOrderBean.getResult_code().equals("FAIL"))
  				{
  					//setFailResult("系统错误");
  				}
  				if(selectScanOrderBean.getResult_code().equals("SUCCESS")&&selectScanOrderBean.getReturn_code().equals("SUCCESS"))
  				{
  					if(!TextUtils.isEmpty(selectScanOrderBean.getTrade_state()))
  					{
  						//支付成功
  						if(selectScanOrderBean.getTrade_state().equals("SUCCESS"))
  						{
  						   //setSucessResult();
  						   //time.cancel();
  						}
  						//转入退款
  						if(selectScanOrderBean.getTrade_state().equals("REFUND"))
  						{
  							//time.cancel();
  						}
  						//未支付
  						if(selectScanOrderBean.getTrade_state().equals("NOTPAY"))
  						{
  							//setFailResult(selectScanOrderBean.getTrade_state_desc());
  							//time.cancel();
  						}
  						//已关闭
  						if(selectScanOrderBean.getTrade_state().equals("CLOSED"))
  						{
  							//setFailResult("订单已关闭");
  							//time.cancel();
  						}
  						//已撤销
  						if(selectScanOrderBean.getTrade_state().equals("REVOKED"))
  						{
  							//setFailResult("订单已撤销");
  							//time.cancel();
  						}
  						//等待用户输入密码
  						if(selectScanOrderBean.getTrade_state().equals("USERPAYING"))
  						{
  							//result.setText("用户支付中");
  						}
  						if(selectScanOrderBean.getTrade_state().equals("PAYERROR"))
  						{
  							if(TextUtils.isEmpty(selectScanOrderBean.getTrade_state_desc()))
  							{
  								//setFailResult("收款失败");
  							}
  							else
  							{
  								//setFailResult(selectScanOrderBean.getTrade_state_desc());
  							}
  							//time.cancel();
  						}
  					}
  					else
  					{
  						///setFailResult("收款失败");
  						//time.cancel();
  					}
  				}
  			}
  			
  			@Override
  			public void onFinish() 
  			{
  				// TODO Auto-generated method stub
  				initData(flag);
  			}
  		});
  	}

	
}
