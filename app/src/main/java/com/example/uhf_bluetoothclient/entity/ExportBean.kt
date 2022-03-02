package com.example.uhf_bluetoothclient.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.example.uhf_bluetoothclient.Constants
import java.sql.Timestamp

/**
 *
 * @ProjectName:    UHF_BluetoothClient
 * @Description:     类作用描述
 * @Author:         KP
 * @CreateDate:     2022/3/2 13:46
 * @Version:        1.0
 */
@Entity(tableName = Constants.NODE_TABLE_NAME)
data class ExportBean(
    @ColumnInfo val address: String,
    @ColumnInfo val antennaNumber: String,
    @ColumnInfo val blueToothMac: String,
    @ColumnInfo val branches: String,
    @ColumnInfo val city: String,
    @ColumnInfo val function: String,
    @ColumnInfo val imei: String,
    @ColumnInfo val ipv4: String,
    @ColumnInfo val ipv6: String,
    @ColumnInfo val location: String,
    @ColumnInfo val netServerSide: String,
    @ColumnInfo val powerSupplyMode: String,
    @ColumnInfo val province: String,
    @ColumnInfo val remark: String,
    @ColumnInfo val rj54mac: String,
    @PrimaryKey @ColumnInfo(name = "SN") val snNum: String,
    @Ignore val serialNumber: String = snNum,
    @Ignore val device: String = "固定设备",
    @ColumnInfo val timestamp: Long = System.currentTimeMillis()
)