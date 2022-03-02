package com.example.uhf_bluetoothclient.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.uhf_bluetoothclient.Constants
import com.example.uhf_bluetoothclient.entity.ExportBean

@Database(
    entities = [ExportBean::class],
    version = Constants.VERSION,
    exportSchema = false
)
abstract class MyRoomDataBase : RoomDatabase() {

    abstract fun getExportInfoDao(): ExportInfoDao

}