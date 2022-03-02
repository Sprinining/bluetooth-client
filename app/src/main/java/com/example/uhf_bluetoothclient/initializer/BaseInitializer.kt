package com.example.uhf_bluetoothclient.initializer

import android.content.Context
import androidx.startup.Initializer
import com.seuic.util.common.BuildConfig
import com.seuic.util.common.CrashUtils
import com.seuic.util.common.LogUtils
import java.io.File


/**
 * @Desc
 * @author KP
 * @date 2022/3/1 15:25
 * @version
 */

class BaseInitializer : Initializer<Unit> {

	override fun create(context: Context) {
		CrashUtils.init(File(context.getExternalFilesDir("")!!.absolutePath, "crash"))
		//fragment自适应
		LogUtils.getConfig().apply{
			isLogSwitch = BuildConfig.DEBUG
//			isLog2FileSwitch = true
		}
	}

	override fun dependencies(): List<Class<out Initializer<*>>> = arrayListOf(
		HttpInitializer::class.java,
		RoomInitializer::class.java,
	)
}