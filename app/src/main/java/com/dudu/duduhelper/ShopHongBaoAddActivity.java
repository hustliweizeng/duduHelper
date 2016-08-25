package com.dudu.duduhelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.http.Header;
import org.fireking.app.imagelib.entity.ImageBean;

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

	private HongbaoListBean hongbaoBean;

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
		// 商品id；
		id = getIntent().getStringExtra("id");
		// 商品分类category；
		category = getIntent().getStringExtra("category");
		sendShaiShaiAdapter = new SendShaiShaiAdapter(this);
		initView();
		if ((HongbaoListBean) getIntent().getSerializableExtra("hongbao") != null) 
		{
			hongbaoBean = (HongbaoListBean) getIntent().getSerializableExtra("hongbao");
			initData();
		} 
		else 
		{
			isAdd = true;
		}
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
				if (isAdd) {
					SubmitProduct(ConstantParamPhone.ADD_HONGBAO);
				} else {
					SubmitProduct(ConstantParamPhone.EDIT_HONGBAO);
				}
			}
		});

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 0x123 && resultCode == RESULT_OK) {
			// imagesList=(List<ImageBean>) data.getSerializableExtra("images");
			// sendShaiShaiAdapter.addAll(imagesList);
			sendShaiShaiAdapter.addAll((List<ImageBean>) data
					.getSerializableExtra("images"));
		} else if (requestCode == 0x456 && resultCode == RESULT_OK) {
			Intent intent = data;
			imagesList = (List<ImageBean>) intent
					.getSerializableExtra("M_LIST");
			System.out.println("返回的数据量:" + imagesList.size());
			for (ImageBean m : imagesList) {
				System.out.println(m.path);
			}
			sendShaiShaiAdapter.clear();
			sendShaiShaiAdapter.addAll(imagesList);
		}

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
		// TODO Auto-generated method stub
		hongbaoNameEditText.setText(hongbaoBean.getTitle());
		hongbaoPrice.setText(hongbaoBean.getTotal());
		hongbaonum.setText(hongbaoBean.getNum());
		hongbaoDownPrice.setText(hongbaoBean.getLower_money());
		hongbaoUpPrice.setText(hongbaoBean.getUpper_money());
		hongbaoStartTimeTextView.setText(Util.DataConVertMint(hongbaoBean
				.getTime_start()));
		hongbaoEndTimeTextView.setText(Util.DataConVertMint(hongbaoBean
				.getTime_end()));
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

		// 后期如果多张图片需要写循环啦
		sendShaiShaiAdapter.clear();
		ImageBean imageBean = new ImageBean(hongbaoBean.getLogo());
		sendShaiShaiAdapter.add(imageBean);

		Gson gson = new Gson();
		// json字符串转换成Map对象
		hongbaoAdapter.clear();
		Map<String, String> limits = gson.fromJson(hongbaoBean.getLimit(),
				new TypeToken<Map<String, String>>() {
				}.getType());
		for (java.util.Map.Entry<String, String> entry : limits.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			System.out.println("key:" + key + "," + value);
			HongbaoAddBean hongbao = new HongbaoAddBean();
			hongbao.setTotalMoney(key);
			hongbao.setDiscountMoney(value);
			hongbaoAdapter.addHongbao(hongbao);
		}
		setListViewHeightBasedOnChildren(hongbaoAddList);
	}

	private void SubmitProduct(String methord) {
		// TODO Auto-generated method stub

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
		if (hongbaoBean != null) {
			if (Integer.parseInt(hongbaoBean.getSend_num()) <= 0) {
				Double upTotal = Double.parseDouble(hongbaoUpPrice.getText()
						.toString().trim())
						* Double.parseDouble(hongbaonum.getText().toString()
								.trim());
				Double downTotal = Double.parseDouble(hongbaoDownPrice
						.getText().toString().trim())
						* Double.parseDouble(hongbaonum.getText().toString()
								.trim());
				if (Double
						.parseDouble(hongbaoPrice.getText().toString().trim()) > upTotal) {
					Toast.makeText(ShopHongBaoAddActivity.this,
							"红包上线不符合要求，请重新输入", Toast.LENGTH_SHORT).show();
					return;
				}
				if (Double
						.parseDouble(hongbaoPrice.getText().toString().trim()) < downTotal) {
					Toast.makeText(ShopHongBaoAddActivity.this,
							"红包下限不符合要求，请重新输入", Toast.LENGTH_SHORT).show();
					return;
				}
			}
		} else {
			Double upTotal = Double.parseDouble(hongbaoUpPrice.getText()
					.toString().trim())
					* Double.parseDouble(hongbaonum.getText().toString().trim());
			Double downTotal = Double.parseDouble(hongbaoDownPrice.getText()
					.toString().trim())
					* Double.parseDouble(hongbaonum.getText().toString().trim());
			if (Double.parseDouble(hongbaoPrice.getText().toString().trim()) > upTotal) {
				Toast.makeText(ShopHongBaoAddActivity.this, "红包上线不符合要求，请重新输入",
						Toast.LENGTH_SHORT).show();
				return;
			}
			if (Double.parseDouble(hongbaoPrice.getText().toString().trim()) < downTotal) {
				Toast.makeText(ShopHongBaoAddActivity.this, "红包下限不符合要求，请重新输入",
						Toast.LENGTH_SHORT).show();
				return;
			}
		}

		final SubmitHongbaoBean submitHongbaoBean = new SubmitHongbaoBean();
		if (!isAdd) {
			submitHongbaoBean.setId(hongbaoBean.getId());
		}
		submitHongbaoBean.setTitle(hongbaoNameEditText.getText().toString()
				.trim());
		submitHongbaoBean.setTotal(hongbaoPrice.getText().toString().trim());
		submitHongbaoBean.setNum(hongbaonum.getText().toString().trim());
		submitHongbaoBean.setTime_start(Util.date2TimeStamp(
				hongbaoStartTimeTextView.getText().toString().trim(),
				"yyyy-MM-dd HH:mm:ss"));
		submitHongbaoBean.setTime_end(Util.date2TimeStamp(
				hongbaoEndTimeTextView.getText().toString().trim(),
				"yyyy-MM-dd HH:mm:ss"));
		submitHongbaoBean.setLife(hongbaodaynumEditText.getText().toString()
				.trim());
		Map<String, String> limits = new HashMap<String, String>();
		for (HongbaoAddBean hongbao : hongbaoAdapter.getList()) {
			limits.put(hongbao.getTotalMoney(), hongbao.getDiscountMoney());
		}
		Gson gson = new Gson();
		String jsonStr = gson.toJson(limits);
		submitHongbaoBean.setLimit(jsonStr);
		submitHongbaoBean.setLower_money(hongbaoDownPrice.getText().toString()
				.trim());
		submitHongbaoBean.setUpper_money(hongbaoUpPrice.getText().toString()
				.trim());
		String data = gson.toJson(submitHongbaoBean);

		ColorDialog.showRoundProcessDialog(ShopHongBaoAddActivity.this,
				R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.add("token", share.getString("token", ""));
		params.add("data", data);
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		// 保存cookie，自动保存到了shareprefercece
		PersistentCookieStore myCookieStore = new PersistentCookieStore(
				ShopHongBaoAddActivity.this);
		client.setCookieStore(myCookieStore);
		client.post(ConstantParamPhone.IP + methord, params,
				new TextHttpResponseHandler() {

					@Override
					public void onFailure(int arg0, Header[] arg1, String arg2,
							Throwable arg3) {
						Toast.makeText(ShopHongBaoAddActivity.this, "网络不给力呀",
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onSuccess(int arg0, Header[] arg1, String arg2) {
						ResponsBean responsBean = new Gson().fromJson(arg2,
								ResponsBean.class);
						if (!responsBean.getStatus().equals("0")) {
							Toast.makeText(ShopHongBaoAddActivity.this,
									responsBean.getInfo(), Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(ShopHongBaoAddActivity.this,
									"操作成功啦", Toast.LENGTH_SHORT).show();
							if (isAdd) {
								ShopHongBaoAddActivity.this.finish();
							} else {
								Intent data = new Intent();
								data.putExtra("hongbaobean", submitHongbaoBean);
								setResult(RESULT_OK, data);
								ShopHongBaoAddActivity.this.finish();
							}

						}
					}

					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						ColorDialog.dissmissProcessDialog();
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
