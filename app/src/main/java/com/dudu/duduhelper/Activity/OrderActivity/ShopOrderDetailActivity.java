package com.dudu.duduhelper.Activity.OrderActivity;

import java.io.OutputStream;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.lang.ArrayUtils;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.dudu.duduhelper.Activity.PrinterActivity.BlutoothUtils;
import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Activity.PrinterActivity.ShopSearchBlueToothActivity;
import com.dudu.duduhelper.GpService;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.adapter.OrderDetailAdapter;
import com.dudu.duduhelper.bean.OrderGoods;
import com.dudu.duduhelper.bean.ResponsBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.OrderDetailBean;
import com.dudu.duduhelper.javabean.OrderStatusBean;
import com.dudu.duduhelper.javabean.SelectorBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.gprinter.command.EscCommand;
import com.gprinter.command.GpCom;
import com.gprinter.io.GpDevice;
import com.gprinter.service.GpPrintService;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.gprinter.command.EscCommand.ENABLE;

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
	private OrderDetailBean.DataBean orderData;
	private String id;
	private TextView order_create_time;
	private Button btn_print;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_order_detail);
		initHeadView("订单详情",true, false,0);
		id = getIntent().getLongExtra("id",0)+"";
		LogUtil.d("id",id);
		orderDetailAdapter=new OrderDetailAdapter(this);
		initFilter();
		initView();
		initData();
		
		//开启打印服务
		/*startService();
		connection();*/
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

		IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
		registerReceiver(receiver, filter);
		filter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
		registerReceiver(receiver, filter);
	}

	private void initData() 
	{
		ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
		if (TextUtils.isEmpty(id)){
			return;
		}
		RequestParams params = new RequestParams();
		HttpUtils.getConnection(context, params, ConstantParamPhone.GET_ORDER_DETAIL+id, "GET", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络错误，稍后再试",Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						LogUtil.d("detail",s);
						orderData  = new Gson().fromJson(s,OrderDetailBean.class).getData();
						
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
				fillData();
			}
		});
	}

	private void fillData() {
		if(orderData == null){
			Toast.makeText(context,"没有要显示的数据",Toast.LENGTH_SHORT).show();
			return;
		}

		//设置店名
		orderNameTextView.setText(orderData.getSubject());
		int color = R.color.text_dark_color;
		String content =null;
		switch (Integer.parseInt(orderData.getStatus())){
			case 0 :
				content = "已取消";
				color = R.color.text_gray_color;
				break;
			case 1 :
				content = "待支付";
				color = R.color.text_color_yellow;
				break;
			case 2 :
				content = "已支付";
				color = R.color.text_green_color;
				
				break;
			case 3 :
				content = "已完成";
				color = R.color.text_gray_color;
				btn_print.setVisibility(View.VISIBLE);
				break;
			case 4 :
				content = "待发货";
				break;
			case 5 :
				content = "待收货";
				break;
			case 6 :
				content = "待核销";
				break;
			case 7 :
				content = "待评价";
				break;
			case -1 :
				content = "已过期";
				color = R.color.text_gray_color;
				break;
			case -2 :
				content = "已退款";
				break;
			default:
				//其他状态时，不可打印
				btn_print.setVisibility(View.GONE);
		}
		//设置状态
		orderActionTextView.setText(content);
		//设置颜色
		orderActionTextView.setTextColor(getResources().getColor(color));
		//订单号
		orderNumTextView.setText(orderData.getId());
		//订单来源
		List<SelectorBean> source = new OrderStatusBean().getAllOrderSource();
		for (SelectorBean bean :source){
			if (bean.id ==Integer.parseInt(orderData.getFrom())){
				orderSourceTextView.setText(bean.name);
			}
		}
		/*//设置核销按钮
		if (content.equals("已支付")&& orderSourceTextView.getText().equals("优惠券")){
			enterButton.setVisibility(View.VISIBLE);	
		}*/
		if (content.equals("已支付") &&orderSourceTextView.getText().equals("大牌抢购")){
			//设置核销按钮可见
			enterButton.setVisibility(View.VISIBLE);
			noButton.setVisibility(View.VISIBLE);
		}
		//下单时间
		order_create_time.setText(Util.DataConVertMint(orderData.getTime()));
		//联系人
		orderContrectTextView.setText(orderData.getName());
		//电话
		orderPhoneTextView.setText(orderData.getMobile());
		//订单金额
		orderFeeTextView.setText(orderData.getFee());
		//折扣金额
		orderdiscountTextView.setText(orderData.getDiscount_shop_fee());
		//总额
		totalPriceTextView.setText(orderData.getTotal_fee());
		
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
		orderNameTextView=(TextView) this.findViewById(R.id.orderNameTextView);
		orderContrectTextView=(TextView) this.findViewById(R.id.orderContrectTextView);
		orderPhoneTextView=(TextView) this.findViewById(R.id.orderPhoneTextView);
		totalPriceTextView=(TextView) this.findViewById(R.id.totalPriceTextView);
		order_create_time = (TextView) findViewById(R.id.order_create_time);
		btn_print = (Button) findViewById(R.id.btn_print);
		btn_print.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				OpenBlueTooth();
			}
		});
		
		//核销按钮
		enterButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				MyDialog.showDialog(ShopOrderDetailActivity.this, "  确认要核销订单号为"+orderNumTextView.getText().toString()+"的订单吗？",true, true, "取消", "确认核销", new OnClickListener() {
					
					@Override
					public void onClick(View v) 
					{
						MyDialog.cancel();
					}
				}, new OnClickListener() {
					
					@Override
					public void onClick(View v) 
					{
						changeStatus("3");
					}
				});
			}
			
		});
		//取消按钮
		noButton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				changeStatus("0");
			}
		});
	}
	//核销订单
	private void changeStatus(String status) 
	{
		ColorDialog.showRoundProcessDialog(this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.put("id",orderData.getId());
		params.put("status",status);
        HttpUtils.getConnection(context,params,ConstantParamPhone.CHANGE_ORDER_STATUS, "post",new TextHttpResponseHandler()
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
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) 
		{
			if (resultCode == RESULT_OK) 
			{
				//蓝牙已经开启
				Toast.makeText(context,"蓝牙已开启",Toast.LENGTH_SHORT).show();
				
			} 
			else if (resultCode == RESULT_CANCELED) 
			{
				ColorDialog.dissmissProcessDialog();
				Toast.makeText(ShopOrderDetailActivity.this,"打开蓝牙取消", Toast.LENGTH_SHORT).show();
			}
		}
	}
	//广播接收者
	//绑定广播过滤器--开关状态和设备绑定，发生其他状态的广播时由另外一个接收者负责处理
	private BroadcastReceiver receiver=new BroadcastReceiver() 
	{
		@Override
		public void onReceive(Context context, Intent intent) 
		{
			//根据不同的action做出响应
			String action = intent.getAction(); 
			//蓝牙开启状态
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
			//找到设备  
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				LogUtil.d("device","found");

				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				LogUtil.d("device",device.getName());


				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

					Log.v("", "find device:" + device.getName()
							+ device.getAddress());
				}
			}
			//绑定状态改变
			if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action))
			{
				LogUtil.d("device","change");

				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);  
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) 
                {
	                LogUtil.d("device",device.getName()+"bond");

	                //绑定过的设备    
                	Toast.makeText(ShopOrderDetailActivity.this,"配对成功", Toast.LENGTH_SHORT).show();
                	/*enterButton.setClickable(true);
                	enterButton.setPressed(false);*/
	                //连接打印机
	               getConnect();
                } 
                if (device.getBondState() == BluetoothDevice.BOND_NONE)  
                {
	                LogUtil.d("device","unbond");

	                //未绑定的设备
                	Toast.makeText(ShopOrderDetailActivity.this,"配对失败", Toast.LENGTH_SHORT).show();
                	enterButton.setClickable(true);
                	enterButton.setPressed(false);
                }    
			}


			//找到设备  
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				LogUtil.d("device","found");
				BluetoothDevice device = intent
						.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

				if (device.getBondState() != BluetoothDevice.BOND_BONDED) {

					Log.v("", "find device:" + device.getName()
							+ device.getAddress());
				}
				
				
				
			}
			//搜索完成  
			else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
					.equals(action)) {
				Toast.makeText(context,"没有发现设备",Toast.LENGTH_SHORT).show();
			}

		}
	};
	//发送打印信息
	private void sendPrint() 
	{
		/*getPrintStatus();
		//获取打印模式
		getMode();
		//打印测试也
		try {
			int rel = mGpService.printeTestPage(mPrinterIndex); //
			Log.i("ServiceConnection", "rel " + rel);
			GpCom.ERROR_CODE r=GpCom.ERROR_CODE.values()[rel];
			if(r != GpCom.ERROR_CODE.SUCCESS){
				Toast.makeText(getApplicationContext(),GpCom.getErrorText(r),
						Toast.LENGTH_SHORT).show();
			}
		} catch (RemoteException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
		
		if (this.isConnection) 
		{    
            System.out.println("开始打印！！");    
            try 
            { 
                EscCommand esc = new EscCommand();
                //设置汉子无效
                //esc.addCancelKanjiMode();
                esc.addPrintAndLineFeed();
                esc.addSelectKanjiMode();
                //esc.addSelectJustification(JUSTIFICATION.LEFT);//设置打印左对齐
                esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);//设置打印居中
                esc.addSelectPrintModes(EscCommand.FONT.FONTA, ENABLE.OFF, ENABLE.ON, ENABLE.OFF, ENABLE.OFF);//设置为倍高倍宽
                esc.addText("欢迎光临\n");   //  打印文字
                esc.addText(sp.getString("shopName", "本店铺")+"\n\n");
                esc.addSelectPrintModes(EscCommand.FONT.FONTA, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF);//设置为倍高倍宽
                //esc.addSetCharcterSize(WIDTH_ZOOM.MUL_3, HEIGHT_ZOOM.MUL_3);//设置字符尺寸
                esc.addSelectJustification(EscCommand.JUSTIFICATION.LEFT);//设置打印左对齐
                esc.addText("时间：   "+ Util.DataConVertMint(orderData.getTime())+"\n");   //  打印文字    
                esc.addText("订单号：  "+orderData.getId()+"\n");   //  打印文字
                esc.addText("--------------------------------\n");   //  打印文字
                esc.addText("名称");   //  打印文字
                //设置距左边绝对位置
                esc.addSetAbsolutePrintPosition((short) 200);
                esc.addText("数量");
                //设置距左边绝对位置
                esc.addSetAbsolutePrintPosition((short) 310);
                esc.addText("金额\n");
	            //打印商品详情
	            if (orderData.getGoods()!=null && orderData.getGoods().size()>0){

		            for (OrderGoods good : orderData.getGoods())
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
	            }
                
                esc.addText("\n\n商家优惠");   //  打印文字
                esc.addSetAbsolutePrintPosition((short) 300);
                esc.addText(orderData.getDiscount_shop_fee()+"\n");
                
                esc.addText("红包减免");
                esc.addSetAbsolutePrintPosition((short) 300);
                esc.addText(orderData.getDiscount_activity_fee()+"\n");
                
                esc.addText("--------------------------------\n");   //  打印文字
                
                esc.addTurnEmphasizedModeOnOrOff(ENABLE.ON);//设置加粗
                esc.addText("实收金额");
                esc.addSetAbsolutePrintPosition((short) 300);
                esc.addText(orderData.getFee()+"\n\n");   //  打印文字
                
                esc.addSelectJustification(EscCommand.JUSTIFICATION.CENTER);//设置打印居中
                esc.addTurnEmphasizedModeOnOrOff(ENABLE.OFF);
                esc.addText("\n"+share.getString("getagentname", "")+"竭诚为您服务\n\n"); 
                esc.addText("*******************************\n\n\n\n\n\n\n");

	            




	            Vector<Byte> datas = esc.getCommand(); //发送数据
                Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
                byte[] bytes = ArrayUtils.toPrimitive(Bytes);

		            //输出流
	            outputStream = bluetoothSocket.getOutputStream();
	            outputStream.write(bytes, 0, bytes.length);    
                outputStream.flush();  
	            
	            
                Toast.makeText(ShopOrderDetailActivity.this, "打印成功！", Toast.LENGTH_LONG).show();
                enterButton.setClickable(true);
                enterButton.setPressed(false);
            }
            catch (Exception e)
            {    
                Toast.makeText(ShopOrderDetailActivity.this, "打印失败！", Toast.LENGTH_LONG).show();  
	            LogUtil.d("erro",e.toString());
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

	

	

	//绑定之后获取连接
	private void getConnect()
	{
		if (!this.isConnection) 
		{    
            try
            {
	            //客户端对象
                bluetoothSocket = this.device.createRfcommSocketToServiceRecord(uuid);   
	            //连接设备
                bluetoothSocket.connect();  
	            
                this.isConnection = true;    
                if (this.bluetoothAdapter.isDiscovering()) 
                {    
                	//取消搜索
	                bluetoothAdapter.cancelDiscovery();
                }    
            } 
            catch (Exception e) 
            {    
                Toast.makeText(this, "连接失败,请检查打印机是否开启！", Toast.LENGTH_LONG).show(); 
                enterButton.setClickable(true);
                enterButton.setPressed(false);
                return;
            }    
        } 
        sendPrint();
	}
    @Override
    protected void onDestroy() 
    {
    	//关闭蓝牙的输出流
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
	//打开蓝牙，打印信息
    public void OpenBlueTooth()
    {
	    //获取蓝牙适配器
    	bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
	    //不支持蓝牙
		if(bluetoothAdapter==null)
		{
			Toast.makeText(ShopOrderDetailActivity.this,"该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
			return;
		}
		//支持蓝牙未开启
		 if(!bluetoothAdapter.isEnabled())//isEnabled enable是激活,开启蓝牙
		{
			//开启
			ColorDialog.showRoundProcessDialog(ShopOrderDetailActivity.this,R.layout.loading_process_dialog_color);
			//打开蓝牙并提示用户
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);     
			// 设置蓝牙可见性，最多300秒      
			intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3000);     
			startActivityForResult(intent, 1);
			//开始搜索
			bluetoothAdapter.startDiscovery();
			LogUtil.d("unacitive","unacitive");
		}
		//蓝牙已开启
		else
		{
			LogUtil.d("acitive","acitive");
			SharedPreferences sharePrint= getSharedPreferences("printinfo", MODE_PRIVATE);
			//判断是否绑定过
			//if(!TextUtils.isEmpty(sharePrint.getString("blueAddress", "")))
			//判断已经绑定的设备
			if (bluetoothAdapter.getBondedDevices()!=null &&bluetoothAdapter.getBondedDevices().size()>0)
			{
				btn_print.setClickable(false);
				btn_print.setPressed(true);
				//根据名称获取蓝牙地址
				//device=bluetoothAdapter.getRemoteDevice(sharePrint.getString("blueAddress", ""));
				for (BluetoothDevice item :bluetoothAdapter.getBondedDevices()){
					if (item.getBondState() ==BluetoothDevice.BOND_BONDED ){
						//当这个设备已经绑定时
						device = item;
						break;
					}
				}
				LogUtil.d("bind",device.getName()+"yes");
				getConnect();
				
				/*//未绑定
				if(device.getBondState() == BluetoothDevice.BOND_NONE)
				{
					LogUtil.d("bind","no");
					try 
					{
						Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
						//通过反射绑定该设备
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
					LogUtil.d("bind","yes");

					//绑定了
					getConnect();
				}*/
			}
			else
			{
				//第一次连接，进入到搜索绑定页面
				Intent intent=new Intent(ShopOrderDetailActivity.this,ShopSearchBlueToothActivity.class);
				startActivity(intent);
			}
		}
    }
	
	
}
