package com.example.uhf_bluetoothclient.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class CalibrationUtils {
    /**
     * @param jsonStr 待判断的字符串
     * @return 是否是json字符串
     */
    public static boolean isJsonStr(String jsonStr) {
        JsonElement jsonElement;
        try {
            jsonElement = JsonParser.parseString(jsonStr);
        } catch (Exception e) {
            return false;
        }
        if (jsonElement == null) {
            return false;
        }
        return jsonElement.isJsonObject();
    }

    public static boolean isMac(String mac) {
        String regex = "^([0-9A-F]{2})((-[0-9A-F]{2}){5})$";
        return mac.matches(regex);
    }

    public static boolean isGatewayOrDNS(String str){
        String regex = "^([1-9]|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])\\.(\\d|[1-9]\\d|1\\d\\d|2[0-4]\\d|25[0-5])$";
        return  str.matches(regex);
    }

    public static boolean isNetmask(String netmask) {
        String regex = "^((128|192)|2(24|4[08]|5[245]))(\\.(0|(128|192)|2((24)|(4[08])|(5[245])))){3}$";
        return netmask.matches(regex);
    }

    public static boolean isIPv4Address(String ip) {
        String regex = "^(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
                + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
                + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\."
                + "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ip.matches(regex);
    }

    /**
     * 带前缀或不带都行
     * @param ip
     * @return
     */
    public static boolean isIPv6Address(String ip) {
        String regex = "^((\\s*((([0-9A-Fa-f]{1,4}:){7}([0-9A-Fa-f]{1,4}|:))|" +
                "(([0-9A-Fa-f]{1,4}:){6}(:[0-9A-Fa-f]{1,4}|" +
                "((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|" +
                "(([0-9A-Fa-f]{1,4}:){5}(((:[0-9A-Fa-f]{1,4}){1,2})|" +
                ":((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|" +
                "2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3})|:))|" +
                "(([0-9A-Fa-f]{1,4}:){4}(((:[0-9A-Fa-f]{1,4}){1,3})|" +
                "((:[0-9A-Fa-f]{1,4})?:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|" +
                "(([0-9A-Fa-f]{1,4}:){3}(((:[0-9A-Fa-f]{1,4}){1,4})|" +
                "((:[0-9A-Fa-f]{1,4}){0,2}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|" +
                "(([0-9A-Fa-f]{1,4}:){2}(((:[0-9A-Fa-f]{1,4}){1,5})|" +
                "((:[0-9A-Fa-f]{1,4}){0,3}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|" +
                "(([0-9A-Fa-f]{1,4}:){1}(((:[0-9A-Fa-f]{1,4}){1,6})|" +
                "((:[0-9A-Fa-f]{1,4}){0,4}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:))|" +
                "(:(((:[0-9A-Fa-f]{1,4}){1,7})|" +
                "((:[0-9A-Fa-f]{1,4}){0,5}:((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}))|:)))(%.+)?\\s*)(\\/(([1-9])|" +
                "([1-9][0-9])|(1[0-1][0-9]|12[0-8]))){0,1})*$";
        return ip.matches(regex);
    }
}
