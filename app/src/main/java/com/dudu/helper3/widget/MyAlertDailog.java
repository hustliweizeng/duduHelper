package com.dudu.helper3.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.dudu.duduhelper.R;

/**
 * Created by lwz on 2016/8/26.
 */
public class MyAlertDailog {

    public static AlertDialog dailog = null;
    private static  TextView tv_title_alertdailog;
    private static ListView lv_alertdailog;
    private static ImageView iv_canle_alertdailog;

    /**
     * @param title
     * @param adapter
     */
    public static void show(Context context, String title, BaseAdapter adapter) {
        dailog = new AlertDialog.Builder(context).create();
        dailog.show();
        //获取window之前必须先show
        Window window = dailog.getWindow();
        window.setContentView(R.layout.alertdailog_choose);
        tv_title_alertdailog = (TextView) window.findViewById(R.id.tv_title_alertdailog);
        lv_alertdailog = (ListView) window.findViewById(R.id.lv_alertdailog);
        iv_canle_alertdailog = (ImageView) window.findViewById(R.id.iv_canle_alertdailog);

        tv_title_alertdailog.setText(title);
        lv_alertdailog.setAdapter(adapter);
        //设置条目点击事件
        lv_alertdailog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int poistion, long id) {
                if (listentner!=null){
                    listentner.Onclick(poistion);
                    dailog.cancel();
                }
            }
        });
        //关闭按钮
        iv_canle_alertdailog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //关闭当前对话框
                dailog.cancel();
            }
        });

    }
    //谁有数据，谁提供接口对外暴露
    private    static  OnItemClickListentner listentner;
    public interface OnItemClickListentner{
        public void Onclick(int posotion);
    }
    public  static  void setOnItemClickListentner(OnItemClickListentner listentner){
        MyAlertDailog.listentner = listentner;
    }
}
