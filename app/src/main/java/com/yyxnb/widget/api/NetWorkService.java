package com.yyxnb.widget.api;

import android.arch.lifecycle.LiveData;

import com.yyxnb.http.network.ApiResponse;
import com.yyxnb.widget.bean.BaseDatas;
import com.yyxnb.widget.bean.TestData;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Headers;

import static com.yyxnb.widget.config.BaseAPI.HEAD_MOCKY;


public interface NetWorkService {

    @Headers(HEAD_MOCKY)
    @GET("v2/5dcd06bb2e00007c007299fd")
    LiveData<BaseDatas<TestData>> getTest();

    @Headers(HEAD_MOCKY)
    @GET("v2/5dd6271933000041d5f38453")
    LiveData<ApiResponse<BaseDatas<List<TestData>>>> getTestList();

    @Headers(HEAD_MOCKY)
    @GET("v2/5dd6271933000041d5f38453")
    LiveData<BaseDatas<List<TestData>>> getTestList2();

}
