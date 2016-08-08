package com.dudu.duduhelper;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.dudu.duduhelper.adapter.FragmentAdapter;
import com.dudu.duduhelper.adapter.ViewPagerAdapter;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.bean.UserBean;
import com.dudu.duduhelper.fragment.GuideFragment1;
import com.dudu.duduhelper.fragment.GuideFragment2;
import com.dudu.duduhelper.fragment.GuideFragment3;
import com.dudu.duduhelper.fragment.GuideFragment4;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.umeng.message.MsgConstant;
import com.umeng.message.PushAgent;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
			device_token = mPushAgent.getRegistrationId();
			
		}
		
		//统计app启动次数
		mPushAgent.onAppStart();
		if(getSharedPreferences("runtime", MODE_PRIVATE).getString("first", "").equals("1"))
		{
//			loadImage=(ImageView) this.findViewById(R.id.loadImage);
//			loadImage.setVisibility(View.VISIBLE);
			if(!TextUtils.isEmpty(share.getString("username", ""))&&!TextUtils.isEmpty(share.getString("password", "")))
			{
				if(share.getString("usertype", "").equals("dianzhang"))
				{
					methord=ConstantParamPhone.GET_USER_INFO;
				}
				else
				{
					methord=ConstantParamPhone.GET_SALER_INFO;
				}
				initTokenData();
//				
			}
			else
			{
				Intent intent=new Intent(this,LoginActivity.class);
				startActivity(intent);
				finish();
			}
			//finish();
		}
		else
		{
			// 初始化组件  
	        initView();  
	        // 初始化数据  
	        initData();  
		}
		getSharedPreferences("runtime", MODE_PRIVATE).edit().putString("first", "1").commit();		
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
    	
    	
    	// 实例化ArrayList对象
//    			views = new ArrayList<View>();
//
//    			// 实例化ViewPager
//    			viewPager = (ViewPager) findViewById(R.id.viewpager);
//
//    			// 实例化ViewPager适配器
//    			vpAdapter = new ViewPagerAdapter(views);
    }  
  
    /** 
     * 初始化数据 
     */  
    private void initData() 
    {
    	// 定义一个布局并设置参数
//    			LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
//    					LinearLayout.LayoutParams.FILL_PARENT,
//    					LinearLayout.LayoutParams.FILL_PARENT);
//
//    			// 初始化引导图片列表
//    			for (int i = 0; i < pics.length; i++) {
//    				ImageView iv = new ImageView(this);
//    				iv.setLayoutParams(mParams);
//    				iv.setImageResource(pics[i]);
//    				views.add(iv);
//    			}
    	
  
        // 设置数据  
	    //前面的views中没有数据 在前面的循环中才插入数据 而此时vpAdapter中已经有数据说明   
	    //初始化adapter的时候 参数传递是传引用  
        // 设置监听  
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
    
    private void initTokenData() 
	{
		// TODO Auto-generated method stub
		SharedPreferences sharePrint= WelcomeActivity.this.getSharedPreferences("printinfo", WelcomeActivity.this.MODE_PRIVATE);
		RequestParams params = new RequestParams();
		params.add("token",WelcomeActivity.this.share.getString("token", ""));
		params.add("version", ConstantParamPhone.VERSION);
		params.add("umeng_token", device_token);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(WelcomeActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+methord, params,new TextHttpResponseHandler(){
	
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				//Toast.makeText(getActivity(), "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				UserBean userBean=new Gson().fromJson(arg2,UserBean.class);
				if(!userBean.getStatus().equals("1"))
				{
					Toast.makeText(WelcomeActivity.this, userBean.getInfo(), Toast.LENGTH_LONG).show();
					share.edit().clear().commit();
					DuduHelperApplication.getInstance().exit();
					//保存用户信息
					
				}
				else
				{
					if(userBean.getStatus().equals("-1006"))
					{
						//Toast.makeText(getActivity(), "出错啦！", Toast.LENGTH_SHORT).show();
						MyDialog.showDialog(WelcomeActivity.this, "该账号已在其他手机登录，请新登录", false, true, "取消", "确定",new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								MyDialog.cancel();
							}
						}, new OnClickListener() {
							
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								Intent intent=new Intent(WelcomeActivity.this,LoginActivity.class);
								startActivity(intent);
							}
						});
						
					}
					
					else
					{
						//Toast.makeText(getActivity(), userBean.getInfo(), Toast.LENGTH_LONG).show();
						//保存用户信息
						SharedPreferences.Editor edit = share.edit(); //编辑文件 
						if(!TextUtils.isEmpty(userBean.getData().getId()))
						{
							edit.putString("userid",userBean.getData().getId());
						}
						if(!TextUtils.isEmpty(userBean.getData().getUsername()))
						{
							edit.putString("username",userBean.getData().getUsername());
						}
						if(!TextUtils.isEmpty(userBean.getData().getShopname()))
						{
							edit.putString("shopname",userBean.getData().getShopname());
						}
						if(!TextUtils.isEmpty(userBean.getData().getShoplogo()))
						{
							edit.putString("shoplogo",userBean.getData().getShoplogo());
						}
						if(!TextUtils.isEmpty(userBean.getData().getMoney()))
						{
							edit.putString("money", userBean.getData().getMoney());
						}
						if(!TextUtils.isEmpty(userBean.getData().getToken()))
						{
							edit.putString("token", userBean.getData().getToken());
						}
						if(!(userBean.getData().getTodaystat()==null))
						{
							edit.putString("todaystatvisitor", userBean.getData().getTodaystat().getVisiter());
							edit.putString("todaystatbuyer", userBean.getData().getTodaystat().getBuyer());
							edit.putString("todaystatorder", userBean.getData().getTodaystat().getOrder());
							edit.putString("todaystatincome", userBean.getData().getTodaystat().getIncome());
						}
						else
						{
							edit.putString("todaystatvisitor", "0");
							edit.putString("todaystatbuyer", "0");
							edit.putString("todaystatorder", "0");
							edit.putString("todaystatincome", "0");
						}
						if(!(userBean.getData().getTotalstat()==null))
						{
							edit.putString("totalstatvisitor", userBean.getData().getTotalstat().getVisiter());
							edit.putString("totalstatbuyer", userBean.getData().getTotalstat().getBuyer());
							edit.putString("totalstatorder", userBean.getData().getTotalstat().getOrder());
							edit.putString("totalstatincome", userBean.getData().getTotalstat().getIncome());
						}
						else
						{
							edit.putString("totalstatvisitor", "0");
							edit.putString("totalstatbuyer", "0");
							edit.putString("totalstatorder", "0");
							edit.putString("totalstatincome", "0");
						}
						if(!(userBean.getData().getBank()==null))
						{
							edit.putString("bankname", userBean.getData().getBank().getBankname());
							edit.putString("bankno", userBean.getData().getBank().getBankno());
							edit.putString("truename", userBean.getData().getBank().getTruename());
							edit.putString("province", userBean.getData().getBank().getProvince());
							edit.putString("city", userBean.getData().getBank().getCity());
							edit.putString("moreinfo", userBean.getData().getBank().getMoreinfo());
						}
						else
						{
							edit.putString("bankname", "");
							edit.putString("bankno", "");
							edit.putString("truename", "");
							edit.putString("province", "");
							edit.putString("city", "");
							edit.putString("moreinfo", "");
						}
					    edit.commit();//保存数据信息 
					    
					}
				}
			}
			@Override
			public void onFinish() 
			{
				Intent intent=new Intent(WelcomeActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
				// TODO Auto-generated method stub
//				if(getActivity()!=null)
//				{
//					shopeNameTextView.setText(((MainActivity)getActivity()).share.getString("shopname", ""));
//					//防止imageLoader闪动
//					ImageAware imageAware = new ImageViewAware(mineImageHead, false);
//					imageLoader.displayImage(((MainActivity)getActivity()).share.getString("shoplogo", ""), imageAware);
//					//imageLoader.displayImage(((MainActivity)getActivity()).share.getString("shoplogo", ""),mineImageHead, options);
//					fangkeNumText.setText(((MainActivity)getActivity()).share.getString("todaystatvisitor", ""));
//					buyerNumText.setText(((MainActivity)getActivity()).share.getString("todaystatbuyer", ""));
//					orderNumText.setText(((MainActivity)getActivity()).share.getString("todaystatorder", ""));
//					earnMoneyTextView.setText("￥"+" "+((MainActivity)getActivity()).share.getString("todaystatincome", ""));
//					getCashMoneyTextView.setText("￥"+" "+((MainActivity)getActivity()).share.getString("money", ""));
//				}
			}
		});
	}


}
