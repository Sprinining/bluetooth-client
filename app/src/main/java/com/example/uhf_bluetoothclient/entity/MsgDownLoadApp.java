package com.example.uhf_bluetoothclient.entity;

public class MsgDownLoadApp {

    //http://服务器地址：服务端口
    //例: http://192.168.80.100:8080
    private String address;

    //apk文件的相对路径
    //例：/opt/gateway/yw/app-release.apk
    private String url;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
