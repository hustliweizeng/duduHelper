package com.dudu.helper3.fragment;


import java.util.ArrayList;
import java.util.List;

import com.dudu.helper3.Activity.GetMoneyActivity.ShopGetInComeCashActivity;
import com.dudu.duduhelper.R;
import com.dudu.helper3.adapter.FragmentAdapter;
import com.dudu.helper3.widget.PagerSlidingTabStrip;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

public class GetCashFragment extends Fragment 
{
	private View GetCashFragmentView;
	private List<Fragment> FragmentViewlists;
	private FragmentAdapter fragmentAdapter;
	private PagerSlidingTabStrip orderSeliderTabs;
	private ViewPager OrderViewPager;
	//编辑按钮
	private Button editButton;
	String[] titles = { "全部", "已支付","未支付"};
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		//return super.onCreateView(inflater, container, savedInstanceState);
		GetCashFragmentView= inflater.inflate(R.layout.fragment_getcash_layout, null);
		return GetCashFragmentView;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initViewFragment();
	}
	private void initViewFragment() 
	{
		// TODO Auto-generated method stub
		//重写父类button,点击弹出checkbox
		editButton=(Button) getActivity().findViewById(R.id.selectTextClickButton);
		editButton.setText("立即收款");
		editButton.setVisibility(View.VISIBLE);
		editButton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),ShopGetInComeCashActivity.class);
				startActivity(intent);
			}
		});
		orderSeliderTabs=(PagerSlidingTabStrip) GetCashFragmentView.findViewById(R.id.orderSeliderTabs);
		OrderViewPager=(ViewPager) GetCashFragmentView.findViewById(R.id.OrderViewPager);
		FragmentViewlists=new ArrayList<Fragment>();
		GetCashSonFragment allFragment=new GetCashSonFragment();
		Bundle bundle1 = new Bundle();
		bundle1.putString("type","0");
		allFragment.setArguments(bundle1);
		
		GetCashSonFragment1 payFragment=new GetCashSonFragment1();
		Bundle bundle2 = new Bundle();
		bundle2.putString("type","2");
		allFragment.setArguments(bundle2);
		
		GetCashSonFragment2 noPayFragment=new GetCashSonFragment2();
		Bundle bundle3 = new Bundle();
		bundle1.putString("type","1");
		allFragment.setArguments(bundle3);
		//GetCashSonFragment closeFragment=new GetCashSonFragment("3");
		FragmentViewlists.add(allFragment);
		FragmentViewlists.add(payFragment);
		FragmentViewlists.add(noPayFragment);
		//FragmentViewlists.add(closeFragment);
		fragmentAdapter = new FragmentAdapter(getChildFragmentManager(),FragmentViewlists,titles,null);
		OrderViewPager.setAdapter(fragmentAdapter);
		orderSeliderTabs.setViewPager(OrderViewPager);
		//防止Fragment被重建
		OrderViewPager.setOffscreenPageLimit(3);
	}
}
