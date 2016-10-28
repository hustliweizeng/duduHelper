package com.dudu.helper3.Activity.WelcomeActivity;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.Activity.MainActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.adapter.FragmentAdapter;
import com.dudu.helper3.adapter.ViewPagerAdapter;
import com.dudu.helper3.fragment.GuideFragment1;
import com.dudu.helper3.fragment.GuideFragment2;
import com.dudu.helper3.fragment.GuideFragment4;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.InfoBean;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

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
  
    // 底部小点的图片  
    private ImageView[] points;  
    // 记录当前选中位置  
    private int currentIndex;  
    private ImageView loadImage;
    private PushAgent mPushAgent;
    private String device_token;
    private String methord="";
	private boolean shopIsoPen;

	@Override
	/**
	 * 开启友盟推送
	 * 判断是否第一次启动
	 *
	 */
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
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
				//直接登录
				RequestParams params = new RequestParams();
				params.add("username",sp.getString("loginname",""));
				params.add("password",sp.getString("password",""));
				String umeng_token = getSharedPreferences("umeng_token",MODE_PRIVATE).getString("token","");
				params.add("umeng_token",umeng_token);
				HttpUtils.getConnection(context, params, ConstantParamPhone.USER_LOGIN, "post", new TextHttpResponseHandler() {
					@Override
					public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
						//登录失败跳转到登录页面
						finish();
						startActivity(new Intent(context,LoginActivity.class));
					}

					@Override
					public void onSuccess(int i, Header[] headers, String s) {
						try {
							JSONObject object = new JSONObject(s);
							String code =  object.getString("code");
							if ("SUCCESS".equalsIgnoreCase(code)){
								//请求用户数据
								requetConnetion();

							}else {
								//数据请求失败
								String msg = object.getString("msg");
								Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
				
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
			sp.edit().putBoolean("firstrun", false).commit();
		}
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
  		//FragmentViewlists.add(new GuideFragment3());
  		FragmentViewlists.add(new GuideFragment4());
  		fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),FragmentViewlists,null,null);
  		viewPager.setAdapter(fragmentAdapter);
  		viewPager.setOffscreenPageLimit(3);
    	
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
	    //线性布局定义小圆点
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.linearLayout);  
        //定义小圆点数量
        points = new ImageView[3];
  
        // 循环取得小点图片  
        for (int i = 0; i < 3; i++) {  
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
        if (position < 0 || position > 3) {  
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
		RequestParams params = new RequestParams();
		//请求网络连接之前，设置保存cookie，
		url = ConstantParamPhone.GET_USER_INFO;
		HttpUtils.getConnection(context, params, url, "POST", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络故障，请重试",Toast.LENGTH_LONG).show();
				startActivity(new Intent(context,LoginActivity.class));
			}
			@Override
			public void onSuccess(int i, Header[] headers, String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code =  object.getString("code");
                    if ("SUCCESS".equalsIgnoreCase(code)){
                        //数据请求成功
                        InfoBean infoBean = new Gson().fromJson(s, InfoBean.class);
	                    //判断有没有绑定手机
	                    if (TextUtils.isEmpty(infoBean.getUser().getMobile())){
		                    Toast.makeText(context,"您还没有绑定手机号!",Toast.LENGTH_SHORT).show();
		                    startActivity(new Intent(context,LoginBindPhoneActivity.class));
		                    finish();
		                    return;
	                    };
	                    requestStatus();
	                    String isshopuser = infoBean.getUser().getIsshopuser();
	                    boolean isManager = true;
	                    if ("1".equals(isshopuser)){
		                    isManager = false;
		                    LogUtil.d("manager","false");
	                    }
	                    if ("0".equals(isshopuser)) {
		                    isManager = true;
		                    LogUtil.d("manager","true");
	                    }
                        //保存用户信息
                        //1.通过sp保存用户信息
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("username",infoBean.getUser().getName())
                        .putString("nickename",infoBean.getUser().getNickname())//手动添加
                        .putString("mobile",infoBean.getUser().getMobile())
                        //2.存储商店信息
                        .putString("id",infoBean.getShop().getId())
                        .putString("shopLogo",infoBean.getShop().getLogo())
                        .putString("shopName",infoBean.getShop().getName())
                        //3.储存今日状态
                        .putString("todayIncome",infoBean.getTodaystat().getIncome())
                        //4.存储总计状态
                        .putString("frozenMoney",infoBean.getTotalstat().getFreezemoney())
                        .putString("useableMoney",infoBean.getTotalstat().getUsablemoney())
		                //访客信息
		                 .putString("totalVistor",infoBean.getTotalstat().getVisitor())
		                 .putString("totalBuyer",infoBean.getTotalstat().getBuyer())
		                 .putString("totalOrder",infoBean.getTotalstat().getOrder())
		                  .putString("totalIncome",infoBean.getTotalstat().getIncome()) 
		                  .putString("totalTrade",infoBean.getTotalstat().getTrade()) 
		                  .putBoolean("isManager",isManager)      
		                  //在后台处理
                        .apply();
                        LogUtil.d("welcome",s);
                        startActivity(new Intent(context,MainActivity.class));
                        finish();
                    }else {
                        //数据请求失败
                        String msg = object.getString("msg");
                        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
	                    //跳转到登陆页面
	                    startActivity(new Intent(context,LoginActivity.class));
	                    finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
			}
		});
	}
	public void requestStatus(){
		HttpUtils.getConnection(context, null, ConstantParamPhone.GET_GUEST_ISOPEN, "GET", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				LogUtil.d("res",s);
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						if (object.getString("status").equals("1")){
							shopIsoPen = true;
						}else{
							shopIsoPen = false;
						}
						sp.edit().putBoolean("shopstatus", shopIsoPen).commit();
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});
	}


}
