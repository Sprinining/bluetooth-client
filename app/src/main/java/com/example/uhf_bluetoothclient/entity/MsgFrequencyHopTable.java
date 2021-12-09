package com.example.uhf_bluetoothclient.entity;

public class MsgFrequencyHopTable {
    int minFrequencyIndex;
    int maxFrequencyIndex;

    public MsgFrequencyHopTable(int minFrequencyIndex, int maxFrequencyIndex) {
        this.minFrequencyIndex = minFrequencyIndex;
        this.maxFrequencyIndex = maxFrequencyIndex;
    }

    public MsgFrequencyHopTable() {
    }

    public int getMinFrequencyIndex() {
        return minFrequencyIndex;
    }

    public void setMinFrequencyIndex(int minFrequencyIndex) {
        this.minFrequencyIndex = minFrequencyIndex;
    }

    public int getMaxFrequencyIndex() {
        return maxFrequencyIndex;
    }

    public void setMaxFrequencyIndex(int maxFrequencyIndex) {
        this.maxFrequencyIndex = maxFrequencyIndex;
    }
}
