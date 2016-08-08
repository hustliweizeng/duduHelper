package com.dudu.duduhelper.fragment;

import org.apache.http.Header;

import com.dudu.duduhelper.SettingActivity;
import com.dudu.duduhelper.ShopAccountDataActivity;
import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.CashSellActivity;
import com.dudu.duduhelper.GetCashActivity;
import com.dudu.duduhelper.LoginActivity;
import com.dudu.duduhelper.LoginBindPhoneActivity;
import com.dudu.duduhelper.MainActivity;
import com.dudu.duduhelper.ShopBankListActivity;
import com.dudu.duduhelper.ShopMemberListActivity;
import com.dudu.duduhelper.ShopOrderDetailActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.ShopSearchBlueToothActivity;
import com.dudu.duduhelper.ShopSettingActivity;
import com.dudu.duduhelper.R.id;
import com.dudu.duduhelper.UserBankInfoConfirmActivity;
import com.dudu.duduhelper.ShopUserBankInfoEditActivity;
import com.dudu.duduhelper.UserBankSelectActivity;
import com.dudu.duduhelper.WebPageActivity;
import com.dudu.duduhelper.bean.UserBean;
import com.dudu.duduhelper.common.Util;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.CircleImageView;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.dudu.duduhelper.widget.OverScrollView;
import com.dudu.duduhelper.widget.OverScrollView.OverScrollTinyListener;
import com.dudu.duduhelper.wxapi.WXEntryActivity;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.mining.app.zxing.decoding.Intents.Share;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class ShopMineFragment extends Fragment 
{
	private OverScrollView mineScrollview;
	private ImageView mineImageHead;
	private RelativeLayout mineheadRelLine;
	private RelativeLayout relupdate;
	private RelativeLayout bankCardRel;
	//private RelativeLayout CashRel;
	//private RelativeLayout accountDataRel;
	//private RelativeLayout printRel;
	private RelativeLayout helpRel;
	private RelativeLayout aboutRel;
	//private TextView printTextView;
	private View MineFragmentView;
	//private TextView fangkeNumText;
	private TextView buyerNumText;
	//private TextView orderNumText;
	private TextView earnMoneyTextView;
	private TextView getCashMoneyTextView;
	private TextView mineText;
	private DisplayImageOptions options;
	private TextView shopeNameTextView;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private SharedPreferences share;
	private BluetoothAdapter bluetoothAdapter;
	private LinearLayout lin1;
	private RelativeLayout rel2;
	private String methord;
	private RelativeLayout qcodeImgRel;
	private RelativeLayout getCashButtonRel;
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		MineFragmentView= inflater.inflate(R.layout.shop_fragment_mine, null);
		return MineFragmentView;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		if(getActivity()!=null)
		{
			share=((MainActivity)getActivity()).share;
		}
		options = new DisplayImageOptions.Builder()
		.showImageOnLoading(R.drawable.icon_head)
		.showImageForEmptyUri(R.drawable.icon_head)
		.showImageOnFail(R.drawable.icon_head)
		.cacheInMemory(true).considerExifParams(true)
		.bitmapConfig(Bitmap.Config.RGB_565)
		.displayer(new FadeInBitmapDisplayer(100)).build();
		initFragemntView();
	}
	@Override
	public void onPause() 
	{
		// TODO Auto-generated method stub
		super.onPause();
		MobclickAgent.onPageEnd("MineFragment");
	}

	@Override
	public void onResume()
	{
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("MineFragment");
		if(share.getString("usertype", "").equals("dianzhang"))
		{
			methord = ConstantParamPhone.GET_USER_INFO;
		}
		else
		{
			methord = ConstantParamPhone.GET_SALER_INFO;			
		}
		initData();
	}
	private void initData() 
	{
		// TODO Auto-generated method stub
		SharedPreferences sharePrint= getActivity().getSharedPreferences("printinfo", getActivity().MODE_PRIVATE);
		if(!TextUtils.isEmpty(sharePrint.getString("blueName", "")))
		{
			//printTextView.setText(sharePrint.getString("blueName", ""));
		}
		RequestParams params = new RequestParams();
		params.add("token",((MainActivity)getActivity()).share.getString("token", ""));
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(getActivity());    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+methord, params,new TextHttpResponseHandler()
        {
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				//Toast.makeText(getActivity(), "网络不给力呀", Toast.LENGTH_LONG).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				UserBean userBean=new Gson().fromJson(arg2,UserBean.class);
				if(!userBean.getStatus().equals("1"))
				{
					Toast.makeText(getActivity(), userBean.getInfo(), Toast.LENGTH_LONG).show();
					//保存用户信息
					
				}
				if(userBean.getStatus().equals("-1006"))
				{
					//Toast.makeText(getActivity(), "出错啦！", Toast.LENGTH_SHORT).show();
					MyDialog.showDialog(getActivity(), "该账号已在其他手机登录，请新登录", false, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(getActivity(),LoginActivity.class);
							startActivity(intent);
						}
					});
					
				}
				
				else
				{
					//Toast.makeText(getActivity(), userBean.getInfo(), Toast.LENGTH_LONG).show();
					//保存用户信息
					SharedPreferences.Editor edit = share.edit(); //编辑文件 
					if(!TextUtils.isEmpty(userBean.getData().getId()))
					{
						edit.putString("userid",userBean.getData().getId());
					}
					if(!TextUtils.isEmpty(userBean.getData().getUsername()))
					{
						edit.putString("username",userBean.getData().getUsername());
					}
					if(!TextUtils.isEmpty(userBean.getData().getShopname()))
					{
						edit.putString("shopname",userBean.getData().getShopname());
					}
					if(!TextUtils.isEmpty(userBean.getData().getShoplogo()))
					{
						edit.putString("shoplogo",userBean.getData().getShoplogo());
					}
					if(!TextUtils.isEmpty(userBean.getData().getMoney()))
					{
						edit.putString("money", userBean.getData().getMoney());
					}
					if(!TextUtils.isEmpty(userBean.getData().getToken()))
					{
						edit.putString("token", userBean.getData().getToken());
					}
					if(!(userBean.getData().getTodaystat()==null))
					{
						edit.putString("todaystatvisitor", userBean.getData().getTodaystat().getVisiter());
						edit.putString("todaystatbuyer", userBean.getData().getTodaystat().getBuyer());
						edit.putString("todaystatorder", userBean.getData().getTodaystat().getOrder());
						edit.putString("todaystatincome", userBean.getData().getTodaystat().getIncome());
					}
					else
					{
						edit.putString("todaystatvisitor", "0");
						edit.putString("todaystatbuyer", "0");
						edit.putString("todaystatorder", "0");
						edit.putString("todaystatincome", "0");
					}
					if(!(userBean.getData().getTotalstat()==null))
					{
						edit.putString("totalstatvisitor", userBean.getData().getTotalstat().getVisiter());
						edit.putString("totalstatbuyer", userBean.getData().getTotalstat().getBuyer());
						edit.putString("totalstatorder", userBean.getData().getTotalstat().getOrder());
						edit.putString("totalstatincome", userBean.getData().getTotalstat().getIncome());
					}
					else
					{
						edit.putString("totalstatvisitor", "0");
						edit.putString("totalstatbuyer", "0");
						edit.putString("totalstatorder", "0");
						edit.putString("totalstatincome", "0");
					}
					if(!(userBean.getData().getBank()==null))
					{
						edit.putString("bankname", userBean.getData().getBank().getBankname());
						edit.putString("bankno", userBean.getData().getBank().getBankno());
						edit.putString("truename", userBean.getData().getBank().getTruename());
						edit.putString("province", userBean.getData().getBank().getProvince());
						edit.putString("city", userBean.getData().getBank().getCity());
						edit.putString("moreinfo", userBean.getData().getBank().getMoreinfo());
					}
					else
					{
						edit.putString("bankname", "");
						edit.putString("bankno", "");
						edit.putString("truename", "");
						edit.putString("province", "");
						edit.putString("city", "");
						edit.putString("moreinfo", "");
					}
				    edit.commit();//保存数据信息 
				}
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				if(getActivity()!=null)
				{
					shopeNameTextView.setText(((MainActivity)getActivity()).share.getString("shopname", ""));
					//防止imageLoader闪动
					ImageAware imageAware = new ImageViewAware(mineImageHead, false);
					imageLoader.displayImage(((MainActivity)getActivity()).share.getString("shoplogo", ""), imageAware);
					//imageLoader.displayImage(((MainActivity)getActivity()).share.getString("shoplogo", ""),mineImageHead, options);
					//fangkeNumText.setText(((MainActivity)getActivity()).share.getString("todaystatvisitor", ""));
					buyerNumText.setText(((MainActivity)getActivity()).share.getString("todaystatbuyer", ""));
					//orderNumText.setText(((MainActivity)getActivity()).share.getString("todaystatorder", ""));
					earnMoneyTextView.setText(((MainActivity)getActivity()).share.getString("todaystatincome", "")+"↑");
					getCashMoneyTextView.setText(((MainActivity)getActivity()).share.getString("money", ""));
				}
			}
		});
	}
	private void initFragemntView() 
	{
		// TODO Auto-generated method stub
		getCashButtonRel=(RelativeLayout) MineFragmentView.findViewById(R.id.getCashButtonRel);
		lin1=(LinearLayout) MineFragmentView.findViewById(R.id.lin1);
		rel2=(RelativeLayout) MineFragmentView.findViewById(R.id.rel2);
		mineheadRelLine=(RelativeLayout) MineFragmentView.findViewById(R.id.mineheadRelLine);
		//printTextView=(TextView) MineFragmentView.findViewById(R.id.printTextView);
		//printRel=(RelativeLayout) MineFragmentView.findViewById(R.id.printRel);
		helpRel=(RelativeLayout) MineFragmentView.findViewById(R.id.helpRel);
		aboutRel=(RelativeLayout) MineFragmentView.findViewById(R.id.aboutRel);
		getCashButtonRel.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),ShopBankListActivity.class);
				intent.putExtra("action", "tixian");
				startActivity(intent);
			}
		});
		mineheadRelLine.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),ShopSettingActivity.class);
				startActivity(intent);
			}
		});
//		printRel.setOnClickListener(new OnClickListener() 
//		{
//			
//			@Override
//			public void onClick(View v) 
//			{
//					Intent intent=new Intent(getActivity(),ShopSearchBlueToothActivity.class);
//					startActivity(intent);
//			}
//		});
		helpRel.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				if(getActivity()!=null)
				{
				Intent intent=new Intent(getActivity(),WebPageActivity.class);
				intent.putExtra("action", "help");
				startActivity(intent);
				}
			}
		});
		aboutRel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(getActivity()!=null)
				{
				Intent intent=new Intent(getActivity(),WebPageActivity.class);
				intent.putExtra("action", "about");
				startActivity(intent);
				}
			}
		});
		
		qcodeImgRel=(RelativeLayout) MineFragmentView.findViewById(R.id.qcodeImgRel);
		relupdate=(RelativeLayout) MineFragmentView.findViewById(R.id.relupdate);
		shopeNameTextView=(TextView) MineFragmentView.findViewById(R.id.shopeNameTextView);
		//fangkeNumText=(TextView) MineFragmentView.findViewById(R.id.fangkeNumText);
		buyerNumText=(TextView) MineFragmentView.findViewById(R.id.buyerNumText);
		//orderNumText=(TextView) MineFragmentView.findViewById(R.id.orderNumText);
		earnMoneyTextView=(TextView) MineFragmentView.findViewById(R.id.earnMoneyTextView);
		getCashMoneyTextView=(TextView) MineFragmentView.findViewById(R.id.getCashMoneyTextView);
		mineText=(TextView) MineFragmentView.findViewById(R.id.mineText);
		mineImageHead=(ImageView) MineFragmentView.findViewById(R.id.mineImageHead);
		mineScrollview=(OverScrollView) MineFragmentView.findViewById(R.id.mineScrollview);
		qcodeImgRel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),WXEntryActivity.class);
				intent.putExtra("type", ConstantParamPhone.GET_CASHIER_CODE);
				startActivity(intent);
			}
		});
		relupdate.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				if(getActivity()!=null)
				{
					UmengUpdateAgent.setUpdateAutoPopup(false);
					UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
					    @Override
					    public void onUpdateReturned(int updateStatus,UpdateResponse updateInfo) {
					        switch (updateStatus) {
					        case UpdateStatus.Yes: // has update
					            UmengUpdateAgent.showUpdateDialog(getActivity(), updateInfo);
					            break;
					        case UpdateStatus.No: // has no update
					        	if(getActivity()!=null)
					        	{
					        		Toast.makeText(getActivity(), "没有更新", Toast.LENGTH_SHORT).show();
					        	}
					            break;
	//				        case UpdateStatus.NoneWifi: // none wifi
	//				            Toast.makeText(mContext, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
	//				            break;
					        case UpdateStatus.Timeout: // time out
					            Toast.makeText(getActivity(), "超时", Toast.LENGTH_SHORT).show();
					            break;
					        }
					    }
	
					  });
					  UmengUpdateAgent.update(getActivity());
				    }
				}
		    });
//		mineScrollview.setOnTouchListener(new OnTouchListener() 
//		{
//			
//			@Override
//			public boolean onTouch(View v, MotionEvent event) 
//			{
//				// TODO Auto-generated method stub
//				if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			        // 可以监听到ScrollView的滚动事件
//			        //System.out.println(mineScrollview.getScrollY());
//			        LayoutParams lp;        
//					lp=mineImageHead.getLayoutParams();
//					lp.height=Util.dip2px(getActivity(),100)-mineScrollview.getScrollY();          
//					mineImageHead.setLayoutParams(lp);
//					mineText.setAlpha((float) (mineScrollview.getScrollY()*0.008));
//			    }
//			    return false;
//			}
//		});
		//滚动停止与滚动距离
		mineScrollview.setOverScrollTinyListener(new OverScrollTinyListener() 
		{
			Boolean isDown=false;
			//恢复
			@Override
			public void scrollLoosen() 
			{
				// TODO Auto-generated method stub
//				if(isDown)
//				{
//					LayoutParams lp;        
//			        lp=mineImageHead.getLayoutParams();
//			        lp.height=Util.dip2px(getActivity(),100);    
//			        mineImageHead.setLayoutParams(lp);
//				}
				//下拉加载数据
				//if(share.getString("usertype", "").equals("dianzhang"))
				//{
					initData();
				//}
			}
			
			@Override
			public void scrollDistance(int tinyDistance, int totalDistance) 
			{
				// TODO Auto-generated method stub
				//Log.i("info",String.valueOf(tinyDistance));  //微量变化
				//Log.i("info2",String.valueOf(totalDistance)); //上拉正数，递增下拉负数递增
//				if(totalDistance<0)
//				{
//					isDown=true;
//					LayoutParams lp1;        
//					lp1=mineImageHead.getLayoutParams();
//					lp1.height=Util.dip2px(getActivity(),100)-totalDistance;     
//					mineImageHead.setLayoutParams(lp1);
//				}
//				else
//				{
//					isDown=false;
//				}
				
			}
		});
		bankCardRel=(RelativeLayout) MineFragmentView.findViewById(R.id.bankCardRel);
//		CashRel=(RelativeLayout) MineFragmentView.findViewById(R.id.CashRel);
//		CashRel.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent=new Intent(getActivity(),GetCashActivity.class);
//				startActivity(intent);
//			}
//		});
		bankCardRel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				if(TextUtils.isEmpty(share.getString("mobile", "")))
				{
					MyDialog.showDialog(getActivity(), "尚未绑定手机号，是否绑定手机号", true, true, "确定", "取消", new OnClickListener() 
			    	{
						
						@Override
						public void onClick(View arg0) 
						{
							// TODO Auto-generated method stub
							Intent intent=new Intent(getActivity(),LoginBindPhoneActivity.class);
							startActivity(intent);
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					});
				}
				else
				{
					Intent intent=new Intent(getActivity(),ShopBankListActivity.class);
					intent.putExtra("action", "banklist");
					startActivity(intent);
				}
			}
		});
//		accountDataRel=(RelativeLayout) MineFragmentView.findViewById(R.id.accountDataRel);
//		accountDataRel.setOnClickListener(new OnClickListener() 
//		{
//			
//			@Override
//			public void onClick(View v) 
//			{
//				// TODO Auto-generated method stub
//				Intent intent=new Intent(getActivity(), ShopAccountDataActivity.class);
//				startActivity(intent);
//			}
//		});
		if(!share.getString("usertype", "").equals("dianzhang"))
		{
			//lin1.setVisibility(View.INVISIBLE);
			//rel2.setVisibility(View.GONE);
			//CashRel.setVisibility(View.GONE);
			bankCardRel.setVisibility(View.GONE);
			//memberDataRel.setVisibility(View.GONE);
		  
		}
		
		
	}
//	private BroadcastReceiver receiver=new BroadcastReceiver() 
//	{
//		@Override
//		public void onReceive(Context context, Intent intent) 
//		{
//			// TODO Auto-generated method stub
//			String action = intent.getAction(); 
//			if(BluetoothAdapter.ACTION_STATE_CHANGED.equals(action))
//			{
//				if (bluetoothAdapter.getState() == BluetoothAdapter.STATE_ON)
//				{
//					//Toast.makeText(OrderDetailActivity.this,"打开成功", Toast.LENGTH_SHORT).show();
//					ColorDialog.dissmissProcessDialog();
//					Intent intent1=new Intent(getActivity(),SearchBlueToothActivity.class);
//					startActivity(intent1);
//				}
//			}
//		}
//	};
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == 1) 
		{
			if (resultCode == getActivity().RESULT_OK) 
			{
				
			} 
			else if (resultCode == getActivity().RESULT_CANCELED) 
			{
				ColorDialog.dissmissProcessDialog();
			}
		}
	}
}
