package com.example.bluetoothsdk.interfaces;

import com.example.bluetoothsdk.ConnectedThread;

public interface ConnectResultListener {
    void connectSuccess(ConnectedThread connectedThread);

    void connectFail(Exception e);

    void disconnect();
}
