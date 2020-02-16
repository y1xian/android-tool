package com.yyxnb.widget.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import com.yyxnb.http.cache.BaseDao;
import com.yyxnb.widget.bean.TestData;

import java.util.List;

@Dao
public interface TestDao extends BaseDao<TestData> {

    @Query("SELECT * FROM test")
    public List<TestData> getTests();//获取集合

    @Query("SELECT * FROM test WHERE id=:id")
    public TestData getTestById(int id);//通过id获取
}