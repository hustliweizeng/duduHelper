package com.dudu.duduhelper.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.dudu.duduhelper.CreateRedBagActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.ShopAccountDataActivity;
import com.dudu.duduhelper.ShopGetInComeCashActivity;
import com.dudu.duduhelper.ShopListManagerActivity;
import com.dudu.duduhelper.ShopMemberListActivity;
import com.dudu.duduhelper.ShopMoneyRecordListActivity;
import com.dudu.duduhelper.ShopOrderActivity;
import com.dudu.duduhelper.ShopSearchBlueToothActivity;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.shopProductListActivity;
import com.dudu.duduhelper.widget.ColorDialog;
import com.example.qr_codescan.MipcaActivityCapture;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

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
	boolean IsRedbagEmpty = false;

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
				//订单按钮
				intent.setClass(getActivity(), ShopOrderActivity.class);
				break;
			case R.id.getCashRelBtn:
				//进入收款页面
				intent=new Intent(getActivity(),ShopGetInComeCashActivity.class);
				break;
			case R.id.getHexiaoRelBtn:
				//核销
				intent=new Intent(getActivity(),MipcaActivityCapture.class);
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
				intent=new Intent(getActivity(),shopProductListActivity.class);
				intent.putExtra("category", "buying");
			    break;
			case R.id.youhuiBtn:
				//优惠券
				intent=new Intent(getActivity(),shopProductListActivity.class);
				intent.putExtra("category", "coupon");
			    break;
			case R.id.hongbaoBtn:
				//进入红包页面
				//intent=new Intent(getActivity(),shopProductListActivity.class);
				/**
				 * 进入之前请求服务器数据，判断是否有红包，根据状态进入不同页面
				 * 异步加载，弹出进度条
				 */
				requestRedbagStatus();
				//intent = new Intent(getActivity(), CreateRedBagActivity.class);
				//intent.putExtra("category", "hongbao");
			    return;
			case R.id.wuzheBtn:
				//五折验证
				intent=new Intent(getActivity(),ShopGetInComeCashActivity.class);
				intent.putExtra("action", "wuzhe");
			    break;
			case R.id.memberBtn:
				//员工管理
				intent=new Intent(getActivity(),ShopMemberListActivity.class);
			    break;
			case R.id.shopmanagerBtn:
				//门店管理
				intent=new Intent(getActivity(),ShopListManagerActivity.class);
			    break;
			//账单流水
			case R.id.moneyRecordLinButton:
				intent=new Intent(getActivity(),ShopMoneyRecordListActivity.class);
			    break;
			default:
				break;
		}
		startActivity(intent);
	}

	/**
	 * 请求红包状态
	 */
	private void requestRedbagStatus() {
		//弹对话框
		//ColorDialog.showRoundProcessDialog(getActivity(),R.layout.loading_process_dialog_color);
		String url = ConstantParamPhone.GET_REDBAG_LIST;
		RequestParams parmas = new RequestParams();
		/**
		 *
		 "token" => "用户Token"
		 "page" => "0"
		 "sort_list" => "getmore,getless,moremoney,lessmoney"
		 "status_list" => "all,ended,releasing"
		 ]
		 */
		//请求参数设置
		String token = getActivity().getSharedPreferences("userconig", Context.MODE_PRIVATE).getString("umeng_token","");
		parmas.put("token",token);
		parmas.put("page",1);
		parmas.put("sort_list","getmore,getless,moremoney,lessmoney");
		parmas.put("status_list","all,ended,releasing");
		//请求结果处理
		HttpUtils.getConnection(getActivity(), parmas, url, "POST", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(getActivity(), "网络不给力呀", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				//设置有无红包的状态,解析处理json数据
				Log.d("redbag",s);
				ColorDialog.dissmissProcessDialog();
				//默认设置没有红包

				IsRedbagEmpty =  true;
				if (IsRedbagEmpty){
					startActivity(new Intent(getActivity(),CreateRedBagActivity.class));

				}else {
					startActivity(new Intent(getActivity(),shopProductListActivity.class));

				}

			}
		});
	}

}
