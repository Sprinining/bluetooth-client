package com.example.bluetoothsdk;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import androidx.annotation.RequiresPermission;

import com.example.bluetoothsdk.broadcast.BluetoothBroadcastReceiver;
import com.example.bluetoothsdk.entity.BluetoothConfig;
import com.example.bluetoothsdk.entity.ScanConfig;
import com.example.bluetoothsdk.interfaces.BluetoothFunctionInterface;
import com.example.bluetoothsdk.interfaces.ConnectResultListener;
import com.example.bluetoothsdk.interfaces.ConnectStateListener;
import com.example.bluetoothsdk.interfaces.PairingResultListener;
import com.example.bluetoothsdk.interfaces.ResultListener;
import com.example.bluetoothsdk.interfaces.ScanResultListener;

import java.lang.reflect.Method;
import java.util.Set;

public class BluetoothUtils implements BluetoothFunctionInterface {
    private static final String TAG = BluetoothUtils.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    private static volatile BluetoothUtils INSTANCE;
    private final BluetoothAdapter bluetoothAdapter;
    private Context context;
    private BluetoothConfig bluetoothConfig;
    private BluetoothServer bluetoothServer;
    private BluetoothClient bluetoothClient;
    private BluetoothBroadcastReceiver bluetoothBroadcastReceiver;
    // 默认自动配对
    private boolean isAutoPair = true;

    private BluetoothUtils() {
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public static BluetoothUtils getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (BluetoothUtils.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BluetoothUtils();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    public BluetoothUtils setContext(Context context) {
        this.context = context;
        config().setUuid("00001101-0000-1000-8000-00805F9B34FB");
        bluetoothBroadcastReceiver = new BluetoothBroadcastReceiver();
        return this;
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    @Override
    public BluetoothUtils enableBluetooth() {
        // 整个系统只有一个蓝牙适配器
        if (bluetoothAdapter == null) {
            Log.e(TAG, "enableBluetooth: 不支持蓝牙");
            return this;
        }

        // 没启用就打开蓝牙
        if (!bluetoothAdapter.isEnabled()) {
            if (bluetoothAdapter.enable()) {
                Log.i(TAG, "enableBluetooth: 蓝牙打开成功");
            } else {
                Log.e(TAG, "enableBluetooth: 蓝牙打开失败");
            }
        }
        return this;
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    @Override
    public BluetoothUtils disableBluetooth() {
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.disable()) {
                Log.i(TAG, "disableBluetooth: 蓝牙关闭成功");
            } else {
                Log.e(TAG, "disableBluetooth: 蓝牙关闭失败");
            }
        }
        return this;
    }

    @Override
    public BluetoothConfig config() {
        if (bluetoothConfig == null) {
            bluetoothConfig = new BluetoothConfig();
        }
        return bluetoothConfig;
    }

    @RequiresPermission("android.permission.BLUETOOTH_SCAN")
    @Override
    public void scan(ScanConfig scanConfig, ScanResultListener resultListener) {
        // 注册好监听
        bluetoothBroadcastReceiver.setScanResultListener(resultListener);

        if (bluetoothAdapter == null) {
            resultListener.scanError("不支持蓝牙");
            return;
        }

        if (!bluetoothAdapter.isEnabled()) {
            resultListener.scanError("蓝牙未打开");
            return;
        }

        // 开始搜索设备
        // 发现进程通常包含约 12 秒钟的查询扫描，随后会对发现的每台设备进行页面扫描，以检索其蓝牙名称。
        bluetoothAdapter.cancelDiscovery();
        boolean b = bluetoothAdapter.startDiscovery();
        if (!b && resultListener != null) {
            resultListener.scanError("启动搜索失败");
        }
    }

    @RequiresPermission("android.permission.BLUETOOTH_SCAN")
    @Override
    public void stopScan() {
        if (bluetoothAdapter != null && bluetoothAdapter.isDiscovering()) {
            bluetoothAdapter.cancelDiscovery();
        }
    }

    @RequiresPermission("android.permission.BLUETOOTH_PRIVILEGED")
    @Override
    public void startPairing(BluetoothDevice device, boolean autoPair, PairingResultListener pairingResultListener) {
        if (device == null) {
            return;
        }
        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            this.isAutoPair = autoPair;
            device.createBond();
        }
        bluetoothBroadcastReceiver.setPairingResultListener(pairingResultListener);
/*        try {
            ClsUtils.createBond(device.getClass(), device);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    @Override
    public void cancelPairing(BluetoothDevice device, ResultListener resultListener) {
        if (device == null) {
            return;
        }
        if (device.getBondState() != BluetoothDevice.BOND_NONE) {
            try {
                Method method = device.getClass().getMethod("removeBond");
                boolean r = (boolean) method.invoke(device);
                if (r) {
                    resultListener.success();
                } else {
                    resultListener.failed(null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                resultListener.failed(e);
            }
        }
    }

    @Override
    public boolean isPaired(BluetoothDevice device) {
        return getPairedDevices().contains(device);
    }

    @Override
    public void connect(BluetoothDevice device, ConnectResultListener connectResultListener) {
        if (bluetoothAdapter.isEnabled()) {
            bluetoothClient = new BluetoothClient(device, config().getUuid());
            bluetoothClient.setConnectResultListener(connectResultListener);
            bluetoothClient.start();
        }
    }

    @Override
    public BluetoothUtils registerServer(ConnectResultListener connectResultListener) {
        if (bluetoothAdapter.isEnabled()) {
            bluetoothServer = BluetoothServer.getINSTANCE(config().getUuid());
            bluetoothServer.setConnectResultListener(connectResultListener);
            bluetoothServer.start();
        }
        return this;
    }

    @Override
    public BluetoothUtils registerConnectStateListener(ConnectStateListener connectStateListener) {
        bluetoothBroadcastReceiver.setConnectStateListener(connectStateListener);
        return this;
    }

    @Override
    public void destroy() {
        if (bluetoothClient != null) {
            bluetoothClient.closeBluetoothClient();
        }
        if (bluetoothServer != null) {
            bluetoothServer.closeBluetoothServer();
        }
    }

    @RequiresPermission("android.permission.BLUETOOTH_ADVERTISE")
    @Override
    public BluetoothUtils enableDiscoverable(long time) {
        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        // 开启时长单位s
        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, time);
        context.startActivity(discoverableIntent);
        return this;
    }

    @Override
    public BluetoothUtils enableDiscoverable() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class, int.class);
            setScanMode.setAccessible(true);

            setDiscoverableTimeout.invoke(adapter, 0);
            setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE, 0);
            Log.i(TAG, "enableDiscoverable: 蓝牙可见性开启");
        } catch (Exception e) {
            Log.e(TAG, "enableDiscoverable: 蓝牙可见性开启失败", e);
        }
        return this;
    }

    @Override
    public BluetoothUtils disableDiscoverable() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        try {
            Method setDiscoverableTimeout = BluetoothAdapter.class.getMethod("setDiscoverableTimeout", int.class);
            setDiscoverableTimeout.setAccessible(true);
            Method setScanMode = BluetoothAdapter.class.getMethod("setScanMode", int.class, int.class);
            setScanMode.setAccessible(true);

            setDiscoverableTimeout.invoke(adapter, 1);
            setScanMode.invoke(adapter, BluetoothAdapter.SCAN_MODE_CONNECTABLE, 1);
            Log.i(TAG, "disableDiscoverable: success");
        } catch (Exception e) {
            Log.e(TAG, "disableDiscoverable: fail", e);
        }
        return this;
    }

    @RequiresPermission("android.permission.BLUETOOTH_CONNECT")
    @Override
    public Set<BluetoothDevice> getPairedDevices() {
        // 获取每台配对过的设备的名称和MAC地址
/*        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress();
                Log.i(TAG, "已配对: deviceName:" + deviceName + " deviceHardwareAddress" + deviceHardwareAddress);
            }
        } else {
            Log.e(TAG, "getPairedDevices: 没有配对过的设备");
        }*/
        return bluetoothAdapter.getBondedDevices();
    }

    @Override
    public BluetoothUtils registerBluetoothBroadcastReceiver() {
        // 注册广播
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.setPriority(1000);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        // 两种情况会触发ACTION_DISCOVERY_FINISHED：1.系统结束扫描（约12秒）；2.调用cancelDiscovery()方法主动结束扫描
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        // 配对
        filter.addAction(BluetoothDevice.ACTION_PAIRING_REQUEST);
        // 蓝牙状态
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        // 蓝牙设备是否连接
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
        // 配对状态
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        if (context != null) {
            context.registerReceiver(bluetoothBroadcastReceiver, filter);
            Log.e(TAG, "registerBluetoothBroadcastReceiver: 注册了广播");
        } else {
            Log.e(TAG, "registerBluetoothBroadcastReceiver: 注册广播失败");
        }
        return this;
    }

    public boolean isAutoPair() {
        return isAutoPair;
    }
}
