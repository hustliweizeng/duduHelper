package com.dudu.duduhelper.UmengService;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.ThemedSpinnerAdapter;

import com.dudu.duduhelper.Activity.OrderActivity.ShopOrderDetailActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageService;
import com.umeng.message.common.UmLog;
import com.umeng.message.entity.UMessage;

import org.android.agoo.common.AgooConstants;
import org.json.JSONObject;

/**
 * @author
 * @version 1.0
 * @date 2016/11/25
 */

public class MyPushIntentService extends UmengMessageService {
	private static final String TAG = MyPushIntentService.class.getName();
	private String orderId;

	@Override
	public void onMessage(Context context, Intent intent) {
		try {
			LogUtil.d("diy service","ok");
			//可以通过MESSAGE_BODY取得消息体
			String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
			UMessage msg = new UMessage(new JSONObject(message));
			UmLog.d(TAG, "message=" + message);      //消息体
			UmLog.d(TAG, "custom=" + msg.custom);    //自定义消息的内容
			UmLog.d(TAG, "title=" + msg.title);      //通知标题
			UmLog.d(TAG, "text=" + msg.text);        //通知内容
			String extra = new JSONObject(message).getString("extra");
			//获取订单id
			orderId = new JSONObject(extra).getString("id");

			/**
			 * 设置消息是否已读
			 */
			boolean isClickOrDismissed = true;
			if (isClickOrDismissed) {
				//完全自定义消息的点击统计
				UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
			} else {
				//完全自定义消息的忽略统计
				UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
			}
			SharedPreferences sp = getSharedPreferences("userconig",Context.MODE_PRIVATE);
			boolean isVoiceOpen = sp.getBoolean("isVoiceOpen", false);
			/**
			 * 通知提醒
			 */
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
			mBuilder.setContentTitle(msg.title)//设置通知栏标题  
					.setContentText(msg.text) 
			.setContentIntent(getDefalutIntent(Notification.FLAG_ONLY_ALERT_ONCE)) //设置通知栏点击意图  
			.setTicker("测试通知来啦") //通知首次出现在通知栏，带上升动画效果的  
			.setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间  
			.setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级  
			.setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)  
			.setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合  
			.setSmallIcon(R.drawable.ic_defalut);//设置通知小ICON  
			
			if(isVoiceOpen){
				mBuilder.setDefaults(Notification.DEFAULT_SOUND);//设置默认声音
			}
			mNotificationManager.notify(22,mBuilder.build());//发送通知
			/**
			 * 自动跳转到指定页面
			 */
			Intent intent1 = new Intent(context,ShopOrderDetailActivity.class);
			intent1.putExtra("id",Long.parseLong(orderId));
			intent1.putExtra("isNetOrder",true);
			intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent1);
			

		} catch (Exception e) {
			UmLog.e(TAG, e.getMessage());
		}
	}

	public PendingIntent getDefalutIntent(int flags){
		Intent intent = new Intent(getBaseContext(), ShopOrderDetailActivity.class);
		intent.putExtra("id",Long.parseLong(orderId));
		intent.putExtra("isNetOrder",true);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		PendingIntent pendingIntent= PendingIntent.getActivity(this, 1,intent, flags);
		return pendingIntent;
	}
}