package com.dudu.duduhelper.fragment;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
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

import com.dudu.duduhelper.Activity.MainActivity;
import com.dudu.duduhelper.Activity.OrderActivity.ShopOrderDetailActivity;
import com.dudu.duduhelper.Activity.WelcomeActivity.LoginActivity;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.adapter.OrderSelectorAdapter;
import com.dudu.duduhelper.adapter.ProductAdapter;
import com.dudu.duduhelper.adapter.ShopOrderAdapter;
import com.dudu.duduhelper.bean.OrderBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.dudu.duduhelper.javabean.ProvinceListBean;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;
import com.dudu.duduhelper.R;
public class CopyOfOrderFragment extends Fragment {
	private View OrderFragmentView;
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
    private OrderSelectorAdapter orderSelectorBean;
    public ProductAdapter productAdapter;
    private String status="";
    private String from="0";
    private String isnew="0";

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		OrderFragmentView = inflater.inflate(R.layout.copy_of_fragment_order,null);
		return OrderFragmentView;
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		orderAdapter=new ShopOrderAdapter(getActivity());
		initViewFragment();
		initData();
	}
	public void setRefreshing()
	{
		orderallswipeLayout.setProgressViewOffset(false, 0, Util.dip2px(getActivity(), 50));
		orderallswipeLayout.setRefreshing(true);
		page=1;
		reftype=1;
		orderAdapter.clear();
		initData();
	}

	private void initData() 
	{
		// TODO Auto-generated method stub
		loading_progressBar.setVisibility(View.VISIBLE);
		loading_text.setText("加载中...");
		allOrderListView.setAdapter(orderAdapter);
		RequestParams params = new RequestParams();
		params.add("token", ((MainActivity)getActivity()).share.getString("token", ""));
		params.add("page",String.valueOf(page));
		params.add("pagesize","10");
		params.add("version", ConstantParamPhone.VERSION);
		
		params.add("status",status);
		params.add("from",from);
		params.add("isnew", isnew);
		
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(getActivity());    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.GET_FULL_LIST, params,new TextHttpResponseHandler()
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
					MyDialog.showDialog(getActivity(), "该账号已在其他手机登录，是否重新登录", false, true, "取消", "确定",new OnClickListener() {
						
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
					if(orderBean.getData()!=null&&orderBean.getData().size()!=0)
					{
						//orderAdapter.addAll(orderBean.getData());
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
							Toast.makeText(getActivity(), "加载完啦", Toast.LENGTH_SHORT).show();
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
		// TODO Auto-generated method stub
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
		statusAction=(TextView) OrderFragmentView.findViewById(R.id.statusAction);
		actionRel=(RelativeLayout) OrderFragmentView.findViewById(R.id.actionRel);
		statusTypeArror=(ImageView) OrderFragmentView.findViewById(R.id.statusTypeArror);
		orderType = (TextView) OrderFragmentView.findViewById(R.id.orderType);
		orderTypeArror = (ImageView) OrderFragmentView.findViewById(R.id.orderTypeArror);
		productAction = (TextView) OrderFragmentView.findViewById(R.id.productAction);
		productTypeArror = (ImageView) OrderFragmentView.findViewById(R.id.productTypeArror);
		selectLine = (LinearLayout) OrderFragmentView.findViewById(R.id.selectLine);
		productRel = (RelativeLayout) OrderFragmentView.findViewById(R.id.productRel);
		orderTypeRel = (RelativeLayout) OrderFragmentView.findViewById(R.id.orderTypeRel);
		
		//弹出分类选择事件
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
		
		
		footView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_listview_foot, null);
		loading_progressBar=(ProgressBar) footView.findViewById(R.id.loading_progressBar);
		loading_text=(TextView) footView.findViewById(R.id.loading_text);
		
		// TODO Auto-generated method stub
		reloadButton=(Button) OrderFragmentView.findViewById(R.id.reloadButton);
		//数据重载按钮
		reloadButton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				ColorDialog.showRoundProcessDialog(getActivity(),R.layout.loading_process_dialog_color);
				initData();
			}
		});
		allOrderListView=(ListView) OrderFragmentView.findViewById(R.id.allOrderListView);
		allOrderListView.addFooterView(footView,null,false);
		allOrderListView.setOnItemClickListener(new OnItemClickListener() 
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(), ShopOrderDetailActivity.class);
				//intent.putExtra("no", orderAdapter.getItem(position).getNo());
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
		orderallswipeLayout = (SwipeRefreshLayout)OrderFragmentView.findViewById(R.id.orderallswipeLayout);
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
		LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
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
        
        int screenWidth=getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int popWidth=popupWindow.getWidth();  
        popupWindow.showAsDropDown(selectLine);
        ListView productSelectList=(ListView) view.findViewById(R.id.productSelectList);
        final List<ProvinceListBean.DataBean> selectList=new ArrayList<>();
        if(action.equals("order"))
        {
    		orderSelectorBean =new OrderSelectorAdapter(getActivity());
			ProvinceListBean.DataBean DataBean=new ProvinceListBean.DataBean();
        	DataBean.setId("0");
        	DataBean.setName("全部来源");
        	selectList.add(DataBean);
        	ProvinceListBean.DataBean DataBean1=new ProvinceListBean.DataBean();
        	DataBean1.setId("1");
        	DataBean1.setName("大牌抢购");
        	selectList.add(DataBean1);
        	ProvinceListBean.DataBean DataBean2=new ProvinceListBean.DataBean();
        	DataBean2.setId("7");
        	DataBean2.setName("优惠券");
        	selectList.add(DataBean2);
        	ProvinceListBean.DataBean DataBean3=new ProvinceListBean.DataBean();
        	DataBean3.setId("4");
        	DataBean3.setName("会员卡");
        	selectList.add(DataBean3);

        	//orderSelectorBean.addAll(selectList,orderType.getText().toString());
	        productSelectList.setAdapter(orderSelectorBean);
        	
        }
        if(action.equals("product"))
        {
    		orderSelectorBean =new OrderSelectorAdapter(getActivity());
        	ProvinceListBean.DataBean DataBean5=new ProvinceListBean.DataBean();
        	DataBean5.setId("0");
        	DataBean5.setName("全部订单");
        	selectList.add(DataBean5);
        	ProvinceListBean.DataBean DataBean4=new ProvinceListBean.DataBean();
        	DataBean4.setId("1");
        	DataBean4.setName("新订单");
        	selectList.add(DataBean4);
//        	DataBean DataBean5=new DataBean();
//        	DataBean5.setId("2");
//        	DataBean5.setName("已上架");
//        	selectList.add(DataBean5);
        	//orderSelectorBean.addAll(selectList,productAction.getText().toString());
	        productSelectList.setAdapter(orderSelectorBean);
        }
        if(action.equals("status"))
        {
    		orderSelectorBean =new OrderSelectorAdapter(getActivity());
        	ProvinceListBean.DataBean DataBean6=new ProvinceListBean.DataBean();
        	DataBean6.setId("");
        	DataBean6.setName("所有状态");
        	selectList.add(DataBean6);
        	ProvinceListBean.DataBean DataBean7=new ProvinceListBean.DataBean();
        	DataBean7.setId("1");
        	DataBean7.setName("未支付");
        	selectList.add(DataBean7);
        	ProvinceListBean.DataBean DataBean8=new ProvinceListBean.DataBean();
        	DataBean8.setId("2");
        	DataBean8.setName("已支付");
        	selectList.add(DataBean8);
        	ProvinceListBean.DataBean DataBean9=new ProvinceListBean.DataBean();
        	DataBean9.setId("3");
        	DataBean9.setName("已完成");
        	selectList.add(DataBean9);
        	ProvinceListBean.DataBean DataBean10=new ProvinceListBean.DataBean();
        	DataBean10.setId("-1");
        	DataBean10.setName("已过期");
        	selectList.add(DataBean10);
        	ProvinceListBean.DataBean DataBean11=new ProvinceListBean.DataBean();
        	DataBean11.setId("0");
        	DataBean11.setName("已取消");
        	selectList.add(DataBean11);
        	//orderSelectorBean.addAll(selectList,statusAction.getText().toString());
	        productSelectList.setAdapter(orderSelectorBean);
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
				ColorDialog.showRoundProcessDialog(getActivity(),R.layout.loading_process_dialog_color);
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
				orderType.setTextColor(getActivity().getResources().getColor(R.color.text_color_gray));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				productAction.setTextColor(getActivity().getResources().getColor(R.color.text_color_gray));
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
