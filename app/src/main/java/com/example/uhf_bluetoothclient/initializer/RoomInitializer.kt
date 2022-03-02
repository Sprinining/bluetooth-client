package com.example.uhf_bluetoothclient.initializer

import android.content.Context
import androidx.room.Room
import androidx.startup.Initializer
import com.example.uhf_bluetoothclient.Constants
import com.example.uhf_bluetoothclient.room.ExportInfoDao
import com.example.uhf_bluetoothclient.room.MyRoomDataBase


/**
 * @Desc
 * @author KP
 * @date 2022/3/1 15:25
 * @version
 */

val Any.exportInfoDao: ExportInfoDao by lazy {
	RoomInitializer.instance.getExportInfoDao()
}


class RoomInitializer : Initializer<Unit> {
	companion object {
		lateinit var instance: MyRoomDataBase
	}

	override fun create(context: Context) {
		instance = Room.databaseBuilder(
			context,
			MyRoomDataBase::class.java,
			Constants.DB_NAME
		)
			.fallbackToDestructiveMigration()
			.allowMainThreadQueries()
			.build()
	}


	override fun dependencies(): List<Class<out Initializer<*>>> = emptyList()
}