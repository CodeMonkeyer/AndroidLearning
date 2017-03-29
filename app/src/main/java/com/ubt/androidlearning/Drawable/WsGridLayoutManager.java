/*
 * Copyright (c) 2008-2016 UBT Corporation.All rights reserved.
 * Redistribution,modification, and use in source and binary forms
 * are not permitted unless otherwise authorized by UBT.
 */

package com.ubt.androidlearning.Drawable;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;

/**
 * Created by Administrator on 2016/11/9.
 */

public class WsGridLayoutManager extends GridLayoutManager {

    private boolean isScrollable = true;

    public WsGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public void setScrollable(boolean isEnable){
        this.isScrollable = isEnable;

    }
    @Override
    public boolean canScrollHorizontally() {
        return isScrollable&&super.canScrollHorizontally();
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollable&&super.canScrollVertically();
    }
}
