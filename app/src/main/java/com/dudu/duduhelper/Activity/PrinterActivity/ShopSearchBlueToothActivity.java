package com.dudu.duduhelper.Activity.PrinterActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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

import com.dudu.duduhelper.Activity.OrderActivity.ShopOrderDetailActivity;
import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.adapter.DeviceAdapter;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.UUID;
import com.dudu.duduhelper.R;
public class ShopSearchBlueToothActivity extends BaseActivity 
{
	private Button scanbutton;
	private BluetoothAdapter bluetoothAdapter;
	private BluetoothDevice device;
	private DeviceAdapter deviceAdapter;
	private ListView devicesList;
	private TextView bindDevicesTextView;
	private TimeCount time;
	
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
		time = new TimeCount(10000, 1000);//构造CountDownTimer对象
		deviceAdapter=new DeviceAdapter(this);
		initFilter();
		initView();
		
		mAnnularImg = (ImageView) findViewById(R.id.radar_img);
		radartop = (ImageView) findViewById(R.id.radar_top_img);
		radarbttom = (ImageView) findViewById(R.id.radar_bttom_img);
		radarImageing = (ImageView) findViewById(R.id.radar_imageing);
		
	}
	/**指针扫描的动画
	 * 一共有三个动画，一个是用xml写的，时间固定，自动停止
	 * 2个是循环互相开启的
	 */
	private void startAnima() 
	{
		//开始动画
		Animation operatingAnim = AnimationUtils.loadAnimation(this, R.anim.sss);//旋转动画
		LinearInterpolator lin = new LinearInterpolator();
		operatingAnim.setInterpolator(lin);
		radarbttom.setVisibility(View.VISIBLE);
		radarImageing.setVisibility(View.VISIBLE);
		radarImageing.startAnimation(operatingAnim);
	}
	//开始动画循环
	private void startcircularAnima() 
	{
		grayAnimal = playHeartbeatAnimation();
		radarbttom.startAnimation(grayAnimal);
		runnable = new Runnable()//子线程执行
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
	//开始指针
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
				startcircularAnima();//一个循环结束，开始新的动画
			}
		});

	}
	//开始圆圈动画，类似雷达
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
			}

			@Override
			public void onAnimationEnd(Animation animation) 
			{
				mAnnularImg.setVisibility(View.GONE);
			}
		});
		//循环播放
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
		scanbutton=(Button) this.findViewById(R.id.scanbutton);
		bindDevicesTextView=(TextView) this.findViewById(R.id.bindDevicesTextView);
		//初始化列表
		Set<BluetoothDevice> bondedDevices = bluetoothAdapter.getBondedDevices();
		if (bondedDevices !=null && bondedDevices.size()>0){
			bindDevicesTextView.setText("已配对设备");
			for (BluetoothDevice device : bondedDevices){
				//显示已配对列表
				deviceAdapter.addItem(device);
				LogUtil.d("bind",device.getAddress());
			}
			
		}else {
			bindDevicesTextView.setText("搜索到的设备");
			LogUtil.d("bind","没有已配对的");
		}
		
		//搜索按钮
		scanbutton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				//先清除之前搜索的结果
				deviceAdapter.clear();
				bindDevicesTextView.setText("搜索到的设备");
				//开启搜索模式
				if(bluetoothAdapter==null)
				{
					Toast.makeText(ShopSearchBlueToothActivity.this,"该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
					return;
				}  
				
				if(!bluetoothAdapter.isEnabled())//蓝牙是否可用
				{
					Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE); //打开蓝牙    
					intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 3000);     
					startActivity(intent);
				}
				//开始搜索
				bluetoothAdapter.startDiscovery();
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
				//显示按钮
				scanbutton.setVisibility(View.VISIBLE);
				//获取指定位置的设备
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
						Toast.makeText(ShopSearchBlueToothActivity.this, "开始匹配!!", Toast.LENGTH_SHORT).show();
					}
					catch (Exception e)
					{
						e.printStackTrace();
						Toast.makeText(ShopSearchBlueToothActivity.this, "配对失败!!", Toast.LENGTH_SHORT).show();
					}

				}else if(device.getBondState() == BluetoothDevice.BOND_BONDED){
					//已经绑定了，就可以开始连接传输数据了
					Toast.makeText(context,"已绑定",Toast.LENGTH_SHORT).show();//已配对的保存地址
					sp.edit().putString("printId",device.getAddress()).commit();
					//连接设备
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
		intentFilter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//蓝牙打开
		// 注册广播接收器，接收并处理搜索结果      
		registerReceiver(receiver, intentFilter);
	}
	private BroadcastReceiver receiver=new BroadcastReceiver() 
	{
		@Override
		//接收到广播信息
		public void onReceive(Context context, Intent intent) 
		{
			String action = intent.getAction(); 
			switch (action){
				case BluetoothAdapter.ACTION_STATE_CHANGED:
					deviceAdapter.clear();
					bluetoothAdapter.startDiscovery();
					break;
				case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
					Toast.makeText(ShopSearchBlueToothActivity.this,"开始搜索", Toast.LENGTH_SHORT).show();
					scanbutton.setVisibility(View.GONE);
					//开始动画
					startAnima();//指针循环
					startcircularAnima();//无限循环
					time.start();//开始计时10秒
					break;
				case BluetoothDevice.ACTION_FOUND:
					//Toast.makeText(ShopSearchBlueToothActivity.this,"发现设备", Toast.LENGTH_SHORT).show();
					//获取查找到的蓝牙设备    
					BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					if (device.getBondState() != BluetoothDevice.BOND_BONDED){
						//未绑定过的显示
						deviceAdapter.addItem(device);
						LogUtil.d("add",device.getAddress());
					}
					break;
				case BluetoothDevice.ACTION_BOND_STATE_CHANGED://绑定状态改变
					BluetoothDevice device1 = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
					//绑定过的设备    
					if (device1.getBondState() == BluetoothDevice.BOND_BONDED)
					{
						LogUtil.d("bind",device1.getName()+"已经绑定");
						Toast.makeText(ShopSearchBlueToothActivity.this,"配对成功", Toast.LENGTH_SHORT).show();
						sp.edit().putString("printId",device1.getAddress()).commit();//已配对的保存地址，可能会覆盖，记录一个就够用了
						//绑定成功后返回到详情页面
						String orderDetail = getIntent().getStringExtra("source");
						if (orderDetail!=null && orderDetail.equals("orderDetail")){
							Intent intent1 = new Intent(context, ShopOrderDetailActivity.class);
							intent1.putExtra("device",device1.getAddress());//把绑定好的地址传回去
							LogUtil.d("res","返回地址");
							ShopSearchBlueToothActivity.this.setResult(2,intent1);
							finish();//结束当前页面
						}
					}
					if (device1.getBondState() == BluetoothDevice.BOND_BONDING){
						Toast.makeText(context,"正在绑定，请稍后！",Toast.LENGTH_SHORT).show();
					}
					if (device1.getBondState() == BluetoothDevice.BOND_NONE)
					{
						//未绑定的设备
						Toast.makeText(ShopSearchBlueToothActivity.this,"配对失败", Toast.LENGTH_SHORT).show();
					}
					break;
				case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
					Toast.makeText(context,"搜索结束",Toast.LENGTH_SHORT).show();
					closeSearch();
					break;
			}}
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
		}
	}

	//关闭动画
	public void closeSearch()
	{
		handler.removeCallbacks(runnable);
		handler.removeCallbacks(runnable2);
		scanbutton.setVisibility(View.VISIBLE);
		
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
