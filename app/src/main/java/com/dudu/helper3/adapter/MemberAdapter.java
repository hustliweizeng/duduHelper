package com.dudu.helper3.adapter;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.dudu.duduhelper.R;
import com.dudu.helper3.http.ConstantParamPhone;
import com.dudu.helper3.javabean.ShopUserBean;
import com.dudu.helper3.widget.ColorDialog;
import com.dudu.helper3.widget.MyDialog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.TextHttpResponseHandler;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MemberAdapter extends BaseAdapter 
{
	private Context context;
    private ViewHolder viewHolder;
    List<ShopUserBean.DataBean> list=new ArrayList<ShopUserBean.DataBean>();
    //private OnClick listener=null;
    
    public MemberAdapter(Context context)
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
    public void addAll(List<ShopUserBean.DataBean> list)
    {
    	this.list.addAll(this.list.size(), list);
    	notifyDataSetChanged();
    }

	@Override
	public ShopUserBean.DataBean getItem(int arg0) 
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
			convertView = LayoutInflater.from(context).inflate(R.layout.activity_member_item, null);
			viewHolder.membername=(TextView) convertView.findViewById(R.id.membername);
			viewHolder.membercount=(TextView) convertView.findViewById(R.id.membercount);
			viewHolder.delectButton=(ImageButton) convertView.findViewById(R.id.delectButton);
			viewHolder.listener=new OnClick();
			viewHolder.delectButton.setOnClickListener(viewHolder.listener);
			convertView.setTag(viewHolder);
		}
		else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if(list.size()!=0)
		{
			if(!TextUtils.isEmpty(list.get(position).getName()))
			{
				viewHolder.membercount.setText("账号："+list.get(position).getName());
			}
			if(!TextUtils.isEmpty(list.get(position).getShop_name()))
			{
				viewHolder.membername.setText("姓名："+list.get(position).getShop_name());
			}
			if(!TextUtils.isEmpty(list.get(position).getId()))
			{
				viewHolder.listener.setIdAndPostion(list.get(position).getId(),list.get(position));
			}
		}
		return convertView;
	}
	private class ViewHolder
	{
		TextView membername;
		TextView membercount;
		ImageButton delectButton;
        OnClick listener;
	}
	//删除按钮的点击事件
	private class OnClick implements OnClickListener
	{
		private String id;
		private ShopUserBean.DataBean item;
		public void setIdAndPostion(String id,ShopUserBean.DataBean item) {
			this.id = id;
			this.item = item;
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
					AsyncHttpClient client = new AsyncHttpClient();
					//保存cookie，自动保存到了shareprefercece  
			        PersistentCookieStore myCookieStore = new PersistentCookieStore(context);    
			        client.setCookieStore(myCookieStore); 
			        client.delete(ConstantParamPhone.BASE_URL+ConstantParamPhone.DEL_USER+id, null,new TextHttpResponseHandler()
					{

						@Override
						public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
						{
							Toast.makeText(context, "网络不给力呀", Toast.LENGTH_LONG).show();
						}
						@Override
						public void onSuccess(int arg0, Header[] arg1, String arg2) 
						{

							try {
								JSONObject object = new JSONObject(arg2);
								String code =  object.getString("code");
								if ("SUCCESS".equalsIgnoreCase(code)){
									//数据请求成功
									MyDialog.cancel();
									//集合中删除该数据
									list.remove(item);
									notifyDataSetChanged();

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
						public void onFinish() 
						{
							// TODO Auto-generated method stub
							ColorDialog.dissmissProcessDialog();
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
