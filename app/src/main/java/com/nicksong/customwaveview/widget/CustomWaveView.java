package com.nicksong.customwaveview.widget;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.PointF;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.nicksong.customwaveview.R;

/**
 * 作者：NickSong
 * 创建时间：2016/11/30
 * 功能描述: 画波浪
 */

public class CustomWaveView extends View {

    private Paint mPaint;
    private Path mPath;
    private float mWidth;
    private float mHeight;
    private float mPeakHeight = 30f; //波峰高度
    private float mWaterHeight; //水平面高度
    private PointF pointF1; //波浪在水平面上第1个点
    private PointF pointF2; //波浪在水平面上第2个点
    private PointF pointF3; //波浪在水平面上第3个点
    private PointF pointF4; //波浪在水平面上第4个点
    private PointF pointF5; //波浪在水平面上第5个点
    private PointF ctrF1; //波浪第1个辅助点
    private PointF ctrF2; //波浪第2个辅助点
    private PointF ctrF3; //波浪第3个辅助点
    private PointF ctrF4; //波浪第4个辅助点
    private int waveColor = 0xBB0000FF; //画笔颜色
    private int duration = 3000; //波浪水平移动时间，值越大，波浪越缓慢，值越小，波浪越湍急
    private boolean mInited = false; //是否已经初始化
    private boolean mWaving = false; //波浪是已经开始
    private WaveViewHandler mHandler = new WaveViewHandler();

    public CustomWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 实例化画笔并设置参数
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomWaveView);
        waveColor = typedArray.getColor(R.styleable.CustomWaveView_waveColor, waveColor);
        duration = typedArray.getInt(R.styleable.CustomWaveView_duration, duration);
        typedArray.recycle();
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(waveColor);
        // 实例化路径对象
        mPath = new Path();
    }

    private void setPointF() {
        pointF1 = new PointF(-mWidth, mWaterHeight);
        pointF2 = new PointF(-mWidth / 2, mWaterHeight);
        pointF3 = new PointF(0, mWaterHeight);
        pointF4 = new PointF(mWidth / 2, mWaterHeight);
        pointF5 = new PointF(mWidth, mWaterHeight);
        ctrF1 = new PointF(pointF1.x + mWidth / 4, pointF1.y - mPeakHeight);
        ctrF2 = new PointF(pointF2.x + mWidth / 4, pointF2.y + mPeakHeight);
        ctrF3 = new PointF(pointF3.x + mWidth / 4, pointF3.y - mPeakHeight);
        ctrF4 = new PointF(pointF4.x + mWidth / 4, pointF4.y + mPeakHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 获取控件宽高
        if (!mInited) {
            mWidth = w;
            mHeight = h;
            mWaterHeight = mHeight / 2;
            mInited = true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!mInited || !mWaving) {
            return;
        }
        mPath.reset();
        mPath.moveTo(pointF1.x, pointF1.y);
        mPath.quadTo(ctrF1.x, ctrF1.y, pointF2.x, pointF2.y);
        mPath.quadTo(ctrF2.x, ctrF2.y, pointF3.x, pointF3.y);
        mPath.quadTo(ctrF3.x, ctrF3.y, pointF4.x, pointF4.y);
        mPath.quadTo(ctrF4.x, ctrF4.y, pointF5.x, pointF5.y);

        mPath.lineTo(pointF5.x, mHeight);
        mPath.lineTo(pointF1.x, mHeight);
        mPath.lineTo(pointF1.x, pointF1.y);

        canvas.drawPath(mPath, mPaint);
    }

    public void setWaveColor(int color) {
        this.waveColor = color;
        mPaint.setColor(color);
        postInvalidate();
    }

    public void setDuration(int duration) {
        this.duration = duration;
        postInvalidate();
    }

    private class WaveViewHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                setPointF();
                mWaving = true;
                startAnimation();
            }
        }
    }

    public void startWaving() {
       new Thread(new Runnable() {
           @Override
           public void run() {
               while (!mInited) {
                   try {
                       Thread.sleep(50);
                   } catch (Exception e) {

                   }
               }
               mHandler.sendEmptyMessage(0);
           }
       }).start();
    }

    private void startAnimation() {
        final ValueAnimator valueAnimator = ValueAnimator.ofFloat(pointF1.x, 0);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                pointF1.x = Float.valueOf(animation.getAnimatedValue().toString());
                pointF2 = new PointF(pointF1.x + mWidth / 2, mWaterHeight);
                pointF3 = new PointF(pointF2.x + mWidth / 2, mWaterHeight);
                pointF4 = new PointF(pointF3.x + mWidth / 2, mWaterHeight);
                pointF5 = new PointF(pointF4.x + mWidth / 2, mWaterHeight);
                ctrF1 = new PointF(pointF1.x + mWidth / 4, pointF1.y - mPeakHeight);
                ctrF2 = new PointF(pointF2.x + mWidth / 4, pointF2.y + mPeakHeight);
                ctrF3 = new PointF(pointF3.x + mWidth / 4, pointF3.y - mPeakHeight);
                ctrF4 = new PointF(pointF4.x + mWidth / 4, pointF4.y + mPeakHeight);
                invalidate();
            }
        });
        valueAnimator.start();
    }

}
