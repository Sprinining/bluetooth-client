package com.example.uhf_bluetoothclient.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.example.uhf_bluetoothclient.R;
import com.example.uhf_bluetoothclient.constants.Constants;
import com.example.uhf_bluetoothclient.entity.LogBaseEpcInfo;
import com.example.uhf_bluetoothclient.entity.Message;
import com.example.uhf_bluetoothclient.entity.MsgAppGetReaderInfo;
import com.example.uhf_bluetoothclient.entity.MsgAppReset;
import com.example.uhf_bluetoothclient.entity.MsgBaseGetFreqRange;
import com.example.uhf_bluetoothclient.entity.MsgBaseGetPower;
import com.example.uhf_bluetoothclient.entity.MsgBaseInventoryEpc;
import com.example.uhf_bluetoothclient.entity.MsgBaseSetBaseband;
import com.example.uhf_bluetoothclient.entity.MsgBaseSetFreqRange;
import com.example.uhf_bluetoothclient.entity.MsgBaseSetPower;
import com.example.uhf_bluetoothclient.entity.MsgBaseStop;
import com.example.uhf_bluetoothclient.entity.MsgFrequencyHopTable;
import com.example.uhf_bluetoothclient.entity.MsgSetIPv4;
import com.example.uhf_bluetoothclient.entity.NetworkStateBean;
import com.example.uhf_bluetoothclient.viewmodel.MyViewModel;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.seuic.util.common.GsonUtils;
import com.seuic.util.common.RegexUtils;

import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class MessageUtils {
    @SuppressLint("StaticFieldLeak")
    private static volatile MessageUtils INSTANCE;
    private static final String TAG = MessageUtils.class.getSimpleName();
    private final Gson gson;
    private final BleClient bleClient;
    private MyViewModel viewModel;
    private Context context;
    private Handler handler;

    private MessageUtils() {
        gson = new Gson();
        bleClient = BleClient.getINSTANCE();
    }

    public static MessageUtils getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (MessageUtils.class) {
                if (INSTANCE == null) {
                    INSTANCE = new MessageUtils();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 对服务器返回的结果进行处理
     *
     * @param message 处理结果
     */
    public void handlerMessage(String message) {
        // 1.去尾部$
        String request = MessageProtocolUtils.removeTail(message);

        // 2.校验
        if (!isDataValid(request)) return;

        // 3.判断是否是json字符串
        if (!isJsonStr(request)) return;

        // 4.转为Message对象
        Message<?> requestMessage = gson.fromJson(request, Message.class);
        try {
            switch (requestMessage.getCode()) {
                case Constants.SET_IPV4:
                    // 设置ipv4
                    if (requestMessage.getRtCode() == 0) {
                        showToast("ipv4设置成功");
                    } else {
                        showToast("ipv4设置失败: " + requestMessage.getRtMsg());
                    }
                    break;
                case Constants.SET_IPV6:
                    // 设置ipv6
                    if (requestMessage.getRtCode() == 0) {
                        showToast("ipv6设置成功");
                    } else {
                        showToast("ipv6设置失败: " + requestMessage.getRtMsg());
                    }
                    break;
                case Constants.MSG_BASE_SET_POWER:
                    // 设置功率
                    if (requestMessage.getRtCode() == 0) {
                        showToast("功率设置成功");
                    } else {
                        showToast("功率设置失败: " + requestMessage.getRtMsg());
                    }
                    break;
                case Constants.MSG_BASE_SET_FREQUENCY_HOP_TABLE:
                    // 设置调频表
                    if (requestMessage.getRtCode() == 0) {
                        showToast("频点设置成功");
                    } else {
                        showToast("频点设置失败: " + requestMessage.getRtMsg());
                    }
                    break;
                case Constants.MSG_BASE_SET_FREQ_RANGE:
                    // 设置频段
                    if (requestMessage.getRtCode() == 0) {
                        showToast("频段设置成功");
                    } else {
                        showToast("频段设置失败: " + requestMessage.getRtMsg());
                    }
                    break;
                case Constants.TEST_PING:
                    // 测试ping
                    if (requestMessage.getRtCode() == 0) {
                        showToast("ping成功");
                    } else {
                        showToast("ping失败：" + requestMessage.getRtMsg());
                    }
                    break;
                case Constants.MSG_APP_GET_READER_INFO:
                    // 获取设备信息，包括SN和version
                    if (requestMessage.getRtCode() == 0) {
                        Map<String, Object> map = (Map<String, Object>) requestMessage.getData();
                        viewModel.getSn().postValue((String) map.get("readerSerialNumber"));
                        viewModel.getVersion().postValue((String) map.get("systemVersions"));
                        showExecuteResult(Constants.GET_SUCCESS);
                    } else {
                        showToast("获取设备信息失败: " + requestMessage.getRtMsg());
                    }
                    break;
                case Constants.MSG_BASE_GET_FREQ_RANGE:
                    // 获取频段
                    if (requestMessage.getRtCode() == 0) {
                        viewModel.getExecuteResult().postValue(Constants.GET_SUCCESS);

                        Map<String, Object> map = (Map<String, Object>) requestMessage.getData();
                        String freqRangeIndex = (String) map.get("freqRangeIndex");
                        String[] frequencyBandArray = context.getResources().getStringArray(R.array.array_spinner_frequency_band);

                        for (int i = 0; i < frequencyBandArray.length; i++) {
                            if (frequencyBandArray[i].equals(freqRangeIndex)) {
                                viewModel.getFrequencyBandIndex().postValue(i);
                                break;
                            }
                        }
                        showExecuteResult(Constants.GET_SUCCESS);
                    } else {
                        showToast("获取频段失败: " + requestMessage.getRtMsg());
                    }
                    break;
                case Constants.MSG_BASE_GET_FREQUENCY_HOP_TABLE:
                    // 获取调频表
                    if (requestMessage.getRtCode() == 0) {
                        Map<String, Object> map = (Map<String, Object>) requestMessage.getData();
                        Double d_min = (Double) map.get("minFrequencyIndex");
                        Double d_max = (Double) map.get("maxFrequencyIndex");
                        int minIndex = 0;
                        int maxIndex = 49;
                        if (d_min != null) minIndex = d_min.intValue();
                        if (d_max != null) maxIndex = d_max.intValue();
                        if (minIndex < 0 || minIndex >= 50) minIndex = 0;
                        if (maxIndex < 0 || maxIndex >= 50) maxIndex = 49;
                        viewModel.getFrequencyMinIndex().postValue(minIndex);
                        viewModel.getFrequencyMaxIndex().postValue(maxIndex);
                        showExecuteResult(Constants.GET_SUCCESS);
                    } else {
                        showToast("获取跳频表失败: " + requestMessage.getRtMsg());
                    }
                    break;
                case Constants.MSG_BASE_GET_POWER:
                    // 获取功率
                    if (requestMessage.getRtCode() == 0) {
                        viewModel.getExecuteResult().postValue(Constants.GET_SUCCESS);
                        Map<String, Object> map = (Map<String, Object>) requestMessage.getData();
                        Map<String, Double> map1 = (Map<String, Double>) map.get("dicPower");
                        Double d = map1.get("1");
                        int index = d.intValue();
                        if (index < 0) index = 0;
                        if (index > 33) index = 33;
                        viewModel.getPowerIndex().postValue(index);
                        showExecuteResult(Constants.GET_SUCCESS);
                    } else {
                        showToast("获取功率失败: " + requestMessage.getRtMsg());
                    }
                    break;
                case Constants.LOG_BASE_EPC_INFO:
                    // 接收标签
                    if (requestMessage.getRtCode() == 0) {
                        List<Object> list = (List<Object>) requestMessage.getData();
                        List<LogBaseEpcInfo> value = new ArrayList<>();
                        for (Object o : list) {
                            if (o != null) {
                                Map<String, Object> map_tag = (Map<String, Object>) o;
                                // 需要啥字段就取啥字段
                                LogBaseEpcInfo tagInfo = new LogBaseEpcInfo();
                                // 天线id
                                Double d_antennaId = (Double) map_tag.get("antId");
                                if (d_antennaId == null) {
                                    d_antennaId = 0.0;
                                }
                                int antennaId = d_antennaId.intValue();
                                // 读取时间
                                Double d_readTime = (Double) map_tag.get("readTime");
                                Date readTime = new Date(d_readTime.longValue());
                                tagInfo.setEpc((String) map_tag.get("epc"))
                                        .setAntId(antennaId)
                                        .setReadTime(readTime);
                                value.add(tagInfo);
                            }
                        }
                        viewModel.updateTagList(value);
                    }
                    break;
                case Constants.MSG_BASE_READ_EPC:
                    // 标签上报
                    if (requestMessage.getRtCode() != 0) {
                        showToast("启动寻卡失败: " + requestMessage.getRtMsg());
                    }
                    break;
                case Constants.MSG_BASE_STOP:
                    if (requestMessage.getRtCode() != 0) {
                        showToast("关闭寻卡失败: " + requestMessage.getRtMsg());
                    }
                    break;
                case Constants.MSG_BASE_SET_BASE_BAND:
                    if (requestMessage.getRtCode() != 0) {
                        showToast("设置基带参数失败: " + requestMessage.getRtMsg());
                    }
                    break;
                case Constants.GET_IP:
                    // 获取ip
                    if (requestMessage.getRtCode() == 0) {
                        Log.e(TAG, "handlerMessage: " + GsonUtils.toJson(requestMessage.getData()));
                        Map<String, Object> map = (Map<String, Object>) requestMessage.getData();
                        NetworkStateBean bean = new NetworkStateBean(
                                (String) map.get("networkType"),
                                (List<String>) map.get("iP"),
                                (String) map.get("nETMASK"),
                                (String) map.get("gATEWAY"),
                                (List<String>) map.get("dNS"),
                                (String) map.get("mAC")
                        );
                        // 网络状态
                        String netWorkType = "无网络连接";
                        switch (bean.networkType) {
                            case "NETWORK_ETHERNET":
                                netWorkType = "以太网";
                                break;
                            case "NETWORK_WIFI":
                                netWorkType = "WIFI";
                                break;
                            case "NETWORK_2G":
                                netWorkType = "2G";
                                break;
                            case "NETWORK_3G":
                                netWorkType = "3G";
                                break;
                            case "NETWORK_4G":
                                netWorkType = "4G";
                                break;
                            case "NETWORK_5G":
                                netWorkType = "5G";
                                break;
                            case "NETWORK_UNKNOWN":
                                netWorkType = "未知";
                                break;
                            case "NETWORK_NO":
                                netWorkType = "无";
                                break;
                            default:
                        }
                        viewModel.getNetworkType().postValue(netWorkType);

                        if (bean.IP != null && !bean.IP.isEmpty()) {
                            bean.IP.forEach(s -> {
                                if (RegexUtils.isIP(s)) {
                                    viewModel.getIpv4().postValue(s);
                                } else {
                                    viewModel.getIpv6().postValue(s);
                                }
                            });
                        } else {
                            viewModel.getIpv4().postValue("");
                            viewModel.getIpv6().postValue("");
                        }
                        viewModel.getNetMask().postValue(bean.NETMASK);
                        viewModel.getGateWay().postValue(bean.GATEWAY);
                        if (bean.DNS != null && !bean.DNS.isEmpty()) {
                            switch (bean.DNS.size()) {
                                case 2:
                                    viewModel.getDns2().postValue(bean.DNS.get(1));
                                case 1:
                                    viewModel.getDns1().postValue(bean.DNS.get(0));
                            }
                        } else {
                            viewModel.getDns1().postValue("");
                            viewModel.getDns2().postValue("");
                        }
                        viewModel.getMac().postValue(bean.MAC);
                        showExecuteResult(Constants.GET_SUCCESS);
                    }
                    break;
                default:
                    Log.e(TAG, "handlerMessage: " + Constants.CODE_ERROR);
            }
        } catch (Exception e) {
            Log.e(TAG, "handlerMessage: " + Constants.CMD_EXCEPTION, e);
            showToast(Constants.CMD_EXCEPTION + ": " + e);
        }
    }

    public void rebootDevice(){
        Message<MsgAppReset> message = new Message<>();
        message.setCode(Constants.MSG_APP_RESET);
        sendMessage(message);
    }

    /**
     * 1012
     *
     * @param powerIndex 功率spinner中的下标
     */
    public void setPower(int powerIndex) {
        // {"code":1012,"data":{"dicPower":{"8":0,"7":0,"6":0,"5":0,"4":0,"3":0,"2":0,"1":0}},"rtCode":-1,"rtMsg":""}
        Message<MsgBaseSetPower> message = new Message<>();
        message.setCode(Constants.MSG_BASE_SET_POWER);

        MsgBaseSetPower msgBaseSetPower = new MsgBaseSetPower();
        Hashtable<Integer, Integer> dicPower = new Hashtable<>();
        for (int i = 0; i < 8; i++) {
            dicPower.put(i + 1, powerIndex);
        }
        msgBaseSetPower.setDicPower(dicPower);
        message.setData(msgBaseSetPower);

        sendMessage(message);
    }

    /**
     * 1013
     */
    public void getPowerIndex() {
        // {"code":1013,"rtCode":-1,"rtMsg":""}
        Message<MsgBaseGetPower> message = new Message<>();
        message.setCode(Constants.MSG_BASE_GET_POWER);
        sendMessage(message);
    }


    /**
     * 1022
     *
     * @param regionIndex 频段spinner的下标
     */
    public void setFrequencyBand(int regionIndex) {
        // {"code":1022,"data":{"freqRangeIndex":"Chinese band2"},"rtCode":-1,"rtMsg":""}
        String[] frequencyBandArray = context.getResources().getStringArray(R.array.array_spinner_frequency_band);

        Message<MsgBaseSetFreqRange> message = new Message<>();
        message.setCode(Constants.MSG_BASE_SET_FREQ_RANGE);
        MsgBaseSetFreqRange msgBaseSetFreqRange = new MsgBaseSetFreqRange();
        msgBaseSetFreqRange.setFreqRangeIndex(frequencyBandArray[regionIndex]);
        message.setData(msgBaseSetFreqRange);

        sendMessage(message);
    }

    /**
     * 1023
     */
    public void getFrequencyBandIndex() {
        // {"code":1023,"rtCode":-1,"rtMsg":""}
        Message<MsgBaseGetFreqRange> message = new Message<>();
        message.setCode(Constants.MSG_BASE_GET_FREQ_RANGE);

        sendMessage(message);
    }

    /**
     * 1027
     */
    public void getFrequencyHopTableIndex() {
        Message<int[]> message = new Message<>();
        message.setCode(Constants.MSG_BASE_GET_FREQUENCY_HOP_TABLE);
        sendMessage(message);
    }

    /**
     * 1028
     */
    public void setFrequencyHopTableIndex(int minIndex, int maxIndex) {
        Message<MsgFrequencyHopTable> message = new Message<>();
        message.setCode(Constants.MSG_BASE_SET_FREQUENCY_HOP_TABLE);
        MsgFrequencyHopTable msgFrequencyHopTable = new MsgFrequencyHopTable(minIndex, maxIndex);
        message.setData(msgFrequencyHopTable);
        sendMessage(message);
    }

    /**
     * 1010
     */
    public void getReaderInfo() {
        Message<MsgAppGetReaderInfo> message = new Message<>();
        message.setCode(Constants.MSG_APP_GET_READER_INFO);
        sendMessage(message);
    }

    /**
     * 1029
     */
    public void getIP() {
        Message<String> message = new Message<>();
        message.setCode(Constants.GET_IP);
        sendMessage(message);
    }

    /**
     * 1030
     *
     * @param mode
     * @param ipAddress
     * @param netmask
     * @param gateway
     * @param dns1
     * @param dns2
     */
    public void setIPv4(String mode, String ipAddress, String netmask, String gateway, String dns1, String dns2) {
        Message<MsgSetIPv4> message = new Message<>();
        message.setCode(Constants.SET_IPV4);
        message.setData(new MsgSetIPv4(
                mode,
                ipAddress,
                netmask,
                gateway,
                dns1,
                dns2
        ));
        sendMessage(message);
    }

    /**
     * 1031
     *
     * @param iPv6
     */
    public void setIPv6(String iPv6) {
        Message<String> message = new Message<>();
        message.setCode(Constants.SET_IPV6);
        message.setData(iPv6);
        sendMessage(message);
    }

    /**
     * 1034
     *
     * @param ipAddress
     */
    public void testPing(String ipAddress) {
        if ((!CalibrationUtils.isIPv4Address(ipAddress) && !CalibrationUtils.isIPv6Address(ipAddress)) || "".equals(ipAddress)) {
            showToast("ip地址格式错误");
        } else {
            Message<String> message = new Message<>();
            message.setCode(Constants.TEST_PING);
            message.setData(ipAddress);
            sendMessage(message);
        }
    }

    /**
     * 开始寻卡
     *
     * @param antenna
     * @param session
     */
    public void startInventory(int antenna, int session) {
        // 设置session 1014
        Message<MsgBaseSetBaseband> message = new Message<>();
        message.setCode(Constants.MSG_BASE_SET_BASE_BAND);
        MsgBaseSetBaseband msgBaseSetBaseband = new MsgBaseSetBaseband();
        msgBaseSetBaseband.setSession(session);
        message.setData(msgBaseSetBaseband);
        sendMessage(message);

        // 开始盘存 1018
        Message<MsgBaseInventoryEpc> message1 = new Message<>();
        message1.setCode(Constants.MSG_BASE_READ_EPC);
        MsgBaseInventoryEpc msgBaseInventoryEpc = new MsgBaseInventoryEpc();
        msgBaseInventoryEpc.setInventoryMode(1);
        msgBaseInventoryEpc.setAntennaEnable(antenna);
        message1.setData(msgBaseInventoryEpc);
        sendMessage(message1);
    }

    /**
     * 停止寻卡
     */
    public void stopInventory() {
        Message<MsgBaseStop> message = new Message<>();
        message.setCode(Constants.MSG_BASE_STOP);
        sendMessage(message);
    }

    public void sendMessage(Message<?> message) {
        bleClient.write(MessageProtocolUtils.addTail(gson.toJson(message)).getBytes(StandardCharsets.UTF_8));
    }

    public void showToast(String str) {
        showExecuteResult(str);

        if (handler != null) {
            android.os.Message message = new android.os.Message();
            message.what = Constants.MESSAGE_WHAT_SHOW_TOAST;
            Bundle bundle = new Bundle();
            bundle.putString("toast", str);
            message.setData(bundle);
            handler.sendMessage(message);
        }
    }

    public void showExitDialog(){
        if (handler != null) {
            android.os.Message message = new android.os.Message();
            message.what = Constants.MESSAGE_WHAT_EXIT_TO_DEVICE_SEARCHING;
            handler.sendMessage(message);
        }
    }

    public void showExecuteResult(String str) {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
        viewModel.getExecuteResult().postValue(simpleDateFormat.format(date) + " " + str);
    }

    public MessageUtils setContext(Context context) {
        this.context = context;
        return this;
    }

    public MessageUtils setViewModel(MyViewModel viewModel) {
        this.viewModel = viewModel;
        return this;
    }

    /**
     * @param request 去掉尾后的数据
     * @return 头尾校验结果
     */
    public boolean isDataValid(String request) {
        if (Objects.equals(Constants.CHECK_ERROR, request)) {
            // 校验出错
            Log.e(TAG, "isDataValid: " + Constants.CHECK_ERROR);
            return false;
        }
        // 校验正确
        return true;
    }

    /**
     * @param jsonStr 待判断的字符串
     * @return 是否是json字符串
     */
    public static boolean isJsonStr(String jsonStr) {
        JsonElement jsonElement;
        try {
            jsonElement = JsonParser.parseString(jsonStr);
        } catch (Exception e) {
            Log.e(TAG, "isJsonStr: " + Constants.JSON_FORMAT_ERROR);
            return false;
        }
        if (jsonElement == null) {
            Log.e(TAG, "isJsonStr: " + Constants.JSON_FORMAT_ERROR);
            return false;
        }
        if (!jsonElement.isJsonObject()) {
            Log.e(TAG, "isJsonStr: " + Constants.JSON_FORMAT_ERROR);
            return false;
        }

        return true;
    }

    public MessageUtils setHandler(Handler handler) {
        this.handler = handler;
        return this;
    }
}
