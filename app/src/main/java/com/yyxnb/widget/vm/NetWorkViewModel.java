package com.yyxnb.widget.vm;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;

import com.yyxnb.arch.base.mvvm.BaseViewModel;
import com.yyxnb.http.network.Resource;
import com.yyxnb.widget.bean.BaseDatas;
import com.yyxnb.widget.bean.TestData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetWorkViewModel extends BaseViewModel {

    private NetWorkRespository mRepository = new NetWorkRespository();

    private MutableLiveData<Map<String, String>> reqTeam = new MutableLiveData();

    public LiveData<Resource<BaseDatas<List<TestData>>>> getTestList(){
        return Transformations.switchMap(reqTeam, input -> mRepository.getTestList());
    }

    public void reqTeam(){
        reqTeam.setValue(new HashMap<>());
    }

}
