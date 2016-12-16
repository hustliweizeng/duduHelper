package com.dudu.duduhelper.application;
import com.dudu.duduhelper.R;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;

import com.dudu.duduhelper.UmengService.MyPushIntentService;
import com.dudu.duduhelper.Utils.LogUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.tencent.bugly.Bugly;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;

import java.util.LinkedList;
import java.util.List;

public class DuduHelperApplication extends Application 
{
	private List<Activity> mList = new LinkedList<>();
	private static DuduHelperApplication instance;
	@Override
	public void onCreate() 
	{
		super.onCreate();
		SharedPreferences sp = getSharedPreferences("userconig", Context.MODE_PRIVATE);
		sp.edit().putStringSet("pushOrdsers",null).commit();//每次清空上次订单存储的信息

		/**
		 * 注册推送服务
		 */
		PushAgent mPushAgent = PushAgent.getInstance(this);
		mPushAgent.setDebugMode(true);
		//注册推送服务，每次调用register方法都会回调该接口
		mPushAgent.register(new IUmengRegisterCallback() {
			@Override
			public void onSuccess(String deviceToken) {
				//注册成功会返回device token
				//保存友盟的token信息
				SharedPreferences sp = getSharedPreferences("userconig",MODE_PRIVATE);
				sp.edit().putString("umeng_token",deviceToken).commit();
				LogUtil.d("token",deviceToken);
			}
			@Override
			public void onFailure(String s, String s1) {
			}
		});
		mPushAgent.setDisplayNotificationNumber(3);//设置通知的条目数
		//自定义处理消息,可以实现对推送消息的自定义行为
		mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
		//客户端打开声音和震动
		mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER);
		mPushAgent.setNotificaitonOnForeground(false);//应用在前台也显示通知
		
		if(sp.getBoolean("isRemindOpen",false)){
			mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);//开启通知声音
		}
		if(sp.getBoolean("isRingOpen",false)){
			mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);//开启震动
		}
		
		/**
		 * buggly自动更新功能
		 */
		Bugly.init(getApplicationContext(), "510ff77a7a", false);
		initImageLoader(getApplicationContext());
		
	}
	
	public synchronized static DuduHelperApplication getInstance() 
	{
		if (null == instance) 
		{
			instance = new DuduHelperApplication();
		}
		return instance;
	}

	/**初始化UIL图片加载框架
	 *
	 * @param context
	 */
	public static void initImageLoader(Context context) 
	{
		//设置图片缓存
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true)
				.cacheOnDisc(true)
				//.showImageOnLoading(R.drawable.icon_head)//加载时候显示的图片
				.showImageForEmptyUri(R.drawable.ic_defalut)
				.showImageOnFail(R.drawable.ic_defalut)
				.considerExifParams(true)
				.bitmapConfig(Bitmap.Config.RGB_565)//图片显示模式
				.displayer(new SimpleBitmapDisplayer())//渐进显示动画,这样会造成图片闪烁！！！！
				//图片缩放设置
				.build();
		//默认配置
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs()
				.defaultDisplayImageOptions(options)
				.build();

		ImageLoader.getInstance().init(config);
	}
	public void addActivity(Activity activity)
	{
		if(!mList.contains(activity)){
			mList.add(activity);
		}
	}

	public void exit() 
	{
		try 
		{
			for (Activity activity : mList) 
			{
				if (activity != null)
					activity.finish();
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally 
		{
			LogUtil.d("logout","out");
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
	}

	
}

