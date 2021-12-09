package com.example.bluetoothsdk.entity;

public class ScanConfig {
    private long scanTime;

    public ScanConfig(long scanTime) {
        this.scanTime = scanTime;
    }

    public long getScanTime() {
        return scanTime;
    }

    public void setScanTime(long scanTime) {
        this.scanTime = scanTime;
    }
}
