package com.dudu.helper3.adapter;

import java.util.ArrayList;
import java.util.List;

import com.dudu.helper3.R;
import com.dudu.helper3.bean.HongbaoAddBean;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;

public class HongbaoAdapter extends BaseAdapter 
{
	private Context context;
	private ImageView delectButton;
	private EditText discountMoneyEdit;
	private EditText totalMoneyEdit;
	private List<HongbaoAddBean> list= new ArrayList<HongbaoAddBean>();
	private OnDelectHongBaoItemListener onDelectHongBaoItemListener;
	

	public HongbaoAdapter(Context context) 
	{
		// TODO Auto-generated constructor stub
		this.context=context;
		HongbaoAddBean hongbao=new HongbaoAddBean();
		hongbao.setTotalMoney("");
		hongbao.setDiscountMoney("");
		list.add(hongbao);
	}
	
	/**
     * 设置点击监听事件
     * @param onItemClickLitener
     */
    public void setOnDelectHongBaoItemListener(OnDelectHongBaoItemListener onDelectHongBaoItemListener)
    {
        this.onDelectHongBaoItemListener = onDelectHongBaoItemListener;
    }

	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
//		if(list.size()==0)
//		{
//			return 1;
//		}
//		else
//		{
			return list.size();
		//}
		
	}
	
	public void addHongbao(HongbaoAddBean hongbao)
	{
		list.add(hongbao);
		notifyDataSetChanged();
	}
	
	

	public List<HongbaoAddBean> getList() {
		return list;
	}


	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		convertView = LayoutInflater.from(context).inflate(R.layout.shop_activity_hongbao_item, null);
		delectButton=(ImageView) convertView.findViewById(R.id.delectButton);
		discountMoneyEdit=(EditText) convertView.findViewById(R.id.discountMoneyEdit);
		totalMoneyEdit=(EditText) convertView.findViewById(R.id.totalMoneyEdit);
		delectButton.setOnClickListener(new OnClickListener() 
		{	
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				list.remove(position);
				notifyDataSetChanged();
				onDelectHongBaoItemListener.onItemDelect();
			}
		});
		discountMoneyEdit.addTextChangedListener(new TextWatcher() 
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				// TODO Auto-generated method stub
				list.get(position).setDiscountMoney(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after) 
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		totalMoneyEdit.addTextChangedListener(new TextWatcher() 
		{
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				// TODO Auto-generated method stub
				list.get(position).setTotalMoney(s.toString());
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after)
			{
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) 
			{
				// TODO Auto-generated method stub
				
			}
		});
		if(!TextUtils.isEmpty(list.get(position).getDiscountMoney()))
		{
			discountMoneyEdit.setText(list.get(position).getDiscountMoney());
		}
		if(!TextUtils.isEmpty(list.get(position).getTotalMoney()))
		{
			totalMoneyEdit.setText(list.get(position).getTotalMoney());
		}
		return convertView;
	}
	
	//设置删除回调接口用于更新listView的高度
	public interface OnDelectHongBaoItemListener
	{
		void onItemDelect();
	}

	public void clear() 
	{
		// TODO Auto-generated method stub
		list.clear();
	}
}
