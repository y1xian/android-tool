package com.yyxnb.http.upload

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MediatorLiveData
import com.yyxnb.http.RetrofitManager
import com.yyxnb.http.network.*
import com.yyxnb.utils.log.LogUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import java.io.File
import java.io.Serializable
import kotlin.collections.ArrayList

/**
 * 为上传单独建一个retrofit
 */
object UploadRetrofit : Serializable {

    /**
     * 上传一张图片
     *
     * @param uploadUrl 上传图片的服务器url
     * @param fileName  后台协定的接受图片的name（没特殊要求就可以随便写）
     * @param filePath  图片路径
     * @return LiveData
     */
    fun uploadImg(uploadUrl: String, fileName: String, filePath: String): LiveData<ResponseBody> {
        val filePaths = ArrayList<String>()
        filePaths.add(filePath)
        return uploadImgsWithParams(uploadUrl, fileName, null, filePaths)

    }

    /**
     * 上传多张图片 不带参数
     *
     * @param uploadUrl 上传图片的服务器url
     * @param fileName  后台协定的接受图片的name（没特殊要求就可以随便写）
     * @param filePaths 图片路径
     * @return LiveData
     */
    fun uploadImgs(uploadUrl: String, fileName: String, filePaths: List<String>): LiveData<ResponseBody> {
        return uploadImgsWithParams(uploadUrl, fileName, null, filePaths)
    }

    /**
     * 图片和参数同时上传的请求
     *
     * @param uploadUrl 上传图片的服务器url
     * @param fileName  后台协定的接受图片的name（没特殊要求就可以随便写）
     * @param map       普通参数
     * @param filePaths 图片路径
     * @return Observable
     */
    fun uploadImgsWithParams(uploadUrl: String, fileName: String, map: Map<String, Any>?, filePaths: List<String>): LiveData<ResponseBody> {

        val builder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)

        if (null != map) {
            for (key in map.keys) {
                builder.addFormDataPart(key, (map[key] as String?)!!)
            }
        }

        for (i in filePaths.indices) {
            val file = File(filePaths[i])
            val imageBody = RequestBody.create("multipart/form-data".toMediaTypeOrNull(), file)
            //"medias"+i 后台接收图片流的参数名
            builder.addFormDataPart(fileName, file.name, imageBody)
        }

        val parts = builder.build().parts

        val result = MediatorLiveData<ResponseBody>()

        result.addSource(RetrofitManager
                .createApi(IUploadFileApi::class.java)
                .uploadFiles(uploadUrl, parts)) { response ->
            when (response) {
                is ApiSuccessResponse -> {
                    GlobalScope.launch {
                        withContext(Dispatchers.IO) {
                            result.postValue(response.body)
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

}