package com.ubt.androidlearning.AndroidIPC;

import android.os.Process;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;

import com.ubt.androidlearning.IMyAidlInterface;


/**
 * Created by Administrator on 2016/10/8.
 */

public class ConnectImp extends IMyAidlInterface.Stub {

    private String content;
    @Override
    public void connect() throws RemoteException {
        Log.d("PushManager","-----------connect()------------:"+ Process.myPid());
    }

    @Override
    public void sendMessage(com.ubt.androidlearning.Model.Messages msg) throws RemoteException {

        content = msg.getContent();
        Log.d("PushManager","-----------sendMessage()------------:"+Process.myPid()+", id:"+ msg.getId()+", msg:"+msg.getContent());
    }

    @Override
    public String replyMessage() throws RemoteException {
        if(TextUtils.isEmpty(content))
        return  "---------nothing to receive--------------";
        else
            return "--------------receive:"+content;
    }


    @Override
    public void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat, double aDouble, String aString) throws RemoteException {

    }
}
