/*
 * Copyright (c) 2008-2016 UBT Corporation.All rights reserved.
 * Redistribution,modification, and use in source and binary forms
 * are not permitted unless otherwise authorized by UBT.
 */

package com.ubt.androidlearning;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/11/8.
 */

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    protected Context mContext;
    protected List<T> mDatas;
    protected int[] mLayoutId;
    protected onItemClickListener mOnItemClickListener;


    protected abstract void onViewCreate(BaseViewHolder holder);

    protected abstract void onBind(BaseViewHolder holder,T t,int position);

    protected abstract int onType(int position);

    public interface onItemClickListener{
        void onItemClick(View view, BaseViewHolder holder, int position);

        boolean onItemLongClickListener(View view, BaseViewHolder holder, int position);
    }

    public BaseAdapter(Context context, List<T> datas, int[] layoutId){
        this.mContext = context;
        this.mDatas = datas;
        this.mLayoutId = layoutId;
    }
    public BaseAdapter(Context context, List<T> datas){
        this.mContext = context;
        this.mDatas = datas;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder holder = null;
        switch (viewType){
            case 0:
                holder = new BaseViewHolder(parent.getContext()
                        , LayoutInflater.from(parent.getContext()).inflate(mLayoutId[0],parent,false));
                break;
            case 1:
                holder = new BaseViewHolder(parent.getContext()
                        , LayoutInflater.from(parent.getContext()).inflate(mLayoutId[1],parent,false));
                break;

        }
        onViewCreate(holder);
        setListeners(parent,holder,viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        onBind(holder,mDatas.get(position),position);
    }

    @Override
    public int getItemViewType(int position) {
        return onType(position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    private void setListeners(final ViewGroup parent, final BaseViewHolder viewHolder, int viewType){

//        viewHolder.getConvertView()
//                .setOnClickListener(v ->{
//                    if(mOnItemClickListener!=null){
//                        mOnItemClickListener.onItemClick(v,viewHolder,viewHolder.getAdapterPosition());
//                    }
//                });
//
//        viewHolder.getConvertView()
//                .setOnLongClickListener(v -> {
//                    if(mOnItemClickListener!=null){
//                       return mOnItemClickListener
//                               .onItemLongClickListener(v,viewHolder,viewHolder.getAdapterPosition());
//                    }
//                    return false;
//                });
    }

    public void setOnItemClick(onItemClickListener listener){
        this.mOnItemClickListener = listener;
    }



    public void setDatas(List<T> mDatas){
        this.mDatas = mDatas;
    }

    public void setLayoutParams(BaseViewHolder holder){

    }


}
