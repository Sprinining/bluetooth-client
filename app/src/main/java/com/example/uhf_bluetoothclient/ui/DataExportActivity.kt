package com.example.uhf_bluetoothclient.ui

import android.os.Bundle
import com.example.uhf_bluetoothclient.BR
import com.example.uhf_bluetoothclient.R
import com.example.uhf_bluetoothclient.databinding.ActivityDataExportBinding
import com.example.uhf_bluetoothclient.entity.CityBean
import com.example.uhf_bluetoothclient.entity.ProvinceBean
import com.example.uhf_bluetoothclient.viewmodel.DataExportModel
import com.lxj.xpopup.XPopup
import com.seuic.util.common.SPUtils
import com.seuic.util.common.ext.getString
import com.seuic.util.common.ext.singleClick
import com.seuic.util.common.ext.toTypeClassList
import com.seuic.util.common.ext.toastShort

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
        binding.exportBranchesEt.setDrawableRightClickListener {
            val exportBranches =
                SPUtils.getInstance().getString("exportBranches", null).toTypeClassList<String>()
                    ?.toMutableList() ?: mutableListOf()
            exportBranches.add("清除网点记录！")
            XPopup.Builder(this).atView(binding.exportBranchesEt)
                .asAttachList(exportBranches.toTypedArray(), null) { pos, text ->
                    if (exportBranches.size > 1 && pos == exportBranches.size - 1) {
                        XPopup.Builder(this).asConfirm("提示", "请确认是否清除网点记录！", "取消", "清除", {
                            SPUtils.getInstance().remove("exportBranches")
                        }, null, false).show()
                        return@asAttachList
                    }
                    binding.exportBranchesEt.setText(text)
                }.show()
        }
        val locations = arrayOf("仓库", "正门", "后门", "楼梯", "食堂", "ATM", "二楼门")
        binding.exportLocationEt.setDrawableRightClickListener {
            XPopup.Builder(this).atView(binding.exportLocationEt)
                .asAttachList(locations, null) { _, text ->
                    binding.exportLocationEt.setText(text)
                }.show()
        }

        binding.connectBtn.singleClick {
            // FIXME: 蓝牙连接设备
        }
        binding.updateBtn.singleClick {
            // FIXME: 获取数据
        }
        val ants = arrayOf("1", "2", "3", "4", "5", "6", "7", "8")
        binding.exportAntTv.singleClick {
            XPopup.Builder(this).atView(binding.exportAntTv)
                .asAttachList(ants, null) { _, text ->
                    binding.exportAntTv.setText(text)
                }.show()
        }
        val powerTypes = arrayOf("电源", "POE")
        binding.exportPowerTypeTv.singleClick {
            XPopup.Builder(this).atView(binding.exportPowerTypeTv)
                .asAttachList(powerTypes, null) { _, text ->
                    binding.exportPowerTypeTv.setText(text)
                }.show()
        }
        val useForTypes = arrayOf("盘点", "门禁")
        binding.exportUseForEt.setDrawableRightClickListener {
            XPopup.Builder(this).atView(binding.exportUseForEt)
                .asAttachList(useForTypes, null) { _, text ->
                    binding.exportUseForEt.setText(text)
                }.show()
        }

        binding.toolbarMoreTv.singleClick {
            if (binding.exportProvinceEt.length()<=0){
                toastShort { "请输入省！" }
                return@singleClick
            }
            if (binding.exportCityEt.length()<=0){
                toastShort { "请输入城市！" }
                return@singleClick
            }
            if (binding.exportBranchesEt.length()<=0){
                toastShort { "请输入网点！" }
                return@singleClick
            }
            if (binding.exportLocationEt.length()<=0){
                toastShort { "请输入安装位置！" }
                return@singleClick
            }
            if (binding.exportAddressEt.length()<=0){
                toastShort { "请输入详细信息！" }
                return@singleClick
            }
            if (binding.exportSnEt.length()<=0){
                toastShort { "请输入设备SN！" }
                return@singleClick
            }
            if (binding.exportIpv4Et.length()<=0){
                toastShort { "请输入IPV4！" }
                return@singleClick
            }
            if (binding.exportIpv4InfoEt.length()<=0){
                toastShort { "请输入IPV4网端信息！" }
                return@singleClick
            }
            if (binding.exportIpv6Et.length()<=0){
                toastShort { "请输入IPV6！" }
                return@singleClick
            }
            if (binding.exportImeiEt.length()<=0){
                toastShort { "请输入IMEI！" }
                return@singleClick
            }
            if (binding.exportRj45macEt.length()<=0){
                toastShort { "请输入RJ45MAC地址！" }
                return@singleClick
            }
            if (binding.exportIpv4InfoEt.length()<=0){
                toastShort { "请输入IPV4网端信息！" }
                return@singleClick
            }
            // FIXME: 以上校验完成。。。。。。


        }
    }

    override fun initOthers() {
    }

    override fun initLayout(savedInstanceState: Bundle?): Int = R.layout.activity_data_export

    override fun initBR(): Int = BR.export_vm
}