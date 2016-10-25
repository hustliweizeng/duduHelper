package com.dudu.duduhelper.fragment;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.Activity.WelcomeActivity.LoginActivity;
import com.dudu.duduhelper.Activity.WelcomeActivity.LoginBindPhoneActivity;
import com.dudu.duduhelper.Activity.MainActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Activity.MyInfoActivity.ShopBankListActivity;
import com.dudu.duduhelper.Activity.MyInfoActivity.ShopSettingActivity;
import com.dudu.duduhelper.Activity.MyInfoActivity.WebPageActivity;
import com.dudu.duduhelper.Utils.LogUtil;
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
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.umeng.analytics.MobclickAgent;
import com.umeng.update.UmengUpdateAgent;
import com.umeng.update.UmengUpdateListener;
import com.umeng.update.UpdateResponse;
import com.umeng.update.UpdateStatus;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;


public class ShopMineFragment extends Fragment {

    private View MineFragmentView;

    private DisplayImageOptions options;
    protected ImageLoader imageLoader = ImageLoader.getInstance();
    private BluetoothAdapter bluetoothAdapter;
    private String methord;
    private RelativeLayout getCashButtonRel;
    private SharedPreferences sp;
    private ImageView mineImageHead;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MineFragmentView = inflater.inflate(R.layout.shop_fragment_mine, null);
        return MineFragmentView;
    }

    @Override
    //当绑定的activity创建的时候
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getActivity() != null) {
            //获取本地的个人信息
            sp = ((MainActivity) getActivity()).sp;
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
        requetConnetion();
        
    }
    private void requetConnetion()
    {
        RequestParams params = new RequestParams();
        //请求网络连接之前，设置保存cookie，
        String url = ConstantParamPhone.GET_USER_INFO;
        HttpUtils.getConnection(getActivity(), params, url, "POST", new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                Toast.makeText(getActivity(),"网络故障，请重试",Toast.LENGTH_LONG).show();
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
                        LogUtil.d("welcome",s);
                    }else {
                        //数据请求失败
                        String msg = object.getString("msg");
                        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
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
            }
        });

    }

    //初始化页面显示
    private void initFragemntView() {
        RelativeLayout getCashButtonRel = (RelativeLayout) MineFragmentView.findViewById(R.id.getCashButtonRel);
        final LinearLayout viewById = (LinearLayout) MineFragmentView.findViewById(R.id.lin1);
        final RelativeLayout viewById1 = (RelativeLayout) MineFragmentView.findViewById(R.id.rel2);
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

        //提现按钮
        getCashButtonRel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShopBankListActivity.class);
                intent.putExtra("action", "tixian");
                startActivity(intent);
            }
        });
        //个人中心按钮
        mineheadRelLine.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ShopSettingActivity.class);
                startActivity(intent);
            }
        });
        //帮助中心按钮
        helpRel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), WebPageActivity.class);
                    intent.putExtra("action", "help");
                    startActivity(intent);
                }
            }
        });
        //关于我们按钮
        aboutRel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), WebPageActivity.class);
                    intent.putExtra("action", "about");
                    startActivity(intent);
                }
            }
        });

        //初始化个人信息，通过三级缓存？不需要，登陆以后自动保存信息，所以sp必然有数据
        if (getActivity() != null) {
            shopeNameTextView.setText(sp.getString("shopName", ""));
            //防止imageLoader闪动
            ImageAware imageAware = new ImageViewAware(mineImageHead, false);
            //动态显示图片
            imageLoader.displayImage(sp.getString("shopLogo", ""), imageAware);
            //冻结金额
            tv_frozen_num_mine.setText(sp.getString("frozenMoney", ""));
            //显示今日收入
            earnMoneyTextView.setText(sp.getString("todayIncome", "") + "↑");
            //可提现金额
            getCashMoneyTextView.setText(sp.getString("useableMoney", ""));
            //初始化手机号码
            shopePhoneTextView.setText(sp.getString("mobile",""));
        }
        //收款二维码页面
        qcodeImgRel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WXEntryActivity.class);
                intent.putExtra("type", ConstantParamPhone.GET_CASHIER_CODE);
                startActivity(intent);
            }
        });
        //版本更新按钮
        relupdate.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                if (getActivity() != null) {
                    UmengUpdateAgent.setUpdateAutoPopup(false);
                    UmengUpdateAgent.setUpdateListener(new UmengUpdateListener() {
                        @Override
                        public void onUpdateReturned(int updateStatus, UpdateResponse updateInfo) {
                            switch (updateStatus) {
                                case UpdateStatus.Yes: // has update
                                    UmengUpdateAgent.showUpdateDialog(getActivity(), updateInfo);
                                    break;
                                case UpdateStatus.No: // has no update
                                    if (getActivity() != null) {
                                        Toast.makeText(getActivity(), "没有更新", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                //				        case UpdateStatus.NoneWifi: // none wifi
                                //				            Toast.makeText(mContext, "没有wifi连接， 只在wifi下更新", Toast.LENGTH_SHORT).show();
                                //				            break;
                                case UpdateStatus.Timeout: // time out
                                    Toast.makeText(getActivity(), "超时", Toast.LENGTH_SHORT).show();
                                    break;
                            }
                        }

                    });
                    UmengUpdateAgent.update(getActivity());
                }
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
                if (TextUtils.isEmpty(sp.getString("mobile", ""))) {
                    MyDialog.showDialog(getActivity(), "尚未绑定手机号，是否绑定手机号", true, true, "确定", "取消", new OnClickListener() {

                        @Override
                        public void onClick(View arg0) {
                            Intent intent = new Intent(getActivity(), LoginBindPhoneActivity.class);
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
                    Intent intent = new Intent(getActivity(), ShopBankListActivity.class);
                    intent.putExtra("action", "banklist");
                    startActivity(intent);
                    //
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK) {

            } else if (resultCode == getActivity().RESULT_CANCELED) {
                ColorDialog.dissmissProcessDialog();
            }
        }
    }
    
   
}
