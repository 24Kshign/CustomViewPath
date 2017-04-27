package com.share.jack.customviewpath.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by jack on 17/4/25
 */

public class CustomPathMeasure extends View {

    private ValueAnimator valueAnimator;
    private Paint paint;
    private Path path;
    private PathMeasure pathMeasure;
    private Path mDst;
    private float mAnimatorValue;
    private float mLength;
    private float radius = 160;
    private float circleWidth = 10;

    private boolean isInit = false;

    public CustomPathMeasure(Context context) {
        this(context, null);
    }

    public CustomPathMeasure(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomPathMeasure(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        pathMeasure = new PathMeasure();
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(circleWidth);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!isInit) {
            path = new Path();
            path.addCircle(getWidth() / 2, getHeight() / 2, radius, Path.Direction.CW);
            pathMeasure.setPath(path, true);
            mLength = pathMeasure.getLength();
            mDst = new Path();
            isInit = true;
            Log.d("TAG", "mLength----->" + mLength);
        }
        mDst.reset();
        // 硬件加速的BUG
        mDst.lineTo(0,0);
        float stop = mLength * mAnimatorValue;
        Log.d("TAG", "stop----->" + stop);
        Log.d("TAG", "AnimatorValue----->" + mAnimatorValue);
        float start = (float) (stop - ((0.5 - Math.abs(mAnimatorValue - 0.5)) * mLength));
        pathMeasure.getSegment(start, stop, mDst, true);
        canvas.drawPath(mDst, paint);
    }

    public void startAnim() {
        valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        valueAnimator.setDuration(1000);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.start();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);

        if (mode == MeasureSpec.EXACTLY) {
            width = size;
        } else {
            width = (int) (2 * radius + circleWidth);
        }

        mode = MeasureSpec.getMode(heightMeasureSpec);
        size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            height = size;
        } else {
            height = (int) (2 * radius + circleWidth);
        }
        setMeasuredDimension(width, height);
    }
}