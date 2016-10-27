package com.dudu.helper3.fragment;


import com.dudu.helper3.Activity.WelcomeActivity.LoginActivity;
import com.dudu.helper3.R;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;


public class GuideFragment4 extends Fragment 
{
	private View OrderFragmentView; 
	private Button enterButton;
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		OrderFragmentView= inflater.inflate(R.layout.fragment_guide4, null);
		return OrderFragmentView;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		enterButton=(Button) OrderFragmentView.findViewById(R.id.enterButton);
		enterButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),LoginActivity.class);
				startActivity(intent);
				getActivity().finish();
			}
		});
	}

}
