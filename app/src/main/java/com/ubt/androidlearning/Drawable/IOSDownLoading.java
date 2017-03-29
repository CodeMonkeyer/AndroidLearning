package com.ubt.androidlearning.Drawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.CountDownTimer;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.ubt.androidlearning.R;

import static android.content.ContentValues.TAG;

/**
 * Created by Administrator on 2016/10/14.
 */

public class IOSDownLoading extends View {

    private static final int BACKGROUND_COLOR = 0xFF0073FF;
    private static final float BORDER_WIDTH = 6f;
    private static final int BORDER_COLOR = 0xFF0073FF;
    private static final String TEXT = "跳过";
    private static final float TEXT_SIZE = 20f;
    private static final int TEXT_COLOR = 0xFFFFFFFF;

    //用于测量宽高的默认值
    private static final int DEFAULT_VIEW_WIDTH = 80;
    private static final int DEFAULT_VIEW_HEIGHT = 80;

    private int backgroundColor; //背景色

    private float borderWidth;

    private int borderColor;

    private String text; //跳过广告

    private int textColor;

    private float textSize;

    private Paint mCirclePaint,mBorderPaint,mTextPaint,mInsidePaint;

//    private Path mPath;

    private float mInsideRect = 10;

    private float mCircleStrokeWidth = 3;

    private float mProgress;

    private  CountDownTimerListener listener;

    private CountDownTimer mCountDownTimer;

    public IOSDownLoading(Context context) {
        this(context,null);
    }

    public IOSDownLoading(Context context, AttributeSet attrs) {
        this(context,attrs,0);
    }

    public IOSDownLoading(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context,attrs,defStyleAttr);
    }

    void initViews(Context context, AttributeSet attrs, int defStyleAttr)
    {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SkippedView);
        backgroundColor = typedArray.getColor(R.styleable.SkippedView_background_color,BACKGROUND_COLOR);
        borderWidth = typedArray.getDimension(R.styleable.SkippedView_broader_width,BORDER_WIDTH);
        borderColor = typedArray.getColor(R.styleable.SkippedView_broader_color,BORDER_COLOR);
        text = typedArray.getString(R.styleable.SkippedView_text);
        if(text == null)
            text = TEXT;
        textColor = typedArray.getColor(R.styleable.SkippedView_text_color,TEXT_COLOR);
        textSize = typedArray.getDimension(R.styleable.SkippedView_text_size,TEXT_SIZE);
        typedArray.recycle();
        initResources();
    }

    private void initResources()
    {
        //圆形背景
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
//        mCirclePaint.setDither(true);
        mCirclePaint.setColor(backgroundColor);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(mCircleStrokeWidth);

        mInsidePaint = new Paint();
        mInsidePaint.setAntiAlias(true);
        mInsidePaint.setColor(backgroundColor);
        mInsidePaint.setStyle(Paint.Style.FILL);

        //圆圈
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
//        mBorderPaint.setDither(true);
        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStrokeWidth(borderWidth);
        mBorderPaint.setStyle(Paint.Style.STROKE);

        //文字
        mTextPaint  = new Paint();
        mTextPaint.setAntiAlias(true);
//        mTextPaint.setDither(true);
        mTextPaint.setTextSize(textSize);
        mTextPaint.setColor(textColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

//        mPath = new Path();
    }


    //测量view的大小，当view的width和height属性是wrap_content的时候需要确定的大小
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);

        int height = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

//如果测量模式不是EXACTLY,表示父控件不清楚当前View的宽高，即wrap_content,则用默认值
        if(widthMode != MeasureSpec.EXACTLY)
        {
            width = DEFAULT_VIEW_WIDTH;
        }

        if(heightMode != MeasureSpec.EXACTLY)
        {
            height = DEFAULT_VIEW_HEIGHT;
        }
        setMeasuredDimension(width,height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int min = Math.min(width,height);

        //画圆圈
        canvas.drawCircle(width/2,height/2,min/2-mCircleStrokeWidth/2,mCirclePaint);

        canvas.drawRect(width/2-mInsideRect,height/2-mInsideRect,width/2+mInsideRect,height/2+mInsideRect,mInsidePaint);

        //画边框 drawArc画圆弧 参数为圆弧所在矩形框
        if (width > height) {
            canvas.drawArc(width / 2 - min / 2 + borderWidth / 2, 0 + borderWidth / 2,
                    width / 2 + min / 2 - borderWidth / 2, height - borderWidth / 2,
                    -90,mProgress,false,mBorderPaint);
        } else {
            canvas.drawArc(mCircleStrokeWidth+borderWidth/2, borderWidth/2+mCircleStrokeWidth,
                    width-borderWidth/2-mCircleStrokeWidth , height / 2  + min / 2-borderWidth/2-mCircleStrokeWidth,
                    -90,mProgress,false,mBorderPaint);
        }
        //画居中的文字
//       canvas.drawText(text, width / 2, height / 2 - mTextPaint.descent() + mTextPaint.getTextSize() / 2, mTextPaint);

    }

    public void start() {
        if (listener != null) {
            listener.onStartCount();
        }
        mCountDownTimer= new CountDownTimer(3600, 16) {
            @Override
            public void onTick(long millisUntilFinished) {
                mProgress = ((3600 - millisUntilFinished) / 3600f) * 360;
                Log.d(TAG, "millisUntilFinished:"+millisUntilFinished+",progress:" + mProgress);
                invalidate();
            }

            @Override
            public void onFinish() {
                mProgress = 360;
                invalidate();

                if (listener != null) {
                    listener.onFinishCount();
                }
//                stop();
            }
        }.start();
    }

    public void stop()
    {
        if(mCountDownTimer!=null)
            mCountDownTimer.cancel();
        setVisibility(View.GONE);
    }

    public void setCountDownTimerListener(CountDownTimerListener listener) {
        this.listener = listener;
    }

    public interface CountDownTimerListener {

        void onStartCount();

        void onFinishCount();
    }
}
