package com.example.uhf_bluetoothclient.entity;

public class ReadReserved {
    //：保留区读取参数 读取模式配置
    private int mode;

    // :读写器需要读取  数据的字(word，16bits，下同)长度
    private int len;

    public int getMode() {
        return mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public int getLen() {
        return len;
    }

    public void setLen(int len) {
        this.len = len;
    }
}
