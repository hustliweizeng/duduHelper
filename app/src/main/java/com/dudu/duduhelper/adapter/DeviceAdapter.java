package com.dudu.duduhelper.adapter;


import java.util.ArrayList;
import java.util.List;

import com.dudu.duduhelper.R;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class DeviceAdapter extends BaseAdapter
{
	private Context context;
	private TextView textView;
	private List<BluetoothDevice> list=new ArrayList<BluetoothDevice>();
    public DeviceAdapter(Context context)
    {
    	this.context=context;
    }
    public void addItem(BluetoothDevice device)
    {
    	list.add(device);
    	notifyDataSetChanged();
    }
	@Override
	public int getCount() 
	{
		return list.size();
	}

	@Override
	public BluetoothDevice getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		convertView = LayoutInflater.from(context).inflate(R.layout.activity_divices_item, null);
		textView=(TextView) convertView.findViewById(R.id.textView1);
		textView.setText(list.get(position).getName());
		return convertView;
	}
	public void clear() {
		list.clear();
		notifyDataSetChanged();
	}

}
