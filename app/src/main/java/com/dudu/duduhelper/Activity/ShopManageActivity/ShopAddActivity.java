package com.dudu.duduhelper.Activity.ShopManageActivity;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dudu.duduhelper.Activity.ShopImageViewBrower;
import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.Utils.ViewUtils;
import com.dudu.duduhelper.adapter.ShopCategoryAdapter;
import com.dudu.duduhelper.adapter.ShopCircleAdapter;
import com.dudu.duduhelper.adapter.ShopDetailBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.ShopCategoryBean;
import com.dudu.duduhelper.javabean.ShopCricleBean;
import com.dudu.duduhelper.javabean.ShopListBean;
import com.dudu.duduhelper.widget.MyAlertDailog;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class ShopAddActivity extends BaseActivity implements View.OnClickListener {

	private EditText ed_title_shop;
	private TextView tv_class_shop;
	private TextView tv_circle_shop;
	private TextView phoneNumText;
	private ImageView shopImageView;
	private TextView tv_img_num;
	private Button submitbtn;
	private EditText ed_location_shop;
	private String shopId;
	private EditText ed_discription;

	protected ImageLoader imageLoader = ImageLoader.getInstance();
	private ArrayList<String> listSource;
	private int category_id;
	private int circle_id;
	private ArrayList<String> uplodImgs;
	//临时数组，返回后剋
	private int subThreadCount;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			//上传完毕后，本地集合改变,网络数据和本地数据统一
			webImgs = uplodImgs;
			imageLoader.displayImage(uplodImgs.get(0), shopImageView);
			tv_img_num.setText("相册有" + uplodImgs.size() + "张图片");
			Toast.makeText(context, "上传完毕", Toast.LENGTH_SHORT).show();
		}
	};
	private ArrayList<String> webImgs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_add);
		initHeadView("门店设置", true, false, 0);
		initView();
		initData();
	}

	//初始化页面详情
	private void initData() {
		shopId = getIntent().getStringExtra("shopId");
		String type = getIntent().getStringExtra("source");
		if (!type.equals("detail")) {
			//新建店铺
			return;

		} else {
			//店铺详情页面
			//请求店铺详情
			HttpUtils.getConnection(context, null,
					ConstantParamPhone.GET_SHOP_DETAIL + shopId, "get", new TextHttpResponseHandler() {
						@Override
						public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
							Toast.makeText(context, "网络异常，稍后再试", Toast.LENGTH_LONG).show();
						}

						@Override
						public void onSuccess(int i, Header[] headers, String s) {
							try {
								JSONObject object = new JSONObject(s);
								String code = object.getString("code");
								if ("SUCCESS".equalsIgnoreCase(code)) {
									//数据请求成功
									LogUtil.d("succ", s);
									ShopDetailBean detaiData = new Gson().fromJson(s, ShopDetailBean.class);
									if (detaiData.getData() != null) {
										ShopDetailBean.DataBean data = detaiData.getData();
										ed_title_shop.setText(data.getName());
										tv_class_shop.setText(data.getCategory_name());
										tv_circle_shop.setText(data.getArea_name());
										phoneNumText.setText(data.getContact());
										ed_location_shop.setText(data.getAddress());
										ed_discription.setText(data.getDescription());
										//初始化行业和商圈id
										circle_id = Integer.parseInt(data.getArea());
										category_id = Integer.parseInt(data.getCategory());
										
										webImgs = data.getImages();
										//设置图片
										if (webImgs != null && webImgs.size() > 0) {
											ImageLoader.getInstance().displayImage(webImgs.get(0), shopImageView);
											tv_img_num.setText("相册有" + webImgs.size() + "张图片");
										}
									}

								} else {
									//数据请求失败
									String msg = object.getString("msg");
									Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
								}
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
					});
		}

	}

	private void initView() {
		ed_title_shop = (EditText) findViewById(R.id.ed_title_shop);
		tv_class_shop = (TextView) findViewById(R.id.tv_class_shop);
		tv_class_shop.setOnClickListener(this);
		tv_circle_shop = (TextView) findViewById(R.id.tv_circle_shop);
		tv_circle_shop.setOnClickListener(this);
		phoneNumText = (TextView) findViewById(R.id.phoneNumText);
		shopImageView = (ImageView) findViewById(R.id.shopImageView);
		shopImageView.setOnClickListener(this);
		tv_img_num = (TextView) findViewById(R.id.tv_img_num);
		submitbtn = (Button) findViewById(R.id.submitbtn);
		submitbtn.setOnClickListener(this);
		ed_location_shop = (EditText) findViewById(R.id.ed_location_shop);
		ed_discription = (EditText) findViewById(R.id.ed_discription);
	}

	//上传门店信息
	private void submit() {
		String shop = ed_title_shop.getText().toString().trim();
		if (TextUtils.isEmpty(shop) ||TextUtils.isEmpty(ed_title_shop.getText().toString().trim())
		||TextUtils.isEmpty(tv_class_shop.getText().toString().trim())||TextUtils.isEmpty(tv_circle_shop.getText().toString().trim()) 
		||TextUtils.isEmpty(phoneNumText.getText().toString().trim())||TextUtils.isEmpty(ed_discription.getText().toString().trim())
		||TextUtils.isEmpty(ed_location_shop.getText().toString().trim())){
			Toast.makeText(this, "信息填写不完整！", Toast.LENGTH_SHORT).show();
			return;
		}
		
		RequestParams params = new RequestParams();
		params.add("name",ed_title_shop.getText().toString());
		params.add("contact",phoneNumText.getText().toString());
		params.add("address",ed_location_shop.getText().toString());
		params.add("description",ed_discription.getText().toString());
		params.add("category",category_id+"");
		params.add("area",circle_id+"");
		//上传的图片转为数组
		//说明没有修改图片,直接用获取到的数据
		if (uplodImgs ==null){
			uplodImgs = webImgs;
		}
		String[] tempImgs  =new String[uplodImgs.size()];
		int i=0;
		for (String img :uplodImgs){
			tempImgs[i] = img;
			i++;
		}

		
		String url = null;
		params.put("images",uplodImgs);
		if (shopId !=null){
			url = ConstantParamPhone.EDIT_SHOP_DETAIL + shopId;
			LogUtil.d("EDIT",url);
		}else {
			url = ConstantParamPhone.ADD_SHOP;
			LogUtil.d("new",url);
		}
		
		HttpUtils.getConnection(context, params, url, "post", new TextHttpResponseHandler() {
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
						Toast.makeText(context,"上传成功",Toast.LENGTH_LONG).show();
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

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.backButton:
				finish();
				break;
			case R.id.submitbtn:
				//提交按钮
				submit();
				break;
			case R.id.tv_class_shop:
				//请求网络数据获取行业分类信息
				HttpUtils.getConnection(context, null, ConstantParamPhone.GET_CATEGPRY_INFO, "GET", new TextHttpResponseHandler() {
					@Override
					public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
						Toast.makeText(context, "网络异常，稍后再试", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(int i, Header[] headers, String s) {

						try {
							JSONObject object = new JSONObject(s);
							String code = object.getString("code");
							if ("SUCCESS".equalsIgnoreCase(code)) {
								//数据请求成功
								//  LogUtil.d("category",s);
								ShopCategoryBean categoryBean = new Gson().fromJson(s, ShopCategoryBean.class);
								showCategorySelctor(categoryBean.getData(), "选择行业");
							} else {
								//数据请求失败
								String msg = object.getString("msg");
								Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
				break;
			case R.id.tv_circle_shop:
				//请求网络数据获取行业分类信息
				HttpUtils.getConnection(context, null, ConstantParamPhone.GET_SHOPCIRCLE_INFO, "GET", new TextHttpResponseHandler() {
					@Override
					public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
						Toast.makeText(context, "网络异常，稍后再试", Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(int i, Header[] headers, String s) {
						try {
							JSONObject object = new JSONObject(s);
							String code = object.getString("code");
							if ("SUCCESS".equalsIgnoreCase(code)) {
								//数据请求成功
								ShopCricleBean cricleBean = new Gson().fromJson(s, ShopCricleBean.class);
								showCircleSelctor(cricleBean.getData(), "选择商圈");
							} else {
								//数据请求失败
								String msg = object.getString("msg");
								Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
							}
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
				});
				break;

			case R.id.shopImageView:
				//进入选择相册页面
				//传递图片过去
				Intent intent1 = new Intent(context, ShopImageViewBrower.class);
				//把网络数据传输过去
				intent1.putStringArrayListExtra("imageList", webImgs);
				intent1.putExtra("type", 5);
				startActivityForResult(intent1, 5);
				break;
		}
	}

	//显示行业选择框
	private void showCategorySelctor(final List<ShopCategoryBean.DataBean> category, final String title) {
		ShopCategoryAdapter adapter = new ShopCategoryAdapter(context, R.layout.item_circle_select);
		adapter.addAll(category);
		LogUtil.d("adapter", adapter.getCount() + "");
		MyAlertDailog.show(context, title, adapter);
		//通过接口回调，确认选择的条目，并展示出来
		MyAlertDailog.setOnItemClickListentner(new MyAlertDailog.OnItemClickListentner() {
			@Override
			public void Onclick(int poistion) {
				//设置选中的行业
				tv_class_shop.setText(category.get(poistion).getName());
				//设置选中的行业id
				category_id = Integer.parseInt(category.get(poistion).getId());
				LogUtil.d("shangquan", "category_id=" + category_id);


			}
		});

	}

	//显示商圈选择框
	private void showCircleSelctor(final List<ShopCricleBean.DataBean> category, final String title) {
		ShopCircleAdapter adapter = new ShopCircleAdapter(context, R.layout.item_circle_select);
		adapter.addAll(category);
		MyAlertDailog.show(context, title, adapter);
		//通过接口回调，确认选择的条目，并展示出来
		MyAlertDailog.setOnItemClickListentner(new MyAlertDailog.OnItemClickListentner() {
			@Override
			public void Onclick(int poistion) {
				//设置选中的行业
				tv_circle_shop.setText(category.get(poistion).getName());
				//设置选中的行业id
				circle_id = Integer.parseInt(category.get(poistion).getId());
				LogUtil.d("shangquan", "circle_id=" + circle_id);
			}
		});

	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case 5:
					//获取店家环境相册
					uplodImgs = (ArrayList<String>) data.getSerializableExtra("pics");
					//后台上传本地的图片
					for (String s : uplodImgs) {
						if (!s.startsWith("http")) {
							//这边是子线程上传，所以不会立即完成
							uploadImg(context, s);
							subThreadCount = 0;
							subThreadCount++;
						}
					}
			}

		}
	}

	/**
	 * imageUri 上传图片的本地uri地址
	 */
	public void uploadImg(final Context context, final String imageUri) {
		//压缩图片再上传
		Bitmap img = BitmapFactory.decodeFile(imageUri);
		//字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		//压缩
		img.compress(Bitmap.CompressFormat.JPEG, 60, baos);
		//输出流转换为字节数组
		byte[] picByte = baos.toByteArray();
		//转换为base64格式
		String picBase64 = Base64.encodeToString(picByte, 1);

		//上传图片
		RequestParams params = new RequestParams();
		params.add("content", picBase64);
		HttpUtils.getConnection(context, params, ConstantParamPhone.UPLOAD_PIC, "post", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context, "网络异常，请稍后再试", Toast.LENGTH_SHORT).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				try {
					JSONObject object = new JSONObject(s);
					String code = object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)) {
						//保存到集合中
						uplodImgs.add(object.getString("url"));
						//移除本地地址
						uplodImgs.remove(imageUri);
						subThreadCount--;
						//说明上传完毕
						if (subThreadCount == 0) {
							handler.sendEmptyMessage(0);
						}
						LogUtil.d("pic_success", s);
					} else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
						LogUtil.d("pic_fail", s);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}


		});


	}
}
