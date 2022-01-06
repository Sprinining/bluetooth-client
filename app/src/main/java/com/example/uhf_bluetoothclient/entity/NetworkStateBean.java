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
    String networkType;
    List<String> IP;
    String NETMASK;
    String GATEWAY;
    List<String> DNS;
    String MAC;
}
