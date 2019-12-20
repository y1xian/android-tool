package com.yyxnb.widget.api

import android.arch.lifecycle.LiveData
import com.yyxnb.http.network.ApiResponse
import com.yyxnb.widget.bean.BaseDatas
import com.yyxnb.widget.bean.TestData
import com.yyxnb.widget.config.BaseAPI
import retrofit2.http.GET
import retrofit2.http.Headers

interface NetWorkService {

    @get:GET("v2/5dcd06bb2e00007c007299fd")
    @get:Headers(BaseAPI.HEAD_MOCKY)
    val test: LiveData<BaseDatas<TestData>>

    @get:GET("v2/5dd6271933000041d5f38453")
    @get:Headers(BaseAPI.HEAD_MOCKY)
    val testList: LiveData<ApiResponse<BaseDatas<List<TestData>>>>

    @get:GET("v2/5dd6271933000041d5f38453")
    @get:Headers(BaseAPI.HEAD_MOCKY)
    val testList2: LiveData<BaseDatas<List<TestData>>>

}