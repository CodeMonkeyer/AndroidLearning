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

public class QQRedView extends View {

    public static final float CENTER_RADIUS = 50;
    public static final float SCOPE_RADIUS = 300;

    //原始圆半径
    public float mCenterRadius = CENTER_RADIUS;

    //拖拽有效范围
    public float mScopeRound = SCOPE_RADIUS;
    //red圆画笔
    private Paint mPaint;

    //有效范围画笔
    private Paint roundPaint;

    //贝塞尔曲线path
    private Path mPath;
    //当前手指拖拽点
    private PointF currentP;
    //原始圆心点
    private PointF centerP;
    //拖拽点和原始圆心点的中点 用于绘制贝塞尔曲线的控制点
    private PointF middleP;
    //拖拽点和原始圆心点的距离
    private float distanceBetween2points =0;
    //阻尼,原始圆心点的变小系数
    private float sensor = 0.75f;
    //判断是否绘制
    private boolean isCleanAll = false;
    //判断是否超出有效范围
    private boolean isBeyondRoundCircle = false;

    public QQRedView(Context context) {
        this(context,null);
    }

    public QQRedView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public QQRedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context){

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.FILL);

        roundPaint=  new Paint();
        roundPaint.setAntiAlias(true);
        roundPaint.setColor(Color.BLUE);
        roundPaint.setStrokeWidth(3);
        roundPaint.setStyle(Paint.Style.STROKE);

        centerP = new PointF();
        currentP = new PointF();
        middleP = new PointF();

        mPath = new Path();

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                currentP.x = event.getX();
                currentP.y = event.getY();
                distanceBetween2points = getDistanceBetween2points(centerP,currentP);
                isBeyondRoundCircle = (distanceBetween2points-CENTER_RADIUS/2)>mScopeRound;
                break;
            case MotionEvent.ACTION_DOWN:
                //如果点击位置不在原始范围内则不响应
                if(!checkTouchScope(event))
                    return false;//Action_Down返回false,则后续事件不会再继续接收

                currentP.x = event.getX();
                currentP.y = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                currentP.x = centerP.x;
                currentP.y = centerP.y;
                isCleanAll =  (distanceBetween2points-CENTER_RADIUS/2)>mScopeRound;
                distanceBetween2points = 0;
                break;

        }
        invalidate();
        return true;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerP.x = w/2;
        centerP.y = h/2;
        currentP.x = w/2;
        currentP.y = h/2;


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        canvas.drawCircle(centerP.x,centerP.y,mScopeRound,roundPaint);
        if(distanceBetween2points>0){
            if(distanceBetween2points<=mScopeRound){
                mCenterRadius = (1-distanceBetween2points*sensor/mScopeRound)*CENTER_RADIUS;
            }
        }else
            mCenterRadius = CENTER_RADIUS;

        if(!isCleanAll){
            canvas.drawCircle(currentP.x,currentP.y,CENTER_RADIUS,mPaint);
            if(!isBeyondRoundCircle){
                canvas.drawCircle(centerP.x,centerP.y,mCenterRadius,mPaint);
                drawBezierView(canvas);
            }


        }
    }


    private void drawBezierView(Canvas canvas){

        //设置为control point 贝塞尔控制点
        getCenterPointF(centerP,currentP);

        double link = (currentP.y-centerP.y)/(currentP.x-centerP.x);
        PointF[] centerPoint = getBezierStartPoint(centerP,mCenterRadius,link);
        PointF[] currentPoint = getBezierStartPoint(currentP,CENTER_RADIUS,link);

        mPaint.setColor(Color.BLUE);

//        canvas.drawCircle(middleP.x, middleP.y, 20f, mPaint);
//        canvas.drawCircle(centerPoint[0].x, centerPoint[0].y, 10f, mPaint);
//        canvas.drawCircle(centerPoint[1].x, centerPoint[1].y, 20f, mPaint);
//        canvas.drawCircle(currentPoint[0].x, currentPoint[0].y, 10f, mPaint);
//        canvas.drawCircle(currentPoint[1].x, currentPoint[1].y, 20f, mPaint);

        mPaint.setColor(Color.RED);

        mPath = new Path();
        //贝塞尔绘制起始点
        mPath.moveTo(centerPoint[0].x,centerPoint[0].y);

        //贝塞尔绘制控制点和终点
        mPath.quadTo(middleP.x,middleP.y,currentPoint[0].x,currentPoint[0].y);
//
        mPath.lineTo(currentPoint[1].x,currentPoint[1].y);

        mPath.quadTo(middleP.x,middleP.y,centerPoint[1].x,centerPoint[1].y);

        mPath.close();

        canvas.drawPath(mPath,mPaint);



    }


    private PointF getCenterPointF(PointF startPoint,PointF endPoint){

        middleP.x = (startPoint.x+endPoint.x)/2;
        middleP.y = (startPoint.y+endPoint.y)/2;

        return  middleP;

    }

    private float getDistanceBetween2points(PointF startPoint,PointF endPoint){

        float distance = 0 ;

        float disX = Math.abs(endPoint.x-startPoint.x);
        float disY = Math.abs(endPoint.y-startPoint.y);
        distance = (float) Math.sqrt(disX*disX+disY*disY);
        return distance;

    }

    /**
     * @Description
     * @param radiusPoint 圆心点
     * @param radius 半径
     * @param link 两个圆心之间的斜率
     * @return
     * @throws
     */

    private PointF[] getBezierStartPoint(PointF radiusPoint,float radius,double link){

        PointF[] pointF = new PointF[2];
        double alpha = Math.atan(link);
        float offsetX = (float) (radius*Math.sin(alpha));
        float offsetY = (float) (radius*Math.cos(alpha));
        PointF p1 = new PointF();
        p1.x = radiusPoint.x+offsetX;
        p1.y = radiusPoint.y-offsetY;

        PointF p2 = new PointF();
        p2.x = radiusPoint.x-offsetX;
        p2.y = radiusPoint.y+offsetY;
        pointF[0] = p1;
        pointF[1] = p2;
        return pointF;



    }

    private boolean checkTouchScope(MotionEvent event){
        if(event.getX()<(centerP.x-CENTER_RADIUS)||event.getX()>centerP.x+CENTER_RADIUS
                ||event.getY()>centerP.y+CENTER_RADIUS||event.getY()<centerP.y-CENTER_RADIUS){
                    return false;
                }

        return true;
    }
}
