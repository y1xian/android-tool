package com.yyxnb.http.cache;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

@Dao
public interface CacheDao extends BaseDao<Cache> {

    //如果是一对多,这里可以写List<Cache>
    @Query("select * from cache where `key`=:key")
    public Cache getCache(String key);


    //根据key删除
    @Query("delete from cache where `key`=:key")
    public int deleteCache(String key);

    // 行数
    @Query("select count(1) from cache")
    public int size();

}
