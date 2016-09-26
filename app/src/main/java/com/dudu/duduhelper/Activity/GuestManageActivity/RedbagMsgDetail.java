package com.dudu.duduhelper.Activity.GuestManageActivity;

import android.os.Bundle;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;

/**
 * @author
 * @version 1.0
 * @date 2016/9/24
 */

public class RedbagMsgDetail extends BaseActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_redbag_msg_detail);
		initHeadView("详情",true,false,0);
	}
}
