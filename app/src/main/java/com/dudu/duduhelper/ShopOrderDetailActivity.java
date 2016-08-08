package com.dudu.duduhelper;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.lang.ArrayUtils;
import org.apache.http.Header;

import com.dudu.duduhelper.adapter.OrderDetailAdapter;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.bean.OrderDetailBean;
import com.dudu.duduhelper.bean.OrderGoods;
import com.dudu.duduhelper.bean.ResponsBean;
import com.dudu.duduhelper.common.Util;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.gprinter.command.EscCommand;
import com.gprinter.command.EscCommand.ENABLE;
import com.gprinter.command.EscCommand.FONT;
import com.gprinter.command.EscCommand.HEIGHT_ZOOM;
import com.gprinter.command.EscCommand.JUSTIFICATION;
import com.gprinter.command.EscCommand.WIDTH_ZOOM;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShopOrderDetailActivity extends BaseActivity 
{
	private TextView orderNumTextView;
	private TextView orderSourceTextView;
	private TextView orderActionTextView;
	private TextView orderPayTypeTextView;
	private TextView orderNameTextView;
	private TextView orderContrectTextView;
	private TextView orderPhoneTextView;
	private ListView orderDetailList;
	private TextView totalPriceTextView;
	private Button noButton;
	private Button enterButton;
	private String no;
	private String action;
	private OrderDetailAdapter orderDetailAdapter;
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothDevice device;
	private static BluetoothSocket bluetoothSocket = null;    
	private static OutputStream outputStream = null;  
	private boolean isConnection = false; 
	private OrderDetailBean orderDetailBean;
	private TextView orderFeeTextView;
	private TextView orderdiscountTextView;
	private TextView couponNumTextView;
	private TextView couponStatusTextView;
	private RelativeLayout couponNumLin;
	private RelativeLayout couponStatusLin;
	private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");    
	

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_order_detail);
		initHeadView("订单详情",true, false,0);
		no=getIntent().getStringExtra("no");
		action=getIntent().getStringExtra("status");
		orderDetailAdapter=new OrderDetailAdapter(this);
		DuduHelperApplication.getInstance().addActivity(this);
		initFilter();
		initView();
		initData();
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

	private void initData() 
	{
		// TODO Auto-generated method stub
		ColorDialog.showRoundProcessDialog(this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("no",no);
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(ShopOrderDetailActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.GET_ORDER_DETAIL, params,new TextHttpResponseHandler()
		{
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(ShopOrderDetailActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				orderDetailBean=new Gson().fromJson(arg2,OrderDetailBean.class);
				if(orderDetailBean.getStatus().equals("-1006"))
				{
					//Toast.makeText(ProductListActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
					//Toast.makeText(getActivity(), "出错啦！", Toast.LENGTH_SHORT).show();
					MyDialog.showDialog(ShopOrderDetailActivity.this, "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(ShopOrderDetailActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				if(!orderDetailBean.getStatus().equals("1"))
				{
					Toast.makeText(ShopOrderDetailActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
				}
				else
				{
					orderNumTextView.setText(orderDetailBean.getData().getNo());
					if(orderDetailBean.getData().getSource()!=null)
					{
						if(!TextUtils.isEmpty(orderDetailBean.getData().getSource().getName()))
						{
							orderSourceTextView.setText(orderDetailBean.getData().getSource().getName());
						}
					}
					
					if(!TextUtils.isEmpty(orderDetailBean.getData().getSource().getId()))
					{
						if(orderDetailBean.getData().getSource().getId().equals("coupon"))
						{
							couponStatusLin.setVisibility(View.VISIBLE);
							if(!TextUtils.isEmpty(orderDetailBean.getData().getCoupon().getSn()))
							{
								couponNumLin.setVisibility(View.VISIBLE);
								couponNumTextView.setText(orderDetailBean.getData().getCoupon().getSn());
							}	
							
							if((System.currentTimeMillis()-Long.parseLong(orderDetailBean.getData().getCoupon().getExptime()))>0)
							{
								couponStatusTextView.setText("已过期");
								couponStatusTextView.setTextColor(couponStatusTextView.getResources().getColor(R.color.text_color_light));
							}
//							else
//							{
//								couponStatusTextView.setText("未验证");
//								couponStatusTextView.setTextColor(couponStatusTextView.getResources().getColor(R.color.text_color_yellow));
//							}
							
							if(!orderDetailBean.getData().getCoupon().getVerifytime().equals("0"))
							{
								couponStatusTextView.setText("已验证");
								couponStatusTextView.setTextColor(couponStatusTextView.getResources().getColor(R.color.text_color));
							}
							else
							{
								couponStatusTextView.setText("未验证");
								couponStatusTextView.setTextColor(couponStatusTextView.getResources().getColor(R.color.text_color_yellow));
							}
							
						}
					}
					
					
//					if(!orderDetailBean.getData().getCoupon().getVerifytime())
//					{
//						
//					}
					
					if(orderDetailBean.getData().getStatus().equals("-1"))
					{
						orderActionTextView.setText("已过期");
						orderActionTextView.setTextColor(orderActionTextView.getResources().getColor(R.color.text_color_light));
						enterButton.setVisibility(View.GONE);
						noButton.setVisibility(View.GONE);
					}
					if(orderDetailBean.getData().getStatus().equals("0"))
					{
						orderActionTextView.setText("已取消");
						orderActionTextView.setTextColor(orderActionTextView.getResources().getColor(R.color.text_color_light));
						enterButton.setVisibility(View.GONE);
						noButton.setVisibility(View.GONE);
					}
					if(orderDetailBean.getData().getStatus().equals("1"))
					{
						orderActionTextView.setText("未支付");
						orderActionTextView.setTextColor(orderActionTextView.getResources().getColor(R.color.text_color_yellow));
						enterButton.setVisibility(View.GONE);
						noButton.setVisibility(View.GONE);
					}
					if(orderDetailBean.getData().getStatus().equals("2"))
					{
						orderActionTextView.setText("已支付");
						orderActionTextView.setTextColor(orderActionTextView.getResources().getColor(R.color.text_color));
						
						if(orderDetailBean.getData().getSource()!=null)
						{
							if(orderDetailBean.getData().getSource().getId().equals("coupon"))
							{
								enterButton.setVisibility(View.VISIBLE);
								enterButton.setText("打印凭据");
								noButton.setVisibility(View.GONE);
								enterButton.setOnClickListener(new OnClickListener() 
								{
									@Override
									public void onClick(View v) 
									{
										// TODO Auto-generated method stub
										OpenBlueTooth();
									}
								});
							}
							else
							{
								enterButton.setVisibility(View.VISIBLE);
								noButton.setVisibility(View.VISIBLE);
							}
						}
					}
					if(orderDetailBean.getData().getStatus().equals("3"))
					{
						orderActionTextView.setText("已完成");
						orderActionTextView.setTextColor(orderActionTextView.getResources().getColor(R.color.text_color_dark));
						enterButton.setVisibility(View.VISIBLE);
						enterButton.setText("打印凭据");
						noButton.setVisibility(View.GONE);
						enterButton.setOnClickListener(new OnClickListener() 
						{
							@Override
							public void onClick(View v) 
							{
								// TODO Auto-generated method stub
								OpenBlueTooth();
							}
						});
					}
					if(!TextUtils.isEmpty(orderDetailBean.getData().getTotal_fee()))
					{
						orderFeeTextView.setText("￥"+orderDetailBean.getData().getTotal_fee());
					}
					if(!TextUtils.isEmpty(orderDetailBean.getData().getDiscount_shop_fee()))
					{
						orderdiscountTextView.setText("￥"+orderDetailBean.getData().getDiscount_shop_fee());
					}
					orderPayTypeTextView.setText(orderDetailBean.getData().getPaytype().getName());
					orderNameTextView.setText(orderDetailBean.getData().getSubject());
					if(!TextUtils.isEmpty(orderDetailBean.getData().getName()))
					{
					    orderContrectTextView.setText(orderDetailBean.getData().getName());
					}
					if(!TextUtils.isEmpty(orderDetailBean.getData().getMobile()))
					{
					    orderPhoneTextView.setText(orderDetailBean.getData().getMobile());
					}
					totalPriceTextView.setText("￥"+orderDetailBean.getData().getFee());
					if(orderDetailBean.getData().getGoods()!=null)
					{
						orderDetailAdapter.addAll(orderDetailBean.getData().getGoods());
					}
					orderDetailList.setAdapter(orderDetailAdapter);
					if(orderDetailBean.getData().getSource()!=null)
					{
						if(orderDetailBean.getData().getSource().getId().equals("membercard"))
						{
							orderDetailList.setVisibility(View.GONE);
						}
					}
					setListViewHeightBasedOnChildren(orderDetailList);
				}
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				ColorDialog.dissmissProcessDialog();
			}
		});
	}
	public void setListViewHeightBasedOnChildren(ListView listView) {   
        // 获取ListView对应的Adapter   
		OrderDetailAdapter orderDetailAdapter = (OrderDetailAdapter) listView.getAdapter();   
        if (orderDetailAdapter == null) {   
            return;   
        }   
   
        int totalHeight = 0;   
        for (int i = 0, len = orderDetailAdapter.getCount(); i < len; i++) {   
            // listAdapter.getCount()返回数据项的数目   
            View listItem = orderDetailAdapter.getView(i, null, listView);   
            // 计算子项View 的宽高   
            listItem.measure(0, 0);    
            // 统计所有子项的总高度   
            totalHeight += listItem.getMeasuredHeight();    
        }   
   
        ViewGroup.LayoutParams params = listView.getLayoutParams();   
        params.height = totalHeight+ (listView.getDividerHeight() * (orderDetailAdapter.getCount() - 1));   
        // listView.getDividerHeight()获取子项间分隔符占用的高度   
        // params.height最后得到整个ListView完整显示需要的高度   
        listView.setLayoutParams(params);   
    }   

	private void initView() 
	{
		// TODO Auto-generated method stub
		couponNumLin=(RelativeLayout) this.findViewById(R.id.couponNumLin);
		couponStatusLin=(RelativeLayout) this.findViewById(R.id.couponStatusLin);
		couponNumTextView=(TextView) this.findViewById(R.id.couponNumTextView);
		couponStatusTextView=(TextView) this.findViewById(R.id.couponStatusTextView);
		orderFeeTextView=(TextView) this.findViewById(R.id.orderFeeTextView);
		orderdiscountTextView=(TextView) this.findViewById(R.id.orderdiscountTextView);
		enterButton=(Button) this.findViewById(R.id.enterButton);
		noButton=(Button)this.findViewById(R.id.noButton);
		orderDetailList=(ListView) this.findViewById(R.id.orderDetailList);
		orderNumTextView=(TextView) this.findViewById(R.id.orderNumTextView);
		orderSourceTextView=(TextView) this.findViewById(R.id.orderSourceTextView);
		orderActionTextView=(TextView) this.findViewById(R.id.orderActionTextView);
		orderPayTypeTextView=(TextView) this.findViewById(R.id.orderPayTypeTextView);
		orderNameTextView=(TextView) this.findViewById(R.id.orderNameTextView);
		orderContrectTextView=(TextView) this.findViewById(R.id.orderContrectTextView);
		orderPhoneTextView=(TextView) this.findViewById(R.id.orderPhoneTextView);
		totalPriceTextView=(TextView) this.findViewById(R.id.totalPriceTextView);
		enterButton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				MyDialog.showDialog(ShopOrderDetailActivity.this, "  确认要核销订单号为"+orderNumTextView.getText().toString()+"的订单吗？",true, true, "取消", "确认核销", new OnClickListener() {
					
					@Override
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub
						MyDialog.cancel();
					}
				}, new OnClickListener() {
					
					@Override
					public void onClick(View v) 
					{
						// TODO Auto-generated method stub
						changeStatus("3");
					}
				});
			}

			
		});
		noButton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				changeStatus("0");
			}
		});
	}
	private void changeStatus(String status) 
	{
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		ColorDialog.showRoundProcessDialog(this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("no",no);
		params.add("status",status);
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(ShopOrderDetailActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.post(ConstantParamPhone.IP+ConstantParamPhone.EDIT_ORDER_STATUS, params,new TextHttpResponseHandler()
		{
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(ShopOrderDetailActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				ResponsBean responsBean=new Gson().fromJson(arg2,ResponsBean.class);
				if(!responsBean.getStatus().equals("1"))
				{
					Toast.makeText(ShopOrderDetailActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(ShopOrderDetailActivity.this, "修改成功啦", Toast.LENGTH_SHORT).show();
					Intent intent=new Intent();  
	                setResult(RESULT_OK, intent);  
	                finish();
				}
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				ColorDialog.dissmissProcessDialog();
				Intent intent=new Intent();  
                setResult(RESULT_OK, intent);  
                finish();
			}
		});
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
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
				Toast.makeText(ShopOrderDetailActivity.this,"打开蓝牙取消", Toast.LENGTH_SHORT).show();
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
					Toast.makeText(ShopOrderDetailActivity.this,"打开蓝牙成功", Toast.LENGTH_SHORT).show();
					ColorDialog.dissmissProcessDialog();
				}
				else
				{
					Toast.makeText(ShopOrderDetailActivity.this,"打开蓝牙失败", Toast.LENGTH_SHORT).show();
					ColorDialog.dissmissProcessDialog();
				}
			}
			if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action))
			{
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);  
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) 
                {    
                    //绑定过的设备    
                	Toast.makeText(ShopOrderDetailActivity.this,"配对成功", Toast.LENGTH_SHORT).show();
                	enterButton.setClickable(true);
                	enterButton.setPressed(false);
                } 
                if (device.getBondState() == BluetoothDevice.BOND_NONE)  
                {    
                    //未绑定的设备
                	Toast.makeText(ShopOrderDetailActivity.this,"配对失败", Toast.LENGTH_SHORT).show();
                	enterButton.setClickable(true);
                	enterButton.setPressed(false);
                }    
			}
		}
	};
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
                esc.addText("时间：        "+Util.DataConVert2(orderDetailBean.getData().getTime())+"\n");   //  打印文字    
                esc.addText("订单号：      "+orderDetailBean.getData().getNo()+"\n");   //  打印文字
                esc.addText("--------------------------------\n");   //  打印文字
                esc.addText("名称");   //  打印文字
                //设置距左边绝对位置
                esc.addSetAbsolutePrintPosition((short) 200);
                esc.addText("数量");
                //设置距左边绝对位置
                esc.addSetAbsolutePrintPosition((short) 310);
                esc.addText("金额\n");
                for (OrderGoods good : orderDetailBean.getData().getGoods()) 
                {
                	if(good.getName().length()>8)
                	{
                		esc.addText(good.getName().substring(0, 7)+"\n");
                		esc.addText(good.getName().substring(7, good.getName().length()));
                	}
                	else
                	{
                		esc.addText(good.getName());
                	}
                	esc.addSetAbsolutePrintPosition((short) 210);
                	esc.addText(good.getNum());
                	esc.addSetAbsolutePrintPosition((short) 300);
                	esc.addText(good.getFee()+"\n");
				}
                
                esc.addText("\n\n商家优惠");   //  打印文字
                esc.addSetAbsolutePrintPosition((short) 300);
                esc.addText(orderDetailBean.getData().getDiscount_shop_fee()+"\n");
                
                esc.addText("红包减免");
                esc.addSetAbsolutePrintPosition((short) 300);
                esc.addText(orderDetailBean.getData().getDiscount_activity_fee()+"\n");
                
                esc.addText("--------------------------------\n");   //  打印文字
                
                esc.addTurnEmphasizedModeOnOrOff(ENABLE.ON);//设置加粗
                esc.addText("实收金额");
                esc.addSetAbsolutePrintPosition((short) 300);
                esc.addText(orderDetailBean.getData().getFee()+"\n\n");   //  打印文字
                
                esc.addSelectJustification(JUSTIFICATION.CENTER);//设置打印居中
                esc.addTurnEmphasizedModeOnOrOff(ENABLE.OFF);
                //打印图片
//                Bitmap b1 = BitmapFactory.decodeResource(getResources(),R.drawable.qcoerd);
//                esc.addRastBitImage(b1, 200, 0);   //打印图片
//              
//                //设置二维码内容
//                esc.addStoreQRCodeData("复杂二维码复杂二维码复杂二维码复杂二维码复杂二维码复杂二维码复杂二维码复杂二维码复杂二维码复杂二维码复杂二维码复杂二维码复杂二维码复杂二维码复杂二维码");
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
                Toast.makeText(ShopOrderDetailActivity.this, "打印成功！", Toast.LENGTH_LONG).show();
                enterButton.setClickable(true);
                enterButton.setPressed(false);
            }
            catch (Exception e)
            {    
                Toast.makeText(ShopOrderDetailActivity.this, "打印失败！", Toast.LENGTH_LONG).show();  
                enterButton.setClickable(true);
                enterButton.setPressed(false);
            }    
        }
		else 
		{
            Toast.makeText(ShopOrderDetailActivity.this, "设备未连接，请重新连接！", Toast.LENGTH_SHORT).show();   
            enterButton.setClickable(true);
            enterButton.setPressed(false);
        }    
	}
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
                enterButton.setClickable(true);
                enterButton.setPressed(false);
                return;
            }    
            //Toast.makeText(this, this.device.getName() + "连接成功！", Toast.LENGTH_SHORT).show();   
        } 
        sendPrint();
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
    public void OpenBlueTooth()
    {
    	bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
		if(bluetoothAdapter==null)
		{
			Toast.makeText(ShopOrderDetailActivity.this,"该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
			return;
		}
		
		else if(!bluetoothAdapter.isEnabled())//isEnabled enable是激活,开启蓝牙
		{
			//开启
			ColorDialog.showRoundProcessDialog(ShopOrderDetailActivity.this,R.layout.loading_process_dialog_color);
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
				enterButton.setClickable(false);
				enterButton.setPressed(true);
				
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
						Toast.makeText(ShopOrderDetailActivity.this, "配对失败！", Toast.LENGTH_SHORT).show(); 
						enterButton.setClickable(true);
						enterButton.setPressed(false);
					}  
				}
				else
				{
					getConnect();
				}
			}
			else
			{
				Intent intent=new Intent(ShopOrderDetailActivity.this,ShopSearchBlueToothActivity.class);
				startActivity(intent);
			}
		}
    }
}
