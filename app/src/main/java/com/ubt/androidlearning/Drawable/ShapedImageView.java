package com.ubt.androidlearning.Drawable;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.ubt.androidlearning.R;

/**
 * Created by Administrator on 2017/3/17.
 */

public class ShapedImageView extends ImageView {

    /**圆**/
    public static final int ROUND = 0;

    /**圆角*/
    public static final int RECT = 1;

    /**圆角度*/
    public static final int BORDER_RADIUS = 10;//px


    private int shapeType = ROUND;

    private int borderRadius = BORDER_RADIUS;

    //圆角类型半径
    private int mRadius;


    /**画笔*/
    private Paint mPaint;
    private Paint mPaintBorder;

    /**矩阵变换 拉伸 缩小*/
    private Matrix mMatrix;

    /**着色器*/
    private BitmapShader mBitmapShader;

    /**view的宽度*/
    private int mWidth;
    private RectF mRoundRect;
    private float borderWidth;
    private float shadowRadius;
    private int shadowColor = 0xFF3f51b5;


    public ShapedImageView(Context context) {
        this(context,null);
    }

    public ShapedImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ShapedImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }


    private void init(Context context,AttributeSet attributeSet){
        TypedArray ta = context.obtainStyledAttributes(attributeSet, R.styleable.ShapedImageView);
        borderRadius = (int) ta.getDimension(R.styleable.ShapedImageView_borderRadius,BORDER_RADIUS);
        shapeType = ta.getInt(R.styleable.ShapedImageView_shape,ROUND);
        ta.recycle();

        float density = context.getResources().getDisplayMetrics().density;
        borderWidth = 6 * density;
        shadowRadius = 4 * density;

        mPaintBorder = new Paint();
        mPaintBorder.setColor(0xFF3f51b5);
        mPaintBorder.setAntiAlias(true);

        setLayerType(LAYER_TYPE_SOFTWARE, mPaintBorder);
        mPaintBorder.setShadowLayer(shadowRadius, 0, 0, shadowColor);

        mPaint = new Paint();
        mMatrix = new Matrix();
        mPaint.setAntiAlias(true);
    }


    private void setShader(){

        Drawable drawable = getDrawable();
        if(drawable == null)
            return;

        Bitmap bmp = drawable2Bitmap(drawable);
        mBitmapShader = new BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        float scale = 1.0f;
        switch (shapeType){
            case ROUND:
                int bsize = Math.min(bmp.getWidth(),bmp.getHeight());
                scale = mWidth*1.0f/bsize;

                break;
            case RECT:
                scale = Math.max(getWidth()*1.0f/bmp.getWidth(),getHeight()*1.0f/bmp.getHeight());

                break;


        }

        mMatrix.setScale(scale,scale);
        mBitmapShader.setLocalMatrix(mMatrix);
        mPaint.setShader(mBitmapShader);

    }


    private Bitmap drawable2Bitmap(Drawable drawable){

        if(drawable instanceof BitmapDrawable){
            BitmapDrawable bd = (BitmapDrawable)drawable;
            return bd.getBitmap();
        }

        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();
        Bitmap bitmap = Bitmap.createBitmap(w,h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0,0,w,h);
        drawable.draw(canvas);
        return bitmap;



    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(shapeType == ROUND){
            mWidth = Math.min(getMeasuredWidth(),getMeasuredHeight());
            mRadius = mWidth/2;
            setMeasuredDimension(mWidth,mWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        if(getDrawable() == null)
            return;
        setShader();
        if(shapeType == RECT){
            canvas.drawRoundRect(mRoundRect,borderRadius,borderRadius,mPaint);
        }else{

            canvas.drawCircle(mRadius,mRadius,mRadius,mPaintBorder);
            canvas.drawCircle(mRadius,mRadius,mRadius-shadowRadius,mPaint);

        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if(shapeType == RECT){
            mRoundRect = new RectF(0,0,getWidth(),getHeight());
        }
    }
}
