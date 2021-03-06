package com.quzubuluo.quzulock.net;

import io.reactivex.Observer;
import com.quzubuluo.quzulock.utils.LogUtil;


public abstract class BaseObserver<T> implements Observer<T> {

    public BaseObserver() {
    }

    @Override
    public void onError(Throwable e) {
        LogUtil.d(e.getLocalizedMessage());
    }

    @Override
    public void onComplete() {
    }

}
