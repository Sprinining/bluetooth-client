package com.example.uhf_bluetoothclient.entity;

public class MsgSetIPv4 {
    String mode;
    String ipAddress;
    String netmask;
    String gateway;
    String dns1;
    String dns2;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getNetmask() {
        return netmask;
    }

    public void setNetmask(String netmask) {
        this.netmask = netmask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getDns1() {
        return dns1;
    }

    public void setDns1(String dns1) {
        this.dns1 = dns1;
    }

    public String getDns2() {
        return dns2;
    }

    public void setDns2(String dns2) {
        this.dns2 = dns2;
    }

    public MsgSetIPv4() {
    }

    public MsgSetIPv4(String mode, String ipAddress, String netmask, String gateway, String dns1, String dns2) {
        this.mode = mode;
        this.ipAddress = ipAddress;
        this.netmask = netmask;
        this.gateway = gateway;
        this.dns1 = dns1;
        this.dns2 = dns2;
    }
}
