package com.dudu.duduhelper.UmengService;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.text.TextUtils;
import android.widget.Toast;

import com.dudu.duduhelper.Activity.MainActivity;
import com.dudu.duduhelper.Activity.OrderActivity.ShopOrderDetailActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageService;
import com.umeng.message.common.UmLog;
import com.umeng.message.entity.UMessage;

import org.android.agoo.common.AgooConstants;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * @author
 * @version 1.0
 * @date 2016/11/25
 */

public class MyPushIntentService extends UmengMessageService {
	private static final String TAG = MyPushIntentService.class.getName();
	private String orderId;
	Set<String> ids = new HashSet<>();
	private SharedPreferences sp;


	@Override
	public void onMessage(final  Context context, Intent intent) {
		try {
			sp = getSharedPreferences("userconig", Context.MODE_PRIVATE);
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
			SharedPreferences sp = context.getSharedPreferences("userconig", Context.MODE_PRIVATE);
			sp.edit().putString("newOrder",orderId).commit();//每次清空上次订单存储的信息
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
			boolean isRemindOpen = sp.getBoolean("isRemindOpen", false);//铃声
			boolean isRingOpen = sp.getBoolean("isRingOpen", false);//震动
			/**
			 * 通知提醒
			 */
			int id = new Random(System.nanoTime()).nextInt();
			NotificationManager mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
			mNotificationManager.cancelAll();//通知之前取消其他的
			NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
			mBuilder
					.setContentTitle(msg.title)//设置通知栏标题  
					.setContentText(msg.text) 
					.setAutoCancel(true)//点击后消失
					//.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL)) //设置通知栏点击意图 ,可以打开详情页
					.setContentIntent(getDefalutIntent(Notification.FLAG_AUTO_CANCEL))
					.setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间  
					.setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级  
					.setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)  
					.setSmallIcon(R.drawable.ic_defalut);//设置通知小ICON  
			if(isRemindOpen){
				if(isRingOpen){//震动开启
					mBuilder.setDefaults(Notification.DEFAULT_ALL);//设置默认声音带震动
				}else {//震动未开
					mBuilder.setDefaults(Notification.DEFAULT_SOUND);//设置只有震动
				}
			}else {
				if (isRemindOpen){
				mBuilder.setDefaults(Notification.DEFAULT_VIBRATE);//开启震动
				}
			}
			//设置后续动作
			mNotificationManager.notify(id,mBuilder.build());//发送通知

			/**
			 * 当登陆成功以后自动跳转到指定页面（订单页面），登陆不成功的时候不跳转
			 * 这种方式打开订单详情走oncreate方法
			 */
			
		if (MainActivity.isLogin){
			//Toast.makeText(context,"已登陆",Toast.LENGTH_SHORT).show();
			Intent intent1 = new Intent(context,ShopOrderDetailActivity.class);
			intent1.putExtra("id",Long.parseLong(orderId));
			intent1.putExtra("isNetOrder",true);
			intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			LogUtil.d("swith",orderId);
			startActivity(intent1);
		}else {
			Toast.makeText(context,"未登录获取不到订单",Toast.LENGTH_SHORT).show();
			if (!ids.contains(orderId)){
				ids.add(orderId);
				sp.edit().putStringSet("pushOrdsers",ids).commit();
			}
			
		}

		} catch (Exception e) {
			UmLog.e(TAG, e.getMessage());
		}
	}

	/**
	 * 这种方式打开订单详情不走oncreate方法走onnewIntent方法
	 * @param flags
	 * @return
	 */
	public PendingIntent getDefalutIntent(int flags){
		
		Intent intent2 = new Intent(getBaseContext(), ShopOrderDetailActivity.class);
		intent2.putExtra("id",Long.parseLong(orderId));
		LogUtil.d("send_message",orderId);
		intent2.putExtra("isNetOrder",true);
		intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		PendingIntent pendingIntent= PendingIntent.getActivity(getApplicationContext(), 1,intent2, flags);
		return pendingIntent;
	}

}