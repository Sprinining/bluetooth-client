package com.example.bluetoothsdk.interfaces;

import com.example.bluetoothsdk.entity.ConnectState;

public interface ConnectStateListener {
    void onStateChange(ConnectState connectState);
}