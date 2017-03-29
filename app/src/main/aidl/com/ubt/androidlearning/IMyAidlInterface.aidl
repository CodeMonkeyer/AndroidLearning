// IMyAidlInterface.aidl
package com.ubt.androidlearning;


// Declare any non-default types here with import statements

import com.ubt.androidlearning.Model.Messages;
interface IMyAidlInterface {

     void connect();

     void sendMessage(in Messages msg);

     String replyMessage();

    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
     void basicTypes(int anInt, long aLong, boolean aBoolean, float aFloat,
            double aDouble, String aString);
}
