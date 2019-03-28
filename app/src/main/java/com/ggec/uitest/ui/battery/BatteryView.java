package com.ggec.uitest.ui.battery;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ggec.uitest.R;

/**
 * 自定义一个BatteryView，通过onDraw()来控制画图
 * BatteryView的执行顺序：BatteryView -> initView -> onMeasure -> onDraw -> setPower
 * 如果是在布局文件引用该控件，则调用BatteryView(Context context, @Nullable AttributeSet attrs)
 * */
public class BatteryView extends View {
    private static final String TAG = "BatteryView";

    private int mBorderWidth = 40;     // 电池宽度
    private int mBorderHeight = 20;    // 电池高度
    private int mHeadWidth = 4;          // 电池盖宽度
    private int mHeadHeight = 8;         // 电池盖高度
    private int mBorderStroke = 1;     // 电池边框画笔宽度
    private int mBatteryPadding = 1;      // 电池内部边距
    private int mBatteryWidth = mBorderWidth - mBorderStroke - mBatteryPadding * 2;   // 电量宽度
    private int mBatteryHeight = mBorderHeight - mBorderStroke - mBatteryPadding * 2; // 电量高度
    private int mPower = 50;   // 当前电量，默认为50

    private float scale;            // dp转px的倍数
    private Paint mBorderPaint;    // 电池边框笔
    private Paint mBatteryPaint;      // 电池电量笔
    private Paint mHeadPaint;        // 电池盖笔
    private RectF mBorderRect;     // 电池边框矩形
    private RectF mBatteryRect;       // 电池电量矩形
    private RectF mHeadRect;         // 电池盖矩形

    public BatteryView(Context context) {
        super(context);
        Log.v(TAG,"BatteryView(Context context)");
        initView(context);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        Log.v(TAG,"BatteryView(Context context, @Nullable AttributeSet attrs)");
        initView(context);
    }

    public BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        Log.v(TAG,"BatteryView(Context context, @Nullable AttributeSet attrs, int defStyleAttr)");
        initView(context);
    }

    private void initView(Context context) {
        Log.v(TAG,"initView()");
        // 有时候View还没有算好宽高度，在执行onMeasure()才计算好
        int width1 = this.getMeasuredWidth();
        int height1 = this.getMeasuredHeight();
        int width2 = this.getWidth();
        int height2 = this.getHeight();
        Log.v(TAG,"width1 = " + width1 + ",height1 = " + height1 + ",width2 = " + width2 + ",height2 = " + height2);

        scale = context.getResources().getDisplayMetrics().density;
        mBorderWidth = dp2px(mBorderWidth);
        mBorderHeight = dp2px(mBorderHeight);
        mHeadWidth = dp2px(mHeadWidth);
        mHeadHeight = dp2px(mHeadHeight);
        mBatteryPadding = dp2px(mBatteryPadding);
        mBorderStroke = dp2px(mBorderStroke);
        mBatteryWidth = dp2px(mBatteryWidth);
        mBatteryHeight = dp2px(mBatteryHeight);
        Log.i(TAG,"scale = " + scale + ",mBorderWidth = " + mBorderWidth + ",mBorderHeight = " + mBorderHeight
                + ",mHeadWidth = " + mHeadWidth + ",mHeadHeight = " + mHeadHeight
                + ",mBatteryPadding = " + mBatteryPadding + ",mBorderStroke = " + mBorderStroke
                + ",mBatteryWidth = " + mBatteryWidth + ",mBatteryHeight = " + mBatteryHeight);

        // 设置电池边框笔和矩形
        mBorderPaint = new Paint();
        mBorderPaint.setColor(Color.WHITE);
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setStrokeWidth(mBorderStroke);
        mBorderRect = new RectF(0, 0, mBorderWidth, mBorderHeight);

        // 设置电池电量笔和矩形
        mBatteryPaint = new Paint();
        mBatteryPaint.setColor(getResources().getColor(R.color.battery_60));
        mBatteryPaint.setAntiAlias(true);
        mBatteryPaint.setStyle(Paint.Style.FILL);
        float powerPercent = mPower / 100.0f;
        int pLeft = mBorderStroke /2 + mBatteryPadding;
        int pTop = mBorderStroke /2 + mBatteryPadding;
        int pRight = pLeft + (int)(mBatteryWidth * powerPercent);
        int pBottom = pTop + mBatteryHeight;
        mBatteryRect = new RectF(pLeft, pTop, pRight , pBottom);

        // 设置电池盖笔和矩形
        mHeadPaint = new Paint();
        mHeadPaint.setColor(Color.WHITE);
        mHeadPaint.setAntiAlias(true);
        mHeadPaint.setStyle(Paint.Style.FILL);
        mHeadRect = new RectF(mBorderWidth, (mBorderHeight - mHeadHeight) / 2,
                mBorderWidth + mHeadWidth, (mBorderHeight - mHeadHeight) / 2 + mHeadHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.v(TAG,"onDraw");
        //先画电池
        canvas.drawRect(mBorderRect, mBorderPaint);
        //画电量
        canvas.drawRect(mBatteryRect, mBatteryPaint);
        // 画电池盖
        canvas.drawRect(mHeadRect, mHeadPaint);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Log.e(TAG,"onMeasure,widthMeasureSpec = " + widthMeasureSpec + ",heightMeasureSpec = " + heightMeasureSpec);
        int measureWidth = MeasureSpec.getSize(widthMeasureSpec);
        int measureHeight = MeasureSpec.getSize(heightMeasureSpec);
        Log.e(TAG,"measureWidth = " + measureWidth + ",measureHeight = " + measureHeight);
        setMeasuredDimension(measureWidth, measureHeight);
    }

    private int dp2px(int px) {
        return  (int) (px * scale + 0.5f);
    }

    public void setPower(int power) {
        mPower = power;
        if(mPower < 0) {
            mPower = 0;
        }
        if(mPower <= 20) {
            mBatteryPaint.setColor(getResources().getColor(R.color.battery_20));
        } else if (mPower <= 40) {
            mBatteryPaint.setColor(getResources().getColor(R.color.battery_40));
        } else if (mPower <= 60) {
            mBatteryPaint.setColor(getResources().getColor(R.color.battery_60));
        } else {
            mBatteryPaint.setColor(getResources().getColor(R.color.battery_100));
        }
        Log.i(TAG,"mPower = " + mPower);
        float powerPercent = mPower / 100.0f;
        int pLeft = mBorderStroke /2 + mBatteryPadding;
        int pTop = mBorderStroke /2 + mBatteryPadding;
        int pRight = pLeft + (int)(mBatteryWidth * powerPercent);
        int pBottom = pTop + mBatteryHeight;
        mBatteryRect = new RectF(pLeft, pTop, pRight , pBottom);
        invalidate();
    }
}
