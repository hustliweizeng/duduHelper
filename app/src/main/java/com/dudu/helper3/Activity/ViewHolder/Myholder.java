package com.dudu.helper3.Activity.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dudu.helper3.R;
import com.dudu.helper3.adapter.GuestListCheckAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/24
 */

class Myholder extends RecyclerView.ViewHolder implements View.OnClickListener {
	View view;
	private ImageView iv_ischeck;
	private ImageView iv_item_guest;
	private TextView tv_title_item_guest;
	private TextView tv_totalprice_item_guest;
	private TextView tv_count_item_guest;
	private TextView tv_date_item_guest;
	GuestListCheckAdapter.OnItemClickListner listener;
	//复选框状态,默认是不选择
	private  boolean isChekcked = false;
	private List<String> list = new ArrayList<>();

	public Myholder(View itemView ,GuestListCheckAdapter.OnItemClickListner listener) {
		super(itemView);
		view = itemView;
		//把adapter的对象传递给holder
		this.listener = listener;
		//把view的监听事件转给holder处理
		itemView.setOnClickListener(this);
		iv_ischeck = (ImageView) itemView.findViewById(R.id.iv_ischeck);
		iv_item_guest= (ImageView) itemView.findViewById(R.id.iv_item_guest);
		tv_title_item_guest = (TextView) itemView.findViewById(R.id.tv_title_item_guest);
		tv_totalprice_item_guest = (TextView) itemView.findViewById(R.id.tv_totalprice_item_guest);
		tv_count_item_guest = (TextView) itemView.findViewById(R.id.tv_count_item_guest);
		tv_date_item_guest = (TextView) itemView.findViewById(R.id.tv_date_item_guest);
	}

	@Override
	//让holder绑定点击事件,触发回调
	public void onClick(View v) {
		if(listener != null){
			//事件回调
			listener.onItemClick(v,getLayoutPosition());
			//当该条目被点击后，设置图标状态
			if (isChekcked){
				iv_ischeck.setImageResource(R.drawable.select_check);
				list.add(getLayoutPosition()+"");
			}else {
				iv_ischeck.setImageResource(R.drawable.select_empty);
				list.remove(getLayoutPosition()+"");
			}

		}

	}
	public List<String> getGuestList(){
		if (list!=null && list.size()!=0){
			return list;
		}else {
			return null;
		}
	}

	/**
	 * 选中所有
	 */
	/*public  void addAll(){
		list.clear();
		for (int i=0;i<itemView.getItemCount(); i++){
			list.add(i+"");
		}
		//刷新所有状态
	}*/
}