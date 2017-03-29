/*
 * Copyright (c) 2008-2016 UBT Corporation.All rights reserved.
 * Redistribution,modification, and use in source and binary forms
 * are not permitted unless otherwise authorized by UBT.
 */

package com.ubt.androidlearning;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by Administrator on 2016/11/9.
 */

public class BaseViewHolder extends RecyclerView.ViewHolder {


    private SparseArray<View> mViews;

    private View mConvertView;

    private Context mContext;


    public BaseViewHolder(Context context, View itemView) {
        super(itemView);
        this.mContext = context;
        this.mConvertView = itemView;
        this.mViews = new SparseArray<>();
    }

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    public <T extends View> T getView(int viewId)
    {
        View view = mViews.get(viewId);
        if (view == null)
        {
            view = mConvertView.findViewById(viewId);
            mViews.put(viewId, view);
        }
        return (T) view;
    }

    public View getConvertView(){

        return mConvertView;
    }
}
