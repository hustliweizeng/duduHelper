package com.dudu.duduhelper.Activity.RedBagActivity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.Header;
import org.fireking.app.imagelib.entity.ImageBean;
import org.json.JSONException;
import org.json.JSONObject;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.adapter.HongbaoAdapter;
import com.dudu.duduhelper.adapter.HongbaoAdapter.OnDelectHongBaoItemListener;
import com.dudu.duduhelper.adapter.SendShaiShaiAdapter;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.bean.HongbaoAddBean;
import com.dudu.duduhelper.bean.HongbaoListBean;
import com.dudu.duduhelper.bean.ResponsBean;
import com.dudu.duduhelper.bean.SubmitHongbaoBean;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.RedbagDetailBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.OverScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

public class ShopHongBaoAddActivity extends BaseActivity {
	private SendShaiShaiAdapter sendShaiShaiAdapter;
	private String id;
	private String category;

	private ListView hongbaoAddList;
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
	private LinearLayout referchHead;
	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private HongbaoAdapter hongbaoAdapter;
	// 用来拼接日期和时间，最终用来显示的
	private StringBuilder datastr = new StringBuilder("");
	private int flag = 0;
	private int flag1 = 0;
	private boolean isAdd = false;
	private List<ImageBean> imagesList = new ArrayList<ImageBean>();
	private TextView textToumingView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_hong_bao_add);
		initHeadView("修改商品", true, false, 0);
		DuduHelperApplication.getInstance().addActivity(this);
		initView();
		initData();
		
	}

	private void initView() 
	{
		// TODO Auto-generated method stub
		textToumingView = (TextView) this.findViewById(R.id.textToumingView);
		productImageView = (ImageView) this.findViewById(R.id.productImageView);
		saveProductbutton = (Button) this.findViewById(R.id.saveProductbutton);
		hongbaoAddList = (ListView) this.findViewById(R.id.hongbaoAddList);
		hongbaoAdapter = new HongbaoAdapter(this);
		hongbaoAddList.setAdapter(hongbaoAdapter);
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
		AlphaAnimation animation = new AlphaAnimation((float) 1, (float) 0);
		animation.setDuration(4000); // 设置持续时间2秒
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
				// TODO Auto-generated method stub
				textToumingView.setVisibility(View.GONE);
			}
		});
		textToumingView.startAnimation(animation);
		/**
		 * 设置回掉函数更新listView的高度
		 */
		hongbaoAdapter
				.setOnDelectHongBaoItemListener(new OnDelectHongBaoItemListener() {
					@Override
					public void onItemDelect() {
						// TODO Auto-generated method stub
						setListViewHeightBasedOnChildren(hongbaoAddList);
					}
				});
		addListLine.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				HongbaoAddBean hongbao = new HongbaoAddBean();
				hongbao.setTotalMoney("");
				hongbao.setDiscountMoney("");
				hongbaoAdapter.addHongbao(hongbao);
				setListViewHeightBasedOnChildren(hongbaoAddList);
			}
		});

		hongbaostarTimeLin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 弹出时间对话框
				showDataDialog(hongbaoStartTimeTextView);
			}
		});

		hongbaoendTimeLin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// 弹出时间对话框
				showDataDialog(hongbaoEndTimeTextView);
			}
		});

		saveProductbutton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
					SubmitProduct();
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
						// textView.setText(year+"-"+(monthOfYear+1)+"-"+dayOfMonth);
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

		RedbagDetailBean bean  = (RedbagDetailBean) getIntent().getSerializableExtra("data");
		hongbaoBean = bean.getData();
		hongbaoNameEditText.setText(hongbaoBean.getTitle());
		hongbaoPrice.setText(hongbaoBean.getTotal());
		hongbaonum.setText(hongbaoBean.getNum());
		hongbaoDownPrice.setText(hongbaoBean.getLower_money());
		hongbaoUpPrice.setText(hongbaoBean.getUpper_money());
		hongbaoStartTimeTextView.setText((hongbaoBean.getTime_start()));
		hongbaoEndTimeTextView.setText(Util.DataConVertMint(hongbaoBean.getTime_end()));
		hongbaodaynumEditText.setText(hongbaoBean.getLife());
		if (Integer.parseInt(hongbaoBean.getSend_num()) > 0) {
			hongbaoPrice.setTextColor(this.getResources().getColor(
					R.color.text_color_light));
			hongbaonum.setTextColor(this.getResources().getColor(
					R.color.text_color_light));
			hongbaoDownPrice.setTextColor(this.getResources().getColor(
					R.color.text_color_light));
			hongbaoUpPrice.setTextColor(this.getResources().getColor(
					R.color.text_color_light));
			hongbaoStartTimeTextView.setTextColor(this.getResources().getColor(
					R.color.text_color_light));
			hongbaodaynumEditText.setTextColor(this.getResources().getColor(
					R.color.text_color_light));
			hongbaoPrice.setEnabled(false);
			hongbaonum.setEnabled(false);
			hongbaoDownPrice.setEnabled(false);
			hongbaoUpPrice.setEnabled(false);
			hongbaoStartTimeTextView.setEnabled(false);
			hongbaodaynumEditText.setEnabled(false);
			hongbaostarTimeLin.setEnabled(false);
			hongbaoStartTimeTextView.setTextColor(this.getResources().getColor(
					R.color.text_color_light));
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
	
/*
		"title" => "required"
		"num" => "required"
		"total" => "required"
		"lower_money" => "required"
		"upper_money" => "required"
		"time_range" => "required"
		"single_limit" => "required"
		"life" => "required"
		"range" => "required"
		"logo" => "required"
		"image" => "required"
		"rules" => "required"
		"limit" => "required"*/
		RequestParams params = new RequestParams();
		params.add("title",hongbaoNameEditText.getText().toString());
		params.add("num",hongbaonum.getText().toString());
		params.add("total",hongbaoPrice.getText().toString());
		params.add("lower_money",hongbaoUpPrice.getText().toString());
		params.add("upper_money",hongbaoUpPrice.getText().toString());
		params.add("time_range",hongbaodaynumEditText.getText().toString());
		/*params.add("single_limit",);
		params.add("life",);
		params.add("range",);
		params.add("life",);
		params.add("logo",);
		params.add("image",);
		params.add("rules",);
		params.add("limit",);*/

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

	public void setListViewHeightBasedOnChildren(ListView listView) {
		// 获取ListView对应的Adapter
		HongbaoAdapter hongbaoAdapter = (HongbaoAdapter) listView.getAdapter();
		if (hongbaoAdapter == null) {
			return;
		}

		int totalHeight = 0;
		for (int i = 0, len = hongbaoAdapter.getCount(); i < len; i++) {
			// listAdapter.getCount()返回数据项的数目
			View listItem = hongbaoAdapter.getView(i, null, listView);
			// 计算子项View 的宽高
			listItem.measure(0, 0);
			// 统计所有子项的总高度
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (hongbaoAdapter.getCount() - 1));
		// listView.getDividerHeight()获取子项间分隔符占用的高度
		// params.height最后得到整个ListView完整显示需要的高度
		listView.setLayoutParams(params);
	}

}
