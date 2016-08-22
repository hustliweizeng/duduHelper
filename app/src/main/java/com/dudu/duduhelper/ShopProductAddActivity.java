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

import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.bean.ImageBean;
import com.dudu.duduhelper.bean.ResponsBean;
import com.dudu.duduhelper.common.Util;
import com.dudu.duduhelper.http.ConstantParamPhone;
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ShopProductAddActivity extends BaseActivity 
{
	//private SendShaiShaiAdapter sendShaiShaiAdapter;
	private String id;
	private String category;
	
	private EditText productNameEditText;
	
	private TextView productTypeTextView;
	
	private EditText productYuanPriceEditText;
	
	private EditText productNowPriceEditText;
	
	private EditText productKuCunNumEditText;
	
	private LinearLayout ll_startTime_shop_product;
	private TextView tv_startTime_shop_product;
	
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
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_product_add);
		initHeadView("详情编辑",true, false, 0);
		DuduHelperApplication.getInstance().addActivity(this);
		//商品id；
		id=getIntent().getStringExtra("id");
		//商品分类category；
		category=getIntent().getStringExtra("category");
		//sendShaiShaiAdapter=new SendShaiShaiAdapter(this);
		initView();
		initData();
	}

	private void initData() 
	{
		// TODO Auto-generated method stub
		ColorDialog.showRoundProcessDialog(ShopProductAddActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("category",category);
		params.add("id",id);
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(ShopProductAddActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.GET_PRODUCT_INFO, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(ShopProductAddActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				ProductDetailBean productDetailBean=new Gson().fromJson(arg2,ProductDetailBean.class);
				if(productDetailBean.getStatus().equals("-1006"))
				{
					//Toast.makeText(ProductListActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
					//Toast.makeText(getActivity(), "出错啦！", Toast.LENGTH_SHORT).show();
					MyDialog.showDialog(ShopProductAddActivity.this, "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(ShopProductAddActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				if(!productDetailBean.getStatus().equals("1"))
				{
					Toast.makeText(ShopProductAddActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
				}
				else
				{
					if(productDetailBean.getData()!=null)
					{
						productNameEditText.setText(productDetailBean.getData().getSubject());
						productSoldTextView.setText(productDetailBean.getData().getSold());
						productYuanPriceEditText.setText(productDetailBean.getData().getPrice2());
						productNowPriceEditText.setText(productDetailBean.getData().getPrice1());
						productKuCunNumEditText.setText(productDetailBean.getData().getStock());
						tv_startTime_shop_product.setText(Util.DataConVertMint(productDetailBean.getData().getTime_end()));
						productDetaliTextView.setText(productDetailBean.getData().getDescription());
						if(productDetailBean.getData().getStatus().equals("1"))
						{
							productStatusSwitch.setState(false);
						}
						else
						{
							productStatusSwitch.setState(true);
						}
						//利用UIL加载数据
						ImageAware imageAware = new ImageViewAware(productImageView, false);
						imageLoader.displayImage(productDetailBean.getData().getThumb(), imageAware);
					}
					else
					{
						Toast.makeText(ShopProductAddActivity.this, "暂无数据！", Toast.LENGTH_SHORT).show();
					}
				}
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				ColorDialog.dissmissProcessDialog();
			}
		});
	}
	
	private void SubmitProduct() 
	{
		// TODO Auto-generated method stub
		
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
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(ShopProductAddActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.post(ConstantParamPhone.IP+ConstantParamPhone.EDIT_PRODUCT_INFO, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(ShopProductAddActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				ResponsBean responsBean=new Gson().fromJson(arg2,ResponsBean.class);
				if(!responsBean.getStatus().equals("1"))
				{
					Toast.makeText(ShopProductAddActivity.this, responsBean.getInfo(), Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(ShopProductAddActivity.this, "修改成功啦", Toast.LENGTH_SHORT).show();
					Intent intent=new Intent();  
	                setResult(RESULT_OK, intent);  
	                finish();
				}
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
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
		final RadioButton rb1_shop_product_add = (RadioButton) findViewById(R.id.rb1_shop_product_add);
		final RadioButton rb2_shop_product_add = (RadioButton) findViewById(R.id.rb2_shop_product_add);
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
		ll_startTime_shop_product=(LinearLayout) this.findViewById(R.id.ll_startTime_shop_product);
		tv_startTime_shop_product=(TextView) this.findViewById(R.id.tv_startTime_shop_product);
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
				//添加本地图片
				List<ImageBean> list = new ArrayList<ImageBean>();
				ImageBean imageBean1 = new ImageBean();
				ImageBean imageBean2 = new ImageBean();
				ImageBean imageBean3 = new ImageBean();
				ImageBean imageBean4 = new ImageBean();
				imageBean1.setPath("http://file.duduapp.net/uploads/default/42/d0/42d0019eb35f21976a5c463bdc1bb03d.jpg");
				imageBean2.setPath("http://file.duduapp.net/98/6e/986e51a7ba15dc3ace6dde27dd1df98b.png");
				imageBean3.setPath("http://file.duduapp.net/uploads/default/6a/81/6a814954b66a73ac70616aea91fc5bc1.jpg");
				imageBean4.setPath("http://file.duduapp.net/uploads/default/85/0d/850dccbe4e99b2a32a45c4b8c7b8d9bf.jpg");
				list.add(imageBean1);
				list.add(imageBean2);
				list.add(imageBean3);
				list.add(imageBean4);
				//传递集合过去
				intent.putExtra("imageList", (Serializable)list);
				//获取选中的图片
				startActivityForResult(intent, 1);
			}
		});
		productDetailLine.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
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
				// TODO Auto-generated method stub
				//弹出时间对话框
				showDataDialog(tv_startTime_shop_product);
			}
		});
		
		saveProductbutton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				SubmitProduct();
			}
		});
		
		if(category.equals("buying"))
		{
			productTypeTextView.setText("大牌抢购");
		}
		if(category.equals("coupon"))
		{
			productTypeTextView.setText("优惠券");
		}
		if(category.equals("goods"))
		{
			productTypeTextView.setText("店铺商品");
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
}
