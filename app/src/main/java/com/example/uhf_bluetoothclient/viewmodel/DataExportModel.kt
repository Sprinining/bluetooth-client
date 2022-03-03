package com.example.uhf_bluetoothclient.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.seuic.util.common.SPUtils

/**
 *
 * @ProjectName:    UHF_BluetoothClient
 * @Description:     类作用描述
 * @Author:         KP
 * @CreateDate:     2022/2/28 16:08
 * @Version:        1.0
 */
class DataExportModel : ViewModel() {
    //IP历史记录
    val lastIP: MutableLiveData<String> =
        object : MutableLiveData<String>(SPUtils.getInstance().getString("lastIP", "")) {
            override fun postValue(value: String?) {
                super.postValue(value)
                SPUtils.getInstance().put("lastIP", value)
            }
        }

    //PORT历史记录
    val lastPort: MutableLiveData<String> =
        object : MutableLiveData<String>(SPUtils.getInstance().getString("lastPort", "")) {
            override fun postValue(value: String?) {
                super.postValue(value)
                SPUtils.getInstance().put("lastPort", value)
            }
        }

    //省历史记录
    val lastProvince: MutableLiveData<String> =
        object : MutableLiveData<String>(SPUtils.getInstance().getString("lastProvince", "")) {
            override fun postValue(value: String?) {
                super.postValue(value)
                SPUtils.getInstance().put("lastProvince", value)
            }
        }

    //城市历史记录
    val lastCity: MutableLiveData<String> =
        object : MutableLiveData<String>(SPUtils.getInstance().getString("lastCity", "")) {
            override fun postValue(value: String?) {
                super.postValue(value)
                SPUtils.getInstance().put("lastCity", value)
            }
        }

    // sn
    val sn = MutableLiveData("")

    // ipv4
    val ipv4 = MutableLiveData("")

    // 网端
    val network_side = MutableLiveData("")

    // ipv6
    val ipv6 = MutableLiveData("")

    // IMEI
    val imei = MutableLiveData("")

    // 网络mac
    val mac_net = MutableLiveData("")

    // 蓝牙mac
    val mac_ble = MutableLiveData("")
}