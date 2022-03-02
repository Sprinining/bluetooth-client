package com.example.uhf_bluetoothclient.entity

data class ResponseBean<T>(
    val code: Int = 0,
    val data: T? = null,
    val msg: String? = null,
    val `object`: Any? = null
)



