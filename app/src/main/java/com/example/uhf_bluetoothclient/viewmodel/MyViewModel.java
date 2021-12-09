package com.example.uhf_bluetoothclient.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.uhf_bluetoothclient.entity.LogBaseEpcInfo;
import com.example.uhf_bluetoothclient.util.BleClient;
import com.example.uhf_bluetoothclient.util.MessageUtils;

import java.util.ArrayList;
import java.util.List;

public class MyViewModel extends ViewModel {
    // 连接状态
//    private final MutableLiveData<String> connectState = new MutableLiveData<>("蓝牙未连接");
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

    // 是否在扫描中
    private final MutableLiveData<Boolean> scanFlag = new MutableLiveData<>(false);
    // 标签列表
    private List<LogBaseEpcInfo> tagInfoList;
    private final MutableLiveData<List<LogBaseEpcInfo>> tagInfoListLiveData = new MutableLiveData<>(new ArrayList<>());
    private final MutableLiveData<Integer> countLiveData = new MutableLiveData<>(0);
    private final MutableLiveData<Long> runTimeLiveData = new MutableLiveData<>(0L);

//    private final TagInfoRecyclerViewAdapter tagInfoRecyclerViewAdapter;
//    private final StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);


    public MyViewModel() {
/*        tagInfoList =  new ArrayList<>();
        LogBaseEpcInfo logBaseEpcInfo = new LogBaseEpcInfo();
        logBaseEpcInfo.setEpc("hh");
        tagInfoList.add(logBaseEpcInfo);
        tagInfoListLiveData.postValue(tagInfoList);
        tagInfoRecyclerViewAdapter = new TagInfoRecyclerViewAdapter(tagInfoList);*/
    }

    public MutableLiveData<Integer> getCountLiveData() {
        return countLiveData;
    }

/*    public TagInfoRecyclerViewAdapter getTagInfoRecyclerViewAdapter() {
        return tagInfoRecyclerViewAdapter;
    }

    public StaggeredGridLayoutManager getStaggeredGridLayoutManager() {
        return staggeredGridLayoutManager;
    }*/

//    public MutableLiveData<String> getConnectState() {
//        return connectState;
//    }

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

    public MutableLiveData<List<LogBaseEpcInfo>> getTagInfoListLiveData() {
        return tagInfoListLiveData;
    }

    // 获取配置信息
    public void readConfigOnClick() {
        MessageUtils.getINSTANCE().getFrequencyBandIndex();
        MessageUtils.getINSTANCE().getFrequencyHopTableIndex();
        MessageUtils.getINSTANCE().getPowerIndex();
        MessageUtils.getINSTANCE().getReaderInfo();
    }

    // 设置功率
    public void setPowerOnClick() {
        if (powerIndex.getValue() == null) {
            MessageUtils.getINSTANCE().setPower(0);
        } else {
            MessageUtils.getINSTANCE().setPower(powerIndex.getValue());
        }
    }

    // 设置频段
    public void setFrequencyBandOnClick() {
        if (frequencyBandIndex.getValue() == null) {
            MessageUtils.getINSTANCE().setFrequencyBand(0);
        } else {
            MessageUtils.getINSTANCE().setFrequencyBand(frequencyBandIndex.getValue());
        }
        if (frequencyMinIndex.getValue() != null && frequencyMaxIndex.getValue() != null) {
            MessageUtils.getINSTANCE().setFrequencyHopTableIndex(frequencyMinIndex.getValue(), frequencyMaxIndex.getValue());
        }
    }

    // 启动客户端
/*    public void startClientOnClick() {
        BleClient.getINSTANCE().scan();
    }*/

    public void updateTagList(List<LogBaseEpcInfo> list) {
        // 在原有的数据上追加新的list
        tagInfoList = tagInfoListLiveData.getValue();

        for (LogBaseEpcInfo logBaseEpcInfo : list) {
            if (tagInfoList != null) {
                tagInfoList.add(logBaseEpcInfo);
            }
        }

/*        LogBaseEpcInfo logBaseEpcInfo = new LogBaseEpcInfo();
        logBaseEpcInfo.setEpc("hh");
        tagInfoList.add(logBaseEpcInfo);
        tagInfoList.add(logBaseEpcInfo);
        tagInfoList.add(logBaseEpcInfo);*/
        tagInfoListLiveData.postValue(tagInfoList);
        countLiveData.postValue(tagInfoList.size());
    }
}
