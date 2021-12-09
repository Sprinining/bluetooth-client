package com.example.uhf_bluetoothclient.entity;

public class MsgAppGetBaseVersion {

    //基带版本
    private String baseVersions;

    //设备相关信息（UDP广播内容）
    private String deviceInfo;


    public String getBaseVersions() {
        return baseVersions;
    }

    public void setBaseVersions(String baseVersions) {
        this.baseVersions = baseVersions;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }
}
