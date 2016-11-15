package com.dudu.helper3.Activity.MyInfoActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.dudu.helper3.BaseActivity;
import com.dudu.helper3.R;

/**
 * @author
 * @version 1.0
 * @date 2016/11/15
 */

public class MsgSettingActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activty_msg_setting);
		initHeadView("消息设置",true,false,0);
	}
}
