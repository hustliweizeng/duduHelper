package com.dudu.duduhelper.Activity.BigBandActivity;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
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
import com.dudu.duduhelper.Activity.DiscountCardActivity.ShopCouponDetailActivity;
import com.dudu.duduhelper.Activity.RedBagActivity.ShopHongBaoAddActivity;
import com.dudu.duduhelper.Activity.RedBagActivity.ShopHongBaoDetailActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.adapter.OrderSelectorAdapter;
import com.dudu.duduhelper.adapter.ProductAdapter;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.bean.HongbaoListBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.BigBandBuy;
import com.dudu.duduhelper.javabean.ProductStatus;
import com.dudu.duduhelper.javabean.RedBagStatus;
import com.dudu.duduhelper.javabean.SelectorBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class shopProductListActivity extends BaseActivity
{
	private TextView orderType;
	private ImageView orderTypeArror;
	private TextView productAction;
	private ImageView productTypeArror;
	private RelativeLayout orderTypeRel;
	private RelativeLayout productRel;
	private LinearLayout selectLine;
	private PopupWindow popupWindow;
	//商品下拉选择数据加载
	private OrderSelectorAdapter orderSelectorAdapter;
	public ProductAdapter productAdapter;
	private ListView productListView;
	//编辑按钮
	private Button editButton;
	//编辑栏
	private LinearLayout editProductLine;
	//重载按钮
	private Button reloadButton;
	//全选按钮
	private ImageButton productAllCheckImg;
	private Button addButton;
	private ImageView productAllDelectButton;
	private boolean isMulChoice = false; //是否显示编辑界面
	private boolean isAllChoice = false;//是否全选
	//private List<Integer> selectid = new ArrayList<Integer>();//选中的id
	//商品分类
	public String category;
	//商品排序
	private String order="default";
	//商品状态
	private String status="0";
	//判断是上拉还是下拉
	private int reftype;
	//判断数据是否加载完成
	private boolean reffinish=false;
	//是否是优惠券
	private boolean isDisCount;
	//是否是红包
	private boolean isHongbao;

	private View footView;
	private ProgressBar loading_progressBar;
	private TextView loading_text;
	private boolean isShowChekckBox = false;
	private TextView tv_chekcAll_product_list;
	private ImageButton backButton;
	private BigBandBuy bigBandBuy;
	private SwipeRefreshLayout swipe_product_list;
	private int lastItemIndex;
	//private View foot;
	private  String upProduct = "all";
	private  String productStatus= "all"; 

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_product_list);
		//获取进入的列表类型（大牌抢购和优惠券公用）
		category=getIntent().getStringExtra("category");
		
		//全屏显示的对话框进度条
		DuduHelperApplication.getInstance().addActivity(this);
		initView();

		//根据不同类型请求不同的参数
		if(category.equals("discount"))
		{
			//优惠券列表
			productAdapter=new ProductAdapter(this,isMulChoice,isDisCount,isHongbao);
			initData(ConstantParamPhone.GET_DISCOUNT_LIST);
		}
		if(category.equals("bigband"))
		{
			LogUtil.d("bigband","商品列表");
			//商品列表
			productAdapter=new ProductAdapter(this,isMulChoice,isDisCount,isHongbao);
			initData(ConstantParamPhone.GET_BIG_BAND_LIST);
		}
		if(category.equals("hongbao"))
		{
			//红包列表
			//商品排序
			order="getmore";
			//商品状态
			status="all";
			productAdapter=new ProductAdapter(this,isMulChoice,isDisCount,isHongbao);
			//接口出来后要改回来！！！
			initData(ConstantParamPhone.GET_HONGBAO_LIST);
		}
	}
	//请求不同的接口url
	private void initData(String url)
	{
		ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
		//loading_progressBar.setVisibility(View.VISIBLE);
		loading_text.setText("加载中...");
		productListView.setAdapter(productAdapter);
		RequestParams params = new RequestParams();
		params.put("default",productStatus);
		params.put("status",upProduct);
		params.put("page","1");
		params.put("num","10");
		HttpUtils.getConnection(context,params,url, "post",new TextHttpResponseHandler()
		{
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3)
			{
				Toast.makeText(shopProductListActivity.this, "网络不给力呀"+arg2, Toast.LENGTH_LONG).show();
				LogUtil.d("fail",arg3.toString()+"==="+arg2);
				reloadButton.setVisibility(View.VISIBLE);
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2)
			{
				LogUtil.d("product",arg2);
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//解析优惠券的信息
						if (category.equals("bigband")){
							bigBandBuy = new Gson().fromJson(arg2, BigBandBuy.class);
						}
						//解析红包列表信息
						if (category.equals("hongbao")){

						}
						//解析大牌抢购列表信息
						if (category.equals("discount")){
							bigBandBuy = new Gson().fromJson(arg2,BigBandBuy.class);
						}
						reloadButton.setVisibility(View.GONE);
						productAdapter.addAll(bigBandBuy.getData(),isAllChoice);

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
				//隐藏对话框
				ColorDialog.dissmissProcessDialog();
				//隐藏swipe的进度条
				swipe_product_list.setRefreshing(false);
				//隐藏脚布局
				footView.setVisibility(View.GONE);
			}
		});

	}

	@SuppressLint("ResourceAsColor")
	private void initView()
	{
		//新建商品按钮
		addButton = (Button) findViewById(R.id.addButton);
		addButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				/*Intent intent=new Intent(shopProductListActivity.this,ShopProductAddActivity.class);
				intent.putExtra("category", category);
				startActivity(intent);*/
				Toast.makeText(context,"功能暂未开通，敬请期待",Toast.LENGTH_SHORT);
				return;
			}
		});
		//第二个筛选项
		productAction=(TextView) this.findViewById(R.id.productAction);
		//设置头布局
		if(category.equals("discount"))
		{
			initHeadView("优惠券", true, false, 0);
			isDisCount=true;
			isHongbao=false;
			productAction.setText("优惠券状态");
		}
		if(category.equals("bigband"))
		{
			initHeadView("大牌抢购", true, false, 0);
			isDisCount=false;
			isHongbao=false;
		}
		if(category.equals("hongbao"))
		{
			initHeadView("商家红包", true, true, R.drawable.icon_tianjia);
			isDisCount=false;
			isHongbao=true;
			productAction.setText("红包状态");
		}
		
		//listview脚布局
		footView = LayoutInflater.from(this).inflate(R.layout.activity_listview_foot, null);
		loading_progressBar=(ProgressBar) footView.findViewById(R.id.loading_progressBar);
		loading_text=(TextView) footView.findViewById(R.id.loading_text);
		
		
		//刷新布局
		swipe_product_list = (SwipeRefreshLayout) findViewById(R.id.swipe_product_list);
		swipe_product_list.setColorSchemeResources(R.color.text_color);
		swipe_product_list.setSize(SwipeRefreshLayout.DEFAULT);
		swipe_product_list.setProgressBackgroundColor(R.color.bg_color);
		//上拉加载
		swipe_product_list.setOnRefreshListener(new OnRefreshListener() {

			@Override
			public void onRefresh()
			{
				//page = 1;
				reftype=1;
				//清空适配器中的数据
				productAdapter.clear();
				//根据不同类型请求不同的参数
				if(category.equals("discount"))
				{
					//优惠券列表
					initData(ConstantParamPhone.GET_DISCOUNT_LIST);
				}
				if(category.equals("bigband"))
				{
					LogUtil.d("bigband","商品列表");
					//商品列表
					initData(ConstantParamPhone.GET_BIG_BAND_LIST);
				}
				if(category.equals("hongbao"))
				{
					//红包列表
					//商品排序
					order="getmore";
					//商品状态
					status="all";
					//接口出来后要改回来！！！
					initData(ConstantParamPhone.GET_HONGBAO_LIST);
				}
			}
		});


		//返回键
		backButton = (ImageButton) findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		loading_text=(TextView) footView.findViewById(R.id.loading_text);
		productAllDelectButton=(ImageView) this.findViewById(R.id.productAllDelectButton);
		reloadButton=(Button) this.findViewById(R.id.reloadButton);
		//全选图标
		productAllCheckImg=(ImageButton) this.findViewById(R.id.productAllCheckImg);
		//全选文字
		tv_chekcAll_product_list = (TextView) findViewById(R.id.tv_chekcAll_product_list);
		editProductLine=(LinearLayout) this.findViewById(R.id.editProductLine);
		//暂时把添加商品隐藏
//		addButton=(ImageButton) this.findViewById(R.id.selectClickButton);
//		addButton.setVisibility(View.GONE);
//		addButton.setImageResource(R.drawable.icon_tianjia);
		//重写父类button,点击弹出checkbox
		//设置右边的按钮
		editButton=(Button) this.findViewById(R.id.selectTextClickButton);
		editButton.setText("批量");
		//editButton.setPadding(28, 30, 26, 28);
		if(category.equals("hongbao"))
		{
			editButton.setVisibility(View.GONE);
		}
		else
		{
			editButton.setVisibility(View.VISIBLE);
		}


		//批量删除选中的条目
		productAllDelectButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				String groupid="";
				//从适配器那里获取要删除的条目id信息
				for (String id : productAdapter.selectid)
				{
					groupid=groupid+","+id;
				}
				if(isDisCount)
				{
					groupid = productAdapter.selectid.get(0);
					AllMethod(groupid,ConstantParamPhone.DEL_DISECOUNT);
				}
				else
				{
					//删除条目
					AllMethod(groupid,ConstantParamPhone.DEL_BIG_BAND);
				}

			}


		});


		//重写父类的foot的显隐性
		//foot=(View) this.findViewById(R.id.foot);
		//数据重载按钮
		reloadButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				if(isDisCount)
				{
					initData(ConstantParamPhone.GET_DISCOUNT_LIST);
				}
				else
				{
					if(isHongbao)
					{
						initData(ConstantParamPhone.GET_HONGBAO_LIST);
					}
					else
					{
						initData(ConstantParamPhone.GET_BIG_BAND_LIST);
					}
				}
			}
		});

		productListView=(ListView) this.findViewById(R.id.productListView);

		orderType=(TextView) this.findViewById(R.id.orderType);
		orderTypeArror=(ImageView) this.findViewById(R.id.orderTypeArror);

		productTypeArror=(ImageView) this.findViewById(R.id.productTypeArror);
		selectLine=(LinearLayout) this.findViewById(R.id.selectLine);
		productRel=(RelativeLayout) this.findViewById(R.id.productRel);
		orderTypeRel=(RelativeLayout) this.findViewById(R.id.orderTypeRel);
		
		//产品列表
		productListView.addFooterView(footView,null,false);
		productListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id)
			{
				if(isDisCount)
				{
					Intent intent=new Intent(context,ShopCouponDetailActivity.class);
					intent.putExtra("id",productAdapter.getItemId(position));
					intent.putExtra("productinfo",((BigBandBuy.DataBean)productAdapter.getItem(position)));
					startActivityForResult(intent, 1);
				}
				else
				{
					if(isHongbao)
					{
						Intent intent=new Intent(context,ShopHongBaoDetailActivity.class);
						intent.putExtra("hongbao", ((HongbaoListBean)productAdapter.getItem(position)));
						startActivityForResult(intent, 1);
					}
					else
					{
						//进入商品详情页面
						Intent intent=new Intent(context,ShopProductDetailActivity.class);
						intent.putExtra("productinfo", ((BigBandBuy.DataBean)productAdapter.getItem(position)));
						intent.putExtra("category", category);
						startActivityForResult(intent, 1);
					}
				}

			}
		});
		//listview的滑动监听
		productListView.setOnScrollListener(new OnScrollListener()
		{
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				lastItemIndex = firstVisibleItem + visibleItemCount -1;
			}

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				//当滚动停止的时候
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) // productAdapter.getCount()记录的是数据的长度
				{
					// 判断滚动到底部
					if (productListView.getLastVisiblePosition() == (productListView.getCount() - 1)) {
						LogUtil.d("scrool","底部");
						if(!reffinish)
						{
							if(isDisCount)
							{
								//优惠券列表
								//initData(ConstantParamPhone.GET_DISCOUNT_LIST);
								Toast.makeText(context,"数据已经加载完毕",Toast.LENGTH_LONG).show();
								productListView.removeFooterView(footView);
							}
							else
							{
								if(isHongbao)
								{
									//红包列表
									initData(ConstantParamPhone.GET_HONGBAO_LIST);
								}
								else
								{
									//大牌抢购页面,api没有提供分页加载接口
									//initData(ConstantParamPhone.GET_BIG_BAND_LIST);
									//Toast.makeText(context,"数据已经加载完毕",Toast.LENGTH_LONG).show();
									productListView.removeFooterView(footView);
								}
							}
						}
						//定位到最后一个条目
						//productListView.setSelection(lastItemIndex-1);
					}
					// 判断滚动到顶部
					if(productListView.getFirstVisiblePosition() == 0){
						LogUtil.d("scrool","顶部");
						//交给父控件处理事件
						productListView.getParent().requestDisallowInterceptTouchEvent(false);
					}
				}
			}

		});
		//左侧筛选
		orderTypeRel.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				orderType.setTextColor(getResources().getColor(R.color.text_green_color));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_shang);
				productAction.setTextColor(getResources().getColor(R.color.text_color_gray));
				productTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				showSelectPopupWindow("order");
			}
		});
		//弹出第二个
		productRel.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				productAction.setTextColor(getResources().getColor(R.color.text_green_color));
				productTypeArror.setImageResource(R.drawable.icon_jiantou_shang);
				orderType.setTextColor(getResources().getColor(R.color.text_color_gray));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				showSelectPopupWindow("product");

			}
		});

		//全选按钮点击事件
		productAllCheckImg.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				if(!isAllChoice)
				{
					productAdapter.selectid.clear();
					isAllChoice=true;
					//全选按钮变为选中状态
					productAllCheckImg.setImageResource(R.drawable.icon_xuanze_sel);
					tv_chekcAll_product_list.setVisibility(View.VISIBLE);
					for(int i=0;i<productAdapter.getCount();i++)
					{
						//设置所有的checkbox可见，选中
						productAdapter.visiblecheck.put(i, CheckBox.VISIBLE);
						productAdapter.ischeck.put(i,true);
						//保存选中的所有条目id
						productAdapter.selectid.add(((BigBandBuy.DataBean)productAdapter.getItem(i)).getId());
					}
				}
				else
				{
					isAllChoice=false;
					productAllCheckImg.setImageResource(R.drawable.icon_xuanze);
					tv_chekcAll_product_list.setVisibility(View.VISIBLE);
					//设置所有的checkbox可见，不选中
					for(int i=0;i<productAdapter.getCount();i++)
					{
						//设置所有的checkbox可见，不选中
						productAdapter.visiblecheck.put(i, CheckBox.VISIBLE);
						productAdapter.ischeck.put(i,false);
						productAdapter.selectid.clear();
					}
				}
				//更新视图状态
				productAdapter.notifyDataSetChanged();

			}
		});
		//显示编辑界面
		editButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//清空选中的数据
				productAdapter.selectid.clear();
				//重置全选按钮
				isAllChoice=false;
				//全选按钮可见
				productAllCheckImg.setImageResource(R.drawable.icon_xuanze);
				if(!isMulChoice)//如果不是编辑，点击进入编辑
				{
					editButton.setText("取消");
					//设置复选框可见
					isShowChekckBox = true;
					productAdapter.isShowCheckBox = isShowChekckBox;
					//全选文本显示
					//编辑界面
					editProductLine.setVisibility(View.VISIBLE);
					//下方新建和添加按钮
					addButton.setVisibility(View.GONE);
					//设置多选按钮状态
					isMulChoice = true;
					productAdapter.notifyDataSetChanged();
				}
				else//如果正在编辑点击取消
				{
					editButton.setText("批量");
					//不显示复选框
					isShowChekckBox = false;
					productAdapter.isShowCheckBox = isShowChekckBox;
					//删除图片不可见
					editProductLine.setVisibility(View.GONE);
					//新建和添加按钮可见
					addButton.setVisibility(View.VISIBLE);
					//当前不是复选状态
					isMulChoice = false;
					//设置不可见，不选中
					//productAdapter.isAllSelect = false;
					productAdapter.notifyDataSetChanged();
				}
			}

		});
	}
	//弹出选择框,过滤信息
	private void showSelectPopupWindow(final String action)
	{
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = (LayoutInflater)shopProductListActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.activity_product_window_select, null);
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,  LayoutParams.MATCH_PARENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);
		//设置半透明
		//WindowManager.LayoutParams params=getActivity().getWindow().getAttributes();
		//params.alpha=0.7f;
		//getActivity().getWindow().setAttributes(params);
		//这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
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
		final List<SelectorBean> selectList=new ArrayList<>();
		RedBagStatus redBagStatus = new RedBagStatus();
		ProductStatus products = new ProductStatus();

		//左侧筛选，设置列表数据
		if(action.equals("order"))
		{
			orderSelectorAdapter =new OrderSelectorAdapter(this);
			if(category.equals("hongbao"))
			//红包列表
			{
				selectList.addAll(redBagStatus.getRedBagOrderby());
			}
			else
			{
				//产品列表和优惠券列表
				selectList.addAll(products.getProductORderBy());
			}
			//把数据集合添加到适配器中
			orderSelectorAdapter.addAll(selectList,orderType.getText().toString());
			productSelectList.setAdapter(orderSelectorAdapter);

		}

		//右侧筛选
		else
		{
			orderSelectorAdapter =new OrderSelectorAdapter(this);
			if(category.equals("hongbao"))
			{
				selectList.addAll(redBagStatus.getRedBagStatus());
			}
			else
			{
				//产品列表和优惠券列表
				selectList.addAll(products.getProductStatus());
			}
			orderSelectorAdapter.addAll(selectList,productAction.getText().toString());
			productSelectList.setAdapter(orderSelectorAdapter);
		}
		
		
		
		//筛选条目点击事件
		productSelectList.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id)
			{
				//左侧筛选事件
				if(action.equals("order"))
				{
					//获取被选中条目的信息-左侧
					order= selectList.get(position).id+"";
					orderType.setText(selectList.get(position).name);
					//更新适配器中被选中的条目
					orderSelectorAdapter.select = selectList.get(position).name;
				}
				else
				//右侧筛选
				{
					//获取被选中条目的信息-右侧
					status=selectList.get(position).id+"";
					productAction.setText(selectList.get(position).name);
					//更新适配器中被选中的条目
					orderSelectorAdapter.select = selectList.get(position).name;
				}

				productAdapter=new ProductAdapter(context,isMulChoice,isDisCount,isHongbao);
				reffinish = false;
				switch (order){
					
					case "1":
						productStatus = "all";
						break;
					case "2":
						productStatus = "saled_asc";
						break;
					case "3":
						productStatus = "saled_desc";
						break;
					case "4":
						productStatus = "clicked_asc";
						break;
					case "5":
						productStatus = "clicked_desc";
						break;
				}
				switch (status){
					case "0":
						upProduct = "all";
						break;
					case "1":
						upProduct = "down";
						break;
					case "2":
						upProduct = "up";
						break;
					
				}
				LogUtil.d("select","upproduct="+upProduct+";status="+productStatus);
				//重新加载，设置筛选条件
				//刷新数据之前，先清空
				productAdapter.list.clear();
				if(isDisCount)
				{
					LogUtil.d("discou","upproduct="+upProduct+";status="+productStatus);
					initData(ConstantParamPhone.GET_DISCOUNT_LIST);
				}
				else
				{
					if(isHongbao)
					{
						initData(ConstantParamPhone.GET_HONGBAO_LIST);
					}
					else
					{
						initData(ConstantParamPhone.GET_BIG_BAND_LIST);
					}
				}
				popupWindow.dismiss();

			}
		});
		popupWindow.setOnDismissListener(new OnDismissListener()
		{

			@Override
			public void onDismiss()
			{
				//重置所有按钮
				orderType.setTextColor(shopProductListActivity.this.getResources().getColor(R.color.text_color_gray));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				productAction.setTextColor(shopProductListActivity.this.getResources().getColor(R.color.text_color_gray));
				productTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
			}
		});
	}
	@Override
	public void onActivityResult(int arg0, int arg1, Intent arg2)
	{
		//清空adapter数据
		productAdapter.clear();
		//loadMoreView.setVisibility(View.GONE);
		reffinish=false;
		if(isDisCount)
		{
			initData(ConstantParamPhone.GET_DISCOUNT_LIST);
		}
		else
		{
			if(isHongbao)
			{
				initData(ConstantParamPhone.GET_HONGBAO_LIST);
			}
			else
			{
				initData(ConstantParamPhone.GET_BIG_BAND_LIST);
			}
		}
	}

	/**
	 * 根据action不同，去请求网络
	 * @param ids    提供的所有id
	 * @param url   请求的url地址
	 */
	private void AllMethod( final String ids, String url)
	{
		if(TextUtils.isEmpty(ids))
		{
			Toast.makeText(shopProductListActivity.this, "您尚未选中任何商品", Toast.LENGTH_SHORT).show();
			return;
		}
		//ColorDialog.showRoundProcessDialog(shopProductListActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("id",ids);
		LogUtil.d("del",ids);
		HttpUtils.getConnection(context,params,url, "post",new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3)
			{
				Toast.makeText(shopProductListActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2)
			{
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//清空数据,再去重新加载
						productAdapter.list.clear();
						//再次请求数据
						if (category.equals("discount")){
							
							initData(ConstantParamPhone.GET_DISCOUNT_LIST);
						}
						if (category.equals("bigband")){
							initData(ConstantParamPhone.GET_BIG_BAND_LIST);
						}
						//清空选中的条目
						productAdapter.selectid.clear();
						Toast.makeText(shopProductListActivity.this, "删除成功啦", Toast.LENGTH_SHORT).show();
						isAllChoice = false;
						productAllCheckImg.setImageResource(R.drawable.icon_xuanze);
						editButton.setText("批量");
						//不显示复选框
						isShowChekckBox = false;
						productAdapter.isShowCheckBox = isShowChekckBox;
						//删除图片不可见
						editProductLine.setVisibility(View.GONE);
						//新建和添加按钮可见
						addButton.setVisibility(View.VISIBLE);
						//当前不是复选状态
						isMulChoice = false;
						//设置不可见，不选中
						//productAdapter.isAllSelect = false;
						productAdapter.notifyDataSetChanged();

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
				ColorDialog.dissmissProcessDialog();
			}
		});
	}
	@Override
	public void RightButtonClick()
	{
		//显示添加界面
		Intent intent=new Intent(shopProductListActivity.this,ShopHongBaoAddActivity.class);
		startActivityForResult(intent, 1);
	}

}
