package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.javabean.RedbagMsgListBean;

import java.util.HashMap;
import java.util.Set;
import com.dudu.duduhelper.R;
/**
 * @author
 * @version 1.0
 * @date 2016/9/24
 */

public class RedbagMsgDetail extends BaseActivity {
	private TextView tv_price;
	private TextView tv_limit;
	private TextView tv_expireTime;
	private TextView tv_promotePrice;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_redbag_msg_detail);
		initView();
		initHeadView("详情", true, false, 0);
	}

	private void initView() {
		tv_price = (TextView) findViewById(R.id.tv_price);
		tv_limit = (TextView) findViewById(R.id.tv_limit);
		tv_expireTime = (TextView) findViewById(R.id.tv_expireTime);
		tv_promotePrice = (TextView) findViewById(R.id.tv_promotePrice);
		RedbagMsgListBean.ListBean data = (RedbagMsgListBean.ListBean) getIntent().getSerializableExtra("data");
		//设置数据
		if (data!=null){
			if (!data.getRed_packet_info().getLimit().equals("0"))
			{
				HashMap<String,String> limit = (HashMap<String,String>) data.getRed_packet_info().getLimit();
				Set<String> keys = limit.keySet();
				for (String item :keys){
					tv_limit.setText("支付满"+item+"元以上可以使用");
				}
				tv_price.setText(data.getRed_packet_info().getMoney());
				tv_expireTime.setText(data.getRed_packet_info().getLife()+"天之内使用");
				tv_promotePrice.setText(data.getPromote_consumer());
			}else {
				Toast.makeText(context,"数据异常",Toast.LENGTH_LONG).show();
			}
		}
		
	}
}
