package com.example.uhf_bluetoothclient.entity;

public class MsgAppSetBeep {

    // 0:停止   1：响
    private int beepStatus;

    // 0:响一次   1：常响
    private int beepMode;


    public int getBeepStatus() {
        return beepStatus;
    }

    public void setBeepStatus(int beepStatus) {
        this.beepStatus = beepStatus;
    }

    public int getBeepMode() {
        return beepMode;
    }

    public void setBeepMode(int beepMode) {
        this.beepMode = beepMode;
    }
}
