package com.example.uhf_bluetoothclient.entity;

import java.util.List;

/**
 * @ProjectName: UHF_BluetoothClient
 * @Description: 解析ip状态
 * @Author: KP
 * @CreateDate: 2022/1/6 10:38
 * @Version: 1.0
 */
public class NetworkStateBean {
    public String networkType;
    public List<String> IP;
    public String NETMASK;
    public String GATEWAY;
    public List<String> DNS;
    public String MAC;

    public NetworkStateBean() {
    }

    public NetworkStateBean(String networkType, List<String> IP, String NETMASK, String GATEWAY, List<String> DNS, String MAC) {
        this.networkType = networkType;
        this.IP = IP;
        this.NETMASK = NETMASK;
        this.GATEWAY = GATEWAY;
        this.DNS = DNS;
        this.MAC = MAC;
    }
}
