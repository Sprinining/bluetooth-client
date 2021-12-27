package com.example.uhf_bluetoothclient.viewmodel;

import android.bluetooth.BluetoothDevice;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.bluetoothsdk.interfaces.PairingResultListener;
import com.example.uhf_bluetoothclient.adapter.ScanDeviceRecyclerViewAdapter;
import com.example.uhf_bluetoothclient.util.BleClient;

import java.util.ArrayList;
import java.util.List;

public class ScanDeviceViewModel extends ViewModel {
    private final BleClient bleClient;
//    private final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
    private final ScanDeviceRecyclerViewAdapter scanDeviceRecyclerViewAdapter;
    // 连接状态
    private final MutableLiveData<String> connectState = new MutableLiveData<>("蓝牙未连接");
    private final MutableLiveData<List<BluetoothDevice>> listMutableLiveData = new MutableLiveData<>(new ArrayList<>());
    private int currentSelectedDeviceIndex = 0;

    public ScanDeviceViewModel() {
        bleClient = BleClient.getINSTANCE();
        scanDeviceRecyclerViewAdapter = new ScanDeviceRecyclerViewAdapter(listMutableLiveData.getValue());
        scanDeviceRecyclerViewAdapter.setMyClickListener(position -> currentSelectedDeviceIndex = position);
    }

    public ScanDeviceRecyclerViewAdapter getScanDeviceRecyclerViewAdapter() {
        return scanDeviceRecyclerViewAdapter;
    }

    public MutableLiveData<List<BluetoothDevice>> getListMutableLiveData() {
        return listMutableLiveData;
    }

    public MutableLiveData<String> getConnectState() {
        return connectState;
    }

/*    public StaggeredGridLayoutManager getStaggeredGridLayoutManager() {
        return staggeredGridLayoutManager;
    }*/

    public void scanDeviceClick() {
        if (listMutableLiveData.getValue() != null) {
            List<BluetoothDevice> list = listMutableLiveData.getValue();
            list.clear();
            listMutableLiveData.postValue(list);
        }
        bleClient.scan();
    }

    public void connectDeviceClick() {
        if (listMutableLiveData.getValue() != null) {
            if (currentSelectedDeviceIndex <= 0) {
                return;
            }
            BluetoothDevice device = listMutableLiveData.getValue().get(currentSelectedDeviceIndex);
            if (bleClient.isPaired(device)) {
                bleClient.connect(device);
            } else {
                bleClient.startPairing(device, new PairingResultListener() {
                    @Override
                    public void pairing(BluetoothDevice device) {

                    }

                    @Override
                    public void paired(BluetoothDevice device) {
                        bleClient.connect(device);
                    }

                    @Override
                    public void pairFailed(BluetoothDevice device) {

                    }
                });
            }
        }
    }
}
