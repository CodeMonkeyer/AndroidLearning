package com.ubt.androidlearning.Drawable;

import android.content.Context;
import android.graphics.Bitmap;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;

/**
 * Created by Administrator on 2016/10/14.
 */

public class BlurBitmap {

    /**
     * 图片缩放比例
     */
    private static final float BITMAP_SCALE = 1f;
    /**
     * 最大模糊度(在0.0到25.0之间)
     */
    private static final float BLUR_RADIUS = 25f;


    public static Bitmap blur(Context context,Bitmap image)
    {

        int width = Math.round(image.getWidth()*BITMAP_SCALE);
        int height = Math.round(image.getHeight()*BITMAP_SCALE);

        Bitmap inputBitmap = Bitmap.createScaledBitmap(image,width,height,false);
        Bitmap outputBitmap = Bitmap.createBitmap(inputBitmap);

        RenderScript renderScript = RenderScript.create(context);

        ScriptIntrinsicBlur intrinsicBlur = ScriptIntrinsicBlur.create(renderScript,Element.U8_4(renderScript));

        // 由于RenderScript并没有使用VM来分配内存,所以需要使用Allocation类来创建和分配内存空间。
        // 创建Allocation对象的时候其实内存是空的,需要使用copyTo()将数据填充进去。
        Allocation tmpIn = Allocation.createFromBitmap(renderScript, inputBitmap);
        Allocation tmpOut = Allocation.createFromBitmap(renderScript, outputBitmap);

        // 设置渲染的模糊程度, 25f是最大模糊度
        intrinsicBlur.setRadius(BLUR_RADIUS);
        // 设置blurScript对象的输入内存
        intrinsicBlur.setInput(tmpIn);
        // 将输出数据保存到输出内存中
        intrinsicBlur.forEach(tmpOut);

        // 将数据填充到Allocation中
        tmpOut.copyTo(outputBitmap);

        return outputBitmap;
    }
}
