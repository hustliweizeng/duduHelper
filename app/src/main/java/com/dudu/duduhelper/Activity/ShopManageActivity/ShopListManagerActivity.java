package com.dudu.duduhelper.Activity.ShopManageActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.adapter.MyCommonNavigatorAdapter;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.ShopListBean;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.dudu.duduhelper.R;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ShopListManagerActivity extends BaseActivity 
{
	//判断数据是否加载完成
    private MyCommonNavigatorAdapter adapter;
	private ShopListBean data;


	private static final String[] CHANNELS = new String[]{"营业中", "审核中", "停业中"};
	private List<String> mDataList = new ArrayList<>(Arrays.asList(CHANNELS));//导航

	private ViewPager mViewPager;
	private MagicIndicator mMagicIndicator;
	private CommonNavigator mCommonNavigator;


	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_list_manager);
		/**
		 * 权限设置
		 */
		boolean isMainShop = sp.getBoolean("isMainShop",false);
		boolean isManager = sp.getBoolean("isManager", false);
		if (isManager){

			if (isMainShop){
				initHeadView("门店管理", true, true, R.drawable.icon_tianjia);
			}else {
				initHeadView("门店管理", true, false, R.drawable.icon_tianjia);
			}
		}else {
			initHeadView("门店管理", true, false, R.drawable.icon_tianjia);
		}

		
		initData();
	}

	@Override
	public void RightButtonClick() 
	{
		Intent intent = new Intent(ShopListManagerActivity.this,ShopAddActivity.class);
		intent.putExtra("source","add");
		startActivity(intent);
	}
	
	@SuppressLint("ResourceAsColor") 
	private void initView() 
	{
		adapter=new MyCommonNavigatorAdapter(this,data.getData());
		//viewpager轮播
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mViewPager.setAdapter(adapter);//联动
		//指示条
		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				
			}

			@Override
			public void onPageSelected(int position) {
				adapter.setCurrentPage(position);
			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});
		mMagicIndicator = (MagicIndicator) findViewById(R.id.magic_indicator);
		mMagicIndicator.setBackgroundColor(Color.parseColor("#ffffff"));
		//导航条，就是下划线
		mCommonNavigator = new CommonNavigator(this);
		mCommonNavigator.setSkimOver(true);
		mCommonNavigator.setAdjustMode(true);
		mCommonNavigator.setAdapter(new CommonNavigatorAdapter() {

			@Override
			public int getCount() {
				return mDataList.size();
			}

			@Override
			public IPagerTitleView getTitleView(Context context, final int index) {
				ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
				clipPagerTitleView.setText(mDataList.get(index));
				clipPagerTitleView.setTextColor(Color.parseColor("#000000"));//文本颜色黑色
				clipPagerTitleView.setClipColor(Color.parseColor("#3dd6bc"));//下划线颜色绿色
				clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						mViewPager.setCurrentItem(index);
					}
				});
				return clipPagerTitleView;
			}

			@Override
			/**
			 * 设置下划线
			 */
			public IPagerIndicator getIndicator(Context context) {
				LinePagerIndicator indicator = new LinePagerIndicator(context);
				float navigatorHeight = UIUtil.dip2px(context, 3);
				float borderWidth = UIUtil.dip2px(context, 1);
				float lineHeight = navigatorHeight - 2 * borderWidth;
				indicator.setLineHeight(lineHeight);
				indicator.setRoundRadius(lineHeight / 2);
				indicator.setYOffset(borderWidth);
				indicator.setColors(Color.parseColor("#3dd6bc"));
				return indicator;
			}
		});
		mMagicIndicator.setNavigator(mCommonNavigator);
		LinearLayout titleContainer = mCommonNavigator.getTitleContainer(); // must after setNavigator
		titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
		titleContainer.setDividerPadding(UIUtil.dip2px(this, 25));
		ViewPagerHelper.bind(mMagicIndicator, mViewPager);//绑定关联
	}
	
	//获取数据
	private void initData() 
	{
		HttpUtils.getConnection(context,null, ConstantParamPhone.GET_SHOP_LIST, "GET",new TextHttpResponseHandler()
		{
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(context, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				LogUtil.d("res",arg2);
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						data = new Gson().fromJson(arg2, ShopListBean.class);
						//防止多次加入数据，先清空原有数据
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				if (data!=null &&data.getData().size()>0){
					
					LogUtil.d("data",data.getData().size()+"");
					initView();//显示页面
				}else {
					Toast.makeText(context,"数据加载失败",Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	//页面返回后，重新加载数据
	@Override
	public void onResume() {
		super.onResume();
		//initData();
	}
}
