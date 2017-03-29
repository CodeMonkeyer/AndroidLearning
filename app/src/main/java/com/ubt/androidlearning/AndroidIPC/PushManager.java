package com.ubt.androidlearning.AndroidIPC;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Process;
import android.os.RemoteException;
import android.util.Log;

import com.ubt.androidlearning.IMyAidlInterface;
import com.ubt.androidlearning.Model.Messages;

/**
 * Created by Administrator on 2016/10/8.
 */

public class PushManager {
    private static final String TAG = "PushManager.class";
    private IMyAidlInterface iMyAidlInterface;
    private static PushManager mInstance = new PushManager();

    private PushManager(){}


    public static PushManager getInstance(){
        return  mInstance;
    }

    public void initApp(Application application)
    {
        Intent binderIntent = new Intent(application,RemoteService.class);
        application.bindService(binderIntent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    public void connect(){

        Log.d(TAG,"----start remote connect-------------:"+ Process.myPid());
        try {
            iMyAidlInterface.connect();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(Messages msg)
    {
        Log.d(TAG,"----sendMessage-------------:"+ Process.myPid());
        try {
            iMyAidlInterface.sendMessage(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }


    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {

            iMyAidlInterface = IMyAidlInterface.Stub.asInterface(service);
            Log.d(TAG,"--------serviceConnection------------");
            connect();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG,"--------onServiceDisconnected------------");
        }
    };
}
