package com.example.uhf_bluetoothclient.entity;

public class Message<T> {
    // 请求码
    private int code;
    // 数据
    private T data;
    // 消息返回码 0 为操作成功，非 0 操作失败，默认值-1
    private byte rtCode = (byte) -1;
    // 操作失败原因
    private String rtMsg = "";

    public Message() {
    }

    public Message(int code, T data, byte rtCode, String rtMsg) {
        this.code = code;
        this.data = data;
        this.rtCode = rtCode;
        this.rtMsg = rtMsg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public byte getRtCode() {
        return rtCode;
    }

    public void setRtCode(byte rtCode) {
        this.rtCode = rtCode;
    }

    public String getRtMsg() {
        return rtMsg;
    }

    public void setRtMsg(String rtMsg) {
        this.rtMsg = rtMsg;
    }

    @Override
    public String toString() {
        return "Message{" +
                "code=" + code +
                ", data=" + data +
                ", rtCode=" + rtCode +
                ", rtMsg='" + rtMsg + '\'' +
                '}';
    }
}
