package com.dudu.duduhelper.application;

import java.util.LinkedList;
import java.util.List;

import org.apache.http.Header;

import com.dudu.duduhelper.MainActivity;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.widget.Toast;

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
	public static void initImageLoader(Context context) 
	{
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs().build();
		ImageLoader.getInstance().init(config);
	}
	// add Activity
	public void addActivity(Activity activity)
	{
		mList.add(activity);
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

