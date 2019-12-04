package com.yyxnb.widget.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * 这个dao作为所有dao的父类分装了共通化的数据库处理
 * @param <T>
 */
@Dao
public interface BaseDao<T> {

    /**
     * 插入单条数据
     * 指定为REPLACE替换原有数据
     * @param item
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertItem(T item);

    /**
     * 插入list数据
     * 指定为REPLACE替换原有数据
     * @param items
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertItems(List<T> items);

    /**
     * 删除item
     * @param item
     */
    @Delete
    public void deleteItem(T item);

    /**
     * 更新item
     * @param item
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void updateItem(T item);

}