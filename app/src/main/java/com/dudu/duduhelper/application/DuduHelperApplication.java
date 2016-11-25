package com.dudu.duduhelper.application;
import com.dudu.duduhelper.Activity.OrderActivity.ShopOrderDetailActivity;
import com.dudu.duduhelper.R;
import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.widget.Toast;

import com.dudu.duduhelper.UmengService.MyPushIntentService;
import com.dudu.duduhelper.Utils.LogUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tencent.bugly.Bugly;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import java.util.LinkedList;
import java.util.List;

public class DuduHelperApplication extends Application 
{
	private List<Activity> mList = new LinkedList<Activity>();
	private static DuduHelperApplication instance;
	@Override
	public void onCreate() 
	{
		super.onCreate();
		/**
		 * 注册推送服务
		 */
		PushAgent mPushAgent = PushAgent.getInstance(this);
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
		//自定义处理消息,可以实现对推送消息的自定义行为
		mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
		
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
				.cacheInMemory(true).considerExifParams(true)
				.cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565)
				.displayer(new FadeInBitmapDisplayer(100))
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
			MobclickAgent.onKillProcess(DuduHelperApplication.this);
			System.exit(0);
		}
	}

	
}

