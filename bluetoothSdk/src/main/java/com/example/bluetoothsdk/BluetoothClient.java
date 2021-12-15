package com.example.bluetoothsdk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.bluetoothsdk.interfaces.ConnectResultListener;

import java.io.IOException;
import java.util.UUID;

public class BluetoothClient {
    private static final String TAG = BluetoothClient.class.getSimpleName();
    private final BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private ConnectedThread connectedThread;
    private ConnectResultListener connectResultListener;

    /**
     * @param device 服务器的蓝牙设备
     * @param uuid   唯一标识
     */
    public BluetoothClient(BluetoothDevice device, String uuid) {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            if (uuid == null || uuid.equals("")) {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"));
            } else {
                bluetoothSocket = device.createRfcommSocketToServiceRecord(UUID.fromString(uuid));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        // 应始终调用 cancelDiscovery()，以确保设备在您调用 connect() 之前不会执行设备发现。
        // 如果正在执行发现操作，则会大幅降低连接尝试的速度，并增加连接失败的可能性。
        bluetoothAdapter.cancelDiscovery();

        try {
            /*
                通过调用 connect() 发起连接。请注意，此方法为阻塞调用。
                当客户端调用此方法后，系统会执行 SDP 查找，以找到带有所匹配 UUID 的远程设备。
                如果查找成功并且远程设备接受连接，则其会共享 RFCOMM 通道以便在连接期间使用，并且 connect() 方法将会返回。
                如果连接失败，或者 connect() 方法超时（约 12 秒后），则此方法将引发 IOException。
             */
            bluetoothSocket.connect();

            // 放到客户端线程中处理
            connectedThread = new ConnectedThread(bluetoothSocket);
            connectedThread.start();
            if (connectResultListener != null) {
                connectResultListener.connectSuccess(connectedThread);
                connectedThread.setConnectResultListener(connectResultListener);
            }
        } catch (IOException e) {
            if (connectResultListener != null) {
                connectResultListener.connectFail(e);
            }
            // 无法连接;关闭套接字并返回。
            try {
                bluetoothSocket.close();
            } catch (IOException closeException) {
                Log.e(TAG, "start: 客户端关闭bluetoothSocket失败", closeException);
            }
        }
    }

    public void closeBluetoothClient() {
        // 断开连接
        if (connectResultListener != null) {
            connectResultListener.disconnect();
        }
        // 关闭线程
        if (connectedThread != null) {
            connectedThread.closeConnectedThread();
        }
        // 关闭socket
        if (bluetoothSocket != null) {
            try {
                bluetoothSocket.close();
                Log.i(TAG, "closeBluetoothClient: 客户端关闭成功");
            } catch (IOException e) {
                Log.e(TAG, "closeBluetoothClient: 客户端关闭异常", e);
            }
        }
    }

    public void setConnectResultListener(ConnectResultListener connectResultListener) {
        this.connectResultListener = connectResultListener;
    }
}
