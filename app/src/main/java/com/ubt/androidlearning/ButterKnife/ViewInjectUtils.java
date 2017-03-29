package com.ubt.androidlearning.ButterKnife;

import android.app.Activity;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * Created by Administrator on 2016/9/30.
 */

public class ViewInjectUtils {

    public static void inject(Activity activity)
    {
        injectContentView(activity);
        injectView(activity);
        injectEvent(activity);
    }

    //注入activity setContentView
    private static void injectContentView(Activity activity)
    {
        Class<? extends  Activity> clazz = activity.getClass();
        ContentView contentView = clazz.getAnnotation(ContentView.class);
        if(contentView != null)
        {
            int layoutId = contentView.value();
            try {
                Method setContentViewMethod = clazz.getMethod("setContentView",int.class);
                setContentViewMethod.invoke(activity,layoutId);
            }catch (NoSuchMethodException e1)
            {

            }catch (IllegalAccessException e2)
            {

            }catch (InvocationTargetException e3)
            {

            }

        }

    }

    //注入视图findViewById
    private static void injectView(Activity activity)
    {
        Class<? extends  Activity> clazz = activity.getClass();
        //获得activity的所有成员变量
        Field[] fields = clazz.getDeclaredFields();
        for(Field field :fields)
        {
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if(viewInject != null)
            {
                int viewId = viewInject.value();
                View view = activity.findViewById(viewId);
                try {
                    field.setAccessible(true);
                    field.set(activity,view);
                }catch (IllegalAccessException e)
                {

                }


            }
        }

    }

    //注入点击事件
    private static void injectEvent(final Activity activity)
    {
        Class<? extends Activity> clazz = activity.getClass();

        //获得activity的所有方法声明
        Method[] methods = clazz.getDeclaredMethods();
        for(final Method method2 :methods)
        {
            OnClick onClick = method2.getAnnotation(OnClick.class);
            if(onClick!=null)
            {
                int[] ids = onClick.value();
                method2.setAccessible(true);

                //动态代理 将点击事件OnClickListener 委托给listener对象 listener对象执行
                Object listener = Proxy.newProxyInstance(View.OnClickListener.class.getClassLoader(),
                        new Class[]{View.OnClickListener.class}, new InvocationHandler() {
                            @Override
                            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                               //代理类所执行的方法
                                return method2.invoke(activity,args);
                            }
                        });

                for(int id :ids)
                {

                    View view = activity.findViewById(id);
                    try {
                        Method setOnClickListener = view.getClass().getMethod("setOnClickListener",View.OnClickListener.class);
                        setOnClickListener.invoke(view,listener);
                    }catch (Exception e)
                    {

                    }

                }

            }

        }
    }
}
