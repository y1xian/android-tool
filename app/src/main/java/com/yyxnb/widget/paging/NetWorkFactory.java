package com.yyxnb.widget.paging;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;

import com.yyxnb.widget.bean.TestData;

public class NetWorkFactory extends DataSource.Factory<Integer, TestData> {

    private MutableLiveData<NetWorkDataSource> mSourceLiveData =
            new MutableLiveData<>();

    @Override
    public DataSource<Integer, TestData> create() {
        NetWorkDataSource concertDataSource = new NetWorkDataSource();
        mSourceLiveData.postValue(concertDataSource);
        return new NetWorkDataSource();
    }
}