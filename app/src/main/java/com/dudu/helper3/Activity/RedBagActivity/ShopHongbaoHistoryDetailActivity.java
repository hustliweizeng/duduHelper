package com.dudu.helper3.Activity.RedBagActivity;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.R;
import com.dudu.helper3.bean.GetHongBaoHistDataBean;
import com.dudu.helper3.Utils.Util;

import android.os.Bundle;
import android.widget.TextView;

public class ShopHongbaoHistoryDetailActivity extends BaseActivity 
{
	private GetHongBaoHistDataBean hongbao;
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
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
		hongbao=(GetHongBaoHistDataBean) getIntent().getSerializableExtra("hongbao");
		hongbaoNameTextView.setText(hongbao.getTitle());
		hongbaoMoneyTextView.setText("¥"+hongbao.getMoney());
		hongbaoUserNameTextView.setText(hongbao.getNickname());
		if((System.currentTimeMillis()-Long.parseLong(hongbao.getExpire_time())*1000)>0)
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
		hongbaoGetTimeTextView.setText(Util.DataConVertMint(hongbao.getLog_time()));
		hongbaoGuoqiTextView.setText(Util.DataConVertMint(hongbao.getExpire_time()));
		hongbaoUseTimeTextView.setText(Util.DataConVertMint(hongbao.getUsed_time()));
	}
}
