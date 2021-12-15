package com.example.uhf_bluetoothclient.constants;

import java.util.ArrayList;
import java.util.List;

public interface Constants {
    String SP_TAG = "sp_tag";
    String SP_FREQUENCY_BAND = "sp_frequency_band";
    String SP_FREQUENCY_MIN = "sp_frequency_min";
    String SP_FREQUENCY_MAX = "sp_frequency_max";
    String SP_POWER = "sp_power";
    String SP_SESSION = "sp_session";
    String SP_ANTENNA1 = "sp_antenna1";
    String SP_ANTENNA2 = "sp_antenna2";
    String SP_ANTENNA3 = "sp_antenna3";
    String SP_ANTENNA4 = "sp_antenna4";
    String SP_ANTENNA5 = "sp_antenna5";
    String SP_ANTENNA6 = "sp_antenna6";
    String SP_ANTENNA7 = "sp_antenna7";
    String SP_ANTENNA8 = "sp_antenna8";

    String CHECK_ERROR = "数据校验错误";
    String JSON_FORMAT_ERROR = "json格式错误";
    String JSON_FIELD_ERROR = "json字段错误";
    String CMD_ERROR = "cmd命令错误";
    String CODE_ERROR = "code值错误";
    String CMD_EXCEPTION = "操作异常";
    String STOP_SEARCHING_THEN_GO_ON = "请停止寻卡再进行相关操作";
    String BLUETOOTH_NOT_CONNECTED = "蓝牙未连接";
    String SET_SUCCESS = "设置成功";
    String SET_FAIL = "设置失败";
    String GET_SUCCESS = "获取成功";
    String GET_FAIL = "获取失败";

    // 建行的
    int MSG_APP_HEARTBEAT = 1000;// 心跳包标识
    int LOG_BASE_EPC_INFO = 1001;// 标签上报事件标识
    int LOG_BASE_EPC_OVER = 1002;// 标签上报结束事件标识
    int LOG_APP_GPI_START = 1003;// 信号输入触发上报事件标识
    int LOG_APP_GPI_OVER = 1004;// 信号输入触发上报结束事件标识
    int MSG_APP_RESET = 1005;// 重启读写器标识
    int MSG_APP_GET_GPI_STATE = 1006;// 查询信号输入状态参数标识
    int MSG_APP_SET_GPI_TRIGGER = 1007;// 配置信号输入触发参数标识
    int MSG_APP_GET_GPI_TRIGGER = 1008;// 查询信号输入触发参数标识
    int MSG_APP_GET_BASE_VERSION = 1009;// 查询软件基带版本标识
    int MSG_APP_GET_READER_INFO = 1010;// 查询读写器基本信息标识
    int MSG_BASE_STOP = 1011;// 停止读写器RFID操作标识
    int MSG_BASE_SET_POWER = 1012;// 配置读写器天线功率标识
    int MSG_BASE_GET_POWER = 1013;// 查询读写器天线功率标识
    int MSG_BASE_SET_BASE_BAND = 1014;// 配置EPC基带参数标识
    int MSG_BASE_GET_BASE_BAND = 1015;// 查询EPC基带参数标识
    int MSG_BASE_SET_TAG_LOG = 1016;// 配置标签上传参数标识
    int MSG_BASE_GET_TAG_LOG = 1017;// 查询标签上传参数标识
    int MSG_BASE_READ_EPC = 1018;// 读取EPC标签指令标识MsgBaseInventoryEpc
    int MSG_BASE_WRITE_EPC = 1019;// 写EPC标签标识
    int MSG_BASE_LOCK_EPC = 1020;// 锁EPC标签标识
    int MSG_BASE_DESTROY_EPC = 1021;// 销毁EPC标签标识
    int MSG_BASE_SET_FREQ_RANGE = 1022;// 配置RFID工作频段标识
    int MSG_BASE_GET_FREQ_RANGE = 1023;// 查询RFID工作频段标识
    int RFID_SET_TIME_SYNCHRONIZATION = 1024;// 读写器时间同步
    int RFID_APP_SET_ALARM = 1025;// 读写器报警
    int RFID_APP_DOWNLOAD_APP = 1026;// 读写器下载app
    int MSG_BASE_GET_FREQUENCY_HOP_TABLE = 1027;// 获取跳频表
    int MSG_BASE_SET_FREQUENCY_HOP_TABLE = 1028;// 设置跳频表
    int GET_IP = 1029;  // 获取ip，如果是静态ipv4就返回，否则返回ipv6
    int SET_IPV4 = 1030;// 设置ipv4
    int SET_IPV6 = 1031;// 设置ipv6
    List<Integer> FREQUENCY_HOP_TABLE = new ArrayList<Integer>() {{
        add(902750);
        add(903250);
        add(903750);
        add(904250);
        add(904750);
        add(905250);
        add(905750);
        add(906250);
        add(906750);
        add(907250);
        add(907750);
        add(908250);
        add(908750);
        add(909250);
        add(909750);
        add(910250);
        add(910750);
        add(911250);
        add(911750);
        add(912250);
        add(912750);
        add(913250);
        add(913750);
        add(914250);
        add(914750);
        add(915250);
        add(915750);
        add(916250);
        add(916750);
        add(917250);
        add(917750);
        add(918250);
        add(918750);
        add(919250);
        add(919750);
        add(920250);
        add(920750);
        add(921250);
        add(921750);
        add(922250);
        add(922750);
        add(923250);
        add(923750);
        add(924250);
        add(924750);
        add(925250);
        add(925750);
        add(926250);
        add(926750);
        add(927250);
    }};
}
