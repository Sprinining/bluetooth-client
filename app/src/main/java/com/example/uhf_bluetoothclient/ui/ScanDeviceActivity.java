package com.example.uhf_bluetoothclient.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.bluetoothsdk.interfaces.BluetoothPermissionInterface;
import com.example.uhf_bluetoothclient.R;
import com.example.uhf_bluetoothclient.databinding.ActivityScanDeviceBinding;
import com.example.uhf_bluetoothclient.util.BleClient;
import com.example.uhf_bluetoothclient.viewmodel.ScanDeviceViewModel;

public class ScanDeviceActivity extends AppCompatActivity implements BluetoothPermissionInterface {
    private static final String TAG = ScanDeviceActivity.class.getSimpleName();

    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String str = msg.getData().getString("toast");
                    Toast.makeText(ScanDeviceActivity.this, str, Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityScanDeviceBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_scan_device);
        ScanDeviceViewModel scanDeviceViewModel = new ViewModelProvider(this).get(ScanDeviceViewModel.class);
        binding.setVm(scanDeviceViewModel);
        binding.setLifecycleOwner(this);

        requestPermission();

        binding.rvDeviceFound.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        binding.rvDeviceFound.setLayoutManager(new LinearLayoutManager(this));

        BleClient.getINSTANCE()
                .setContext(this)
                .enableBluetooth()
                .registerConnectStateListener()
                .registerBluetoothBroadcastReceiver()
                .setHandler_scan_activity(handler)
                .setScanDeviceViewModel(scanDeviceViewModel);

        scanDeviceViewModel.getConnectState().observe(this, binding.tvConnectState::setText);
    }

    @Override
    public void requestPermission() {
        // 安卓6.0及以上要运行时申请权限
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(ScanDeviceActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        // 判断授权结果
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(this, "已拒绝权限", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}