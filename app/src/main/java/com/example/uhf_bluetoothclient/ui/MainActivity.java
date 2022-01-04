package com.example.uhf_bluetoothclient.ui;

import android.graphics.Color;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.example.uhf_bluetoothclient.R;
import com.example.uhf_bluetoothclient.adapter.MyFragmentPagerAdapter;
import com.example.uhf_bluetoothclient.databinding.ActivityMainBinding;
import com.example.uhf_bluetoothclient.util.MessageUtils;
import com.example.uhf_bluetoothclient.viewmodel.MyViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private final String[] tabs = {"配置", "扫描"};
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;
    private MyViewModel viewModel;

    @Override
    public void initView() {
        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);
        binding.setVm(viewModel);
        binding.setLifecycleOwner(this);

        viewPager2 = findViewById(R.id.vp);
        tabLayout = findViewById(R.id.table_layout);
        tabLayout.setBackgroundColor(Color.parseColor("#D3D3D3"));

        initPager();
        initTabLayout();
    }

    @Override
    public void initOthers() {
        MessageUtils.getINSTANCE()
                .setViewModel(viewModel);
    }

    public void initPager() {
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(ConfigurationFragment.newInstance());
        fragments.add(ScanFragment.newInstance());
        // 模拟服务器功能
//        fragments.add(ServerSimulationFragment.newInstance());
        MyFragmentPagerAdapter fragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager(), getLifecycle(), fragments);
        viewPager2.setAdapter(fragmentPagerAdapter);

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
}