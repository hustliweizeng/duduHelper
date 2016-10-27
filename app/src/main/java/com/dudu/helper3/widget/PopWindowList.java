package com.dudu.helper3.widget;

import org.apache.http.Header;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.adapter.ShopListAdapter;
import com.dudu.helper3.bean.GetHongBaoHistBean;
import com.dudu.helper3.http.ConstantParamPhone;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

public class PopWindowList
{
	private PopupWindow popupWindow;
	private ListView shopPopListView;
	private ImageView closePopButton;
	private BaseActivity baseActivity;
	private SwipeRefreshLayout shopListRef;
	private ShopListAdapter hongbaoHistoryAdapter;
	private OnItemClickLinster onItemClickLinster;
	private GetHongBaoHistBean getHongBaoHistBean;
	//private View footView;
	@SuppressLint("ResourceAsColor") 
	public PopWindowList(BaseActivity context) 
	{
		this.baseActivity = context;
		LayoutInflater layoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        View view = layoutInflater.inflate(R.layout.shop_select_pop_window, null);  
        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,  LayoutParams.MATCH_PARENT);  
        popupWindow.setFocusable(true);  
        popupWindow.setOutsideTouchable(true);  
        //这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景  
        popupWindow.setBackgroundDrawable(new ColorDrawable(0xb0000000)); 
        //设置popWindow的显示和消失动画
        popupWindow.setAnimationStyle(R.style.mypopwindow_anim_style);
        popupWindow.showAsDropDown(context.findViewById(R.id.head));
        shopPopListView =(ListView) view.findViewById(R.id.shopPopListView);
        closePopButton=(ImageView) view.findViewById(R.id.closePopButton);
        shopListRef = (SwipeRefreshLayout) view.findViewById(R.id.shopListRef);
        //footView = LayoutInflater.from(baseActivity).inflate(R.layout.activity_listview_foot, null);
        hongbaoHistoryAdapter=new ShopListAdapter(this.baseActivity);
        shopPopListView.setAdapter(hongbaoHistoryAdapter);
        //shopPopListView.addFooterView(footView,null,false);
        shopListRef = (SwipeRefreshLayout)view.findViewById(R.id.shopListRef);
        shopListRef.setColorSchemeResources(R.color.text_color);
        shopListRef.setSize(SwipeRefreshLayout.DEFAULT);
        shopListRef.setProgressBackgroundColor(R.color.bg_color);
        shopPopListView.setOnItemClickListener(new OnItemClickListener() 
		{
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				onItemClickLinster.onClick(getHongBaoHistBean.getData().get(position).getNickname());
			}
		});
        
        shopListRef.setOnRefreshListener(new OnRefreshListener() 
		{
			
			@Override
			public void onRefresh() 
			{
				hongbaoHistoryAdapter.clear();
				getDataList();
			}
		});        
        
        closePopButton.setOnClickListener(new OnClickListener() 
        {
			@Override
			public void onClick(View arg0) 
			{
				dismiss();
			}
		});
        getDataList();
	}
	
	public void getDataList()
	{
		RequestParams params = new RequestParams();
		params.add("token", baseActivity.share.getString("token", ""));
		params.add("page",String.valueOf(1));
		params.add("pagesize","100");
		params.add("id","90");
		params.setContentEncoding("UTF-8");
		AsyncHttpClient client = new AsyncHttpClient();
		//保存cookie，自动保存到了shareprefercece  
        PersistentCookieStore myCookieStore = new PersistentCookieStore(baseActivity);    
        client.setCookieStore(myCookieStore); 
        client.post(ConstantParamPhone.IP+ConstantParamPhone.GET_HONGBAOHIST_LIST, params,new TextHttpResponseHandler()
		{

			@Override
			public void onFailure(int arg0, Header[] arg1, String arg2,Throwable arg3) 
			{
				
			}
			@Override
			public void onSuccess(int arg0, Header[] arg1, String arg2) 
			{
				getHongBaoHistBean=new Gson().fromJson(arg2,GetHongBaoHistBean.class);
				if(!getHongBaoHistBean.getStatus().equals("0"))
				{
					
				}
				else
				{
					if(getHongBaoHistBean.getData()!=null&&getHongBaoHistBean.getData().size()!=0)
					{
						hongbaoHistoryAdapter.addAll(getHongBaoHistBean.getData());
					}
					else
					{
						
					}
				}
			}
			@Override
			public void onFinish() 
			{
				// TODO Auto-generated method stub
				shopListRef.setRefreshing(false);
			}
		});
	}
	
	public void dismiss()
	{
		popupWindow.dismiss();
	}
	
	public void setOnItemClickLinster(OnItemClickLinster onItemClickLinster)
	{
		this.onItemClickLinster = onItemClickLinster;
	}
	
	public interface OnItemClickLinster
	{
		void onClick(String shopName);
	}

}
