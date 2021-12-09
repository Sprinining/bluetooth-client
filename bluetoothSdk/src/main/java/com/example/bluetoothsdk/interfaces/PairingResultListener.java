package com.example.bluetoothsdk.interfaces;

import android.bluetooth.BluetoothDevice;

public interface PairingResultListener {
    void pairing(BluetoothDevice device);

    void paired(BluetoothDevice device);

    void pairFailed(BluetoothDevice device);
}
