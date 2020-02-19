package com.yyxnb.widget.vm;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.yyxnb.arch.base.mvvm.BasePagedViewModel;
import com.yyxnb.widget.bean.TestData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PagingViewModel extends BasePagedViewModel<TestData> {
    @Override
    public DataSource createDataSource() {
        return new PageKeyedDataSource<Integer, TestData>() {
            @Override
            public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, TestData> callback) {
                callback.onResult(fetchItems(0), null, null);
            }

            @Override
            public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, TestData> callback) {
                callback.onResult(Collections.emptyList(), null);
            }

            @Override
            public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, TestData> callback) {
                callback.onResult(Collections.emptyList(), null);
            }
        };
    }

    //invalidate 之后Paging会重新创建一个DataSource 重新调用它的loadInitial方法加载初始化数据
    public void invalidateDataSource() {
        getDataSource().invalidate();
    }

    public LiveData<PagedList<TestData>> getConvertList() {
        return getPageData();
    }

    private List<TestData> fetchItems(int page) {
        List<TestData> list = new ArrayList<>();
        for (int i = page * 10; i < (page + 1) * 10; i++) {
            TestData concert = new TestData();
            concert.setId(i);
            concert.setContent("content = " + i);
            list.add(concert);
        }
        return list;
    }
}
