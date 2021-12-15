package com.example.uhf_bluetoothclient.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.uhf_bluetoothclient.R;
import com.example.uhf_bluetoothclient.adapter.MyFragmentPagerAdapter;
import com.example.uhf_bluetoothclient.databinding.ActivityMainBinding;
import com.example.uhf_bluetoothclient.util.BleClient;
import com.example.uhf_bluetoothclient.util.MessageUtils;
import com.example.uhf_bluetoothclient.viewmodel.MyViewModel;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String[] tabs = {"配置", "扫描"};
    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private boolean mIsExit;
    private AlertDialog alertDialog;
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String str = msg.getData().getString("toast");
                    Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    BleClient.getINSTANCE().enableBluetooth();
                    // 断开连接
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    alertDialog = builder.setIcon(R.drawable.bluetooth_disconnect)
                            .setTitle("连接中断")
                            .setMessage("与设备的蓝牙连接已断开")
                            .setPositiveButton("退出应用", dialogOnclickListener)
                            .setNeutralButton("重新连接", dialogOnclickListener)
                            .create();
                    alertDialog.show();
                    break;
                case 3:
                    // 重连成功
                    try {
                        if (alertDialog != null) {
                            alertDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case 4:
                    // 重连失败
                    try {
                        if (alertDialog != null) {
                            alertDialog.setMessage("重连失败，请再次尝试");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                default:
            }
        }
    };

    private final DialogInterface.OnClickListener dialogOnclickListener = (dialog, which) -> {
        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                exitAPP();
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                BleClient.getINSTANCE().reconnect();
                alertDialog.setMessage("重连中...");
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        MyViewModel viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        viewPager2 = findViewById(R.id.vp);
        tabLayout = findViewById(R.id.table_layout);
        tabLayout.setBackgroundColor(Color.parseColor("#D3D3D3"));

        initPager();
        initTabLayout();

        MessageUtils.getINSTANCE()
                .setViewModel(viewModel)
                .setContext(getApplicationContext())
                .setHandler(handler);

        BleClient.getINSTANCE().setHandler_main_activity(handler);
    }

    public void initPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ConfigurationFragment.newInstance());
        fragments.add(ScanFragment.newInstance());
        // 模拟服务器功能
//        fragments.add(ServerSimulationFragment.newInstance());
        MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        viewPager2.setAdapter(fragmentPagerAdapter);

/*        ArrayList<String> stringArrayList = new ArrayList<>();
        stringArrayList.add("enen");
        stringArrayList.add("hehe");
        MyViewPagerAdapter viewPagerAdapter = new MyViewPagerAdapter(stringArrayList);
        viewPager2.setAdapter(viewPagerAdapter);*/

        // 监听viewPager2滑动 设置tab选中
        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tabLayout.selectTab(tabLayout.getTabAt(position));
            }
        });
    }

    public void initTabLayout() {
        // 设置标题
        for (String tab : tabs) {
            tabLayout.addTab(tabLayout.newTab().setText(tab));
        }

        // 监听tabLayout事件 设置选中的viewpager2
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition(), false);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mIsExit) {
                exitAPP();
            } else {
                Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                mIsExit = true;
                new Handler().postDelayed(() -> mIsExit = false, 2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exitAPP() {
        // 关闭蓝牙客户端
        BleClient.getINSTANCE().destroy();
        // 关闭app
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();
        for (ActivityManager.AppTask appTask : appTaskList) {
            appTask.finishAndRemoveTask();
        }
    }
}