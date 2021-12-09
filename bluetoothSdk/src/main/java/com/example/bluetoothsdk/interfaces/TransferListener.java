package com.example.bluetoothsdk.interfaces;

public interface TransferListener {
    void transferFinish();

    void transferSuccess(byte[] bytes);

    void transferException(String str, Exception e);
}
