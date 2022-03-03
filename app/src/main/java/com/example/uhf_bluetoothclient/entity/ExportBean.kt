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
    @ColumnInfo var address: String = "",
    @ColumnInfo var antennaNumber: String = "",
    @ColumnInfo var blueToothMac: String = "",
    @ColumnInfo var branches: String = "",
    @ColumnInfo var city: String = "",
    @ColumnInfo var function: String = "",
    @ColumnInfo var imei: String = "",
    @ColumnInfo var ipv4: String = "",
    @ColumnInfo var ipv6: String = "",
    @ColumnInfo var location: String = "",
    @ColumnInfo var netServerSide: String = "",
    @ColumnInfo var powerSupplyMode: String = "",
    @ColumnInfo var province: String = "",
    @ColumnInfo var remark: String = "",
    @ColumnInfo var rj54mac: String = "",
    @PrimaryKey @ColumnInfo(name = "SN") var snNum: String = "",
    @Ignore var serialNumber: String = snNum,
    @ColumnInfo var device: String = "",
    @ColumnInfo var timestamp: Long = System.currentTimeMillis()
)