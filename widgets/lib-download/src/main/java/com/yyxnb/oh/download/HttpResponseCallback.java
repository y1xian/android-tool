package com.yyxnb.oh.download;

public interface HttpResponseCallback<T> extends ProgressCallback {

    void onStarted();

    void onError(Throwable ex);

    void onSuccess(T result);

    void onExecute(T result);

}
