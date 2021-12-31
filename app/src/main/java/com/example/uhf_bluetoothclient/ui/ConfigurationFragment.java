package com.example.uhf_bluetoothclient.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.uhf_bluetoothclient.R;
import com.example.uhf_bluetoothclient.databinding.FragmentConfigurationBinding;
import com.example.uhf_bluetoothclient.util.BleClient;
import com.example.uhf_bluetoothclient.util.CalibrationUtils;
import com.example.uhf_bluetoothclient.util.MessageUtils;
import com.example.uhf_bluetoothclient.viewmodel.MyViewModel;
import com.seuic.util.common.NetworkUtils;

public class ConfigurationFragment extends Fragment {
    private View rootView;
    private String[] frequencyBandArray, frequencyMinArray, frequencyMaxArray, powerArray, ipModeArray;
    private MyViewModel viewModel;

    public static ConfigurationFragment newInstance() {
        // 传参的时候用setArguments(bundle)
        return new ConfigurationFragment();
    }

    public ConfigurationFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 初始化spinner数组
        frequencyBandArray = getResources().getStringArray(R.array.array_spinner_frequency_band);
        frequencyMinArray = getResources().getStringArray(R.array.array_spinner_frequency_min);
        frequencyMaxArray = getResources().getStringArray(R.array.array_spinner_frequency_max);
        ipModeArray = getResources().getStringArray(R.array.array_spinner_ip_mode);
        powerArray = new String[34];
        for (int i = 0; i <= 33; i++) {
            powerArray[i] = String.valueOf(i);
        }
        viewModel = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 只需渲染一次
        if (rootView == null) {
            FragmentConfigurationBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_configuration, container, false);
            rootView = binding.getRoot();
            binding.setVm(viewModel);
            binding.setLifecycleOwner(requireActivity());

            // 为spinner设置数据源
            ArrayAdapter<String> frequencyBandAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, frequencyBandArray);
            frequencyBandAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerFrequencyBand.setAdapter(frequencyBandAdapter);
            ArrayAdapter<String> frequencyMinAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, frequencyMinArray);
            frequencyMinAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerFrequencyMin.setAdapter(frequencyMinAdapter);
            ArrayAdapter<String> frequencyMaxAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, frequencyMaxArray);
            frequencyMaxAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerFrequencyMax.setAdapter(frequencyMaxAdapter);
            ArrayAdapter<String> powerAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, powerArray);
            powerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerPower.setAdapter(powerAdapter);
            ArrayAdapter<String> ipModeAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, ipModeArray);
            ipModeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            binding.spinnerIpv4Mode.setAdapter(ipModeAdapter);

            viewModel.getScanFlag().observe(requireActivity(), aBoolean -> {
                if (aBoolean) {
                    binding.btnReadConfig.setEnabled(false);
                    binding.btnSetFrequencyBand.setEnabled(false);
                    binding.btnSetPower.setEnabled(false);
                    binding.spinnerPower.setEnabled(false);
                    binding.spinnerFrequencyBand.setEnabled(false);
                    binding.spinnerFrequencyMin.setEnabled(false);
                    binding.spinnerFrequencyMax.setEnabled(false);
                    binding.spinnerIpv4Mode.setEnabled(false);
                    binding.btnSetIpv4.setEnabled(false);
                    binding.btnSetIpv6.setEnabled(false);
                } else {
                    binding.btnReadConfig.setEnabled(true);
                    binding.btnSetFrequencyBand.setEnabled(true);
                    binding.btnSetPower.setEnabled(true);
                    binding.spinnerPower.setEnabled(true);
                    binding.spinnerFrequencyBand.setEnabled(true);
                    binding.spinnerFrequencyMin.setEnabled(true);
                    binding.spinnerFrequencyMax.setEnabled(true);
                    binding.spinnerIpv4Mode.setEnabled(true);
                    binding.btnSetIpv4.setEnabled(true);
                    binding.btnSetIpv6.setEnabled(true);
                }
            });

            binding.btnTestPing.setOnClickListener(v -> {
                String ipAddress = binding.edtPingAddress.getText().toString();
                if ((!CalibrationUtils.isIPv4Address(ipAddress) && !CalibrationUtils.isIPv6Address(ipAddress)) || ipAddress.equals("")) {
                    Toast.makeText(requireContext(), "ip地址格式错误", Toast.LENGTH_SHORT).show();
                } else {
                    boolean availableByPing = NetworkUtils.isAvailableByPing(ipAddress);
                    if (availableByPing) {
                        Toast.makeText(requireContext(), "ping成功", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(requireContext(), "ping失败", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            binding.btnReadConfig.setOnClickListener(v -> {
                MessageUtils.getINSTANCE().getFrequencyHopTableIndex();
                MessageUtils.getINSTANCE().getFrequencyBandIndex();
                MessageUtils.getINSTANCE().getPowerIndex();
                MessageUtils.getINSTANCE().getReaderInfo();
                MessageUtils.getINSTANCE().getIP();
            });
            binding.btnSetPower.setOnClickListener(v -> {
                MessageUtils.getINSTANCE().setPower(binding.spinnerPower.getSelectedItemPosition());
/*                List<LogBaseEpcInfo> list = new ArrayList<>();
                LogBaseEpcInfo logBaseEpcInfo = new LogBaseEpcInfo();
                logBaseEpcInfo.setEpc("xixi");
                logBaseEpcInfo.setAntId(1);
                logBaseEpcInfo.setReadTime(new Date());
                list.add(logBaseEpcInfo);
                viewModel.updateTagList(list);*/
            });
            binding.btnSetFrequencyBand.setOnClickListener(v -> {
                if (binding.spinnerFrequencyMin.getSelectedItemPosition() >= binding.spinnerFrequencyMax.getSelectedItemPosition()) {
                    Toast.makeText(requireContext(), "请选择正确的频点范围", Toast.LENGTH_SHORT).show();
                    return;
                }
                MessageUtils.getINSTANCE().setFrequencyBand(binding.spinnerFrequencyBand.getSelectedItemPosition());
                MessageUtils.getINSTANCE().setFrequencyHopTableIndex(binding.spinnerFrequencyMin.getSelectedItemPosition(), binding.spinnerFrequencyMax.getSelectedItemPosition());
/*                List<LogBaseEpcInfo> list = new ArrayList<>();
                LogBaseEpcInfo logBaseEpcInfo = new LogBaseEpcInfo();
                logBaseEpcInfo.setEpc("haha");
                logBaseEpcInfo.setAntId(1);
                logBaseEpcInfo.setReadTime(new Date());
                list.add(logBaseEpcInfo);
                viewModel.updateTagList(list);*/
            });
            binding.btnSetIpv4.setOnClickListener(v -> {
                MessageUtils.getINSTANCE().setIPv4(
                        ipModeArray[binding.spinnerIpv4Mode.getSelectedItemPosition()],
                        binding.edtIpv4Address.getText().toString(),
                        binding.edtIpv4Netmask.getText().toString(),
                        binding.edtIpv4Gateway.getText().toString(),
                        binding.edtIpv4Dns1.getText().toString(),
                        binding.edtIpv4Dns2.getText().toString()
                );
/*                List<LogBaseEpcInfo> list = new ArrayList<>();
                LogBaseEpcInfo logBaseEpcInfo = new LogBaseEpcInfo();
                logBaseEpcInfo.setEpc("xixi");
                logBaseEpcInfo.setAntId(2);
                logBaseEpcInfo.setReadTime(new Date());
                list.add(logBaseEpcInfo);
                viewModel.updateTagList(list);*/
            });
            binding.btnSetIpv6.setOnClickListener(v -> {
                MessageUtils.getINSTANCE().setIPv6(binding.edtIpv6.getText().toString());
/*                List<LogBaseEpcInfo> list = new ArrayList<>();
                LogBaseEpcInfo logBaseEpcInfo = new LogBaseEpcInfo();
                logBaseEpcInfo.setEpc("xixi");
                logBaseEpcInfo.setAntId(3);
                logBaseEpcInfo.setReadTime(new Date());
                list.add(logBaseEpcInfo);
                viewModel.updateTagList(list);*/
            });
            binding.btnExit.setOnClickListener(v -> {
                // TODO: 2021/12/31 退回设备搜索界面
                BleClient.getINSTANCE().destroy();
                Intent intent = new Intent(requireActivity(), ScanDeviceActivity.class);
                startActivity(intent);
            });
        }

        return rootView;
    }
}