package com.example.bluetoothsdk.entity;

public class BluetoothConfig {
    private String uuid;

    public BluetoothConfig() {
    }

    public BluetoothConfig(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
}
