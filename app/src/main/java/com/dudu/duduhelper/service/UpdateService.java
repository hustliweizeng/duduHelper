package com.dudu.duduhelper.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.dudu.duduhelper.R;



import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

public class UpdateService extends Service 
{
	private static final int NOTIFY_ID = 0;
	private int progress;
	private String apkUrl="";
	private NotificationManager mNotificationManager;
	private Context mContext = this;
	Notification mNotification;
	
	@Override
	public void onCreate() 
	{
		// TODO Auto-generated method stub
		super.onCreate();
		mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE); 
		setUpNotification();
	}
	
	private void setUpNotification() 
	{
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

//		mBuilder.setContentTitle("测试标题")//设置通知栏标题
//		    .setContentText("测试内容") //设置通知栏显示内容
//		    //.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图
//		     //  .setNumber(number) //设置通知集合的数量
//		    .setTicker("测试通知来啦") //通知首次出现在通知栏，带上升动画效果的
//		    .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
//		    .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
//		     //  .setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消  
//		    .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
//		    .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
//		    //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
//		    .setSmallIcon(R.drawable.ic_launcher).setProgress(count, progress,boolean indeterminate);//设置通知小ICON
		mNotification = new Notification(); 
		mNotification.icon=R.drawable.icon_head;
		mNotification.tickerText="嘟嘟";
		mNotification.when=System.currentTimeMillis();
		mNotification.flags = Notification.FLAG_ONGOING_EVENT;
		RemoteViews contentView = new RemoteViews(getPackageName(), R.layout.download_notification_layout);
		contentView.setTextViewText(R.id.name, "嘟嘟下载中...");
		mNotification.contentView = contentView;
		mNotificationManager.notify(NOTIFY_ID, mNotification);
	}
	
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		  apkUrl=intent.getStringExtra("apkUrl");
		  startDownload();
		  return START_NOT_STICKY;
	}
	
	private void startDownload() 
	{
		// TODO Auto-generated method stub
		downloadApk();
	}
	
	/**
	 * 下载apk
	 */
	private Thread downLoadThread;
	private void downloadApk() 
	{
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}
	
	private int lastRate = 0;
	private Runnable mdownApkRunnable = new Runnable() 
	{
		@Override
		public void run() 
		{
			try 
			{
				URL url = new URL(apkUrl);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();
				File file = new File(Environment.getExternalStorageDirectory()+"/");
				if (!file.exists()) 
				{
					file.mkdirs();
				}
				File ApkFile = new File(Environment.getExternalStorageDirectory()+"/DuduHelper.apk");
				FileOutputStream fos = new FileOutputStream(ApkFile);
				int count = 0;
				byte buf[] = new byte[1024];
				do 
				{
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					Message msg = mHandler.obtainMessage();
					msg.what = 1;
					msg.arg1 = progress;
					if (progress >= lastRate + 1) 
					{
						mHandler.sendMessage(msg);
						lastRate = progress;
					}
					if (numread <= 0) 
					{
						mHandler.sendEmptyMessage(0);
						break;
					}
					fos.write(buf, 0, numread);
				}
				while (true);
				fos.close();
				is.close();
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				mNotification.flags = Notification.FLAG_AUTO_CANCEL;
				mNotificationManager.cancel(NOTIFY_ID);
				mNotification.contentView = null;
			}

		}
	};
	
	private Handler mHandler = new Handler() 
	{
		@Override
		public void handleMessage(Message msg) 
		{
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			try
			{
				switch (msg.what) 
				{
				   case 1:
					int rate = msg.arg1;
					//app.setDownload(true);
					if (rate < 100) 
					{
						RemoteViews contentview = mNotification.contentView;
						contentview.setTextViewText(R.id.tv_progress, rate + "%");
						contentview.setProgressBar(R.id.progressbar, 100, rate, false);
					}
					else 
					{
						mNotification.flags = Notification.FLAG_AUTO_CANCEL;
						mNotificationManager.cancel(NOTIFY_ID);
						mNotification.contentView = null;
						installApk();
						stopSelf();//停掉自身服务
					}
					mNotificationManager.notify(NOTIFY_ID, mNotification);
					break;
				}
			}
			catch (Exception e) 
			{
				// TODO: handle exception
				e.printStackTrace();
			}
		}
	};
	/**
	 *安装apk
	 * 
	 * @param url
	 */
	private void installApk() 
	{
		File apkfile = new File(Environment.getExternalStorageDirectory()+"/DuduHelper.apk");
		if (!apkfile.exists()) 
		{
			return;
		}
		Intent i = new Intent(Intent.ACTION_VIEW);
		i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
		mContext.startActivity(i);
	}

	

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

}
