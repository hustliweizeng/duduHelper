package com.dudu.duduhelper.widget;

import com.dudu.duduhelper.Utils.Util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.view.View;


public class DrawcircleView extends View 
{
    private Context context;
    private int color;
    private int width;
    private int hight;
    private float start;
    private float end;
    private boolean stroke;
	public DrawcircleView(Context context) {
		super(context);
		this.context=context;
		// TODO Auto-generated constructor stub
	}
	public DrawcircleView(Context context,int color,int width,int hight,float start,float end,boolean stroke) 
	{
		super(context);
		this.context=context;
		this.color=color;
		this.width=width;
		this.hight=hight;
		this.start=start;
		this.end=end;
		this.stroke=stroke;
		// TODO Auto-generated constructor stub
	}
	
	public float getEnd() {
		return end;
	}
	public void setEnd(float end) {
		this.end = end;
	}
	@Override
	protected void onDraw(Canvas canvas) 
	{
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		// 创建画笔  
        Paint p1 = new Paint();  
        if(!stroke)
        {
	        p1.setColor(color);// 设置红色  
	        p1.setAntiAlias(true);
	        
        // 画弧，第一个参数是RectF：该类是第二个参数是角度的开始，第三个参数是多少度，第四个参数是真的时候画扇形，是假的时候画弧线
        }
        else
        {
        	p1.setStrokeWidth(Util.dip2px(context, 2));
        	p1.setStyle(Paint.Style.STROKE);
        	p1.setColor(Color.WHITE);
        	p1.setAntiAlias(true);
        }
        RectF oval1 = new RectF(0, 0,Util.dip2px(context, width), Util.dip2px(context, hight));// 设置个新的长方形，扫描测量  
        canvas.drawArc(oval1, start, end, true, p1);  
        
//        Paint p2 = new Paint();  
//        p2.setColor(Color.BLUE);// 设置红色  
//        p2.setAntiAlias(true);
//        RectF oval2 = new RectF(0, 0, Util.dip2px(context, 160), Util.dip2px(context, 160));// 设置个新的长方形，扫描测量  
//        // 画弧，第一个参数是RectF：该类是第二个参数是角度的开始，第三个参数是多少度，第四个参数是真的时候画扇形，是假的时候画弧线
//        canvas.drawArc(oval2, 315, 315, true, p2);  
	}

}
