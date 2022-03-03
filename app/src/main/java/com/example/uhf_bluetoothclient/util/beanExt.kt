package com.example.uhf_bluetoothclient.util

import com.chad.library.adapter.base.entity.node.BaseNode
import com.example.uhf_bluetoothclient.entity.ExportBean
import com.example.uhf_bluetoothclient.entity.RootNodeBean
import com.example.uhf_bluetoothclient.entity.SecondNodeBean

/**
 *
 * @ProjectName:    seuic_industrial_secondary_node
 * @Description:     类作用描述
 * @Author:         KP
 * @CreateDate:     2021/12/21 13:25
 * @Version:        1.0
 */

fun ExportBean.toDatasetRootNodeBean(): RootNodeBean {
    val rootNodeBean = RootNodeBean(snNum)
    rootNodeBean.childNode = mutableListOf(
        SecondNodeBean("省", province),
        SecondNodeBean("城市", city),
        SecondNodeBean("网点", branches),
        SecondNodeBean("安装位置", location),
        SecondNodeBean("详细地址", address),
        SecondNodeBean("设备类型", device),
        SecondNodeBean("IPV4", ipv4),
        SecondNodeBean("IPV4网端", netServerSide),
        SecondNodeBean("IPV6", ipv6),
        SecondNodeBean("IMEI", imei),
        SecondNodeBean("RJ45MAC", rj54mac),
        SecondNodeBean("蓝牙MAC", blueToothMac),
        SecondNodeBean("天线", antennaNumber),
        SecondNodeBean("供电", powerSupplyMode),
        SecondNodeBean("功能", function),
        SecondNodeBean("备注", remark),
    )
    return rootNodeBean
}