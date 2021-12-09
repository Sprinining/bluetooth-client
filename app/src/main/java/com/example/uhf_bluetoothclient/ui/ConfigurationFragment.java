package com.example.uhf_bluetoothclient.ui;

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
import com.example.uhf_bluetoothclient.util.MessageUtils;
import com.example.uhf_bluetoothclient.viewmodel.MyViewModel;

public class ConfigurationFragment extends Fragment {
    private View rootView;
    private String[] frequencyBandArray, frequencyMinArray, frequencyMaxArray, powerArray;
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

            viewModel.getScanFlag().observe(requireActivity(), aBoolean -> {
                if (aBoolean) {
                    binding.btnReadConfig.setEnabled(false);
                    binding.btnSetFrequencyBand.setEnabled(false);
                    binding.btnSetPower.setEnabled(false);
//                    binding.btnConnect.setEnabled(false);
                    binding.spinnerPower.setEnabled(false);
                    binding.spinnerFrequencyBand.setEnabled(false);
                    binding.spinnerFrequencyMin.setEnabled(false);
                    binding.spinnerFrequencyMax.setEnabled(false);
                } else {
                    binding.btnReadConfig.setEnabled(true);
                    binding.btnSetFrequencyBand.setEnabled(true);
                    binding.btnSetPower.setEnabled(true);
//                    binding.btnConnect.setEnabled(true);
                    binding.spinnerPower.setEnabled(true);
                    binding.spinnerFrequencyBand.setEnabled(true);
                    binding.spinnerFrequencyMin.setEnabled(true);
                    binding.spinnerFrequencyMax.setEnabled(true);
                }
            });

            binding.btnReadConfig.setOnClickListener(v -> {
                MessageUtils.getINSTANCE().getFrequencyHopTableIndex();
                MessageUtils.getINSTANCE().getFrequencyBandIndex();
                MessageUtils.getINSTANCE().getPowerIndex();
                MessageUtils.getINSTANCE().getReaderInfo();
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
        }

        return rootView;
    }
}