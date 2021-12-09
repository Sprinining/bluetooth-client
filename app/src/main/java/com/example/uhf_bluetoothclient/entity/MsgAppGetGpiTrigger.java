package com.example.uhf_bluetoothclient.entity;

import java.util.Date;

public class MsgAppGetGpiTrigger {

    //信号输入 端口号，索引从 0 开始
    private int gpiPort;

    //触发开始（0 触发关闭，1 低电平触发，2 高电平触发，3 上升沿触发，4下降沿触发，5 任意边沿触发）
    private int triggerStart;

    //触发绑定命令（Hex，可为空）
    private String hexTriggerCommand;

    //触发绑定命令（Byte[]，可以空）
    private byte[] triggerCommand;

    //触发停止（0 不停止，1 低电平触发，2 高电平触发，3 上升沿触发，4 下降沿触发，5 任意边沿触发，6 延时停止）
    private int triggerOver;

    //延时停止时间（仅当停止条件为“延时停止”生效）
    private Date overDelayTime;

    //触发不停止时 IO 电平变化上传开关（0 不上传，1 上传）
    public int levelUploadSwitch;

    public int getGpiPort() {
        return gpiPort;
    }

    public void setGpiPort(int gpiPort) {
        this.gpiPort = gpiPort;
    }

    public int getTriggerStart() {
        return triggerStart;
    }

    public void setTriggerStart(int triggerStart) {
        this.triggerStart = triggerStart;
    }

    public String getHexTriggerCommand() {
        return hexTriggerCommand;
    }

    public void setHexTriggerCommand(String hexTriggerCommand) {
        this.hexTriggerCommand = hexTriggerCommand;
    }

    public byte[] getTriggerCommand() {
        return triggerCommand;
    }

    public void setTriggerCommand(byte[] triggerCommand) {
        this.triggerCommand = triggerCommand;
    }

    public int getTriggerOver() {
        return triggerOver;
    }

    public void setTriggerOver(int triggerOver) {
        this.triggerOver = triggerOver;
    }

    public Date getOverDelayTime() {
        return overDelayTime;
    }

    public void setOverDelayTime(Date overDelayTime) {
        this.overDelayTime = overDelayTime;
    }

    public int getLevelUploadSwitch() {
        return levelUploadSwitch;
    }

    public void setLevelUploadSwitch(int levelUploadSwitch) {
        this.levelUploadSwitch = levelUploadSwitch;
    }
}
