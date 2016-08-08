package com.dudu.duduhelper.fragment;
import org.apache.http.Header;

import com.dudu.duduhelper.CashSellActivity;
import com.dudu.duduhelper.DiscountSellActivity;
import com.dudu.duduhelper.GiftSellActivity;
import com.dudu.duduhelper.LoginActivity;
import com.dudu.duduhelper.MainActivity;
import com.dudu.duduhelper.MemberSellActivity;
import com.dudu.duduhelper.MessageCenterListActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.bean.GetFirstCardBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage.MessageLevel;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MessageCenterFragment extends Fragment implements OnClickListener
{
	private View MessageCenterFragmentView;
	private LinearLayout systemMessageLin;
	private LinearLayout yunyingMessageLin;
	private LinearLayout caozuoMessageLin;
	private LinearLayout orderMessageLin;
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
//		discountSellButton=(LinearLayout) SellFragmentView.findViewById(R.id.discountSellButton);
//		discountSellButton.setOnClickListener(new OnClickListener() 
//		{
//			@Override
//			public void onClick(View v) 
//			{
//				// TODO Auto-generated method stub
//				getFirstData();
//				
//			}
//		});
	}
	@Override
	public void onClick(View v) 
	{
		Intent intent = new Intent();
		switch (v.getId()) 
		{
			case R.id.systemMessageLin:
				intent.setClass(getActivity(), MessageCenterListActivity.class);
				intent.putExtra("type", "");
				break;
			case R.id.yunyingMessageLin:
				intent.setClass(getActivity(), MessageCenterListActivity.class);
				intent.putExtra("type", "");
				break;
			case R.id.caozuoMessageLin:
				intent.setClass(getActivity(), MessageCenterListActivity.class);
				intent.putExtra("type", "");
				break;
			case R.id.orderMessageLin:
				intent.setClass(getActivity(), MessageCenterListActivity.class);
				intent.putExtra("type", "");
				break;
			default:
				break;
		}
		startActivity(intent);
	}
	//获取五折卡前缀
//	private void getFirstData() 
//	{
//		// TODO Auto-generated method stub
//		ColorDialog.showRoundProcessDialog(getActivity(),R.layout.loading_process_dialog_color);
//		RequestParams params = new RequestParams();
//		params.add("token", ((MainActivity)getActivity()).share.getString("token", ""));
//		params.add("version", ConstantParamPhone.VERSION);
//		params.setContentEncoding("UTF-8");
//		AsyncHttpClient client = new AsyncHttpClient();
//		//保存cookie，自动保存到了shareprefercece  
//        PersistentCookieStore myCookieStore = new PersistentCookieStore(getActivity());    
//        client.setCookieStore(myCookieStore); 
//        client.post(ConstantParamPhone.IP+ConstantParamPhone.GET_FIRST_CARD, params,new TextHttpResponseHandler(){
//
//			@Override
//			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
//			{
//				Toast.makeText(getActivity(), "网络不给力呀", Toast.LENGTH_LONG).show();
//			}
//			@Override
//			public void onSuccess(int arg0, Header[] arg1, String arg2) 
//			{
//				GetFirstCardBean getFirstCardBean=new Gson().fromJson(arg2, GetFirstCardBean.class);
//				if(getFirstCardBean.getStatus().equals("-1006"))
//				{
//					MyDialog.showDialog(getActivity(), "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							MyDialog.cancel();
//						}
//					}, new OnClickListener() {
//						
//						@Override
//						public void onClick(View v) {
//							// TODO Auto-generated method stub
//							Intent intent=new Intent(getActivity(),LoginActivity.class);
//							startActivity(intent);
//						}
//					});
//				}
//				if(getFirstCardBean.getStatus().equals("0"))
//				{
//					if(!TextUtils.isEmpty(getFirstCardBean.getData()))
//					{
//						//discountFirstNumTextView.setText();
//						Intent intent=new Intent(getActivity(),DiscountSellActivity.class);
//						intent.putExtra("first", getFirstCardBean.getData());
//						startActivity(intent);
//					}
//				}
//				if(getFirstCardBean.getStatus().equals("-1"))
//				{
//					Toast.makeText(getActivity(), getFirstCardBean.getInfo().split(" ")[1], Toast.LENGTH_SHORT).show();
//				}			
//			}
//			@Override
//			public void onFinish() 
//			{
//				// TODO Auto-generated method stub
//				ColorDialog.dissmissProcessDialog();
//			}
//		});
//	}
}
