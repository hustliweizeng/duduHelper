package com.dudu.duduhelper;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.text.TextUtils;
import android.util.Log;
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

import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.adapter.OrderSelectorAdapter;
import com.dudu.duduhelper.adapter.ProductAdapter;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.bean.HongbaoListBean;
import com.dudu.duduhelper.bean.ProductListBean;
import com.dudu.duduhelper.bean.ResponsBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.BigBandBuy;
import com.dudu.duduhelper.javabean.ProvinceListBean.DataBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class shopProductListActivity extends BaseActivity
{
	private RelativeLayout allTypeRel;
	private TextView allType;
	private ImageView allTypeArror;
	private TextView orderType;
	private ImageView orderTypeArror;
	private TextView productAction;
	private ImageView productTypeArror;
	private RelativeLayout orderTypeRel;
	private RelativeLayout productRel;
	private LinearLayout selectLine;
	private PopupWindow popupWindow;
	//商品下拉选择数据加载
	private OrderSelectorAdapter orderSelectorBean;
	public ProductAdapter productAdapter;
	private SwipeRefreshLayout productSwipeLayout;
	private ListView productListView;
	//编辑按钮
	private Button editButton;
	//编辑按钮
	//private ImageButton addButton;
	//编辑栏
	private LinearLayout editProductLine;
	//重载按钮
	private Button reloadButton;
	//全选按钮
	private ImageButton productAllCheckImg;
	//上架按钮
	private Button upbutton;
	//下架按钮
	private Button downbutton;
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
	//下拉分页
	private int page=1;
	private int lastItemIndex;
	//private View loadMoreView;
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
	//private View foot;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_product_list);
		//获取进入的列表类型
		category=getIntent().getStringExtra("category");
		ColorDialog.showRoundProcessDialog(this,R.layout.loading_process_dialog_color);
		DuduHelperApplication.getInstance().addActivity(this);
		initView();

		//根据不同类型请求不同的参数
		if(category.equals("discount"))
		{
			//折扣列表
			productAdapter=new ProductAdapter(this,isMulChoice,isDisCount,isHongbao);
			initData(ConstantParamPhone.GET_COUPON_LIST);
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
		loading_progressBar.setVisibility(View.VISIBLE);
		loading_text.setText("加载中...");
		productListView.setAdapter(productAdapter);
		RequestParams params = new RequestParams();
		if(category.equals("bigband"))
		{
			HttpUtils.getConnection(context,params,url, "post",new TextHttpResponseHandler()
			{
				@Override
				public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3)
				{
					Toast.makeText(shopProductListActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
					if(page==1)
					{
						reloadButton.setVisibility(View.VISIBLE);
					}
				}
				@Override
				public void onSuccess(int arg0, Header[] arg1, String arg2)
				{
					//LogUtil.d("bigband",arg2);
					try {
						JSONObject object = new JSONObject(arg2);
						String code =  object.getString("code");
						if ("SUCCESS".equalsIgnoreCase(code)){
							//数据请求成功
							BigBandBuy bigBandBuy = new Gson().fromJson(arg2, BigBandBuy.class);
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
					//设置刷新状态
					productSwipeLayout.setRefreshing(false);
					//隐藏对话框
					ColorDialog.dissmissProcessDialog();
				}
			});
		}
	}

	@SuppressLint("ResourceAsColor")
	private void initView()
	{
		addButton=(Button) this.findViewById(R.id.addButton);
		addButton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				Intent intent=new Intent(shopProductListActivity.this,ShopProductAddActivity.class);
				intent.putExtra("category", "coupon");
				startActivity(intent);
			}
		});
		productAction=(TextView) this.findViewById(R.id.productAction);
		if(category.equals("coupon"))
		{
			initHeadView("优惠券", true, false, 0);
			isDisCount=true;
			isHongbao=false;
			productAction.setText("优惠券状态");
		}
		if(category.equals("buying"))
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
		footView = LayoutInflater.from(this).inflate(R.layout.activity_listview_foot, null);
		loading_progressBar=(ProgressBar) footView.findViewById(R.id.loading_progressBar);
		loading_text=(TextView) footView.findViewById(R.id.loading_text);
		downbutton=(Button) this.findViewById(R.id.downbutton);
		upbutton=(Button) this.findViewById(R.id.upButton);
		backButton = (ImageButton)findViewById(R.id.backButton);
		//返回键
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
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
		//批量上架
		upbutton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String groupid="";
				for (String id : productAdapter.selectid)
				{
					groupid=groupid+","+id;
				}
				// TODO Auto-generated method stub
				if(isDisCount)
				{
					AllMethod("changestatus",groupid,"2",ConstantParamPhone.COUPON_MULIT);
				}
				else
				{
					AllMethod("changestatus",groupid,"2",ConstantParamPhone.GOODS_MULIT);
				}

			}
		});
		//批量下架
		downbutton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String groupid="";
				for (String id : productAdapter.selectid)
				{
					groupid=groupid+","+id;
				}
				// TODO Auto-generated method stub
				if(isDisCount)
				{
					AllMethod("changestatus",groupid,"1",ConstantParamPhone.COUPON_MULIT);
				}
				else
				{
					AllMethod("changestatus",groupid,"1",ConstantParamPhone.GOODS_MULIT);
				}
			}
		});
		//批量删除
		productAllDelectButton.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//productAdapter.getItem(productAdapter.selectid);

				String groupid="";
				for (String id : productAdapter.selectid)
				{
					groupid=groupid+","+id;
				}

				if(isDisCount)
				{
					AllMethod("delete",groupid,"",ConstantParamPhone.DEL_BIG_BAND);
				}
				else
				{
					AllMethod("delete",groupid,"",ConstantParamPhone.DEL_BIG_BAND);
				}

				//Toast.makeText(ProductListActivity.this, groupid, Toast.LENGTH_SHORT).show();
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
				ColorDialog.showRoundProcessDialog(shopProductListActivity.this,R.layout.loading_process_dialog_color);
				if(isDisCount)
				{
					initData(ConstantParamPhone.GET_COUPON_LIST);
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
		//下拉刷新设置
		productSwipeLayout=(SwipeRefreshLayout) this.findViewById(R.id.productSwipeLayout);
		productSwipeLayout.setColorSchemeResources(R.color.text_color);
		productSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		productSwipeLayout.setProgressBackgroundColor(R.color.bg_color);
		productListView=(ListView) this.findViewById(R.id.productListView);

		//筛选器控件
		allType=(TextView) this.findViewById(R.id.allType);
		allTypeArror=(ImageView) this.findViewById(R.id.allTypeArror);
		orderType=(TextView) this.findViewById(R.id.orderType);
		orderTypeArror=(ImageView) this.findViewById(R.id.orderTypeArror);

		productTypeArror=(ImageView) this.findViewById(R.id.productTypeArror);
		selectLine=(LinearLayout) this.findViewById(R.id.selectLine);
		productRel=(RelativeLayout) this.findViewById(R.id.productRel);
		allTypeRel=(RelativeLayout) this.findViewById(R.id.allTypeRel);
		orderTypeRel=(RelativeLayout) this.findViewById(R.id.orderTypeRel);
		//productListView.addFooterView(footView);
		productListView.addFooterView(footView,null,false);
		productListView.setOnItemClickListener(new OnItemClickListener()
		{

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id)
			{
				// TODO Auto-generated method stub
				if(isDisCount)
				{
					Intent intent=new Intent(shopProductListActivity.this,ShopCouponDetailActivity.class);
					intent.putExtra("coupon", (ProductListBean)productAdapter.getItem(position));
					intent.putExtra("category", category);
					startActivityForResult(intent, 1);
				}
				else
				{
					if(isHongbao)
					{
						Intent intent=new Intent(shopProductListActivity.this,ShopHongBaoDetailActivity.class);
						intent.putExtra("hongbao", ((HongbaoListBean)productAdapter.getItem(position)));
						startActivityForResult(intent, 1);
					}
					else
					{
						Intent intent=new Intent(shopProductListActivity.this,ShopProductDetailActivity.class);
						intent.putExtra("coupon", ((ProductListBean)productAdapter.getItem(position)));
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
			public void onScrollStateChanged(AbsListView view, int scrollState)
			{
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE&&lastItemIndex == productAdapter.getCount()) // productAdapter.getCount()记录的是数据的长度
				{
					//Log.i(TAG, "onScrollStateChanged");
					page++;
					//设置刷新方式 下拉刷新
					reftype=2;
					if(!reffinish)
					{
						if(isDisCount)
						{
							//优惠券列表
							initData(ConstantParamPhone.GET_COUPON_LIST);
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
								//大牌抢购页面
								initData(ConstantParamPhone.GET_BIG_BAND_LIST);
							}
						}
					}
					//设置最后一个条目
					productListView.setSelection(lastItemIndex-1);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) //totalItemCount记录的是整个listView的长度
			{
				// TODO Auto-generated method stub
				lastItemIndex = firstVisibleItem + visibleItemCount -1;
			}
		});
		//弹出分类选择事件颜色变化
		allTypeRel.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				allType.setTextColor(allType.getResources().getColor(R.color.text_color));
				allTypeArror.setImageResource(R.drawable.icon_jiantou_shang);
				orderType.setTextColor(orderType.getResources().getColor(R.color.text_color_gray));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				productAction.setTextColor(getResources().getColor(R.color.text_color_gray));
				productTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				//showSelectPopupWindow();
			}

		});
		//弹出分类选择事件颜色变化
		orderTypeRel.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				orderType.setTextColor(getResources().getColor(R.color.text_green_color));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_shang);
				allType.setTextColor(getResources().getColor(R.color.text_color_gray));
				allTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
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
				productAction.setTextColor(getResources().getColor(R.color.text_green_color));
				productTypeArror.setImageResource(R.drawable.icon_jiantou_shang);
				orderType.setTextColor(getResources().getColor(R.color.text_color_gray));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				orderType.setTextColor(getResources().getColor(R.color.text_color_gray));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				showSelectPopupWindow("");

			}
		});
		//下拉刷新事件
		productSwipeLayout.setOnRefreshListener(new OnRefreshListener()
		{

			@Override
			public void onRefresh()
			{
				// TODO Auto-generated method stub
				page=1;
				reftype=1;
				reffinish=false;
				productAdapter.clear();
				if(isDisCount)
				{
					//优惠券列表
					initData(ConstantParamPhone.GET_COUPON_LIST);
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
						//大牌产品列表
						initData(ConstantParamPhone.GET_BIG_BAND_LIST);
					}
				}
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
					tv_chekcAll_product_list.setVisibility(View.GONE);
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
					productAllCheckImg.setVisibility(View.VISIBLE);
					//删除按钮
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
					productAllCheckImg.setVisibility(View.GONE);
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
	//弹出选择框poupwindow
	private void showSelectPopupWindow(final String action)
	{
		LayoutInflater layoutInflater = (LayoutInflater)shopProductListActivity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.activity_product_window_select, null);
		popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,  LayoutParams.MATCH_PARENT);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(true);

		//这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		int screenWidth=shopProductListActivity.this.getWindowManager().getDefaultDisplay().getWidth();
		int popWidth=popupWindow.getWidth();
		popupWindow.showAsDropDown(selectLine);
		ImageView closeImageButton = (ImageView) view.findViewById(R.id.closeImageButton);
		closeImageButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				popupWindow.dismiss();
			}
		});
		//下拉的列表
		ListView productSelectList=(ListView) view.findViewById(R.id.productSelectList);
		final List<DataBean> selectList=new ArrayList<DataBean>();
		if(action.equals("order"))
		{
			orderSelectorBean =new OrderSelectorAdapter(this);
			if(category.equals("hongbao"))
			//红包列表
			{
				DataBean DataBean=new DataBean();
				DataBean.setId("getmore");
				DataBean.setName("默认排序");
				selectList.add(DataBean);
				DataBean DataBean1=new DataBean();
				DataBean1.setId("getmore");
				DataBean1.setName("领取最多");
				selectList.add(DataBean1);
				DataBean DataBean2=new DataBean();
				DataBean2.setId("getless");
				DataBean2.setName("领取最少");
				selectList.add(DataBean2);
				DataBean DataBean3=new DataBean();
				DataBean3.setId("moremoney");
				DataBean3.setName("金额最多");
				selectList.add(DataBean3);
				DataBean DataBean4=new DataBean();
				DataBean4.setId("lessmoney");
				DataBean4.setName("金额最少");
				selectList.add(DataBean4);
			}
			else
			{
				//商品列表

				DataBean DataBean=new DataBean();
				DataBean.setId("default");
				DataBean.setName("默认排序");
				selectList.add(DataBean);
				DataBean DataBean1=new DataBean();
				DataBean1.setId("soldasc");
				DataBean1.setName("销量最低");
				selectList.add(DataBean1);
				DataBean DataBean2=new DataBean();
				DataBean2.setId("solddesc");
				DataBean2.setName("销量最高");
				selectList.add(DataBean2);
				DataBean DataBean3=new DataBean();
				DataBean3.setId("viewasc");
				DataBean3.setName("人气最低");
				selectList.add(DataBean3);
				DataBean DataBean4=new DataBean();
				DataBean4.setId("viewdesc");
				DataBean4.setName("人气最高");
				selectList.add(DataBean4);
			}
			//orderSelectorBean.addAll(selectList,orderType.getText().toString());
			productSelectList.setAdapter(orderSelectorBean);

		}
		else
		{
			orderSelectorBean =new OrderSelectorAdapter(this);
			if(category.equals("hongbao"))
			{
				DataBean DataBean6=new DataBean();
				DataBean6.setId("all");
				DataBean6.setName("所有");
				selectList.add(DataBean6);
				DataBean DataBean4=new DataBean();
				DataBean4.setId("ended");
				DataBean4.setName("已截止");
				selectList.add(DataBean4);
				DataBean DataBean5=new DataBean();
				DataBean5.setId("releasing");
				DataBean5.setName("发放中");
				selectList.add(DataBean5);
			}
			else
			{
				DataBean DataBean6=new DataBean();
				DataBean6.setId("0");
				DataBean6.setName("所有");
				selectList.add(DataBean6);
				DataBean DataBean4=new DataBean();
				DataBean4.setId("1");
				DataBean4.setName("未上架");
				selectList.add(DataBean4);
				DataBean DataBean5=new DataBean();
				DataBean5.setId("2");
				DataBean5.setName("已上架");
				selectList.add(DataBean5);
			}
			//orderSelectorBean.addAll(selectList,productAction.getText().toString());
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
					order=selectList.get(position).getId();
					orderType.setText(selectList.get(position).getName());
				}
				else
				{
					status=selectList.get(position).getId();
					productAction.setText(selectList.get(position).getName());
				}
				productAdapter=new ProductAdapter(shopProductListActivity.this,isMulChoice,isDisCount,isHongbao);
				ColorDialog.showRoundProcessDialog(shopProductListActivity.this,R.layout.loading_process_dialog_color);
				page=1;
				reffinish=false;
				if(isDisCount)
				{
					initData(ConstantParamPhone.GET_COUPON_LIST);
				}
				else
				{
					if(isHongbao)
					{
						initData(ConstantParamPhone.GET_HONGBAO_LIST);
					}
					else
					{
						initData(ConstantParamPhone.GET_PRODUCT_LIST);
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
				// TODO Auto-generated method stub
				//设置半透明
//			        WindowManager.LayoutParams params=getActivity().getWindow().getAttributes();
//			        params.alpha=1f;
//			        getActivity().getWindow().setAttributes(params);
				//重置所有按钮
				allType.setTextColor(shopProductListActivity.this.getResources().getColor(R.color.text_color_gray));
				allTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
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
		// TODO Auto-generated method stub
		page=1;
		//清空adapter数据
		productAdapter.clear();
		//loadMoreView.setVisibility(View.GONE);
		reffinish=false;
		ColorDialog.showRoundProcessDialog(this,R.layout.loading_process_dialog_color);
		if(isDisCount)
		{
			initData(ConstantParamPhone.GET_COUPON_LIST);
		}
		else
		{
			if(isHongbao)
			{
				initData(ConstantParamPhone.GET_HONGBAO_LIST);
			}
			else
			{
				initData(ConstantParamPhone.GET_PRODUCT_LIST);
			}
		}
	}
	private void AllMethod(final String action,String ids,final String statetype,String methord)
	{
		if(TextUtils.isEmpty(ids))
		{
			Toast.makeText(shopProductListActivity.this, "您尚未选中任何商品", Toast.LENGTH_SHORT).show();
			return;
		}
		ColorDialog.showRoundProcessDialog(shopProductListActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("id",ids);
		HttpUtils.getConnection(context,params,methord, "post",new TextHttpResponseHandler(){

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
						//数据请求成功
						Toast.makeText(context,"删除成功",Toast.LENGTH_SHORT).show();

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
				productSwipeLayout.setRefreshing(false);
				ColorDialog.dissmissProcessDialog();
			}
		});
	}
	@Override
	public void RightButtonClick()
	{
		// TODO Auto-generated method stub
		//显示添加界面
		// TODO Auto-generated method stub
		Intent intent=new Intent(shopProductListActivity.this,ShopHongBaoAddActivity.class);
		startActivityForResult(intent, 1);
	}

}
