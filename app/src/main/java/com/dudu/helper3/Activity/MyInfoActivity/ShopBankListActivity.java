package com.dudu.helper3.Activity.MyInfoActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.helper3.Activity.WelcomeActivity.LoginActivity;
import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.adapter.BankListAdapter;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.http.HttpUtils;
import com.dudu.helper3.javabean.BankCardListBean;
import com.dudu.helper3.widget.ColorDialog;
import com.dudu.helper3.widget.WaveHelper;
import com.dudu.helper3.widget.WaveView;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

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
	private int pagnum = 1;//初始化时加载的页面
    
  //波浪助手
  	private WaveHelper mWaveHelper;
  	private WaveView waveView;
  	private int image;
	private Button btnGetmess;
	private String bankCardNo;
	private EditText getCodeedit;
	private EditText getcashmoneyedit;
	private BankCardListBean bean1;
	private Button loginbutton;
	private ImageButton backButton;

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
	    		image = R.drawable.icon_historical;
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
		if (savedInstanceState!=null){
			//重新获取保存的信息
			bean1 = (BankCardListBean) savedInstanceState.getSerializable("data");
			memberAdapter.addAll(bean1.getData());
		}else {
			//第一次进入加载数据
			initData();
		}
	}
	
	@Override
	//新增银行卡页面
	public void RightButtonClick() 
	{	//进入提现记录
		if (action.equalsIgnoreCase("tixian")){
			startActivity(new Intent(context,GetCashHistoryListActivity.class));

		}else {
			//进入新增银行卡页面
			Intent intent=new Intent(ShopBankListActivity.this,ShopUserBankInfoEditActivity.class);
			//不需要传递银行卡信息过去
			startActivity(intent);

		}
	}

	@SuppressLint("ResourceAsColor") 
	@SuppressWarnings("deprecation")
	private void initView() 
	{
		memberAdapter=new BankListAdapter(this);
		backgroundLinearlayout = (LinearLayout) this.findViewById(R.id.backgroundLinearlayout);
		memberListswipeLayout=(SwipeRefreshLayout) this.findViewById(R.id.memberListswipeLayout);
		backButton = (ImageButton) findViewById(R.id.backButton);
		//下拉刷新事件
		memberListswipeLayout.setOnRefreshListener(new OnRefreshListener() 
		{
			
			@Override
			public void onRefresh() 
			{
				memberAdapter.clear();
				initData();
			}
		});
		//下拉刷设置
		memberListswipeLayout.setColorSchemeResources(R.color.text_color);
		memberListswipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		memberListswipeLayout.setProgressBackgroundColor(R.color.bg_color);

		memberList=(ListView) this.findViewById(R.id.memberList);
		//设置适配器，显示银行卡信息列表
		memberList.setAdapter(memberAdapter);
		reloadButton=(Button) this.findViewById(R.id.reloadButton);
		reloadButton=(Button) this.findViewById(R.id.reloadButton);
		footView = LayoutInflater.from(this).inflate(R.layout.activity_listview_foot, null);
		loading_progressBar=(ProgressBar) footView.findViewById(R.id.loading_progressBar);
		loading_text=(TextView) footView.findViewById(R.id.loading_text);
		reloadButton=(Button) this.findViewById(R.id.reloadButton);
		//数据重载按钮
		reloadButton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
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
					//获取当前条目的银行卡号
					bankCardNo = memberAdapter.getCardNo(position);
				}
				else
				{
					//进入银行卡详细信息条目，修改完成后，需要返回当前列表
					Intent intent=new Intent(ShopBankListActivity.this,ShopUserBankInfoActivity.class);
					Bundle bundle = new Bundle();
					intent.putExtra("cardInfo",bean1.getData().get(position));
					startActivityForResult(intent, 1);
				}
			}
		});
	}
	//请求银行卡列表信息
	private void initData() 
	{
		loading_progressBar.setVisibility(View.VISIBLE);
		loading_text.setText("加载中...");
		RequestParams params = new RequestParams();
        HttpUtils.getConnection(context,params,ConstantParamPhone.GET_BANKCARD_LIST, "GET",new TextHttpResponseHandler()
		{
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				//显示重新加载页面
				reloadButton.setVisibility(View.VISIBLE);
				Toast.makeText(context,"当前没有要显示的数据",Toast.LENGTH_LONG).show();

			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						//Toast.makeText(context,"加载成功",Toast.LENGTH_LONG).show();
						Gson gson = new Gson();
						bean1 = gson.fromJson(arg2, BankCardListBean.class);
						if (bean1.getData()!=null &&bean1.getData().size()>0){
							memberAdapter.addAll(bean1.getData());

						}else {
							Toast.makeText(context,"您还没有绑定银行卡信息，请先绑定银行卡！",Toast.LENGTH_SHORT).show();
						}
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
						startActivity(new Intent(context, LoginActivity.class));
						finish();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFinish() 
			{
				memberListswipeLayout.setRefreshing(false);
				loading_progressBar.setVisibility(View.GONE);
			}
		});
	}

	//当前activity结束时保存状态
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//把当前的数据保存
		outState.putSerializable("data",bean1);
	}

	//显示下来提现窗口
	private void getCashWindow()
	{
		//屏蔽标题栏的按钮
		initHeadView("提现", false, false, 0);
		//设置动画效果
		AlphaAnimation animation = new AlphaAnimation((float)0, (float)1.0);
		animation.setDuration(500); //设置持续时间0.5秒
		backgroundLinearlayout.startAnimation(animation);
		//背景颜色可见
		backgroundLinearlayout.setVisibility(View.VISIBLE);
		
		LayoutInflater layoutInflater = (LayoutInflater)ShopBankListActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
		//载入poupowindow的页面
        View view = layoutInflater.inflate(R.layout.shop_getcash_popwindow, null);
		//验证码
		getCodeedit = (EditText) view.findViewById(R.id.getCodeedit);
		//提现的金额
		getcashmoneyedit = (EditText) view.findViewById(R.id.getcashmoneyedit);
		getcashmoneyedit.setHint(sp.getString("useableMoney", ""));//设置可体现金额
		//确认提现按钮
		loginbutton = (Button) view.findViewById(R.id.loginbutton);
		btnGetmess = (Button) view.findViewById(R.id.btnGetmess);
		//设置获取验证码的监听
		btnGetmess.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {

				RequestParams params = new RequestParams();
				params.add("mobile",sp.getString("mobile",""));
				params.add("type","outmoney");
				HttpUtils.getConnection(context, params, ConstantParamPhone.GET_SMS_CONFIRM, "GET", new TextHttpResponseHandler() {
					@Override
					public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
						Toast.makeText(context,"服务器错误，请稍后再试",Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(int i, Header[] headers, String s) {
						showResidueSeconds();
					}
				});
			}
		});
		//确认提现按钮
		loginbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TextUtils.isEmpty(getCodeedit.getText().toString()) ||TextUtils.isEmpty(getcashmoneyedit.getText().toString())){
					Toast.makeText(context,"信息填写不完整",Toast.LENGTH_SHORT).show();
					return;
				}
				RequestParams params = new RequestParams();
				params.add("bank_card_id",bankCardNo);
				params.add("money", getcashmoneyedit.getText().toString().trim());
				params.add("code",getCodeedit.getText().toString().trim());
				HttpUtils.getConnection(context, params, ConstantParamPhone.CREATE_GET_MONEY, "post", new TextHttpResponseHandler() {
					@Override
					public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
						Toast.makeText(context,"服务器错误，请稍后再试",Toast.LENGTH_LONG).show();
						//请求网络数据
					}

					@Override
					public void onSuccess(int i, Header[] headers, String s) {
						try {
							String info = new JSONObject(s).getString("code");
							if ("SUCCESS".equalsIgnoreCase(info)){
								//显示倒计时
								Toast.makeText(context,"提现成功",Toast.LENGTH_LONG).show();
								finish();
							}else {
								Toast.makeText(context,new JSONObject(s).getString("msg"),Toast.LENGTH_SHORT).show();
								return;
							}

						}catch (Exception ex){

						}
					}
				});
			}
		});

        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,  LayoutParams.WRAP_CONTENT);  
        //设置popWindow弹出窗体可点击，这句话必须添加，并且是true
        popupWindow.setFocusable(true);  
        popupWindow.setOutsideTouchable(true);  
        //这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景  
        //设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
		//显示的位置
        popupWindow.showAtLocation(ShopBankListActivity.this.findViewById(R.id.head),Gravity.BOTTOM, 0, 0);
        //波浪
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
				initHeadView("提现", true, true, image);
				LogUtil.d("tag","title");
				mWaveHelper.cancel();
				popupWindow.dismiss();
				//屏蔽标题栏的按钮
				AlphaAnimation animation = new AlphaAnimation((float)1, (float)0);
				animation.setDuration(500); //设置持续时间5秒
				animation.setAnimationListener(new AnimationListener() 
				{
					@Override
					public void onAnimationStart(Animation animation) 
					{
					}
					@Override
					public void onAnimationRepeat(Animation animation) 
					{
					}
					@Override
					public void onAnimationEnd(Animation animation) 
					{
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
			  initHeadView("提现",true,true,image);
			  backButton.setVisibility(View.VISIBLE);
			  LogUtil.d("tag","title");
          }
        });
	}

	@Override
	//当开启的activity结束以后，重新加载数据
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		//清空adapter数据，重新载入数据
		memberAdapter.clear();
		initData();
	}
	/**
	 * 显示剩余秒数
	 */
	private void showResidueSeconds() {
		//显示倒计时按钮
		new CountDownTimer(60*1000,1000){

			@Override
			public void onTick(long lastTime) {
				//倒计时执行的方法
				btnGetmess.setClickable(false);
				btnGetmess.setFocusable(false);
				btnGetmess.setText(lastTime/1000+"秒后重发");
				btnGetmess.setTextColor(getResources().getColor(R.color.text_hint_color));
				btnGetmess.setBackgroundResource(R.drawable.btn_bg_hint);
				// LogUtil.d("lasttime","剩余时间:"+lastTime/1000);
			}

			@Override
			public void onFinish() {
				btnGetmess.setClickable(true);
				btnGetmess.setFocusable(true);
				btnGetmess.setText("重新获取");

			}
		}.start();
	}

}
