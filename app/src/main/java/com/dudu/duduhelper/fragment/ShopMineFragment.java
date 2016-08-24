package com.dudu.duduhelper.fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.LoginActivity;
import com.dudu.duduhelper.LoginBindPhoneActivity;
import com.dudu.duduhelper.MainActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.ShopBankListActivity;
import com.dudu.duduhelper.ShopSettingActivity;
import com.dudu.duduhelper.WebPageActivity;
import com.dudu.duduhelper.bean.UserBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import org.apache.http.Header;


public class ShopMineFragment extends Fragment
{
	private OverScrollView mineScrollview;
	private ImageView mineImageHead;
	private RelativeLayout mineheadRelLine;
	private RelativeLayout relupdate;
	private RelativeLayout bankCardRel;
	private RelativeLayout helpRel;
	private RelativeLayout aboutRel;
	private View MineFragmentView;
	private TextView tv_frozen_num_mine;
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
	private SharedPreferences sp;
	
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		MineFragmentView= inflater.inflate(R.layout.shop_fragment_mine, null);
		return MineFragmentView;
	}
	@Override
	//当绑定的activity创建的时候
	public void onActivityCreated(@Nullable Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		if(getActivity()!=null)
		{
			//获取本地的个人信息
			sp = ((MainActivity) getActivity()).sp;
		}
		//UIL的设置
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
		if("dianzhang".equals(share.getString("usertype", "")))
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
	        //请求网络成功后？如果
			@Override
			public void onFinish() 
			{

			}
		});
	}
	//初始化页面显示
	private void initFragemntView() 
	{
		getCashButtonRel=(RelativeLayout) MineFragmentView.findViewById(R.id.getCashButtonRel);
		lin1=(LinearLayout) MineFragmentView.findViewById(R.id.lin1);
		rel2=(RelativeLayout) MineFragmentView.findViewById(R.id.rel2);
		mineheadRelLine=(RelativeLayout) MineFragmentView.findViewById(R.id.mineheadRelLine);
		helpRel=(RelativeLayout) MineFragmentView.findViewById(R.id.helpRel);
		aboutRel=(RelativeLayout) MineFragmentView.findViewById(R.id.aboutRel);
		//提现按钮
		getCashButtonRel.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				Intent intent=new Intent(getActivity(),ShopBankListActivity.class);
				intent.putExtra("action", "tixian");
				startActivity(intent);
			}
		});
		//个人中心按钮
		mineheadRelLine.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				Intent intent=new Intent(getActivity(),ShopSettingActivity.class);
				startActivity(intent);
			}
		});
		//帮助中心按钮
		helpRel.setOnClickListener(new OnClickListener()
		{
			
			@Override
			public void onClick(View arg0) 
			{
				if(getActivity()!=null)
				{
				Intent intent=new Intent(getActivity(),WebPageActivity.class);
				intent.putExtra("action", "help");
				startActivity(intent);
				}
			}
		});
		//关于我们按钮
		aboutRel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
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
		tv_frozen_num_mine=(TextView) MineFragmentView.findViewById(R.id.tv_frozen_num_mine);
		//orderNumText=(TextView) MineFragmentView.findViewById(R.id.orderNumText);
		earnMoneyTextView=(TextView) MineFragmentView.findViewById(R.id.earnMoneyTextView);
		getCashMoneyTextView=(TextView) MineFragmentView.findViewById(R.id.getCashMoneyTextView);
		mineText=(TextView) MineFragmentView.findViewById(R.id.mineText);
		mineImageHead=(ImageView) MineFragmentView.findViewById(R.id.mineImageHead);
		mineScrollview=(OverScrollView) MineFragmentView.findViewById(R.id.mineScrollview);
		//初始化个人信息，通过三级缓存？不需要，登陆以后自动保存信息，所以sp必然有数据
		if(getActivity()!=null)
		{
			shopeNameTextView.setText(sp.getString("shopName", ""));
			//防止imageLoader闪动
			ImageAware imageAware = new ImageViewAware(mineImageHead, false);
			//动态显示图片
			imageLoader.displayImage(sp.getString("shopLogo", ""), imageAware);
			//冻结金额
			tv_frozen_num_mine.setText(sp.getString("frozenMoney", ""));
			//显示今日收入
			earnMoneyTextView.setText(sp.getString("todayIncome", "")+"↑");
			//可提现金额
			getCashMoneyTextView.setText(sp.getString("useableMoney", ""));
		}

		qcodeImgRel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
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

		mineScrollview.setOverScrollTinyListener(new OverScrollTinyListener() 
		{
			Boolean isDown=false;
			//恢复
			@Override
			public void scrollLoosen() 
			{

					initData();
			}
			
			@Override
			public void scrollDistance(int tinyDistance, int totalDistance) 
			{

				
			}
		});
		bankCardRel=(RelativeLayout) MineFragmentView.findViewById(R.id.bankCardRel);

		bankCardRel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				if(TextUtils.isEmpty(share.getString("mobile", "")))
				{
					MyDialog.showDialog(getActivity(), "尚未绑定手机号，是否绑定手机号", true, true, "确定", "取消", new OnClickListener() 
			    	{
						
						@Override
						public void onClick(View arg0) 
						{
							Intent intent=new Intent(getActivity(),LoginBindPhoneActivity.class);
							startActivity(intent);
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View arg0) {
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

		if(!("dianzhang".equals(share.getString("usertype", ""))))
		{
			bankCardRel.setVisibility(View.GONE);

		}
		
		
	}

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
