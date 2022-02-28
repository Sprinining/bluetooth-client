package com.example.uhf_bluetoothclient.entity

/**
 *
 * @ProjectName:    UHF_BluetoothClient
 * @Description:     类作用描述
 * @Author:         KP
 * @CreateDate:     2022/2/28 15:54
 * @Version:        1.0
 */
data class ProvinceBean(
    val city: List<CityBean>,
    val name: String
)

data class CityBean(
    val name: String
)