package com.dudu.duduhelper.Activity.RedBagActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dudu.duduhelper.Activity.ShopImageViewBrower;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.Utils.ViewUtils;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.SystemBarTintManager;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.R.attr.required;
import static android.R.attr.spacing;

/**
 * Created by lwz on 2016/8/19.
 */
public class EditRedbag2Activity extends Activity implements View.OnClickListener {

	private LinearLayout ll_content_edit_redab2;
	private LinearLayout ll_condition_edit_redbag2;
	//红包使用条件的默认加入位置
	private int position = 7;
	private ImageButton backButton;
	private LinearLayout relayout_mytitle;
	private EditText ed_total;
	private EditText ed_low;
	private EditText ed_high;
	private EditText ed_num;
	private ImageView iv_add_edit_redbag2;
	private ImageView iv_plus;
	private LinearLayout ll_plus;
	private Button btn_create_redbag2_submit;
	private boolean isPlus;
	private String title;
	private String startTime;
	private String endTime;
	private EditText ed_rule;
	private EditText ed_life;
	private ImageView iv_logo;
	private ImageView iv_pic;
	private int subThreadCount;
	private Uri urilocal;
	private ArrayList<String> uplodImgs;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			//上传完毕后，本地集合改变,网络数据和本地数据统一
			ImageLoader.getInstance().displayImage(uplodImgs.get(0), iv_pic);
			Toast.makeText(EditRedbag2Activity.this, "上传完毕", Toast.LENGTH_SHORT).show();
		}
	};
	private String imagepath;
	private String uploadPicPath;
	private SharedPreferences sp;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_edit_redbag2);
		sp = getSharedPreferences("userconig", Context.MODE_PRIVATE);
		initView();
		initview();
	}

	private void initview() {
		ImageButton backButton = (ImageButton) findViewById(R.id.backButton);
		//红包内容的总布局
		ll_content_edit_redab2 = (LinearLayout) findViewById(R.id.ll_content_edit_redab2);
		//红包使用限制的布局view
		ll_condition_edit_redbag2 = (LinearLayout) findViewById(R.id.ll_condition_edit_redbag2);
		ImageView iv_add_edit_redbag2 = (ImageView) findViewById(R.id.iv_add_edit_redbag2);
		//添加条目设置点击事件
		iv_add_edit_redbag2.setOnClickListener(this);
		//首次创建时，默认添加一个条件
		createConditon();
		backButton.setOnClickListener(this);
		//图片设置为圆角
		ImageLoader.getInstance().displayImage(sp.getString("shopLogo",""),iv_logo, new DisplayImageOptions.Builder()
				.displayer(new RoundedBitmapDisplayer(Util.dip2px(EditRedbag2Activity.this,80))).build());

	}

	public void init() {
		//每个activity界面设置为沉浸式状态栏，android 4.4以上才支持
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			setTranslucentStatus(true);
		}
		SystemBarTintManager tintManager = new SystemBarTintManager(this);
		//设置通知栏（状态栏）的颜色
		tintManager.setStatusBarTintEnabled(true);
		tintManager.setStatusBarTintResource(R.color.status_Bar_color_red);//通知栏所需颜色
	}

	//做版本兼容，
	@TargetApi(19)
	private void setTranslucentStatus(boolean on) {
		Window win = getWindow();
		WindowManager.LayoutParams winParams = win.getAttributes();
		final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
		if (on) {
			winParams.flags |= bits;
		} else {
			winParams.flags &= ~bits;
		}
		win.setAttributes(winParams);
		
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.backButton:
				finish();
				break;
			case R.id.btn_create_redbag2_submit:
				//请求网络
				submit();
				break;
			//添加红包使用条件
			case R.id.iv_add_edit_redbag2:
				createConditon();
				break;
			case R.id.iv_plus:
				//是否开启高级页面
				if (!isPlus){
					iv_plus.setImageResource(R.drawable.icon_up_arr);
					ll_plus.setVisibility(View.VISIBLE);

				}else {
					iv_plus.setImageResource(R.drawable.icon_down_arr);
					ll_plus.setVisibility(View.GONE);
				}
				isPlus = !isPlus;
				break;
			case R.id.iv_pic:
				//进入选择相册页面
				Intent intent1 = new Intent(this, ShopImageViewBrower.class);
				if(uplodImgs!=null &&uplodImgs.size()>0){
					//当再次进入相册编辑时，显示之前选择的相册
					intent1.putStringArrayListExtra("imageList", uplodImgs);
				}
				intent1.putExtra("type",8);
				startActivityForResult(intent1, 8);
				break;
			case R.id.iv_logo:
				//进入logo裁剪页面
				Intent intent=new Intent(Intent.ACTION_GET_CONTENT);//ACTION_OPEN_DOCUMENT
				intent.addCategory(Intent.CATEGORY_OPENABLE);
				intent.setType("image/jpeg");
				startActivityForResult(intent,  1);
		}
	}
	//1创建集合管理红包条件
	HashMap<Integer,LinearLayout> conditions = new HashMap<>();
	int itemId  =0;
	private void createConditon() {
		//每次载入红包条件的布局
		LinearLayout ll_limit = (LinearLayout) View.inflate(this, R.layout.item_condition_edit_redbag, null);
		ImageView iv_del_item = (ImageView) ll_limit.findViewById(R.id.iv_del_item);
		//给每个条目绑定一个key
		iv_del_item.setTag(itemId);
		iv_del_item.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//获取最外层的父对象
				int itemId = (int) v.getTag();
				//Toast.makeText(EditRedbag2Activity.this,"点击了"+itemId,Toast.LENGTH_SHORT).show();
				//删除指定位置的view
				ll_content_edit_redab2.removeView(conditions.get(itemId));
				//删除指定的item对象
				conditions.remove(itemId);
				//记录position最新位置
				position--;
			}
		});
		//把当前对象保存到集合中
		conditions.put(itemId,ll_limit);
		ll_content_edit_redab2.addView(ll_limit, position);
		//每次添加以后，位置会变动
		position++;
		itemId++;
		Log.d("condition", "创建了条件" + position);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
				case 8:
					//获取返回的相册
					uplodImgs = (ArrayList<String>) data.getSerializableExtra("pics");
					//后台上传本地的图片
					for (String s : uplodImgs) {
						if (!s.startsWith("http")) {
							//这边是子线程上传，所以不会立即完成
							uploadImg(EditRedbag2Activity.this, s);
							//每上传一次记录一次
							subThreadCount++;
						}
					}
					break;
				case 1:
					//logo选择页面
					String path = Util.getPath(EditRedbag2Activity.this, data.getData());
					Uri uri = Uri.fromFile(new File(path));
					startPhotoZoom(uri);
					System.out.println("开始裁剪"+data);
					break;
				case 2:
					//裁剪结束上传
					ImageLoader.getInstance().displayImage(imagepath,iv_logo, new DisplayImageOptions.Builder()
							.displayer(new RoundedBitmapDisplayer(Util.dip2px(EditRedbag2Activity.this,80))).build());
					//子线程上传图片，上传完毕handler告诉主线程
					String imgPath = ViewUtils.getRealFilePath(EditRedbag2Activity.this,urilocal);
					uploadPicPath = ViewUtils.uploadImg(EditRedbag2Activity.this,imgPath);
					LogUtil.d("logo", uploadPicPath);
					break;
			}
		}
	}

	private void initView() {
		backButton = (ImageButton) findViewById(R.id.backButton);
		relayout_mytitle = (LinearLayout) findViewById(R.id.relayout_mytitle);
		ed_total = (EditText) findViewById(R.id.ed_total);
		ed_low = (EditText) findViewById(R.id.ed_low);
		ed_high = (EditText) findViewById(R.id.ed_high);
		ed_num = (EditText) findViewById(R.id.ed_num);
		iv_add_edit_redbag2 = (ImageView) findViewById(R.id.iv_add_edit_redbag2);
		iv_plus = (ImageView) findViewById(R.id.iv_plus);
		ll_plus = (LinearLayout) findViewById(R.id.ll_plus);
		ed_rule = (EditText) findViewById(R.id.ed_rule);
		ed_life = (EditText) findViewById(R.id.ed_life);
		iv_logo = (ImageView) findViewById(R.id.iv_logo);
		iv_pic = (ImageView) findViewById(R.id.iv_pic);
		iv_pic.setOnClickListener(this);
		iv_logo.setOnClickListener(this);
		btn_create_redbag2_submit = (Button) findViewById(R.id.btn_create_redbag2_submit);
		
		
		iv_plus.setOnClickListener(this);
		backButton.setOnClickListener(this);
		iv_add_edit_redbag2.setOnClickListener(this);
		btn_create_redbag2_submit.setOnClickListener(this);
	}

	private void submit() {
		// validate
		String total = ed_total.getText().toString().trim();
		if (TextUtils.isEmpty(total)) {
			Toast.makeText(this, "请输入红包金额", Toast.LENGTH_SHORT).show();
			return;
		}

		String low = ed_low.getText().toString().trim();
		if (TextUtils.isEmpty(low)) {
			Toast.makeText(this, "请输入最低", Toast.LENGTH_SHORT).show();
			return;
		}

		String high = ed_high.getText().toString().trim();
		if (TextUtils.isEmpty(high)) {
			Toast.makeText(this, "请输入最高", Toast.LENGTH_SHORT).show();
			return;
		}

		String num = ed_num.getText().toString().trim();
		if (TextUtils.isEmpty(num)) {
			Toast.makeText(this, "请输入红包数量", Toast.LENGTH_SHORT).show();
			return;
		}




		title = getIntent().getStringExtra("title");
		startTime = getIntent().getStringExtra("starTime");
		endTime = getIntent().getStringExtra("endTime");
		
		
		RequestParams params = new RequestParams();
		params.put("title",title);
		params.put("time_start",startTime);
		params.put("time_end",endTime);
		params.put("num",num);
		params.put("total",total);
		params.put("lower_money",low);
		params.put("upper_money",high);
		params.put("life",ed_life);
		params.put("logo",sp.getString("shopLogo",""));
		params.put("rules",ed_rule.getText().toString().trim());
		params.put("range[]","1");

		//在condition中维护了一组集合条件
		Set<Map.Entry<Integer, LinearLayout>> items = conditions.entrySet();
		//转换为迭代器
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			LinearLayout val = (LinearLayout) entry.getValue();
			EditText ed_high = (EditText) val.findViewById(R.id.ed_high);
			EditText ed_low = (EditText) val.findViewById(R.id.ed_low);
			params.put("limit[price][" + Integer.parseInt(ed_high.getText().toString().trim()) + "]", Integer.parseInt(ed_high.getText().toString().trim()));
			params.put("limit[usable][" + Integer.parseInt(ed_low.getText().toString().trim()) + "]", Integer.parseInt(ed_low.getText().toString().trim()));
		}

		if (uplodImgs!=null &&uplodImgs.size()>0){
			params.put("image",uplodImgs.get(0));
			LogUtil.d("ima",uplodImgs.get(0));
		}else {
			params.put("image","22");
			LogUtil.d("ima","2");
		}
		HttpUtils.getConnection(this, params, ConstantParamPhone.ADD_REDBAG, "POST", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				LogUtil.d("erro",s);
				Toast.makeText(EditRedbag2Activity.this,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						Toast.makeText(EditRedbag2Activity.this,"成功",Toast.LENGTH_LONG).show();
						finish();
						startActivity(new Intent());


					}else {
						//数据请求失败
						String msg = object.getString("msg");
						Toast.makeText(EditRedbag2Activity.this,msg,Toast.LENGTH_LONG).show();
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		});


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
	/**
	 * 根据uri地址裁剪图片，缩放
	 * @param uri
	 */
	private void startPhotoZoom(Uri uri)
	{   //系统自带的裁剪工具
		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		// crop为true是设置在开启的intent中设置显示的view可以剪裁
		intent.putExtra("crop", "true");
		// aspectX aspectY 是宽高的比例
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		// outputX,outputY 是剪裁图片的宽高
		intent.putExtra("outputX", 300);
		intent.putExtra("outputY", 300);

		//裁剪后的名称,上传的时候用
		imagepath = Environment.getExternalStorageDirectory()+getPhotoFileName();
		File cropFile = new File(Environment.getExternalStorageDirectory(),getPhotoFileName());
		//把file转换成uri格式
		urilocal = Uri.fromFile(cropFile);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, urilocal);

		intent.putExtra("return-data", false);//设置返回data数据
		intent.putExtra("noFaceDetection", true); //关闭人脸检测

		startActivityForResult(intent, 2);
	}
	// 使用系统当前日期加以调整作为照片的名称
	private String getPhotoFileName()
	{
		Date date = new Date(System.currentTimeMillis());
		SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
		return dateFormat.format(date) + ".jpg";
	}
}
