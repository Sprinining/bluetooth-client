package com.example.uhf_bluetoothclient.entity;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.example.uhf_bluetoothclient.BR;

import java.util.Date;

public class LogBaseEpcInfo extends BaseObservable {


    // 16 进制 EPC 字符串
//    private String epc;
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

    public void setEpc(String epc) {
        this.epc = epc;
        notifyPropertyChanged(BR.epc);
    }

    public byte[] getbEpc() {
        return bEpc;
    }

    public void setbEpc(byte[] bEpc) {
        this.bEpc = bEpc;
    }

    public String getPc() {
        return pc;
    }

    public void setPc(String pc) {
        this.pc = pc;
    }

    public int getAntId() {
        return antId;
    }

    public void setAntId(int antId) {
        this.antId = antId;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getTid() {
        return tid;
    }

    public void setTid(String tid) {
        this.tid = tid;
    }

    public byte[] getbTid() {
        return bTid;
    }

    public void setbTid(byte[] bTid) {
        this.bTid = bTid;
    }

    public String getUserdata() {
        return userdata;
    }

    public void setUserdata(String userdata) {
        this.userdata = userdata;
    }

    public byte[] getbUser() {
        return bUser;
    }

    public void setbUser(byte[] bUser) {
        this.bUser = bUser;
    }

    public String getReserved() {
        return reserved;
    }

    public void setReserved(String reserved) {
        this.reserved = reserved;
    }

    public byte[] getbRes() {
        return bRes;
    }

    public void setbRes(byte[] bRes) {
        this.bRes = bRes;
    }

    public Date getReadTime() {
        return readTime;
    }

    public void setReadTime(Date readTime) {
        this.readTime = readTime;
    }
}
