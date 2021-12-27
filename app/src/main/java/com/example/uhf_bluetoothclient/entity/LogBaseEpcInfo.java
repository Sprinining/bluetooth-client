package com.example.uhf_bluetoothclient.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.uhf_bluetoothclient.BR;

import java.util.Date;

public class LogBaseEpcInfo extends BaseObservable {
    // 16 进制 EPC 字符串
    private String epc;

    // EPC 字节数组
    private byte[] bEpc;

    // PC 值
    private String pc;

    // 天线编号
    private int antId;

    //信号强度
    private int rssi;

    //标签读取结果，0 为读取成功，非 0 为失败
    private int result;

    //16 进制 TID 字符串
    private String tid;

    //TID 字节数组
    private byte[] bTid;

    // 16 进制 Userdata 字符串
    private String userdata;

    //Userdata 字节数组
    private byte[] bUser;

    //16 进制保留区字符串
    private String reserved;

    //保留区字节数组
    private byte[] bRes;

    //读取标签时间
    private Date readTime;

    @Bindable
    public String getEpc() {
        return epc;
    }

    public LogBaseEpcInfo setEpc(String epc) {
        this.epc = epc;
        notifyPropertyChanged(BR.epc);
        return this;
    }

    public byte[] getbEpc() {
        return bEpc;
    }

    public LogBaseEpcInfo setbEpc(byte[] bEpc) {
        this.bEpc = bEpc;
        return this;
    }

    public String getPc() {
        return pc;
    }

    public LogBaseEpcInfo setPc(String pc) {
        this.pc = pc;
        return this;
    }

    public int getAntId() {
        return antId;
    }

    public LogBaseEpcInfo setAntId(int antId) {
        this.antId = antId;
        return this;
    }

    public int getRssi() {
        return rssi;
    }

    public LogBaseEpcInfo setRssi(int rssi) {
        this.rssi = rssi;
        return this;
    }

    public int getResult() {
        return result;
    }

    public LogBaseEpcInfo setResult(int result) {
        this.result = result;
        return this;
    }

    public String getTid() {
        return tid;
    }

    public LogBaseEpcInfo setTid(String tid) {
        this.tid = tid;
        return this;
    }

    public byte[] getbTid() {
        return bTid;
    }

    public LogBaseEpcInfo setbTid(byte[] bTid) {
        this.bTid = bTid;
        return this;
    }

    public String getUserdata() {
        return userdata;
    }

    public LogBaseEpcInfo setUserdata(String userdata) {
        this.userdata = userdata;
        return this;
    }

    public byte[] getbUser() {
        return bUser;
    }

    public LogBaseEpcInfo setbUser(byte[] bUser) {
        this.bUser = bUser;
        return this;
    }

    public String getReserved() {
        return reserved;
    }

    public LogBaseEpcInfo setReserved(String reserved) {
        this.reserved = reserved;
        return this;
    }

    public byte[] getbRes() {
        return bRes;
    }

    public LogBaseEpcInfo setbRes(byte[] bRes) {
        this.bRes = bRes;
        return this;
    }

    public Date getReadTime() {
        return readTime;
    }

    public LogBaseEpcInfo setReadTime(Date readTime) {
        this.readTime = readTime;
        return this;
    }

    public LogBaseEpcInfo() {
    }

    public LogBaseEpcInfo(String epc, byte[] bEpc, String pc, int antId, int rssi, int result, String tid, byte[] bTid, String userdata, byte[] bUser, String reserved, byte[] bRes, Date readTime) {
        this.epc = epc;
        this.bEpc = bEpc;
        this.pc = pc;
        this.antId = antId;
        this.rssi = rssi;
        this.result = result;
        this.tid = tid;
        this.bTid = bTid;
        this.userdata = userdata;
        this.bUser = bUser;
        this.reserved = reserved;
        this.bRes = bRes;
        this.readTime = readTime;
    }
}
