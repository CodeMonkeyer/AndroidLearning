package com.ubt.androidlearning;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ubt.androidlearning.Drawable.CircluarDrawable;
import com.ubt.androidlearning.Drawable.FlowLayout;
import com.ubt.androidlearning.Drawable.RoundDrawable;

public class FlowActivity extends Activity {


    private FlowLayout mFlow;
    private ImageView ivRound;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flow);
        mFlow = (FlowLayout)findViewById(R.id.fl_fl);
        ivRound = (ImageView) findViewById(R.id.iv_round);
        mFlow.setAdapter(new MyAdapter());
        mFlow.setSelection(2);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dayu);
        RoundDrawable drawable = new RoundDrawable(bitmap);
        CircluarDrawable drawable2 = new CircluarDrawable(bitmap);
        ivRound.setImageDrawable(drawable2);

    }

    private class MyAdapter extends android.widget.BaseAdapter {

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = null;
            if (convertView == null) {
                view = LayoutInflater.from(FlowActivity.this).inflate(R.layout.item_layout, null);
            } else {
                view = convertView;
            }
            return view;
        }
    }
}
