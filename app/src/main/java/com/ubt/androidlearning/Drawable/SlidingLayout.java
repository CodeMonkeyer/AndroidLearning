package com.ubt.androidlearning.Drawable;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import com.ubt.androidlearning.R;

import static android.support.v4.widget.ViewDragHelper.INVALID_POINTER;

/**
 * @ClassName SlidingLayout
 * @date 2016/10/19 10:56
 * @author zhouchen
 * @Description bounce layout
 * @modifier
 * @modify_time
 */


public class SlidingLayout extends FrameLayout {

    public static final int SLIDING_MODE_BOTH = 0;
    public static final int SLIDING_MODE_TOP = 1;
    public static final int SLIDING_MODE_BOTTOM = 2;

    public static final int SLIDING_POINTER_MODE_ONE = 0;
    public static final int SLIDING_POINTER_MODE_MORE = 1;

    public static final int SLIDING_MAX_DISTANCE = 100;


    private int mTouchSlop;//系统允许最小的滑动判断值
    private int mBackgroundViewLayoutId = R.layout.view_background;

    private View mBackgroundView;//背景View
    private View mTargetView;//正面View

    private int mSlidingMode = SLIDING_MODE_BOTH;
    private int mSlidingPointerMode = SLIDING_POINTER_MODE_ONE;
    private int mSlidingMaxDistance = SLIDING_MAX_DISTANCE;

    private boolean mIsBeingDragged;
    private float mInitialDownY;
    private float mInitialMotionY;
    private float mLastMotionY;
    private int mActivePointerId = INVALID_POINTER;

    private float mSlidingOffset = 0.3F;//滑动阻力系数

    private static final int RESET_DURATION = 200;
    private static final int SMOOTH_DURATION = 1000;

    public SlidingLayout(Context context) {
        this(context,null);
    }

    public SlidingLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SlidingLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);

    }


    private void init (Context context,AttributeSet attrs)
    {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlidingLayout);
        mBackgroundViewLayoutId = a.getResourceId(R.styleable.SlidingLayout_background_view, mBackgroundViewLayoutId);
        mSlidingMode = a.getInteger(R.styleable.SlidingLayout_sliding_mode,SLIDING_MODE_BOTH);
        mSlidingPointerMode = a.getInteger(R.styleable.SlidingLayout_sliding_pointer_mode,SLIDING_POINTER_MODE_MORE);
        mSlidingMaxDistance = a.getDimensionPixelSize(R.styleable.SlidingLayout_sliding_margin,SLIDING_MAX_DISTANCE);
        a.recycle();
        if(mBackgroundViewLayoutId != 0){
            View view = View.inflate(getContext(), mBackgroundViewLayoutId, null);
            setBackgroundView(view);
        }
        mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();

    }
    public void setBackgroundView(View view){
        if(mBackgroundView != null){
            this.removeView(mBackgroundView);
        }
        mBackgroundView = view;
        this.addView(view, 0);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if(getChildCount() == 0)
            return;
        if(mTargetView == null)
            ensureTargetView();
        if(mTargetView == null)
             return;


    }

    private void ensureTargetView() {
        if(mTargetView == null)
            mTargetView = getChildAt(getChildCount()-1);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        Log.d("SlidingLayout","MotionEvent:"+ev.getAction());
        switch (ev.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                mActivePointerId = ev.getPointerId(0);
                mIsBeingDragged = false;
                final float initialDownY = getMotionEventY(ev,mActivePointerId);
//                if(initialDownY==-1)
//                    return  false;
                mInitialDownY = initialDownY;

                break;
            case MotionEvent.ACTION_MOVE:
                if (mActivePointerId == INVALID_POINTER) {
//                    Log.e(LOG_TAG, "Got ACTION_MOVE event but don't have an active pointer id.");
//                    return false;
                }
                final float y = getMotionEventY(ev, mActivePointerId);
//                if (y == -1) {
//                    return false;
//                }
                Log.d("SlidingLayout","y:"+y+",mInitialDownY:"+mInitialDownY);
                if(y>mInitialDownY) //上拉
                {
                    if(Math.abs(y-mInitialDownY)>mTouchSlop&&!mIsBeingDragged&&!canChildScrollUp())
                    {
                        mInitialMotionY = mInitialDownY+mTouchSlop;
                        mLastMotionY = mInitialMotionY;
                        mIsBeingDragged = true;
                    }

                }else//下滑
                {
                    if(Math.abs(y-mInitialDownY)>mTouchSlop&&!mIsBeingDragged&&!canChildScrollDown())
                    {
                        mInitialMotionY =  mInitialDownY+mTouchSlop;
                        mLastMotionY =  mInitialMotionY;
                        mIsBeingDragged = true;
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                mActivePointerId = INVALID_POINTER;
                break;


        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float delta = 0.0f;
                float movemment = 0.0f;
                float tempOffset = 1 - Math.abs(mTargetView.getTranslationY() / mTargetView.getMeasuredHeight());
                Log.d("SlidingLayout","translationY:"+mTargetView.getTranslationY()+", tempOffset:"+tempOffset);
                delta = (event.getY() - mInitialMotionY) * mSlidingOffset*tempOffset;
                //used for judge which side move to
                movemment = event.getY() - mInitialMotionY;

//                float distance = getSlidingDistance();

                mTargetView.clearAnimation();
                mTargetView.setTranslationY(delta);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
//                Log.i("onTouchEvent", "up");
//                if(mSlidingListener != null){
//                    mSlidingListener.onSlidingStateChange(this, STATE_IDLE);
//                }
                reset(mTargetView,RESET_DURATION);
                break;

        }
        return true;
    }

    private float getMotionEventY(MotionEvent ev, int activePointerId) {
        final int index = ev.findPointerIndex(activePointerId);
        if (index < 0) {
            return -1;
        }
        return ev.getY(activePointerId);
    }
    public void setSlidingDistance(int distance){
        this.mSlidingMaxDistance = distance;
    }

    public int getSlidingDistance(){
        return this.mSlidingMaxDistance;
    }

    /**
     * 判断View是否可以上拉
     * @return canChildScrollUp
     */
    public boolean canChildScrollUp() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (mTargetView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTargetView;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(mTargetView, -1) || mTargetView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTargetView, -1);
        }
    }

    /**
     * 判断View是否可以下拉
     * @return canChildScrollDown
     */
    public boolean canChildScrollDown() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            if (mTargetView instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) mTargetView;
                return absListView.getChildCount() > 0 && absListView.getAdapter() != null
                        && (absListView.getLastVisiblePosition() < absListView.getAdapter().getCount() - 1 || absListView.getChildAt(absListView.getChildCount() - 1)
                        .getBottom() < absListView.getPaddingBottom());
            } else {
                return ViewCompat.canScrollVertically(mTargetView, 1) || mTargetView.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(mTargetView, 1);
        }
    }

    public void reset(View view,long duration)
    {
        if(view == null)
            return;
        view.clearAnimation();
        ObjectAnimator.ofFloat(view,"translationY",0f).setDuration(duration).start();

    }
}
