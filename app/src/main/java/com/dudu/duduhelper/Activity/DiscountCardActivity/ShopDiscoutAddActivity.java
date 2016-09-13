package com.dudu.duduhelper.Activity.DiscountCardActivity;


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
import android.text.TextUtils;
import android.util.Base64;
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

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Activity.BigBandActivity.ProductInfoEditActivity;
import com.dudu.duduhelper.Activity.ShopImageViewBrower;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.BigBandBuy;
import com.dudu.duduhelper.javabean.DiscountDeatailBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.SwitchView;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ShopDiscoutAddActivity extends BaseActivity 
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
	private String[] imgs;
	private ArrayList<String> uplodImgs;
	private String[] picsPath;
	private ArrayList<String> listSource;
	private int subThreadCount;
	private Handler handler = new Handler(){
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

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_discount_add);
		initHeadView("修改优惠券",true, false, 0);
		DuduHelperApplication.getInstance().addActivity(this);
		//页面类别category；
		initView();
		initData();
	}

	private void initData() 
	{
		BigBandBuy.DataBean data = (BigBandBuy.DataBean) getIntent().getSerializableExtra("productinfo");
		if (data.getPics()!=null &&data.getPics().length>0 ){
			imgs = data.getPics();
			tv_photo_num_shop_product.setText("相册有"+imgs.length+"张图片");
			imageLoader.displayImage(data.getPics()[0],productImageView);
		}else {
			
		}
		
		//设置页面信息
		productNameEditText.setText(data.getName());
		productSoldTextView.setText(data.getSold());
		productYuanPriceEditText.setText(data.getPrice());
		productNowPriceEditText.setText(data.getCurrent_price());
		productKuCunNumEditText.setText(data.getStock());
		tv_startTime_shop_product.setText(data.getUpshelf());
		tv_endTime_shop_product.setText(data.getDownshelf());
		productDetaliTextView.setText(data.getExplain());
		
	}
					
	//提交修改的信息
	private void SubmitProduct() 
	{
		
		if(TextUtils.isEmpty(productNameEditText.getText().toString().trim()))
		{
			Toast.makeText(ShopDiscoutAddActivity.this, "请输入商品名称",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(productYuanPriceEditText.getText().toString().trim()))
		{
			Toast.makeText(ShopDiscoutAddActivity.this, "请输入商品原价",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(productNowPriceEditText.getText().toString().trim()))
		{
			Toast.makeText(ShopDiscoutAddActivity.this, "请输入商品现价",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(productKuCunNumEditText.getText().toString().trim()))
		{
			Toast.makeText(ShopDiscoutAddActivity.this, "请输入商品库存",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(tv_startTime_shop_product.getText().toString().trim()))
		{
			Toast.makeText(ShopDiscoutAddActivity.this, "请输入商品上架时间",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(tv_endTime_shop_product.getText().toString().trim()))
		{
			Toast.makeText(ShopDiscoutAddActivity.this, "请输入商品下架时间",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(productDetaliTextView.getText().toString().trim()))
		{
			Toast.makeText(ShopDiscoutAddActivity.this, "请输入商品描述",Toast.LENGTH_SHORT).show();
			return;
		}
		DiscountDeatailBean.Data dataBean = new DiscountDeatailBean.Data();
		//封装数据
		dataBean.setName(productNameEditText.getText().toString().trim());
		dataBean.setCurrent_price(productNowPriceEditText.getText().toString().trim());
		dataBean.setPrice(productYuanPriceEditText.getText().toString().trim());
		dataBean.setStock(productKuCunNumEditText.getText().toString().trim());
		dataBean.setExplain(productDetaliTextView.getText().toString().trim());
		dataBean.setPics(picsPath);
		
		ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.put("op","save");
		params.add("id",id);
		//把数据封装成bean
		String panic_data=  new Gson().toJson(dataBean);
		params.put("panic_data",panic_data);
		HttpUtils.getConnection(context,params,ConstantParamPhone.GET_BIG_BAND_DETAIL, "post",new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3)
			{
				Toast.makeText(context, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2)
			{
				LogUtil.d("FIX",arg2);

				try {
					JSONObject object = new JSONObject(arg2);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						Toast.makeText(context,"上传成功",Toast.LENGTH_SHORT).show();
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
			@Override
			public void onFinish()
			{
				ColorDialog.dissmissProcessDialog();
			}
		});
	}

	private void initView() 
	{
		//弹窗提醒动画
		textToumingView = (TextView) this.findViewById(R.id.textToumingView);
		AlphaAnimation animation = new AlphaAnimation((float)1, (float)0);
		animation.setDuration(4000); //设置持续时间2秒  
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
		tv_photo_num_shop_product = (TextView) findViewById(R.id.tv_photo_num_shop_product);
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
			Intent intent = new Intent(context,ShopImageViewBrower.class);
			//把网络数据传输过去
			listSource = new ArrayList<String>();
			if(imgs!=null){
				for (String img:imgs){
					listSource.add(img);
				}
			}
			intent.putStringArrayListExtra("imageList", listSource);
			intent.putExtra("type",10);
			startActivityForResult(intent, 10);

			}
		});
		productDetailLine.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
			//弹出编辑产品详情页面
			Intent intent = new Intent(ShopDiscoutAddActivity.this, ProductInfoEditActivity.class);
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
		productTypeTextView.setText("优惠券");
		
	}
	//获取裁剪的图片
	public void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		if (requestCode == 10 && resultCode == RESULT_OK)
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
    	 DatePickerDialog datePicker=new DatePickerDialog(ShopDiscoutAddActivity.this,AlertDialog.THEME_HOLO_LIGHT, new OnDateSetListener() 
    	 {
             @Override
             public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) 
             {
                 // TODO Auto-generated method stub
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
