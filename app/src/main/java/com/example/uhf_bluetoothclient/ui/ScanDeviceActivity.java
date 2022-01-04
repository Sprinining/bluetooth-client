package com.example.uhf_bluetoothclient.ui;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uhf_bluetoothclient.R;
import com.example.uhf_bluetoothclient.databinding.ActivityScanDeviceBinding;
import com.example.uhf_bluetoothclient.util.BleClient;
import com.example.uhf_bluetoothclient.viewmodel.ScanDeviceViewModel;

public class ScanDeviceActivity extends BaseActivity {
    private ScanDeviceViewModel scanDeviceViewModel;

    @Override
    public void initView() {
        ActivityScanDeviceBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_scan_device);
        scanDeviceViewModel = new ViewModelProvider(this).get(ScanDeviceViewModel.class);
        binding.setVm(scanDeviceViewModel);
        binding.setLifecycleOwner(this);
        binding.rvDeviceFound.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.rvDeviceFound.setLayoutManager(new LinearLayoutManager(this));
        scanDeviceViewModel.getConnectState().observe(this, binding.tvConnectState::setText);
    }

    @Override
    public void initOthers() {
        BleClient.getINSTANCE()
                .setScanDeviceViewModel(scanDeviceViewModel);
    }
}