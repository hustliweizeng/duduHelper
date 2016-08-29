package com.dudu.duduhelper;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.adapter.BankAreAdapter;
import com.dudu.duduhelper.adapter.ProductAdapter;
import com.dudu.duduhelper.adapter.ShopOrderAdapter;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.bean.OrderBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.ProvinceListBean.DataBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

public class ShopOrderActivity extends BaseActivity 
{
	private TextView orderType;
	private ImageView orderTypeArror;
	private ImageView statusTypeArror;
	private TextView productAction;
	private TextView statusAction;
	private ImageView productTypeArror;
	private RelativeLayout orderTypeRel;
	private RelativeLayout productRel;
	private RelativeLayout actionRel;
	private LinearLayout selectLine;
	private ListView allOrderListView;
	private ShopOrderAdapter orderAdapter;
	private int page=1;
	private SwipeRefreshLayout orderallswipeLayout;
	private Button reloadButton;
	private int lastItemIndex;
	private PopupWindow popupWindow;
    //判断是上拉还是下拉
    private int reftype;
    //判断数据是否加载完成
    private boolean reffinish=false;
    private View footView;
    private ProgressBar loading_progressBar;
    private TextView loading_text;
    private BankAreAdapter bankAreAdapter;
    public ProductAdapter productAdapter;
    private String status="";
    private String from="0";
    private String isnew="0";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_order);
		orderAdapter=new ShopOrderAdapter(this);
		initHeadView("订单管理", true, true, R.drawable.icon_sousuo);
		DuduHelperApplication.getInstance().addActivity(this);
		initViewFragment();
		initData();
	}
	public void setRefreshing()
	{
		orderallswipeLayout.setProgressViewOffset(false, 0, Util.dip2px(this, 50));
		orderallswipeLayout.setRefreshing(true);
		page=1;
		reftype=1;
		orderAdapter.clear();
		initData();
	}

	private void initData() 
	{

		loading_progressBar.setVisibility(View.VISIBLE);
		loading_text.setText("加载中...");
		allOrderListView.setAdapter(orderAdapter);
		/*"moduleid" => "模块ID,多个模块用逗号分隔"
		"status" => "订单状态"
		"date" => "指定日期,用于新订单和收款列表"
		"lastid" => "分页标识,上一页最后一个订单id"
		"limit" => "每页多少条"
		"ispay" => "是否支付"*/
		RequestParams params = new RequestParams();
		params.add("page",String.valueOf(page));
		params.add("pagesize","10");
		params.add("status",status);
		params.add("from",from);
		params.add("ispay", isnew);
		
		HttpUtils.getConnection(context,params,ConstantParamPhone.GET_ORDER_LIST, "GET",new TextHttpResponseHandler()
		{

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				//Toast.makeText(getActivity(), "网络不给力呀", Toast.LENGTH_SHORT).show();
				if(page==1)
				{
					reloadButton.setVisibility(View.VISIBLE);
				}
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				OrderBean orderBean=new Gson().fromJson(arg2,OrderBean.class);
				if(!orderBean.getStatus().equals("1"))
				{
					MyDialog.showDialog(ShopOrderActivity.this, "该账号已在其他手机登录，是否重新登录", false, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(ShopOrderActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				else
				{
					if(orderBean.getData()!=null&&orderBean.getData().size()!=0)
					{
						orderAdapter.addAll(orderBean.getData());
						reloadButton.setVisibility(View.GONE);
						
						if(page==1&&orderBean.getData().size()<10)
						{
							loading_progressBar.setVisibility(View.GONE);
							loading_text.setText("加载完啦！");
						}
						
					}
					else
					{
						if(page==1)
						{
							//Toast.makeText(getActivity(), "啥也没有！", Toast.LENGTH_SHORT).show();
							loading_progressBar.setVisibility(View.GONE);
							loading_text.setText("啥也没有！");
							reloadButton.setVisibility(View.VISIBLE);
						}
						else
						{
							Toast.makeText(ShopOrderActivity.this, "加载完啦", Toast.LENGTH_SHORT).show();
							loading_progressBar.setVisibility(View.GONE);
							loading_text.setText("加载完啦！");
							reffinish=true;
						}
					}
				}
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				orderallswipeLayout.setRefreshing(false);
				ColorDialog.dissmissProcessDialog();
			}
		});
	}

	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd("OrderFragment");
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		MobclickAgent.onPageStart("OrderFragment");
	}

	@SuppressLint("ResourceAsColor") 
	private void initViewFragment() {
		// TODO Auto-generated method stub
		statusAction=(TextView) ShopOrderActivity.this.findViewById(R.id.statusAction);
		actionRel=(RelativeLayout) ShopOrderActivity.this.findViewById(R.id.actionRel);
		statusTypeArror=(ImageView) ShopOrderActivity.this.findViewById(R.id.statusTypeArror);
		orderType = (TextView) ShopOrderActivity.this.findViewById(R.id.orderType);
		orderTypeArror = (ImageView) ShopOrderActivity.this.findViewById(R.id.orderTypeArror);
		productAction = (TextView) ShopOrderActivity.this.findViewById(R.id.productAction);
		productTypeArror = (ImageView) ShopOrderActivity.this.findViewById(R.id.productTypeArror);
		selectLine = (LinearLayout) ShopOrderActivity.this.findViewById(R.id.selectLine);
		productRel = (RelativeLayout) ShopOrderActivity.this.findViewById(R.id.productRel);
		orderTypeRel = (RelativeLayout) ShopOrderActivity.this.findViewById(R.id.orderTypeRel);
		
		//弹出订单分类选择事件
		orderTypeRel.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				orderType.setTextColor(getResources().getColor(R.color.text_color));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_shang);
				statusAction.setTextColor(getResources().getColor(R.color.text_color_gray));
				statusTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				productAction.setTextColor(getResources().getColor(R.color.text_color_gray));
				productTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				showSelectPopupWindow("order");
			}
		});
		//弹出分类选择事件
		productRel.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				productAction.setTextColor(getResources().getColor(R.color.text_color));
				productTypeArror.setImageResource(R.drawable.icon_jiantou_shang);
				orderType.setTextColor(getResources().getColor(R.color.text_color_gray));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				statusAction.setTextColor(getResources().getColor(R.color.text_color_gray));
				statusTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				showSelectPopupWindow("product");
				
			}
		});
		//弹出分类选择事件
		actionRel.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				orderType.setTextColor(getResources().getColor(R.color.text_color_gray));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				statusAction.setTextColor(getResources().getColor(R.color.text_color));
				statusTypeArror.setImageResource(R.drawable.icon_jiantou_shang);
				productAction.setTextColor(getResources().getColor(R.color.text_color_gray));
				productTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				showSelectPopupWindow("status");
			}
		});
		
		
		footView = LayoutInflater.from(ShopOrderActivity.this).inflate(R.layout.activity_listview_foot, null);
		loading_progressBar=(ProgressBar) footView.findViewById(R.id.loading_progressBar);
		loading_text=(TextView) footView.findViewById(R.id.loading_text);
		
		// TODO Auto-generated method stub
		reloadButton=(Button) ShopOrderActivity.this.findViewById(R.id.reloadButton);
		//数据重载按钮
		reloadButton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				ColorDialog.showRoundProcessDialog(ShopOrderActivity.this,R.layout.loading_process_dialog_color);
				initData();
			}
		});
		allOrderListView=(ListView) ShopOrderActivity.this.findViewById(R.id.allOrderListView);
		allOrderListView.addFooterView(footView,null,false);
		allOrderListView.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(ShopOrderActivity.this, ShopOrderDetailActivity.class);
				intent.putExtra("no", orderAdapter.getItem(position).getNo());
				intent.putExtra("status", orderAdapter.getItem(position).getStatus());
				startActivityForResult(intent, 1);
			}
		});
		allOrderListView.setOnScrollListener(new OnScrollListener() {
			
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) 
			{
				// TODO Auto-generated method stub
				//
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE&&lastItemIndex == orderAdapter.getCount()) 
				{  
                    //Log.i(TAG, "onScrollStateChanged");  
                    //加载数据代码，此处省略了  
					page++;
					//设置刷新方式
					reftype=2;
					if(!reffinish)
					{
						initData();
					}
					allOrderListView.setSelection(lastItemIndex-1);
					//Toast.makeText(ProductListActivity.this, "加载中",  Toast.LENGTH_SHORT).show();
                }  
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) 
			{
				// TODO Auto-generated method stub
				lastItemIndex = firstVisibleItem + visibleItemCount -1; 
			}
		});
		orderallswipeLayout = (SwipeRefreshLayout)ShopOrderActivity.this.findViewById(R.id.orderallswipeLayout);
		orderallswipeLayout.setColorSchemeResources(R.color.text_color);
		orderallswipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		orderallswipeLayout.setProgressBackgroundColor(R.color.bg_color);
		orderallswipeLayout.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() 
			{
				// TODO Auto-generated method stub
				page=1;
				reftype=1;
				orderAdapter.clear();
				initData();
			}
		});
	}
	//弹出选择框
	private void showSelectPopupWindow(final String action) 
	{
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = (LayoutInflater)ShopOrderActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        View view = layoutInflater.inflate(R.layout.activity_product_window_select, null);  
        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,  LayoutParams.MATCH_PARENT);  
        popupWindow.setFocusable(true);  
        popupWindow.setOutsideTouchable(true);  
        
        //设置半透明
//		        WindowManager.LayoutParams params=getActivity().getWindow().getAttributes();  
//		        params.alpha=0.7f;  
//		        getActivity().getWindow().setAttributes(params);  
        
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景  
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); 
        
        popupWindow.showAsDropDown(selectLine);
        ImageView closeImageButton = (ImageView) view.findViewById(R.id.closeImageButton);
        closeImageButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				popupWindow.dismiss();
			}
		});
        ListView productSelectList=(ListView) view.findViewById(R.id.productSelectList);
        final List<DataBean> selectList=new ArrayList<>();
        if(action.equals("order"))
        {
    		bankAreAdapter=new BankAreAdapter(ShopOrderActivity.this);
        	DataBean DataBean=new DataBean();
        	DataBean.setId("0");
        	DataBean.setName("全部来源");
        	selectList.add(DataBean);
        	DataBean DataBean1=new DataBean();
        	DataBean1.setId("1");
        	DataBean1.setName("大牌抢购");
        	selectList.add(DataBean1);
        	DataBean DataBean2=new DataBean();
        	DataBean2.setId("7");
        	DataBean2.setName("优惠券");
        	selectList.add(DataBean2);
        	DataBean DataBean3=new DataBean();
        	DataBean3.setId("4");
        	DataBean3.setName("会员卡");
        	selectList.add(DataBean3);

        	bankAreAdapter.addAll(selectList,orderType.getText().toString());
	        productSelectList.setAdapter(bankAreAdapter);
        	
        }
        if(action.equals("product"))
        {
    		bankAreAdapter=new BankAreAdapter(ShopOrderActivity.this);
        	DataBean DataBean5=new DataBean();
        	DataBean5.setId("0");
        	DataBean5.setName("全部订单");
        	selectList.add(DataBean5);
        	DataBean DataBean4=new DataBean();
        	DataBean4.setId("1");
        	DataBean4.setName("新订单");
        	selectList.add(DataBean4);
//        	DataBean DataBean5=new DataBean();
//        	DataBean5.setId("2");
//        	DataBean5.setName("已上架");
//        	selectList.add(DataBean5);
        	bankAreAdapter.addAll(selectList,productAction.getText().toString());
	        productSelectList.setAdapter(bankAreAdapter);
        }
        if(action.equals("status"))
        {
    		bankAreAdapter=new BankAreAdapter(ShopOrderActivity.this);
        	DataBean DataBean6=new DataBean();
        	DataBean6.setId("");
        	DataBean6.setName("所有状态");
        	selectList.add(DataBean6);
        	DataBean DataBean7=new DataBean();
        	DataBean7.setId("1");
        	DataBean7.setName("未支付");
        	selectList.add(DataBean7);
        	DataBean DataBean8=new DataBean();
        	DataBean8.setId("2");
        	DataBean8.setName("已支付");
        	selectList.add(DataBean8);
        	DataBean DataBean9=new DataBean();
        	DataBean9.setId("3");
        	DataBean9.setName("已完成");
        	selectList.add(DataBean9);
        	DataBean DataBean10=new DataBean();
        	DataBean10.setId("-1");
        	DataBean10.setName("已过期");
        	selectList.add(DataBean10);
        	DataBean DataBean11=new DataBean();
        	DataBean11.setId("0");
        	DataBean11.setName("已取消");
        	selectList.add(DataBean11);
        	bankAreAdapter.addAll(selectList,statusAction.getText().toString());
	        productSelectList.setAdapter(bankAreAdapter);
        }
        productSelectList.setOnItemClickListener(new OnItemClickListener() 
        {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				// TODO Auto-generated method stub
				if(action.equals("order"))
				{
					from=selectList.get(position).getId();
					orderType.setText(selectList.get(position).getName());
				}
				if(action.equals("product"))
				{
					isnew=selectList.get(position).getId();
					productAction.setText(selectList.get(position).getName());
				}
				if(action.equals("status"))
				{
					status=selectList.get(position).getId();
					statusAction.setText(selectList.get(position).getName());
				}
				ColorDialog.showRoundProcessDialog(ShopOrderActivity.this,R.layout.loading_process_dialog_color);
				page=1;
				reffinish=false;
				orderAdapter.clear();
				initData();
				popupWindow.dismiss();

			}
		});
        popupWindow.setOnDismissListener(new OnDismissListener() 
        {
			
			@Override
			public void onDismiss() 
			{
				// TODO Auto-generated method stub
				orderType.setTextColor(ShopOrderActivity.this.getResources().getColor(R.color.text_color_gray));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				productAction.setTextColor(ShopOrderActivity.this.getResources().getColor(R.color.text_color_gray));
				productTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				statusAction.setTextColor(getResources().getColor(R.color.text_color_gray));
				statusTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				
			}
		});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
	}

}
