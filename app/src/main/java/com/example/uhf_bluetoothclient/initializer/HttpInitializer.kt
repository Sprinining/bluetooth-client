package com.example.uhf_bluetoothclient.initializer

import android.content.Context
import androidx.startup.Initializer
import com.example.uhf_bluetoothclient.BuildConfig
import com.seuic.util.common.ext.logITag
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import rxhttp.RxHttpPlugins
import java.util.concurrent.TimeUnit


/**
 * @Desc
 * @author KP
 * @date 2022/3/1 15:25
 * @version
 */

class HttpInitializer : Initializer<Unit> {
    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) {
                    setLevel(HttpLoggingInterceptor.Level.BASIC)
                }
            })
//			.addInterceptor(TokenInterceptor())
            .build()
    }

    override fun create(context: Context) {
        RxHttpPlugins.init(httpClient)
            .setDebug(BuildConfig.DEBUG)
    }

    override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}