package com.dudu.duduhelper.Activity.RedBagActivity;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.Utils.Util;
import com.dudu.duduhelper.javabean.RedBagHitsoryBean;

import android.os.Bundle;
import android.widget.TextView;
import com.dudu.duduhelper.R;
public class ShopHongbaoHistoryDetailActivity extends BaseActivity 
{
	private RedBagHitsoryBean.DataBean hongbao;
	private TextView hongbaoNameTextView;
	private TextView hongbaoMoneyTextView;
	private TextView hongbaoUserNameTextView;
	private TextView hongbaoActionTextView;
	private TextView hongbaoGetTimeTextView;
	private TextView hongbaoGuoqiTextView;
	private TextView hongbaoUseTimeTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.shop_activity_hongbao_history_detail);
		initHeadView("使用详情", true, false, 0);
		initView();
		initData();
	}
	private void initView() 
	{
		hongbaoNameTextView=(TextView) this.findViewById(R.id.hongbaoNameTextView);
		hongbaoMoneyTextView=(TextView) this.findViewById(R.id.hongbaoMoneyTextView);
		hongbaoUserNameTextView=(TextView) this.findViewById(R.id.hongbaoUserNameTextView);
		hongbaoActionTextView=(TextView) this.findViewById(R.id.hongbaoActionTextView);
		hongbaoGetTimeTextView=(TextView) this.findViewById(R.id.hongbaoGetTimeTextView);
		hongbaoGuoqiTextView=(TextView) this.findViewById(R.id.hongbaoGuoqiTextView);
		hongbaoUseTimeTextView=(TextView) this.findViewById(R.id.hongbaoUseTimeTextView);
	}

	private void initData() 
	{
		hongbao=(RedBagHitsoryBean.DataBean) getIntent().getSerializableExtra("hongbao");
		if (hongbao==null){
			return;
		}
		hongbaoNameTextView.setText(hongbao.getRed_packet_title());
		hongbaoMoneyTextView.setText("¥"+hongbao.getMoney());
		hongbaoUserNameTextView.setText(hongbao.getMember_nickname());
		Long expireTime = Util.Data2Unix(hongbao.getExpire_time()) *1000;
		if(System.currentTimeMillis()- expireTime >0)//判断是否过期
		{
			hongbaoActionTextView.setTextColor(hongbaoActionTextView.getResources().getColor(R.color.text_color_light));
			hongbaoActionTextView.setText("已过期");
		}
		else
		{
			if(hongbao.getUsed().equals("0"))
			{
				hongbaoActionTextView.setTextColor(hongbaoActionTextView.getResources().getColor(R.color.text_color_red));
				hongbaoActionTextView.setText("未使用");
			}
			if(hongbao.getUsed().equals("1"))
			{
				hongbaoActionTextView.setTextColor(hongbaoActionTextView.getResources().getColor(R.color.text_color));
				hongbaoActionTextView.setText("已使用");
			}
		}
		hongbaoGetTimeTextView.setText(hongbao.getCreated_at());
		hongbaoGuoqiTextView.setText(hongbao.getExpire_time());
		if (hongbao.getUsed_at()!=null){
			hongbaoUseTimeTextView.setText(hongbao.getUsed_at());
		}
	}
}
