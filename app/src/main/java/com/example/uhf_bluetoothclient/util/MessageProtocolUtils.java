package com.example.uhf_bluetoothclient.util;

import com.example.uhf_bluetoothclient.constants.Constants;

import java.util.ArrayList;

public class MessageProtocolUtils {
    // 0: 0x55, 0xAA 1:$
    private static int flag = 0;

    /**
     * @param str 接收到的带有头尾的数据
     * @return 去掉头尾的数据
     */
    public static String removeHeadAndTail(String str) {
        // 检验头尾是否是0x55 0xAA
        int head = str.charAt(0);
        int tail = str.charAt(str.length() - 1);
        if (head != 85 || tail != 170) {
            // 数据接收不对的处理
            return Constants.CHECK_ERROR;
        } else {
            // 去掉头尾后的json字符串
            return str.substring(1, str.length() - 1);
        }
    }

    /**
     * @param str 待发送的数据
     * @return 加上头尾的数据
     */
    public static String addHeadAndTail(String str) {
        return ((char) 85) + str + ((char) 170);
    }

    /**
     * @param str 带有$的json字符串
     * @return 去掉$
     */
    public static String removeTail(String str) {
        // 检验末尾是否为$
        int tail = str.charAt(str.length() - 1);
        if (tail != '$') {
            // 数据接收不对的处理
            return Constants.CHECK_ERROR;
        } else {
            // 去掉$后的json字符串
            return str.substring(0, str.length() - 1);
        }
    }

    /**
     * @param str 待发送的json字符串
     * @return 加上$的json字符串
     */
    public static String addTail(String str) {
        return str + "$";
    }

    /**
     * 共用的校验
     *
     * @param str 待发送的json字符串
     * @return 接收到的带有头尾的数据 or 带有$的json字符串
     */
    public static String addMessageValidation(String str) {
        if (flag == 0) {
            return addHeadAndTail(str);
        } else {
            return addTail(str);
        }
    }

    /**
     * 共用的校验
     *
     * @param str 接收到的带有头尾的数据 or 带有$的json字符串
     * @return 去掉头尾的数据 or 去掉$的json字符串
     */
    public static String removeMessageValidation(String str) {
        int head = str.charAt(0);
        int tail = str.charAt(str.length() - 1);
        if (head == 85 || tail == 170) {
            // 检验头尾是否是0x55 0xAA
            flag = 0;
            // 去掉头尾后的json字符串
            return str.substring(1, str.length() - 1);
        } else if (str.charAt(str.length() - 1) == '$') {
            // 检验末尾是否为$
            flag = 1;
            // 去掉$后的json字符串
            return str.substring(0, str.length() - 1);
        } else {
            // 数据接收不对的处理
            return Constants.CHECK_ERROR;
        }
    }

    public static int judgeType(String str) {
        int head = str.charAt(0);
        int tail = str.charAt(str.length() - 1);
        if (head == 85 || tail == 170) {
            // 检验头尾是否是0x55 0xAA
            flag = 0;
            return 0;
        } else if (str.charAt(str.length() - 1) == '$') {
            // 检验末尾是否为$
            flag = 1;
            return 1;
        } else {
            // 数据接收不对的处理
            flag = 1;
            return 2;
        }
    }

    /**
     * 1为启用，0为不启用，int从右到左的八位对应天线从一到八
     * 如111表示启用第1,2,3根天线
     *
     * @param antennaEnable 天线是否启用的byte值
     * @return 天线数组
     */
    public static int[] byteToAntInt(int antennaEnable) {
        int[] array = new int[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte) (antennaEnable & 1);
            antennaEnable = (byte) (antennaEnable >> 1);
        }
        ArrayList<Integer> arrayList = new ArrayList<>();
        for (int i = 0; i < array.length; i++) {
            if (array[i] == 1) {
                arrayList.add(8 - i);
            }
        }
        return arrayList.stream().mapToInt(Integer::intValue).toArray();
    }

    /**
     * @param ants 下标从低到高对应天线1到8
     * @return
     */
    public static int antAryToByte(boolean[] ants) {
        int res = 0;
        byte b = 0b00000001;
        for (int i = ants.length - 1; i >= 0; i--) {
            if (ants[i]) {
                res += b;
            }
            res = res << 1;
        }
        res = res >> 1;
        return res;
    }
}
