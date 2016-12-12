package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.dudu.duduhelper.Activity.ShopManageActivity.ShopStatusActivity;
import com.dudu.duduhelper.Utils.LogUtil;
import com.dudu.duduhelper.javabean.ShopListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/12/9
 */

public class MyCommonNavigatorAdapter extends PagerAdapter {
	private List<ShopListBean.DataBean> mDataList;//全部门店信息
	private Context context;
	private  List<ShopListBean.DataBean> displaylist = new ArrayList<>() ;//当前显示的商店列表
	private  List<ShopListBean.DataBean> openList = new ArrayList<>();//开业列表
	private  List<ShopListBean.DataBean> checkList = new ArrayList<>();;//审核列表
	private  List<ShopListBean.DataBean> stopList = new ArrayList<>();;
	private ListView listView;
	private ShopAdapterAdapter adapter;
	private boolean isDetail;
	private int currentPos;

	public MyCommonNavigatorAdapter(Context context, List<ShopListBean.DataBean> dataList) {
		mDataList = dataList;
		this.context = context;
		/**
		 * 把门店信息归类到集合中
		 */
		for (ShopListBean.DataBean item :dataList){
			if (item.getStatus().equals("1")||item.getStatus().equals("01")||item.getStatus().equals("11")){
				if (!openList.contains(item)){
					openList.add(item);
				}
			}else if (item.getStatus().equals("2")||item.getStatus().equals("02")||item.getStatus().equals("12")){
				if (!checkList.contains(item)){
					checkList.add(item);
				}
			}else {
				stopList.add(item);
			}
		}
		
	}

	@Override
	//vp的数量
	public int getCount() {
		return mDataList == null ? 0 : 3;
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@Override
	/**
	 * 显示viewpager的内容,根据导航页的位置，显示不同的页面内容,会预加载！
	 */
	public Object instantiateItem(ViewGroup container, final int pageNum) {
		LogUtil.d("init_page","pos="+pageNum);
		/**
		 * 如果把初始化放到构造方法处会报错误，alerady has child..
		 */
		adapter = new ShopAdapterAdapter(context);
		listView = new ListView(context);
		listView.setDivider(null);//分割线为空
		isDetail = false;//是否显示详情数据
		switch (pageNum){
			case 0:
				displaylist = openList;
				isDetail = true;
				break;
			case 1:
				displaylist = checkList;
				break;
			case 2:
				displaylist = stopList;
				break;
		}
		/**
		 * 显示列表
		 */
		adapter.addAll(displaylist,isDetail);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(context, ShopStatusActivity.class);
				/**
				 * 因为预加载的原因，所以displaylist的数据并不一定是当前页的列表，所以在传递数据之前
				 * 要做判断
				 */
				if (currentPos == 0){
					displaylist = openList;
				}else if (currentPos ==1){
					displaylist = checkList;
				}else {
					displaylist = stopList;
				}
				LogUtil.d("currentPos","currentPos="+currentPos);
				intent.putExtra("detail",displaylist.get(position));
				intent.putExtra("isDetail",isDetail);
				context.startActivity(intent);
			}
		});
		container.addView(listView);
		return  listView;
		
	}

	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		LogUtil.d("remove_page","pos="+position);
		container.removeView((View) object);
	}

	@Override
	public int getItemPosition(Object object) {
		TextView textView = (TextView) object;
		String text = textView.getText().toString();
		LogUtil.d("title","title="+text);
		int index = -1;
		switch (text){
			case "营业中":
				index = 0;
				break;
			case "审核中":
				index = 1;
				break;
			case "停业中":
				index = 2;
				break;
		}
		if (index>=0){
			return  index;
		}
		return POSITION_NONE;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		String name = null;
		switch (position){
			case 0:
				name = "营业中";
				break;
			case 1:
				name = "审核中";
				break;
			case 2:
				name = "停业中";
				break;
		}
		return name;
	}

	public void setCurrentPage(int position) {
		this.currentPos = position;
	}
}
