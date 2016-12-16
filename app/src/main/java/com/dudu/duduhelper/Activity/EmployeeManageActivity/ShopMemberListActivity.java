package com.dudu.duduhelper.Activity.EmployeeManageActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.adapter.MemberAdapter;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.ShopUserBean;
import com.google.gson.Gson;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.dudu.duduhelper.R;
public class ShopMemberListActivity extends BaseActivity {
    private SwipeRefreshLayout memberListswipeLayout;
    private ListView memberList;
    private Button reloadButton;
    private ProgressBar loading_progressBar;
    private TextView loading_text;
    private View footView;
    private MemberAdapter memberAdapter;
    private Button editButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_member_list);
        initHeadView("店员管理", true, true, R.drawable.ic_launcher);
        initView();
        memberListswipeLayout.setProgressViewOffset(false, 0, Util.dip2px(context, 24));//第一次启动时刷新
        memberListswipeLayout.setRefreshing(true);
        initData();
    }

    @Override
    public void onResume() {
        super.onResume();
        memberListswipeLayout.setProgressViewOffset(false, 0, Util.dip2px(context, 24));//第一次启动时刷新
        memberListswipeLayout.setRefreshing(true);
    }

    @SuppressLint("ResourceAsColor")
    @SuppressWarnings("deprecation")
    private void initView() {
        editButton = (Button) this.findViewById(R.id.selectTextClickButton);
        editButton.setText("添加");
        editButton.setVisibility(View.VISIBLE);
        editButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShopMemberListActivity.this, ShopMemberAddActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        memberAdapter = new MemberAdapter(this);
        memberListswipeLayout = (SwipeRefreshLayout) this.findViewById(R.id.memberListswipeLayout);
        //下拉刷新事件
        memberListswipeLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                memberAdapter.clear();
                initData();
            }
        });
        memberListswipeLayout.setColorSchemeResources(R.color.text_color);
        memberListswipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
        memberListswipeLayout.setProgressBackgroundColor(R.color.bg_color);
        memberList = (ListView) this.findViewById(R.id.memberList);
        memberList.setAdapter(memberAdapter);
        reloadButton = (Button) this.findViewById(R.id.reloadButton);
        reloadButton = (Button) this.findViewById(R.id.reloadButton);
        footView = LayoutInflater.from(this).inflate(R.layout.activity_listview_foot, null);
        loading_progressBar = (ProgressBar) footView.findViewById(R.id.loading_progressBar);
        loading_text = (TextView) footView.findViewById(R.id.loading_text);
        reloadButton = (Button) this.findViewById(R.id.reloadButton);
        //数据重载按钮
        reloadButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                initData();
            }
        });
        memberList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ShopMemberListActivity.this, ShopMemberAddActivity.class);
                intent.putExtra("detail", memberAdapter.getItem(position));
                startActivityForResult(intent, 1);
            }
        });
    }

    private void initData() {
        loading_progressBar.setVisibility(View.VISIBLE);
        loading_text.setText("加载中...");
      HttpUtils.getConnection(context,null, ConstantParamPhone.GET_SHOP_USER,"get" , new TextHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                reloadButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {
                try {
                    JSONObject object = new JSONObject(arg2);
                    String code =  object.getString("code");
                    if ("SUCCESS".equalsIgnoreCase(code)){
                        //数据请求成功
                        ShopUserBean bean = new Gson().fromJson(arg2, ShopUserBean.class);
                        if (bean.getData()!=null)
                        memberAdapter.addAll(bean.getData());

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
                // TODO Auto-generated method stub
                memberListswipeLayout.setRefreshing(false);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //清空adapter数据
        memberAdapter.clear();
        initData();
    }

}
