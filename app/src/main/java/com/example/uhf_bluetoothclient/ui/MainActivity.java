package com.example.uhf_bluetoothclient.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import androidx.databinding.library.baseAdapters.BR;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.uhf_bluetoothclient.R;
import com.example.uhf_bluetoothclient.adapter.MyFragmentPagerAdapter;
import com.example.uhf_bluetoothclient.databinding.ActivityMainBinding;
import com.example.uhf_bluetoothclient.util.MessageUtils;
import com.example.uhf_bluetoothclient.viewmodel.MyViewModel;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity<ActivityMainBinding, MyViewModel> {

    private final String[] tabs = {"配置", "扫描"};
    private ViewPager2 viewPager2;
    private TabLayout tabLayout;

    @Override
    public void initView() {
        initPager();
        initTabLayout();
    }

    @Override
    public void initOthers() {
        MessageUtils.getINSTANCE()
                .setViewModel(viewModel);
    }

    @Override
    public int initLayout(Bundle savedInstanceState) {
        return R.layout.activity_main;
    }

    @Override
    public int initBR() {
        return BR.vm;
    }

    public void initPager() {
        viewPager2 = findViewById(R.id.vp);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ConfigurationFragment());
        fragments.add(new ScanFragment());
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
        tabLayout = findViewById(R.id.table_layout);
        tabLayout.setBackgroundColor(Color.parseColor("#D3D3D3"));
        // 设置标题
        for (String tab : tabs) {
            TextView textView = new TextView(this);
            textView.setText(tab);
            textView.setTextSize(24);
            textView.setGravity(Gravity.CENTER);
            tabLayout.addTab(tabLayout.newTab().setCustomView(textView));
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