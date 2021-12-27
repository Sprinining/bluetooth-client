package com.example.uhf_bluetoothclient.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.uhf_bluetoothclient.R;
import com.example.uhf_bluetoothclient.adapter.TagInfoRecyclerViewAdapter;
import com.example.uhf_bluetoothclient.entity.TagCells;
import com.example.uhf_bluetoothclient.util.MessageProtocolUtils;
import com.example.uhf_bluetoothclient.util.MessageUtils;
import com.example.uhf_bluetoothclient.viewmodel.MyViewModel;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;

public class ScanFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = ScanFragment.class.getSimpleName();
    private View rootView;

    private String mParam1;
    private Spinner spinner_session;
    private TextView tv_count, tv_runTime;
    private CheckBox checkBox_antenna1,
            checkBox_antenna2,
            checkBox_antenna3,
            checkBox_antenna4,
            checkBox_antenna5,
            checkBox_antenna6,
            checkBox_antenna7,
            checkBox_antenna8;
    private TagCells tagCells;
    private Button btn_scan, btn_clear;
    private MyViewModel viewModel;
    private volatile boolean run_flag = false;
    private ScheduledFuture<?> scheduledFuture;
    private TagInfoRecyclerViewAdapter tagInfoRecyclerViewAdapter;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor;

    public ScanFragment() {
    }

    public static ScanFragment newInstance() {
        /*        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);*/
        return new ScanFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        tagCells = viewModel.getTagCellsMutableLiveData().getValue();
    }


    @SuppressLint("NotifyDataSetChanged")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (rootView == null) {
//            FragmentScanBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_scan, container, false);
//            FragmentScanBinding binding = FragmentScanBinding.inflate(getLayoutInflater());
//            rootView = binding.getRoot();
//            binding.setVm(viewModel);
//            binding.setLifecycleOwner(requireActivity());

            rootView = inflater.inflate(R.layout.fragment_scan, container, false);

            spinner_session = rootView.findViewById(R.id.spinner_session);
            tv_count = rootView.findViewById(R.id.tv_tag_count);
            tv_runTime = rootView.findViewById(R.id.tv_runTime);
            btn_scan = rootView.findViewById(R.id.btn_scan);
            btn_clear = rootView.findViewById(R.id.btn_clear);
            checkBox_antenna1 = rootView.findViewById(R.id.checkBox_antenna1);
            checkBox_antenna2 = rootView.findViewById(R.id.checkBox_antenna2);
            checkBox_antenna3 = rootView.findViewById(R.id.checkBox_antenna3);
            checkBox_antenna4 = rootView.findViewById(R.id.checkBox_antenna4);
            checkBox_antenna5 = rootView.findViewById(R.id.checkBox_antenna5);
            checkBox_antenna6 = rootView.findViewById(R.id.checkBox_antenna6);
            checkBox_antenna7 = rootView.findViewById(R.id.checkBox_antenna7);
            checkBox_antenna8 = rootView.findViewById(R.id.checkBox_antenna8);


            tagCells = viewModel.getTagCellsMutableLiveData().getValue();
//            StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
            LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
            tagInfoRecyclerViewAdapter = new TagInfoRecyclerViewAdapter(requireContext(), tagCells.getTagCells());
            RecyclerView recyclerView = rootView.findViewById(R.id.rv_taginfo);
            recyclerView.setAdapter(tagInfoRecyclerViewAdapter);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

            // 暂时直接用observer
            viewModel.getCountLiveData().observe(requireActivity(), integer -> {
                TextView textView = rootView.findViewById(R.id.tv_tag_count);
                textView.setText(String.valueOf(integer));
            });
            viewModel.getTagCellsMutableLiveData().observe(requireActivity(), tagCells -> {
                tagInfoRecyclerViewAdapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(tagCells.getTagCells().size() - 1);
            });
            viewModel.getCountLiveData().observe(requireActivity(), integer -> tv_count.setText(String.valueOf(integer)));
            viewModel.getRunTimeLiveData().observe(requireActivity(), aLong -> tv_runTime.setText(String.valueOf(aLong)));

            // 为spinner设置数据源
            String[] sessionArray = getResources().getStringArray(R.array.array_spinner_session);
            ArrayAdapter<String> sessionAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, sessionArray);
            sessionAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_session.setAdapter(sessionAdapter);

            // 监听是否在寻卡中
            viewModel.getScanFlag().observe(requireActivity(), aBoolean -> {
                if (aBoolean) {
                    // 修改按钮文字，并禁用其他功能
                    btn_scan.setText("停止");
                    spinner_session.setEnabled(false);
                    checkBox_antenna1.setEnabled(false);
                    checkBox_antenna2.setEnabled(false);
                    checkBox_antenna3.setEnabled(false);
                    checkBox_antenna4.setEnabled(false);
                    checkBox_antenna5.setEnabled(false);
                    checkBox_antenna6.setEnabled(false);
                    checkBox_antenna7.setEnabled(false);
                    checkBox_antenna8.setEnabled(false);
                    btn_clear.setEnabled(false);
                } else {
                    btn_scan.setText("扫描");
                    spinner_session.setEnabled(true);
                    checkBox_antenna1.setEnabled(true);
                    checkBox_antenna2.setEnabled(true);
                    checkBox_antenna3.setEnabled(true);
                    checkBox_antenna4.setEnabled(true);
                    checkBox_antenna5.setEnabled(true);
                    checkBox_antenna6.setEnabled(true);
                    checkBox_antenna7.setEnabled(true);
                    checkBox_antenna8.setEnabled(true);
                    btn_clear.setEnabled(true);
                }
            });

            // 扫描事件
            btn_scan.setOnClickListener(v -> {
                if (viewModel.getScanFlag().getValue() == null || viewModel.getScanFlag().getValue()) {
                    // 扫描中，点击后就是停止扫描
                    viewModel.getScanFlag().postValue(false);
                    run_flag = false;
                    if (scheduledFuture != null) {
                        scheduledFuture.cancel(true);
                        Log.i(TAG, "onCreateView: 停止扫描");
                        scheduledThreadPoolExecutor.shutdown();
                    }
                    MessageUtils.getINSTANCE().stopInventory();
                } else {
                    // 扫描前判断
                    // 天线启用
                    boolean[] ants = new boolean[8];
                    ants[0] = checkBox_antenna1.isChecked();
                    ants[1] = checkBox_antenna2.isChecked();
                    ants[2] = checkBox_antenna3.isChecked();
                    ants[3] = checkBox_antenna4.isChecked();
                    ants[4] = checkBox_antenna5.isChecked();
                    ants[5] = checkBox_antenna6.isChecked();
                    ants[6] = checkBox_antenna7.isChecked();
                    ants[7] = checkBox_antenna8.isChecked();
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
                    Log.i(TAG, "onCreateView: 开始扫描");
                    MessageUtils.getINSTANCE().startInventory(antenna, spinner_session.getSelectedItemPosition());

/*                    // 模拟收到数据
                    scheduledFuture = scheduledThreadPoolExecutor.scheduleAtFixedRate(() -> {
                        List<LogBaseEpcInfo> list = new ArrayList<>();
                        LogBaseEpcInfo logBaseEpcInfo = new LogBaseEpcInfo();
                        logBaseEpcInfo.setEpc("xixi");
                        list.add(logBaseEpcInfo);
                        viewModel.updateTagList(list);
                    }, 0, 10, TimeUnit.MILLISECONDS);*/
                }
            });

            btn_clear.setOnClickListener(v -> {
                // 重置界面
                viewModel.getCountLiveData().postValue(0);
                viewModel.getRunTimeLiveData().postValue(0L);
                if (viewModel.getTagCellsMutableLiveData().getValue() != null) {
                    viewModel.getTagCellsMutableLiveData().getValue().clear();
                }
                tagInfoRecyclerViewAdapter.notifyDataSetChanged();
                Log.i(TAG, "onCreateView: 重置界面");
            });
        }
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.getScanFlag().postValue(false);
        run_flag = false;
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            Log.i(TAG, "onDestroy: 停止扫描");
            scheduledThreadPoolExecutor.shutdown();
        }
        MessageUtils.getINSTANCE().stopInventory();
    }
}
