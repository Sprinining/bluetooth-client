package com.example.uhf_bluetoothclient.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class BaseFragment<T extends ViewDataBinding, V extends ViewModel> extends Fragment {
    private View rootView;
    protected T binding;
    protected V viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Type superClass = getClass().getGenericSuperclass();
        Class<V> vClass = (Class<V>) ((ParameterizedType) superClass).getActualTypeArguments()[1];
        viewModel = new ViewModelProvider(requireActivity()).get(vClass);

        initData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (rootView == null) {
            binding = DataBindingUtil.inflate(inflater, initLayout(savedInstanceState), container, false);
            rootView = binding.getRoot();
            binding.setLifecycleOwner(requireActivity());
            binding.setVariable(initBR(), viewModel);
        }

        initView();
        initObserver();
        initClick();
        return rootView;
    }

    public abstract void initData();

    public abstract void initView();

    public abstract void initObserver();

    public abstract void initClick();

    public abstract int initLayout(Bundle bundle);

    public abstract int initBR();

}
