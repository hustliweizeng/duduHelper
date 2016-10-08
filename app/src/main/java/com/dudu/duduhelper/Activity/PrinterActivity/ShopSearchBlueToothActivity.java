package com.dudu.duduhelper.Activity.PrinterActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.adapter.DeviceAdapter;
import com.dudu.duduhelper.widget.ColorDialog;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;

public class ShopSearchBlueToothActivity extends BaseActivity 
{
	private Button scanbutton;
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothDevice device;
	private DeviceAdapter deviceAdapter;
	private ListView devicesList;
	private String devicesAddress="";
	private String devicesName="";
	private TextView bindDevicesTextView;
	private TimeCount time;
	private View lineView;
	
	private ImageView radarbttom;
	private ImageView radartop;
	private ImageView mAnnularImg;
	private AnimationSet grayAnimal;
	private ImageView radarImageing;
	//动画计时线程
	private Handler handler=new Handler();
	//第一波线程
	private Runnable runnable;
	//第二波线程
	private Runnable runnable2;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_search_blue_tooth);
		initHeadView("打印机", true, false, 0);
		time = new TimeCount(30000, 1000);//构造CountDownTimer对象
		deviceAdapter=new DeviceAdapter(this);
		initFilter();
		initView();
		
		mAnnularImg = (ImageView) findViewById(R.id.radar_img);
		radartop = (ImageView) findViewById(R.id.radar_top_img);
		radarbttom = (ImageView) findViewById(R.id.radar_bttom_img);
		radarImageing = (ImageView) findViewById(R.id.radar_imageing);
		
	}
	/**
	 */
	private void startAnima() 
	{
		//开始动画
		Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.sss);
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		radarbttom.setVisibility(View.VISIBLE);
		radarImageing.setVisibility(View.VISIBLE);
		radarImageing.startAnimation(operatingAnim);
	}
	
	private void startcircularAnima() 
	{
		grayAnimal = playHeartbeatAnimation();
		radarbttom.startAnimation(grayAnimal);
		runnable = new Runnable()
		{
			@Override
			public void run() 
			{
				startwhiteAnimal();
			}
		};
		runnable2 = new Runnable() 
		{
			@Override
			public void run() 
			{
				startannularAnimat();
			}
		};
		handler.postDelayed(runnable, 400);
		handler.postDelayed(runnable2, 600);

	}
	
	private AnimationSet playHeartbeatAnimation() 
	{
		AnimationSet animationSet = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(0.3f, 1.0f, 0.3f, 1.0f,ScaleAnimation.RELATIVE_TO_SELF, 0.5f,ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		// animationSet.addAnimation(new AlphaAnimation(1.0f, 0.9f));
		sa.setDuration(900);
		sa.setFillAfter(true);
		sa.setRepeatCount(0);
		sa.setInterpolator(new LinearInterpolator());
		animationSet.addAnimation(sa);
		return animationSet;
	}
	
	private void startwhiteAnimal() 
	{
		AnimationSet whiteAnimal = playHeartbeatAnimation();
		whiteAnimal.setRepeatCount(0);
		whiteAnimal.setDuration(700);
		radartop.setVisibility(View.VISIBLE);
		radartop.startAnimation(whiteAnimal);
		whiteAnimal.setAnimationListener(new AnimationListener() 
		{
			@Override
			public void onAnimationStart(Animation animation) 
			{
				
			}

			@Override
			public void onAnimationRepeat(Animation animation) 
			{
			}

			@Override
			public void onAnimationEnd(Animation animation) 
			{
				mAnnularImg.setVisibility(View.GONE);
				radartop.setVisibility(View.GONE);
				startcircularAnima();
			}
		});

	}
	
	private void startannularAnimat() 
	{
		mAnnularImg.setVisibility(View.VISIBLE);
		AnimationSet annularAnimat = getAnimAnnular();
		annularAnimat.setAnimationListener(new AnimationListener() 
		{

			@Override
			public void onAnimationStart(Animation animation) 
			{

			}

			@Override
			public void onAnimationRepeat(Animation animation) 
			{
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation animation) 
			{
				mAnnularImg.setVisibility(View.GONE);
			}
		});
		mAnnularImg.startAnimation(annularAnimat);
	}
	
	private AnimationSet getAnimAnnular() 
	{
		AnimationSet animationSet = new AnimationSet(true);
		ScaleAnimation sa = new ScaleAnimation(1.0f, 1.5f, 1.0f, 1.5f,ScaleAnimation.RELATIVE_TO_SELF, 0.5f,ScaleAnimation.RELATIVE_TO_SELF, 0.5f);
		animationSet.addAnimation(new AlphaAnimation(1.0f, 0.1f));
		animationSet.setDuration(400);
		sa.setDuration(500);
		sa.setFillAfter(true);
		sa.setRepeatCount(0);
		sa.setInterpolator(new LinearInterpolator());
		animationSet.addAnimation(sa);
		return animationSet;
	}

	private void initView() 
	{
		bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
		lineView=this.findViewById(R.id.lineView);
		scanbutton=(Button) this.findViewById(R.id.scanbutton);
		bindDevicesTextView=(TextView) this.findViewById(R.id.bindDevicesTextView);
		SharedPreferences sharePrint= getSharedPreferences("printinfo", MODE_PRIVATE);
		//查找已配对的设备
		Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
		
		if (bondedDevices !=null && bondedDevices.size()>0){
			bindDevicesTextView.setText("已配对设备");
			bindDevicesTextView.setVisibility(View.VISIBLE);
			lineView.setVisibility(View.VISIBLE);
			for (BluetoothDevice device : bondedDevices){
				//显示已配对列表
				deviceAdapter.addItem(device);
			}
			LogUtil.d("bind",sharePrint.getString("blueName",""));
			
		}
		/*if(!TextUtils.isEmpty(sharePrint.getString("blueName", "")))
		{
			bindDevicesTextView.setVisibility(View.VISIBLE);
			lineView.setVisibility(View.VISIBLE);
			bindDevicesTextView.setText("已配对设备："+sharePrint.getString("blueName", ""));
			LogUtil.d("bind",sharePrint.getString("blueName",""));
		}else {
			LogUtil.d("bind","没有已配对的");
		}*/
		//搜索按钮
		scanbutton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				//先清除之前搜索的结果
				deviceAdapter.clear();
				//隐藏已配对的设备
				bindDevicesTextView.setText("搜素到的设备");
				//开启搜索模式
				bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
				if(bluetoothAdapter==null)
				{
					Toast.makeText(ShopSearchBlueToothActivity.this,"该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
					return;
				}
				
				else if(!bluetoothAdapter.isEnabled())//isEnabled enable是激活
				{
					Toast.makeText(ShopSearchBlueToothActivity.this,"蓝牙已打开", Toast.LENGTH_SHORT).show();
					//开启
					ColorDialog.showRoundProcessDialog(ShopSearchBlueToothActivity.this,R.layout.loading_process_dialog_color);
					Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);     
					// 设置蓝牙可见性，最多300秒      
					intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3000);     
					startActivityForResult(intent, 1); 
				}
				// 寻找蓝牙设备，android会将查找到的设备以广播形式发出去
				else
				{
					Toast.makeText(ShopSearchBlueToothActivity.this,"开始搜索", Toast.LENGTH_SHORT).show();
					deviceAdapter.clear();
					bluetoothAdapter.startDiscovery(); 
					time.start();
				}
				
			}
		});
		//设备列表
		devicesList=(ListView) this.findViewById(R.id.devicesList);
		devicesList.setAdapter(deviceAdapter);
		//配对设备
		devicesList.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				//取消搜索
				if (bluetoothAdapter.isDiscovering())
				bluetoothAdapter.cancelDiscovery();
				//关闭动画
				closeSearch();
				device=bluetoothAdapter.getRemoteDevice(deviceAdapter.getItem(position).getAddress());
				//如果当前设备没有绑定
				if (device.getBondState() == BluetoothDevice.BOND_NONE){
					LogUtil.d("bond","unbonde");

					try
					{
						//利用反射调用方法
						Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
						createBondMethod.invoke(device);
						//配对成功后设置数据
						devicesAddress=deviceAdapter.getItem(position).getAddress();
						devicesName=deviceAdapter.getItem(position).getName();
						Toast.makeText(ShopSearchBlueToothActivity.this, "开始匹配!!", Toast.LENGTH_SHORT).show();

					}
					catch (Exception e)
					{
						e.printStackTrace();
						Toast.makeText(ShopSearchBlueToothActivity.this, "配对失败!!", Toast.LENGTH_SHORT).show();
					}

				}else if(device.getBondState() == BluetoothDevice.BOND_BONDED){
					//已经绑定了，就可以开始连接传输数据了
					LogUtil.d("bond","bonde");
					try {
						BluetoothSocket btsocket = device.createRfcommSocketToServiceRecord(uuid);
						Log.d("BlueToothTestActivity", "开始连接...");
						btsocket.connect();

					}catch (Exception e){
						e.printStackTrace();

					}
				}
				
			}
		});
	}
	private static final UUID uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
	private void initFilter() 
	{
		// 设置广播信息过滤      
		IntentFilter intentFilter = new IntentFilter();     
		intentFilter.addAction(BluetoothDevice.ACTION_FOUND);//设备已经发现
		intentFilter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//设备开始绑定
		intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);//开始搜索 
	    intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);//结束搜索    
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//开关状态
		// 注册广播接收器，接收并处理搜索结果      
		registerReceiver(receiver, intentFilter);
	}
	private BroadcastReceiver receiver=new BroadcastReceiver() 
	{
		@Override
		//接收到广播信息
		public void onReceive(Context context, Intent intent) 
		{
			// TODO Auto-generated method stub
			String action = intent.getAction(); 
			//蓝牙打开
			if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
			{
				if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON)
				{
					Toast.makeText(ShopSearchBlueToothActivity.this,"打开成功", Toast.LENGTH_SHORT).show();
					deviceAdapter.clear();
					bluetoothAdapter.startDiscovery(); 
					//开始计时
					time.start();
				}
				else
				{
					Toast.makeText(ShopSearchBlueToothActivity.this,"打开蓝牙失败", Toast.LENGTH_SHORT).show();
				}
				ColorDialog.dissmissProcessDialog();
			}
			//搜索
			if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
			{
				Toast.makeText(ShopSearchBlueToothActivity.this,"开始搜索", Toast.LENGTH_SHORT).show();
				
				scanbutton.setVisibility(View.GONE);
				//开始动画
				startAnima();
				startcircularAnima();
			}
			//发现设备
			if (BluetoothDevice.ACTION_FOUND.equals(action)) 
			{

				Toast.makeText(ShopSearchBlueToothActivity.this,"发现设备", Toast.LENGTH_SHORT).show();
				//获取查找到的蓝牙设备    
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE); 
				//配对过
				if (device.getBondState() == 12){
					deviceAdapter.addItem(device);
					LogUtil.d("contain",device.getAddress());


				}else if(device.getBondState() == 10){
					//没有配对过
					deviceAdapter.addItem(device);
					LogUtil.d("add",device.getAddress());

				}
               /* if (!device.getAddress().equals(share.getString("blueAddress", ""))) 
                {    
	                //发现的设备插入列表
                	deviceAdapter.addItem(device);
	                LogUtil.d("add",device.getAddress());
                }*/
            }
			
			if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action))
			{
				//获取设备
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);  
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) 
                {
	                LogUtil.d("bind",device.getName()+"已经绑定");

	                //绑定过的设备    
	                devicesAddress = device.getAddress();
	                devicesName = device.getName();
	                Toast.makeText(ShopSearchBlueToothActivity.this,"配对成功", Toast.LENGTH_SHORT).show();
	                //查找绑定过的设备
                	SharedPreferences sharePrint= getSharedPreferences("printinfo", MODE_PRIVATE);
                	SharedPreferences.Editor edit = sharePrint.edit(); 
                	edit.putString("blueAddress",devicesAddress);
                	edit.putString("blueName",devicesName);
                	edit.commit();
                	finish();
                } 
                if (device.getBondState() == BluetoothDevice.BOND_NONE)  
                {    
                    //未绑定的设备
                	Toast.makeText(ShopSearchBlueToothActivity.this,"配对失败", Toast.LENGTH_SHORT).show();
                }    
			}
			if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
				Toast.makeText(context,"搜索结束",Toast.LENGTH_SHORT).show();
				//关闭动画
				closeSearch();
			}
		}
	};
	class TimeCount extends CountDownTimer 
	{
		public TimeCount(long millisInFuture, long countDownInterval) 
		{
		    super(millisInFuture, countDownInterval);//参数依次为总时长,和计时的时间间隔
		}
		@Override
		public void onFinish() 
		{
			LogUtil.d("finish","finish");
			//计时完毕时触发
			scanbutton.setText("立即扫描");
			bluetoothAdapter.cancelDiscovery();
			scanbutton.setVisibility(View.VISIBLE);
			closeSearch();
		}
		@Override
		public void onTick(long millisUntilFinished)
		{ 
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) 
		{
			if (resultCode == RESULT_OK) 
			{
				
			} 
			else if (resultCode == RESULT_CANCELED) 
			{
				ColorDialog.dissmissProcessDialog();
				Toast.makeText(ShopSearchBlueToothActivity.this,"打开蓝牙取消", Toast.LENGTH_SHORT).show();
			}
		}
	}
	//关闭动画
	public void closeSearch()
	{
		handler.removeCallbacks(runnable);
		handler.removeCallbacks(runnable2);
		
		mAnnularImg.clearAnimation();
		
		radartop.clearAnimation();
		
		radarbttom.clearAnimation();
		
		radarImageing.clearAnimation();
		
		mAnnularImg.setVisibility(View.GONE);
		radartop.setVisibility(View.GONE);
		radarbttom.setVisibility(View.GONE);
		radarImageing.setVisibility(View.GONE);
	}
}
