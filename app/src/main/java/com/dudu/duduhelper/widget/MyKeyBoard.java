package com.dudu.duduhelper.widget;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.dudu.duduhelper.BaseActivity;
import com.dudu.duduhelper.R;

public class MyKeyBoard implements OnClickListener
{
	private OnKeyBoardClickListener onMyKeyBoardClickListener=null;
	private  TextView num1btn;
	private  TextView num2btn;
	private  TextView num3btn;
	private  TextView num4btn;
	private  TextView num5btn;
	private  TextView num6btn;
	private  TextView num7btn;
	private  TextView num8btn;
	private  TextView num9btn;
	private  TextView num0btn;
	private  TextView pointbtn;
	private  Button submitbtn;
	private  ImageView closebtn;
	private  ImageView deletebtn;
	private  BaseActivity context;
	
	public MyKeyBoard(BaseActivity context)
	{
		this.context = context;
		num1btn = (TextView) context.findViewById(R.id.num1btn);
		num2btn = (TextView) context.findViewById(R.id.num2btn);
		num3btn = (TextView) context.findViewById(R.id.num3btn);
		num4btn = (TextView) context.findViewById(R.id.num4btn);
		num5btn = (TextView) context.findViewById(R.id.num5btn);
		num6btn = (TextView) context.findViewById(R.id.num6btn);
		num7btn = (TextView) context.findViewById(R.id.num7btn);
		num8btn = (TextView) context.findViewById(R.id.num8btn);
		num9btn = (TextView) context.findViewById(R.id.num9btn);
		num0btn = (TextView) context.findViewById(R.id.num0btn);
		pointbtn = (TextView) context.findViewById(R.id.pointbtn);
		submitbtn = (Button) context.findViewById(R.id.submitbtn);
		closebtn = (ImageView) context.findViewById(R.id.closebtn);
		deletebtn = (ImageView) context.findViewById(R.id.deletebtn);
		
		num1btn.setOnClickListener(MyKeyBoard.this);
		num2btn.setOnClickListener(MyKeyBoard.this);
		num3btn.setOnClickListener(MyKeyBoard.this);
		num4btn.setOnClickListener(MyKeyBoard.this);
		num5btn.setOnClickListener(MyKeyBoard.this);
		num6btn.setOnClickListener(MyKeyBoard.this);
		num7btn.setOnClickListener(MyKeyBoard.this);
		num8btn.setOnClickListener(MyKeyBoard.this);
		num9btn.setOnClickListener(MyKeyBoard.this);
		num0btn.setOnClickListener(MyKeyBoard.this);
		pointbtn.setOnClickListener(MyKeyBoard.this);
		submitbtn.setOnClickListener(MyKeyBoard.this);
		closebtn.setOnClickListener(MyKeyBoard.this);
		deletebtn.setOnClickListener(MyKeyBoard.this);
	}
	

	
	@Override
	//接口回调
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch (v.getId()) 
		{
			case R.id.closebtn:
				context.findViewById(R.id.keyboard).setVisibility(View.GONE);
				break;
				
	        case R.id.deletebtn:
	        	onMyKeyBoardClickListener.onDelect();
				break;
				
	        case R.id.submitbtn:
	        	onMyKeyBoardClickListener.onSubmit();
	        	break;
			default:
				onMyKeyBoardClickListener.onClick(((TextView)v).getText().toString());
				break;
		}
		
	}
	public interface OnKeyBoardClickListener
	{
		//键盘三种点击方式
		void onClick(String content);
		void onDelect();
		//提交按钮
		void onSubmit();
	}
	
	public void setOnKeyBoardClickListener(OnKeyBoardClickListener onKeyBoardClickListener)
	{
		onMyKeyBoardClickListener = onKeyBoardClickListener;
	}


}


