package com.dudu.helper3.adapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.SystemClock;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dudu.helper3.R;
import com.dudu.helper3.Utils.LogUtil;
import com.dudu.helper3.javabean.GuestListBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * @author
 * @version 1.0
 * @date 2016/9/24
 */

public class GuestListCheckAdapter extends RecyclerView.Adapter {
	Context mContext;
	//客户列表
	private  List<GuestListBean.GuestDetails> guestList = new ArrayList<>();
	//选中的条目
	public  ArrayList<CharSequence> list = new ArrayList<>();
	//选中的客户id
	public  ArrayList<CharSequence> guest_ids = new ArrayList<>();

	//是否全选
	private boolean isAll = false;
	private boolean isEnd;

	public GuestListCheckAdapter(Context context) {
		mContext = context;
	}
	public  ArrayList<CharSequence> getIds(){
		LogUtil.d("return",guest_ids.size()+"");
		return  guest_ids;
	}
	public ArrayList<CharSequence> getList(){
		return list;
	}
	//设置存储的位置列表
	public void setList(ArrayList<CharSequence> list){
		if(list!=null){
			this.list = list;
			notifyDataSetChanged();
		}
			
	}
	//设置存储的id列表
	public void setIds(ArrayList<CharSequence> ids){
		if (guest_ids!=null){
			this.guest_ids = ids;
			notifyDataSetChanged();
		}
	}
	public void addGuests (List<GuestListBean.GuestDetails> guestList){
		if (guestList!=null & guestList.size()>0){
			this.guestList.addAll(guestList);
			LogUtil.d("ok","size="+guestList.size());
			notifyDataSetChanged();
		}
	}

	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = View.inflate(mContext, R.layout.item_guest_checkable, null);
		Myholder myholder = new Myholder(view,mListener);
		return myholder;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
		
		final Myholder myholder = (Myholder)holder;
		if (list.contains(position+"")){
			myholder.iv_ischeck.setImageResource(R.drawable.select_check);
		}else {
			myholder.iv_ischeck.setImageResource(R.drawable.select_empty);
		}
		//设置客户详情
		GuestListBean.GuestDetails guestDetails = guestList.get(position);
		ImageLoader.getInstance().displayImage(guestDetails.getAvatar(),myholder.iv_item_guest);
		myholder.tv_title_item_guest.setText(guestDetails.getNickname());
		myholder.tv_count_item_guest.setText(guestDetails.getTotal_num());
		myholder.tv_totalprice_item_guest.setText(guestDetails.getTotal_consumption());
		myholder.tv_date_item_guest.setText(guestDetails.getLast_consumption_time());
		if (position == guestList.size()-1){//如果是最后一个条目
			myholder.foot.setVisibility(View.VISIBLE);
			if (mOnLoadMore!=null){//加载更多数据
				isEnd = mOnLoadMore.loadMore();
				LogUtil.d("LoadMore",position+"'");
			}
			CountDownTimer timer = new CountDownTimer(2000, 2000) {//利用计时器等待请求数据返回结果
				@Override
				public void onTick(long millisUntilFinished) {
				}
				@Override
				public void onFinish() {
					if (isEnd){
						myholder.foot.setVisibility(View.GONE);
					}else {
						myholder.loading_text.setText("已经加载到底部");
						SystemClock.sleep(1000);
						myholder.foot.setVisibility(View.GONE);
					}
				}
			};
			timer.start();
		}else {
			LogUtil.d("content",position+"=="+guestList.size());
			myholder.foot.setVisibility(View.GONE);
		}
	}
	public  interface  OnLoadMore{
		public  boolean loadMore();
	}
	private    OnLoadMore mOnLoadMore;
	public  void setOnLoadMore(OnLoadMore onLoadMore){
		mOnLoadMore = onLoadMore;
	}
	
	

	@Override
	public int getItemCount() {
		return guestList.size();
	}

	class Myholder extends RecyclerView.ViewHolder implements View.OnClickListener {
		private ImageView iv_ischeck;
		private ImageView iv_item_guest;
		private TextView tv_title_item_guest;
		private TextView tv_totalprice_item_guest;
		private TextView tv_count_item_guest;
		private TextView tv_date_item_guest;
		private LinearLayout foot;
		private TextView loading_text;
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
			foot = (LinearLayout) itemView.findViewById(R.id.foot);
			loading_text = (TextView) itemView.findViewById(R.id.loading_text);
			
		}

		@Override
		//让holder绑定点击事件,触发回调
		public void onClick(View v) {
			if(listener != null){
				//事件回调
				listener.onItemClick(v,getLayoutPosition());
				String position = getLayoutPosition()+"";
				//根据位置获取id
				String member_id = guestList.get(getLayoutPosition()).getMember_id();
				LogUtil.d("id","id="+member_id);
				//当该条目被点击后，设置图标状态
				if (!list.contains(position)){
					list.add(position);
					guest_ids.add(member_id);
					LogUtil.d("ischeck","ad_id="+member_id);
				}else {
					list.remove(position);
					guest_ids.remove(member_id);
					LogUtil.d("ischeck","remove_id="+member_id);
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
		list.clear();
		if (isAll){
			//取消全选
			isAll = !isAll;
		}else {
			for (int i=0;i<getItemCount(); i++){
				list.add(i+"");
				//把所有id放进去
				String member_id = guestList.get(i).getMember_id();
				guest_ids.add(member_id);
				LogUtil.d("addall","id="+member_id);
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
