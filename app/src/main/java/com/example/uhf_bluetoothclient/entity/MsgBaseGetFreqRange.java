package com.example.uhf_bluetoothclient.entity;

public class MsgBaseGetFreqRange {

    //频段索引，具体对应关系，详见附录 1
    private String freqRangeIndex;

    public String getFreqRangeIndex() {
        return freqRangeIndex;
    }

    public void setFreqRangeIndex(String freqRangeIndex) {
        this.freqRangeIndex = freqRangeIndex;
    }
}
