package com.example.uhf_bluetoothclient.http

import java.io.IOException

/**
 *
 * @ProjectName:    seuic-ocr
 * @Description:    自定义请求错误
 * @Author:         KP
 * @CreateDate:     2021/9/24 15:05
 * @Version:        1.0
 */
class CommonException(message: String? = "") : IOException(message)