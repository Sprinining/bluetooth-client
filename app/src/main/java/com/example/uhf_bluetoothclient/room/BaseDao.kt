package com.seuic.industrial_node.room

import androidx.room.*


@Dao
interface BaseDao<in T> {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertItem(item: T) //插入单条数据

    @Insert
    fun insertItems(items: List<T>) //插入list数据


    @Delete
    fun deleteItem(item: T) //删除item


    @Delete
    fun deleteItem(items: List<T>) //删除list


    @Update
    fun updateItem(item: T) //更新item

}