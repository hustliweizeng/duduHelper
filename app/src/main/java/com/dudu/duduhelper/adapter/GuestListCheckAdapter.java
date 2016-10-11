package com.dudu.duduhelper.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/24
 */

public class GuestListCheckAdapter extends RecyclerView.Adapter {
	Context mContext;
	//选中的条目
	private ArrayList<CharSequence> list = new ArrayList<>();
	//是否全选
	private boolean isAll = false;

	public GuestListCheckAdapter(Context context) {
		mContext = context;
	}
	public ArrayList<CharSequence> getList(){
		return list;
	}
	public void setList(ArrayList<CharSequence> list){
		this.list = list;
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = View.inflate(mContext, R.layout.item_guest_checkable, null);
		Myholder myholder = new Myholder(view,mListener);
		return myholder;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		
		Myholder myholder = (Myholder)holder;
		if (list.contains(position+"")){
			LogUtil.d("che","yes");
			myholder.iv_ischeck.setImageResource(R.drawable.select_check);
		}else {
			LogUtil.d("che","no");

			myholder.iv_ischeck.setImageResource(R.drawable.select_empty);
		}
		
		
			
	}

	@Override
	public int getItemCount() {
		return 10;
	}

	class Myholder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private ImageView iv_ischeck;
		private ImageView iv_item_guest;
		private TextView tv_title_item_guest;
		private TextView tv_totalprice_item_guest;
		private TextView tv_count_item_guest;
		private TextView tv_date_item_guest;
		OnItemClickListner listener;
		//复选框状态,默认是不选择
		private  boolean isChekcked = false;
		

		public Myholder(View itemView ,OnItemClickListner listener) {
			super(itemView);
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
				String position = getLayoutPosition()+"";
				//当该条目被点击后，设置图标状态
				if (!list.contains(position)){
					list.add(position);
					LogUtil.d("ischeck","add");
				}else {
					list.remove(position);
					LogUtil.d("ischeck","remove");

				}
				notifyDataSetChanged();
			}

		}
		public List<CharSequence> getGuestList(){
			if (list!=null && list.size()!=0){
				return list;
			}else {
				return null;
			}
		}

		
	}
	/**
	 * 选中所有
	 */
	public  void addAll(){
		if (isAll){
			//取消全选
			list.clear();
			isAll = !isAll;
		}else {
			for (int i=0;i<getItemCount(); i++){
				list.add(i+"");
			}
			isAll = !isAll;
		}
		//刷新所有状态
		notifyDataSetChanged();
	}
	
	
	OnItemClickListner mListener;
	//为adapter编写条目点击事件
	public interface OnItemClickListner{
		public void onItemClick(View view ,int position);
	}
	//回调方法
	public void setOnItemClickListner(OnItemClickListner listner){
		mListener = listner;
	}
	
	

}
