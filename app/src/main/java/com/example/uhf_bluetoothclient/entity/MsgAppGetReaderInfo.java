package com.example.uhf_bluetoothclient.entity;

import java.util.Date;

public class MsgAppGetReaderInfo {

    //: 读写器序列号
    private String readerSerialNumber;

    //：上电时间
    private Date powerOnTime;

    //：基带编译时间
    private Date baseCompileTime;

    //：应用软件版本（如：“0.1.0.0”）
    private String appVersions;

    //：应用编译时间
    private Date appCompileTime;

    //：操作系统版本
    private String systemVersions;

    public String getReaderSerialNumber() {
        return readerSerialNumber;
    }

    public void setReaderSerialNumber(String readerSerialNumber) {
        this.readerSerialNumber = readerSerialNumber;
    }

    public Date getPowerOnTime() {
        return powerOnTime;
    }

    public void setPowerOnTime(Date powerOnTime) {
        this.powerOnTime = powerOnTime;
    }

    public Date getBaseCompileTime() {
        return baseCompileTime;
    }

    public void setBaseCompileTime(Date baseCompileTime) {
        this.baseCompileTime = baseCompileTime;
    }

    public String getAppVersions() {
        return appVersions;
    }

    public void setAppVersions(String appVersions) {
        this.appVersions = appVersions;
    }

    public Date getAppCompileTime() {
        return appCompileTime;
    }

    public void setAppCompileTime(Date appCompileTime) {
        this.appCompileTime = appCompileTime;
    }

    public String getSystemVersions() {
        return systemVersions;
    }

    public void setSystemVersions(String systemVersions) {
        this.systemVersions = systemVersions;
    }
}
