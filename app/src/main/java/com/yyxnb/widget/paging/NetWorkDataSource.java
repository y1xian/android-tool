package com.yyxnb.widget.paging;

import android.arch.paging.PageKeyedDataSource;
import android.support.annotation.NonNull;

import com.yyxnb.arch.utils.log.LogUtils;
import com.yyxnb.http.RetrofitManager;
import com.yyxnb.widget.api.NetWorkService;
import com.yyxnb.widget.bean.TestData;

import java.util.ArrayList;
import java.util.List;

/**
    PageKeyedDataSource：如果后端API返回数据是分页之后的，可以使用它；例如：官方Demo中GitHub API中的SearchRespositories就可以返回分页数据，
    我们在GitHub API的请求中制定查询关键字和想要的哪一页，同时也可以指明每个页面的项数。

    ItemKeyedDataSource：如果通过键值请求后端数据；例如我们需要获取在某个特定日期起Github的前100项代码提交记录，该日期将成为DataSource的键,
    ItemKeyedDataSource允许自定义如何加载初始页；该场景多用于评论信息等类似请求

    PositionalDataSource：适用于目标数据总数固定，通过特定的位置加载数据，这里Key是Integer类型的位置信息，T即Value。 比如从数据库中的1200条开始加在20条数据。
 */

public class NetWorkDataSource extends PageKeyedDataSource<Integer,TestData> {

    private NetWorkService netWorkService = RetrofitManager.INSTANCE.createApi(NetWorkService.class);

    /*初始加载的数据 也就是我们直接能看见的数据*/
    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, TestData> callback) {

//        netWorkService.getTestList().observe();

        LogUtils.INSTANCE.w("--------loadInitial " + params.requestedLoadSize);
        callback.onResult(fetchItems(0), 0,1);
    }

    /*往上滑加载的数据 每次传递的第二个参数 就是 你加载数据依赖的key*/
    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, TestData> callback) {
        LogUtils.INSTANCE.w("----------loadBefore");
//        callback.onResult(fetchItems(params.key),params.key - 1);
    }

    /*往下滑加载的数据 每次传递的第二个参数 就是 你加载数据依赖的key*/
    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, TestData> callback) {
        LogUtils.INSTANCE.w("--------loadAfter");
        callback.onResult(fetchItems(params.key),params.key + 1);
    }


    private List<TestData> fetchItems(int page) {
        List<TestData> list = new ArrayList<>();
        for (int i = page * 20; i < (page + 1) * 20; i++) {
            TestData concert = new TestData();
            concert.setId(i);
            concert.setContent("content = " + i);
            list.add(concert);
        }
        return list;
    }
}
