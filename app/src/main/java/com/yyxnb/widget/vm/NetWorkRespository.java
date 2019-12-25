package com.yyxnb.widget.vm;


import android.arch.lifecycle.LiveData;

import com.yyxnb.http.network.ApiResponse;
import com.yyxnb.http.network.BaseRepository;
import com.yyxnb.http.network.NetworkBound;
import com.yyxnb.http.network.NetworkBoundResource;
import com.yyxnb.http.network.Resource;
import com.yyxnb.widget.api.NetWorkService;
import com.yyxnb.widget.bean.BaseDatas;
import com.yyxnb.widget.bean.TestData;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class NetWorkRespository extends BaseRepository<NetWorkService> {

    public LiveData<Resource<BaseDatas<List<TestData>>>> getTestList() {
        return new NetworkBoundResource<BaseDatas<List<TestData>>>(){
            @NotNull
            @Override
            protected LiveData<ApiResponse<BaseDatas<List<TestData>>>> createCall() {
                return mApi.getTestList();
            }
        }.asLiveData();
    }

    public LiveData<BaseDatas<List<TestData>>> getTestList2() {
        return new NetworkBound<BaseDatas<List<TestData>>>(){
            @NotNull
            @Override
            protected LiveData<ApiResponse<BaseDatas<List<TestData>>>> createCall() {
                return mApi.getTestList();
            }
        }.asLiveData();
    }

}
