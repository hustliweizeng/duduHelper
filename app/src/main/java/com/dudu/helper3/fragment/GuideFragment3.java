package com.dudu.helper3.fragment;


import com.dudu.helper3.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class GuideFragment3 extends Fragment 
{
	private View OrderFragmentView;  
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		OrderFragmentView= inflater.inflate(R.layout.fragment_guide3, null);
		return OrderFragmentView;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

}
