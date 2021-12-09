package com.example.uhf_bluetoothclient.entity;

public class MsgBaseLockEpc  {

    //:天线端口
    private int antennaEnable;

    // 待锁定的标签数据区(0，灭活密码区；1，访问密码区；2，EPC 区；3，TID 区；4， 用户数据区)
    private int area;

    //锁操作类型(0，解锁；1，锁定；2，永久解锁；3，永久锁定)
    private int mode;

    //:选择读取参数（可选）（详见参数说明）
    private ParamEpcFilter filter;

    //:访问密码（可选）
    private String hexPassword;

    public int getAntennaEnable() {
        return antennaEnable;
    }

    public void setAntennaEnable(int antennaEnable) {
        this.antennaEnable = antennaEnable;
    }

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public ParamEpcFilter getFilter() {
        return filter;
    }

    public void setFilter(ParamEpcFilter filter) {
        this.filter = filter;
    }

    public String getHexPassword() {
        return hexPassword;
    }

    public void setHexPassword(String hexPassword) {
        this.hexPassword = hexPassword;
    }
}
