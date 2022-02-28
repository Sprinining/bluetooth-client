package com.example.uhf_bluetoothclient.ui

import android.os.Bundle
import com.example.uhf_bluetoothclient.BR
import com.example.uhf_bluetoothclient.R
import com.example.uhf_bluetoothclient.databinding.ActivityDataExportBinding
import com.example.uhf_bluetoothclient.entity.CityBean
import com.example.uhf_bluetoothclient.entity.ProvinceBean
import com.example.uhf_bluetoothclient.viewmodel.DataExportModel
import com.lxj.xpopup.XPopup
import com.seuic.util.common.ext.getString
import com.seuic.util.common.ext.toTypeClassList

/**
 *
 * @ProjectName:    UHF_BluetoothClient
 * @Description:     类作用描述
 * @Author:         KP
 * @CreateDate:     2022/2/25 10:45
 * @Version:        1.0
 */
class DataExportActivity : BaseActivity<ActivityDataExportBinding, DataExportModel>() {
    private val provinceJson by lazy {
        assets.open("china_province.json").getString(Charsets.UTF_8).toTypeClassList<ProvinceBean>()
            .also {
                selectProvinceBean = it?.firstOrNull()
            }
    }
    private var selectProvinceBean: ProvinceBean? = null

    override fun initView() {
        binding.exportProvinceEt.setDrawableRightClickListener {
            XPopup.Builder(this).atView(binding.exportProvinceEt).asCustom(object :
                MyAttachListPopupView<ProvinceBean>(this) {
                override fun getDataString(data: ProvinceBean) = data.name

                override fun getIcon(data: ProvinceBean) = null
            }.setStringData(provinceJson?.toMutableList()).setOnSelectListener { _, data ->
                selectProvinceBean = data
                binding.exportVm!!.lastProvince.value = data.name
            }).show()
        }
        binding.exportCityEt.setDrawableRightClickListener {
            XPopup.Builder(this).atView(binding.exportCityEt).asCustom(
                object :
                    MyAttachListPopupView<CityBean>(this) {
                    override fun getDataString(data: CityBean) = data.name

                    override fun getIcon(data: CityBean) = null
                }.setStringData(selectProvinceBean?.city?.toMutableList())
                    .setOnSelectListener { _, data ->
                        binding.exportVm!!.lastCity.value = data.name
                    }).show()
        }
    }

    override fun initOthers() {
    }

    override fun initLayout(savedInstanceState: Bundle?): Int = R.layout.activity_data_export

    override fun initBR(): Int = BR.export_vm
}