package com.example.uhf_bluetoothclient.ui

import android.content.Context
import android.util.Log
import androidx.camera.view.PreviewView
import com.example.uhf_bluetoothclient.R
import com.example.uhf_bluetoothclient.util.CameraUtils
import com.example.uhf_bluetoothclient.util.Nv21ImageListener
import com.google.zxing.*
import com.google.zxing.common.HybridBinarizer
import com.lxj.xpopup.core.CenterPopupView
import com.seuic.scancode.DecodeFormatManager
import com.seuic.scancode.ScanCodeUtil
import com.seuic.scancode.ScanMode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import java.util.*

/**
 *
 * @ProjectName:    UHF_BluetoothClient
 * @Description:     类作用描述
 * @Author:         KP
 * @CreateDate:     2022/3/3 13:21
 * @Version:        1.0
 */
class CameraPopupWindow(context: Context) : CenterPopupView(context) {
    private lateinit var view_finder: PreviewView
    override fun getImplLayoutId(): Int {
        return R.layout.camera_window_layout
    }

    override fun onCreate() {
        super.onCreate()
        view_finder = contentView.findViewById(R.id.view_finder)
    }

    override fun onShow() {
        super.onShow()
        CameraUtils.openCamera(null, view_finder, listener)
    }

    override fun onDismiss() {
        super.onDismiss()
        CameraUtils.closeCamera()
    }

    private var barcodeListener: ((String) -> Unit)? = null

   public fun setBarcodeListener(listener: (String) -> Unit) {
        barcodeListener = listener
    }

    private val listener: Nv21ImageListener = object : Nv21ImageListener {
        override fun invoke(image_nv21: ByteArray, width: Int, height: Int) {
            //zxing核心解码块，因为图片旋转了90度，所以宽高互换，最后一个参数是左右翻转
            kotlin.runCatching {
                val source =
                    PlanarYUVLuminanceSource(image_nv21, width, height, 0, 0, width, height, false)
                val bitmap = BinaryBitmap(HybridBinarizer(source))
                reader.decode(bitmap).text
            }.getOrNull()
                .takeIf {
                    Log.i("CameraPopupWindow", "识别条码:${it}")
                    !it.isNullOrBlank()
                }?.also {
                    MainScope().launch(Dispatchers.Main) {
                        barcodeListener?.invoke(it)
                        dismiss()
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

}