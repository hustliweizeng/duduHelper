package com.dudu.duduhelper.Activity.BigBandActivity;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Activity.ShopImageViewBrower;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.BigBandBuy;
import com.google.gson.Gson;
import com.kyleduo.switchbutton.SwitchButton;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http  .Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import com.dudu.duduhelper.R;

public class ShopProductAddActivity extends BaseActivity
{
	private String category;

	private EditText productNameEditText;

	private TextView productTypeTextView;

	private EditText productYuanPriceEditText;

	private EditText productNowPriceEditText;

	private EditText productKuCunNumEditText;

	private LinearLayout ll_startTime_shop_product;
	private TextView tv_startTime_shop_product;

	private LinearLayout ll_endTime_shop_product;
	private TextView tv_endTime_shop_product;


	private LinearLayout productDetailLine;
	private TextView productDetaliTextView;

	private Button saveProductbutton;
	private EditText productSoldTextView;
	private ImageView productImageView;
	private TextView textToumingView;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	//用来拼接日期和时间，最终用来显示的
	private StringBuilder datastr = new StringBuilder("");
	private int flag=0;
	private int flag1=0;
	private ImageView shopImageView;
	private TextView tv_photo_num_shop_product;
	private EditText ed_explain;
	private String[] imgs;
	private ArrayList<String> uplodImgs;
	private String[] picsPath;
	private ArrayList<String> listSource;
	private int subThreadCount;
	private  Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			//把上传的url放入到数组中
			for (int i= 0;i<uplodImgs.size();i++){
				picsPath[i] = uplodImgs.get(i);
			}
			//修改数据源
			imgs =picsPath;
			//设置相册数量
			imageLoader.displayImage(picsPath[0],productImageView);
			tv_photo_num_shop_product.setText("相册有"+picsPath.length+"张图片");
			Toast.makeText(context,"上传完毕",Toast.LENGTH_SHORT).show();
		}
	};
	private BigBandBuy.DataBean data;
	private AlertDialog dailog1;
	private RadioButton rb1_shop_product_add;
	private RadioButton rb2_shop_product_add;
	private RadioGroup rg_shop_product_add;
	private ScrollView scroll_ed;
	private ImageView iv_select;
	private ArrayList<String> idses;
	private LinearLayout ll_select;
	private SwitchButton btn_vip;
	private EditText vip_price;
	private TextView tv_vip;
	private boolean isVipChecked;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_product_add);
		initHeadView("详情编辑",true, false, 0);
		//页面类别category；
		category=getIntent().getStringExtra("category");
		LogUtil.d("Category",category);
		initView();
		initData();
	}


	private void showAlertDailog() {
		
		//弹窗提醒
		dailog1 = new AlertDialog.Builder(context).create();
		dailog1.show();
		//获取window之前必须先show
		Window window = dailog1.getWindow();
		window.setContentView(R.layout.alertdailg_bigband);
		Button confirm = (Button) window.findViewById(R.id.confirm);
		Button canle = (Button) window.findViewById(R.id.cancel);
		TextView dis = (TextView) window.findViewById(R.id.disc);
		canle.setText("取消编辑");
		confirm.setText("继续提交");
		String status = data.getStatus();
		String is_on_sale = data.getIs_on_sale();
		LogUtil.d("tag",status);
		if ("0".equals(status)){
			//正在审核
			dis.setText("您的商品正在审核，变更信息将会导致商品下架,并需运营商重新审核方能上架出售!");
		}
		if ("1".equals(is_on_sale)){
			dis.setText("您的商品正在出售，变更信息将会导致商品下架,并需运营商重新审核方能上架出售!");
		}
		if ("0".equals(is_on_sale)){
			dis.setText("您的商品是下架状态,提交后运营商将进行审核，请耐心等待!");
		}
		//确定按钮
		confirm.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				RequestParams params  =new RequestParams();
				params.add("id",data.getId());

				HttpUtils.getConnection(context, params, ConstantParamPhone.SWITCH_STATUS, "post", new TextHttpResponseHandler() {
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
								dailog1.dismiss();
								SubmitProduct();
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
		});
		//取消按钮
		canle.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dailog1.dismiss();
				
			}
		});
	}
	private void initData()
	{
		//接收传递过来的数据(说明进入编辑页面，否则是进入新增商品页面)
		data = (BigBandBuy.DataBean) getIntent().getSerializableExtra("productinfo");
		if (data ==null){
			return;
		}
		//设置页面信息
		productNameEditText.setText(data.getName());
		productSoldTextView.setText(data.getAmount());
		productYuanPriceEditText.setText(data.getPrice());
		productKuCunNumEditText.setText(data.getStock());
		ed_explain.setText(Html.fromHtml(data.getExplain()));
		productDetaliTextView.setText(data.getExplain());
		//设置现价和vip价格
		try {
			JSONArray rule = new JSONArray(data.getRule());
			JSONObject time = rule.getJSONObject(0);
			String price = time.getString("price");
			productNowPriceEditText.setText(price);//现价
			String vipPrice = time.getString("vip_price");
			if ("1".equals(data.getIs_vip_price())){//说明有vip价格
				btn_vip.setChecked(true);
				vip_price.setText(vipPrice);
				vip_price.setTextColor(getResources().getColor(R.color.text_dark_color));
				tv_vip.setTextColor(getResources().getColor(R.color.text_dark_color));
			}else {//没有vip价格
				btn_vip.setChecked(false);
				tv_vip.setTextColor(getResources().getColor(R.color.text_color_light));
				vip_price.setTextColor(getResources().getColor(R.color.text_color_light));
			}
		}catch (Exception e){
			System.out.print(e);
		}


		
		//设置配送方式
		String delivery = data.getDelivery();
		LogUtil.d("deliver",delivery);
		//设置配送方式
		if ("1".equals(delivery)){
			rb2_shop_product_add.setChecked(true);
			rb2_shop_product_add.setTextColor(getResources().getColor(R.color.text_color_red));
		}else if ("2".equals(delivery)){
			rb1_shop_product_add.setChecked(true);
			rb1_shop_product_add.setTextColor(getResources().getColor(R.color.text_color_red));
		}

		//设置开始结束时间
		if (!TextUtils.isEmpty(data.getRule())){

			try {
				JSONArray array = new JSONArray(data.getRule());
				JSONObject time = array.getJSONObject(0);
				String begin = time.getString("begin");
				String end = time.getString("end");
				LogUtil.d("time","begin="+begin+"end="+end);
				if (category.equals("discount")){
					tv_startTime_shop_product.setText(data.getUpshelf());
					tv_endTime_shop_product.setText(data.getDownshelf());

				}else {
					tv_startTime_shop_product.setText(begin);
					tv_endTime_shop_product.setText(end);
				}
			}catch (Exception e){
				LogUtil.d("Jsonerro",e.toString());
			}
		}

		if(category.equals("discount")){

			if (data.getPics()!=null & data.getPics().length >0){
				imgs = data.getPics();
				imageLoader.displayImage(data.getPics()[0],productImageView);
				tv_photo_num_shop_product.setText("相册有"+ data.getPics().length+"张图片");
			}
		}else {
			//设置图片
			if (data.getShow_img()!=null && data.getShow_img().length>0){
				imgs = data.getShow_img();
				imageLoader.displayImage(data.getShow_img()[0],productImageView);
				tv_photo_num_shop_product.setText("相册有"+ data.getShow_img().length+"张图片");
			}
		}
	}




	//提交修改的信息
	private void SubmitProduct()
	{
		

		if(TextUtils.isEmpty(productNameEditText.getText().toString().trim()))
		{
			Toast.makeText(ShopProductAddActivity.this, "请输入商品名称",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(productYuanPriceEditText.getText().toString().trim()))
		{
			Toast.makeText(ShopProductAddActivity.this, "请输入商品原价",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(productNowPriceEditText.getText().toString().trim()))
		{
			Toast.makeText(ShopProductAddActivity.this, "请输入商品现价",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(productKuCunNumEditText.getText().toString().trim()))
		{
			Toast.makeText(ShopProductAddActivity.this, "请输入商品库存",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(productKuCunNumEditText.getText().toString().trim()))
		{
			Toast.makeText(ShopProductAddActivity.this, "请输入商品库存",Toast.LENGTH_SHORT).show();
			return;
		}
	
		if(TextUtils.isEmpty(productDetaliTextView.getText().toString().trim()))
		{
			Toast.makeText(ShopProductAddActivity.this, "请输入商品描述",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(tv_startTime_shop_product.getText().toString().trim()))
		{
			Toast.makeText(ShopProductAddActivity.this, "请输入商品上架时间",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(tv_endTime_shop_product.getText().toString().trim()))
		{
			Toast.makeText(ShopProductAddActivity.this, "请输入商品下架时间",Toast.LENGTH_SHORT).show();
			return;
		}
		if (isVipChecked){
			String vipPirce = vip_price.getText().toString().trim();
			if (TextUtils.isEmpty(vipPirce)){
				Toast.makeText(ShopProductAddActivity.this, "请输入会员价格",Toast.LENGTH_SHORT).show();
				return;
			}else {
				String price = productNowPriceEditText.getText().toString().trim();
				if (Float.parseFloat(price) < Float.parseFloat(vipPirce)){
					Toast.makeText(ShopProductAddActivity.this, "会员价格必须小于商品现价",Toast.LENGTH_SHORT).show();
					return;
				}
			}
		}
		
		

		BigBandBuy.DataBean dataBean = new BigBandBuy.DataBean();
		if (category.equals("discount")){

			dataBean.setName(productNameEditText.getText().toString().trim());
			//dataBean.setCurrent_price(productNowPriceEditText.getText().toString().trim());
			dataBean.setPrice(productYuanPriceEditText.getText().toString().trim());
			dataBean.setStock(productKuCunNumEditText.getText().toString().trim());
			dataBean.setExplain(productDetaliTextView.getText().toString().trim());
			dataBean.setPics(picsPath);
			dataBean.setAmount(productSoldTextView.getText().toString().trim());
			dataBean.setRule(data.getRule());
			dataBean.setShow_img(picsPath);
			//把id信息重新提交
			dataBean.setShop_id(data.getShop_id());
			dataBean.setApply_shops(dataBean.getApply_shops());
		}else {
			//大牌抢购
			dataBean.setName(productNameEditText.getText().toString().trim());
			//dataBean.setCurrent_price(productNowPriceEditText.getText().toString().trim());//废弃字段
			dataBean.setPrice(productYuanPriceEditText.getText().toString().trim());
			dataBean.setStock(productKuCunNumEditText.getText().toString().trim());
			dataBean.setExplain(productDetaliTextView.getText().toString().trim());
			dataBean.setAmount(productSoldTextView.getText().toString().trim());
			int checkedID = rg_shop_product_add.getCheckedRadioButtonId();//送货方式
			String delivery = (checkedID == R.id.rb1_shop_product_add ?"2":"1");
			LogUtil.d("des",delivery);
			dataBean.setDelivery(delivery);
			dataBean.setRule(data.getRule());//不修改上下架及价格
			/*
			*设置上下架及价格
			 */

			/**
			 * 修改上下架及价格的功能
			 */
			/*String currentPrice = productNowPriceEditText.getText().toString().trim();
			String startTime = tv_startTime_shop_product.getText().toString().trim();
			String endTime = tv_endTime_shop_product.getText() .toString().trim();
			JSONObject item = new JSONObject();
			try {
				item.put("begin",startTime);
				item.put("end",endTime);
				item.put("price",currentPrice);
				if (isVipChecked){
					item.put("vip_price",vip_price.getText().toString().trim());
					dataBean.setIs_vip_price("1");
				}else {
					item.put("vip_price","");
					dataBean.setIs_vip_price("0");
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
			JSONArray array = new JSONArray();
			array.put(item);
			LogUtil.d("array",array.toString());
			dataBean.setRule(array.toString());//设置规则内容*/
			//设置组图
			dataBean.setShow_img(picsPath);
			dataBean.setId(dataBean.getId());
			//把id信息重新提交
			dataBean.setShop_id(data.getShop_id());
			dataBean.setApply_shops(idses);//使用的商店

		}
		
		RequestParams params = new RequestParams();
		params.put("id",data.getId());
		params.put("op","save");
		//把数据封装成bean
		String panic_data=  new Gson().toJson(dataBean);
		LogUtil.d("data",panic_data);
		params.put("panic_data",panic_data);
		HttpUtils.getConnection(context,params,ConstantParamPhone.GET_BIG_BAND_DETAIL, "post",new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3)
			{
				Toast.makeText(ShopProductAddActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2)
			{
				LogUtil.d("res",arg2);
				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						Toast.makeText(context,"上传成功",Toast.LENGTH_SHORT).show();
						startActivity(new Intent(context,shopProductListActivity.class));
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

	private void initView()
	{
		/**
		 * vip价格
		 */
		btn_vip = (SwitchButton) findViewById(R.id.btn_vip);
		vip_price = (EditText) findViewById(R.id.vip_price);
		tv_vip = (TextView)findViewById(R.id.tv_vip);
		btn_vip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				isVipChecked = isChecked;
				
				if (isChecked){
					vip_price.setFocusable(true);
					tv_vip.setTextColor(getResources().getColor(R.color.text_dark_color));
					vip_price.setTextColor(getResources().getColor(R.color.text_dark_color));
					vip_price.setFocusable(true);
					vip_price.setFocusableInTouchMode(true);
				}else {
					vip_price.setFocusable(false);
					tv_vip.setTextColor(getResources().getColor(R.color.text_color_light));
					vip_price.setTextColor(getResources().getColor(R.color.text_color_light));
					vip_price.setFocusable(false);
					vip_price.setFocusableInTouchMode(false);
				}
			}
		});
		
		//点击添加图片
		shopImageView = (ImageView) this.findViewById(R.id.productImageView);
		//选择配送方式
		rg_shop_product_add = (RadioGroup) findViewById(R.id.rg_shop_product_add);
		rb1_shop_product_add = (RadioButton) findViewById(R.id.rb1_shop_product_add);
		rb2_shop_product_add = (RadioButton) findViewById(R.id.rb2_shop_product_add);
		ll_select = (LinearLayout) findViewById(R.id.ll_select);
		rg_shop_product_add.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				switch (checkedId){
					case R.id.rb1_shop_product_add:
						rb1_shop_product_add.setTextColor(getResources().getColor(R.color.text_red_color));
						rb2_shop_product_add.setTextColor(getResources().getColor(R.color.text_color_light));
						break;
					case R.id.rb2_shop_product_add:
						rb2_shop_product_add.setTextColor(getResources().getColor(R.color.text_red_color));
						rb1_shop_product_add.setTextColor(getResources().getColor(R.color.text_color_light));
						break;
				}
			}
		});
		ed_explain = (EditText) findViewById(R.id.ed_explain);
		//动态显示图片的数量
		tv_photo_num_shop_product = (TextView) findViewById(R.id.tv_photo_num_shop_product);
		shopImageView.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				// TODO Auto-generated method stub
				Intent intent = new Intent();
				startActivity(intent);
			}
		});
		//弹窗提醒动画
		textToumingView = (TextView) this.findViewById(R.id.textToumingView);
		AlphaAnimation animation = new AlphaAnimation((float)1, (float)0);
		animation.setDuration(4000); //设置持续时间2秒  
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
				textToumingView.setVisibility(View.GONE);
			}
		});
		textToumingView.startAnimation(animation);
		scroll_ed = (ScrollView) findViewById(R.id.scroll_ed);
		iv_select = (ImageView) findViewById(R.id.iv_select);
		ll_select.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {//传递已有的店铺
				Intent intent = new Intent(context, ShopSelectActivity.class);
				intent.putExtra("data",(Serializable) data.getApply_shops());
				startActivityForResult(intent,2);
			}
		});
		//编辑栏的滑动箭头
		productImageView=(ImageView) this.findViewById(R.id.productImageView);
		productSoldTextView=(EditText) this.findViewById(R.id.productSoldTextView);
		saveProductbutton=(Button) this.findViewById(R.id.saveProductbutton);
		productNameEditText=(EditText) this.findViewById(R.id.productNameEditText);
		productTypeTextView=(TextView) this.findViewById(R.id.productTypeTextView);
		productYuanPriceEditText=(EditText) this.findViewById(R.id.productYuanPriceEditText);
		productNowPriceEditText=(EditText) this.findViewById(R.id.productNowPriceEditText);
		productKuCunNumEditText=(EditText) this.findViewById(R.id.productKuCunNumEditText);
		//开始时间
		ll_startTime_shop_product=(LinearLayout) this.findViewById(R.id.ll_startTime_shop_product);
		tv_startTime_shop_product=(TextView) this.findViewById(R.id.tv_startTime_shop_product);
		//结束时间
		ll_endTime_shop_product = (LinearLayout) this.findViewById(R.id.ll_endTime_shop_product);
		tv_endTime_shop_product =(TextView) this.findViewById(R.id.tv_endTime_shop_product);


		productDetailLine=(LinearLayout) this.findViewById(R.id.productDetailLine);
		productDetaliTextView=(TextView) this.findViewById(R.id.productDetaliTextView);
		//选择图片上传
		productImageView.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{

				Intent intent = new Intent(ShopProductAddActivity.this,ShopImageViewBrower.class);
				//把网络数据传输过去
				listSource = new ArrayList<String>();
				if(imgs!=null){
					for (String img:imgs){
						listSource.add(img);
					}
				}
				intent.putStringArrayListExtra("imageList", listSource);
				intent.putExtra("type",1);
				startActivityForResult(intent, 1);

			}
		});
		productDetailLine.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//弹出编辑产品详情页面
				Intent intent = new Intent(ShopProductAddActivity.this, ProductInfoEditActivity.class);
				intent.putExtra("content", productDetaliTextView.getText().toString());
				startActivityForResult(intent, 1);
			}
		});
		ll_startTime_shop_product.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				//弹出时间对话框
				//showDataDialog(tv_startTime_shop_product);
			}
		});
		ll_endTime_shop_product.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//showDataDialog(tv_endTime_shop_product);
			}
		});

		saveProductbutton.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View v)
			{
				String is_on_sale = data.getIs_on_sale();
				LogUtil.d("status",data.getStatus());
				LogUtil.d("ison",data.getIs_on_sale());

				if ("2".equals(data.getStatus())){
					//审核失败时
					LogUtil.d("load","sds");
					SubmitProduct();
				}else {
					LogUtil.d("load","22");
					showAlertDailog();
				}
			}
		});

		if("bigband".equals(category))
		{
			productTypeTextView.setText("大牌抢购");
		}
		if("discount".equals(category))
		{
			productTypeTextView.setText("优惠券");
		}
		if("hongbao".equals(category))
		{
			productTypeTextView.setText("商家红包");
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if(resultCode == RESULT_OK){
			if (requestCode == 1 )
			{
				uplodImgs = (ArrayList<String>) data.getSerializableExtra("pics");
				picsPath = new String[uplodImgs.size()];
				//后台上传本地的图片
				for(String s:uplodImgs){
					if (!s.startsWith("http")){
						//这边是子线程上传，所以不会立即完成
						uploadImg(context, s);
						subThreadCount = 0;
						subThreadCount++;
					}
				}
			}
			if (requestCode == 2 ){
				idses = data.getStringArrayListExtra("ids");
				if (idses !=null && idses.size()>0){
					LogUtil.d("ides", idses.toString());
					Toast.makeText(context,"选择了"+idses.size()+"条目",Toast.LENGTH_SHORT).show();
				}
			}


		}
	}

	//选择日期,调用系统主题
	@SuppressLint("InlinedApi")
	private void showDataDialog(final TextView textView)
	{
		flag=0;
		flag1=0;
		datastr.delete(0,datastr.length());
		Calendar mycalendar=Calendar.getInstance(Locale.CHINA);
		Date mydate=new Date(); //获取当前日期Date对象
		mycalendar.setTime(mydate);////为Calendar对象设置时间为当前日期
		DatePickerDialog datePicker=new DatePickerDialog(ShopProductAddActivity.this,AlertDialog.THEME_HOLO_LIGHT, new OnDateSetListener()
		{
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth)
			{
				//Toast.makeText(RegisterActivity.this, year+"year "+(monthOfYear+1)+"month "+dayOfMonth+"day", Toast.LENGTH_SHORT).show();
				if(flag==0)
				{
					flag=1;
					datastr.append(year+"-"+(monthOfYear+1)+"-"+dayOfMonth+" ");
					showTimePickerDialog(textView);
				}
				//textView.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
			}
		},mycalendar.get(Calendar.YEAR),mycalendar.get(Calendar.MONTH), mycalendar.get(Calendar.DAY_OF_MONTH));
		datePicker.show();
	}
	@SuppressLint("InlinedApi")
	public void showTimePickerDialog(final TextView textView)
	{
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(System.currentTimeMillis());
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		TimePickerDialog dialog = new TimePickerDialog(this,AlertDialog.THEME_HOLO_LIGHT, new OnTimeSetListener() {

			@Override
			public void onTimeSet(TimePicker arg0, int hour, int minute)
			{
				if(flag1==0)
				{
					flag1=1;
					datastr.append(hour+":"+minute+":00");
					textView.setText(datastr);
				}
			}

		}, hour, minute, true);
		dialog.show();
	}
	/**
	 * imageUri 上传图片的本地uri地址
	 */
	public  void uploadImg(final Context context, final String imageUri) {
		//压缩图片再上传
		Bitmap img = BitmapFactory.decodeFile(imageUri);
		//字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//压缩
		img.compress(Bitmap.CompressFormat.JPEG,60,baos);
		//输出流转换为字节数组
		byte[] picByte  = baos.toByteArray();
		//转换为base64格式
		String picBase64 = Base64.encodeToString(picByte,1);

		//上传图片
		RequestParams params = new RequestParams();
		params.add("content",picBase64);
		HttpUtils.getConnection(context, params, ConstantParamPhone.UPLOAD_PIC, "post", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，请稍后再试",Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//保存到集合中
						uplodImgs.add(object.getString("url"));
						//移除本地地址
						uplodImgs.remove(imageUri);
						subThreadCount--;
						//说明上传完毕
						if (subThreadCount ==0){
							handler.sendEmptyMessage(0);
						}
						LogUtil.d("pic_success",s);
					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
						LogUtil.d("pic_fail",s);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});

	}
}
