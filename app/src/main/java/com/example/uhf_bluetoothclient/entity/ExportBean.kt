package com.example.uhf_bluetoothclient.entity

/**
 *
 * @ProjectName:    UHF_BluetoothClient
 * @Description:     类作用描述
 * @Author:         KP
 * @CreateDate:     2022/3/2 13:46
 * @Version:        1.0
 */
data class ExportBean(
    val address: String,
    val antennaNumber: String,
    val blueToothMac: String,
    val branches: String,
    val city: String,
    val function: String,
    val imei: String,
    val ipv4: String,
    val ipv6: String,
    val location: String,
    val netServerSide: String,
    val powerSupplyMode: String,
    val province: String,
    val remark: String,
    val rj54mac: String,
    val snNum: String,
    val serialNumber: String = snNum,
    val device: String = "固定设备",
)