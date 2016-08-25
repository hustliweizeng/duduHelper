package com.dudu.duduhelper;



import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.Header;
import org.fireking.app.imagelib.entity.ImageBean;

import com.dudu.duduhelper.adapter.SendShaiShaiAdapter;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.bean.CouponDetailBean;
import com.dudu.duduhelper.bean.ResponsBean;
import com.dudu.duduhelper.Utils.Util;
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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class CouponAddActivity extends BaseActivity 
{
	private SendShaiShaiAdapter sendShaiShaiAdapter;
	private String id;
	private String category;
	
	private EditText couponNameEditText;
	
	private EditText couponTagNameEditText;
	
	private EditText couponYuanPriceEditText;
	
	private EditText couponNowPriceEditText;
	
	private EditText couponKuCunNumEditText;
	
	private EditText couponSoldTextView;
	
	private LinearLayout couponUpTimeLin;
	
	private LinearLayout couponDownTimeLin;
	
	private TextView couponDownTimeTextView;
	
	private TextView couponUpTimeTextView;
	
	private SwitchView couponStatusSwitch;
	
	private LinearLayout productDetailLine;
	private TextView productDetaliTextView;
	private EditText couponPriceEditText;
	private Button saveProductbutton;
	private String desprotion;
	private ImageView productImageView;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	//用来拼接日期和时间，最终用来显示的
	private StringBuilder datastr = new StringBuilder("");
    private int flag=0;
    private int flag1=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coupon_add);
		initHeadView("修改优惠券",true, false, 0);
		DuduHelperApplication.getInstance().addActivity(this);
		//商品id；
		id=getIntent().getStringExtra("id");
		//商品分类category；
		category=getIntent().getStringExtra("category");
		sendShaiShaiAdapter=new SendShaiShaiAdapter(this);
		initView();
		initData();
	}

	private void initData() 
	{
		// TODO Auto-generated method stub
		ColorDialog.showRoundProcessDialog(CouponAddActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		//params.add("category",category);
		params.add("id",id);
		params.add("version", ConstantParamPhone.VERSION);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(CouponAddActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.get(ConstantParamPhone.IP+ConstantParamPhone.GET_COUPON_INFO, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(CouponAddActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				CouponDetailBean productDetailBean=new Gson().fromJson(arg2,CouponDetailBean.class);
				if(productDetailBean.getStatus().equals("-1006"))
				{
					//Toast.makeText(ProductListActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
					//Toast.makeText(getActivity(), "出错啦！", Toast.LENGTH_SHORT).show();
					MyDialog.showDialog(CouponAddActivity.this, "该账号已在其他手机登录，是否重新登录", true, true, "取消", "确定",new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							MyDialog.cancel();
						}
					}, new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent=new Intent(CouponAddActivity.this,LoginActivity.class);
							startActivity(intent);
						}
					});
				}
				if(!productDetailBean.getStatus().equals("1"))
				{
					Toast.makeText(CouponAddActivity.this, "出错啦！", Toast.LENGTH_SHORT).show();
				}
				else
				{
					if(productDetailBean.getData()!=null)
					{
						couponPriceEditText.setText(productDetailBean.getData().getFee());
						couponNameEditText.setText(productDetailBean.getData().getSubject());
						couponTagNameEditText.setText(productDetailBean.getData().getTag());
						couponYuanPriceEditText.setText(productDetailBean.getData().getPrice2());
						couponNowPriceEditText.setText(productDetailBean.getData().getPrice1());
						couponKuCunNumEditText.setText(productDetailBean.getData().getStock());
						couponSoldTextView.setText(productDetailBean.getData().getGet_limit());
						couponUpTimeTextView.setText(Util.DataConVertMint(productDetailBean.getData().getTime_start()));
						couponDownTimeTextView.setText(Util.DataConVertMint(productDetailBean.getData().getTime_end()));
						if(productDetailBean.getData().getStatus().equals("1"))
						{
							couponStatusSwitch.setState(false);
						}
						else
						{
							couponStatusSwitch.setState(true);
						}
						ImageAware imageAware = new ImageViewAware(productImageView, false);
						imageLoader.displayImage(productDetailBean.getData().getPic(), imageAware);
					}
					else
					{
						Toast.makeText(CouponAddActivity.this, "暂无数据！", Toast.LENGTH_SHORT).show();
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
		if(TextUtils.isEmpty(couponNameEditText.getText().toString().trim()))
		{
			Toast.makeText(CouponAddActivity.this, "请输入优惠券名称",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(couponTagNameEditText.getText().toString().trim()))
		{
			Toast.makeText(CouponAddActivity.this, "请输入优惠券副标题",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(couponPriceEditText.getText().toString().trim()))
		{
			Toast.makeText(CouponAddActivity.this, "请输在线支付价格",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(couponYuanPriceEditText.getText().toString().trim()))
		{
			Toast.makeText(CouponAddActivity.this, "请输入原价",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(couponNowPriceEditText.getText().toString().trim()))
		{
			Toast.makeText(CouponAddActivity.this, "请输入优惠价",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(couponKuCunNumEditText.getText().toString().trim()))
		{
			Toast.makeText(CouponAddActivity.this, "请输入发放数量",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(couponSoldTextView.getText().toString().trim()))
		{
			Toast.makeText(CouponAddActivity.this, "请输入单人限领数量",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(couponUpTimeTextView.getText().toString().trim()))
		{
			Toast.makeText(CouponAddActivity.this, "请输入上架时间",Toast.LENGTH_SHORT).show();
			return;
		}
		if(TextUtils.isEmpty(couponDownTimeTextView.getText().toString().trim()))
		{
			Toast.makeText(CouponAddActivity.this, "请输入下架时间",Toast.LENGTH_SHORT).show();
			return;
		}
//		
//		if(TextUtils.isEmpty(couponDownTimeTextView.getText().toString().trim()))
//		{
//			Toast.makeText(CouponAddActivity.this, "请输入下架时间",Toast.LENGTH_SHORT).show();
//			return;
//		}
		ColorDialog.showRoundProcessDialog(CouponAddActivity.this,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("id",id);
		params.add("fee",couponPriceEditText.getText().toString().trim());
		params.add("subject",couponNameEditText.getText().toString().trim());
		params.add("tag",couponTagNameEditText.getText().toString().trim());
		params.add("stock",couponKuCunNumEditText.getText().toString().trim());
		params.add("price2",couponYuanPriceEditText.getText().toString().trim());
		params.add("price1",couponNowPriceEditText.getText().toString().trim());
		params.add("time_start",couponUpTimeTextView.getText().toString().trim());
		params.add("time_end",couponDownTimeTextView.getText().toString().trim());
		params.add("get_limit",couponSoldTextView.getText().toString().trim());
		params.add("version", ConstantParamPhone.VERSION);
		//params.add("description",productDetaliTextView.getText().toString().trim());
		if(couponStatusSwitch.getState()==1)
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
        PersistentCookieStore myCookieStore = new PersistentCookieStore(CouponAddActivity.this);    
        client.setCookieStore(myCookieStore); 
        client.post(ConstantParamPhone.IP+ConstantParamPhone.EDIT_COUPON_INFO, params,new TextHttpResponseHandler(){

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				Toast.makeText(CouponAddActivity.this, "网络不给力呀", Toast.LENGTH_SHORT).show();
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				ResponsBean responsBean=new Gson().fromJson(arg2,ResponsBean.class);
				if(!responsBean.getStatus().equals("1"))
				{
					Toast.makeText(CouponAddActivity.this, responsBean.getInfo(), Toast.LENGTH_SHORT).show();
				}
				else
				{
					Toast.makeText(CouponAddActivity.this, "修改成功啦", Toast.LENGTH_SHORT).show();
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
		// TODO Auto-generated method stub
		couponPriceEditText=(EditText) this.findViewById(R.id.couponPriceEditText);
		productImageView=(ImageView) this.findViewById(R.id.productImageView);
		saveProductbutton=(Button) this.findViewById(R.id.saveProductbutton);
		couponNameEditText=(EditText) this.findViewById(R.id.couponNameEditText);
		couponTagNameEditText=(EditText) this.findViewById(R.id.couponTagNameEditText);
		couponYuanPriceEditText=(EditText) this.findViewById(R.id.couponYuanPriceEditText);
		couponNowPriceEditText=(EditText) this.findViewById(R.id.couponNowPriceEditText);
		couponKuCunNumEditText=(EditText) this.findViewById(R.id.couponKuCunNumEditText);
		couponSoldTextView=(EditText) this.findViewById(R.id.couponSoldTextView);
		couponUpTimeLin=(LinearLayout) this.findViewById(R.id.couponUpTimeLin);
		couponDownTimeLin=(LinearLayout) this.findViewById(R.id.couponDownTimeLin);
		couponDownTimeTextView=(TextView) this.findViewById(R.id.couponDownTimeTextView);
		couponUpTimeTextView=(TextView) this.findViewById(R.id.couponUpTimeTextView);
		couponStatusSwitch=(SwitchView) this.findViewById(R.id.couponStatusSwitch);
		productDetailLine=(LinearLayout) this.findViewById(R.id.productDetailLine);
		productDetaliTextView=(TextView) this.findViewById(R.id.productDetaliTextView);
		productDetailLine.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//弹出编辑产品详情页面
				Intent intent = new Intent(CouponAddActivity.this, ProductInfoEditActivity.class);
				intent.putExtra("content", productDetaliTextView.getText().toString());
				startActivityForResult(intent, 1);
			}
		});
		couponDownTimeLin.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//弹出时间对话框
				showDataDialog(couponDownTimeTextView);
			}
		});
		
		couponUpTimeLin.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				//弹出时间对话框
				showDataDialog(couponUpTimeTextView);
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
		
//		if(category.equals("buying"))
//		{
//			productTypeTextView.setText("大牌抢购");
//		}
//		if(category.equals("coupon"))
//		{
//			productTypeTextView.setText("优惠券");
//		}
//		if(category.equals("goods"))
//		{
//			productTypeTextView.setText("店铺商品");
//		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		if (requestCode == 0x123 && resultCode == RESULT_OK) 
		{
			sendShaiShaiAdapter.addAll((List<ImageBean>) data.getSerializableExtra("images"));
		}
		if (requestCode == 1 && resultCode == RESULT_OK) 
		{
			productDetaliTextView.setText(data.getStringExtra("descrip"));
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
    	 DatePickerDialog datePicker=new DatePickerDialog(CouponAddActivity.this,AlertDialog.THEME_HOLO_LIGHT, new OnDateSetListener() 
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
