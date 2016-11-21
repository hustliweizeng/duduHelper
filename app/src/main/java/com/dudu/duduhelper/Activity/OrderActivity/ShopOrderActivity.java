package com.dudu.duduhelper.Activity.OrderActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
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

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.adapter.OrderSelectorAdapter;
import com.dudu.duduhelper.adapter.ProductAdapter;
import com.dudu.duduhelper.javabean.SelectorBean;
import com.dudu.duduhelper.adapter.ShopOrderAdapter;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.OrderListBean;
import com.dudu.duduhelper.javabean.OrderStatusBean;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ShopOrderActivity extends BaseActivity 
{
	//状态选择器的显示信息
	private ImageView orderTypeArror;
	private ImageView statusTypeArror;
	private TextView orderType;
	private TextView productAction;
	private TextView statusAction;
	private ImageView productTypeArror;


	private RelativeLayout orderSource;
	private RelativeLayout productRel;
	private RelativeLayout actionRel;

	private LinearLayout selectLine;
	private ListView allOrderListView;
	//订单列表适配器
	private ShopOrderAdapter orderAdapter;
	//订单选择适配器
	private OrderSelectorAdapter orderSelectorAdapter;
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

    public ProductAdapter productAdapter;
    private String status="";
	//筛选需要的参数
    private int source= -100;
	private int isNew= 0;
	private int statuss = -100;

    private String isnew="0";
	//筛选的集合
	private List<SelectorBean> list;
	//第一次加载的数据条目
	int pageLimit = 10;
	private OrderListBean orderData;
	private String lastId;
	private int orderId;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_order);
		orderAdapter=new ShopOrderAdapter(this);
		initHeadView("订单管理", true, true, R.drawable.icon_sousuo);
		//初始化view
		initViewFragment();
		orderallswipeLayout.setProgressViewOffset(false, 0, Util.dip2px(context, 24));//第一次启动时刷新
		orderallswipeLayout.setRefreshing(true);
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

	@Override
	public void RightButtonClick() {
		super.RightButtonClick();
		startActivityForResult(new Intent(context,SearchActivity.class),100);
	}

	//第一次请求时，请求所有数据
	private void initData()
	{
		loading_progressBar.setVisibility(View.VISIBLE);
		loading_text.setText("加载中...");
		RequestParams params = new RequestParams();
		//每次请求10个条目
		params.add("limit",pageLimit+"");
		//第一次运行时为空,想上滑动才需要这个参数
		if(lastId != null && reftype ==2 ){
			params.add("lastid", lastId);
		}
		//订单筛选需要3个条件
		if (statuss != -100 ){
			if (statuss!=0)
			params.add("status",statuss+"");
		}
		//初始值不参与请求
		if (source !=-100){
			if(source!=0)
			params.add("moduleid",source+"");
		}
		//params.add("ispay", isNew+"");
		//根据订单id查找
		if (orderId !=0){
			//清空之前的搜索项
			params = new RequestParams();
			params.add("id",orderId+"");
		}

		HttpUtils.getConnection(context,params, ConstantParamPhone.GET_ORDER_LIST, "GET",new TextHttpResponseHandler()
		{
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3)
			{
				Toast.makeText(context, "网络不给力呀", Toast.LENGTH_SHORT).show();
				if(page==1)
				{
					reloadButton.setVisibility(View.VISIBLE);
				}
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2)
			{
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功,每次请求成功后，添加数据到集合
						orderData = new Gson().fromJson(arg2, OrderListBean.class);
						if (orderData.getList() == null || orderData.getList().size()== 0 ){
							Toast.makeText(context,"当前没有要显示的数据",Toast.LENGTH_SHORT).show();
							loading_text.setText("当前没有要显示的数据");
							loading_progressBar.setVisibility(View.GONE);
							return;
						}
						orderAdapter.addAll(orderData.getList());
						//设置最后一个订单的id,
						lastId = orderAdapter.getLastId();
						
						//LogUtil.d("lastid=", lastId);
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
			@Override
			public void onFinish()
			{
				orderallswipeLayout.setRefreshing(false);
				//ColorDialog.dissmissProcessDialog();
				loading_progressBar.setVisibility(View.INVISIBLE);
				loading_text.setText("");
			}
		});
		//设置适配器
		allOrderListView.setAdapter(orderAdapter);
	}

	@SuppressLint("ResourceAsColor")
	private void initViewFragment() {
		statusAction=(TextView) ShopOrderActivity.this.findViewById(R.id.statusAction);
		actionRel=(RelativeLayout) ShopOrderActivity.this.findViewById(R.id.actionRel);
		statusTypeArror=(ImageView) ShopOrderActivity.this.findViewById(R.id.statusTypeArror);
		orderType = (TextView) ShopOrderActivity.this.findViewById(R.id.orderType);
		orderTypeArror = (ImageView) ShopOrderActivity.this.findViewById(R.id.orderTypeArror);
		productAction = (TextView) ShopOrderActivity.this.findViewById(R.id.productAction);
		productTypeArror = (ImageView) ShopOrderActivity.this.findViewById(R.id.productTypeArror);
		selectLine = (LinearLayout) ShopOrderActivity.this.findViewById(R.id.selectLine);
		productRel = (RelativeLayout) ShopOrderActivity.this.findViewById(R.id.productRel);
		orderSource = (RelativeLayout) ShopOrderActivity.this.findViewById(R.id.orderSource);

		//弹出订单来源选择事件，
		// 当条目点击的时候，本条目颜色变化，图标也变化，其他条目恢复默认值
		orderSource.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				orderType.setTextColor(getResources().getColor(R.color.text_color));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_shang);
				statusAction.setTextColor(getResources().getColor(R.color.text_color_gray));
				statusTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				productAction.setTextColor(getResources().getColor(R.color.text_color_gray));
				productTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				showSelectPopupWindow("source");
			}
		});
		//弹出是否新订单选择事件
		// 当条目点击的时候，本条目颜色变化，图标也变化，其他条目恢复默认值
		productRel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) {
				productAction.setTextColor(getResources().getColor(R.color.text_color));
				productTypeArror.setImageResource(R.drawable.icon_jiantou_shang);
				orderType.setTextColor(getResources().getColor(R.color.text_color_gray));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				statusAction.setTextColor(getResources().getColor(R.color.text_color_gray));
				statusTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				showSelectPopupWindow("isNew");
			}
		});
		//弹出状态选择事件
		// 当条目点击的时候，本条目颜色变化，图标也变化，其他条目恢复默认值
		actionRel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				orderType.setTextColor(getResources().getColor(R.color.text_color_gray));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				statusAction.setTextColor(getResources().getColor(R.color.text_color));
				statusTypeArror.setImageResource(R.drawable.icon_jiantou_shang);
				productAction.setTextColor(getResources().getColor(R.color.text_color_gray));
				productTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				showSelectPopupWindow("status");
			}
		});
		//重新载入按钮
		reloadButton=(Button) ShopOrderActivity.this.findViewById(R.id.reloadButton);
		reloadButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
			//ColorDialog.showRoundProcessDialog(ShopOrderActivity.this,R.layout.loading_process_dialog_color);
				initData();
			}
		});
		//listview初始化
		allOrderListView=(ListView) ShopOrderActivity.this.findViewById(R.id.allOrderListView);
		
		footView = LayoutInflater.from(ShopOrderActivity.this).inflate(R.layout.activity_listview_foot, null);
		loading_progressBar=(ProgressBar) footView.findViewById(R.id.loading_progressBar);
		loading_text=(TextView) footView.findViewById(R.id.loading_text);
		allOrderListView.addFooterView(footView,null,false);
		//listview条目点击事件
		allOrderListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id)
			{
				Intent intent=new Intent(context, ShopOrderDetailActivity.class);
				//传递当前条目的数据过去
				intent.putExtra("id", orderAdapter.getItemId(position));
				LogUtil.d("POS",orderAdapter.getItemId(position)+"");
				startActivityForResult(intent, 1);
			}
		});
		//listview滚动监听,当滚动到最后时候请求新的数据
		allOrderListView.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				//当滚动停止，并且已经是最后一条数据的时候
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE&&lastItemIndex == orderAdapter.getCount())
				{
					//pageLimit+=10;
					//设置刷新方式,这里是下拉刷新
					reftype=2;
					if(!reffinish)
					{
						//再次请求加载数据的时候，需要上一页最后条目的id，以及这次刷新的数量
						initData();
					}
					
					//定位到最后一个条目
					allOrderListView.setSelection(lastItemIndex-1);
                }
				//当滚到第一个条目时,请求父控件处理事件
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE && allOrderListView.getFirstVisiblePosition()  == 0){
					allOrderListView.getParent().requestDisallowInterceptTouchEvent(false);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount)
			{
				lastItemIndex = firstVisibleItem + visibleItemCount -1;
			}
		});
		//下拉刷新
		orderallswipeLayout = (SwipeRefreshLayout)ShopOrderActivity.this.findViewById(R.id.orderallswipeLayout);
		orderallswipeLayout.setColorSchemeResources(R.color.text_color);
		orderallswipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		orderallswipeLayout.setProgressBackgroundColor(R.color.bg_color);
		//上拉加载
		orderallswipeLayout.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh()
			{
				page=1;
				reftype=1;
				//清空适配器中的数据
				orderAdapter.clear();
				initData();
			}
		});
	}
	//弹出选择框，根据action进入不同的下拉界面
	private void showSelectPopupWindow(final String action)
	{
		final OrderStatusBean selector = new OrderStatusBean();
		LayoutInflater layoutInflater = (LayoutInflater)ShopOrderActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.activity_product_window_select, null);
        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,  LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.showAsDropDown(selectLine);
        ImageView closeImageButton = (ImageView) view.findViewById(R.id.closeImageButton);
        closeImageButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		//筛选器里面的listview
        ListView productSelectList=(ListView) view.findViewById(R.id.productSelectList);
		list = null;
		//订单模块
        if(action.equals("source"))
        {
			//获取所有订单来源信息
			orderSelectorAdapter =new OrderSelectorAdapter(context);
			//获取所有订单信息
			list = selector.getAllOrderSource();
			orderSelectorAdapter.addAll(list,orderType.getText().toString());
	        productSelectList.setAdapter(orderSelectorAdapter);
        }
		//订单类型
        if(action.equals("isNew"))
        {
			orderSelectorAdapter =new OrderSelectorAdapter(ShopOrderActivity.this);
        	list = selector.getAllOrderType();
			orderSelectorAdapter.addAll(list,productAction.getText().toString());
	        productSelectList.setAdapter(orderSelectorAdapter);
        }
		//订单状态
        if(action.equals("status"))
        {
			orderSelectorAdapter =new OrderSelectorAdapter(ShopOrderActivity.this);
			list = selector.getAllOrderStatus();
			//把数据和选中的条目传递给adapter
			orderSelectorAdapter.addAll(list,statusAction.getText().toString());
	        productSelectList.setAdapter(orderSelectorAdapter);
        }
		//筛选条目的点击事件,保存选中的条目信息
        productSelectList.setOnItemClickListener(new OnItemClickListener()
        {
			@Override
			//保存选中的状态
			public void onItemClick(AdapterView<?> parent, View view,int position, long id)
			{
				if(action.equals("source"))
				{
					source = list.get(position).id;
					orderType.setText(list.get(position).name);
					LogUtil.d("source",source+"");
				}
				if(action.equals("isNew"))
				{
					isNew = list.get(position).id;
					productAction.setText(list.get(position).name);
					LogUtil.d("isnew",statuss+"");
				}
				if(action.equals("status"))
				{
					statuss = list.get(position).id;
					statusAction.setText(list.get(position).name);
					LogUtil.d("status",statuss+"");
				}
				//条目点击之后，根据条件请求新的数据
				//ColorDialog.showRoundProcessDialog(ShopOrderActivity.this,R.layout.loading_process_dialog_color);
				reffinish=false;
				//清空所有数据
				orderAdapter.clear();
				initData();
				popupWindow.dismiss();
			}
		});

		//popwindow消失后的响应事件
        popupWindow.setOnDismissListener(new OnDismissListener()
        {

			@Override
			public void onDismiss()
			{
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
	//通过搜索返回的结果id号
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		//orderId = data.getIntExtra("id",0);
		initData();
	}

}
