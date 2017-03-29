package com.ubt.androidlearning;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Toast;

import com.ubt.androidlearning.AndroidIPC.RemoteService;
import com.ubt.androidlearning.ButterKnife.ContentView;
import com.ubt.androidlearning.ButterKnife.OnClick;
import com.ubt.androidlearning.ButterKnife.ViewInject;
import com.ubt.androidlearning.ButterKnife.ViewInjectUtils;
import com.ubt.androidlearning.Drawable.BlurBitmap;
import com.ubt.androidlearning.Drawable.IOSDownLoading;
import com.ubt.androidlearning.Drawable.SkippedView;
import com.ubt.androidlearning.processor.CustomAnnotation;


@CustomAnnotation
@ContentView(R.layout.activity_main)
public class MainActivity extends Activity{

    @ViewInject(R.id.btn)
    private Button btn;
    @ViewInject(R.id.btn_start)
    private Button btnStart;
    @ViewInject(R.id.skipped_view)
    private SkippedView mSkippedView;
    @ViewInject(R.id.ios_downloading)
            private IOSDownLoading iosDownLoading;
    @ViewInject(R.id.iv_blur)
            private ImageView ivBlur;
    @ViewInject(R.id.iv_blur_res)
            private ImageView ivBlurOriginal;
    @ViewInject(R.id.seek_blur)
    private SeekBar mSeekBar;

    private Bitmap mBlurBitmap;
    private Bitmap mOriginalBitmap;

    Intent serviceIntent;

    /** Messenger for communicating with the service. */
    Messenger mService = null;

    /** Flag indicating whether we have called bind on the service. */
    boolean mBound;

    public static final int MSG_CLIENT_SEND = 2;

    Messenger mClient = new Messenger(new ClientHandler());
    int count = 0;

    class ClientHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case MSG_CLIENT_SEND:
                    Toast.makeText(getApplicationContext(),++count+"",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewInjectUtils.inject(this);
//        serviceIntent = new Intent(this,RemoteService.class);
        // only activity services contentProvider can bind a service
//        bindService(serviceIntent,serviceConnection, Context.BIND_AUTO_CREATE);
        mSkippedView.setCountDownTimerListener(new SkippedView.CountDownTimerListener() {
            @Override
            public void onStartCount() {

            }

            @Override
            public void onFinishCount() {
            }
        });
        mOriginalBitmap = BitmapFactory.decodeResource(getResources(),R.drawable.dayu);
        mBlurBitmap = BlurBitmap.blur(this, mOriginalBitmap);
        ivBlur.setImageBitmap(mBlurBitmap);
        ivBlurOriginal.setImageBitmap(mOriginalBitmap);
        setSeekBar();
//        startService(serviceIntent);
//        PushManager.getInstance().initApp(getApplication());
//        setContentView(R.layout.activity_main);
//        btn = (Button)findViewById(R.id.btn);
//        btnStart =(Button)findViewById(R.id.btn_start);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                startActivity(new Intent(MainActivity.this,SecondActivity.class));
                startActivity(new Intent(MainActivity.this,BezierActivity.class));
//                startActivity(new Intent(MainActivity.this,AnimationActivity.class));
//                startActivity(new Intent(MainActivity.this,ScrollInterceptActivity.class));
//                startActivity(new Intent(MainActivity.this,FlowActivity.class));

            }
        });

    }
    private void setSeekBar() {
        mSeekBar.setMax(100);
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                ivBlurOriginal.setAlpha((int) (255 - progress * 2.55));
//                mProgressTv.setText(String.valueOf(mAlpha));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId())
//        {
//            case R.id.btn_start:
//                startAnimation();
//                break;
//        }
//    }


    private void startAnimation()
    {


    }


   @OnClick(R.id.btn_start)
    private void tanslationAnimate(View v)
    {
       btn.setText("hahahahahaha");
//        if (mBound) {
//            unbindService(serviceConnection);
//            mBound = false;
//        }
        mSkippedView.start();
        iosDownLoading.start();
//        startService(serviceIntent);

//        Messages msgs = new Messages();
//        msgs.setId(123456);
//        msgs.setContent("ipc call message----------------");
//        PushManager.getInstance().sendMessage(msgs);
    }

    @OnClick(R.id.skipped_view)
    private void skippedView(View v)
    {
        mSkippedView.stop();
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(RemoteService.TAG,"--------onServiceConnected------------");
            mService = new Messenger(service);
            mBound = true;
            Message message = new Message();
            message.what = RemoteService.MSG_CLIENT;
            message.obj = mClient;
            try {
                mService.send(message);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(RemoteService.TAG,"--------onServiceDisconnected------------");
            mService = null;
            mBound = false;
        }
    };

    public void sayHello(View v) {
        if (!mBound) return;
        // Create and send a message to the service, using a supported 'what' value
        Message msg = Message.obtain(null, RemoteService.MSG_INCOMING, 0, 0);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Unbind from the service
        if (mBound) {
            unbindService(serviceConnection);
            mBound = false;
        }
    }
}
