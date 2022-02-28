package com.example.uhf_bluetoothclient.ui

import com.seuic.util.common.SPUtils

/**
 *
 * @ProjectName:    UHF_BluetoothClient
 * @Description:     类作用描述
 * @Author:         KP
 * @CreateDate:     2022/2/28 15:02
 * @Version:        1.0
 */
object Constants {
    //省历史记录
    var lastProvince: String
        get() = SPUtils.getInstance().getString("lastProvince", "")
        set(value) {
            SPUtils.getInstance().put("lastProvince", value)
        }

    //城市历史记录
    var lastCity: String
        get() = SPUtils.getInstance().getString("lastCity", "")
        set(value) {
            SPUtils.getInstance().put("lastCity", value)
        }
}