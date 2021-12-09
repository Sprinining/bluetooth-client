package com.example.uhf_bluetoothclient.entity;

public class MsgBaseWriteEpc {

    // : 天线端口
    private int antennaEnable;

    //：待写入的标签数据区(0，保留区；1，EPC 区；2，TID 区；3，用户数据区)
    private int area;

    //：待写入标签数据区的字起始地址
    private int start;

    // ：待写入的数据内容（可选）(16进制)
    private String hexWriteData;

    //：待写入的数据内容
    private String bwriteData;

    //:选择读取参数（可选）（详见参数说明）
    private ParamEpcFilter filter = null;

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

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public String getHexWriteData() {
        return hexWriteData;
    }

    public void setHexWriteData(String hexWriteData) {
        this.hexWriteData = hexWriteData;
    }

    public String getBwriteData() {
        return bwriteData;
    }

    public void setBwriteData(String bwriteData) {
        this.bwriteData = bwriteData;
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
