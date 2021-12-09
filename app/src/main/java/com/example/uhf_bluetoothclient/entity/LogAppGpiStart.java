package com.example.uhf_bluetoothclient.entity;

import java.util.Date;

public class LogAppGpiStart {

    //触发 信号输入 端口号
    private String gpiPort;

    // 触发 信号输入 端口号
    private String gpiPortLevel;

    //当前系统时间
    private Date systemTime;


    public String getGpiPort() {
        return gpiPort;
    }

    public void setGpiPort(String gpiPort) {
        this.gpiPort = gpiPort;
    }

    public String getGpiPortLevel() {
        return gpiPortLevel;
    }

    public void setGpiPortLevel(String gpiPortLevel) {
        this.gpiPortLevel = gpiPortLevel;
    }

    public Date getSystemTime() {
        return systemTime;
    }

    public void setSystemTime(Date systemTime) {
        this.systemTime = systemTime;
    }
}
