package com.dudu.duduhelper.wxapi;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.widget.ColorDialog;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import org.apache.http.Header;

import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler
{
	private ImageView ImageCode;
	private String imageUrl="";
	private String type="";
	private TextView codeImagedesprition;
	private String content;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private PopupWindow popupWindow;
	private String APP_ID;
	private IWXAPI api;
	private String sharedespriton="";
//	private Button printbutton;
//	private BluetoothAdapter bluetoothAdapter;
//	private BluetoothDevice device;
//	private boolean isConnection = false;
//	private static BluetoothSocket bluetoothSocket = null;   
//	private static OutputStream outputStream = null;  
//	private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private String money="";
	private String qrcodeContent="";
	private String no="";
	private String time="";
	private Button saveImageButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_get_in_come_cash_code_image);
		APP_ID = "wx777383ea9b6c01c1";
		api = WXAPIFactory.createWXAPI(this,APP_ID, false);
		DuduHelperApplication.getInstance().addActivity(this);
		api.registerApp(APP_ID);  
		api.handleIntent(getIntent(), WXEntryActivity.this);
		imageUrl=getIntent().getStringExtra("qrcode");
		type=getIntent().getStringExtra("type");
		money=getIntent().getStringExtra("money");
		no=getIntent().getStringExtra("no");
		time=getIntent().getStringExtra("time");
		
		saveImageButton = (Button) this.findViewById(R.id.saveImageButton);
		saveImageButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//把布局保存成图片
				saveViewToImage();
			}
		});
		qrcodeContent=getIntent().getStringExtra("qrcodeContent");
		if(null!=savedInstanceState)
		{
			if(TextUtils.isEmpty(type))
			{
				type=savedInstanceState.getString("type");
			}
			if(TextUtils.isEmpty(imageUrl))
			{
				imageUrl=savedInstanceState.getString("imageUrl");
			}
		}
		if(TextUtils.isEmpty(imageUrl)&&!TextUtils.isEmpty(type))
		{
			if(type.equals(ConstantParamPhone.GET_SHOPE_CODE))
			{
				initHeadView("店铺二维码", true,  true, R.drawable.icon_share);
				content="         客户使用微信扫描该二维码即可进入店铺首页，截取该二维码图片打印成物料可以更好的做店铺宣传噢 ！";
				sharedespriton="快来本店看看啦，惊喜多多，优惠多多！";
			}
			if(type.equals(ConstantParamPhone.GET_CASHIER_CODE))
			{
				initHeadView("收款", true,  true, R.drawable.icon_share);
				content="         客户使用微信扫描该二维码即可进行支付，截取该二维码图片并打印成物料可以方便用户更快进行支付噢！";
				sharedespriton="本店已经开通微信支付，方便快捷，快来试试吧。";
			}
			initData();
		}
		else
		{
			initHeadView("收款", true,  false, 0);
			//initFilter();
			//initView();
		}
	}
	
	private void initData()
	{
		// TODO Auto-generated method stub
		ColorDialog.showRoundProcessDialog(WXEntryActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("free", "88");
		params.add("body","二维码收款");
		params.setContentEncoding("UTF-8");
		//保存cookie，自动保存到了shareprefercece  
		HttpUtils.getConnection(context,params,ConstantParamPhone.CREATE_PAY_PIC, "post",new TextHttpResponseHandler()
		{
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(WXEntryActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{

			}
			
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				ColorDialog.dissmissProcessDialog();
			}
		});
	}

	@Override
	public void RightButtonClick() 
	{
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = (LayoutInflater)WXEntryActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        View view = layoutInflater.inflate(R.layout.activity_share_popwindow, null);  
        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,  LayoutParams.MATCH_PARENT);  
        popupWindow.setFocusable(true);  
        popupWindow.setOutsideTouchable(true);  
        //这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景  
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000)); 
        //设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAsDropDown(WXEntryActivity.this.findViewById(R.id.head));
        ImageButton sendWeixinBtn=(ImageButton) view.findViewById(R.id.sendWeixinBtn);
        Button cancelBtn=(Button) view.findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				popupWindow.dismiss();
			}
		});
        ImageButton sendWeixinFriendsBtn=(ImageButton) view.findViewById(R.id.sendWeixinFriendsBtn);
        sendWeixinBtn.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				shareWeiXin(SendMessageToWX.Req.WXSceneSession);
			}
		});
        sendWeixinFriendsBtn.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				shareWeiXin(SendMessageToWX.Req.WXSceneTimeline);
			}
		});
        
	}
	private void shareWeiXin(int action) 
	{
		// TODO Auto-generated method stub
		ColorDialog.showRoundProcessDialog(WXEntryActivity.this,R.layout.loading_process_dialog_color);
		new WeiXinTask().execute(action);
	}
	private String buildTransaction(final String type) 
	{
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	@Override
	public void onReq(BaseReq arg0) 
	{
		// TODO Auto-generated method stub
		String a="";
	}
	@Override
	public void onResp(BaseResp resp) 
	{
		String result = "";
		switch (resp.errCode) 
		{
			case BaseResp.ErrCode.ERR_OK:
				result = "分享成功啦";
				break;
			case BaseResp.ErrCode.ERR_USER_CANCEL:
				result = "发送取消";
				break;
			case BaseResp.ErrCode.ERR_AUTH_DENIED:
				result = "发送被拒绝";
				break;
			default:
				result = "分享粗错啦";
				break;
		}
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		this.finish();  
		ColorDialog.dissmissProcessDialog();
	}
//	@Override
//	protected void onNewIntent(Intent intent) 
//	{
//		super.onNewIntent(intent);
//		setIntent(intent);
//        api.handleIntent(intent, this);
//	}
	private class WeiXinTask extends AsyncTask<Integer, Integer, String>
	{
		@Override
		protected String doInBackground(Integer... arg0) 
		{
			// TODO Auto-generated method stub
			try
			{
				WXWebpageObject webpage = new WXWebpageObject();
				webpage.webpageUrl = imageUrl;
				WXMediaMessage msg = new WXMediaMessage(webpage);
				msg.title = share.getString("shopname", "");
				msg.description = sharedespriton;
				Bitmap bmp = BitmapFactory.decodeStream(new URL(share.getString("shoplogo", "")).openStream());
				Bitmap thumb = Bitmap.createScaledBitmap(bmp, 80, 80, true);
				//Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
				msg.thumbData = Util.bmpToByteArray(thumb, true);
				SendMessageToWX.Req req = new SendMessageToWX.Req();
				req.transaction = buildTransaction("webpage");
				req.message = msg;
				req.scene = arg0[0];
				api.sendReq(req);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				return "0";
			}
			return "1";
			
		}
		//onProgressUpdate方法用于更新进度信息  
        @Override  
        protected void onProgressUpdate(Integer... progresses) 
        {  
        	
        }  
        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果  
        @Override  
        protected void onPostExecute(String result) 
        {
        	if(result.equals("0"))
        	{
        		Toast.makeText(WXEntryActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
        		ColorDialog.dissmissProcessDialog();
        	}
        }  
          
        //onCancelled方法用于在取消执行中的任务时更改UI  
        @Override  
        protected void onCancelled() 
        {
        	
        }  
		
	}
	//保存变量以及视图状态
	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		outState.putString("type", type);
		outState.putString("imageUrl", imageUrl);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
		type=savedInstanceState.getString("type");
		imageUrl=savedInstanceState.getString("imageUrl");
	}
	//获取连接
//	private void getConnect()
//	{
//		if (!this.isConnection) 
//		{    
//            try
//            {
//                bluetoothSocket = this.device.createRfcommSocketToServiceRecord(uuid);    
//                bluetoothSocket.connect();    
//                outputStream = bluetoothSocket.getOutputStream();    
//                this.isConnection = true;    
//                if (this.bluetoothAdapter.isDiscovering()) 
//                {    
//                	//取消搜索
//                }    
//            } 
//            catch (Exception e) 
//            {    
//                Toast.makeText(this, "连接失败,请检查打印机是否开启！", Toast.LENGTH_LONG).show(); 
//                return;
//            }    
//            Toast.makeText(this, this.device.getName() + "连接成功！", Toast.LENGTH_SHORT).show();   
//        } 
//        sendPrint();
//	}
//	
//	//发送打印
//	private void sendPrint() 
//	{
//		if (this.isConnection) 
//		{    
//            System.out.println("开始打印！！");    
//            try 
//            { 
//                EscCommand esc = new EscCommand();
//                //esc.addPrintAndFeedLines((byte) 3);
//                //esc.addSelectCodePage(EscCommand.CODEPAGE.UYGUR);
//                //设置汉子无效
//                //esc.addCancelKanjiMode();
//                esc.addPrintAndLineFeed();
//                esc.addSelectKanjiMode();
//                //esc.addSelectJustification(JUSTIFICATION.LEFT);//设置打印左对齐
//                esc.addSelectJustification(JUSTIFICATION.CENTER);//设置打印居中
//                esc.addSelectPrintModes(FONT.FONTA, ENABLE.OFF, ENABLE.ON, ENABLE.OFF, ENABLE.OFF);//设置为倍高倍宽
//                esc.addText("欢迎光临\n");   //  打印文字
//                esc.addText(share.getString("shopname", "本店铺")+"\n\n");
//                esc.addSelectPrintModes(FONT.FONTA, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF, ENABLE.OFF);//设置为倍高倍宽
//                //esc.addSetCharcterSize(WIDTH_ZOOM.MUL_3, HEIGHT_ZOOM.MUL_3);//设置字符尺寸
//                esc.addSelectJustification(JUSTIFICATION.LEFT);//设置打印左对齐
//                esc.addText("时间：        "+Util.DataConVert2(time)+"\n");   //  打印文字    
//                esc.addText("订单号：      "+no+"\n");   //  打印文字
//                esc.addText("--------------------------------\n");   //  打印文字
//                esc.addText("名称");   //  打印文字
//                //设置距左边绝对位置
////                esc.addSetAbsolutePrintPosition((short) 200);
////                esc.addText("数量");
//                //设置距左边绝对位置
//                esc.addSetAbsolutePrintPosition((short) 310);
//                esc.addText("金额\n");
////                for (OrderGoods good : orderDetailBean.getData().getGoods()) 
////                {
////                	if(good.getName().length()>8)
////                	{
////                		esc.addText(good.getName().substring(0, 7)+"\n");
////                		esc.addText(good.getName().substring(7, good.getName().length()));
////                	}
////                	else
////                	{
////                		esc.addText(good.getName());
////                	}
////                	esc.addSetAbsolutePrintPosition((short) 210);
////                	esc.addText(good.getNum());
////                	esc.addSetAbsolutePrintPosition((short) 300);
////                	esc.addText(good.getFee()+"\n");
////				}
//                esc.addText("商家收款");
//                esc.addSetAbsolutePrintPosition((short) 300);
//            	esc.addText(money+"\n");
//            	
////                esc.addText("\n\n商家优惠");   //  打印文字
////                esc.addSetAbsolutePrintPosition((short) 300);
////                esc.addText("5000.00\n");
////                
////                esc.addText("红包减免");
////                esc.addSetAbsolutePrintPosition((short) 300);
////                esc.addText("50.00\n");
//                
//                esc.addText("--------------------------------\n");   //  打印文字
//                
//                esc.addTurnEmphasizedModeOnOrOff(ENABLE.ON);//设置加粗
//                esc.addText("实收金额");
//                esc.addSetAbsolutePrintPosition((short) 300);
//                esc.addText(money+"\n\n");   //  打印文字
//                
//                esc.addSelectJustification(JUSTIFICATION.CENTER);//设置打印居中
//                esc.addTurnEmphasizedModeOnOrOff(ENABLE.OFF);
//                //打印图片
////	                Bitmap b1 = BitmapFactory.decodeResource(getResources(),R.drawable.qcoerd);
////	                esc.addRastBitImage(b1, 200, 0);   //打印图片
////	              
////	                //设置二维码内容
//                esc.addStoreQRCodeData(qrcodeContent);
//                //设置二维码尺寸
//                esc.addSelectSizeOfModuleForQRCode((byte)6);
//                esc.addPrintQRCode();
//                esc.addText("\n使用微信扫描二维码即可完成支付\n\n");
//                esc.addText("\n"+share.getString("getagentname", "")+"竭诚为您服务\n\n"); 
//                esc.addText("*******************************\n\n\n\n\n\n\n");
//                Vector<Byte> datas = esc.getCommand(); //发送数据
//                Byte[] Bytes = datas.toArray(new Byte[datas.size()]);
//                byte[] bytes = ArrayUtils.toPrimitive(Bytes);
//                outputStream.write(bytes, 0, bytes.length);    
//                outputStream.flush();  
//                Toast.makeText(WXEntryActivity.this, "打印成功！", Toast.LENGTH_LONG).show();
//            }
//            catch (Exception e)
//            {    
//                Toast.makeText(WXEntryActivity.this, "打印失败！", Toast.LENGTH_LONG).show();    
//            }    
//        }
//		else 
//		{
//            Toast.makeText(WXEntryActivity.this, "设备未连接，请重新连接！", Toast.LENGTH_SHORT).show();    
//        }    
//	}
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
				Toast.makeText(WXEntryActivity.this,"打开蓝牙取消", Toast.LENGTH_SHORT).show();
			}
		}
	}
	private void saveViewToImage() 
	{
		try 
		{
			View view = this.findViewById(R.id.getQueryCode);
			final Bitmap bmp = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.RGB_565);  
			view.draw(new Canvas(bmp));  
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");  
			String time = dateFormat.format(new Date());  
			File dir = new File(Environment.getExternalStorageDirectory(), "嘟嘟商家助手");
	        if (!dir.exists()) 
	        {  
	          dir.mkdirs();  
	        }  
			final String photoName = time + ".png";//换成自己的图片保存路径  
			final File file = new File(dir,photoName); 
			FileOutputStream fos;
				fos = new FileOutputStream(file);
	        bmp.compress(CompressFormat.JPEG, 100, fos);
	        fos.flush();
	        fos.close();
		    // 其次把文件插入到系统图库
		    MediaStore.Images.Media.insertImage(WXEntryActivity.this.getContentResolver(),file.getAbsolutePath(), photoName, null);
		    // 最后通知图库更新
		    WXEntryActivity.this.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,	Uri.fromFile(new File(file.getPath()))));
		} 
		catch (Exception e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			Toast.makeText(WXEntryActivity.this, "保存成功啦", Toast.LENGTH_SHORT).show();
		}
	}
//	private BroadcastReceiver receiver=new BroadcastReceiver() 
//	{
//		@Override
//		public void onReceive(Context context, Intent intent) 
//		{
//			// TODO Auto-generated method stub
//			String action = intent.getAction(); 
//			if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
//			{
//				if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON)
//				{
//					Toast.makeText(WXEntryActivity.this,"打开成功", Toast.LENGTH_SHORT).show();
//					ColorDialog.dissmissProcessDialog();
//				}
//				else
//				{
//					Toast.makeText(WXEntryActivity.this,"打开蓝牙失败", Toast.LENGTH_SHORT).show();
//					ColorDialog.dissmissProcessDialog();
//				}
//			}
//			if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action))
//			{
//				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);  
//                if (device.getBondState() == BluetoothDevice.BOND_BONDED) 
//                {    
//                    //绑定过的设备    
//                	Toast.makeText(WXEntryActivity.this,"配对成功", Toast.LENGTH_SHORT).show();
//                } 
//                if (device.getBondState() == BluetoothDevice.BOND_NONE)  
//                {    
//                    //未绑定的设备
//                	Toast.makeText(WXEntryActivity.this,"配对失败", Toast.LENGTH_SHORT).show();
//                }    
//			}
//		}
//	};
    @Override
    protected void onDestroy() 
    {
    	// TODO Auto-generated method stub
    	super.onDestroy();
//    	try
//    	{
//			outputStream.close();
//			bluetoothSocket.close();
//		} 
//    	catch (Exception e) 
//    	{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
        
    }

}
