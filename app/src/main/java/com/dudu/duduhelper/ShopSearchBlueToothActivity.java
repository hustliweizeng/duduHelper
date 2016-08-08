package com.dudu.duduhelper;

import java.lang.reflect.Method;
import java.util.UUID;

import com.dudu.duduhelper.ShopRebindPhoneSteponeActivity.TimeCount;
import com.dudu.duduhelper.adapter.DeviceAdapter;
import com.dudu.duduhelper.widget.ColorDialog;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Address;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
//		new Handler().postDelayed(new Runnable()
//		{
//			@Override
//			public void run() 
//			{
//				startwhiteAnimal();
//			}
//		}, 400);
//		new Handler().postDelayed(new Runnable() 
//		{
//			@Override
//			public void run() 
//			{
//				startannularAnimat();
//			}
//		}, 600);
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
		// TODO Auto-generated method stub
		bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
		lineView=this.findViewById(R.id.lineView);
		scanbutton=(Button) this.findViewById(R.id.scanbutton);
		bindDevicesTextView=(TextView) this.findViewById(R.id.bindDevicesTextView);
		SharedPreferences sharePrint= getSharedPreferences("printinfo", MODE_PRIVATE);
		if(!TextUtils.isEmpty(sharePrint.getString("blueName", "")))
		{
			bindDevicesTextView.setVisibility(View.VISIBLE);
			lineView.setVisibility(View.VISIBLE);
			bindDevicesTextView.setText("已配对设备："+sharePrint.getString("blueName", ""));
		}
		scanbutton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//开启搜索模式
				bluetoothAdapter=BluetoothAdapter.getDefaultAdapter();
				if(bluetoothAdapter==null)
				{
					Toast.makeText(ShopSearchBlueToothActivity.this,"该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
					return;
				}
				
				else if(!bluetoothAdapter.isEnabled())//isEnabled enable是激活
				{
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
					deviceAdapter.clear();
					bluetoothAdapter.startDiscovery(); 
					time.start();
				}
				
			}
		});
		devicesList=(ListView) this.findViewById(R.id.devicesList);
		deviceAdapter=new DeviceAdapter(this);
		devicesList.setAdapter(deviceAdapter);
		devicesList.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				// TODO Auto-generated method stub
				bluetoothAdapter.cancelDiscovery();
				try 
				{
					device=bluetoothAdapter.getRemoteDevice(deviceAdapter.getItem(position).getAddress());
					Method createBondMethod = BluetoothDevice.class.getMethod("createBond");
					createBondMethod.invoke(device);   
					devicesAddress=deviceAdapter.getItem(position).getAddress();
					devicesName=deviceAdapter.getItem(position).getName();
				} 
				catch (Exception e) 
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(ShopSearchBlueToothActivity.this, "配对失败!!", Toast.LENGTH_SHORT).show(); 
				}  
			}
		});
	}
	private void initFilter() 
	{
		// TODO Auto-generated method stub
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
		public void onReceive(Context context, Intent intent) 
		{
			// TODO Auto-generated method stub
			String action = intent.getAction(); 
			if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
			{
				if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON)
				{
					Toast.makeText(ShopSearchBlueToothActivity.this,"打开成功", Toast.LENGTH_SHORT).show();
					deviceAdapter.clear();
					bluetoothAdapter.startDiscovery(); 
					time.start();
				}
				else
				{
					//Toast.makeText(SearchBlueToothActivity.this,"打开蓝牙失败", Toast.LENGTH_SHORT).show();
				}
				ColorDialog.dissmissProcessDialog();
			}
			if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action))
			{
				Toast.makeText(ShopSearchBlueToothActivity.this,"开始搜索", Toast.LENGTH_SHORT).show();
				
				scanbutton.setVisibility(View.GONE);
				startAnima();
				startcircularAnima();
			}
		
			if (BluetoothDevice.ACTION_FOUND.equals(action)) 
			{    
				//获取查找到的蓝牙设备    
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);  
                if (!device.getAddress().equals(share.getString("blueAddress", ""))) 
                {    
                	deviceAdapter.addItem(device);
                } 
            }
			if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action))
			{
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);  
                if (device.getBondState() == BluetoothDevice.BOND_BONDED) 
                {    
                    //绑定过的设备    
                	//deviceAdapter.addItem(device);
                	Toast.makeText(ShopSearchBlueToothActivity.this,"配对成功", Toast.LENGTH_SHORT).show();
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
			//计时完毕时触发
			scanbutton.setText("立即扫描");
			bluetoothAdapter.cancelDiscovery();
			scanbutton.setVisibility(View.VISIBLE);
			closeSearch();
		}
		@Override
		public void onTick(long millisUntilFinished)
		{ 
			//计时过程显示
//			scanbutton.setClickable(false);
//			scanbutton.setPressed(true);
//			scanbutton.setText("正在扫描，请稍后("+millisUntilFinished /1000+"秒)");
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
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
				Toast.makeText(ShopSearchBlueToothActivity.this,"打开蓝牙取消", Toast.LENGTH_SHORT).show();
			}
		}
	}
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
