package com.dudu.duduhelper.Activity.RedBagActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.adapter.ShopListSelectAdapter;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.RedbagDetailBean;
import com.dudu.duduhelper.javabean.ShopListBean;
import com.dudu.duduhelper.widget.OverScrollView;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.dudu.duduhelper.R;
public class ShopHongBaoAddActivity extends BaseActivity {

	private EditText hongbaoNameEditText;
	private EditText hongbaoPrice;
	private EditText hongbaonum;
	private EditText hongbaoDownPrice;
	private EditText hongbaoUpPrice;
	private LinearLayout hongbaostarTimeLin;
	private LinearLayout hongbaoendTimeLin;
	private TextView hongbaoEndTimeTextView;
	private TextView hongbaoStartTimeTextView;
	private EditText hongbaodaynumEditText;
	private LinearLayout addListLine;

	private OverScrollView mineScrollview;

	private RedbagDetailBean.DataBean hongbaoBean;

	private Button saveProductbutton;
	private ImageView productImageView;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	// 用来拼接日期和时间，最终用来显示的
	private StringBuilder datastr = new StringBuilder("");
	private int flag = 0;
	private int flag1 = 0;
	private TextView textToumingView;
	private LinearLayout ll_view;
	private int position = 0;
	private EditText ed_rules;
	private LinearLayout ll_apply_shop;
	private ShopListBean ShopListData;
	private RedbagDetailBean bean;
	private ImageView iv_cancle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_hong_bao_add);
		adapter = new ShopListSelectAdapter(context, R.layout.item_circle_multi_select);
		initHeadView("修改商品", true, false, 0);
		DuduHelperApplication.getInstance().addActivity(this);
		initView();
		initData();
	}

	private void initView() 
	{
		
		ll_apply_shop = (LinearLayout) findViewById(R.id.ll_apply_shop);
		textToumingView = (TextView) this.findViewById(R.id.textToumingView);
		productImageView = (ImageView) this.findViewById(R.id.productImageView);
		saveProductbutton = (Button) this.findViewById(R.id.saveProductbutton);
		hongbaoNameEditText = (EditText) this.findViewById(R.id.hongbaoNameEditText);
		hongbaoPrice = (EditText) this.findViewById(R.id.hongbaoPrice);
		hongbaonum = (EditText) this.findViewById(R.id.hongbaonum);
		hongbaoDownPrice = (EditText) this.findViewById(R.id.hongbaoDownPrice);
		hongbaoUpPrice = (EditText) this.findViewById(R.id.hongbaoUpPrice);
		hongbaostarTimeLin = (LinearLayout) this.findViewById(R.id.hongbaostarTimeLin);
		hongbaoendTimeLin = (LinearLayout) this.findViewById(R.id.hongbaoendTimeLin);
		hongbaoEndTimeTextView = (TextView) this.findViewById(R.id.hongbaoEndTimeTextView);
		hongbaoStartTimeTextView = (TextView) this.findViewById(R.id.hongbaoStartTimeTextView);
		hongbaodaynumEditText = (EditText) this.findViewById(R.id.hongbaodaynumEditText);
		addListLine = (LinearLayout) this.findViewById(R.id.addListLine);
		mineScrollview = (OverScrollView) this.findViewById(R.id.mineScrollview);
		textToumingView = (TextView) this.findViewById(R.id.textToumingView);
		ed_rules = (EditText) findViewById(R.id.ed_rules);
		ll_view = (LinearLayout) findViewById(R.id.ll_view);
		AlphaAnimation animation = new AlphaAnimation((float) 1, (float) 0);
		animation.setDuration(8000); // 设置持续时间2秒
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
				textToumingView.setVisibility(View.GONE);
			}
		});
		textToumingView.startAnimation(animation);
		
		addListLine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				createConditon("","");
			}
		});

		hongbaostarTimeLin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 弹出时间对话框
				showDataDialog(hongbaoStartTimeTextView);
			}
		});

		hongbaoendTimeLin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 弹出时间对话框
				showDataDialog(hongbaoEndTimeTextView);
			}
		});

		saveProductbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
					SubmitProduct();
			}
		});
		ll_apply_shop.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				getShopListData();//获取店铺列表信息
			}
		});
	}

	//获取店铺列表数据
	private void getShopListData()
	{
		HttpUtils.getConnection(context,null, ConstantParamPhone.GET_SHOP_LIST, "GET",new TextHttpResponseHandler()
		{
			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3)
			{
				Toast.makeText(context, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2)
			{
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						ShopListData = new Gson().fromJson(arg2, ShopListBean.class);
						//防止多次加入数据，先清空原有数据
						adapter.clear();
						adapter.addAll(ShopListData.getData());
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
			public void onFinish() {
				super.onFinish();
				showShopListSelctor(ShopListData.getData(),"适用门店");
			}
		});
	}
	private ShopListSelectAdapter adapter;
	List<String> checkedIds = new ArrayList<>();

	/**
	 * 选择红包适用门店
	 * @param category
	 * @param title
	 */
	private void showShopListSelctor(final List<ShopListBean.DataBean> category, final String title) {
		//每次进入传递已选中的列表
		adapter.addCheckedIds(category,checkedIds,true);
		final AlertDialog dailog = new AlertDialog.Builder(context).create();
		dailog.show();
		//获取window之前必须先show
		Window window = dailog.getWindow();
		window.setContentView(R.layout.alertdailog_multi_choose);
		TextView tv_title_alertdailog = (TextView) window.findViewById(R.id.tv_title_alertdailog);
		ListView lv_alertdailog = (ListView) window.findViewById(R.id.lv_alertdailog);
		Button iv_canle_alertdailog = (Button) window.findViewById(R.id.iv_canle_alertdailog);
		iv_cancle = (ImageView) window.findViewById(R.id.iv_cancle);
		tv_title_alertdailog.setText(title);
		lv_alertdailog.setAdapter(adapter);

		LogUtil.d("adapter", adapter.getCount()+"");
		//通过接口回调，确认选择的条目，并展示出来
		lv_alertdailog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String itemId = adapter.getItemId(position)+"";
				if (checkedIds.contains(itemId)){
					checkedIds.remove(itemId);
				}else {
					checkedIds.add(itemId);
				}
				adapter.setcheckedId(position+"");//设置位置
			}

		});
		iv_canle_alertdailog.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dailog.dismiss();
			}
		});
		iv_cancle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dailog.dismiss();
			}
		});
	}

	// 选择日期,调用系统主题
	@SuppressLint("InlinedApi")
	private void showDataDialog(final TextView textView) {
		flag = 0;
		flag1 = 0;
		datastr.delete(0, datastr.length());
		Calendar mycalendar = Calendar.getInstance(Locale.CHINA);
		Date mydate = new Date(); // 获取当前日期Date对象
		mycalendar.setTime(mydate);// //为Calendar对象设置时间为当前日期
		DatePickerDialog datePicker = new DatePickerDialog(
				ShopHongBaoAddActivity.this, AlertDialog.THEME_HOLO_LIGHT,
				new OnDateSetListener() {
					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// TODO Auto-generated method stub
						// Toast.makeText(RegisterActivity.this,
						// year+"year "+(monthOfYear+1)+"month "+dayOfMonth+"day",
						// Toast.LENGTH_SHORT).show();
						if (flag == 0) {
							flag = 1;
							datastr.append(year + "-" + (monthOfYear + 1) + "-"
									+ dayOfMonth + " ");
							showTimePickerDialog(textView);
						}
					}
				}, mycalendar.get(Calendar.YEAR),
				mycalendar.get(Calendar.MONTH),
				mycalendar.get(Calendar.DAY_OF_MONTH));
		datePicker.show();
	}

	@SuppressLint("InlinedApi")
	public void showTimePickerDialog(final TextView textView) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		TimePickerDialog dialog = new TimePickerDialog(this,
				AlertDialog.THEME_HOLO_LIGHT, new OnTimeSetListener() {

					@Override
					public void onTimeSet(TimePicker arg0, int hour, int minute) {
						if (flag1 == 0) {
							flag1 = 1;
							datastr.append(hour + ":" + minute + ":00");
							textView.setText(datastr);
						}
					}

				}, hour, minute, true);
		dialog.show();
	}

	private void initData() {
		//通过详情页面获取传递的数据
		bean = (RedbagDetailBean) getIntent().getSerializableExtra("data");
		if (bean ==null){
			Toast.makeText(context,"没有要获取到数据",Toast.LENGTH_SHORT).show();
			LogUtil.d("ok","获取到数据");
			return;
		}
		/**
		 * 设置已选中的条目
		 */
		//初始化获取已经设置的适用门店信息
		List<RedbagDetailBean.DataBean.Shopbean> apply_shops = bean.getData().getApply_shops();
		if (apply_shops!=null &&apply_shops.size()>0){
			for (RedbagDetailBean.DataBean.Shopbean item :apply_shops){
				if (!checkedIds.contains(item.getId())){
					checkedIds.add(item.getId());//把已知数据放到集合中
					LogUtil.d("init_add",item.getId());
				}
			}
		}
		hongbaoBean = bean.getData();
		hongbaoNameEditText.setText(hongbaoBean.getTitle());
		hongbaoPrice.setText(hongbaoBean.getTotal());
		hongbaonum.setText(hongbaoBean.getNum());
		hongbaoDownPrice.setText(hongbaoBean.getLower_money());
		hongbaoUpPrice.setText(hongbaoBean.getUpper_money());
		hongbaoStartTimeTextView.setText((hongbaoBean.getTime_start()));
		hongbaoEndTimeTextView.setText(hongbaoBean.getTime_end());
		hongbaodaynumEditText.setText(hongbaoBean.getLife());
		ed_rules.setText(hongbaoBean.getRules());
		//获取限制条件
		List<RedbagDetailBean.DataBean.LimitBean> limits = hongbaoBean.getLimit();
		if (limits!=null &&limits.size()>0){
			for (RedbagDetailBean.DataBean.LimitBean limitBean:limits)
			createConditon(limitBean.getPrice(),limitBean.getUsable());
		}
		//设置图片
		if (hongbaoBean.getLogo()!=null){
			ImageLoader.getInstance().displayImage(hongbaoBean.getLogo(),productImageView);
		}
		
	}

	private void SubmitProduct() {

		if (TextUtils.isEmpty(hongbaoNameEditText.getText().toString().trim())) {
			Toast.makeText(ShopHongBaoAddActivity.this, "请输入红包名称",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(hongbaoPrice.getText().toString().trim())) {
			Toast.makeText(ShopHongBaoAddActivity.this, "请输入发放总金额",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(hongbaonum.getText().toString().trim())) {
			Toast.makeText(ShopHongBaoAddActivity.this, "请输入发放总数量",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(hongbaoDownPrice.getText().toString().trim())) {
			Toast.makeText(ShopHongBaoAddActivity.this, "请输入金额下限",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(hongbaoUpPrice.getText().toString().trim())) {
			Toast.makeText(ShopHongBaoAddActivity.this, "请输入金额上限",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(hongbaoStartTimeTextView.getText().toString()
				.trim())) {
			Toast.makeText(ShopHongBaoAddActivity.this, "请输入开始时间",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils.isEmpty(hongbaoEndTimeTextView.getText().toString()
				.trim())) {
			Toast.makeText(ShopHongBaoAddActivity.this, "请输入截止时间",
					Toast.LENGTH_SHORT).show();
			return;
		}
		if (TextUtils
				.isEmpty(hongbaodaynumEditText.getText().toString().trim())) {
			Toast.makeText(ShopHongBaoAddActivity.this, "请输入有效天数",
					Toast.LENGTH_SHORT).show();
			return;
		}
	
		
		RequestParams params = new RequestParams();
		params.add("title",hongbaoNameEditText.getText().toString());
		params.put("time_start",hongbaoStartTimeTextView.getText().toString());
		params.put("time_end",hongbaoEndTimeTextView.getText().toString());
		params.put("num",hongbaonum.getText().toString().trim());
		params.put("total",hongbaoPrice.getText().toString().trim());
		params.put("lower_money",hongbaoDownPrice.getText().toString().trim());
		params.put("upper_money",hongbaoUpPrice.getText().toString().trim());
		params.put("life",hongbaodaynumEditText.getText().toString().trim());
		params.put("rules",ed_rules.getText().toString().trim());
		params.put("range[]","1");
		params.put("logo",sp.getString("shopLogo",""));
		params.put("image","232");
		if (checkedIds!=null &&checkedIds.size()>0){//设置选中的店铺
			params.put("apply_shops",checkedIds);
			LogUtil.d("apply_shops",checkedIds.toString());
		}
		//在condition中维护了一组集合条件
		Set<Map.Entry<Integer, LinearLayout>> items = conditions.entrySet();
		//转换为迭代器
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			LinearLayout val = (LinearLayout) entry.getValue();
			EditText ed_high = (EditText) val.findViewById(R.id.ed_high);
			EditText ed_low = (EditText) val.findViewById(R.id.ed_low);
			if (!TextUtils.isEmpty(ed_high.getText().toString().trim()) && !TextUtils.isEmpty(ed_low.getText().toString().trim())){
				params.put("limit[price][" + Float.parseFloat(ed_high.getText().toString().trim()) + "]", Float.parseFloat(ed_high.getText().toString().trim()));
				params.put("limit[usable][" + Float.parseFloat(ed_low.getText().toString().trim()) + "]", Float.parseFloat(ed_low.getText().toString().trim()));
			}
		}
		
		
		HttpUtils.getConnection(context, params, ConstantParamPhone.EDIT_REDBAG + hongbaoBean.getId(), "post", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						finish();

					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});
		
		
		
	}

	//1创建集合管理红包条件
	HashMap<Integer,LinearLayout> conditions = new HashMap<>();
	int itemId  =0;
	private void createConditon(String high,String low) {
		//每次载入红包条件的布局
		LinearLayout ll_limit = (LinearLayout) View.inflate(this, R.layout.item_condition_edit_redbag, null);
		ImageView iv_del_item = (ImageView) ll_limit.findViewById(R.id.iv_del_item);
		EditText ed_high = (EditText) ll_limit.findViewById(R.id.ed_high);
		EditText ed_low = (EditText) ll_limit.findViewById(R.id.ed_low);
		ed_high.setText(high);
		ed_low.setText(low);
		//给每个条目绑定一个key
		iv_del_item.setTag(itemId);
		iv_del_item.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//获取最外层的父对象
				int itemId = (int) v.getTag();
				//Toast.makeText(EditRedbag2Activity.this,"点击了"+itemId,Toast.LENGTH_SHORT).show();
				//删除指定位置的view
				ll_view.removeView(conditions.get(itemId));
				//删除指定的item对象
				conditions.remove(itemId);
				//记录position最新位置
				position--;
			}
		});
		//把当前对象保存到集合中
		conditions.put(itemId,ll_limit);
		ll_view.addView(ll_limit, position);
		//每次添加以后，位置会变动
		position++;
		itemId++;
		Log.d("condition", "创建了条件" + position);
	}

}
