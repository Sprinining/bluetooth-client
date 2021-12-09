package com.example.bluetoothsdk;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.example.bluetoothsdk.interfaces.ConnectResultListener;

import java.io.IOException;
import java.util.UUID;

public class BluetoothServer {
    private static final String TAG = BluetoothServer.class.getSimpleName();
    private static volatile BluetoothServer INSTANCE;
    private BluetoothServerSocket bluetoothServerSocket;
    private final UUID uuid;
    private ConnectResultListener connectResultListener;
    private ConnectedThread connectedThread;

    private BluetoothServer(String uuid) {
        // 设置uuid，服务器和客户端用同一个uuid；类似于tcp socket通信的端口，mac地址类似于ip
        if (uuid == null || uuid.equals("")) {
            this.uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
        } else {
            this.uuid = UUID.fromString(uuid);
        }
    }

    /**
     * @return 单例
     */
    public static BluetoothServer getINSTANCE(String uuid) {
        if (INSTANCE == null) {
            synchronized (BluetoothServer.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BluetoothServer(uuid);
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 启动监听，收到一个客户端请求后，放入服务线程操作，并立刻关闭bluetoothServerSocket停止监听
     */
    public void start() {
        // 通过蓝牙适配器，获取BluetoothServerSocket用于监听
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        try {
            bluetoothServerSocket = bluetoothAdapter.listenUsingRfcommWithServiceRecord(BluetoothAdapter.getDefaultAdapter().getName(), uuid);
            Log.i(TAG, "start: 蓝牙服务器已启动");
        } catch (IOException e) {
            Log.e(TAG, "start: bluetoothServerSocket获取失败", e);
            closeBluetoothServer();
            return;
        }

        // 持续监听（监听到一个就终止循环）
        while (true) {
            BluetoothSocket bluetoothSocket;

            try {
                /*
                只有当远程设备发送包含 UUID 的连接请求，并且该 UUID 与使用此侦听服务器套接字注册的 UUID 相匹配时，
                服务器才会接受连接。连接成功后，accept() 将返回已连接的 BluetoothSocket。
                 */
                bluetoothSocket = bluetoothServerSocket.accept();
            } catch (IOException e) {
                Log.e(TAG, "start: 蓝牙服务器监听异常终止", e);
                if (connectResultListener != null) {
                    connectResultListener.connectFail(e);
                }
                break;
            }

            if (bluetoothSocket != null) {
                Log.i(TAG, "start: 服务器收到了一个客户端连接 name:" + bluetoothSocket.getRemoteDevice().getName() +
                        ", address:" + bluetoothSocket.getRemoteDevice().getAddress());

                // 在子线程中处理socket
                connectedThread = new ConnectedThread(bluetoothSocket);
                connectedThread.start();
                if (connectResultListener != null) {
                    connectResultListener.connectSuccess(connectedThread);
                }

                // 然后立刻关闭serverSocket
                // 从 BluetoothServerSocket 获取 BluetoothSocket 后，可以（并且应该）舍弃 BluetoothServerSocket，除非设备需要接受更多连接。
                /*
                此方法调用会释放服务器套接字及其所有资源，但不会关闭 accept() 所返回的已连接的 BluetoothSocket。
                与 TCP/IP 不同，RFCOMM 一次只允许每个通道有一个已连接的客户端，因此大多数情况下，在接受已连接的套接字后，
                可以立即在 BluetoothServerSocket 上调用 close()。
                 */

                // 关闭socket不再接收
/*                if (bluetoothServerSocket != null) {
                    try {
                        bluetoothServerSocket.close();
                        Log.i(TAG, "closeBluetoothServer: 服务器关闭成功");
                    } catch (IOException e) {
                        Log.e(TAG, "closeBluetoothServer: 服务器关闭异常", e);
                    }
                }*/
//                break;
            }
        }
    }

    /**
     * 释放资源
     */
    public void closeBluetoothServer() {
        if (connectResultListener != null) {
            connectResultListener.disconnect();
        }
        // 关闭线程
        if (connectedThread != null) {
            connectedThread.closeConnectedThread();
        }
        // 关闭socket
        if (bluetoothServerSocket != null) {
            try {
                bluetoothServerSocket.close();
                Log.i(TAG, "closeBluetoothServer: 服务器关闭成功");
            } catch (IOException e) {
                Log.e(TAG, "closeBluetoothServer: 服务器关闭异常", e);
            }
        }
    }

    public void setConnectResultListener(ConnectResultListener connectResultListener) {
        this.connectResultListener = connectResultListener;
    }
}
