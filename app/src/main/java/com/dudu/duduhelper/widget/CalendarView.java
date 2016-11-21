package com.dudu.duduhelper.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by xuboyang on 16/8/5.
 */
public class CalendarView extends View implements View.OnTouchListener
{
    private final static String TAG = "anCalendar";
    private Date selectedStartDate;
    private Date selectedEndDate;
    private Date curDate; // 当前日历显示的月
    private Date today; // 今天的日期文字显示红色
    private Date downDate; // 手指按下状态时临时日期
    private Date showFirstDate, showLastDate; // 日历显示的第一个日期和最后一个日期
    private int downIndex; // 按下的格子索引
    private Calendar calendar;
    private Surface surface;
    private int[] date = new int[42]; // 日历显示数字
    private int curStartIndex, curEndIndex; // 当前显示的日历起始的索引
    private boolean completed = false; // 为false表示只选择了开始日期，true表示结束日期也选择了
    private boolean isSelectMore = false;
    //给控件设置监听事件
    private OnItemClickListener onItemClickListener;
    private String formateDownDate;
    //返回按下的日期，date格式
    public Date getDownDate(){
        return downDate;
    }
    //返回按下的日期
    public String getFormateDownDate(){
    return formateDownDate;
    }
    public CalendarView(Context context) {
        super(context);
        init();
    }

    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }
    
    private void init() {
        curDate = selectedStartDate = selectedEndDate = today = new Date();
        calendar = Calendar.getInstance();
        calendar.setTime(curDate);
        surface = new Surface();//初始化surface
        surface.density = getResources().getDisplayMetrics().density;
        setBackgroundColor(surface.bgColor);
        setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        surface.width = getResources().getDisplayMetrics().widthPixels;//控件宽度
        surface.height = (int) (getResources().getDisplayMetrics().heightPixels*3/9);//控件高度
        widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(surface.width,View.MeasureSpec.EXACTLY);//制定测量规则
        heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(surface.height,View.MeasureSpec.EXACTLY);
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right,int bottom)
    {
        Log.d(TAG, "[onLayout] changed:"+ (changed ? "new size" : "not change") + " left:" + left+ " top:" + top + " right:" + right + " bottom:" + bottom);
        if (changed)
        {
            surface.init();
        }
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        Log.d(TAG, "onDraw");
        // 画框
        // 计算日期
        calculateDate();
        // 按下状态，选择状态背景色
        drawDownOrSelectedBg(canvas);
        // write date number
        // today index
        int todayIndex = -1;
        calendar.setTime(curDate);
        String curYearAndMonth = calendar.get(Calendar.YEAR) + ""+ calendar.get(Calendar.MONTH);
        calendar.setTime(today);
        String todayYearAndMonth = calendar.get(Calendar.YEAR) + ""+ calendar.get(Calendar.MONTH);
        if (curYearAndMonth.equals(todayYearAndMonth))
        {
            int todayNumber = calendar.get(Calendar.DAY_OF_MONTH);
            todayIndex = curStartIndex + todayNumber - 1;
        }
        for (int i = 0; i < 42; i++)
        {
            int color = surface.textColor;
            if (isLastMonth(i))
            {
                //上个月字体颜色
                color = surface.otherDayColor;
            }
            else if (isNextMonth(i))
            {
                //下个月字体颜色
                color = surface.otherDayColor;
            }
            if (todayIndex != -1 && i == todayIndex)
            {
                color = surface.todayNumberColor;
            }
            drawCellText(canvas, i, date[i] + "", color);
        }
        super.onDraw(canvas);
    }

    private void calculateDate()
    {
        calendar.setTime(curDate);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int dayInWeek = calendar.get(Calendar.DAY_OF_WEEK);
        Log.d(TAG, "day in week:" + dayInWeek);
        int monthStart = dayInWeek;
        if (monthStart == 1)
        {
            monthStart = 8;
        }
        monthStart -= 1;  //以日为开头-1，以星期一为开头-2
        curStartIndex = monthStart;
        date[monthStart] = 1;
        // last month
        if (monthStart > 0)
        {
            calendar.set(Calendar.DAY_OF_MONTH, 0);
            int dayInmonth = calendar.get(Calendar.DAY_OF_MONTH);
            for (int i = monthStart - 1; i >= 0; i--)
            {
                date[i] = dayInmonth;
                dayInmonth--;
            }
            calendar.set(Calendar.DAY_OF_MONTH, date[0]);
        }
        showFirstDate = calendar.getTime();
        // this month
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, 1);
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        // Log.d(TAG, "m:" + calendar.get(Calendar.MONTH) + " d:" +
        // calendar.get(Calendar.DAY_OF_MONTH));
        int monthDay = calendar.get(Calendar.DAY_OF_MONTH);
        for (int i = 1; i < monthDay; i++)
        {
            date[monthStart + i] = i + 1;
        }
        curEndIndex = monthStart + monthDay;
        // next month
        for (int i = monthStart + monthDay; i < 42; i++)
        {
            date[i] = i - (monthStart + monthDay) + 1;
        }
        if (curEndIndex < 42)
        {
            // 显示了下一月的
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        calendar.set(Calendar.DAY_OF_MONTH, date[41]);
        showLastDate = calendar.getTime();
    }

    /**
     *
     * @param canvas
     * @param index
     * @param text
     */
    private void drawCellText(Canvas canvas, int index, String text, int color)
    {
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        surface.datePaint.setColor(color);
        float cellY = surface.monthHeight + surface.weekHeight + (y - 1)* surface.cellHeight + surface.cellHeight * 2 / 3f;
        float cellX = (surface.cellWidth * (x - 1))+ (surface.cellWidth - surface.datePaint.measureText(text))/ 2f;
        canvas.drawText(text, cellX, cellY, surface.datePaint);
    }

    /**
     *
     * @param canvas
     * @param index
     * @param color
     */
    private void drawCellBg(Canvas canvas, int index, int color)
    {
        int x = getXByIndex(index);
        int y = getYByIndex(index);
        surface.cellBgPaint.setColor(color);
        float left = surface.cellWidth * (x - 1) + surface.borderWidth*16;
        float top = surface.monthHeight + surface.weekHeight + (y - 1)* surface.cellHeight + surface.borderWidth;

        canvas.drawRect(left, top, left + surface.cellWidth- surface.borderWidth*28, top + surface.cellHeight- surface.borderWidth, surface.cellBgPaint);
    }

    private void drawDownOrSelectedBg(Canvas canvas)
    {
        // down and not up
        if (downDate != null)
        {
            drawCellBg(canvas, downIndex, surface.cellDownColor);
        }
        // selected bg color
        if (!selectedEndDate.before(showFirstDate)&& !selectedStartDate.after(showLastDate))
        {
            int[] section = new int[] { -1, -1 };
            calendar.setTime(curDate);
            calendar.add(Calendar.MONTH, -1);
            findSelectedIndex(0, curStartIndex, calendar, section);
            if (section[1] == -1)
            {
                calendar.setTime(curDate);
                findSelectedIndex(curStartIndex, curEndIndex, calendar, section);
            }
            if (section[1] == -1)
            {
                calendar.setTime(curDate);
                calendar.add(Calendar.MONTH, 1);
                findSelectedIndex(curEndIndex, 42, calendar, section);
            }
            if (section[0] == -1)
            {
                section[0] = 0;
            }
            if (section[1] == -1)
            {
                section[1] = 41;
            }
            for (int i = section[0]; i <= section[1]; i++)
            {
                drawCellBg(canvas, i, surface.cellSelectedColor);
            }
        }
    }

    private void findSelectedIndex(int startIndex, int endIndex,Calendar calendar, int[] section)
    {
        for (int i = startIndex; i < endIndex; i++)
        {
            calendar.set(Calendar.DAY_OF_MONTH, date[i]);
            Date temp = calendar.getTime();
            // Log.d(TAG, "temp:" + temp.toLocaleString());
            if (temp.compareTo(selectedStartDate) == 0)
            {
                section[0] = i;
            }
            if (temp.compareTo(selectedEndDate) == 0)
            {
                section[1] = i;
                return;
            }
        }
    }

    public Date getSelectedStartDate()
    {
        return selectedStartDate;
    }

    public Date getSelectedEndDate()
    {
        return selectedEndDate;
    }

    private boolean isLastMonth(int i)
    {
        if (i < curStartIndex)
        {
            return true;
        }
        return false;
    }

    private boolean isNextMonth(int i)
    {
        if (i >= curEndIndex)
        {
            return true;
        }
        return false;
    }

    private int getXByIndex(int i)
    {
        return i % 7 + 1; // 1 2 3 4 5 6 7
    }

    private int getYByIndex(int i)
    {
        return i / 7 + 1; // 1 2 3 4 5 6
    }

    // 获得当前应该显示的年月
    public String getYearAndmonth()
    {
        calendar.setTime(curDate);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH)+1;
        return year + "-" + month;
    }

    //上一月
    public String clickLeftMonth()
    {
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, -1);
        curDate = calendar.getTime();
        invalidate();
        return getYearAndmonth();
    }
    //下一月
    public String clickRightMonth()
    {
        calendar.setTime(curDate);
        calendar.add(Calendar.MONTH, 1);
        curDate = calendar.getTime();
        invalidate();
        return getYearAndmonth();
    }

    //设置日历时间
    public void setCalendarData(Date date)
    {
        calendar.setTime(date);
        invalidate();
    }

    //获取日历时间
    public void getCalendatData()
    {
        calendar.getTime();
    }

    //设置是否多选
    public boolean isSelectMore()
    {
        return isSelectMore;
    }

    public void setSelectMore(boolean isSelectMore)
    {
        this.isSelectMore = isSelectMore;
    }
    //设置点下的日期颜色和当前日期
    private void setSelectedDateByCoor(float x, float y)
    {
        if (y > surface.monthHeight + surface.weekHeight)
        {
            int m = (int) (Math.floor(x / surface.cellWidth) + 1);
            int n = (int) (Math.floor((y - (surface.monthHeight + surface.weekHeight))/ Float.valueOf(surface.cellHeight)) + 1);
            downIndex = (n - 1) * 7 + m - 1;
            Log.d(TAG, "downIndex:" + downIndex);
            calendar.setTime(curDate);
            if (isLastMonth(downIndex))
            {
                calendar.add(Calendar.MONTH, -1);
            }
            else if (isNextMonth(downIndex))
            {
                calendar.add(Calendar.MONTH, 1);
            }
            calendar.set(Calendar.DAY_OF_MONTH, date[downIndex]);
            //当按下时的日期
            downDate = calendar.getTime();
            //给主线程发消息改变日期
            SimpleDateFormat formatter = new SimpleDateFormat ("yyyy年M月dd日");
            formateDownDate = formatter.format(downDate);
            Log.d("downdate",formateDownDate);
            /*Handler handler = new Handler();
            Message msg = Message.obtain();
            msg.obj = date;
            handler.sendMessage(msg);
*/
        }
        //刷新页面
        invalidate();
    }

    @Override
    /**
     * 当日历被点击时做出响应
     */
    public boolean onTouch(View v, MotionEvent event)
    {
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                //设置点下的日期颜色
                setSelectedDateByCoor(event.getX(), event.getY());
                //发消息给主线程改变当前显示的日期


                break;
            case MotionEvent.ACTION_UP://手指抬起以后
                if (downDate != null)
                {
                    if(isSelectMore)
                    {
                        if (!completed)
                        {
                            if (downDate.before(selectedStartDate))
                            {
                                selectedEndDate = selectedStartDate;
                                selectedStartDate = downDate;
                            }
                            else
                            {
                                selectedEndDate = downDate;
                            }
                            completed = true;
                            //响应监听事件
                            onItemClickListener.OnItemClick(selectedStartDate,selectedEndDate,downDate);
                        }
                        else
                        {
                            selectedStartDate = selectedEndDate = downDate;
                            completed = false;
                        }
                    }
                    else
                    //单选模式下
                    {
                        selectedStartDate = selectedEndDate = downDate;
                        //响应监听事件，进行非空判断，在使用时需要重写setOnItemClickListener,
                        if (onItemClickListener != null){
                            onItemClickListener.OnItemClick(selectedStartDate,selectedEndDate,downDate);

                        }
                    }
                    invalidate();
                }

                break;
        }
        return true;
    }

    //给控件设置监听事件
    public void setOnItemClickListener(OnItemClickListener onItemClickListener)
    {
        this.onItemClickListener =  onItemClickListener;
    }
    //监听接口
    public interface OnItemClickListener
    {
        void OnItemClick(Date selectedStartDate,Date selectedEndDate, Date downDate);
    }

    /**
     *
     * 1. 布局尺寸 2. 文字颜色，大小 3. 当前日期的颜色，选择的日期颜色
     */
    private class Surface
    {
        public float density;
        public int width; // 整个控件的宽度
        public int height; // 整个控件的高度
        public float monthHeight; // 显示月的高度
        //public float monthChangeWidth; // 上一月、下一月按钮宽度
        public float weekHeight=0f; // 显示星期的高度
        public float cellWidth; // 日期方框宽度
        public float cellHeight; // 日期方框高度
        public float borderWidth;
        //背景色
        public int bgColor = Color.parseColor("#2c4660");
        //默认日期颜色
        private int textColor = Color.WHITE;
        //private int textColorUnimportant = Color.parseColor("#666666");
        private int btnColor = Color.parseColor("#000000");
        //描边颜色
        private int borderColor = Color.parseColor("#CCCCCC");
        //上个月的颜色与下个月的颜色
        private int otherDayColor = Color.parseColor("#acb2ba");
        //选中日期颜色
        public int todayNumberColor = Color.WHITE;
        //按钮按下去的颜色
        public int cellDownColor = Color.parseColor("#3dd6bc");
        //选中日期的背景颜色
        public int cellSelectedColor = Color.parseColor("#3dd6bc");
        public Paint borderPaint;
        public Paint monthPaint;
        public Paint weekPaint;
        public Paint datePaint;
        //public Paint monthChangeBtnPaint;
        public Paint cellBgPaint;
        //public Path boxPath; // 边框路径
        //public Path preMonthBtnPath; // 上一月按钮三角形
        //public Path nextMonthBtnPath; // 下一月按钮三角形
        //public String[] weekText = { "Sun","Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        //public String[] monthText = {"Jan","Feb","Mar","Apr","May","Jun","Jul","Aug","Sep","Oct","Nov","Dec"};

        public void init()
        {
            //float temp = height / 7f;
            monthHeight = 0;//(float) ((temp + temp * 0.3f) * 0.6);
            //monthChangeWidth = monthHeight * 1.5f;
            //星期高度
            //weekHeight = (float) ((temp + temp * 0.3f) * 0.7);
            cellHeight = (height - monthHeight - weekHeight) / 5f;
            cellWidth = width / 7f;
            borderPaint = new Paint();
            borderPaint.setColor(borderColor);
            borderPaint.setStyle(Paint.Style.STROKE);
            borderWidth = (float) (0.5 * density);
            // Log.d(TAG, "borderwidth:" + borderWidth);
            borderWidth = borderWidth < 1 ? 1 : borderWidth;
            borderPaint.setStrokeWidth(borderWidth);
            monthPaint = new Paint();
            monthPaint.setColor(textColor);
            monthPaint.setAntiAlias(true);
            float textSize = cellHeight * 0.4f;
            Log.d(TAG, "text size:" + textSize);
            monthPaint.setTextSize(textSize);
            monthPaint.setTypeface(Typeface.DEFAULT_BOLD);
            weekPaint = new Paint();
            weekPaint.setColor(textColor);
            weekPaint.setAntiAlias(true);
            float weekTextSize = weekHeight * 0.6f;
            weekPaint.setTextSize(weekTextSize);
            weekPaint.setTypeface(Typeface.DEFAULT_BOLD);
            datePaint = new Paint();
            datePaint.setColor(textColor);
            datePaint.setAntiAlias(true);
            //设置字体大小
            float cellTextSize = cellHeight * 0.4f;
            datePaint.setTextSize(cellTextSize);
            //设置粗体
            datePaint.setTypeface(Typeface.DEFAULT_BOLD);
            cellBgPaint = new Paint();
            cellBgPaint.setAntiAlias(true);
            cellBgPaint.setStyle(Paint.Style.FILL);
            cellBgPaint.setColor(cellSelectedColor);
        }
    }
}
