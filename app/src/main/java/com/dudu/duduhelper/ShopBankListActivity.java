package com.dudu.duduhelper;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.adapter.BankListAdapter;
import com.dudu.duduhelper.bean.MemberDataBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.WaveHelper;
import com.dudu.duduhelper.widget.WaveView;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

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
	private Button btnGetmess;
	private String bankCardNo;
	private EditText getCodeedit;
	private EditText getcashmoneyedit;

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
				memberAdapter.clear();
				initData();
			}
		});
		//下拉刷设置
		memberListswipeLayout.setColorSchemeResources(R.color.text_color);
		memberListswipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		memberListswipeLayout.setProgressBackgroundColor(R.color.bg_color);

		memberList=(ListView) this.findViewById(R.id.memberList);
		//设置适配器
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
					//进入银行卡条目
					Intent intent=new Intent(ShopBankListActivity.this,ShopUserBankInfoActivity.class);
					startActivityForResult(intent, 1);
				}
			}
		});
	}
	//请求数据
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
				//reloadButton.setVisibility(View.VISIBLE);
				Toast.makeText(context,"记载数据失败",Toast.LENGTH_LONG).show();
				showBankCardList();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				showBankCardList();


			}
			@Override
			public void onFinish() 
			{
				memberListswipeLayout.setRefreshing(false);
				ColorDialog.dissmissProcessDialog();
			}
		});
	}

	private void showBankCardList() {
		//获取数据，传递给适配器adapter
		//伪造数据
		List<MemberDataBean> list = new ArrayList<MemberDataBean>();
		MemberDataBean bean1 = new MemberDataBean();
		bean1.setNickname("小米");
		MemberDataBean bean2 = new MemberDataBean();
		bean1.setNickname("魅族");
		MemberDataBean bean3 = new MemberDataBean();
		bean1.setNickname("华为");
		list.add(bean1);
		list.add(bean2);
		list.add(bean3);
		memberAdapter.addAll(list);
	}
	//显示下来提现窗口
	private void getCashWindow()
	{
		//设置动画效果
		AlphaAnimation animation = new AlphaAnimation((float)0, (float)1.0);
		animation.setDuration(500); //设置持续时间0.5秒
		backgroundLinearlayout.startAnimation(animation);
		//背景颜色可见
		backgroundLinearlayout.setVisibility(View.VISIBLE);
		
		LayoutInflater layoutInflater = (LayoutInflater)ShopBankListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		//载入poupowindow的页面
        View view = layoutInflater.inflate(R.layout.shop_getcash_popwindow, null);
		//验证码
		getCodeedit = (EditText) view.findViewById(R.id.getCodeedit);
		//提现的金额
		getcashmoneyedit = (EditText) findViewById(R.id.getcashmoneyedit);
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
						Toast.makeText(context,"发送失败",Toast.LENGTH_LONG).show();
						//请求网络数据
						showResidueSeconds();
					}

					@Override
					public void onSuccess(int i, Header[] headers, String s) {
						//请求网络数据
						showResidueSeconds();
					}
				});
			}
		});
		//确认提现按钮
		Button loginbutton = (Button) view.findViewById(R.id.loginbutton);
		loginbutton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				RequestParams params = new RequestParams();
				params.add("bank_card_id",bankCardNo);
				params.add("money", getcashmoneyedit.getText().toString().trim());
				params.add("code",getCodeedit.getText().toString().trim());
				HttpUtils.getConnection(context, params, ConstantParamPhone.GET_SMS_CONFIRM, "post", new TextHttpResponseHandler() {
					@Override
					public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
						Toast.makeText(context,"发送失败",Toast.LENGTH_LONG).show();
						//请求网络数据
						showResidueSeconds();
					}

					@Override
					public void onSuccess(int i, Header[] headers, String s) {
						//请求网络数据
						showResidueSeconds();
						Toast.makeText(context,"提醒成功",Toast.LENGTH_LONG).show();
						finish();
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
        	  
          }
        });
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		//清空adapter数据
		memberAdapter.clear();
		//loadMoreView.setVisibility(View.GONE);
		ColorDialog.showRoundProcessDialog(this,R.layout.loading_process_dialog_color);
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
