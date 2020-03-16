package com.yyxnb.http.download

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.yyxnb.http.RetrofitManager
import com.yyxnb.http.network.ApiErrorResponse
import com.yyxnb.http.network.ApiSuccessResponse
import com.yyxnb.utils.AppConfig
import com.yyxnb.utils.log.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.Serializable

/**
 * 为下载单独建一个retrofit 无断点
 */
object DownloadRetrofit : Serializable {

    val mDestFileDir = AppConfig.context.getExternalFilesDir(null)!!.toString() + File.separator
//        val destFileDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).absolutePath +"/downloadFile" + File.separator

    /**
     * @param fileUrl   文件下载地址
     * @param destFileName 文件名（包括文件后缀）
     * @param destFileDir  存放路径
     */
    @JvmOverloads
    fun downloadFile(fileUrl: String, destFileName: String, destFileDir: String = mDestFileDir): LiveData<Progress> {

        val result = MediatorLiveData<Progress>()

        result.addSource(RetrofitManager
                .createApi(IDownloadApi::class.java)
                .downloadFile(fileUrl)) { response ->
            when (response) {
                is ApiSuccessResponse -> {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO){
                            DownloadManager.saveFile(response.body,destFileDir, destFileName, object : IProgressListener {
                                override fun onResponseProgress(bytesRead: Long, contentLength: Long, progress: Int, done: Boolean, filePath: String) {
                                    result.postValue(Progress(bytesRead, contentLength, progress, done, filePath))
                                }
                            })
                        }
                    }
                }
                is ApiErrorResponse -> {
                    LogUtils.e(response.errorMessage)
                }
            }
        }
        return result
    }

    data class Progress(var bytesRead: Long, var contentLength: Long, var progress: Int, var done: Boolean, var filePath: String) : Serializable
}
