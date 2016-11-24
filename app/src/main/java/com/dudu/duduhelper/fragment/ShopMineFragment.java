package com.dudu.duduhelper.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.Activity.MyInfoActivity.ChangeShopActivity;
import com.dudu.duduhelper.Activity.WelcomeActivity.LoginBindPhoneActivity;
import com.dudu.duduhelper.Activity.MainActivity;
import com.dudu.duduhelper.Activity.MyInfoActivity.ShopBankListActivity;
import com.dudu.duduhelper.Activity.MyInfoActivity.ShopSettingActivity;
import com.dudu.duduhelper.Activity.MyInfoActivity.WebPageActivity;
import com.dudu.duduhelper.Utils.CleanAppCache;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.application.DuduHelperApplication;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.http.HttpUtils;
import com.dudu.duduhelper.javabean.InfoBean;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.dudu.duduhelper.widget.OverScrollView;
import com.dudu.duduhelper.widget.OverScrollView.OverScrollTinyListener;
import com.dudu.duduhelper.wxapi.WXEntryActivity;
import com.google.gson.Gson;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;
import com.dudu.duduhelper.R;

public class ShopMineFragment extends Fragment {

    private View MineFragmentView;

    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private SharedPreferences sp;
    private ImageView mineImageHead;
    private SwipeRefreshLayout swiperefresh;
    private LinearLayout ll_manager_status;
    private Button logoutButton;
    private RelativeLayout select_shop;
    private TextView tv_uncheckPrice;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MineFragmentView = inflater.inflate(R.layout.shop_fragment_mine, null);
        return MineFragmentView;
    }

    @Override
    //当绑定的activity创建的时候
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        context = getActivity();
        if (context != null) {
            //获取本地的个人信息
            sp = ((MainActivity) context).sp;
        }
        initFragemntView();
        
    }


    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd("MineFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart("MineFragment");
        //返回页面时，重新加载数据
        LogUtil.d("info","reload");
        swiperefresh.setProgressViewOffset(false, 0, Util.dip2px(context, 24));//第一次启动时刷新
        swiperefresh.setRefreshing(true);
        requetConnetion();
        
    }
    private void requetConnetion()
    {
        String shopid = sp.getString("shopid", "");
        //请求网络连接之前，设置保存cookie，
        String url = ConstantParamPhone.CHECK_SHOP;
        HttpUtils.getConnection(context, null, url+shopid, "get", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                Toast.makeText(context,"网络故障，请重试",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                try {
                    JSONObject object = new JSONObject(s);
                    String code =  object.getString("code");
                    if ("SUCCESS".equalsIgnoreCase(code)){
                        //数据请求成功
                        InfoBean infoBean = new Gson().fromJson(s, InfoBean.class);
                        //保存用户信息
                        //1.通过sp保存用户信息
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("username",infoBean.getUser().getName())
                                .putString("nickename",infoBean.getUser().getNickname())//手动添加
                                .putString("mobile",infoBean.getUser().getMobile())
                                //2.存储商店信息
                                .putString("id",infoBean.getShop().getId())
                                .putString("shopLogo",infoBean.getShop().getLogo())
                                .putString("shopName",infoBean.getShop().getName())
                                //3.储存今日状态
                                .putString("todayIncome",infoBean.getTodaystat().getIncome())
                                //4.存储总计状态
                                .putString("frozenMoney",infoBean.getTotalstat().getFreezemoney())
                                .putString("useableMoney",infoBean.getTotalstat().getUsablemoney())
                                //在后台处理
                                .apply();
                    }else {
                        //数据请求失败
                        String msg = object.getString("msg");
                        //Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFinish() {
                super.onFinish();
                //更新数据
                initFragemntView();
                swiperefresh.setRefreshing(false);
            }
        });

    }
  

    //初始化页面显示
    private void initFragemntView() {
        ll_manager_status = (LinearLayout) MineFragmentView.findViewById(R.id.ll_manager_status);//今日状态
        logoutButton = (Button) MineFragmentView.findViewById(R.id.logoutButton);//退出
        logoutButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDialog.showDialog(context, "  退出登录将清空用户信息，是否退出",true, true, "取消","确定", new OnClickListener()
                        {
                            @Override
                            //取消退出
                            public void onClick(View v)
                            {
                                MyDialog.cancel();
                            }
                        },
                        new OnClickListener()
                        {
                            @Override
                            //确认退出
                            public void onClick(View v)
                            {
                                sp.edit().clear().commit();
                                CleanAppCache.cleanApplicationData(context);
                                DuduHelperApplication.getInstance().exit();
                                requestLogOut();
                            }
                        });
            }
        });
        select_shop = (RelativeLayout) MineFragmentView.findViewById(R.id.select_shop);//选择店铺
        select_shop.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ChangeShopActivity.class));
            }
        });
        final boolean isManager = sp.getBoolean("isManager", false);
        if (!isManager){
            ll_manager_status.setVisibility(View.GONE);
            logoutButton.setVisibility(View.VISIBLE);
            select_shop.setVisibility(View.VISIBLE);
        }else {
            logoutButton.setVisibility(View.GONE);
            select_shop.setVisibility(View.GONE);
            ll_manager_status.setVisibility(View.VISIBLE);
        }


        RelativeLayout getCashButtonRel = (RelativeLayout) MineFragmentView.findViewById(R.id.getCashButtonRel);
        final LinearLayout viewById = (LinearLayout) MineFragmentView.findViewById(R.id.lin1);
        swiperefresh = (SwipeRefreshLayout) MineFragmentView.findViewById(R.id.swiperefresh);
        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requetConnetion();
            }
        });
        final RelativeLayout mineheadRelLine = (RelativeLayout) MineFragmentView.findViewById(R.id.mineheadRelLine);
        final RelativeLayout helpRel = (RelativeLayout) MineFragmentView.findViewById(R.id.helpRel);
        //关于我们
        final RelativeLayout aboutRel = (RelativeLayout) MineFragmentView.findViewById(R.id.aboutRel);
        //二维码
        final RelativeLayout qcodeImgRel = (RelativeLayout) MineFragmentView.findViewById(R.id.qcodeImgRel);
        final RelativeLayout relupdate = (RelativeLayout) MineFragmentView.findViewById(R.id.relupdate);
        final TextView shopeNameTextView = (TextView) MineFragmentView.findViewById(R.id.shopeNameTextView);
        //fangkeNumText=(TextView) MineFragmentView.findViewById(R.id.fangkeNumText);
        final TextView tv_frozen_num_mine = (TextView) MineFragmentView.findViewById(R.id.tv_frozen_num_mine);
        //orderNumText=(TextView) MineFragmentView.findViewById(R.id.orderNumText);
        final TextView earnMoneyTextView = (TextView) MineFragmentView.findViewById(R.id.earnMoneyTextView);
        final TextView getCashMoneyTextView = (TextView) MineFragmentView.findViewById(R.id.getCashMoneyTextView);
        final TextView mineText = (TextView) MineFragmentView.findViewById(R.id.mineText);
        mineImageHead = (ImageView) MineFragmentView.findViewById(R.id.mineImageHead);
        final  TextView shopePhoneTextView = (TextView) MineFragmentView.findViewById(R.id.shopePhoneTextView);
        //初始化图片
        ImageLoader.getInstance().displayImage(sp.getString("shopLogo",""), mineImageHead);
        //滚动条
        final OverScrollView mineScrollview = (OverScrollView) MineFragmentView.findViewById(R.id.mineScrollview);
        //银行卡页面
        final RelativeLayout bankCardRel = (RelativeLayout) MineFragmentView.findViewById(R.id.bankCardRel);
        tv_uncheckPrice = (TextView) MineFragmentView.findViewById(R.id.tv_uncheckPrice);//未核销金额

        //提现按钮
        getCashButtonRel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!sp.getBoolean("isManager",false)){
                    Toast.makeText(context,"您没有管理权限",Toast.LENGTH_SHORT).show();
                    return;
                    
                }else {
                    Intent intent = new Intent(context, ShopBankListActivity.class);
                    intent.putExtra("action", "tixian");
                    startActivity(intent);
                }
            }
        });
        //个人中心按钮
        mineheadRelLine.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if(isManager){
                    Intent intent = new Intent(context, ShopSettingActivity.class);
                    startActivity(intent);

                }
            }
        });
        //帮助中心按钮
        helpRel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (context != null) {
                    Intent intent = new Intent(context, WebPageActivity.class);
                    intent.putExtra("action", "help");
                    startActivity(intent);
                }
            }
        });
        //关于我们按钮
        aboutRel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (context != null) {
                    Intent intent = new Intent(context, WebPageActivity.class);
                    intent.putExtra("action", "about");
                    startActivity(intent);
                }
            }
        });

        //初始化个人信息，通过三级缓存？不需要，登陆以后自动保存信息，所以sp必然有数据
        if (context != null) {
            shopeNameTextView.setText(sp.getString("shopName", ""));
            //防止imageLoader闪动
            ImageAware imageAware = new ImageViewAware(mineImageHead, false);
            //动态显示图片
            imageLoader.displayImage(sp.getString("shopLogo", ""), imageAware);
            //冻结金额
            tv_frozen_num_mine.setText(sp.getString("frozenMoney", ""));
            //显示今日收入
            earnMoneyTextView.setText(sp.getString("todayIncome", "") + "↑");
            LogUtil.d("shouru",sp.getString("todayIncome", ""));
            //可提现金额
            getCashMoneyTextView.setText(sp.getString("useableMoney", ""));
            tv_uncheckPrice.setText(sp.getString("uncheckPrice",""));
            //初始化手机号码
            shopePhoneTextView.setText(sp.getString("mobile",""));
        }
        //收款二维码页面
        qcodeImgRel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, WXEntryActivity.class);
                intent.putExtra("type", ConstantParamPhone.GET_CASHIER_CODE);
                startActivity(intent);
            }
        });
      
        //滑动监听按钮
        mineScrollview.setOverScrollTinyListener(new OverScrollTinyListener() {
            Boolean isDown = false;
            //恢复
            @Override
            public void scrollLoosen() {
                //initData();
            }

            @Override
            public void scrollDistance(int tinyDistance, int totalDistance) {

            }
        });

        //银行卡
        bankCardRel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!sp.getBoolean("isManager",false)){
                    Toast.makeText(context,"您没有管理权限",Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    if (TextUtils.isEmpty(sp.getString("mobile", ""))) {
                        MyDialog.showDialog(context, "尚未绑定手机号，是否绑定手机号", true, true, "确定", "取消", new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                Intent intent = new Intent(context, LoginBindPhoneActivity.class);
                                startActivity(intent);
                            }
                        }, new OnClickListener() {

                            @Override
                            public void onClick(View arg0) {
                                MyDialog.cancel();
                            }
                        });
                    } else {
                        //进入银行卡页面
                        Intent intent = new Intent(context, ShopBankListActivity.class);
                        intent.putExtra("action", "banklist");
                        startActivity(intent);
                        //
                    }
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {

            } else if (resultCode == Activity.RESULT_CANCELED) {
                ColorDialog.dissmissProcessDialog();
            }
        }
    }

    /**
     * 联网请求退出
     */
    private void requestLogOut() {
        RequestParams params = new RequestParams();

        HttpUtils.getConnection(context, params, ConstantParamPhone.LOG_OUT, "post", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {

            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                Toast.makeText(context,"已经退出当前账户，请重新登陆",Toast.LENGTH_LONG).show();
            }
        });
    }
    
   
}
