package com.dudu.duduhelper.Activity.OrderActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
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

import com.baidu.tts.auth.AuthInfo;
import com.baidu.tts.client.SpeechError;
import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Activity.PrinterActivity.ShopSearchBlueToothActivity;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.adapter.OrderDetailAdapter;
import com.dudu.duduhelper.bean.OrderGoods;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.OrderDetailBean;
import com.dudu.duduhelper.javabean.OrderStatusBean;
import com.dudu.duduhelper.javabean.SelectorBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.google.gson.Gson;
import com.gprinter.command.EscCommand;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
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

public class ShopOrderDetailActivity extends BaseActivity implements SpeechSynthesizerListener
{
	private TextView orderNumTextView;
	private TextView orderSourceTextView;
	private TextView orderActionTextView;
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
	private boolean isNetNotification;
	private boolean isTypeAutoPrint;//订单来源是否需要自动打印
	private boolean isFinishing;
	private TextView order_Num;
	private TextView order_dis;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		initialEnv();//第一次的时候需要创建本地语音包
		setContentView(R.layout.shop_order_detail);
		initHeadView("订单详情",true, false,0);
		orderDetailAdapter=new OrderDetailAdapter(this);
		initView();
		
	}

	/**
	 * 加载数据
	 */
	@Override
	public void onResume() {
		super.onResume();
		isNetNotification = getIntent().getBooleanExtra("isNetOrder",false);
		id = getIntent().getLongExtra("id",0)+"";
		initData();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		setIntent(intent);
	}

	private void initData() 
	{
		if (TextUtils.isEmpty(id)){
			return;
		}
		LogUtil.d("incomeID",id);
		ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		HttpUtils.getConnection(context, params, ConstantParamPhone.GET_ORDER_DETAIL+id, "GET", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络错误，稍后再试",Toast.LENGTH_SHORT).show();
				throwable.printStackTrace();
				ColorDialog.dissmissProcessDialog();
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
				ColorDialog.dissmissProcessDialog();
				
			}
		});
	}

	/**
	 * 执行自动打印
	 */
	public  void setTypeAutoPrint(){
		/**
		 * 判断是否是从网络获取的数据,如果是的话自动打印
		 */
		//判断订单是否已完成
		
		if (isNetNotification){
			//判断是否开启自动打印
			
			//判断是否开启语音播报
			if (sp.getBoolean("isVoiceOpen",false)){
				startTTS();//获取到数据后开启百度语音服务
			}
			if (sp.getBoolean("isAutoPrintOpen",false)){
				LogUtil.d("auto_print","true");
				//如果打印过不再打印
				if (sp.getBoolean("orderData.getId()",false)){
					Toast.makeText(context,"已经打印过了",Toast.LENGTH_SHORT).show();
				}else {
					OpenBlueTooth();//打开蓝牙
				}
			}else {
				Toast.makeText(context,"未开启自动打印",Toast.LENGTH_SHORT).show();
				LogUtil.d("auto_print","false");
			}
			isNetNotification = false;//打印订单后关闭
		}
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
				isFinishing = true;
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
				if (bean.id == 6 |bean.id== 9 |bean.id ==10 ){
					isTypeAutoPrint = true;
				}
			}
		}
		if ("大牌抢购".equals(orderSourceTextView.getText())){
			String address1 = orderData.getAddress();
			if (!TextUtils.isEmpty(address1)){
				rl_address.setVisibility(View.VISIBLE);
				address.setText(address1);
			}
		}
		/*if (content.equals("已支付") &&orderSourceTextView.getText().equals("大牌抢购")){
			//设置核销按钮可见
			//enterButton.setVisibility(View.VISIBLE);
			//noButton.setVisibility(View.VISIBLE);
		}*/
		//下单时间
		order_create_time.setText(Util.DataConVertMint(orderData.getTime()));
		//联系人
		orderContrectTextView.setText(orderData.getName());
		//电话
		orderPhoneTextView.setText(orderData.getMobile());
		//订单总数
		try {
			JSONObject ext = new JSONObject(orderData.getExt());
			order_Num.setText(ext.getString("num"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		//订单金额
		orderFeeTextView.setText(orderData.getTotal_fee());
		//折扣金额
		order_dis.setText(orderData.getDiscount_shop_fee());
		//总额
		totalPriceTextView.setText(orderData.getFee());
		setTypeAutoPrint();//执行自动打印
	}


	private void initView() 
	{
		order_Num = (TextView)findViewById(R.id.order_Num);//数量
		order_dis = (TextView)findViewById(R.id.order_dis);
		
		
		orderFeeTextView=(TextView) this.findViewById(R.id.orderFeeTextView);
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
	            //记录订单号
	            sp.edit().putBoolean("orderData.getId()",true).commit();//说明该订单已经打印
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
	            enterButton.setPressed(true);
            }
            catch (Exception e)
            {    
                Toast.makeText(ShopOrderDetailActivity.this, "打印失败！", Toast.LENGTH_LONG).show();  
	            LogUtil.d("erro",e.toString());
	            enterButton.setClickable(true);
	            enterButton.setPressed(true);
            }    
        }
		else 
		{
            Toast.makeText(ShopOrderDetailActivity.this, "设备未连接，请重新连接！", Toast.LENGTH_SHORT).show();   
        }
		enterButton.setClickable(true);
		enterButton.setPressed(true);
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
	    if (mSpeechSynthesizer!=null){
		    this.mSpeechSynthesizer.release();
	    }
	    try
    	{
		    if (outputStream!=null){
			    outputStream.close();
		    }
		    if (bluetoothSocket!=null)
			bluetoothSocket.close();
		} 
    	catch (Exception e) 
    	{
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
			Toast.makeText(context,"打开蓝牙后，请再次点击打印，",Toast.LENGTH_LONG).show();
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
						sendPrint();//打印
						return;
					}
				}
				//循环完毕没找到就结束当前方法，跳转到打印机列表界面
				Toast.makeText(context,"没有适配的打印机",Toast.LENGTH_LONG).show();
				Intent intent=new Intent(ShopOrderDetailActivity.this,ShopSearchBlueToothActivity.class);
				intent.putExtra("source","orderDetail");
				startActivityForResult(intent,2);
				return;
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

	/**
	 * 百度语音服务
	 */

	private SpeechSynthesizer mSpeechSynthesizer;
	private String mSampleDirPath;
	private static final String SAMPLE_DIR_NAME = "baiduTTS";
	private static final String SPEECH_FEMALE_MODEL_NAME = "bd_etts_speech_female.dat";
	private static final String TEXT_MODEL_NAME = "bd_etts_text.dat";


	/**
	 * 把语音文件拷到sd卡
	 */
	private void initialEnv() {
		if (mSampleDirPath == null) {
			String sdcardPath = Environment.getExternalStorageDirectory().toString();
			mSampleDirPath = sdcardPath + "/" + SAMPLE_DIR_NAME;
		}
		makeDir(mSampleDirPath);//创建目录
		/**
		 * 复制资料到sd卡
		 */
		copyFromAssetsToSdcard(false, SPEECH_FEMALE_MODEL_NAME, mSampleDirPath + "/" + SPEECH_FEMALE_MODEL_NAME);
		copyFromAssetsToSdcard(false, TEXT_MODEL_NAME, mSampleDirPath + "/" + TEXT_MODEL_NAME);
	}

	private void makeDir(String dirPath) {
		File file = new File(dirPath);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * 将sample工程需要的资源文件拷贝到SD卡中使用（授权文件为临时授权文件，请注册正式授权）
	 *
	 * @param isCover 是否覆盖已存在的目标文件
	 * @param source
	 * @param dest
	 */
	private void copyFromAssetsToSdcard(boolean isCover, String source, String dest) {
		File file = new File(dest);
		if (isCover || (!isCover && !file.exists())) {
			InputStream is = null;
			FileOutputStream fos = null;
			try {
				is = getResources().getAssets().open(source);
				String path = dest;
				fos = new FileOutputStream(path);
				byte[] buffer = new byte[1024];
				int size = 0;
				while ((size = is.read(buffer, 0, 1024)) >= 0) {
					fos.write(buffer, 0, size);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				if (fos != null) {
					try {
						fos.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				try {
					if (is != null) {
						is.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		LogUtil.d("copydata","success");
	}
	
	// 初始化语音合成客户端并启动
	private void startTTS() {
		// 获取语音合成对象实例
		mSpeechSynthesizer = SpeechSynthesizer.getInstance();
		// 设置context
		mSpeechSynthesizer.setContext(this);
		// 设置语音合成状态监听器
		mSpeechSynthesizer.setSpeechSynthesizerListener(this);
		
		// 文本模型文件路径 (离线引擎使用)
		this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_TEXT_MODEL_FILE, mSampleDirPath + "/"
				+ TEXT_MODEL_NAME);
		// 声学模型文件路径 (离线引擎使用)
		this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_TTS_SPEECH_MODEL_FILE, mSampleDirPath + "/"
				+ SPEECH_FEMALE_MODEL_NAME);
		this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "0");//女声
		// 设置Mix模式的合成策略
		this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);


		// 设置在线语音合成授权，需要填入从百度语音官网申请的api_key和secret_key
		mSpeechSynthesizer.setApiKey("HrLqNhkxXrH19aZ1KUA7A9iU", "ed3813a82ef96d20b61c05397a59a6b4");
		// 设置离线语音合成授权，需要填入从百度语音官网申请的app_id
		mSpeechSynthesizer.setAppId("8943945");
		// 设置Mix模式的合成策略
		this.mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_MIX_MODE, SpeechSynthesizer.MIX_MODE_DEFAULT);
		
		// 判断授权信息是否正确，如果正确则初始化语音合成器并开始语音合成，如果失败则做错误处理
		AuthInfo authInfo = mSpeechSynthesizer.auth(TtsMode.MIX);
		if (authInfo.isSuccess()) {
			mSpeechSynthesizer.initTts(TtsMode.MIX);//初始化语音合成器，混合模式
			mSpeechSynthesizer.speak("您有一笔新订单，订单金额是:"+orderData.getFee()+"元");
			LogUtil.d("yuyin","sucess");
		} else {
			// 授权失败
			Toast.makeText(context,"语言合成失败",Toast.LENGTH_SHORT).show();
			LogUtil.d("yuyin","fail");
		}
	}
	public void onError(String arg0, SpeechError arg1) {
		// 监听到出错，在此添加相关操作
	}
	public void onSpeechFinish(String arg0) {
		// 监听到播放结束，在此添加相关操作
		LogUtil.d("voice","end");
	}
	public void onSpeechProgressChanged(String arg0, int arg1) {
		// 监听到播放进度有变化，在此添加相关操作
	}
	public void onSpeechStart(String arg0) {
		// 监听到合成并播放开始，在此添加相关操作
		LogUtil.d("voice","play");
		
	}
	public void onSynthesizeDataArrived(String arg0, byte[] arg1, int arg2) {
		// 监听到有合成数据到达，在此添加相关操作
		LogUtil.d("voice","arrive");
	}
	public void onSynthesizeFinish(String arg0) {
		// 监听到合成结束，在此添加相关操作
		
	}
	public void onSynthesizeStart(String arg0) {
		// 监听到合成开始，在此添加相关操作
	}
	
	
}
