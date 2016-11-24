package com.dudu.duduhelper.fragment;


import java.util.ArrayList;
import java.util.List;

import com.dudu.duduhelper.Activity.OrderActivity.SearchActivity;
import com.dudu.duduhelper.adapter.FragmentAdapter;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import com.dudu.duduhelper.R;
//带viewpager的订单界面，已经废弃
public class OrderFragment extends Fragment 
{
	private View OrderFragmentView;  
	private TextView newOrder;
	private TextView allOrder;
	private View newOrderViewLine;
	private View allOrderViewLine;
	private ViewPager orderviewpager;
	private List<Fragment> FragmentViewlists;
	private FragmentAdapter fragmentAdapter;
	private EditText searchBarEdit;
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		OrderFragmentView= inflater.inflate(R.layout.fragment_order, null);
		return OrderFragmentView;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initViewFragment();
	}
	 @Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			MobclickAgent.onPageEnd("OrderFragment");
		}

		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			MobclickAgent.onPageStart("OrderFragment");
		}
	private void initViewFragment() 
	{
		// TODO Auto-generated method stub
		searchBarEdit=(EditText) OrderFragmentView.findViewById(R.id.searchBarEdit);
		newOrder=(TextView) OrderFragmentView.findViewById(R.id.newOrder);
		allOrder=(TextView) OrderFragmentView.findViewById(R.id.allOrder);
		newOrderViewLine=OrderFragmentView.findViewById(R.id.newOrderViewLine);
		allOrderViewLine=OrderFragmentView.findViewById(R.id.allOrderViewLine);
		orderviewpager=(ViewPager) OrderFragmentView.findViewById(R.id.order_view_pager);
		FragmentViewlists=new ArrayList<Fragment>();
		NewOrderFragment newOrderFragment=new NewOrderFragment();
		AllOrderFragment allOrderFragment=new AllOrderFragment();
		FragmentViewlists.add(newOrderFragment);
		FragmentViewlists.add(allOrderFragment);
		fragmentAdapter = new FragmentAdapter(getChildFragmentManager(),FragmentViewlists,null,null);
		orderviewpager.setAdapter(fragmentAdapter);
		newOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				orderviewpager.setCurrentItem(0, false);
				newOrder.setTextColor(newOrder.getResources().getColor(R.color.text_color));
				newOrderViewLine.setVisibility(View.VISIBLE);
				allOrder.setTextColor(allOrder.getResources().getColor(R.color.text_color_dark));
				allOrderViewLine.setVisibility(View.GONE);
			}
		});
		allOrder.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				orderviewpager.setCurrentItem(1, false);
				allOrder.setTextColor(allOrder.getResources().getColor(R.color.text_color));
				allOrderViewLine.setVisibility(View.VISIBLE);
				newOrder.setTextColor(newOrder.getResources().getColor(R.color.text_color_dark));
				newOrderViewLine.setVisibility(View.GONE);
			}
		});
		//防止Fragment被重建
		orderviewpager.setOffscreenPageLimit(2);
		orderviewpager.setOnPageChangeListener(new OnPageChangeListener() 
		{
			
			@Override
			public void onPageSelected(int arg0) 
			{
				// TODO Auto-generated method stub
				if(arg0==0)
				{
					newOrder.setTextColor(newOrder.getResources().getColor(R.color.text_color));
					newOrderViewLine.setVisibility(View.VISIBLE);
					allOrder.setTextColor(allOrder.getResources().getColor(R.color.text_color_dark));
					allOrderViewLine.setVisibility(View.GONE);
				}
				if(arg0==1)
				{
					allOrder.setTextColor(allOrder.getResources().getColor(R.color.text_color));
					allOrderViewLine.setVisibility(View.VISIBLE);
					newOrder.setTextColor(newOrder.getResources().getColor(R.color.text_color_dark));
					newOrderViewLine.setVisibility(View.GONE);
				}
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		searchBarEdit.setOnEditorActionListener(new OnEditorActionListener() 
		{

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) 
			{
				// TODO Auto-generated method stub
				if(arg1 ==EditorInfo.IME_ACTION_SEARCH)
				{
					//Toast.makeText(getActivity(), "haha", Toast.LENGTH_SHORT).show();

					// 先隐藏键盘
					//((InputMethodManager) searchText.getContext().getSystemService(getActivity().INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
					//跳转activity  
				   Intent intent = new Intent();
				   intent.setClass(getActivity(), SearchActivity.class);
				   intent.putExtra("keyword", searchBarEdit.getText().toString());
				   startActivity(intent);
		            // 将查询的数据插入数据库
	               //mDbHelper.insert_search_history(searchText.getText().toString(), getStringDate());
	               return true;
	            }
				return false;
			}
		});
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

}
