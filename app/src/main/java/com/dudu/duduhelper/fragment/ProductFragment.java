package com.dudu.duduhelper.fragment;

import com.dudu.duduhelper.Activity.BigBandActivity.shopProductListActivity;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.dudu.duduhelper.R;
public class ProductFragment extends Fragment 
{
	private View ProductFragmentView;
	private LinearLayout dapaibuyLine;
	private LinearLayout youhuiquanLine;
	private LinearLayout shopeProductLine;
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		ProductFragmentView= inflater.inflate(R.layout.fragment_product, null);
		return ProductFragmentView;
	}
	 @Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			MobclickAgent.onPageEnd("ProductFragment");
		}

		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			MobclickAgent.onPageStart("ProductFragment");
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
		dapaibuyLine=(LinearLayout) ProductFragmentView.findViewById(R.id.dapaibuyLine);
		dapaibuyLine.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),shopProductListActivity.class);
				intent.putExtra("category", "buying");
				startActivity(intent);
			}
		});
		youhuiquanLine=(LinearLayout) ProductFragmentView.findViewById(R.id.youhuiquanLine);
		youhuiquanLine.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),shopProductListActivity.class);
				intent.putExtra("category", "coupon");
				startActivity(intent);
			}
		});
		shopeProductLine=(LinearLayout) ProductFragmentView.findViewById(R.id.shopeProductLine);
		shopeProductLine.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),shopProductListActivity.class);
				intent.putExtra("category", "hongbao");
				startActivity(intent);
			}
		});
	}

}
