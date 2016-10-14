package com.dudu.duduhelper.Activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.Activity.MyInfoActivity.ShopSettingActivity;
import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.application.DuduHelperApplication.GetPushNot;
import com.dudu.duduhelper.fragment.CopyOfOrderFragment;
import com.dudu.duduhelper.fragment.MessageCenterFragment;
import com.dudu.duduhelper.fragment.ShopMineFragment;
import com.dudu.duduhelper.fragment.ShopeMainFragment;

import java.util.LinkedList;

public class MainActivity extends BaseActivity 
{

	private FrameLayout FrameLayoutPager;
	//首页fragment
	private LinearLayout orderlin;
	//消息页面
	private LinearLayout selllin;
	//个人中心页面
	private LinearLayout minelin;
	private TextView title;
	private ImageButton selectClickButton;
	private FragmentTransaction ft;
	private FragmentManager fm;
	private CopyOfOrderFragment orderFragment;
	private ShopeMainFragment MainFragment;
	private MessageCenterFragment messageCenterFragment;
	private ShopMineFragment mineFragment;
	private ImageView order_icon;
	private TextView order_text;
	private ImageView sell_icon;
	private TextView sell_text;
	private ImageView mine_icon;
	private TextView mine_text;
	private Button editButton;
	private LinkedList<Fragment> fragments =  new LinkedList<>();;
	//private PushAgent mPushAgent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("hello","hello");

		setContentView(R.layout.activity_main);
		initHeadView("", false,false, 0);
		DuduHelperApplication.getInstance().addActivity(this);
		
		//开启友盟推送
//		if(mPushAgent==null)
//		{
//			mPushAgent = PushAgent.getInstance(this);
//			//sdk开启通知声音
//			mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
//			mPushAgent.enable();
//			String device_token = mPushAgent.getRegistrationId();
//			
//		}
		
		//统计app启动次数
		//mPushAgent.onAppStart();
		initView();
		if(savedInstanceState!= null)
        {
            String FRAGMENTS_TAG = "android:support:fragments";
            savedInstanceState.remove(FRAGMENTS_TAG);
        }
	}
	long firtTime = 0;
	boolean isFirst = true;
	//返回按钮监听
	@Override
	public void onBackPressed() {
		//如果是第一次点击
		if (isFirst){
			//记录第一次点击的时间
			firtTime = System.currentTimeMillis();
			isFirst = false;
			LogUtil.d("time",firtTime+"'");
			Toast.makeText(context,"再按一次退出",Toast.LENGTH_SHORT).show();
		}else {
			//第二次点击
			if (System.currentTimeMillis()-firtTime>1000){
				DuduHelperApplication application = (DuduHelperApplication) getApplication();
				application.exit();
				LogUtil.d("time",System.currentTimeMillis()-firtTime+"'");
				isFirst = true;
			}
		}
		
	}
	//Activity被回收导致fragment的getActivity为null的解决办法
	// 加载视图
	private void initView() {
		editButton=(Button) this.findViewById(R.id.selectTextClickButton);
		selectClickButton = (ImageButton) this.findViewById(R.id.selectClickButton);
		order_icon = (ImageView) this.findViewById(R.id.order_icon);
		order_text = (TextView) this.findViewById(R.id.order_text);
		sell_icon = (ImageView) this.findViewById(R.id.sell_icon);
		sell_text = (TextView) this.findViewById(R.id.sell_text);
		mine_icon = (ImageView) this.findViewById(R.id.mine_icon);
		mine_text = (TextView) this.findViewById(R.id.mine_text);
		title = (TextView) this.findViewById(R.id.title);
		FrameLayoutPager = (FrameLayout) this.findViewById(R.id.FrameLayoutPager);
		orderlin = (LinearLayout) this.findViewById(R.id.mainlin);
		selllin = (LinearLayout) this.findViewById(R.id.messagelin);
		minelin = (LinearLayout) this.findViewById(R.id.shopelin);

		//初始化fragment，只创建一次


		//设置底部导航的监听
		orderlin.setOnClickListener(new tabBtnClick());
		selllin.setOnClickListener(new tabBtnClick());
		minelin.setOnClickListener(new tabBtnClick());
		//进入首页默认显示第一个fragement
		fm = getSupportFragmentManager();
		ft = fm.beginTransaction();
		ft.add(R.id.FrameLayoutPager, createFragment(1)).commit();
		
		//当有新订单推送时获取回调函数，跳转到当前orderFragment
        DuduHelperApplication.getInstance().setPushNot(new GetPushNot() 
        {

			@Override
			public void getPushCallback() 
			{
				initHeadView("我的订单", false,true, R.drawable.icon_mysearch);
				editButton.setVisibility(View.GONE);
				// MainActivity.this.findViewById(R.id.head).setVisibility(View.VISIBLE);
				order_icon.setImageResource(R.drawable.icon_dingdan_sel);
				order_text.setTextColor(order_text.getResources().getColor(R.color.text_color));
				sell_icon.setImageResource(R.drawable.icon_hexiao);
				sell_text.setTextColor(sell_text.getResources().getColor(R.color.text_color_noselect));
				mine_icon.setImageResource(R.drawable.icon_wode);
				mine_text.setTextColor(mine_text.getResources().getColor(R.color.text_color_noselect));
				ft = fm.beginTransaction();
				ft.replace(R.id.FrameLayoutPager, createFragment(4)).commit();
				orderFragment.setRefreshing();
			}
		});
	}

	/**
	 * 创建fragment缓存
	 * @param i
	 * @return
	 */
	private Fragment createFragment(int i) {
		switch (i){
			case 1:
				//主页
				if (!fragments.contains(MainFragment)){

					MainFragment = new ShopeMainFragment();
					fragments.add(MainFragment);
				}
					return fragments.get(fragments.indexOf(MainFragment));
			case 2:
				//消息中心
				if (!fragments.contains(messageCenterFragment)){
					messageCenterFragment = new MessageCenterFragment();
					fragments.add(messageCenterFragment);

				}
					return fragments.get(fragments.indexOf(messageCenterFragment));
			case 3:
				//个人中心
				if (!fragments.contains(mineFragment)){
					mineFragment = new ShopMineFragment();
					fragments.add(mineFragment);

				}
					return fragments.get(fragments.indexOf(mineFragment));
			case 4:
				//订单中心
				if (!fragments.contains(orderFragment)){

					orderFragment = new CopyOfOrderFragment();
					fragments.add(orderFragment);
				}
					return fragments.get(fragments.indexOf(orderFragment));

		}
		return null;
	}

	// 设置点击切换事件,切换不同的fragment，一共有三个
	@SuppressLint("ResourceAsColor")
	class tabBtnClick implements View.OnClickListener 
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			//进入首页
			case R.id.mainlin:
				initHeadView("", false,false,0);
				editButton.setVisibility(View.GONE);
				order_icon.setImageResource(R.drawable.icon_shouye_sel);
				order_text.setTextColor(order_text.getResources().getColor(R.color.text_color));
				sell_icon.setImageResource(R.drawable.icon_xiaoxi);
				sell_text.setTextColor(sell_text.getResources().getColor(R.color.head_color));
				mine_icon.setImageResource(R.drawable.icon_wode);
				mine_text.setTextColor(mine_text.getResources().getColor(R.color.head_color));
				ft = fm.beginTransaction();
				//主页
				ft.replace(R.id.FrameLayoutPager, createFragment(1)).commit();
				break;
			//进入消息列表
			case R.id.messagelin:
				initHeadView("消息", false, false, 0);
				editButton.setVisibility(View.GONE);
				order_icon.setImageResource(R.drawable.icon_shouye);
				order_text.setTextColor(order_text.getResources().getColor(R.color.head_color));
				sell_icon.setImageResource(R.drawable.icon_xiaoxi_sel);
				sell_text.setTextColor(sell_text.getResources().getColor(R.color.text_color));
				mine_icon.setImageResource(R.drawable.icon_wode);
				mine_text.setTextColor(mine_text.getResources().getColor(R.color.head_color));
				ft = fm.beginTransaction();
				ft.replace(R.id.FrameLayoutPager, createFragment(2)).commit();
				break;
			//进入个人中心
			case R.id.shopelin:
				editButton.setVisibility(View.GONE);
				initHeadView("个人中心", false, true, R.drawable.icon_settings);
				order_icon.setImageResource(R.drawable.icon_shouye);
				order_text.setTextColor(order_text.getResources().getColor(R.color.head_color));
				sell_icon.setImageResource(R.drawable.icon_xiaoxi);
				sell_text.setTextColor(sell_text.getResources().getColor(R.color.head_color));
				mine_icon.setImageResource(R.drawable.icon_wode_sel);
				mine_text.setTextColor(mine_text.getResources().getColor(R.color.text_color));
				ft = fm.beginTransaction();
				ft.replace(R.id.FrameLayoutPager, createFragment(3)).commit();
				break;
			}
		}

	}
	
	@Override
	public void RightButtonClick() 
	{
		super.RightButtonClick();
		Intent intent=new Intent(this,ShopSettingActivity.class);
		startActivity(intent);
	}

}
