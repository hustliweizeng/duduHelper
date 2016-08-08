package com.dudu.duduhelper.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class FragmentAdapter extends FragmentPagerAdapter 
{
	private List<Fragment> list;
    private String[] _titles;
    private String[] _nums;
	public FragmentAdapter(FragmentManager fm,List<Fragment> list,String[] titles,String[] nums) 
	{
		super(fm);
		this.list=list;
		_titles=titles;
		_nums=nums;
		// TODO Auto-generated constructor stub
	}
	@Override
	public CharSequence getPageTitle(int position) {
		return _titles[position];
	}
	
	public String getPageNum(int position) 
	{
		return _nums[position];
	}

	@Override
	public Fragment getItem(int arg0) 
	{
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return list.size();
	}
	
	public void setNums(String[] nums)
	{
		_nums=nums;
	}

}
