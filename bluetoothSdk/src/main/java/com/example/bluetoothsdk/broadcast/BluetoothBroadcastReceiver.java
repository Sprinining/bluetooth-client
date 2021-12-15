package com.example.bluetoothsdk.broadcast;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.bluetoothsdk.entity.ConnectState;
import com.example.bluetoothsdk.interfaces.ConnectStateListener;
import com.example.bluetoothsdk.interfaces.PairingResultListener;
import com.example.bluetoothsdk.interfaces.ScanResultListener;
import com.example.bluetoothsdk.utils.ClsUtils;

public class BluetoothBroadcastReceiver extends BroadcastReceiver {
    private ScanResultListener scanResultListener;
    private ConnectStateListener connectStateListener;
    private PairingResultListener pairingResultListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

        switch (action) {
            case BluetoothDevice.ACTION_FOUND:
                if (scanResultListener != null) scanResultListener.onDeviceFound(device);
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_STARTED:
                sendConnectState(ConnectState.ACTION_DISCOVERY_STARTED);
                break;
            case BluetoothAdapter.ACTION_DISCOVERY_FINISHED:
                if (scanResultListener != null) scanResultListener.scanFinish();
                sendConnectState(ConnectState.ACTION_DISCOVERY_FINISHED);
                break;
            case BluetoothAdapter.ACTION_STATE_CHANGED:
                int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);
                switch (state) {
                    case BluetoothAdapter.STATE_OFF:
                        sendConnectState(ConnectState.STATE_OFF);
                        break;
                    case BluetoothAdapter.STATE_TURNING_OFF:
                        sendConnectState(ConnectState.STATE_TURNING_OFF);
                        break;
                    case BluetoothAdapter.STATE_ON:
                        sendConnectState(ConnectState.STATE_ON);
                        break;
                    case BluetoothAdapter.STATE_TURNING_ON:
                        sendConnectState(ConnectState.STATE_TURNING_ON);
                        break;
                }
                break;
            case BluetoothDevice.ACTION_ACL_CONNECTED:
                sendConnectState(ConnectState.ACTION_ACL_CONNECTED);
                break;
            case BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED:
                sendConnectState(ConnectState.ACTION_ACL_DISCONNECT_REQUESTED);
                break;
            case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                sendConnectState(ConnectState.ACTION_ACL_DISCONNECTED);
                break;
            case BluetoothDevice.ACTION_PAIRING_REQUEST:
                sendConnectState(ConnectState.ACTION_PAIRING_REQUEST);
/*                try {
                    ClsUtils.setPairingConfirmation(device.getClass(), device, true);
                    abortBroadcast();
                    ClsUtils.setPin(device.getClass(), device, "1234");
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                break;
            case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING:
                        sendConnectState(ConnectState.BOND_BONDING);
                        if (pairingResultListener != null) pairingResultListener.pairing(device);
                        break;
                    case BluetoothDevice.BOND_BONDED:
                        sendConnectState(ConnectState.BOND_BONDED);
                        if (pairingResultListener != null) pairingResultListener.paired(device);
                        break;
                    case BluetoothDevice.BOND_NONE:
                        sendConnectState(ConnectState.BOND_NONE);
                        if (pairingResultListener != null) pairingResultListener.pairFailed(device);
                        break;
                }
                break;
            default:
        }
    }

    public void setScanResultListener(ScanResultListener scanResultListener) {
        this.scanResultListener = scanResultListener;
    }

    public void setConnectStateListener(ConnectStateListener connectStateListener) {
        this.connectStateListener = connectStateListener;
    }

    public void setPairingResultListener(PairingResultListener pairingResultListener) {
        this.pairingResultListener = pairingResultListener;
    }

    public void sendConnectState(ConnectState connectState) {
        if (connectStateListener != null) {
            connectStateListener.onStateChange(connectState);
        }
    }
}
