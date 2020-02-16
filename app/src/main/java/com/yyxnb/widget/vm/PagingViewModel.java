package com.yyxnb.widget.vm;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.LivePagedListBuilder;
import android.arch.paging.PagedList;

import com.yyxnb.arch.base.mvvm.BaseViewModel;
import com.yyxnb.widget.bean.TestData;
import com.yyxnb.widget.paging.NetWorkFactory;

public class PagingViewModel extends BaseViewModel {

    private LiveData<PagedList<TestData>> convertList;
    private DataSource<Integer, TestData> concertDataSource;

    public PagingViewModel() {
        NetWorkFactory concertFactory = new NetWorkFactory();
        concertDataSource = concertFactory.create();

        int pageSize = 10;
        PagedList.Config config = new PagedList.Config.Builder()
                //配置分页加载的数量
                .setPageSize(pageSize)
                //初始化加载的数量
                .setInitialLoadSizeHint(pageSize + 2)
                //距离底部还有多少条数据时开始预加载
//                .setPrefetchDistance(pageSize / 4)
                //配置是否启动PlaceHolders
//                .setEnablePlaceholders(false)
                .build();

        convertList = new LivePagedListBuilder<>(concertFactory, config).build();
    }

    //invalidate 之后Paging会重新创建一个DataSource 重新调用它的loadInitial方法加载初始化数据
    public void invalidateDataSource() {
        concertDataSource.invalidate();
    }

    public LiveData<PagedList<TestData>> getConvertList() {
        return convertList;
    }
}
