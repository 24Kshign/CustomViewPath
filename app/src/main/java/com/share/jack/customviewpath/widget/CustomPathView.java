package com.share.jack.customviewpath.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

/**
 *
 */

public class CustomPathView extends View {

    private Paint mPaint;
    private Path path;

    public CustomPathView(Context context) {
        this(context, null);
    }

    public CustomPathView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomPathView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();       // 创建画笔
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLACK);  // 画笔颜色 - 黑色
        mPaint.setStyle(Paint.Style.STROKE);  // 填充模式 - 描边
        mPaint.setStrokeWidth(5);

        path = new Path();
        path.moveTo(getWidth() / 4 * 3, getWidth() / 4);
        path.lineTo(getWidth() / 4, getWidth() / 4 * 3);
        path.moveTo(getWidth() / 4, getWidth() / 4);
        path.lineTo(getWidth() / 4 * 3, getWidth() / 4 * 3);
        canvas.drawPath(path, mPaint);
    }
}