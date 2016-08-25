package com.dudu.duduhelper.widget;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.v4.view.animation.PathInterpolatorCompat;
import android.util.AttributeSet;
import android.view.View;


import java.util.ArrayList;
import java.util.List;

import com.dudu.duduhelper.R;
import com.dudu.duduhelper.Utils.Util;

public class WheelIndicatorView extends View 
{
	private final static int ANGLE_INIT_OFFSET = -90;
    private final static int DEFAULT_FILLED_PERCENT = 100;
    private final static int DEFAULT_ITEM_LINE_WIDTH = 2;
    public static final int ANIMATION_DURATION = 2400;
    public static final int INNER_BACKGROUND_CIRCLE_COLOR = Color.parseColor("#2c4660"); // Color for

    private Paint itemArcPaint;
    private Paint itemEndPointsPaint;
    private Paint innerBackgroundCirclePaint;
    private List<WheelIndicatorItem> wheelIndicatorItems;
    private int viewHeight;
    private int viewWidth;
    private int minDistViewSize;
    private int maxDistViewSize;
    private int traslationX;
    private int traslationY;
    private RectF wheelBoundsRectF;
    private Paint circleBackgroundPaint;
    private ArrayList<Float> wheelItemsAngles;     // calculated angle for each @WheelIndicatorItem
    private int filledPercent = 100;
    private int itemsLineWidth;
    private TypedArray attributesArray;

    public WheelIndicatorView(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public WheelIndicatorView(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init(attrs);
    }

    public WheelIndicatorView(Context context)
    {
        super(context);
        init(null);
    }
    //设置弧形列表
    public void setWheelIndicatorItems(List<WheelIndicatorItem> wheelIndicatorItems)
    {
        if (wheelIndicatorItems == null)
            throw new IllegalArgumentException("wheelIndicatorItems cannot be null");
        this.wheelIndicatorItems = wheelIndicatorItems;
        recalculateItemsAngles();
        invalidate();
    }
    //单个添加户型列表
    public void addWheelIndicatorItem(WheelIndicatorItem indicatorItem)
    {
        if (indicatorItem == null)
            throw new IllegalArgumentException("wheelIndicatorItems cannot be null");

        this.wheelIndicatorItems.add(indicatorItem);
        recalculateItemsAngles();
        invalidate();
    }

    public void setFilledPercent(int filledPercent)
    {
        if (filledPercent < 0)
            this.filledPercent = 0;
        else if (filledPercent > 100)
            this.filledPercent = 100;
        else
            this.filledPercent = filledPercent;
        invalidate();
    }

    public int getFilledPercent()
    {
        return filledPercent;
    }

    public void setItemsLineWidth(int itemLineWidth)
    {
        if (itemLineWidth <= 0)
            throw new IllegalArgumentException("itemLineWidth must be greater than 0");
        this.itemsLineWidth = itemLineWidth;
        invalidate();
    }


    public void notifyDataSetChanged()
    {
        recalculateItemsAngles();
        invalidate();
    }

    public void setBackgroundColor(int color)
    {
        circleBackgroundPaint = new Paint();
        circleBackgroundPaint.setColor(color);
        circleBackgroundPaint.setAntiAlias(true);
        invalidate();
    }

    private void init(AttributeSet attrs)
    {
        attributesArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.WheelIndicatorView, 0, 0);

        int itemsLineWidth = attributesArray.getDimensionPixelSize(R.styleable.WheelIndicatorView_itemsLineWidth, Util.dip2px(getContext(),DEFAULT_ITEM_LINE_WIDTH) );
        setItemsLineWidth(itemsLineWidth);

        int filledPercent = attributesArray.getInt(R.styleable.WheelIndicatorView_filledPercent, DEFAULT_FILLED_PERCENT);
        setFilledPercent(filledPercent);

        int bgColor = attributesArray.getColor(R.styleable.WheelIndicatorView_backgroundColor, -1);
        if (bgColor != -1)
            setBackgroundColor(bgColor);

        this.wheelIndicatorItems = new ArrayList<WheelIndicatorItem>();
        this.wheelItemsAngles = new ArrayList<Float>();

        BlurMaskFilter maskFilter = new BlurMaskFilter(Util.dip2px(getContext(), 2), BlurMaskFilter.Blur.SOLID);



        itemArcPaint = new Paint();
        itemArcPaint.setStyle(Paint.Style.STROKE);
        itemArcPaint.setAntiAlias(true);
        itemArcPaint.setMaskFilter(maskFilter);

        innerBackgroundCirclePaint = new Paint();
        innerBackgroundCirclePaint.setColor(INNER_BACKGROUND_CIRCLE_COLOR);
        innerBackgroundCirclePaint.setStyle(Paint.Style.STROKE);
        innerBackgroundCirclePaint.setStrokeWidth(itemsLineWidth * 2);
        innerBackgroundCirclePaint.setAntiAlias(true);

        itemEndPointsPaint = new Paint();
        itemEndPointsPaint.setAntiAlias(true);
        itemEndPointsPaint.setMaskFilter(maskFilter);
    }

    private void recalculateItemsAngles()
    {
        wheelItemsAngles.clear();
//        float total = 0;
        float total = 1;
        float angleAccumulated = 0;

//        for (WheelIndicatorItem item : wheelIndicatorItems)
//        {
//            total += item.getWeight();
//        }
        for (int i = 0; i < wheelIndicatorItems.size(); ++i)
        {
            float normalizedValue = wheelIndicatorItems.get(i).getWeight() / total;
            float angle = -(360 * normalizedValue * filledPercent / 100);
//            wheelItemsAngles.add(angle + angleAccumulated);
//            angleAccumulated += angle;
            wheelItemsAngles.add(angle);
        }
    }

    public void startItemsAnimation()
    {
        ObjectAnimator animation = ObjectAnimator.ofInt(WheelIndicatorView.this, "filledPercent", 0, filledPercent);
        animation.setDuration(ANIMATION_DURATION);
        animation.setInterpolator(PathInterpolatorCompat.create(0.4F, 0.0F, 0.2F, 1.0F));
        animation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                recalculateItemsAngles();
                invalidate();
            }
        });
        animation.start();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh)
    {
        super.onSizeChanged(w, h, oldw, oldh);

        this.viewHeight = getMeasuredHeight();
        this.viewWidth = getMeasuredWidth();
        this.minDistViewSize = Math.min(getMeasuredWidth(), getMeasuredHeight());
        this.maxDistViewSize = Math.max(getMeasuredWidth(), getMeasuredHeight());

        if (viewWidth <= viewHeight)
        {
            this.traslationX = 0;
            this.traslationY = (maxDistViewSize - minDistViewSize) / 2;
        } else
        {
            this.traslationX = (maxDistViewSize - minDistViewSize) / 2;
            this.traslationY = 0;
        }
        // Adding artificial padding, depending on line width
        //float left,float top,float right,float bottom
        wheelBoundsRectF = new RectF(0 + itemsLineWidth*5, 0 + itemsLineWidth*5, minDistViewSize - itemsLineWidth*5, minDistViewSize - itemsLineWidth*5);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.translate(traslationX, traslationY);
        if (circleBackgroundPaint != null)
            //画圆
            //canvas.drawCircle(wheelBoundsRectF.centerX(), wheelBoundsRectF.centerY(), wheelBoundsRectF.width() / 3 - itemsLineWidth, circleBackgroundPaint);
        	//画圆圈
        	canvas.drawArc(wheelBoundsRectF, ANGLE_INIT_OFFSET, 360, false, innerBackgroundCirclePaint);
        	drawIndicatorItems(canvas);
    }

    private void drawIndicatorItems(Canvas canvas)
    {
        if (wheelIndicatorItems.size() > 0)
        {
            for (int i = wheelIndicatorItems.size() - 1; i >= 0; i--)
            { // Iterate backward to overlap larger items
                draw(wheelIndicatorItems.get(i), wheelBoundsRectF, wheelItemsAngles.get(i), canvas);
            }
        }
    }

    private void draw(WheelIndicatorItem indicatorItem, RectF surfaceRectF, float angle, Canvas canvas)
    {
        int indicatorItemWidth= indicatorItem.getWidth();
        itemArcPaint.setColor(indicatorItem.getColor());
        itemArcPaint.setStrokeWidth(indicatorItemWidth * 2);
        itemEndPointsPaint.setColor(indicatorItem.getColor());
        itemEndPointsPaint.setStrokeWidth(indicatorItemWidth * 2);
        //真尼玛坑爹
        //画圆圈
        canvas.drawArc(surfaceRectF, ANGLE_INIT_OFFSET, angle, false, itemArcPaint);
        //画圆点
        canvas.drawCircle(minDistViewSize / 2, 0 + itemsLineWidth*5, indicatorItemWidth, itemEndPointsPaint);
        int topPosition = minDistViewSize / 2 - itemsLineWidth*5;
        //画圆点
        canvas.drawCircle((float) (Math.cos(Math.toRadians(angle + ANGLE_INIT_OFFSET)) * topPosition + topPosition + itemsLineWidth*5), (float) (Math.sin(Math.toRadians((angle + ANGLE_INIT_OFFSET))) * topPosition + topPosition + itemsLineWidth*5), indicatorItemWidth, itemEndPointsPaint);
    }
}
