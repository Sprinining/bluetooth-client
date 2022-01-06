package com.example.uhf_bluetoothclient.ui;

import android.os.Bundle;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.uhf_bluetoothclient.BR;
import com.example.uhf_bluetoothclient.R;
import com.example.uhf_bluetoothclient.databinding.ActivityScanDeviceBinding;
import com.example.uhf_bluetoothclient.util.BleClient;
import com.example.uhf_bluetoothclient.viewmodel.ScanDeviceViewModel;

public class ScanDeviceActivity extends BaseActivity<ActivityScanDeviceBinding, ScanDeviceViewModel> {

    @Override
    public void initView() {
        binding.rvDeviceFound.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.rvDeviceFound.setLayoutManager(new LinearLayoutManager(this));
        viewModel.getConnectState().observe(this, binding.tvConnectState::setText);
    }

    @Override
    public void initOthers() {
        BleClient.getINSTANCE()
                .setScanDeviceViewModel(viewModel);
    }

    @Override
    public int initLayout(Bundle savedInstanceState) {
        return R.layout.activity_scan_device;
    }

    @Override
    public int initBR() {
        return BR.vmm;
    }
}