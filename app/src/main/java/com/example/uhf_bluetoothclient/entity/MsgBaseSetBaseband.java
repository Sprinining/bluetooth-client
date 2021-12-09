package com.example.uhf_bluetoothclient.entity;

public class MsgBaseSetBaseband {

    //EPC 基带速率（可选）
    private double baseSpeed;

    //默认 Q 值（可选）(0~15)
    private int qValue;

    //（可选）(0,Session0; 1,Session1; 2,Session2; 3,Session3)
    private int session;

    //盘存标志参数（可选）(0,仅用 Flag A 盘存;1,仅用 Flag B 盘存;2,轮流使用 Flag A 和 Flag B)
    private int inventoryFlag;

    public double getBaseSpeed() {
        return baseSpeed;
    }

    public void setBaseSpeed(double baseSpeed) {
        this.baseSpeed = baseSpeed;
    }

    public int getqValue() {
        return qValue;
    }

    public void setqValue(int qValue) {
        this.qValue = qValue;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public int getInventoryFlag() {
        return inventoryFlag;
    }

    public void setInventoryFlag(int inventoryFlag) {
        this.inventoryFlag = inventoryFlag;
    }
}
