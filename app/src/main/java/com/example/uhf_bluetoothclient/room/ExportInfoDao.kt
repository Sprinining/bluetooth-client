package com.example.uhf_bluetoothclient.room

import androidx.room.Dao
import androidx.room.Query
import com.example.uhf_bluetoothclient.Constants
import com.example.uhf_bluetoothclient.entity.ExportBean

@Dao
interface ExportInfoDao : BaseDao<ExportBean> {

    //如果是查询
    @Query("SELECT * FROM ${Constants.NODE_TABLE_NAME} ORDER BY timestamp DESC")
    fun getAll(): List<ExportBean>
}