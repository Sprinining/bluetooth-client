package com.example.bluetoothsdk.entity;

public enum ConnectState {
    STATE_TURNING_ON("蓝牙正在打开"),
    STATE_ON("蓝牙已经打开"),
    STATE_TURNING_OFF("蓝牙正在关闭"),
    STATE_OFF("蓝牙已经关闭"),
    ACTION_ACL_CONNECTED("蓝牙设备已连接"),
    ACTION_ACL_DISCONNECT_REQUESTED("蓝牙设备即将断开"),
    ACTION_ACL_DISCONNECTED("蓝牙设备已断开"),
    ACTION_PAIRING_REQUEST("蓝牙配对请求"),
    ACTION_DISCOVERY_STARTED("蓝牙扫描开始"),
    ACTION_DISCOVERY_FINISHED("蓝牙扫描结束"),
    BOND_BONDING("配对中"),
    BOND_BONDED("配对成功"),
    BOND_NONE("取消配对");

    private final String detail;

    ConnectState(String detail) {
        this.detail = detail;
    }

    @Override
    public String toString() {
        return detail;
    }
}
