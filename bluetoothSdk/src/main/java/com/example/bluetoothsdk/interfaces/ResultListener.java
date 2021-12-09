package com.example.bluetoothsdk.interfaces;

public interface ResultListener {
    void success();

    void failed(Exception e);
}
