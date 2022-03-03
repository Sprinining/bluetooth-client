package com.example.uhf_bluetoothclient.ui

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import com.example.uhf_bluetoothclient.BR
import com.example.uhf_bluetoothclient.R
import com.example.uhf_bluetoothclient.databinding.ActivityDataExportBinding
import com.example.uhf_bluetoothclient.entity.CityBean
import com.example.uhf_bluetoothclient.entity.ExportBean
import com.example.uhf_bluetoothclient.entity.ProvinceBean
import com.example.uhf_bluetoothclient.http.ErrorInfo
import com.example.uhf_bluetoothclient.initializer.exportInfoDao
import com.example.uhf_bluetoothclient.util.MessageUtils
import com.example.uhf_bluetoothclient.viewmodel.DataExportModel
import com.google.zxing.BarcodeFormat
import com.google.zxing.DecodeHintType
import com.google.zxing.MultiFormatReader
import com.lxj.xpopup.XPopup
import com.seuic.scancode.DecodeFormatManager
import com.seuic.util.common.ActivityUtils
import com.seuic.util.common.SPUtils
import com.seuic.util.common.ext.*
import kotlinx.coroutines.launch
import org.json.JSONObject
import rxhttp.RxHttp
import rxhttp.awaitResult
import rxhttp.toResponse
import java.util.*

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

    private val provincePopupView by lazy {
        object :
            MyAttachListPopupView<ProvinceBean>(this) {
            override fun getDataString(data: ProvinceBean) = data.name

            override fun getIcon(data: ProvinceBean) = null
        }.setStringData(provinceJson?.toMutableList()).setOnSelectListener { _, data ->
            selectProvinceBean = data
            binding.exportVm!!.lastProvince.value = data.name
        }
    }

    private val cityPopupView by lazy {
        object :
            MyAttachListPopupView<CityBean>(this) {
            override fun getDataString(data: CityBean) = data.name

            override fun getIcon(data: CityBean) = null
        }.setOnSelectListener { _, data ->
            binding.exportVm!!.lastCity.value = data.name
        }
    }

    override fun initView() {
        initObserve()

        binding.toolbarBack.singleClick {
            finish()
        }
        binding.toolbarMoreTv.singleClick {
            ActivityUtils.startActivity(NotExportActivity::class.java)
        }
        binding.exportProvinceEt.setDrawableRightClickListener {
            XPopup.Builder(this).atView(binding.exportProvinceEt)
                .asCustom(provincePopupView).show()
        }
        binding.exportCityEt.setDrawableRightClickListener {
            if (selectProvinceBean != null)
                XPopup.Builder(this).atView(binding.exportCityEt).asCustom(
                    cityPopupView.apply {
                        setStringData(selectProvinceBean?.city?.toMutableList())
                    }
                ).show()
        }
        binding.exportBranchesEt.setDrawableRightClickListener {
            val exportBranches =
                SPUtils.getInstance().getString("exportBranches", null)?.toTypeClassList<String>()
                    ?.toMutableList() ?: mutableListOf()
            exportBranches.add("清除网点记录")
            XPopup.Builder(this).atView(binding.exportBranchesEt)
                .asAttachList(exportBranches.toTypedArray(), null) { pos, text ->
                    if (pos == exportBranches.size - 1) {
                        if (exportBranches.size > 1) {
                            XPopup.Builder(this).asConfirm("提示", "请确认是否清除网点记录！", "取消", "清除", {
                                SPUtils.getInstance().remove("exportBranches")
                            }, null, false).show()
                        }
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
            ActivityUtils.startActivity(ScanDeviceActivity::class.java)
        }
        binding.updateBtn.singleClick {
            // 没连上蓝牙的时候要手动输入
            MessageUtils.getINSTANCE().getReaderInfo()
            MessageUtils.getINSTANCE().getIP()
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

        binding.submitBtn.singleClick {
            if (binding.exportIpEt.length() <= 0) {
                toastShort { "请输入IP！" }
                return@singleClick
            }
            if (binding.exportPortEt.length() <= 0) {
                toastShort { "请输入端口！" }
                return@singleClick
            }

            if (binding.exportProvinceEt.length() <= 0) {
                toastShort { "请输入省！" }
                return@singleClick
            }
            if (binding.exportCityEt.length() <= 0) {
                toastShort { "请输入城市！" }
                return@singleClick
            }
            if (binding.exportBranchesEt.length() <= 0) {
                toastShort { "请输入网点！" }
                return@singleClick
            }
            if (binding.exportLocationEt.length() <= 0) {
                toastShort { "请输入安装位置！" }
                return@singleClick
            }
            if (binding.exportAddressEt.length() <= 0) {
                toastShort { "请输入详细信息！" }
                return@singleClick
            }
            if (binding.exportSnEt.length() <= 0) {
                toastShort { "请输入设备SN！" }
                return@singleClick
            }
            if (binding.exportIpv4Et.length() <= 0) {
                toastShort { "请输入IPV4！" }
                return@singleClick
            }
            if (binding.exportIpv4InfoEt.length() <= 0) {
                toastShort { "请输入IPV4网端信息！" }
                return@singleClick
            }
            if (binding.exportIpv6Et.length() <= 0) {
                toastShort { "请输入IPV6！" }
                return@singleClick
            }
            if (binding.exportImeiEt.length() <= 0) {
                toastShort { "请输入IMEI！" }
                return@singleClick
            }
            if (binding.exportRj45macEt.length() <= 0) {
                toastShort { "请输入RJ45MAC地址！" }
                return@singleClick
            }
            if (binding.exportBluetoothmacEt.length() <= 0) {
                toastShort { "请输入蓝牙MAC地址！" }
                return@singleClick
            }
            if (binding.exportAntTv.length() <= 0) {
                toastShort { "请输入天线数量！" }
                return@singleClick
            }
            if (binding.exportPowerTypeTv.length() <= 0) {
                toastShort { "请输入供电方式！" }
                return@singleClick
            }
            if (binding.exportUseForEt.length() <= 0) {
                toastShort { "请输入功能！" }
                return@singleClick
            }
            ExportBean(
                province = binding.exportProvinceEt.text?.trim().toString(),
                city = binding.exportCityEt.text?.trim().toString(),
                branches = binding.exportBranchesEt.text?.trim().toString(),
                location = binding.exportLocationEt.text?.trim().toString(),
                address = binding.exportAddressEt.text?.trim().toString(),
                snNum = binding.exportSnEt.text?.trim().toString(),
                ipv4 = binding.exportIpv4Et.text?.trim().toString(),
                netServerSide = binding.exportIpv4InfoEt.text?.trim().toString(),
                ipv6 = binding.exportIpv6Et.text?.trim().toString(),
                imei = binding.exportImeiEt.text?.trim().toString(),
                rj54mac = binding.exportRj45macEt.text?.trim().toString(),
                blueToothMac = binding.exportBluetoothmacEt.text?.trim().toString(),
                antennaNumber = binding.exportAntTv.text?.trim().toString(),
                powerSupplyMode = binding.exportPowerTypeTv.text?.trim().toString(),
                function = binding.exportUseForEt.text?.trim().toString(),
                remark = binding.exportOtherEt.text?.trim().toString(),
            ).also { bean ->
                //保存记录
                viewModel.lastIP.postValue(binding.exportIpEt.text?.trim().toString())
                viewModel.lastPort.postValue(binding.exportPortEt.text?.trim().toString())
                val exportBranches =
                    SPUtils.getInstance().getString("exportBranches", null)
                        ?.toTypeClassList<String>()
                        ?.toMutableList() ?: mutableListOf()
                exportBranches.add(0, bean.branches)
                SPUtils.getInstance().put("exportBranches", exportBranches.toJsonStr())

                lifecycleScope.launch {
                    showLoading("")
                    RxHttp.postJson("https://${viewModel.lastIP.value}:${viewModel.lastPort.value}/server/information/save")
                        .addAll(
                            JSONObject().put(
                                "jsonString",
                                bean.toJsonStr()
                            ).toString()
                        ).toResponse<Any>().awaitResult {
                            toastShort { "上传成功" }
                            hideLoading()
                        }.onFailure {
                            hideLoading()
                            ErrorInfo(it)
                            //保存到数据库
                            exportInfoDao.insertItem(bean)
                        }
                }
            }
        }
    }

    val reader: MultiFormatReader by lazy {
        val formatReader = MultiFormatReader()
        val hints = Hashtable<DecodeHintType, Any>()
        val decodeFormats = Vector<BarcodeFormat>()

        //添加一维码解码格式
        decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS)
        //添加二维码解码格式
        decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS)
        decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS)

        hints[DecodeHintType.POSSIBLE_FORMATS] = decodeFormats
        //设置解码的字符类型
        hints[DecodeHintType.CHARACTER_SET] = "UTF-8"
        //这边是焦点回调，就是找到那个条码的所在位置，这里我不处理
//        hints[DecodeHintType.NEED_RESULT_POINT_CALLBACK] = mPointCallBack
        hints[DecodeHintType.PURE_BARCODE] = true
        hints[DecodeHintType.TRY_HARDER] = true
        formatReader.setHints(hints)
        formatReader
    }


    override fun initOthers() {
        MessageUtils.getINSTANCE().setDataExportModel(viewModel)
    }

    private fun initObserve() {
        viewModel.sn.observe(this, { s: String ->
            binding.exportSnEt.setText(s)
            Log.e("wmj", "sn: $s")
        })

        viewModel.ipv4.observe(this, { s: String ->
            binding.exportIpv4Et.setText(s)
            Log.e("wmj", "ipv4: $s")
        })


        viewModel.network_side.observe(this, { s: String ->
            binding.exportIpv4InfoEt.setText(s)
            Log.e("wmj", "network_side: $s")
        })

        viewModel.ipv6.observe(this, { s: String ->
            binding.exportIpv6Et.setText(s)
            Log.e("wmj", "ipv6: $s")
        })

        viewModel.imei.observe(this, { s: String ->
            binding.exportImeiEt.setText(s)
            Log.e("wmj", "imei: $s")
        })

        viewModel.mac_net.observe(this, { s: String ->
            binding.exportRj45macEt.setText(s)
            Log.e("wmj", "mac_net: $s")
        })

        viewModel.mac_ble.observe(this, { s: String ->
            binding.exportBluetoothmacEt.setText(s)
            Log.e("wmj", "mac_ble: $s")
        })
    }

    override fun initLayout(savedInstanceState: Bundle?): Int = R.layout.activity_data_export

    override fun initBR(): Int = BR.export_vm
}