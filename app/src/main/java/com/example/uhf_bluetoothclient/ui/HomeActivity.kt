package com.example.uhf_bluetoothclient.ui

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.uhf_bluetoothclient.R
import com.seuic.util.common.ActivityUtils
import com.seuic.util.common.ext.singleClick

/**
 *
 * @ProjectName:    UHF_BluetoothClient
 * @Description:     类作用描述
 * @Author:         KP
 * @CreateDate:     2022/2/25 10:32
 * @Version:        1.0
 */
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        findViewById<AppCompatButton>(R.id.device_test_btn).singleClick {
            ActivityUtils.startActivity(ScanDeviceActivity::class.java)
        }
        findViewById<AppCompatButton>(R.id.data_export_btn).singleClick {
            ActivityUtils.startActivity(DataExportActivity::class.java)
        }
    }
}