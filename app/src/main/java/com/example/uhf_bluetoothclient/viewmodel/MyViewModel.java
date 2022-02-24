package com.example.uhf_bluetoothclient.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uhf_bluetoothclient.entity.LogBaseEpcInfo;
import com.example.uhf_bluetoothclient.entity.NetworkStateBean;
import com.example.uhf_bluetoothclient.entity.TagCells;

import java.util.List;

public class MyViewModel extends ViewModel {
    private static final String TAG = MyViewModel.class.getSimpleName();
    // 执行结果
    private final MutableLiveData<String> executeResult = new MutableLiveData<>("");
    // 版本号
    private final MutableLiveData<String> version = new MutableLiveData<>("");
    // 频段下标
    private final MutableLiveData<Integer> frequencyBandIndex = new MutableLiveData<>(0);
    // 最小频点下标
    private final MutableLiveData<Integer> frequencyMinIndex = new MutableLiveData<>(0);
    // 最大频点下标
    private final MutableLiveData<Integer> frequencyMaxIndex = new MutableLiveData<>(49);
    // 功率下标
    private final MutableLiveData<Integer> powerIndex = new MutableLiveData<>(33);
    // SN
    private final MutableLiveData<String> sn = new MutableLiveData<>("");
    // networkType
    private final MutableLiveData<String> networkType = new MutableLiveData<String>("");
    // ipv4
    private final MutableLiveData<String> ipv4 = new MutableLiveData<String>("");
    // ipv6
    private final MutableLiveData<String> ipv6 = new MutableLiveData<String>("");
    // networkType
    private final MutableLiveData<String> netMask = new MutableLiveData<String>("");
    // gateWay
    private final MutableLiveData<String> gateWay = new MutableLiveData<String>("");
    // dns1
    private final MutableLiveData<String> dns1 = new MutableLiveData<String>("");
    // dns2
    private final MutableLiveData<String> dns2 = new MutableLiveData<String>("");
    // mac
    private final MutableLiveData<String> mac = new MutableLiveData<String>("");
    // 是否在扫描中
    private final MutableLiveData<Boolean> scanFlag = new MutableLiveData<>(false);
    // 标签列表
    private final MutableLiveData<TagCells> tagCellsMutableLiveData = new MutableLiveData<>(new TagCells());
    // 扫描总次数
    private final MutableLiveData<Integer> countLiveData = new MutableLiveData<>(0);
    // 扫描时长
    private final MutableLiveData<Long> runTimeLiveData = new MutableLiveData<>(0L);

    public MutableLiveData<Integer> getCountLiveData() {
        return countLiveData;
    }

    public MutableLiveData<TagCells> getTagCellsMutableLiveData() {
        return tagCellsMutableLiveData;
    }

    public MutableLiveData<String> getIpv4() {
        return ipv4;
    }

    public MutableLiveData<String> getIpv6() {
        return ipv6;
    }

    public MutableLiveData<String> getNetworkType() {
        return networkType;
    }

    public MutableLiveData<String> getNetMask() {
        return netMask;
    }

    public MutableLiveData<String> getGateWay() {
        return gateWay;
    }

    public MutableLiveData<String> getDns1() {
        return dns1;
    }

    public MutableLiveData<String> getDns2() {
        return dns2;
    }

    public MutableLiveData<String> getMac() {
        return mac;
    }

    public MutableLiveData<Long> getRunTimeLiveData() {
        return runTimeLiveData;
    }

    public MutableLiveData<String> getVersion() {
        return version;
    }

    public MutableLiveData<Integer> getFrequencyMinIndex() {
        return frequencyMinIndex;
    }

    public MutableLiveData<Integer> getFrequencyMaxIndex() {
        return frequencyMaxIndex;
    }

    public MutableLiveData<String> getSn() {
        return sn;
    }

    public MutableLiveData<Integer> getFrequencyBandIndex() {
        return frequencyBandIndex;
    }

    public MutableLiveData<Integer> getPowerIndex() {
        return powerIndex;
    }

    public MutableLiveData<String> getExecuteResult() {
        return executeResult;
    }

    public MutableLiveData<Boolean> getScanFlag() {
        return scanFlag;
    }

    public void updateTagList(List<LogBaseEpcInfo> list) {
        // 获取已有的
        TagCells tagCells = tagCellsMutableLiveData.getValue();
        if (tagCells != null) {
            for (int i = 0; i < list.size(); i++) {
                // 在原有的数据上追加新的list
                tagCells.addTagCell(list.get(i));
            }
//            Log.e(TAG, "updateTagList: " + tagCells.toString());
            // 更新数据
            tagCellsMutableLiveData.postValue(tagCells);
            // 更新总扫描次数
            countLiveData.postValue(tagCells.getEpcMap().size());
        }
    }
}
