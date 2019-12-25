package com.yyxnb.widget.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.yyxnb.arch.base.mvvm.BaseViewModel;
import com.yyxnb.http.RetrofitManager;
import com.yyxnb.http.network.ApiResponse;
import com.yyxnb.http.network.NetworkBound;
import com.yyxnb.http.network.Resource;
import com.yyxnb.widget.api.NetWorkService;
import com.yyxnb.widget.bean.BaseDatas;
import com.yyxnb.widget.bean.TestData;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetWorkViewModel extends BaseViewModel {

    private NetWorkRespository mRepository = new NetWorkRespository();
    private NetWorkService mApi = RetrofitManager.INSTANCE.createApi(NetWorkService.class);

    private MutableLiveData<Map<String, String>> reqTeam = new MutableLiveData();
    private MutableLiveData<Map<String, String>> reqTeam2 = new MutableLiveData();

    public LiveData<Resource<BaseDatas<List<TestData>>>> getTestList(){
        return Transformations.switchMap(reqTeam, input -> mRepository.getTestList());
    }

    public LiveData<BaseDatas<List<TestData>>> getTestList2(){
        return Transformations.switchMap(reqTeam2, input -> new NetworkBound<BaseDatas<List<TestData>>>(){
            @NotNull
            @Override
            protected LiveData<ApiResponse<BaseDatas<List<TestData>>>> createCall() {
                return mApi.getTestList();
            }
        }.asLiveData());
    }

    public void reqTeam(){
        reqTeam.setValue(new HashMap<>());
    }

    public void reqTeam2(){
        reqTeam2.setValue(new HashMap<>());
    }

}
