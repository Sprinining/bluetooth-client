package com.example.uhf_bluetoothclient.ui;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String[] tabs = {"配置", "扫描"};
    private static final String TAG = MainActivity.class.getSimpleName();
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private boolean mIsExit;
    private final Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            switch (msg.what) {
                case 1:
                    String str = msg.getData().getString("toast");
                    Toast.makeText(MainActivity.this, str, Toast.LENGTH_SHORT).show();
                    break;
                default:
            }
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

        initPager();
        initTabLayout();

        MessageUtils.getINSTANCE()
                .setViewModel(viewModel)
                .setContext(getApplicationContext())
                .setHandler(handler);
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
                BleClient.getINSTANCE().destroy();
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
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.AppTask> appTaskList = activityManager.getAppTasks();
        for (ActivityManager.AppTask appTask : appTaskList) {
            appTask.finishAndRemoveTask();
        }
    }
}