package com.dudu.duduhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.adapter.FragmentAdapter;
import com.dudu.duduhelper.adapter.ViewPagerAdapter;
import com.dudu.duduhelper.fragment.GuideFragment1;
import com.dudu.duduhelper.fragment.GuideFragment2;
import com.dudu.duduhelper.fragment.GuideFragment3;
import com.dudu.duduhelper.fragment.GuideFragment4;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

public class WelcomeActivity extends BaseActivity implements OnClickListener,OnPageChangeListener
{
//	 // 定义ViewPager对象  
    private ViewPager viewPager;  
    private List<Fragment> FragmentViewlists;
	private FragmentAdapter fragmentAdapter;
    // 定义ViewPager适配器  
    private ViewPagerAdapter vpAdapter;  
    private LinearLayout linearLayout;
    //定义一个ArrayList来存放View  
    //private ArrayList<View> views;  
  
    // 引导图片资源  
    //private static final int[] pics = { R.drawable.guide1, R.drawable.guide2,  
    //R.drawable.guide3, R.drawable.guide4 };  
    // 底部小点的图片  
    private ImageView[] points;  
    // 记录当前选中位置  
    private int currentIndex;  
    private ImageView loadImage;
    private PushAgent mPushAgent;
    private String device_token;
    private String methord="";

	@Override
	/**
	 * 开启友盟推送
	 * 判断是否第一次启动
	 *
	 */
	protected void onCreate(Bundle savedInstanceState) 
	{

		super.onCreate(savedInstanceState);
		//Log.d("device_token",device_token);
		setContentView(R.layout.activity_welcome);
		//开启友盟推送
		if(mPushAgent==null)
		{
			mPushAgent = PushAgent.getInstance(this);
			//sdk开启通知声音
			mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);
			mPushAgent.enable();
			//获取设备id
			device_token = mPushAgent.getRegistrationId();

		}
		//保存友盟的token信息
		sp.edit().putString("umeng_token",device_token).commit();
		//统计app启动次数
		mPushAgent.onAppStart();
		if(!sp.getBoolean("firstrun", true))
		{
			//如果登陆保存过用户数据,直接请问网络
			if(!TextUtils.isEmpty(sp.getString("username", "")))
			{
//				if(sp.getString("usertype", "").equals("dianzhang"))
//				{
//					methord=ConstantParamPhone.GET_USER_INFO;
//				}
//				else
//				{
//					methord=ConstantParamPhone.GET_SALER_INFO;
//				}
				//请求网络数据，并保存到本地
				requetConnetion();
//				
			}
			else
			{
				//如果没有登陆信息，跳转到登陆页面
				Intent intent=new Intent(this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
			//finish();
		}
		else
		{
			// 是一次启动
	        initView();  
	        // 初始化数据  
	        initData();  
		}
		sp.edit().putBoolean("firstrun", false).commit();
	}
	
	/** 
     * 初始化组件 
     */  
    private void initView() {  
        viewPager = (ViewPager)this.findViewById(R.id.viewpager);  
        linearLayout=(LinearLayout) this.findViewById(R.id.linearLayout);  
        linearLayout.setVisibility(View.VISIBLE);
        FragmentViewlists=new ArrayList<Fragment>();
        FragmentViewlists.add(new GuideFragment1());
  		FragmentViewlists.add(new GuideFragment2());
  		FragmentViewlists.add(new GuideFragment3());
  		FragmentViewlists.add(new GuideFragment4());
  		fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),FragmentViewlists,null,null);
  		viewPager.setAdapter(fragmentAdapter);
  		viewPager.setOffscreenPageLimit(4);
    	
    }
  
    /** 
     * 初始化数据 
     */  
    private void initData() 
    {
        viewPager.setOnPageChangeListener(this);
  
        // 初始化底部小点  
        initPoint();  
    }  
  
    /** 
     * 初始化底部小点 
     */  
    private void initPoint() {  
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);  
  
        points = new ImageView[4];  
  
        // 循环取得小点图片  
        for (int i = 0; i < 4; i++) {  
            // 得到一个LinearLayout下面的每一个子元素  
            points[i] = (ImageView) linearLayout.getChildAt(i);  
            // 默认都设为灰色  
            points[i].setEnabled(true);  
            // 给每个小点设置监听  
            points[i].setOnClickListener(this);  
            // 设置位置tag，方便取出与当前位置对应  
            points[i].setTag(i);  
        }  
  
        // 设置当面默认的位置  
        currentIndex = 0;  
        // 设置为白色，即选中状态  
        points[currentIndex].setEnabled(false);  
    }  
  
    /** 
     * 当滑动状态改变时调用 
     */  
    @Override  
    public void onPageScrollStateChanged(int arg0) {  
  
    }  
  
    /** 
     * 当当前页面被滑动时调用 
     */  
  
    @Override  
    public void onPageScrolled(int arg0, float arg1, int arg2) {  
  
    }  
  
    /** 
     * 当新的页面被选中时调用 
     */  
  
    @Override  
    public void onPageSelected(int position) {  
        // 设置底部小点选中状态  
        setCurDot(position);  
    }  
  
    /** 
     * 通过点击事件来切换当前的页面 
     */  
    @Override  
    public void onClick(View v) {  
        int position = (Integer) v.getTag();  
        setCurView(position);  
        setCurDot(position);  
    }  
  
    /** 
     * 设置当前页面的位置 
     */  
    private void setCurView(int position) {  
        // 排除异常情况  
        if (position < 0 || position >= 4) {  
            return;  
        }  
        viewPager.setCurrentItem(position);  
    }  
  
    /** 
     * 设置当前的小点的位置 
     */  
    private void setCurDot(int positon) {  
        // 排除异常情况  
        if (positon < 0 || positon > 4 - 1 || currentIndex == positon) {  
            return;  
        }  
        points[positon].setEnabled(false);  
        points[currentIndex].setEnabled(true);  
  
        currentIndex = positon;  
    }

	/**
	 * 请问网络连接，通过cookie请求数据（cookie超时以后，需要重新登陆）
	 */
	private void requetConnetion()
	{
		// TODO Auto-generated method stub
		RequestParams params = new RequestParams();
		AsyncHttpClient client = new AsyncHttpClient();
		//请求网络连接之前，设置保存cookie，
		url = ConstantParamPhone.GET_USER_INFO;
		HttpUtils.getConnection(context, params, url, "POST", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"登陆超时，请重新登陆",Toast.LENGTH_LONG).show();

			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				LogUtil.d("welcome",s);
                startActivity(new Intent(context,MainActivity.class));
                finish();
			}
		});

	}


}
