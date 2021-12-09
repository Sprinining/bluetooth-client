package com.example.uhf_bluetoothclient.entity;


import java.util.Hashtable;

public class MsgBaseGetPower {

    //读写器对应天线功率( Hashtable<Integer, Integer> , key：天线索引号，value：天线功率值)
    private Hashtable<Integer, Integer> dicPower;

    public Hashtable<Integer, Integer> getDicPower() {
        return dicPower;
    }

    public void setDicPower(Hashtable<Integer, Integer> dicPower) {
        this.dicPower = dicPower;
    }

}
