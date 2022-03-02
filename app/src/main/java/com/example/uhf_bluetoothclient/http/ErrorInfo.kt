package com.example.uhf_bluetoothclient.http

import android.text.TextUtils
import com.example.uhf_bluetoothclient.R
import com.google.gson.JsonSyntaxException
import com.seuic.util.common.ToastUtils
import com.seuic.util.common.Utils
import rxhttp.wrapper.exception.HttpStatusCodeException
import rxhttp.wrapper.exception.ParseException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.TimeoutException

/**
 * Http请求错误信息
 * User: ljx
 * Date: 2019-06-26
 * Time: 14:26
 */
class ErrorInfo(  //异常信息
    val throwable: Throwable
) {
    var errorCode //仅指服务器返回的错误码
            = 0
    val errorMsg //错误文案，网络错误、请求失败错误、服务器返回的错误等文案
            : String?

    fun show(): Boolean {
        ToastUtils.showShort(if (TextUtils.isEmpty(errorMsg)) throwable.message else errorMsg)
        return true
    }

    /**
     * @param standbyMsg 备用的提示文案
     */
    fun show(standbyMsg: String?): Boolean {
        ToastUtils.showShort(if (TextUtils.isEmpty(errorMsg)) standbyMsg else errorMsg)
        return true
    }

    /**
     * @param standbyMsg 备用的提示文案
     */
    fun show(standbyMsg: Int): Boolean {
        ToastUtils.showShort(
            if (TextUtils.isEmpty(errorMsg)) Utils.getApp().getString(standbyMsg) else errorMsg
        )
        return true
    }

    fun getString(resId: Int): String {
        return Utils.getApp().getString(resId)
    }

    init {
        var errorMsg: String?
        when (throwable) {
            is UnknownHostException -> {
                errorMsg =
                    if (!ExceptionHelper.isNetworkConnected(Utils.getApp())) {
                        getString(R.string.network_error)
                    } else {
                        getString(R.string.notify_no_network)
                    }
            }
            is SocketTimeoutException, is TimeoutException -> {
                //前者是通过OkHttpClient设置的超时引发的异常，后者是对单个请求调用timeout方法引发的超时异常
                errorMsg = getString(R.string.time_out_please_try_again_later)
            }
            is ConnectException -> {
                errorMsg = getString(R.string.esky_service_exception)
            }
            is HttpStatusCodeException -> { //请求失败异常
                val code = throwable.getLocalizedMessage()
                errorMsg = if ("416" == code) {
                    "请求范围不符合要求"
                } else {
                    throwable.message
                }
            }
            is JsonSyntaxException -> { //请求成功，但Json语法异常,导致解析失败
                errorMsg = "数据解析失败,请稍后再试"
            }
            is ParseException -> { // ParseException异常表明请求成功，但是数据不正确
                val errorCode = throwable.getLocalizedMessage()
                this.errorCode = errorCode?.toIntOrNull() ?: 0
                errorMsg = throwable.message
                if (TextUtils.isEmpty(errorMsg)) errorMsg = errorCode //errorMsg为空，显示errorCode
            }
            else -> {
                errorMsg = throwable.message
            }
        }
        this.errorMsg = errorMsg
        show(errorMsg)
    }
}