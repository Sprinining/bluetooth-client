package com.example.uhf_bluetoothclient.entity;

public class ParamEpcReadUserdata {


    //：起始字地址
    private int start;

    //：读写器需要读取的用户数据的字长度
    private int len;

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }
}
