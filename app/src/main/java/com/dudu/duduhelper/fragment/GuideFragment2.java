package com.dudu.duduhelper.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.dudu.duduhelper.R;
public class GuideFragment2 extends Fragment 
{
	private View OrderFragmentView;  
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		OrderFragmentView= inflater.inflate(R.layout.fragment_guide2, null);
		return OrderFragmentView;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
	}

}
