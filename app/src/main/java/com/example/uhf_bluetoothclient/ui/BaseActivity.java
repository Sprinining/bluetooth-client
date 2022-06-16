package com.example.uhf_bluetoothclient.ui;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.bluetoothsdk.interfaces.BluetoothPermissionInterface;
import com.example.uhf_bluetoothclient.R;
import com.example.uhf_bluetoothclient.constants.Constants;
import com.example.uhf_bluetoothclient.util.BleClient;
import com.example.uhf_bluetoothclient.util.MessageUtils;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.impl.LoadingPopupView;
import com.seuic.util.common.PermissionUtils;
import com.seuic.util.common.constant.PermissionConstants;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public abstract class BaseActivity<T extends ViewDataBinding, V extends ViewModel> extends AppCompatActivity implements BluetoothPermissionInterface {
    protected T binding;
    protected V viewModel;
    private boolean mIsExit;
    private AlertDialog disconnect_alertDialog;
    private AlertDialog exit_alterDialog;
    public final Handler baseActivityHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case Constants.MESSAGE_WHAT_SHOW_TOAST:
                    String str = msg.getData().getString("toast");
                    showToast(str);
                    break;
                case Constants.MESSAGE_WHAT_BLE_DISCONNECT:
                    BleClient.getINSTANCE().enableBluetooth();
                    // 断开连接
                    showDisconnectDialog();
                    break;
                case Constants.MESSAGE_WHAT_BLE_RECONNECT_SUCCESS:
                    // 重连成功
                    dismissDialog();
                    break;
                case Constants.MESSAGE_WHAT_BLE_RECONNECT_FAIL:
                    // 重连失败
                    setDialogMessage("重连失败，请再次尝试");
                    break;
                case Constants.MESSAGE_WHAT_EXIT_TO_DEVICE_SEARCHING:
                    // 退出至设备搜索
                    showExitDialog();
                default:
            }
        }
    };
    private LoadingPopupView loadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Type superclass = getClass().getGenericSuperclass();
        Class<V> bClass = (Class<V>) ((ParameterizedType) superclass).getActualTypeArguments()[1];
        viewModel = new ViewModelProvider(this).get(bClass);
        binding = DataBindingUtil.setContentView(this, initLayout(savedInstanceState));
        binding.setLifecycleOwner(this);
        binding.setVariable(initBR(), viewModel);
        setContentView(binding.getRoot());

        requestPermission();
        initBleClient();
        initMessageUtil();
        initDialog();

        initView();
        initOthers();
        loadingView = new XPopup.Builder(this).asLoading();
    }

    public abstract void initView();

    public abstract void initOthers();

    public abstract int initLayout(Bundle savedInstanceState);

    public abstract int initBR();

    // 蓝牙
    private void initBleClient() {
        BleClient.getINSTANCE()
                .setContext(getApplicationContext())
                .enableBluetooth()
                .registerConnectStateListener()
                .registerBluetoothBroadcastReceiver()
                .setHandler(baseActivityHandler);
    }

    private void initMessageUtil() {
        MessageUtils.getINSTANCE()
                .setContext(getApplicationContext())
                .setHandler(baseActivityHandler);
    }

    // Toast
    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    // Dialog
    private void initDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(BaseActivity.this);
        disconnect_alertDialog = builder.setIcon(R.drawable.bluetooth_disconnect)
                .setTitle("连接中断")
                .setMessage("与设备的蓝牙连接已断开")
                .setPositiveButton("退出应用", disconnect_dialogOnclickListener)
                .setNeutralButton("重新连接", disconnect_dialogOnclickListener)
                .create();

        exit_alterDialog = builder.setIcon(R.drawable.exit)
                .setTitle("返回设备搜索")
                .setMessage("是否重启设备")
                .setPositiveButton("是", exit_dialogOnclickListener)
                .setNeutralButton("否", exit_dialogOnclickListener)
                .create();
    }

    public void showExitDialog() {
        exit_alterDialog.show();
    }

    public void showDisconnectDialog() {
        disconnect_alertDialog.show();
    }

    public void dismissDialog() {
        try {
            if (disconnect_alertDialog != null) {
                disconnect_alertDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setDialogMessage(String message) {
        try {
            if (disconnect_alertDialog != null) {
                disconnect_alertDialog.setMessage(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final DialogInterface.OnClickListener disconnect_dialogOnclickListener = (dialog, which) -> {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                exitAPP();
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                BleClient.getINSTANCE().reconnect();
                disconnect_alertDialog.setMessage("重连中...");
                // 不退出dialog
                try {
                    Field field = dialog.getClass().getSuperclass().getSuperclass().getDeclaredField("mShowing");
                    field.setAccessible(true);
                    field.set(dialog, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
        }
    };

    private final DialogInterface.OnClickListener exit_dialogOnclickListener = (dialog, which) -> {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                // 重启R102S设备
                MessageUtils.getINSTANCE().rebootDevice();
            case DialogInterface.BUTTON_NEUTRAL:
                // 断开蓝牙并返回设备搜索界面
                BleClient.getINSTANCE().destroy();
                Intent intent = new Intent(this, ScanDeviceActivity.class);
                startActivity(intent);
                break;
            default:
        }
    };

    // 权限
    @Override
    public void requestPermission() {
        // 安卓6.0及以上要运行时申请权限
        PermissionUtils.permission(PermissionConstants.LOCATION).request();
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(BaseActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // 关闭蓝牙客户端
        BleClient.getINSTANCE().destroy();
        return super.onKeyDown(keyCode, event);
    }

    public void exitAPP() {
        // 关闭蓝牙客户端
        BleClient.getINSTANCE().destroy();
        // 关闭app
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();
        for (ActivityManager.AppTask appTask : appTaskList) {
            appTask.finishAndRemoveTask();
        }
    }

    protected void showLoading(String message) {
        loadingView.setTitle(message);
        if (!loadingView.isShow())
            loadingView.show();
    }

    protected void hideLoading() {
        loadingView.dismiss();
    }

}
