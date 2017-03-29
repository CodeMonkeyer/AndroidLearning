/*
 * Copyright (c) 2008-2016 UBT Corporation.All rights reserved.
 * Redistribution,modification, and use in source and binary forms
 * are not permitted unless otherwise authorized by UBT.
 */

package com.ubt.androidlearning.Drawable;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Administrator on 2016/11/9.
 */

public class WsLinearLayoutManager extends LinearLayoutManager {

    private boolean isScrollable = true;

    public WsLinearLayoutManager(Context context, int orientation, boolean reverse) {
        super(context,orientation,reverse);
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
