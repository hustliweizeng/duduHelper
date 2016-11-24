package com.dudu.duduhelper.Activity.OrderActivity;

import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.Vector;

import org.apache.commons.lang.ArrayUtils;
import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Activity.PrinterActivity.ShopSearchBlueToothActivity;
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
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.gprinter.command.EscCommand;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.app.Notification;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.gprinter.command.EscCommand.ENABLE;
import com.dudu.duduhelper.R;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

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
	private TextView orderFeeTextView;
	private TextView orderdiscountTextView;
	private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private OrderDetailBean.DataBean orderData;
	private String id;
	private TextView order_create_time;
	private Button btn_print;
	private RelativeLayout rl_address;
	private TextView address;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_order_detail);
		initHeadView("订单详情",true, false,0);
		id = getIntent().getLongExtra("id",0)+"";
		LogUtil.d("id",id);
		orderDetailAdapter=new OrderDetailAdapter(this);
		initView();

	}

	/**
	 * 获取传送过来的参数
	 */
	@Override
	public void onResume() {
		super.onResume();
		Bundle bun = getIntent().getExtras();
		if (bun != null) {
			Set<String> keySet = bun.keySet();
			for (String key : keySet) {
				String value = bun.getString(key);
				id = value;
				LogUtil.d("recivew=id",value);
				
			}
		}
		initData();
	}

	private void initData() 
	{
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
				fillData();
			}
		});
	}

	private void fillData() {
		if(orderData == null){
			Toast.makeText(context,"没有要显示的数据",Toast.LENGTH_SHORT).show();
			return;
		}
		//判断是否显示地址

		

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
		if ("大牌抢购".equals(orderSourceTextView.getText())){
			String address1 = orderData.getAddress();
			if (!TextUtils.isEmpty(address1)){
				rl_address.setVisibility(View.VISIBLE);
				address.setText(address1);
			}
		}
		
		
		
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
		orderFeeTextView.setText(orderData.getTotal_fee());
		//折扣金额
		orderdiscountTextView.setText(orderData.getDiscount_shop_fee());
		//总额
		totalPriceTextView.setText(orderData.getFee());
		
	}


	private void initView() 
	{
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
		rl_address = (RelativeLayout) findViewById(R.id.rl_address);
		address = (TextView) findViewById(R.id.address);
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
		
		//
		
		
	}
	//核销订单
	private void changeStatus(String status) 
	{
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
		if (resultCode == 2) 
		{
			String adrress = data.getStringExtra("device");//获取返回的设备地址
			if (adrress!=null){
				device = bluetoothAdapter.getRemoteDevice(adrress);//设置设备
				LogUtil.d("get","接收");
				Toast.makeText(context,"蓝牙链接成功",Toast.LENGTH_SHORT).show();
				getConnect();
			}
		}
	}
	
	//发送打印信息
	private void sendPrint() 
	{

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
            }
            catch (Exception e)
            {    
                Toast.makeText(ShopOrderDetailActivity.this, "打印失败！", Toast.LENGTH_LONG).show();  
	            LogUtil.d("erro",e.toString());
            }    
        }
		else 
		{
            Toast.makeText(ShopOrderDetailActivity.this, "设备未连接，请重新连接！", Toast.LENGTH_SHORT).show();   
        }
		enterButton.setClickable(true);
		enterButton.setPressed(false);
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
	            e.printStackTrace();
                Toast.makeText(this, "蓝牙小票机没有配对，请配对后再打印小票！", Toast.LENGTH_LONG).show(); 
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
			//打开蓝牙并提示用户
			Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);     
			// 设置蓝牙可见性，最多300秒      
			intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3000);     
			startActivity(intent);
			//开始搜索
			bluetoothAdapter.startDiscovery();
		}
		//蓝牙已开启
		else
		{
			//判断已经绑定的设备是否可用
			if (bluetoothAdapter.getBondedDevices()!=null &&bluetoothAdapter.getBondedDevices().size()>0)
			{
				btn_print.setClickable(false);
				btn_print.setPressed(true);
				//根据名称获取蓝牙地址,已绑定的设备是个列表,需要循环遍历设备打印
				for (BluetoothDevice item :bluetoothAdapter.getBondedDevices()){
					if (item.getBondState() ==BluetoothDevice.BOND_BONDED  ){
						//判断当前设备是否可用
						//尝试链接看是否可用
						try {
							bluetoothSocket = item.createRfcommSocketToServiceRecord(uuid);
							bluetoothSocket.connect();//尝试链接设备
						} catch (Exception e) {
							//e.printStackTrace();
							LogUtil.d("next",item.getName());
							continue;//继续下次循环
						}
						//当这个设备可用时
						device = item;
						isConnection = true;
						LogUtil.d("useable",device.getName()+"yes");
						break;//跳出循环
					}
				}
				sendPrint();//打印
			}
			else
			{
				//第一次连接，进入到搜索绑定页面
				Intent intent=new Intent(ShopOrderDetailActivity.this,ShopSearchBlueToothActivity.class);
				intent.putExtra("source","orderDetail");
				startActivityForResult(intent,2);
			}
		}
    }
	
}
