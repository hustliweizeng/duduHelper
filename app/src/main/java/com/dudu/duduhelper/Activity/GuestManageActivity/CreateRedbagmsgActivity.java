package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.adapter.GuestListCheckAdapter;
import com.dudu.duduhelper.adapter.RedbagMsgListAdapter;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.RedbagMsgListBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/23
 */

public class CreateRedbagmsgActivity extends BaseActivity {
	private TextView tv_send_num;
	private TextView tv_create_money;
	private RecyclerView recycleview_msg;
	private SwipeRefreshLayout refresh_msg;
	private Button submitbtn;
	private RedbagMsgListAdapter adapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_create_redbag_msg);
		initHeadView("红包通知",true,false,0);
		adapter = new RedbagMsgListAdapter(context);
		initView();
		initData();
	}

	private void initData() {
		ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
		RequestParams params = new RequestParams();
		params.put("type_id",2);//红包的typeid是2
		params.put("page","1");
		params.put("size","2");
		HttpUtils.getConnection(context, params, ConstantParamPhone.GET_SEND_RECORD, "post", new TextHttpResponseHandler() {
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
						RedbagMsgListBean redbagMsgListBean = new Gson().fromJson(s, RedbagMsgListBean.class);
						tv_send_num.setText(redbagMsgListBean.getTotal_red_packet());
						tv_create_money.setText(redbagMsgListBean.getTotal_promote_consumer());
						List<RedbagMsgListBean.ListBean> list = redbagMsgListBean.getList();
						if (list!=null &&list.size()>0){
							adapter.addAll(list);
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

			@Override
			public void onFinish() {
				super.onFinish();
				ColorDialog.dissmissProcessDialog();
			}
		});
		
	}

	private void initView() {
		tv_send_num = (TextView) findViewById(R.id.tv_send_num);
		tv_create_money = (TextView) findViewById(R.id.tv_create_money);
		recycleview_msg = (RecyclerView) findViewById(R.id.recycleview_msg);
		submitbtn = (Button) findViewById(R.id.submitbtn);
		refresh_msg = (SwipeRefreshLayout) findViewById(R.id.refresh_msg);
		refresh_msg.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			@Override
			public void onRefresh() {
				initData();
				refresh_msg.setRefreshing(false);
			}
		});
		
		submitbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(context,NewRedbagMsgActivity.class));
			}
		});
		recycleview_msg.setLayoutManager(new LinearLayoutManager(this));
		recycleview_msg.setAdapter(adapter);
		adapter.setOnItemClickListner(new GuestListCheckAdapter.OnItemClickListner() {
			@Override
			public void onItemClick(View view, int position) {
				startActivity(new Intent(context,RedbagMsgDetail.class));
			}
		});
	}
}
