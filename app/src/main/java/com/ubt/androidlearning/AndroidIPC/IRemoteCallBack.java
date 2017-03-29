package com.ubt.androidlearning.AndroidIPC;

import com.ubt.androidlearning.Model.Messages;

/**
 * Created by Administrator on 2016/10/8.
 */

public interface IRemoteCallBack {

    void onReply(Messages msg);
}
