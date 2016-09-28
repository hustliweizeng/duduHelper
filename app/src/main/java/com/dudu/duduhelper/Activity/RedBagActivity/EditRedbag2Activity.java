package com.dudu.duduhelper.Activity.RedBagActivity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
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

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.SystemBarTintManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
		setContentView(R.layout.activity_edit_redbag2);
		initView();
		initview();

		String title = getIntent().getStringExtra("title");
		String startTime = getIntent().getStringExtra("starTime");
		String endTime = getIntent().getStringExtra("endTime");

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
				String url = ConstantParamPhone.BASE_URL + ConstantParamPhone.ADD_REDBAG;
				submit();
				break;
			//添加红包使用条件
			case R.id.iv_add_edit_redbag2:
				createConditon();
				break;
			case R.id.iv_plus:
				
				if (!isPlus){
					iv_plus.setImageResource(R.drawable.icon_up_arr);
					ll_plus.setVisibility(View.VISIBLE);

				}else {
					iv_plus.setImageResource(R.drawable.icon_down_arr);
					ll_plus.setVisibility(View.GONE);
				}
				isPlus = !isPlus;
			case R.id.iv_del:
				
				break;
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
			Toast.makeText(this, "最低", Toast.LENGTH_SHORT).show();
			return;
		}

		String high = ed_high.getText().toString().trim();
		if (TextUtils.isEmpty(high)) {
			Toast.makeText(this, "最高", Toast.LENGTH_SHORT).show();
			return;
		}

		String num = ed_num.getText().toString().trim();
		if (TextUtils.isEmpty(num)) {
			Toast.makeText(this, "请输入红包数量", Toast.LENGTH_SHORT).show();
			return;
		}


		/*String ed_low_price = ed_low_price.getText().toString().trim();
		if (TextUtils.isEmpty(price)) {
			Toast.makeText(this, "price不能为空", Toast.LENGTH_SHORT).show();
			return;
		}*/
		//ColorDialog.showRoundProcessDialog(this,R.layout.loading_process_dialog_color);
		//在condition中维护了一组集合条件
		Set<Map.Entry<Integer, LinearLayout>> items = conditions.entrySet();
		//转换为迭代器
		Iterator iter = items.iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			LinearLayout val = (LinearLayout) entry.getValue();
			EditText ed_high = (EditText) val.findViewById(R.id.ed_high);
			EditText ed_low = (EditText) val.findViewById(R.id.ed_low);
			String s1 = ed_high.getText().toString().trim();
			String s2 = ed_low.getText().toString().trim();
			if (TextUtils.isEmpty(s1) &&TextUtils.isEmpty(s2)){
				Toast.makeText(this,"限制条件填写不完整",Toast.LENGTH_SHORT).show();
				return;
			}
			limits.add(new RedbagLimit(s1,s2));
		}
		final String s = new Gson().toJson(limits);
		LogUtil.d("limit",s);


	}
	List<RedbagLimit> limits = new ArrayList<>();
	class RedbagLimit{
		String price ;
		String useable;
		RedbagLimit(String price ,String useable){
			this.price = price;
			this.useable = useable;
		}
	}
}
