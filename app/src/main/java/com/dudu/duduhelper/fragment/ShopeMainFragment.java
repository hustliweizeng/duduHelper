package com.dudu.duduhelper.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dudu.duduhelper.Activity.BigBandActivity.shopProductListActivity;
import com.dudu.duduhelper.Activity.CashHistoryActivity.ShopMoneyRecordListActivity;
import com.dudu.duduhelper.Activity.EmployeeManageActivity.ShopMemberListActivity;
import com.dudu.duduhelper.Activity.GetMoneyActivity.ShopGetInComeCashActivity;
import com.dudu.duduhelper.Activity.GuestManageActivity.GuestMangageActivity;
import com.dudu.duduhelper.Activity.OrderActivity.ShopOrderActivity;
import com.dudu.duduhelper.Activity.RedBagActivity.RedBagList;
import com.dudu.duduhelper.Activity.ShopManageActivity.ShopListManagerActivity;
import com.dudu.duduhelper.Activity.PrinterActivity.ShopSearchBlueToothActivity;
import com.dudu.duduhelper.Activity.SummaryActivity.ShopAccountDataActivity;
import com.dudu.duduhelper.Activity.VipUserActivity.VipUserVertifyActivity;
import com.dudu.duduhelper.Activity.fiveDiscountActivity.DiscountSellActivity;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.example.qr_codescan.MipcaActivityCapture;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.dudu.duduhelper.R;
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
	private LinearLayout vip_manager;
	private boolean isVipOpen;
	private boolean isWuzheOpen;
	private String msg;
	private String wuZhemsg;
	private String vipMsg;
	private Context context;

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
		context = getActivity();
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initFragmentView();
		requestVipStatus();//请求会员管理状态
		requestGusetStatus();//请求客户管理状态
		requestStatus();//请求五折卡
		
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("MainFragment");
	}

	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart("MainFragment");
	}

	private void initFragmentView() 
	{
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
		vip_manager = (LinearLayout) MainFragmentView.findViewById(R.id.vip_manager);
		vip_manager.setOnClickListener(this);

	}
	
	@Override
	public void onClick(View v) 
	{
		SharedPreferences sp = getContext().getSharedPreferences("userconig", Context.MODE_PRIVATE);
		boolean isManager = sp.getBoolean("isManager", false);
		boolean isMainShop = sp.getBoolean("isMainShop",false);
		intent = new Intent();
		switch (v.getId())
		{
			case R.id.orderBtnLin:
				//订单按钮
				intent.setClass(context, ShopOrderActivity.class);
				break;
			case R.id.getCashRelBtn:
				//进入收款页面
				intent =new Intent(context,ShopGetInComeCashActivity.class);
				intent.putExtra("name","getMoney");
				break;
			case R.id.getHexiaoRelBtn:
				//核销
				intent =new Intent(context,MipcaActivityCapture.class);
				intent.putExtra("action", "hexiao");
				break;
			case R.id.getCountBtn:
				//统计
				if (isManager){
					intent = new Intent(context, ShopAccountDataActivity.class);
				}else {
					Toast.makeText(context,"您没有管理权限",Toast.LENGTH_SHORT).show();
					return;
				}
				break;
			case R.id.configPrintBtn:
				//打印机
			    intent = new Intent(context,ShopSearchBlueToothActivity.class);
			    break;
			case R.id.dapaibuyBtn:
				//大牌抢购
				intent =new Intent(context,shopProductListActivity.class);
				intent.putExtra("category", "bigband");
			    break;
			case R.id.youhuiBtn:
				//优惠券
				intent =new Intent(context,shopProductListActivity.class);
				intent.putExtra("category", "discount");
			    break;
			case R.id.hongbaoBtn:
				//红包
				if (isManager){
					if (isMainShop){
						startActivity(new Intent(context,RedBagList.class));
					}else {
						Toast.makeText(context,"您没有管理权限",Toast.LENGTH_SHORT).show();
						return;
					}
				}else {
					Toast.makeText(context,"您没有管理权限",Toast.LENGTH_SHORT).show();
					return;
				}
			    return;
			case R.id.wuzheBtn:
				//五折验证
				if (isWuzheOpen){
					intent =new Intent(context,DiscountSellActivity.class);
					intent.putExtra("action", "wuzhe");
				}else {
					if (!TextUtils.isEmpty(wuZhemsg)){
						Toast.makeText(context,wuZhemsg,Toast.LENGTH_SHORT).show();
					}
					return;
				}
				break;
			case R.id.memberBtn:
				//员工管理
				if (isManager){
					if (isMainShop){
						intent =new Intent(context,ShopMemberListActivity.class);
					}else {
						Toast.makeText(context,"您没有管理权限",Toast.LENGTH_SHORT).show();
						return;
					}
				}else {
					Toast.makeText(context,"您没有管理权限",Toast.LENGTH_SHORT).show();
					return;
				}
			    break;
			case R.id.shopmanagerBtn:
				//门店管理
				if (isManager){
					intent =new Intent(context,ShopListManagerActivity.class);
				}else {
					Toast.makeText(context,"您没有管理权限",Toast.LENGTH_SHORT).show();
					return;
				}
				break;
			//账单流水
			case R.id.moneyRecordLinButton:
				intent =new Intent(context,ShopMoneyRecordListActivity.class);
				break;
			case R.id.guest_manager:
				//客户管理
				if (shopIsoPen){//模块是否开启
					/*if(isManager){//员工权限
						intent = new  Intent(context, GuestMangageActivity.class);
					}else {
						Toast.makeText(context,"您没有管理权限",Toast.LENGTH_SHORT).show();
						return;
					}*/
					intent = new  Intent(context, GuestMangageActivity.class);
				}else {
					if (!TextUtils.isEmpty(msg)){
						Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
					}
					return;
				}
				break;
			//会员管理
			case R.id.vip_manager:
				if(isVipOpen){
					intent =new Intent(context,VipUserVertifyActivity.class);
					startActivity(intent);
				}else {
					if (!TextUtils.isEmpty(vipMsg)){
						Toast.makeText(context,vipMsg,Toast.LENGTH_SHORT).show();
					}
					return;
				}
				break;
			default:
				break;
		}
		startActivity(intent);
	}

	/**
	 * 验证会员管理模块
	 */
	private void requestVipStatus() {
		HttpUtils.getConnection(context, null, ConstantParamPhone.GET_VIP_ISOPEN, "get", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				//Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				LogUtil.d("vip",s);
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						isVipOpen = true;
					}else {
						isVipOpen = false;
						//数据请求失败
						vipMsg = object.getString("msg");
						LogUtil.d("vip",vipMsg);
						
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * 五折卡是否开通
	 */
	private void requestStatus() {
		HttpUtils.getConnection(context, null, ConstantParamPhone.CHECK_CARD, "post", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
			//	Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				LogUtil.d("wuzhe",s);
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						isWuzheOpen = true;
					}else {
						isWuzheOpen = false;
								
						//数据请求失败
						wuZhemsg = object.getString("msg");
						LogUtil.d("wuzhe",wuZhemsg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
	}
	/**
	 * 获取客户管理是否开启
	 */
	public void requestGusetStatus(){
		HttpUtils.getConnection(context, null, ConstantParamPhone.GET_GUEST_ISOPEN, "GET", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				//Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				LogUtil.d("guest",s);
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						if (object.getString("status").equals("1")){
							shopIsoPen = true;
						}else{
							shopIsoPen = false;
						}
					}else {
						shopIsoPen = false;
						//数据请求失败
						msg = object.getString("msg");//会员日提醒
						//Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
						LogUtil.d("msg",msg);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		});
	}



}
