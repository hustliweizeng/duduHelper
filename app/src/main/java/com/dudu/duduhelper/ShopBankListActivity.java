package com.dudu.duduhelper;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.dudu.duduhelper.adapter.BankListAdapter;
import com.dudu.duduhelper.bean.MemberBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.dudu.duduhelper.widget.WaveHelper;
import com.dudu.duduhelper.widget.WaveView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

public class ShopBankListActivity extends BaseActivity 
{
	
	private SwipeRefreshLayout memberListswipeLayout;
	private ListView memberList;
	private Button reloadButton;
	private ProgressBar loading_progressBar;
    private TextView loading_text;
    private View footView;
    private BankListAdapter memberAdapter;
    private Button editButton;
    private String title="";
    private String action = "";
    private PopupWindow popupWindow;
    private LinearLayout backgroundLinearlayout;
    
  //波浪助手
  	private WaveHelper mWaveHelper;
  	private WaveView waveView;
  	private int image;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_bank_list);
		//判断页面来源
		action = getIntent().getStringExtra("action");
		if(!TextUtils.isEmpty(action))
		{
	    	if(action.equals("tixian"))
	    	{
	    		title = "提现";
	    		image = R.drawable.icon_qiehuan;
	    	}
	    	else
	    	{
	    		title = "我的银行卡";
	    		image = R.drawable.icon_tianjia;
	    	}
		}
		else
		{
			finish();
		}
		initHeadView(title, true, true, image);
		initView();
		ColorDialog.showRoundProcessDialog(ShopBankListActivity.this,R.layout.loading_process_dialog_color);
		initData();
	}
	
	@Override
	public void RightButtonClick() 
	{
		Intent intent=new Intent(ShopBankListActivity.this,ShopUserBankInfoEditActivity.class);
		startActivity(intent);
	}

	@SuppressLint("ResourceAsColor") 
	@SuppressWarnings("deprecation")
	private void initView() 
	{
		memberAdapter=new BankListAdapter(this);
		backgroundLinearlayout = (LinearLayout) this.findViewById(R.id.backgroundLinearlayout);
		memberListswipeLayout=(SwipeRefreshLayout) this.findViewById(R.id.memberListswipeLayout);
		//下拉刷新事件
		memberListswipeLayout.setOnRefreshListener(new OnRefreshListener() 
		{
			
			@Override
			public void onRefresh() 
			{
				// TODO Auto-generated method stub
				memberAdapter.clear();
				initData();
			}
		});
		memberListswipeLayout.setColorSchemeResources(R.color.text_color);
		memberListswipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		memberListswipeLayout.setProgressBackgroundColor(R.color.bg_color);
		memberList=(ListView) this.findViewById(R.id.memberList);
		memberList.setAdapter(memberAdapter);
		reloadButton=(Button) this.findViewById(R.id.reloadButton);
		reloadButton=(Button) this.findViewById(R.id.reloadButton);
		footView = LayoutInflater.from(this).inflate(R.layout.activity_listview_foot, null);
		loading_progressBar=(ProgressBar) footView.findViewById(R.id.loading_progressBar);
		loading_text=(TextView) footView.findViewById(R.id.loading_text);
		// TODO Auto-generated method stub
		reloadButton=(Button) this.findViewById(R.id.reloadButton);
		//数据重载按钮
		reloadButton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				initData();
			}
		});
		memberList.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				if(action.equals("tixian"))
				{
					
					//弹出提现列表框
					getCashWindow();
				}
				else
				{
					Intent intent=new Intent(ShopBankListActivity.this,ShopUserBankInfoActivity.class);
					startActivityForResult(intent, 1);
				}
			}
		});
	}
	
	private void initData() 
	{
		// TODO Auto-generated method stub
		loading_progressBar.setVisibility(View.VISIBLE);
		loading_text.setText("加载中...");
		RequestParams params = new RequestParams();
		params.add("token", this.share.getString("token", ""));
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(ShopBankListActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.GET_MEMBER_LIST, params,new TextHttpResponseHandler()
		{

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				reloadButton.setVisibility(View.VISIBLE);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				MemberBean getCouponSellBean=new Gson().fromJson(arg2,MemberBean.class);
				if(!getCouponSellBean.getStatus().equals("1"))
				{
					MyDialog.showDialog(ShopBankListActivity.this, "该账号已在其他手机登录，是否重新登录", false, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(ShopBankListActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				else
				{
					if(getCouponSellBean.getData()!=null&&getCouponSellBean.getData().size()!=0)
					{
						memberAdapter.addAll(getCouponSellBean.getData());
						reloadButton.setVisibility(View.GONE);
						loading_progressBar.setVisibility(View.GONE);
						loading_text.setText("加载完啦！");
						
					}
					else
					{
						loading_progressBar.setVisibility(View.GONE);
						loading_text.setText("啥也没有！");
						reloadButton.setVisibility(View.VISIBLE);
					}
				}
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				memberListswipeLayout.setRefreshing(false);
				ColorDialog.dissmissProcessDialog();
			}
		});
	}
	private void getCashWindow()
	{
		
		AlphaAnimation animation = new AlphaAnimation((float)0, (float)1.0);
		animation.setDuration(500); //设置持续时间5秒
		backgroundLinearlayout.startAnimation(animation);
		backgroundLinearlayout.setVisibility(View.VISIBLE);
		
		LayoutInflater layoutInflater = (LayoutInflater)ShopBankListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        View view = layoutInflater.inflate(R.layout.shop_getcash_popwindow, null);  
        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,  LayoutParams.WRAP_CONTENT);  
        //设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        popupWindow.setFocusable(true);  
        popupWindow.setOutsideTouchable(true);  
        //这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景  
        //设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAtLocation(ShopBankListActivity.this.findViewById(R.id.head),Gravity.BOTTOM, 0, 0);
        
        waveView = (WaveView)view.findViewById(R.id.wave);
		//waveView.setBorder(10, Color.parseColor("#44FFFFFF"));
		mWaveHelper = new WaveHelper(waveView,2500,0.6f);
		waveView.setShapeType(WaveView.ShapeType.SQUARE);
		waveView.setWaveColor(Color.parseColor("#28ffffff"),Color.parseColor("#14ffffff"));
		mWaveHelper.start();
        
        ImageView closeImageButton=(ImageView) view.findViewById(R.id.closeImageButton);
        closeImageButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{
				// TODO Auto-generated method stub
				mWaveHelper.cancel();
				popupWindow.dismiss();
				AlphaAnimation animation = new AlphaAnimation((float)1, (float)0);
				animation.setDuration(500); //设置持续时间5秒
				animation.setAnimationListener(new AnimationListener() 
				{
					
					@Override
					public void onAnimationStart(Animation animation) 
					{
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) 
					{
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) 
					{
						// TODO Auto-generated method stub
						backgroundLinearlayout.setVisibility(View.GONE);
					}
				});
				backgroundLinearlayout.startAnimation(animation);
			}
		});
      //popWindow消失监听方法
        popupWindow.setOnDismissListener(new OnDismissListener() {
          
          @Override
          public void onDismiss() 
          {
        	  
          }
        });
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		// TODO Auto-generated method stub
		//清空adapter数据
		memberAdapter.clear();
		//loadMoreView.setVisibility(View.GONE);
		ColorDialog.showRoundProcessDialog(this,R.layout.loading_process_dialog_color);
		initData();
	}

}
