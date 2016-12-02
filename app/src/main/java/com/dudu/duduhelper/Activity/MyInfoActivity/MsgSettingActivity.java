package com.dudu.duduhelper.Activity.MyInfoActivity;

import android.os.Bundle;
import android.view.View;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;
import com.kyleduo.switchbutton.SwitchButton;

/**
 * @author
 * @version 1.0
 * @date 2016/11/15
 */

public class MsgSettingActivity extends BaseActivity implements View.OnClickListener {
	private SwitchButton btn_voice;
	private SwitchButton btn_ring;
	private SwitchButton btn_ding;
	private SwitchButton btn_auto_print;
	private boolean isVoiceOpen;
	private boolean isRemindOpen;
	private boolean isRingOpen;
	private boolean isAutoPrintOpen;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activty_msg_setting);
		initView();
		initStatus();
		initHeadView("设置", true, false, 0);
	}

	private void initStatus() {
		//语言开关
		isVoiceOpen = sp.getBoolean("isVoiceOpen", false);
		//铃声
		isRemindOpen = sp.getBoolean("isRemindOpen", false);
		//震动
		isRingOpen = sp.getBoolean("isRingOpen", false);
		//自动打印
		isAutoPrintOpen = sp.getBoolean("isAutoPrintOpen", false);

		/**
		 * 设置开关的状态
		 */
		btn_voice.setCheckedImmediatelyNoEvent(isVoiceOpen);
		btn_ring.setCheckedImmediatelyNoEvent(isRemindOpen);//铃声
		btn_ding.setCheckedImmediatelyNoEvent(isRingOpen);//震动
		btn_auto_print.setCheckedImmediatelyNoEvent(isAutoPrintOpen);

	}

	private void initView() {
		btn_voice = (SwitchButton) findViewById(R.id.btn_voice);
		btn_ring = (SwitchButton) findViewById(R.id.btn_ring);//铃声
		btn_ding = (SwitchButton) findViewById(R.id.btn_ding);//震动
		btn_auto_print = (SwitchButton) findViewById(R.id.btn_auto_print);

		btn_voice.setOnClickListener(this);
		btn_ring.setOnClickListener(this);
		btn_ding.setOnClickListener(this);
		btn_auto_print.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.btn_voice:
				isVoiceOpen = !isVoiceOpen;
				sp.edit().putBoolean("isVoiceOpen",isVoiceOpen).commit();
				break;
			case R.id.btn_ring:
				isRemindOpen = !isRemindOpen;
				sp.edit().putBoolean("isRemindOpen",isRemindOpen).commit();
				break;
			case R.id.btn_ding://震动
				isRingOpen = !isRingOpen;
				sp.edit().putBoolean("isRingOpen",isRingOpen).commit();
				break;
			case R.id.btn_auto_print:
				isAutoPrintOpen = !isAutoPrintOpen;
				sp.edit().putBoolean("isAutoPrintOpen",isAutoPrintOpen).commit();
				break;
		}
	}
}
