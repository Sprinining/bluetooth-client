package com.example.uhf_bluetoothclient.entity;

public class MsgBaseInventoryEpc {

    //:天线端口(使用天线常量,详见快速上手)
    private int antennaEnable;

    // 连续/单次读取 (0: 单次读取模式，读写器尽在各个使能的天线上进行
    //            一轮读卡操作便结束读卡操作并自动进入空闲状态; 1: 连续读取模式，读写器一直进行
    //            读卡操作直到读写器收到停止指令后结束读卡)
    private int inventoryMode;

    //: 选择读取参数（可选）（详见参数说明）
    private ParamEpcFilter filter;

    //: TID 读取参数（可选）（详见参数说明）
    private ParamEpcReadTid readTid;

    //用户数据区读取参数（可选）（详见参数说明）
    private ParamEpcReadUserdata readUserdata;

    // 保留区读取参数（可选）（详见参数说明）
    private ReadReserved readReserved;

    // :访问密码（可选）
    private String hexPassword;

    public int getAntennaEnable() {
        return antennaEnable;
    }

    public void setAntennaEnable(int antennaEnable) {
        this.antennaEnable = antennaEnable;
    }

    public int getInventoryMode() {
        return inventoryMode;
    }

    public void setInventoryMode(int inventoryMode) {
        this.inventoryMode = inventoryMode;
    }

    public ParamEpcFilter getFilter() {
        return filter;
    }

    public void setFilter(ParamEpcFilter filter) {
        this.filter = filter;
    }

    public ParamEpcReadTid getReadTid() {
        return readTid;
    }

    public void setReadTid(ParamEpcReadTid readTid) {
        this.readTid = readTid;
    }

    public ParamEpcReadUserdata getReadUserdata() {
        return readUserdata;
    }

    public void setReadUserdata(ParamEpcReadUserdata readUserdata) {
        this.readUserdata = readUserdata;
    }

    public ReadReserved getReadReserved() {
        return readReserved;
    }

    public void setReadReserved(ReadReserved readReserved) {
        this.readReserved = readReserved;
    }

    public String getHexPassword() {
        return hexPassword;
    }

    public void setHexPassword(String hexPassword) {
        this.hexPassword = hexPassword;
    }
}
