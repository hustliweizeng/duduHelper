package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.adapter.GuestListCheckAdapter;
import com.dudu.duduhelper.adapter.GuestManageAdapter;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.GuestListBean;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static com.dudu.duduhelper.R.id.total_guest;


/**
 * @author
 * @version 1.0
 * @date 2016/9/24
 */

public class GuestSelectActivity extends BaseActivity implements View.OnClickListener {
	private RecyclerView recycleview_list;
	private TextView tv_select_all;
	private TextView tv_submit;
	private GuestListCheckAdapter adapter;
	private int page = 1;
	private int num = 10;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		LogUtil.d("res","success");
		setContentView(R.layout.activity_guest_select);
		initHeadView("选择客户", false, false, 0);
		adapter = new GuestListCheckAdapter(this);
		//设置客户列表,从静态变量那里直接获取

		initView();
		initData();
	}

	private void initData() {
		RequestParams params = new RequestParams();
		params.put("page",page);
		params.put("size",num);
		HttpUtils.getConnection(context, params, ConstantParamPhone.GET_GUEST_LIST, "POST", new TextHttpResponseHandler() {
			@Override
			public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
				Toast.makeText(context,"网络异常，稍后再试",Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(int i, Header[] headers, String s) {
				LogUtil.d("res",s);
				try {
					JSONObject object = new JSONObject(s);
					String code =  object.getString("code");
					if ("SUCCESS".equalsIgnoreCase(code)){
						//数据请求成功
						GuestListBean guestList = new Gson().fromJson(s, GuestListBean.class);
						if (guestList.getList()!= null &&guestList.getList().size()>0){
							adapter.addGuests(guestList.getList());
						}
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

	private void initView() {
		recycleview_list = (RecyclerView) findViewById(R.id.recycleview_list);
		tv_select_all = (TextView) findViewById(R.id.tv_select_all);
		tv_select_all.setOnClickListener(this);
		tv_submit = (TextView) findViewById(R.id.tv_submit);
		tv_submit.setOnClickListener(this);
		ArrayList<CharSequence> checked = getIntent().getCharSequenceArrayListExtra("checked");
		ArrayList<CharSequence> checkedIdS = getIntent().getCharSequenceArrayListExtra("checked_id");

		if (checked!=null){
			adapter.setList(checked);
		}
		if (checkedIdS!=null){
			adapter.setIds(checkedIdS);
			LogUtil.d("set_ok",checkedIdS.size()+"");
		}else {
			LogUtil.d("set_fail","fail");

		}
		//设置店铺点击事件
		adapter.setOnItemClickListner(new GuestListCheckAdapter.OnItemClickListner() {
			@Override
			public void onItemClick(View view, int position) {
				//点击
				Toast.makeText(context,"点击了"+position,Toast.LENGTH_SHORT).show();
			}
		});
		recycleview_list.setLayoutManager(new LinearLayoutManager(this));
		recycleview_list.setAdapter(adapter);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case  R.id.recycleview_list:
				break;
			case  R.id.tv_select_all:
				//全选
				adapter.addAll();
				break;
			case  R.id.tv_submit:
				//确定按钮
				Intent intent = new Intent();
				intent.putCharSequenceArrayListExtra("list",adapter.getList());
				intent.putCharSequenceArrayListExtra("ids",adapter.getIds());
				setResult(2,intent);
				finish();
				break;

		}
	}
}
