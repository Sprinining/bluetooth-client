package com.example.uhf_bluetoothclient.entity;

import java.util.HashMap;

public class MsgAppGetGpiState {

    private HashMap<Integer, Integer> hpGpiState;

    public HashMap<Integer, Integer> getHpGpiState() {
        return hpGpiState;
    }

    public void setHpGpiState(HashMap<Integer, Integer> hpGpiState) {
        this.hpGpiState = hpGpiState;
    }
}
