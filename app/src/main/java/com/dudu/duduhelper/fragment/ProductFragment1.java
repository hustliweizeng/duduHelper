package com.dudu.duduhelper.fragment;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import com.dudu.duduhelper.ShopProductAddActivity;
import com.dudu.duduhelper.R;
import com.dudu.duduhelper.adapter.ProductAdapter;
import com.dudu.duduhelper.adapter.ProductTypeAdapter;

import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ProductFragment1 extends Fragment 
{
	private View ProductFragmentView;
	private RelativeLayout allTypeRel;
	private TextView allType;
	private ImageView allTypeArror;
	private TextView orderType;
	private ImageView orderTypeArror;
	private TextView productAction;
	private ImageView productTypeArror;
	private RelativeLayout orderTypeRel;
	private RelativeLayout productRel;
	private LinearLayout selectLine;
	private PopupWindow popupWindow;
	//商品下拉选择数据加载
	private ProductTypeAdapter productTypeAdapter;
	private ProductAdapter productAdapter;
	private SwipeRefreshLayout productSwipeLayout;
	private ListView productListView;
	//编辑按钮
	private ImageButton editButton;
	//编辑按钮
	private ImageButton addButton;
	//编辑栏
	private LinearLayout editProductLine;
	//全选按钮
	private ImageButton productAllCheckImg;
	private boolean isMulChoice = false; //是否显示编辑界面
	private boolean isAllChoice = false;//是否全选
    private List<String> selectid = new ArrayList<String>();//选中的id
    //private View foot;
	//商品列表加载
	@Override
	public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		ProductFragmentView= inflater.inflate(R.layout.fragment_product1, null);
		productTypeAdapter=new ProductTypeAdapter(getActivity());
		//productAdapter=new ProductAdapter(getActivity(),isMulChoice,false);
		return ProductFragmentView;
	}
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) 
	{
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		initViewFragment();
		initData();
	}
	//加载商品数据
	private void initData() 
	{
		// TODO Auto-generated method stub
		productListView.setAdapter(productAdapter);
	}
	@SuppressLint("ResourceAsColor") 
	private void initViewFragment() 
	{
		// TODO Auto-generated method stub
		productAllCheckImg=(ImageButton) ProductFragmentView.findViewById(R.id.productAllCheckImg);
		editProductLine=(LinearLayout) ProductFragmentView.findViewById(R.id.editProductLine);
		addButton=(ImageButton) getActivity().findViewById(R.id.selectClickButton);
		addButton.setVisibility(View.VISIBLE);
		addButton.setImageResource(R.drawable.icon_tianjia);
		
		//重写父类button,点击弹出checkbox
		editButton=(ImageButton) getActivity().findViewById(R.id.backButton);
		editButton.setImageResource(R.drawable.icon_piliang);
		editButton.setPadding(28, 30, 26, 28);
		editButton.setScaleType(ScaleType.CENTER_INSIDE);
		editButton.setVisibility(View.VISIBLE);
		
		
		
		//重写父类的foot的显隐性
	    //foot=(View) getActivity().findViewById(R.id.foot);
	    
		productSwipeLayout=(SwipeRefreshLayout) ProductFragmentView.findViewById(R.id.productSwipeLayout);
		productSwipeLayout.setColorSchemeResources(R.color.text_color);
		productSwipeLayout.setSize(SwipeRefreshLayout.DEFAULT);
		productSwipeLayout.setProgressBackgroundColor(R.color.bg_color);
		productListView=(ListView) ProductFragmentView.findViewById(R.id.productListView);
		allType=(TextView) ProductFragmentView.findViewById(R.id.allType);
		allTypeArror=(ImageView) ProductFragmentView.findViewById(R.id.allTypeArror);
		orderType=(TextView) ProductFragmentView.findViewById(R.id.orderType);
		orderTypeArror=(ImageView) ProductFragmentView.findViewById(R.id.orderTypeArror);
		productAction=(TextView) ProductFragmentView.findViewById(R.id.productAction);
		productTypeArror=(ImageView) ProductFragmentView.findViewById(R.id.productTypeArror);
		selectLine=(LinearLayout) ProductFragmentView.findViewById(R.id.selectLine);
		productRel=(RelativeLayout) ProductFragmentView.findViewById(R.id.productRel);
		allTypeRel=(RelativeLayout) ProductFragmentView.findViewById(R.id.allTypeRel);
		orderTypeRel=(RelativeLayout) ProductFragmentView.findViewById(R.id.orderTypeRel);
		//弹出分类选择事件
		allTypeRel.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				allType.setTextColor(getActivity().getResources().getColor(R.color.text_color));
				allTypeArror.setImageResource(R.drawable.icon_jiantou_shang);
				orderType.setTextColor(getActivity().getResources().getColor(R.color.text_color_gray));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				productAction.setTextColor(getActivity().getResources().getColor(R.color.text_color_gray));
				productTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				showSelectPopupWindow();
			}

		});
		//弹出分类选择事件
		orderTypeRel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				orderType.setTextColor(getActivity().getResources().getColor(R.color.text_color));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_shang);
				allType.setTextColor(getActivity().getResources().getColor(R.color.text_color_gray));
				allTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				productAction.setTextColor(getActivity().getResources().getColor(R.color.text_color_gray));
				productTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				showSelectPopupWindow();
			}
		});
		//弹出分类选择事件
		productRel.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				productAction.setTextColor(getActivity().getResources().getColor(R.color.text_color));
				productTypeArror.setImageResource(R.drawable.icon_jiantou_shang);
				orderType.setTextColor(getActivity().getResources().getColor(R.color.text_color_gray));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				orderType.setTextColor(getActivity().getResources().getColor(R.color.text_color_gray));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				showSelectPopupWindow();
				
			}
		});
		//下拉刷新事件
		productSwipeLayout.setOnRefreshListener(new OnRefreshListener() 
		{
			
			@Override
			public void onRefresh() 
			{
				// TODO Auto-generated method stub
				
			}
		});
		//全选按钮点击事件
		productAllCheckImg.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				if(!isAllChoice)
				{
					isAllChoice=true;
					productAllCheckImg.setImageResource(R.drawable.icon_xuanze_sel);
					// TODO Auto-generated method stub
					for(int i=0;i<productAdapter.getCount();i++)
	                {
						//设置所有的checkbox可见，选中
						productAdapter.visiblecheck.put(i, CheckBox.VISIBLE);
						productAdapter.ischeck.put(i,true);
	                }
				}
				else
				{
					isAllChoice=false;
					productAllCheckImg.setImageResource(R.drawable.icon_xuanze);
					//设置所有的checkbox可见，不选中
					for(int i=0;i<productAdapter.getCount();i++)
	                {
						//设置所有的checkbox可见，不选中
						productAdapter.visiblecheck.put(i, CheckBox.VISIBLE);
						productAdapter.ischeck.put(i,false);
	                }
				}
				Iterator i = productAdapter.ischeck.entrySet().iterator(); 
				while(i.hasNext())
				{  
            	    Entry  entry=(Entry)i.next();  
            	    String key=String.valueOf( entry.getKey());  
            	    String value=String.valueOf( entry.getValue());  
            	    Log.i("info",key +"---"+ value);
            	}
				//更新视图状态
				productAdapter.notifyDataSetChanged();	
				
			}
		});
		//显示添加界面
		addButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) 
			{
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),ShopProductAddActivity.class);
				startActivity(intent);
			}
		});
		//显示编辑界面
		editButton.setOnClickListener(new OnClickListener() 
		{
			
			@Override
			public void onClick(View v) 
			{
				editButton.setImageResource(R.drawable.icon_quxiao);
				editButton.setPadding(20, 20, 20, 20);
				editButton.setScaleType(ScaleType.CENTER_INSIDE);
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "haha", Toast.LENGTH_SHORT).show();
				if(!isMulChoice)//如果不是编辑，点击进入编辑
				{
//					TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF,   
//		                    0.0f, Animation.RELATIVE_TO_SELF, 0.0f,   
//		                    Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,   
//		                    -1.0f);  
//		            mHiddenAction.setDuration(500);   
//		            foot.startAnimation(mHiddenAction);  
		            
					//foot.setVisibility(View.GONE);
					editProductLine.setVisibility(View.VISIBLE);
					isMulChoice = true;
					selectid.clear();
					//与adapter里的方法可能有重复
					for(int i=0;i<productAdapter.getCount();i++)
	                {
						//设置所有的checkbox可见，不选中
						productAdapter.visiblecheck.put(i, CheckBox.VISIBLE);
						productAdapter.ischeck.put(i,false);
	                }
				}
				else//如果正在编辑点击取消
				{
					editButton.setImageResource(R.drawable.icon_piliang);
					editButton.setPadding(20, 20, 20, 20);
					editButton.setScaleType(ScaleType.CENTER_INSIDE);
					//foot.setVisibility(View.VISIBLE);
					editProductLine.setVisibility(View.GONE);
					isMulChoice = false;
					selectid.clear();
					//与adapter里的方法可能有重复
					for(int i=0;i<productAdapter.getCount();i++)
	                {
						//设置所有的checkbox不可见，不选中
						productAdapter.visiblecheck.put(i, CheckBox.GONE);
						productAdapter.ischeck.put(i, false);
	                }
				}
				//重新加载适配器，刷新视图
//						productAdapter=new ProductAdapter(getActivity(),isMulChoice);
//		              productListView.setAdapter(productAdapter);
				
			}
		});
	}
	//弹出选择框
	private void showSelectPopupWindow() 
	{
		// TODO Auto-generated method stub
		LayoutInflater layoutInflater = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
        View view = layoutInflater.inflate(R.layout.activity_product_window_select, null);  
        popupWindow = new PopupWindow(view, LayoutParams.MATCH_PARENT,  LayoutParams.WRAP_CONTENT);  
        popupWindow.setFocusable(true);  
        popupWindow.setOutsideTouchable(true);  
        
        //设置半透明
//        WindowManager.LayoutParams params=getActivity().getWindow().getAttributes();  
//        params.alpha=0.7f;  
//        getActivity().getWindow().setAttributes(params);  
        
        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景  
        popupWindow.setBackgroundDrawable(new BitmapDrawable()); 
        
        int screenWidth=getActivity().getWindowManager().getDefaultDisplay().getWidth();
        int popWidth=popupWindow.getWidth();  
        popupWindow.showAsDropDown(selectLine);
        ListView productSelectList=(ListView) view.findViewById(R.id.productSelectList);
        productSelectList.setAdapter(productTypeAdapter);
        productSelectList.setOnItemClickListener(new OnItemClickListener() 
        {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) 
			{
				// TODO Auto-generated method stub
				popupWindow.dismiss();
			}
		});
        popupWindow.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss() 
			{
				// TODO Auto-generated method stub
				//设置半透明
//		        WindowManager.LayoutParams params=getActivity().getWindow().getAttributes();  
//		        params.alpha=1f;  
//		        getActivity().getWindow().setAttributes(params);  
				//重置所有按钮
				allType.setTextColor(getActivity().getResources().getColor(R.color.text_color_gray));
				allTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				orderType.setTextColor(getActivity().getResources().getColor(R.color.text_color_gray));
				orderTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
				productAction.setTextColor(getActivity().getResources().getColor(R.color.text_color_gray));
				productTypeArror.setImageResource(R.drawable.icon_jiantou_xia);
			}
		});
	}

}
