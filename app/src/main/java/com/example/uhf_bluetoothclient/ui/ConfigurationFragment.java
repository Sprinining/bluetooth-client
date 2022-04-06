package com.example.uhf_bluetoothclient.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.uhf_bluetoothclient.BR;
import com.example.uhf_bluetoothclient.R;
import com.example.uhf_bluetoothclient.databinding.FragmentConfigurationBinding;
import com.example.uhf_bluetoothclient.util.MessageUtils;
import com.example.uhf_bluetoothclient.viewmodel.MyViewModel;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.seuic.util.common.StringUtils;
import com.seuic.util.common.ToastUtils;

public class ConfigurationFragment extends BaseFragment<FragmentConfigurationBinding, MyViewModel> {
    private String[] frequencyBandArray, frequencyMinArray, frequencyMaxArray, powerArray, ipModeArray;

    public ConfigurationFragment() {
    }

    @Override
    public void initData() {
        // 初始化spinner数组
        frequencyBandArray = getResources().getStringArray(R.array.array_spinner_frequency_band);
        frequencyMinArray = getResources().getStringArray(R.array.array_spinner_frequency_min);
        frequencyMaxArray = getResources().getStringArray(R.array.array_spinner_frequency_max);
        ipModeArray = getResources().getStringArray(R.array.array_spinner_ip_mode);
        powerArray = new String[34];
        for (int i = 0; i <= 33; i++) {
            powerArray[i] = String.valueOf(i);
        }
    }

    @Override
    public void initView() {
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
    }

    @Override
    public void initObserver() {
        viewModel.getMode().observe(requireActivity(), integer -> {
            if (integer == 0) {
                // static
                binding.edtIpv4Netmask.setEnabled(true);
                binding.edtIpv4Gateway.setEnabled(true);
                binding.edtIpv4Dns1.setEnabled(true);
                binding.edtIpv4Dns2.setEnabled(true);
            } else if (integer == 1) {
                // dhcp
                binding.edtIpv4Netmask.setEnabled(false);
                binding.edtIpv4Gateway.setEnabled(false);
                binding.edtIpv4Dns1.setEnabled(false);
                binding.edtIpv4Dns2.setEnabled(false);
            }
        });

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
    }

    @Override
    public void initClick() {
        binding.spinnerIpv4Mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                viewModel.getMode().postValue(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        binding.btnTestPing.setOnClickListener(v -> {
            MessageUtils.getINSTANCE().testPing(binding.edtPingAddress.getText().toString());
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
        });

        binding.btnSetFrequencyBand.setOnClickListener(v -> {
            if (binding.spinnerFrequencyMin.getSelectedItemPosition() >= binding.spinnerFrequencyMax.getSelectedItemPosition()) {
                Toast.makeText(requireContext(), "请选择正确的频点范围", Toast.LENGTH_SHORT).show();
                return;
            }
            MessageUtils.getINSTANCE().setFrequencyBand(binding.spinnerFrequencyBand.getSelectedItemPosition());
            MessageUtils.getINSTANCE().setFrequencyHopTableIndex(binding.spinnerFrequencyMin.getSelectedItemPosition(), binding.spinnerFrequencyMax.getSelectedItemPosition());
        });
        binding.btnAutoFillIpv4.setOnClickListener(v -> {
            if (binding.edtIpv4Address.length() <= 0) {
                binding.edtIpv4Address.setText("192.168.1.100");
            }
            if (binding.edtIpv4Netmask.length() <= 0) {
                binding.edtIpv4Netmask.setText("255.255.255.0");
            }
            if (binding.edtIpv4Gateway.length() <= 0) {
                binding.edtIpv4Gateway.setText("192.168.1.1");
            }
            if (binding.edtIpv4Dns1.length() <= 0) {
                binding.edtIpv4Dns1.setText("4.4.4.4");
            }
            if (binding.edtIpv4Dns2.length() <= 0) {
                binding.edtIpv4Dns2.setText("8.8.8.8");
            }
            binding.spinnerIpv4Mode.setSelection(0);
        });

        binding.btnSetIpv4.setOnClickListener(v -> {

            String mode = "STATIC";
            switch (binding.spinnerIpv4Mode.getSelectedItemPosition()) {
                case 0:
                    mode = "STATIC";
                    break;
                case 1:
                    mode = "DHCP";
                    break;
            }

            MessageUtils.getINSTANCE().setIPv4(
                    mode,
                    binding.edtIpv4Address.getText().toString(),
                    binding.edtIpv4Netmask.getText().toString(),
                    binding.edtIpv4Gateway.getText().toString(),
                    binding.edtIpv4Dns1.getText().toString(),
                    binding.edtIpv4Dns2.getText().toString()
            );
        });

        binding.btnSetIpv6.setOnClickListener(v -> {
            if (StringUtils.isTrimEmpty(viewModel.getNetworkType().getValue())) {
                ToastUtils.showShort("请先读取设备信息！");
                return;
            }
            String ipv6 = binding.edtIpv6.getText().toString();
            if (!StringUtils.isTrimEmpty(ipv6) && StringUtils.isTrimEmpty(viewModel.getIpv4().getValue())) {
                ToastUtils.showShort("静态IPV6想要正常工作需要同时设置静态IPV4！");
                return;
            }
            MessageUtils.getINSTANCE().setIPv6(ipv6);
        });

        binding.btnExit.setOnClickListener(v -> MessageUtils.getINSTANCE().showExitDialog());
    }

    @Override
    public int initLayout(Bundle bundle) {
        return R.layout.fragment_configuration;
    }

    @Override
    public int initBR() {
        return BR.config_vm;
    }
}