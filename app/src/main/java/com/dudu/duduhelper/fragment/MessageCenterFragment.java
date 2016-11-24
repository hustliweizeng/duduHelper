package com.dudu.duduhelper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dudu.duduhelper.Activity.MessageCentreActivity.MessageCenterListActivity;
import com.jauker.widget.BadgeView;
import com.umeng.analytics.MobclickAgent;
import com.dudu.duduhelper.R;
public class MessageCenterFragment extends Fragment implements OnClickListener
{
	private View MessageCenterFragmentView;
	private LinearLayout systemMessageLin;
	private LinearLayout yunyingMessageLin;
	private LinearLayout caozuoMessageLin;
	private LinearLayout orderMessageLin;
	private ImageView iv_manage_messagelist;
	private ImageView iv_system_messagelist;
	private ImageView iv_operation_messagelist;
	private ImageView iv_order_messagelist;

	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		MessageCenterFragmentView= inflater.inflate(R.layout.shop_message_center_fragment, null);
		return MessageCenterFragmentView;
	}
	 @Override
		public void onPause() {
			// TODO Auto-generated method stub
			super.onPause();
			MobclickAgent.onPageEnd("SellFragment");
		}

		@Override
		public void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			MobclickAgent.onPageStart("SellFragment");
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

		systemMessageLin = (LinearLayout) MessageCenterFragmentView.findViewById(R.id.systemMessageLin);
		yunyingMessageLin = (LinearLayout) MessageCenterFragmentView.findViewById(R.id.yunyingMessageLin);
		caozuoMessageLin = (LinearLayout) MessageCenterFragmentView.findViewById(R.id.caozuoMessageLin);
		orderMessageLin = (LinearLayout) MessageCenterFragmentView.findViewById(R.id.orderMessageLin);
		systemMessageLin.setOnClickListener(this);
		yunyingMessageLin.setOnClickListener(this);
		caozuoMessageLin.setOnClickListener(this);
		orderMessageLin.setOnClickListener(this);
		iv_system_messagelist = (ImageView) MessageCenterFragmentView.findViewById(R.id.iv_system_messagelist);
		iv_manage_messagelist = (ImageView) MessageCenterFragmentView.findViewById(R.id.iv_manage_messagelist);
		iv_operation_messagelist = (ImageView) MessageCenterFragmentView.findViewById(R.id.iv_operation_messagelist);
		iv_order_messagelist = (ImageView) MessageCenterFragmentView.findViewById(R.id.iv_order_messagelist);

		//setMsgRemind(8);//设置消息条目

	}

	/**
	 * 根据网络请求数据，动态消息提醒
	 */
	private void setMsgRemind(int num) {
		//设置小红点消息提醒
		BadgeView badgeView = new BadgeView(getActivity());
		//设置提醒的目标view
		badgeView.setTargetView(iv_manage_messagelist);
		badgeView.setBadgeCount(num);
		//设置背景
		badgeView.setBackgroundResource(R.drawable.circle_remind_msglist_);
		//设置显示位置
		badgeView.setBadgeGravity(Gravity.RIGHT|Gravity.TOP);
		//badgeView.setBadgeMargin(3,0,0,0);
	}


	@Override
	//跳转到消息详情页面
	public void onClick(View v) 
	{
		Intent intent = new Intent();
		switch (v.getId()) 
		{
			case R.id.systemMessageLin:
				intent.setClass(getActivity(), MessageCenterListActivity.class);
				intent.putExtra("type", 1);
				break;
			case R.id.yunyingMessageLin:
				intent.setClass(getActivity(), MessageCenterListActivity.class);
				intent.putExtra("type", 2);
				break;
			case R.id.caozuoMessageLin:
				intent.setClass(getActivity(), MessageCenterListActivity.class);
				intent.putExtra("type", 3);
				break;
			case R.id.orderMessageLin:
				intent.setClass(getActivity(), MessageCenterListActivity.class);
				intent.putExtra("type", 4);
				break;
			default:
				break;
		}
		startActivity(intent);
	}

}
