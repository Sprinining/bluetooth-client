package com.example.uhf_bluetoothclient.util;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.bluetoothsdk.BluetoothUtils;
import com.example.bluetoothsdk.ConnectedThread;
import com.example.bluetoothsdk.interfaces.ConnectResultListener;
import com.example.bluetoothsdk.interfaces.ConnectStateListener;
import com.example.bluetoothsdk.interfaces.PairingResultListener;
import com.example.bluetoothsdk.interfaces.ScanResultListener;
import com.example.bluetoothsdk.interfaces.TransferListener;
import com.example.uhf_bluetoothclient.constants.Constants;
import com.example.uhf_bluetoothclient.ui.MainActivity;
import com.example.uhf_bluetoothclient.viewmodel.ScanDeviceViewModel;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class BleClient {
    private static final String TAG = BleClient.class.getSimpleName();
    private final BluetoothUtils bluetoothUtils;
    @SuppressLint("StaticFieldLeak")
    private static volatile BleClient INSTANCE;
    private ConnectedThread connectedThread;
    private ScanDeviceViewModel scanDeviceViewModel;
    private Context context;
    private Handler handler;
    private BluetoothDevice tempDevice;

    private BleClient() {
        bluetoothUtils = BluetoothUtils.getINSTANCE();
    }

    public static BleClient getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (BleClient.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BleClient();
                }
            }
        }
        return INSTANCE;
    }

    public BleClient setContext(Context context) {
        bluetoothUtils.setContext(context);
        this.context = context;
        return this;
    }

    public BleClient enableBluetooth() {
        bluetoothUtils.enableBluetooth();
        return this;
    }

    public boolean isPaired(BluetoothDevice device) {
        return bluetoothUtils.isPaired(device);
    }

    @SuppressLint("MissingPermission")
    public void startPairing(BluetoothDevice device, PairingResultListener pairingResultListener) {
        bluetoothUtils.startPairing(device, false, pairingResultListener);
    }

    public void connect(BluetoothDevice device) {
        Log.e(TAG, "connect: " + device.getName());
        Log.e(TAG, "connect: " + device.getAddress());
        tempDevice = device;
        bluetoothUtils.stopScan();
        bluetoothUtils.connect(device, connectResultListener);
    }

    public void reconnect() {
        if (tempDevice != null) {
            Log.e(TAG, "reconnect: 重新连接中...");
            bluetoothUtils.stopScan();
            bluetoothUtils.connect(tempDevice, connectResultListener);
        }
    }

    public void destroy() {
        bluetoothUtils.destroy();
    }

    /**
     * @param bytes 发送的数据
     */
    public void write(byte[] bytes) {
        if (connectedThread != null) {
            connectedThread.write(bytes);
        } else {
            Log.e(TAG, "write: " + Constants.BLUETOOTH_NOT_CONNECTED);
        }
    }

    /**
     * 扫描
     */
    public void scan() {
        bluetoothUtils.scan(null, scanResultListener);
    }

    public BleClient registerConnectStateListener() {
        bluetoothUtils.registerConnectStateListener(connectStateListener);
        return this;
    }

    public BleClient registerBluetoothBroadcastReceiver() {
        bluetoothUtils.registerBluetoothBroadcastReceiver();
        return this;
    }

    // 连接监听
    private final ConnectResultListener connectResultListener = new ConnectResultListener() {
        @Override
        public void connectSuccess(ConnectedThread connectedThread) {
            showToast("连接成功");
            dismissDialog();
            BleClient.getINSTANCE().connectedThread = connectedThread;
            // 读监听
            connectedThread.setReadTransferListener(readTransferListener);
            // 写监听
            connectedThread.setWriteTransferListener(writeTransferListener);
            // 跳转界面
            if (context != null) {
                Intent intent = new Intent(context, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }

        @Override
        public void connectFail(Exception e) {
            if (handler != null) {
                Message message = new Message();
                message.what = Constants.MESSAGE_WHAT_BLE_RECONNECT_FAIL;
                handler.sendMessage(message);
            }
            showToast("蓝牙连接失败");
        }

        @Override
        public void disconnect() {
            showToast("蓝牙断开连接");
            // 弹出提示框
            Message message = new Message();
            message.what = Constants.MESSAGE_WHAT_BLE_DISCONNECT;
            if (handler != null) {
                handler.sendMessage(message);
            }
        }
    };

    // 扫描监听
    private final ScanResultListener scanResultListener = new ScanResultListener() {
        @SuppressLint("NotifyDataSetChanged")
        @Override
        public void onDeviceFound(BluetoothDevice device) {
            String deviceName = device.getName();
            String deviceHardwareAddress = device.getAddress();
            Log.i(TAG, "搜索到的设备: deviceName: " + deviceName + " deviceHardwareAddress: " + deviceHardwareAddress);
            if (scanDeviceViewModel != null) {
                List<BluetoothDevice> list = scanDeviceViewModel.getListMutableLiveData().getValue();
                // TODO: 2021/12/31 对东集设备进行过滤
                if (list != null && !list.contains(device)) {
                    list.add(device);
                    scanDeviceViewModel.getListMutableLiveData().setValue(list);
                    scanDeviceViewModel.getScanDeviceRecyclerViewAdapter().notifyDataSetChanged();
                }
            }
        }

        @Override
        public void scanFinish() {
            Log.i(TAG, "scanFinish: ");
        }

        @Override
        public void scanError(String err) {
            Log.e(TAG, "scanError: " + err);
        }
    };

    // 读监听
    private final TransferListener readTransferListener = new TransferListener() {

        @Override
        public void transferFinish() {
            Log.i(TAG, "transferFinish: 客户端读线程关闭");
        }

        @Override
        public void transferSuccess(byte[] bytes) {
            Log.i(TAG, "transferSuccess: 客户端接收：" + new String(bytes, StandardCharsets.UTF_8));
            MessageUtils.getINSTANCE().handlerMessage(new String(bytes, StandardCharsets.UTF_8));
        }

        @Override
        public void transferException(String str, Exception e) {
            Log.e(TAG, "transferException: " + str, e);
        }
    };

    // 写监听
    private final TransferListener writeTransferListener = new TransferListener() {
        @Override
        public void transferFinish() {
            Log.i(TAG, "transferFinish: 客户端写线程关闭");
        }

        @Override
        public void transferSuccess(byte[] bytes) {
            Log.i(TAG, "transferSuccess: 客户端发送：" + new String(bytes, StandardCharsets.UTF_8));
        }

        @Override
        public void transferException(String str, Exception e) {
            Log.e(TAG, "transferException: " + str, e);
        }
    };

    // 连接状态监听
    private final ConnectStateListener connectStateListener = connectState -> {
        Log.i(TAG, "广播接收: " + connectState.toString());
        if (scanDeviceViewModel != null) {
            scanDeviceViewModel.getConnectState().setValue(connectState.toString());
        }
    };

    public BleClient setScanDeviceViewModel(ScanDeviceViewModel scanDeviceViewModel) {
        this.scanDeviceViewModel = scanDeviceViewModel;
        return this;
    }

    public void showToast(String str) {
        if (handler != null) {
            android.os.Message message = new android.os.Message();
            message.what = Constants.MESSAGE_WHAT_SHOW_TOAST;
            Bundle bundle = new Bundle();
            bundle.putString("toast", str);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }

    public void dismissDialog() {
        if (handler != null) {
            Message message = new Message();
            message.what = Constants.MESSAGE_WHAT_BLE_RECONNECT_SUCCESS;
            handler.sendMessage(message);
        }
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
