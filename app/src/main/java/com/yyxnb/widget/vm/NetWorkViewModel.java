package com.yyxnb.widget.vm;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.yyxnb.arch.base.mvvm.BaseViewModel;
import com.yyxnb.widget.bean.TestData;
import com.yyxnb.widget.paging.NetWorkFactory;

public class NetWorkViewModel extends BaseViewModel {

    private LiveData<PagedList<TestData>> convertList;
    private DataSource<Integer, TestData> concertDataSource;

    public NetWorkViewModel() {
        NetWorkFactory concertFactory = new NetWorkFactory();
        concertDataSource = concertFactory.create();

        PagedList.Config config = new PagedList.Config.Builder()
                //配置分页加载的数量
                .setPageSize(20)
                //初始化加载的数量
                .setInitialLoadSizeHint(20 * 2)
                //配置是否启动PlaceHolders
                .setEnablePlaceholders(false)
                .build();

        convertList = new LivePagedListBuilder<>(concertFactory, config).build();
    }

    public void invalidateDataSource() {
        concertDataSource.invalidate();
    }

    public LiveData<PagedList<TestData>> getConvertList() {
        return convertList;
    }
}
