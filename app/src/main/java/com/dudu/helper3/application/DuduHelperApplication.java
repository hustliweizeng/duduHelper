package com.dudu.helper3.application;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.dudu.helper3.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;

import java.util.LinkedList;
import java.util.List;

public class DuduHelperApplication extends Application 
{
	private List<Activity> mList = new LinkedList<Activity>();
	private static DuduHelperApplication instance;
	private PushAgent mPushAgent;
	private GetPushNot getPushNot;
	
	@Override
	public void onCreate() 
	{
		// TODO Auto-generated method stub
		super.onCreate();
		CrashReport.initCrashReport(getApplicationContext(), "510ff77a7a", false);
		initImageLoader(getApplicationContext());
		mPushAgent = PushAgent.getInstance(this);
		UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler()
		{
			@Override
			public void launchApp(Context arg0, com.umeng.message.entity.UMessage arg1) 
			{
				super.launchApp(arg0, arg1);
				//为什么要加getInstance,如果不加默认获取的不是已经实例化的application,所以获取的getPushNot为空，所以也就回调不到当前activity
				try 
				{
					for (Activity activity : getInstance().mList) 
					{
						if (activity != null&&!activity.getClass().getName().equals("com.dudu.duduhelper.MainActivity"))
							activity.finish();
					}
				} 
				catch (Exception e)
				{
					e.printStackTrace();
				}
				getInstance().getPushNot.getPushCallback();
				
			};
			
		};
		mPushAgent.setNotificationClickHandler(notificationClickHandler);
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
	// add Activity
	public void addActivity(Activity activity)
	{
		mList.add(activity);
		//LogUtil.d("activityNum:",""+mList.size());
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
	
	public interface GetPushNot
	{
		public void getPushCallback();
	}
	
	public void setPushNot(GetPushNot getPushNot)
	{
		this.getPushNot=getPushNot;
	}
}

