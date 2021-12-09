package com.example.uhf_bluetoothclient.entity;

public class ParamEpcFilter {


    //: 要匹配的数据区(1，EPC 区；2，TID 区；3，用户数据区)
    private int area;

    //: 匹配数据起始位地址
    private int bitStart;

    //  : 需要匹配的数据位长度
    private int bitLength;

    //:需要匹配的数据内容（可选）(16进制 )
    private String hexData;

    //:需要匹配的数据内容
    private String bData;

    public int getArea() {
        return area;
    }

    public void setArea(int area) {
        this.area = area;
    }

    public int getBitStart() {
        return bitStart;
    }

    public void setBitStart(int bitStart) {
        this.bitStart = bitStart;
    }

    public int getBitLength() {
        return bitLength;
    }

    public void setBitLength(int bitLength) {
        this.bitLength = bitLength;
    }

    public String getHexData() {
        return hexData;
    }

    public void setHexData(String hexData) {
        this.hexData = hexData;
    }

    public String getbData() {
        return bData;
    }

    public void setbData(String bData) {
        this.bData = bData;
    }
}
