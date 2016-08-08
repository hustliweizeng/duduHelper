package com.dudu.duduhelper;

import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.application.DuduHelperApplication.GetPushNot;
import com.dudu.duduhelper.fragment.CopyOfOrderFragment;
import com.dudu.duduhelper.fragment.GetCashFragment;
import com.dudu.duduhelper.fragment.ShopMineFragment;
import com.dudu.duduhelper.fragment.OrderFragment;
import com.dudu.duduhelper.fragment.ProductFragment;
import com.dudu.duduhelper.fragment.ProductFragment1;
import com.dudu.duduhelper.fragment.MessageCenterFragment;
import com.dudu.duduhelper.fragment.ShopeMainFragment;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;
import com.umeng.update.UmengUpdateAgent;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends BaseActivity 
{
	private FrameLayout FrameLayoutPager;
	private LinearLayout orderlin;
	private LinearLayout selllin;
	private LinearLayout minelin;
	private TextView title;
	private ImageButton selectClickButton;
	private FragmentTransaction ft;
	private FragmentManager fm;
	private CopyOfOrderFragment orderFragment;
	private ShopeMainFragment shopeMainFragment;
	private MessageCenterFragment sellFragment;
	private ShopMineFragment mineFragment;
	private ImageView order_icon;
	private TextView order_text;
	private ImageView sell_icon;
	private TextView sell_text;
	private ImageView mine_icon;
	private TextView mine_text;
	private Button editButton;
	//private PushAgent mPushAgent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
	//Activity被回收导致fragment的getActivity为null的解决办法
	// 加载视图
	private void initView() {
		// TODO Auto-generated method stub
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
		
		shopeMainFragment = new ShopeMainFragment();
		
		orderFragment = new CopyOfOrderFragment();
		sellFragment = new MessageCenterFragment();
		mineFragment = new ShopMineFragment();
		orderlin.setOnClickListener(new tabBtnClick());
		selllin.setOnClickListener(new tabBtnClick());
		minelin.setOnClickListener(new tabBtnClick());
		fm = getSupportFragmentManager();
		ft = fm.beginTransaction();
		ft.add(R.id.FrameLayoutPager, shopeMainFragment).commit();
		
		//当有新订单推送时获取回调函数，跳转到当前orderFragment
        DuduHelperApplication.getInstance().setPushNot(new GetPushNot() 
        {
			
			@Override
			public void getPushCallback() 
			{
				// TODO Auto-generated method stub
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
				ft.replace(R.id.FrameLayoutPager, orderFragment).commit();
				orderFragment.setRefreshing();
			}
		});
	}

	// 设置点击切换事件
	@SuppressLint("ResourceAsColor")
	class tabBtnClick implements View.OnClickListener 
	{
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
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
				ft.replace(R.id.FrameLayoutPager, shopeMainFragment).commit();
				break;
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
				ft.replace(R.id.FrameLayoutPager, sellFragment).commit();
				break;
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
				ft.replace(R.id.FrameLayoutPager, mineFragment).commit();
				break;
			}
		}

	}
	
	@Override
	public void RightButtonClick() 
	{
		// TODO Auto-generated method stub
		super.RightButtonClick();
		Intent intent=new Intent(this,ShopSettingActivity.class);
		startActivity(intent);
		
	}
	

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

}
