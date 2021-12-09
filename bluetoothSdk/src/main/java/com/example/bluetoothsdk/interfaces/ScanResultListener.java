package com.example.bluetoothsdk.interfaces;

import android.bluetooth.BluetoothDevice;

public interface ScanResultListener {
    void onDeviceFound(BluetoothDevice device);

    void scanFinish();

    void scanError(String err);
}
