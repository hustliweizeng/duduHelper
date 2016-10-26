package com.dudu.helper3.widget;

import android.app.AlertDialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.dudu.duduhelper.R;

public class MyDialog 
{
	static AlertDialog dlg=null;
	public static void showDialog(Context context,String content,boolean confirmbtnvisable,boolean cancelButtonvisable,String confirmbtntext,String cancelButtontext,OnClickListener confirmbtnClick,OnClickListener cancelButtonClick) 
	{
		// TODO Auto-generated method stub
		dlg = new AlertDialog.Builder(context).create();
		dlg.show();
		Window window = dlg.getWindow();
		// *** 主要就是在这里实现这种效果的.
		// 设置窗口的内容页面,send_message_dialog.xml文件中定义view内容
		window.setContentView(R.layout.activity_dialog);
		TextView textContent=(TextView) window.findViewById(R.id.textContent);
		textContent.setText(content);
		
		if(confirmbtnvisable)
		{
			Button confirmButton=(Button) window.findViewById(R.id.confirmButton);
			confirmButton.setVisibility(View.VISIBLE);
			confirmButton.setText(confirmbtntext);
			confirmButton.setOnClickListener(confirmbtnClick);
		}
		if(cancelButtonvisable)
		{
			Button cancelButton=(Button) window.findViewById(R.id.cancelButton);
			cancelButton.setVisibility(View.VISIBLE);
			cancelButton.setText(cancelButtontext);
			cancelButton.setOnClickListener(cancelButtonClick);
		}
	}
	
	public static void cancel()
	{
		if(dlg!=null)
		{
			dlg.cancel();

		}
	}
	
}
