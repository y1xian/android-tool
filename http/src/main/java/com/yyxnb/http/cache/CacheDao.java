package com.yyxnb.http.cache;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

@Dao
public interface CacheDao extends BaseDao<Cache> {

    //如果是一对多,这里可以写List<Cache>
    @Query("select * from cache where `key`=:key")
    public Cache getCache(String key);

}
