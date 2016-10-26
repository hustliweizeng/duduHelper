package com.dudu.helper3.wxapi;

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

import com.dudu.helper3.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.helper3.Utils.Util;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.widget.ColorDialog;
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
import org.json.JSONException;
import org.json.JSONObject;

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
	private IWXAPI api;
	private String sharedespriton="";
	private String qrcodeContent="";
	private String no="";
	private String time="";
	private Button saveImageButton;
	private String qrcode;
	private String APP_ID ;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_get_in_come_cash_code_image);

		// 通过WXAPIFactory工厂，获取IWXAPI的实例
		api = WXAPIFactory.createWXAPI(this, APP_ID, false);
		api.registerApp(APP_ID);
		api.handleIntent(getIntent(), this);
		
		
		
		//状态保存
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
		initHeadView("收款",true,false,0);
		initView();
		initData();
	}

	private void initView() {
		ImageCode = (ImageView) findViewById(R.id.ImageCode);
		ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		//保存按钮
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
	}

	private void initData()
	{
		//ColorDialog.showRoundProcessDialog(WXEntryActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		HttpUtils.getConnection(context,params, ConstantParamPhone.GET_SHOP_WXPIC, "GET",new TextHttpResponseHandler()
		{
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(WXEntryActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功,设置二维码地址
						qrcode = object.getString("qrcode");
						imageLoader.displayImage(qrcode,ImageCode);


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
				//ColorDialog.dissmissProcessDialog();
			}
		});
	}

	@Override
	//分享按钮
	public void RightButtonClick() 
	{
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
				popupWindow.dismiss();
			}
		});
        ImageButton sendWeixinFriendsBtn=(ImageButton) view.findViewById(R.id.sendWeixinFriendsBtn);
		//发送给朋友
        sendWeixinBtn.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{
				shareWeiXin(SendMessageToWX.Req.WXSceneSession);
			}
		});
		//发到朋友圈
        sendWeixinFriendsBtn.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{
				shareWeiXin(SendMessageToWX.Req.WXSceneTimeline);
			}
		});
        
	}
	private void shareWeiXin(int action) 
	{
		//异步任务
		new WeiXinTask().execute(action);
	}
	private String buildTransaction(final String type) 
	{
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}
	//微信发送消息给app，app接受并处理的回调函数
	@Override
	public void onReq(BaseReq arg0) 
	{
	}
	//app发送消息给微信，微信返回的消息回调函数,根据不同的返回码来判断操作是否成功
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
	}
	//异步任务
	private class WeiXinTask extends AsyncTask<Integer, Integer, String>
	{
		@Override
		protected String doInBackground(Integer... arg0) 
		{
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
        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果  
        @Override  
        protected void onPostExecute(String result) 
        {
        	if(result.equals("0"))
        	{
        		Toast.makeText(WXEntryActivity.this, "出错啦", Toast.LENGTH_SHORT).show();
        		ColorDialog.dissmissProcessDialog();
        	}else {
		        
	        }
        }  
		
	}
	//保存变量以及视图状态
	@Override
	protected void onSaveInstanceState(Bundle outState) 
	{
		super.onSaveInstanceState(outState);
		outState.putString("type", type);
		outState.putString("imageUrl", imageUrl);
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) 
	{
		super.onRestoreInstanceState(savedInstanceState);
		type=savedInstanceState.getString("type");
		imageUrl=savedInstanceState.getString("imageUrl");
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
			e.printStackTrace();
		}
		finally
		{
			Toast.makeText(WXEntryActivity.this, "保存成功啦", Toast.LENGTH_SHORT).show();
		}
	}
}
