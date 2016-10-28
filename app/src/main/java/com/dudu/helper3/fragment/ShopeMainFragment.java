package com.dudu.helper3.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dudu.helper3.Activity.BigBandActivity.shopProductListActivity;
import com.dudu.helper3.Activity.CashHistoryActivity.ShopMoneyRecordListActivity;
import com.dudu.helper3.Activity.EmployeeManageActivity.ShopMemberListActivity;
import com.dudu.helper3.Activity.GetMoneyActivity.ShopGetInComeCashActivity;
import com.dudu.helper3.Activity.GuestManageActivity.GuestMangageActivity;
import com.dudu.helper3.Activity.OrderActivity.ShopOrderActivity;
import com.dudu.helper3.Activity.RedBagActivity.RedBagList;
import com.dudu.helper3.Activity.ShopManageActivity.ShopListManagerActivity;
import com.dudu.helper3.Activity.PrinterActivity.ShopSearchBlueToothActivity;
import com.dudu.helper3.Activity.SummaryActivity.ShopAccountDataActivity;
import com.dudu.helper3.Activity.fiveDiscountActivity.DiscountSellActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.widget.ColorDialog;
import com.example.qr_codescan.MipcaActivityCapture;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

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
	//红包列表状态
	private LinearLayout guest_manager;
	private Intent intent;
	private boolean shopIsoPen;

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
		guest_manager = (LinearLayout) MainFragmentView.findViewById(R.id.guest_manager);
		guest_manager.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) 
	{
		SharedPreferences sp = getContext().getSharedPreferences("userconig", Context.MODE_PRIVATE);
		boolean isManager = sp.getBoolean("isManager", false);
		// TODO Auto-generated method stub
		intent = new Intent();
		switch (v.getId())
		{
			case R.id.orderBtnLin:
				//订单按钮
				intent.setClass(getActivity(), ShopOrderActivity.class);
				break;
			case R.id.getCashRelBtn:
				//进入收款页面
				intent =new Intent(getActivity(),ShopGetInComeCashActivity.class);
				break;
			case R.id.getHexiaoRelBtn:
				//核销
				intent =new Intent(getActivity(),MipcaActivityCapture.class);
				intent.putExtra("action", "hexiao");
				break;
			case R.id.getCountBtn:
				//统计
				intent = new Intent(getActivity(), ShopAccountDataActivity.class);
				break;
			case R.id.configPrintBtn:
				//打印机
			    intent = new Intent(getActivity(),ShopSearchBlueToothActivity.class);
			    break;
			case R.id.dapaibuyBtn:
				//大牌抢购
				intent =new Intent(getActivity(),shopProductListActivity.class);
				intent.putExtra("category", "bigband");
			    break;
			case R.id.youhuiBtn:
				//优惠券
				intent =new Intent(getActivity(),shopProductListActivity.class);
				intent.putExtra("category", "discount");
			    break;
			case R.id.hongbaoBtn:
				startActivity(new Intent(getActivity(),RedBagList.class));
			    return;
			case R.id.wuzheBtn:
				//五折验证
				requestStatus();
			    break;
			case R.id.memberBtn:
				//员工管理
				if (isManager){
					intent =new Intent(getActivity(),ShopMemberListActivity.class);
				}else {
					Toast.makeText(getActivity(),"您没有管理权限",Toast.LENGTH_SHORT).show();
					return;
				}
			    break;
			case R.id.shopmanagerBtn:
				//门店管理
				if (isManager){
					intent =new Intent(getActivity(),ShopListManagerActivity.class);
				}else {
					Toast.makeText(getActivity(),"您没有管理权限",Toast.LENGTH_SHORT).show();
					return;
				}
				break;
			//账单流水
			case R.id.moneyRecordLinButton:
				intent =new Intent(getActivity(),ShopMoneyRecordListActivity.class);
				break;
			case R.id.guest_manager:
				//客户管理
				shopIsoPen = sp.getBoolean("shopstatus",false);
				if (shopIsoPen){
					intent = new  Intent(getActivity(), GuestMangageActivity.class);
				}else {
					Toast.makeText(getActivity(),"当前功能未开通，敬请期待",Toast.LENGTH_SHORT).show();
					return;
				}

				break;
			default:
				break;
		}
		startActivity(intent);
	}

	private void requestStatus() {
		HttpUtils.getConnection(getActivity(), null, ConstantParamPhone.CHECK_CARD, "post", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(getActivity(),"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				LogUtil.d("res",s);
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						intent =new Intent(getActivity(),DiscountSellActivity.class);
						intent.putExtra("action", "wuzhe");

					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
						return;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}


}
