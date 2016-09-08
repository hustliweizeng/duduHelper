package com.dudu.duduhelper;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.bean.ResponsBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.BigBandBuy;
import com.dudu.duduhelper.javabean.DiscountDeatailBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.dudu.duduhelper.widget.SwitchView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ShopProductAddActivity extends BaseActivity 
{
	private String id;
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
	
	private SwitchView productStatusSwitch;
	
	private LinearLayout productDetailLine;
	private TextView productDetaliTextView;
	
	private Button saveProductbutton;
	private EditText productSoldTextView;
	private String desprotion;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_product_add);
		initHeadView("详情编辑",true, false, 0);
		DuduHelperApplication.getInstance().addActivity(this);
		//页面类别category；
		category=getIntent().getStringExtra("category");
		LogUtil.d("Category",category);
		initView();
		initData();
	}

	private void initData() 
	{
		//接收传递过来的数据(说明进入编辑页面，否则是进入新增商品页面)
		BigBandBuy.DataBean data = (BigBandBuy.DataBean) getIntent().getSerializableExtra("productinfo");
		if (data ==null){
			return;
		}
		//设置页面信息
		productNameEditText.setText(data.getName());
		productSoldTextView.setText(data.getSaled_count());
		productYuanPriceEditText.setText(data.getPrice());
		productNowPriceEditText.setText(data.getCurrent_price());
		productKuCunNumEditText.setText(data.getStock());
		ed_explain.setText(data.getExplain());
		productDetaliTextView.setText(data.getExplain());
		//设置开始结束时间
		if (!TextUtils.isEmpty(data.getRule())){
			try {
				String begin = new JSONObject(data.getRule()).getString("begin");
				String end = new JSONObject(data.getRule()).getString("end");
				LogUtil.d("time","begin="+begin+"end="+end);
				if (category.equals("discount")){
					tv_startTime_shop_product.setText(data.getUpshelf());
					tv_endTime_shop_product.setText(data.getDownshelf());
					
				}else {
					tv_startTime_shop_product.setText(begin);
					tv_endTime_shop_product.setText(end);
				}
			}catch (Exception e){

			}
		}
		//设置图片
		if (data.getShow_img()!=null &&data.getShow_img().length>0){
			imgs = data.getShow_img();
			imageLoader.displayImage(data.getShow_img()[0],productImageView);
			tv_photo_num_shop_product.setText("相册有"+data.getShow_img().length+"张图片");
		}
		if (data.getPics()!=null & data.getPics().length >0){
			imgs = data.getPics();
			imageLoader.displayImage(data.getPics()[0],productImageView);
			tv_photo_num_shop_product.setText("相册有"+data.getPics().length+"张图片");
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
		if(TextUtils.isEmpty(productDetaliTextView.getText().toString().trim()))
		{
			Toast.makeText(ShopProductAddActivity.this, "请输入商品描述",Toast.LENGTH_SHORT).show();
			return;
		}
		ColorDialog.showRoundProcessDialog(ShopProductAddActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("id",id);
		params.add("category",category);
		params.add("subject",productNameEditText.getText().toString().trim());
		params.add("price1",productNowPriceEditText.getText().toString().trim());
		params.add("price2",productYuanPriceEditText.getText().toString().trim());
		params.add("stock",productKuCunNumEditText.getText().toString().trim());
		params.add("time_end",tv_startTime_shop_product.getText().toString().trim());
		params.add("description",productDetaliTextView.getText().toString().trim());
		params.add("version", ConstantParamPhone.VERSION);
		if(productStatusSwitch.getState()==1)
		{
			params.add("status","1");
		}
		else
		{
			params.add("status","2");
		}
        HttpUtils.getConnection(context,params,ConstantParamPhone.GET_BIG_BAND_DETAIL, "post",new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(ShopProductAddActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{

				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功


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

	private void initView() 
	{
		//点击添加图片
		shopImageView = (ImageView) this.findViewById(R.id.productImageView);
		//选择配送方式
		RadioGroup rg_shop_product_add = (RadioGroup) findViewById(R.id.rg_shop_product_add);
		ed_explain = (EditText) findViewById(R.id.ed_explain);
		final RadioButton rb1_shop_product_add = (RadioButton) findViewById(R.id.rb1_shop_product_add);
		final RadioButton rb2_shop_product_add = (RadioButton) findViewById(R.id.rb2_shop_product_add);
		//动态显示图片的数量
		tv_photo_num_shop_product = (TextView) findViewById(R.id.tv_photo_num_shop_product);
		//初始化选中状态
		rb1_shop_product_add.setChecked(true);
		rb1_shop_product_add.setTextColor(getResources().getColor(R.color.text_red_color));
		//设置按钮监听事件，应该是把事件加入到消息队列中了
		rg_shop_product_add.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
				//获取选中状态
				if (checkedId == R.id.rb1_shop_product_add){
					//动态改变文字颜色
					rb1_shop_product_add.setTextColor(getResources().getColor(R.color.text_red_color));
					rb2_shop_product_add.setTextColor(getResources().getColor(R.color.text_color_light));

				}else if(checkedId == R.id.rb2_shop_product_add){
					rb2_shop_product_add.setTextColor(getResources().getColor(R.color.text_red_color));
					rb1_shop_product_add.setTextColor(getResources().getColor(R.color.text_color_light));

				}
			}
		});


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
		ll_endTime_shop_product = (LinearLayout) this.findViewById(R.id.ll_startTime_shop_product);
		tv_endTime_shop_product =(TextView) this.findViewById(R.id.tv_startTime_shop_product);
		
		
		productDetailLine=(LinearLayout) this.findViewById(R.id.productDetailLine);
		productDetaliTextView=(TextView) this.findViewById(R.id.productDetaliTextView);
		//选择图片上传
		productImageView.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
			Intent intent = new Intent(ShopProductAddActivity.this,ShopImageViewBrower.class);
			//把网络数据传输过去
			ArrayList<String> list = new ArrayList<String>();
			for (String img:imgs){
				list.add(img);
			}
			//网络图片地址
			//传递集合过去
			intent.putStringArrayListExtra("imageList", list);
			intent.putExtra("type",1);
			//获取选中的图片
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
				showDataDialog(tv_startTime_shop_product);
			}
		});
		ll_endTime_shop_product.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDataDialog(tv_endTime_shop_product);
			}
		});
		
		saveProductbutton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				SubmitProduct();
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
		if (requestCode == 1 && resultCode == RESULT_OK) 
		{
			
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
}
