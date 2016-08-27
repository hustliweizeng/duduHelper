package com.dudu.duduhelper;

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

import com.dudu.duduhelper.adapter.MemberAdapter;
import com.dudu.duduhelper.bean.MemberBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;

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
        ColorDialog.showRoundProcessDialog(ShopMemberListActivity.this, R.layout.loading_process_dialog_color);
        initData();
    }

    @SuppressLint("ResourceAsColor")
    @SuppressWarnings("deprecation")
    private void initView() {
        // TODO Auto-generated method stub
        editButton = (Button) this.findViewById(R.id.selectTextClickButton);
        editButton.setText("添加");
        editButton.setVisibility(View.VISIBLE);
        editButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
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
                // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        reloadButton = (Button) this.findViewById(R.id.reloadButton);
        //数据重载按钮
        reloadButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                initData();
            }
        });
        memberList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ShopMemberListActivity.this, ShopMemberAddActivity.class);
                intent.putExtra("account", memberAdapter.getItem(position));
                startActivityForResult(intent, 1);
            }
        });
    }

    private void initData() {
        // TODO Auto-generated method stub
        loading_progressBar.setVisibility(View.VISIBLE);
        loading_text.setText("加载中...");
        RequestParams params = new RequestParams();
        params.add("token", this.share.getString("token", ""));
        params.add("version", ConstantParamPhone.VERSION);
        params.setContentEncoding("UTF-8");
        /**
         * 每次请求创建网络连接，这样比较消耗内容，应该用线程池好点
         */
      HttpUtils.getConnection(context,params, ConstantParamPhone.GET_MEMBER_LIST,"post" , new TextHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                reloadButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {
                MemberBean getCouponSellBean = new Gson().fromJson(arg2, MemberBean.class);
                if (!getCouponSellBean.getStatus().equals("1")) {
                    MyDialog.showDialog(ShopMemberListActivity.this, "该账号已在其他手机登录，是否重新登录", false, true, "取消", "确定", new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            MyDialog.cancel();
                        }
                    }, new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            // TODO Auto-generated method stub
                            Intent intent = new Intent(ShopMemberListActivity.this, LoginActivity.class);
                            startActivity(intent);
                        }
                    });
                } else {
                    if (getCouponSellBean.getData() != null && getCouponSellBean.getData().size() != 0) {
                        memberAdapter.addAll(getCouponSellBean.getData());
                        reloadButton.setVisibility(View.GONE);
                        loading_progressBar.setVisibility(View.GONE);
                        loading_text.setText("加载完啦！");

                    } else {
                        loading_progressBar.setVisibility(View.GONE);
                        loading_text.setText("啥也没有！");
                        reloadButton.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                memberListswipeLayout.setRefreshing(false);
                ColorDialog.dissmissProcessDialog();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        //清空adapter数据
        memberAdapter.clear();
        //loadMoreView.setVisibility(View.GONE);
        ColorDialog.showRoundProcessDialog(this, R.layout.loading_process_dialog_color);
        initData();
    }

}
