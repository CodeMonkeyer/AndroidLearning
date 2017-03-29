package com.ubt.androidlearning.Drawable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 2017/3/17.
 */

public class FlowLayout extends ViewGroup {


    private int mTouchSlop;
    private Scroller mScroller;


    private int leftBorder;
    private int rightBorder;


    private float mLastX;
    private int childWidth;

    private int selectPos = 0;

    private int mViewWidth;

    private Adapter mAdapter;

    public FlowLayout(Context context) {
        this(context,null);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
    }


    public void setAdapter(Adapter adapter) {
        this.mAdapter = adapter;
//        reset();
    }

    public void setSelection(int selection) {
        if (mAdapter == null) {
            throw new IllegalStateException("Error: Adapter is null.");
        }
        if (selection < 0 || selection > (mAdapter.getCount() - 1)) {
            throw new IllegalArgumentException("Position index must be in range of adapter values (0 - getCount()-1)");
        }
        this.selectPos = selection;
        reset();
    }

    private void reset(){
        if(mAdapter == null || mAdapter.getCount() == 0){
            return;
        }

        removeAllViewsInLayout();

        for(int i = 0 ;i<mAdapter.getCount();i++){
            View view = mAdapter.getView(i,null,this);
            addViewInLayout(view,0,new RelativeLayout.LayoutParams(320,320),true);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int count = getChildCount();
        for(int i = 0;i<count;i++){
            View view = getChildAt(i);
            measureChild(view,widthMeasureSpec,heightMeasureSpec);
        }

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        int count = getChildCount();
        int pianterX = l;
        for(int i = 0 ;i<count;i++){

            View child = getChildAt(i);
            int width = child.getMeasuredWidth();
            int height = child.getMeasuredHeight();
            childWidth = width;
            pianterX+=width;
            child.layout(l+i*width,t,l+(i+1)*width,t+height);
        }

        leftBorder = l-mViewWidth/2;
        rightBorder = pianterX+mViewWidth/2;
//        scroller2Center();

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

       switch (ev.getAction()){

           case MotionEvent.ACTION_DOWN:

               break;
           case MotionEvent.ACTION_UP:
               break;

       }

        return super.onInterceptTouchEvent(ev);
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        mLastX = event.getRawX();
        switch (event.getAction()) {

            case MotionEvent.ACTION_DOWN:
                break;

            case MotionEvent.ACTION_MOVE:
                float xMove = event.getRawX();
                int deltaX = (int) (mLastX - xMove);
                if ((getScrollX() + deltaX) < leftBorder) {
                    scrollTo(leftBorder, 0);
                } else if ((getScrollX() + childWidth + deltaX) > rightBorder) {
                    scrollTo(rightBorder - childWidth, 0);
                }
                scrollBy(deltaX, 0); //
                mLastX = xMove;
                updateSelection();
                break;
            case MotionEvent.ACTION_UP:
                scroller2Center();
                break;

        }
        return super.dispatchTouchEvent(event);
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        mLastX = event.getRawX();
//        switch (event.getAction()){
//
//            case MotionEvent.ACTION_DOWN:
//                break;
//
//            case MotionEvent.ACTION_MOVE:
//                float xMove = event.getRawX();
//                int deltaX = (int) (mLastX - xMove);
//                if ((getScrollX() + deltaX) < leftBorder) {
//                    scrollTo(leftBorder, 0);
//                } else if ((getScrollX() + childWidth + deltaX) > rightBorder) {
//                    scrollTo(rightBorder - childWidth, 0);
//                }
//                scrollBy(deltaX, 0); //
//                mLastX = xMove;
//                updateSelection();
//                break;
//            case MotionEvent.ACTION_UP:
//                scroller2Center();
//                break;
//
//        }
//
//
//        return super.onTouchEvent(event);
//    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
    }


    private void updateSelection() {
        selectPos = (getScrollX() + mViewWidth/2 ) / childWidth;
        if (selectPos < 0) {
            selectPos = 0;
        } else if (selectPos > mAdapter.getCount() - 1) {
            selectPos = mAdapter.getCount() - 1;
        }
    }

    private void scroller2Center(){

        if(mAdapter == null||mAdapter.getCount()==0){
            return;
        }

        if(selectPos<0||selectPos>mAdapter.getCount()-1){
            return;
        }

        View child = getChildAt(selectPos);
        int scrollX = (selectPos*childWidth+childWidth/2)-child.getScrollX()-mViewWidth/2;
        mScroller.startScroll(getScrollX(),0,scrollX,0);
        invalidate();

    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if(mScroller.computeScrollOffset()){
            scrollTo(mScroller.getCurrX(),mScroller.getCurrY());
            invalidate();
        }

        for(int i = 0 ;i<getChildCount();i++){

            View child = getChildAt(i);
            boolean select = i==selectPos;
            child.setScaleX(select?1f:0.75f);
            child.setScaleY(select?1f:0.75f);
            child.setAlpha(select?1f:0.5f);
        }
    }
}
