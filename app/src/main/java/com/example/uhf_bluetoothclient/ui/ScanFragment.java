package com.example.uhf_bluetoothclient.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.uhf_bluetoothclient.BR;
import com.example.uhf_bluetoothclient.R;
import com.example.uhf_bluetoothclient.adapter.TagInfoRecyclerViewAdapter;
import com.example.uhf_bluetoothclient.databinding.FragmentScanBinding;
import com.example.uhf_bluetoothclient.entity.TagCells;
import com.example.uhf_bluetoothclient.util.MessageProtocolUtils;
import com.example.uhf_bluetoothclient.util.MessageUtils;
import com.example.uhf_bluetoothclient.viewmodel.MyViewModel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;

@SuppressLint("NotifyDataSetChanged")
public class ScanFragment extends BaseFragment<FragmentScanBinding, MyViewModel> {
    private TagCells tagCells;
    private RecyclerView recyclerView;
    private volatile boolean run_flag = false;
    private TagInfoRecyclerViewAdapter tagInfoRecyclerViewAdapter;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    public ScanFragment() {
    }

    @Override
    public void initData() {
        tagCells = viewModel.getTagCellsMutableLiveData().getValue();
    }

    @Override
    public void initView() {
        // 初始化recyclerView
        tagInfoRecyclerViewAdapter = new TagInfoRecyclerViewAdapter(requireContext(), tagCells.getTagCells());
        recyclerView = binding.rvTaginfo;
        recyclerView.setAdapter(tagInfoRecyclerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        // 为spinner设置数据源
        String[] sessionArray = getResources().getStringArray(R.array.array_spinner_session);
        ArrayAdapter<String> sessionAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sessionArray);
        sessionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerSession.setAdapter(sessionAdapter);
    }

    @Override
    public void initObserver() {
        viewModel.getCountLiveData().observe(requireActivity(), integer -> {
            TextView textView = binding.tvTagCount;
            textView.setText(String.valueOf(integer));
        });

        viewModel.getTagCellsMutableLiveData().observe(requireActivity(), tagCells -> {
            tagInfoRecyclerViewAdapter.notifyDataSetChanged();
            recyclerView.scrollToPosition(tagCells.getTagCells().size() - 1);
        });

        viewModel.getCountLiveData().observe(requireActivity(), integer -> binding.tvTagCount.setText(String.valueOf(integer)));

        viewModel.getRunTimeLiveData().observe(requireActivity(), aLong -> binding.tvRunTime.setText(String.valueOf(aLong)));

        // 监听是否在寻卡中
        viewModel.getScanFlag().observe(requireActivity(), aBoolean -> {
            if (aBoolean) {
                // 修改按钮文字，并禁用其他功能
                binding.btnScan.setText("停止");
                binding.spinnerSession.setEnabled(false);
                binding.checkBoxAntenna1.setEnabled(false);
                binding.checkBoxAntenna2.setEnabled(false);
                binding.checkBoxAntenna3.setEnabled(false);
                binding.checkBoxAntenna4.setEnabled(false);
                binding.checkBoxAntenna5.setEnabled(false);
                binding.checkBoxAntenna6.setEnabled(false);
                binding.checkBoxAntenna7.setEnabled(false);
                binding.checkBoxAntenna8.setEnabled(false);
                binding.btnClear.setEnabled(false);
            } else {
                binding.btnScan.setText("扫描");
                binding.spinnerSession.setEnabled(true);
                binding.checkBoxAntenna1.setEnabled(true);
                binding.checkBoxAntenna2.setEnabled(true);
                binding.checkBoxAntenna3.setEnabled(true);
                binding.checkBoxAntenna4.setEnabled(true);
                binding.checkBoxAntenna5.setEnabled(true);
                binding.checkBoxAntenna6.setEnabled(true);
                binding.checkBoxAntenna7.setEnabled(true);
                binding.checkBoxAntenna8.setEnabled(true);
                binding.btnClear.setEnabled(true);
            }
        });
    }

    @Override
    public void initClick() {
        // 扫描事件
        binding.btnScan.setOnClickListener(v -> {
            if (viewModel.getScanFlag().getValue() == null || viewModel.getScanFlag().getValue()) {
                // 扫描中，点击后就是停止扫描
                viewModel.getScanFlag().postValue(false);
                run_flag = false;
                scheduledThreadPoolExecutor.shutdown();
                MessageUtils.getINSTANCE().stopInventory();
            } else {
                // 扫描前判断
                // 天线启用
                boolean[] ants = new boolean[8];
                ants[0] = binding.checkBoxAntenna1.isChecked();
                ants[1] = binding.checkBoxAntenna2.isChecked();
                ants[2] = binding.checkBoxAntenna3.isChecked();
                ants[3] = binding.checkBoxAntenna4.isChecked();
                ants[4] = binding.checkBoxAntenna5.isChecked();
                ants[5] = binding.checkBoxAntenna6.isChecked();
                ants[6] = binding.checkBoxAntenna7.isChecked();
                ants[7] = binding.checkBoxAntenna8.isChecked();
                int antenna = MessageProtocolUtils.antAryToByte(ants);
                if (antenna == 0) {
                    Toast.makeText(requireContext(), "请启用天线端口", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 没扫描，点击后就是开始扫描
                viewModel.getScanFlag().postValue(true);
                run_flag = true;
                long startTime = System.currentTimeMillis();
                scheduledThreadPoolExecutor = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);
                scheduledThreadPoolExecutor.execute(() -> {
                    while (run_flag) {
                        viewModel.getRunTimeLiveData().postValue(System.currentTimeMillis() - startTime);
                    }
                });
                MessageUtils.getINSTANCE().startInventory(antenna, binding.spinnerSession.getSelectedItemPosition());
            }
        });

        binding.btnClear.setOnClickListener(v -> {
            // 重置界面
            viewModel.getCountLiveData().postValue(0);
            viewModel.getRunTimeLiveData().postValue(0L);
            if (viewModel.getTagCellsMutableLiveData().getValue() != null) {
                viewModel.getTagCellsMutableLiveData().getValue().clear();
            }
            tagInfoRecyclerViewAdapter.notifyDataSetChanged();
        });
    }

    @Override
    public int initLayout(Bundle bundle) {
        return R.layout.fragment_scan;
    }

    @Override
    public int initBR() {
        return BR.scan_vm;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.getScanFlag().postValue(false);
        run_flag = false;
        scheduledThreadPoolExecutor.shutdown();
        MessageUtils.getINSTANCE().stopInventory();
    }
}
