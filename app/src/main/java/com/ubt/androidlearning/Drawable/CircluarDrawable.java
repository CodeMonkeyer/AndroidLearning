package com.ubt.androidlearning.Drawable;

import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;

/**
 * Created by Administrator on 2017/3/17.
 */

public class CircluarDrawable extends Drawable {


    private Bitmap mBitmap;
    private Paint mPaint;
    private int mWidth;


    public CircluarDrawable(Bitmap bitmap){

        this.mBitmap = bitmap;
        mPaint = new Paint();
        BitmapShader bitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        mPaint.setAntiAlias(true);
        mPaint.setShader(bitmapShader);
        mWidth = Math.min(bitmap.getHeight(),bitmap.getWidth());
    }
    @Override
    public void draw(Canvas canvas) {

        canvas.drawCircle(mWidth/2,mWidth/2,mWidth/2,mPaint);
    }

    @Override
    public void setAlpha(int alpha) {

        mPaint.setAlpha(alpha);

    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

        mPaint.setColorFilter(colorFilter);
    }


    @Override
    public int getIntrinsicHeight() {
        return mWidth;
    }


    @Override
    public int getIntrinsicWidth() {
        return mWidth;
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }
}
