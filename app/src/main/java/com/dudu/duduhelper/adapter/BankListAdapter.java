package com.dudu.duduhelper.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.dudu.duduhelper.LoginActivity;
import com.dudu.duduhelper.ShopMemberListActivity;
import com.dudu.duduhelper.shopProductListActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.bean.GetCouponSellDataBean;
import com.dudu.duduhelper.bean.MemberBean;
import com.dudu.duduhelper.bean.MemberDataBean;
import com.dudu.duduhelper.bean.ResponsBean;
import com.dudu.duduhelper.http.ConstantParamPhone;
import com.dudu.duduhelper.widget.CircleImageView;
import com.dudu.duduhelper.widget.ColorDialog;
import com.dudu.duduhelper.widget.MyDialog;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class BankListAdapter extends BaseAdapter 
{
	private Context context;
    private ViewHolder viewHolder;
    List<MemberDataBean> list=new ArrayList<MemberDataBean>();
    //private OnClick listener=null;
    
    public BankListAdapter(Context context)
	{
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return list.size();
	}
	public void clear()
    {
    	list.clear();
    	notifyDataSetChanged();
    }
    public void addAll(List<MemberDataBean> list)
    {
    	this.list.addAll(this.list.size(), list);
    	notifyDataSetChanged();
    }

	@Override
	public MemberDataBean getItem(int arg0) 
	{
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) 
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		if(convertView == null)
		{
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.shop_bank_item, null);
			viewHolder.bankNameText=(TextView) convertView.findViewById(R.id.bankNameText);
			viewHolder.bankCardNum=(TextView) convertView.findViewById(R.id.bankCardNum);
//			viewHolder.delectButton=(ImageButton) convertView.findViewById(R.id.delectButton);
//			viewHolder.listener=new OnClick();
//			viewHolder.delectButton.setOnClickListener(viewHolder.listener);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(list.size()!=0)
		{
			if(!TextUtils.isEmpty(list.get(position).getUsername()))
			{
				//viewHolder.membercount.setText("账号："+list.get(position).getUsername());
			}
			if(!TextUtils.isEmpty(list.get(position).getNickname()))
			{
				//viewHolder.membername.setText("姓名："+list.get(position).getNickname());
			}
//			if(!TextUtils.isEmpty(list.get(position).getId()))
//			{
//				viewHolder.listener.setIdAndPostion(list.get(position).getId(),position);
//			}
		}
		return convertView;
	}
	private class ViewHolder
	{
		TextView bankNameText;
		TextView bankCardNum;
		TextView bankCardUsername;
		CircleImageView banklogoImg;
        OnClick listener;
	}
	private class OnClick implements OnClickListener
	{
		private String id;
		private int postion;
		

		public void setIdAndPostion(String id,int postion) {
			this.id = id;
			this.postion=postion;
		}

		@Override
		public void onClick(View v) 
		{
			// TODO Auto-generated method stub
			MyDialog.showDialog(context, "是否删除该店员", true, true, "确定", "取消", new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ColorDialog.showRoundProcessDialog(context,R.layout.loading_process_dialog_color);
					RequestParams params = new RequestParams();
					params.add("token", ((ShopMemberListActivity)context).share.getString("token", ""));
					params.add("id",id);
					params.add("version", ConstantParamPhone.VERSION);
					params.setContentEncoding("UTF-8");
					AsyncHttpClient client = new AsyncHttpClient();
					//保存cookie，自动保存到了shareprefercece  
			        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);    
			        client.setCookieStore(myCookieStore); 
			        client.post(ConstantParamPhone.IP+ConstantParamPhone.MEMBER_DELECT, params,new TextHttpResponseHandler()
					{

						@Override
						public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
						{
							Toast.makeText(context, "网络不给力呀", Toast.LENGTH_LONG).show();
						}
						@Override
						public void onSuccess(int arg0, Header[] arg1, String arg2) 
						{
							MemberBean responsBean=new Gson().fromJson(arg2,MemberBean.class);
							if(!responsBean.getStatus().equals("1"))
							{
								Toast.makeText(context, "出错啦！", Toast.LENGTH_LONG).show();
							}
							else
							{
								Toast.makeText(context, "删除成功啦！", Toast.LENGTH_LONG).show();
							}
							MyDialog.cancel();
						}
						@Override
						public void onFinish() 
						{
							// TODO Auto-generated method stub
							ColorDialog.dissmissProcessDialog();
							list.remove(postion);
							notifyDataSetChanged();
						}
					});
				}
			}, new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					MyDialog.cancel();
				}
			});
			
		}
		
	}

}
