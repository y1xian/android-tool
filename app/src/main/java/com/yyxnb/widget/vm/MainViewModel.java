package com.yyxnb.widget.vm;

import android.arch.lifecycle.LiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;
import android.arch.paging.PagedList;
import android.support.annotation.NonNull;

import com.yyxnb.http.BasePagedViewModel;
import com.yyxnb.widget.bean.MainBean;
import com.yyxnb.widget.data.DataConfig;

import java.util.Collections;

public class MainViewModel extends BasePagedViewModel<MainBean> {
    @Override
    public DataSource createDataSource() {
        return new PageKeyedDataSource<Integer, MainBean>() {
            @Override
            public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, MainBean> callback) {
                callback.onResult(DataConfig.getMainBeans(), null, null);
            }

            @Override
            public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, MainBean> callback) {
                callback.onResult(Collections.emptyList(), null);
            }

            @Override
            public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, MainBean> callback) {
                callback.onResult(Collections.emptyList(), null);
            }
        };
    }

    //invalidate 之后Paging会重新创建一个DataSource 重新调用它的loadInitial方法加载初始化数据
    public void invalidateDataSource() {
        getDataSource().invalidate();
    }

}
