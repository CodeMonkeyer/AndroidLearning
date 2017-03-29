package com.ubt.androidlearning.Drawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/3/29.
 */

public class BezierView extends View {

    private Paint mPaint;
    private int centerX,centerY;
    private PointF pStart,pEnd,pControl;
    private Path mPath;


    public BezierView(Context context) {
        this(context,null);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs) {
        this(context,attrs,0);
    }

    public BezierView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
        mPaint.setStrokeWidth(8);
        mPaint.setStyle(Paint.Style.STROKE);

        pStart = new PointF(0,0);
        pEnd = new PointF(0,0);
        pControl = new PointF(0,0);
    }

//    centerX = w/2;
//    centerY = h/2;
//
//    // 初始化数据点和控制点的位置
//    start.x = centerX-200;
//    start.y = centerY;
//    end.x = centerX+200;
//    end.y = centerY;
//    control.x = centerX;
//    control.y = centerY-100;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = w/2;
        centerY = h/2;
        pStart.x = centerX-200;
        pStart.y = centerY;
        pEnd.x = centerX+200;
        pEnd.y = centerY;

        pControl.x = centerX;
        pControl.y = centerY-100;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        pControl.x = event.getX();
        pControl.y = event.getY();
        invalidate();

        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        mPaint.setColor(Color.GRAY);
        mPaint.setStrokeWidth(20);
        canvas.drawPoint(pStart.x,pStart.y,mPaint);
        canvas.drawPoint(pEnd.x,pEnd.y,mPaint);
        canvas.drawPoint(pControl.x,pControl.y,mPaint);


        mPaint.setStrokeWidth(4);
        canvas.drawLine(pStart.x,pStart.y,pControl.x,pControl.y,mPaint);
        canvas.drawLine(pEnd.x,pEnd.y,pControl.x,pControl.y,mPaint);

        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(8);

        mPath = new Path();
        mPath.moveTo(pStart.x,pStart.y);
        mPath.quadTo(pControl.x,pControl.y,pEnd.x,pEnd.y);

        canvas.drawPath(mPath,mPaint);
    }
}
