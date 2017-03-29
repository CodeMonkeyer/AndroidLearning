package com.ubt.androidlearning;

import android.app.Activity;
import android.os.Bundle;

import com.ubt.androidlearning.ButterKnife.ViewInjectUtils;
import com.ubt.androidlearning.Drawable.BaseRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class ScrollInterceptActivity extends Activity {


    BaseRecyclerView baseRecyclerView1;
    BaseRecyclerView baseRecyclerView2;

    private List<String> mDatas = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInjectUtils.inject(this);
        setContentView(R.layout.activity_scroll_intercept);
        baseRecyclerView1 = (BaseRecyclerView)findViewById(R.id.ryc_1);
        baseRecyclerView2 = (BaseRecyclerView)findViewById(R.id.ryc_2);
        for(int i = 0;i<20;i++){
            mDatas.add("sssssssssssss");
        }
        adapter1.setDatas(mDatas);
        baseRecyclerView1.setAdapter(adapter1);
        adapter2.setDatas(mDatas);
        baseRecyclerView2.setAdapter(adapter2);
    }



    final  BaseAdapter<String> adapter1 = new BaseAdapter<String>(this,mDatas,new int[]{R.layout.layout_scroll_item}){

        @Override
        protected void onViewCreate(BaseViewHolder holder) {

        }

        @Override
        protected void onBind(BaseViewHolder holder, String s, int position) {
//            ((ImageView)holder.getView(R.id.tv_string)).setBackgroundResource(R.drawable.little_apple);
        }

        @Override
        protected int onType(int position) {
            return 0;
        }
    };

    final  BaseAdapter<String> adapter2 = new BaseAdapter<String>(this,mDatas,new int[]{R.layout.layout_scroll_item}){

        @Override
        protected void onViewCreate(BaseViewHolder holder) {

        }

        @Override
        protected void onBind(BaseViewHolder holder, String s, int position) {
//            ((ImageView)holder.getView(R.id.tv_string)).setBackgroundResource(R.drawable.little_apple);
        }

        @Override
        protected int onType(int position) {
            return 0;
        }
    };
}
