package com.example.uhf_bluetoothclient.entity;

public class ParamEpcReadTid {


    //：TID 读取模式配置，(0，TID 读取长度自适应，但最大长度不超过字节 1 定义的长度；1，按照字节 1 定义的长度读取 TID)
    private int mode;

    // :读写器需要读取 TID 数据的字(word，16bits，下同)长度
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
