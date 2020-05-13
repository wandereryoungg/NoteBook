package com.young.note;

import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.young.note.constants.Constants;
import com.young.note.exception.YoungException;

import java.lang.ref.WeakReference;

public class BaseHandler extends Handler {
    private WeakReference<BaseActivity> mReferences;

    public BaseHandler(BaseActivity baseActivity) {
        this.mReferences = new WeakReference<>(baseActivity);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        super.handleMessage(msg);
        if (msg.what == Constants.HANDLER_SUCCESS) {
            if (mReferences.get() instanceof HandlerResultCallBack) {
                HandlerResultCallBack callBack = (HandlerResultCallBack) mReferences.get();
                callBack.handlerSuccess(msg);
            }
        } else if (msg.what == Constants.HANDLER_FAILED) {
            if (mReferences.get() instanceof HandlerResultCallBack) {
                HandlerResultCallBack callBack = (HandlerResultCallBack) mReferences.get();
                callBack.handlerFail((YoungException) msg.obj);
            }
        }
    }

    public interface HandlerResultCallBack {
        void handlerSuccess(Message msg);

        void handlerFail(YoungException e);
    }
}
