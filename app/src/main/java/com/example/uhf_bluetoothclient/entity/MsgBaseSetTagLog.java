package com.example.uhf_bluetoothclient.entity;

public class MsgBaseSetTagLog {

    //重复标签过滤时间（可选）（表示在一个读卡指令执行周期内，在指定 的重复过滤时间内相同的标签内容只上传一次，0~65535，时间单位：10ms）。
    private int repeatedTime;

    // RSSI 阈值（可选）（标签 RSSI 值低于阈值时标签数据将不上传并丢弃）
    private int rssiTv;

    public int getRepeatedTime() {
        return repeatedTime;
    }

    public void setRepeatedTime(int repeatedTime) {
        this.repeatedTime = repeatedTime;
    }

    public int getRssiTv() {
        return rssiTv;
    }

    public void setRssiTv(int rssiTv) {
        this.rssiTv = rssiTv;
    }
}
