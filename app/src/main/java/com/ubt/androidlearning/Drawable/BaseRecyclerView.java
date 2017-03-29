/*
 * Copyright (c) 2008-2016 UBT Corporation.All rights reserved.
 * Redistribution,modification, and use in source and binary forms
 * are not permitted unless otherwise authorized by UBT.
 */

package com.ubt.androidlearning.Drawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.ubt.androidlearning.R;

/**
 * @ClassName BaseRecyclerView
 * @date 2016/11/9 15:34 
 * @author zhouchen
 * @Description
 * @modifier
 * @modify_time
 */

public class BaseRecyclerView extends RecyclerView {


    public static final int LINEAR = 0;
    public static final int GRID = 1;
    public static final int STAGGER = 2;

    private int lastWidth;
    private int mOrientation = VERTICAL;
    private int mLayout = LINEAR;
    private int mSpan = 3; //默认grid布局为3行3列
    private boolean mCenterLayout = true;

    private LayoutManager mLayoutManager;

    public BaseRecyclerView(Context context) {
        this(context,null);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BaseRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs){

        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.BaseRecyclerView);
        mOrientation = ta.getInt(R.styleable.BaseRecyclerView_rcy_orientation,mOrientation);
        mLayout = ta.getInt(R.styleable.BaseRecyclerView_rcy_layout,mLayout);
        mSpan = ta.getInt(R.styleable.BaseRecyclerView_rcy_span,mSpan);
        mCenterLayout = ta.getBoolean(R.styleable.BaseRecyclerView_rcy_center,mCenterLayout);
        switch (mLayout){
            case LINEAR:
                mLayoutManager = new WsLinearLayoutManager(context,mOrientation,false);
                break;
            case GRID:
                mLayoutManager = new WsGridLayoutManager(context,mSpan,mOrientation,false);
                break;
            case STAGGER:
//                mLayoutManager = new WsStaggeredLayoutManager(mSpan,mOrientation);
                break;
            default:
                mLayoutManager = new WsLinearLayoutManager(context,mOrientation,false);
                break;
        }
        ta.recycle();
        this.setHasFixedSize(true);
        this.setLayoutManager(mLayoutManager);
        //取消单个item刷新闪烁 API<=22
//        this.getItemAnimator().setSupportsChangeAnimations(false);
    }

    
    /**
     * @Description 
     * @param isEnable 是否可滑动
     * @return 
     * @throws 
     */
    
    public void setScrollable(boolean isEnable){

        switch (mLayout){

            case LINEAR:
                ((WsLinearLayoutManager)mLayoutManager).setScrollable(isEnable);
                break;

            case GRID:
                ((WsGridLayoutManager)mLayoutManager).setScrollable(isEnable);
                break;

            case STAGGER:
//                ((WsStaggeredLayoutManager)mLayoutManager).setScrollable(isEnable);
                break;
        }

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent e) {

        switch (e.getAction()){

//            case MotionEvent.ACTION_DOWN:
//            case MotionEvent.ACTION_MOVE:
//                requestDisallowInterceptTouchEvent(true);
//                break;
//            case MotionEvent.ACTION_CANCEL:
//            case MotionEvent.ACTION_UP:
//                requestDisallowInterceptTouchEvent(false);
//                break;

        }

        return super.onTouchEvent(e);

    }

    //添加padding的方式使子view居中(子view不满一屏时)
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(!mCenterLayout)
            return;
        if (getChildCount() > 0) {
            int newWidth = 0;
            for (int i = 0; i < getChildCount(); i++) {
                newWidth += getChildAt(i).getMeasuredWidth();
            }
            if (lastWidth!=newWidth) {
                lastWidth = newWidth;

                int empty = getMeasuredWidth() - newWidth;
                if (empty > 0) {
                    if (getPaddingLeft() == empty / 2) {
                        return;
                    }

                    setPadding(empty / 2, 0, empty / 2, 0);
                    //如果不再一次onLayout，子view就不会有padding
                    super.onLayout(changed, l, t, r, b);
                }
            }
        }
    }

}
