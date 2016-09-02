package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.javabean.CashHistoryBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lwz on 2016/8/17.
 */
public class MoneyHistoryAdapter extends BaseAdapter {

    public List<CashHistoryBean> list = new ArrayList<>();
    Context context;

    public MoneyHistoryAdapter(Context context) {
        this.context = context;

    }

    public void addAll(List<CashHistoryBean> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_checkhistroy, null);
            holder = new ViewHolder(null);
            holder.checkhistory_date = convertView.findViewById(R.id.checkhistory_date);
        }
        Log.d("adapter", "已经打印");
        
        
        
        
        
        return convertView;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    public static class ViewHolder {
        public View rootView;
        public TextView checkhistory_title;
        public ImageView checkhistory_img;
        public TextView checkhistory_name;
        public TextView checkhistory_id;
        public TextView checkhistory_date;

        public ViewHolder(View rootView) {
            this.rootView = rootView;
            this.checkhistory_title = (TextView) rootView.findViewById(R.id.checkhistory_title);
            this.checkhistory_img = (ImageView) rootView.findViewById(R.id.checkhistory_img);
            this.checkhistory_name = (TextView) rootView.findViewById(R.id.checkhistory_name);
            this.checkhistory_id = (TextView) rootView.findViewById(R.id.checkhistory_id);
            this.checkhistory_date = (TextView) rootView.findViewById(R.id.checkhistory_date);
        }

    }
}
