package com.example.uhf_bluetoothclient.ui

import android.Manifest
import android.app.ActivityManager
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
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

    private var mIsExit = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        findViewById<AppCompatButton>(R.id.device_test_btn).singleClick {
            ActivityUtils.startActivity(ScanDeviceActivity::class.java)
        }
        findViewById<AppCompatButton>(R.id.data_export_btn).singleClick {
            ActivityUtils.startActivity(DataExportActivity::class.java)
        }

        var requestBluetooth =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK) {
                    //granted
                } else {
                    //deny
                }
            }

        val requestMultiplePermissions =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                permissions.entries.forEach {
                    Log.d("test006", "${it.key} = ${it.value}")
                }
            }
        //check android12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestMultiplePermissions.launch(
                arrayOf(
                    Manifest.permission.BLUETOOTH_SCAN,
                    Manifest.permission.BLUETOOTH_CONNECT
                )
            )
        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
        }
    }

    // 退出app
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                exitAPP()
            } else {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show()
                mIsExit = true
                Handler().postDelayed({ mIsExit = false }, 2000)
            }
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun exitAPP() {
        // 关闭app
        val activityManager = applicationContext.getSystemService(ACTIVITY_SERVICE) as ActivityManager
        val appTaskList = activityManager.appTasks
        for (appTask in appTaskList) {
            appTask.finishAndRemoveTask()
        }
    }
}