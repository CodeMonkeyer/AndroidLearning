package com.ubt.androidlearning.AndroidIPC;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.ubt.androidlearning.MainActivity;

/**
 * Created by Administrator on 2016/10/8.
 */

public class RemoteService extends Service {

    public static final String TAG = "RemoteService";

    public  static final int MSG_INCOMING = 1;

    public  static final int MSG_INCOMING_REPLY = 3;

    public  static final int MSG_CLIENT= 4;

//    private IBinder mBinder = new LocalBinder(); //binder 方式

    private IncomingHandler incomingHandler = new IncomingHandler();

    final Messenger messenger = new Messenger(incomingHandler); //messenger方式 需要handler

    Messenger mClientReference;

    int count= 0;

    public RemoteService()
    {

    }

    class LocalBinder extends Binder
    {
        RemoteService getService()
        {
            return RemoteService.this;
        }
    }

    class IncomingHandler extends Handler
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what)
            {
                case MSG_INCOMING:
                    Toast.makeText(getApplicationContext(), "hello!", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_CLIENT:
                    mClientReference = (Messenger) msg.obj;
                    sendEmptyMessageDelayed(MSG_INCOMING_REPLY,1000);
                    break;
                case MSG_INCOMING_REPLY:
                    Message message = Message.obtain(null, MainActivity.MSG_CLIENT_SEND, 0, 0);
                    try {
                        mClientReference.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    sendEmptyMessageDelayed(MSG_INCOMING_REPLY,500);

                    break;
            }
        }
    }



    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"onCreate:");
        //called when first started only once
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG,"onStartCommand:"+startId);
        return super.onStartCommand(intent, flags, startId);
        //called when other component call startService();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind:");
//        return new ConnectImp();
        //called when other components call bindService();
        Toast.makeText(getApplicationContext(), "binding", Toast.LENGTH_SHORT).show();
        return messenger.getBinder();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG,"onUnbind:");
        incomingHandler.removeMessages(MSG_INCOMING_REPLY);
        incomingHandler = null;
        return super.onUnbind(intent);
        // true
    }

    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
    }
}
