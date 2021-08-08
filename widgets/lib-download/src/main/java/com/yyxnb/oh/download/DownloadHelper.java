package com.yyxnb.oh.download;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.yyxnb.oh.core.UITask;
import com.yyxnb.oh.log.LogUtils;
import com.yyxnb.oh.okhttp.AbsOkHttp;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class DownloadHelper extends AbsOkHttp {

    private static volatile DownloadHelper mInstance = null;

    private DownloadHelper() {
    }

    public static DownloadHelper getInstance() {
        if (null == mInstance) {
            synchronized (DownloadHelper.class) {
                if (null == mInstance) {
                    mInstance = new DownloadHelper();
                }
            }
        }
        return mInstance;
    }

    @Override
    protected String baseUrl() {
        return "";
    }

    /**
     * @param url        下载链接
     * @param startIndex 下载起始位置
     * @param endIndex   结束为止
     * @param callback   回调
     * @throws IOException
     */
    public void downloadFileByRange(String url, long startIndex, long endIndex, Callback callback) throws IOException {
        // 创建一个Request
        // 设置分段下载的头信息。 Range:做分段数据请求,断点续传指示下载的区间。格式: Range bytes=0-1024或者bytes:0-1024
        Request request = new Request.Builder().header("RANGE", "bytes=" + startIndex + "-" + endIndex)
                .url(url)
                .build();
        doAsync(request, callback);
    }

    public void getContentLength(String url, Callback callback) throws IOException {
        // 创建一个Request
        Request request = new Request.Builder()
                .url(url)
                .build();
        doAsync(request, callback);
    }

    /**
     * 异步请求
     */
    private void doAsync(Request request, Callback callback) throws IOException {
        //创建请求会话
        Call call = okHttpClient().newCall(request);
        //异步执行会话请求
        call.enqueue(callback);
    }

    /**
     * 同步请求
     */
    private Response doSync(Request request) throws IOException {

        //创建请求会话
        Call call = okHttpClient().newCall(request);
        //同步执行会话请求
        return call.execute();
    }


    public void downloadByUrl(LifecycleOwner owner, String url, File targetFile, HttpResponseCallback<File> callback) {
        MediatorLiveData<Object> mediatorLiveData = new MediatorLiveData<>();
        MutableLiveData<Void> onStarted = new MediatorLiveData<>();
        MutableLiveData<File> onSuccess = new MediatorLiveData<>();
        MutableLiveData<Exception> onError = new MediatorLiveData<>();
        MutableLiveData<Long[]> onProgress = new MediatorLiveData<>();
        mediatorLiveData.addSource(onStarted, o -> callback.onStarted());
        mediatorLiveData.addSource(onSuccess, callback::onSuccess);
        mediatorLiveData.addSource(onError, callback::onError);
        mediatorLiveData.addSource(onProgress, o -> {
            callback.onProgress(o[0], o[1]);
        });
        mediatorLiveData.observe(owner, o -> {
            LogUtils.i("executeForLiveData:" + o);
        });
        UITask.run(() -> {
            Response downloadFileResponse = null;
            InputStream inputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                onStarted.postValue(null);
                long total = 1L;
                long progress = 0L;
                onProgress.postValue(new Long[]{total, progress});
                Request downloadFileRequest = new Request.Builder().get().url(url).build();
                downloadFileResponse = okHttpClient().newCall(downloadFileRequest).execute();
                if (!downloadFileResponse.isSuccessful()) {
                    throw new RuntimeException("下载文件失败");
                }
                inputStream = downloadFileResponse.body().byteStream();
                if (!FileUtil.exist(targetFile.getParentFile())) {
                    FileUtil.mkParentDirs(targetFile);
                }
                fileOutputStream = new FileOutputStream(targetFile);
                int length;
                byte[] bytes = new byte[1024 * 10];
                while ((length = inputStream.read(bytes)) != -1) {
                    // 写入文件
                    fileOutputStream.write(bytes, 0, length);
                    progress += length;
                    onProgress.postValue(new Long[]{total, progress});
                }
                fileOutputStream.flush();
                callback.onExecute(targetFile);
//                FileObserverHelper.refreshFileObserverService();
                onSuccess.postValue(targetFile);
            } catch (Exception e) {
                LogUtils.e("下载文件失败", e);
                onError.postValue(e);
            } finally {
                IoUtil.close(inputStream);
                IoUtil.close(fileOutputStream);
                IoUtil.close(downloadFileResponse);
            }
        });
    }


}
