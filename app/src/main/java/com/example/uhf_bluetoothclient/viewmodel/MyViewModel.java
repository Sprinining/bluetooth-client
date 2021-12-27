package com.example.uhf_bluetoothclient.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uhf_bluetoothclient.entity.LogBaseEpcInfo;
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
    private final MutableLiveData<Integer> powerIndex = new MutableLiveData<>(20);
    // SN
    private final MutableLiveData<String> sn = new MutableLiveData<>("");
    // ip
    private final MutableLiveData<String> ip = new MutableLiveData<>("");
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

    public MutableLiveData<String> getIp() {
        return ip;
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
            for (LogBaseEpcInfo tagInfo : list) {
                // 在原有的数据上追加新的list
                tagCells.addTagCell(tagInfo);
            }
//            Log.e(TAG, "updateTagList: " + tagCells.toString());
            // 更新数据
            tagCellsMutableLiveData.postValue(tagCells);
            // 更新总扫描次数
            countLiveData.postValue(tagCells.getEpcMap().size());
        }
    }
}
