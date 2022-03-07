package com.example.uhf_bluetoothclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

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
        Intent intent = getIntent();
        boolean btn_flag = intent.getBooleanExtra("btn_flag", true);
        // true显示连接设备，false显示仅连接
        if (btn_flag) {
            binding.btnConnectDevice.setVisibility(View.VISIBLE);
            binding.btnGetInfo.setVisibility(View.INVISIBLE);
        } else {
            binding.btnConnectDevice.setVisibility(View.INVISIBLE);
            binding.btnGetInfo.setVisibility(View.VISIBLE);
        }
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