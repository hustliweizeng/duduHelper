package com.dudu.duduhelper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.dudu.duduhelper.CreateRedBagActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.ShopAccountDataActivity;
import com.dudu.duduhelper.ShopGetInComeCashActivity;
import com.dudu.duduhelper.ShopListManagerActivity;
import com.dudu.duduhelper.ShopMemberListActivity;
import com.dudu.duduhelper.ShopMoneyRecordListActivity;
import com.dudu.duduhelper.ShopOrderActivity;
import com.dudu.duduhelper.ShopSearchBlueToothActivity;
import com.dudu.duduhelper.shopProductListActivity;
import com.example.qr_codescan.MipcaActivityCapture;

public class ShopeMainFragment extends Fragment implements OnClickListener
{
	private View MainFragmentView;
	private LinearLayout orderBtnLin;
	private RelativeLayout getCashRelBtn;
	private RelativeLayout getHexiaoRelBtn;
	private LinearLayout getCountBtn;
	private LinearLayout configPrintBtn;
	private LinearLayout dapaibuyBtn;
	private LinearLayout youhuiBtn;
	private LinearLayout hongbaoBtn;
	private LinearLayout wuzheBtn;
	private LinearLayout memberBtn;
	private LinearLayout shopmanagerBtn;
	private LinearLayout moneyRecordLinButton;
	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		MainFragmentView= inflater.inflate(R.layout.shop_main_fragment, null);
		return MainFragmentView;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initFragmentView();
	}
	private void initFragmentView() 
	{
		// TODO Auto-generated method stub
		shopmanagerBtn = (LinearLayout) MainFragmentView.findViewById(R.id.shopmanagerBtn);
		shopmanagerBtn.setOnClickListener(this);
		orderBtnLin = (LinearLayout) MainFragmentView.findViewById(R.id.orderBtnLin);
		orderBtnLin.setOnClickListener(this);
		getCashRelBtn = (RelativeLayout) MainFragmentView.findViewById(R.id.getCashRelBtn);
		getCashRelBtn.setOnClickListener(this);
		getHexiaoRelBtn = (RelativeLayout) MainFragmentView.findViewById(R.id.getHexiaoRelBtn);
		getHexiaoRelBtn.setOnClickListener(this);
		getCountBtn = (LinearLayout) MainFragmentView.findViewById(R.id.getCountBtn);
		getCountBtn.setOnClickListener(this);
		configPrintBtn = (LinearLayout) MainFragmentView.findViewById(R.id.configPrintBtn);
		configPrintBtn.setOnClickListener(this);
		dapaibuyBtn = (LinearLayout) MainFragmentView.findViewById(R.id.dapaibuyBtn);
		dapaibuyBtn.setOnClickListener(this);
		youhuiBtn = (LinearLayout) MainFragmentView.findViewById(R.id.youhuiBtn);
		youhuiBtn.setOnClickListener(this);
		hongbaoBtn = (LinearLayout) MainFragmentView.findViewById(R.id.hongbaoBtn);
		hongbaoBtn.setOnClickListener(this);
		wuzheBtn = (LinearLayout) MainFragmentView.findViewById(R.id.wuzheBtn);
		wuzheBtn.setOnClickListener(this);
		memberBtn = (LinearLayout) MainFragmentView.findViewById(R.id.memberBtn);
		memberBtn.setOnClickListener(this);
		moneyRecordLinButton = (LinearLayout) MainFragmentView.findViewById(R.id.moneyRecordLinButton);
		moneyRecordLinButton.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		Intent intent=new Intent();
		switch (v.getId())
		{
			case R.id.orderBtnLin:
				intent.setClass(getActivity(), ShopOrderActivity.class);
				break;
			case R.id.getCashRelBtn:
				//进入收款页面
				intent=new Intent(getActivity(),ShopGetInComeCashActivity.class);
				break;
			case R.id.getHexiaoRelBtn:
				intent=new Intent(getActivity(),MipcaActivityCapture.class);

				intent.putExtra("action", "hexiao");
				break;
			case R.id.getCountBtn:
				intent = new Intent(getActivity(), ShopAccountDataActivity.class);
				break;
			case R.id.configPrintBtn:
			    intent = new Intent(getActivity(),ShopSearchBlueToothActivity.class);
			    break;
			case R.id.dapaibuyBtn:
				intent=new Intent(getActivity(),shopProductListActivity.class);
				intent.putExtra("category", "buying");
			    break;
			case R.id.youhuiBtn:
				intent=new Intent(getActivity(),shopProductListActivity.class);
				intent.putExtra("category", "coupon");
			    break;
			case R.id.hongbaoBtn:
				//intent=new Intent(getActivity(),shopProductListActivity.class);
				intent = new Intent(getActivity(), CreateRedBagActivity.class);
				intent.putExtra("category", "hongbao");
			    break;
			case R.id.wuzheBtn:
				intent=new Intent(getActivity(),ShopGetInComeCashActivity.class);
				intent.putExtra("action", "wuzhe");
			    break;
			case R.id.memberBtn:
				intent=new Intent(getActivity(),ShopMemberListActivity.class);
			    break;
			case R.id.shopmanagerBtn:
				intent=new Intent(getActivity(),ShopListManagerActivity.class);
			    break;
			case R.id.moneyRecordLinButton:
				intent=new Intent(getActivity(),ShopMoneyRecordListActivity.class);
			    break;
			default:
				break;
		}
		startActivity(intent);
	}

}
