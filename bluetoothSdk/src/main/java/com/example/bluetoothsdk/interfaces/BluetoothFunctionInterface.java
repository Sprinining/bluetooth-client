package com.example.bluetoothsdk.interfaces;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import com.example.bluetoothsdk.entity.BluetoothConfig;
import com.example.bluetoothsdk.entity.ScanConfig;

import java.util.Set;

public interface BluetoothFunctionInterface {

    BluetoothFunctionInterface setContext(Context context);

    /**
     * 打开蓝牙
     */
    BluetoothFunctionInterface enableBluetooth();

    /**
     * 关闭蓝牙
     */
    BluetoothFunctionInterface disableBluetooth();

    /**
     * @return 蓝牙配置
     */
    BluetoothConfig config();

    /**
     * 搜索设备
     *
     * @param scanConfig         搜索参数
     * @param scanResultListener 搜索监听
     */
    void scan(ScanConfig scanConfig, ScanResultListener scanResultListener);

    void stopScan();

    /**
     * 配对
     *
     * @param device                设备
     * @param pairingResultListener 配对结果监听
     */
    void startPairing(BluetoothDevice device, PairingResultListener pairingResultListener);

    /**
     * 取消配对
     *
     * @param device         设备
     * @param resultListener 结果监听
     */
    void cancelPairing(BluetoothDevice device, ResultListener resultListener);

    boolean isPaired(BluetoothDevice device);

    /**
     * 给客户端连接服务器的
     *
     * @param device                服务器
     * @param connectResultListener 连接监听
     */
    void connect(BluetoothDevice device, ConnectResultListener connectResultListener);

    /**
     * 注册服务器
     *
     * @param connectResultListener 连接监听
     */
    BluetoothFunctionInterface registerServer(ConnectResultListener connectResultListener);

    BluetoothFunctionInterface registerConnectStateListener(ConnectStateListener connectStateListener);

    void destroy();

    /**
     * 启用可检测性
     * 默认情况下，设备处于可检测到模式的时间为 120 秒（2 分钟）。
     * 通过添加 EXTRA_DISCOVERABLE_DURATION Extra 属性，可以定义不同的持续时间，最高可达 3600 秒（1 小时）
     * 如果尚未在设备上启用蓝牙，则启用设备可检测性会自动启用蓝牙。
     *
     * @param time 可见时间
     */
    BluetoothFunctionInterface enableDiscoverable(long time);

    /**
     * 蓝牙开启之后才有效
     * 永久打开可见性
     */
    BluetoothFunctionInterface enableDiscoverable();

    /**
     * 蓝牙开启之后才有效
     * 永久关闭可见性
     */
    BluetoothFunctionInterface disableDiscoverable();

    /**
     * @return 配对过的设备信息
     */
    Set<BluetoothDevice> getPairedDevices();

    BluetoothFunctionInterface registerBluetoothBroadcastReceiver();

}
