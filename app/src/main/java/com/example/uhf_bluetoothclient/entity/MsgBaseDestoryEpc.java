package com.example.uhf_bluetoothclient.entity;

public class MsgBaseDestoryEpc {

    //: 天线端口
    private int antennaEnable;

    //: 销毁密码
    private String hexPassword;

    //: 选择读取参数（可选）（详见参数说明）
    private ParamEpcFilter filter;

    public int getAntennaEnable() {
        return antennaEnable;
    }

    public void setAntennaEnable(int antennaEnable) {
        this.antennaEnable = antennaEnable;
    }

    public String getHexPassword() {
        return hexPassword;
    }

    public void setHexPassword(String hexPassword) {
        this.hexPassword = hexPassword;
    }

    public ParamEpcFilter getFilter() {
        return filter;
    }

    public void setFilter(ParamEpcFilter filter) {
        this.filter = filter;
    }
}
