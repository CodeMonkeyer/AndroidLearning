package com.ubt.androidlearning;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;


/****主要知识点
 * 1、属性动画
 *   1.1 ObjectAnimator 单个、多个、包装view
 *   1.2 ValueAnimator  自己改变属性
 *   1.3 AnimatorSet 多个属性同时改变、多个属性依次改变
 * 2、xml动画
 *
 * 3、布局动画 Layout Animations
 *
 * */
public class AnimationActivity extends AppCompatActivity implements View.OnClickListener {


    private ImageView mImage;

    private float mScreenHeight = 0f;


    private Button btn1,btn2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        mImage = (ImageView) findViewById(R.id.iv_bounce_ball);
        DisplayMetrics displayMetrics = new DisplayMetrics();
         getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        mScreenHeight = displayMetrics.heightPixels;
        btn1 = (Button)findViewById(R.id.btn_1);
        btn2 = (Button)findViewById(R.id.btn_2);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);


    }

    //objectAnimator
    public void rotateAnimRun(final View view) {

        //改变单个属性
//       ObjectAnimator.ofFloat(view,"rotationY",0f,360f).setDuration(300).start();
        //改变多个属性
//        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"wilson",1.0f,0.0f).setDuration(500);
//        animator.start();
//        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float val = (Float) animation.getAnimatedValue();
//                view.setScaleX(val);
//                view.setScaleY(val);
//                view.setAlpha(val);
//
//            }
//        });
//        propertyValuesHolder(view);
        ViewWrapper viewWrapper = new ViewWrapper(view);
        ObjectAnimator.ofInt(viewWrapper,"height",viewWrapper.getWidth(),500,viewWrapper.getWidth()).setDuration(2000).start();
    }

    //一次改变多个属性
    public void propertyValuesHolder(View view) {
        PropertyValuesHolder propertyValuesHolderA = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f);
        PropertyValuesHolder propertyValuesHolderX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f);
        PropertyValuesHolder propertyValuesHolderY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, propertyValuesHolderA,
                propertyValuesHolderX, propertyValuesHolderY).setDuration(1000).start();

    }

    //valueAnimator
    private void verticalAnimate(final View view){

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0,mScreenHeight-200);
        valueAnimator.setTarget(view);
        valueAnimator.setDuration(1000);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                view.setTranslationY((Float) animation.getAnimatedValue());
            }
        });

    }


    private void parabolaAnimate(final View view){

        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(3000);
        valueAnimator.setObjectValues(new PointF(0, 0));
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setEvaluator(new TypeEvaluator<PointF>()
        {
            // fraction = t / duration
            @Override
            public PointF evaluate(float fraction, PointF startValue,
                                   PointF endValue)
            {
                // x方向200px/s ，则y方向0.5 * 10 * t
                PointF point = new PointF();
                point.x = 200 * fraction * 3;
                point.y = 0.5f * 200 * (fraction * 3) * (fraction * 3);
                return point;
            }
        });

        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
        {
            @Override
            public void onAnimationUpdate(ValueAnimator animation)
            {
                PointF point = (PointF) animation.getAnimatedValue();
                view.setX(point.x);
                view.setY(point.y);

            }
        });

    }

    private void scaleAndFadeOut(final View view)
    {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(2f,1f,0f);
        valueAnimator.setTarget(view);
        valueAnimator.setDuration(800);
        valueAnimator.start();
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                 view.setScaleX((Float) animation.getAnimatedValue());
                 view.setScaleY((Float) animation.getAnimatedValue());
                 view.setAlpha((Float) animation.getAnimatedValue());
            }
        });
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                ViewGroup viewGroup = (ViewGroup) view.getParent();
                if(viewGroup!=null)
                    viewGroup.removeView(view);
            }
    });
    }

    //同时执行多个动画 用AnimatorSet
    private void togetherAnimate(View view)
    {
        ObjectAnimator objectAnimator1 = ObjectAnimator.ofFloat(view,"scaleX",1.0f,2f);
        ObjectAnimator objectAnimator2 = ObjectAnimator.ofFloat(view,"scaleY",1.0f,2f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(2000);
        animatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.playTogether(objectAnimator1,objectAnimator2);
        animatorSet.start();

    }

    //同时执行多个动画 用AnimatorSet
    private void beforeAndAfterAnimate(View view)
    {

        float cy = view.getY();
        ObjectAnimator o1 = ObjectAnimator.ofFloat(view,"scaleX",1.0f,2.0f);
        ObjectAnimator o2 = ObjectAnimator.ofFloat(view,"scaleY",1.0f,2.0f);
        ObjectAnimator o3 = ObjectAnimator.ofFloat(view,"y",cy,(mScreenHeight-300));
        ObjectAnimator o4 = ObjectAnimator.ofFloat(view,"y",cy);
        ObjectAnimator o5 = ObjectAnimator.ofFloat(view,"scaleX",1.0f);
        ObjectAnimator o6 = ObjectAnimator.ofFloat(view,"scaleY",1.0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.play(o1).with(o2);
        animatorSet.play(o2).with(o3);
        animatorSet.play(o4).after(o3);
        animatorSet.play(o5).after(o3);
        animatorSet.play(o6).after(o3);
        animatorSet.setDuration(3000);
        animatorSet.start();


    }

    private void animateByXml(View view){

            //xml
//        Animator animator = AnimatorInflater.loadAnimator(this,R.animator.scale_x);
//        animator.setTarget(view);
//        animator.start();

            //set
          Animator animator = AnimatorInflater.loadAnimator(this,R.animator.scale_x_y);
          view.setPivotX(0);
          view.setPivotY(0);
          view.invalidate();
          animator.setTarget(view);
          animator.start();



    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btn_1:
//                verticalAnimate(mImage);
                beforeAndAfterAnimate(mImage);
            break;
            case R.id.btn_2:
//                parabolaAnimate(mImage);
//                scaleAndFadeOut(mImage);
//                togetherAnimate(mImage);
                animateByXml(mImage);
                startActivity(new Intent(AnimationActivity.this,LayoutAnimationActivity.class));
                break;
        }

    }


    private class ViewWrapper {
        private View mTarget;

        public ViewWrapper(View target) {
            mTarget = target;
        }

        public int getWidth() {
            return mTarget.getLayoutParams().width;
        }

        public void setWidth(int width) {
            mTarget.getLayoutParams().width = width;
            mTarget.requestLayout();
        }

        public int getHeight()
        {
            return  mTarget.getLayoutParams().height;
        }

        public void setHeight(int height)
        {
            mTarget.getLayoutParams().height = height;
            mTarget.requestLayout();
        }
    }

}
