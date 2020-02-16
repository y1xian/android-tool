package com.yyxnb.http.cache;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * 这个dao作为所有dao的父类分装了共通化的数据库处理
 *
 * @param <T>
 */
@Dao
public interface BaseDao<T> {

    /*
     * 查询
     * 注意，冒号后面必须紧跟参数名，中间不能有空格。大于小于号和冒号中间是有空格的。
     * select *from table where【表中列名】 =:【参数名】------>等于
     * where 【表中列名】 < :【参数名】 小于
     * where 【表中列名】 between :【参数名1】 and :【参数2】------->这个区间
     * where 【表中列名】like :参数名----->模糊查询
     * where 【表中列名】 in (:【参数名集合】)---->查询符合集合内指定字段值的记录
     */

    /**
     * 插入单条数据
     * 指定为REPLACE替换原有数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertItem(T item);

    /**
     * 插入list数据
     * 指定为REPLACE替换原有数据
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertItems(List<T> items);

    /**
     * 删除item
     */
    @Delete
    public void deleteItem(T item);

    /**
     * 更新item
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void updateItem(T item);

}