package com.yyxnb.http.upload

import android.arch.lifecycle.LiveData
import com.yyxnb.http.network.ApiResponse
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Url

interface IUploadFileApi {
    /**
     * 上传多个文件
     *
     * @param uploadUrl 地址
     * @param files      文件
     * @return ResponseBody
     */
    @Multipart
    @POST
    fun uploadFiles(@Url uploadUrl: String?,
                    @Part files: List<MultipartBody.Part?>?): LiveData<ApiResponse<ResponseBody>>
}