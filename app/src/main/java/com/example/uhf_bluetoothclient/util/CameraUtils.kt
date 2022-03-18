package com.example.uhf_bluetoothclient.util

import android.annotation.SuppressLint
import android.graphics.ImageFormat
import android.util.Log
import android.util.Size
import android.view.View
import androidx.camera.core.*
import androidx.camera.core.AspectRatio.RATIO_4_3
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.google.common.util.concurrent.ListenableFuture
import com.seuic.core.utils.ext.appContext
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean


/**
 *
 * @ProjectName:    PSBC_OCR
 * @Description:     类作用描述
 * @Author:         KP
 * @CreateDate:     2021/9/17 18:33
 * @Version:        1.0
 */

typealias Nv21ImageListener = (image_nv21: ByteArray, width: Int, height: Int) -> Unit
object CameraUtils : LifecycleOwner {

    private lateinit var mLifecycleRegistry: LifecycleRegistry
    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null

    //    private var imageAnalyzer: ImageAnalysis? = null
    private var camera: Camera? = null
    private val cameraExecutor: ExecutorService by lazy { Executors.newSingleThreadExecutor() }
    private var imageAnalyzer: ImageAnalysis? = null

    private var mListener: Nv21ImageListener? = null
//    private val orientationEventListener by lazy {
//        object : OrientationEventListener(appContext) {
//            override fun onOrientationChanged(orientation: Int) {
//                val rotation = when (orientation) {
//                    in 45 until 135 -> Surface.ROTATION_270
//                    in 135 until 225 -> Surface.ROTATION_180
//                    in 225 until 315 -> Surface.ROTATION_90
//                    else -> Surface.ROTATION_0
//                }
////				preview?.targetRotation = rotation
////                imageAnalyzer?.targetRotation = rotation
//            }
//        }
//    }

    private val previewAnalysis: PreviewAnalyzer by lazy {
        PreviewAnalyzer { image, width, height ->
            mListener?.invoke(image, width, height)
        }
    }

    private var mIsFlashOpen = false
    private var mIsZoomEnabled = true
    private var mZoomSize = 0F

    /** Declare and bind preview, capture and analysis use cases */
    @SuppressLint("RestrictedApi")
    private fun bindCameraUseCases(lifecycleOwner: LifecycleOwner, view_finder: PreviewView?) {
// Get screen metrics used to setup camera for full screen resolution
        // Bind the CameraProvider to the LifeCycleOwner
        val cameraSelector =
            CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build()
        cameraProviderFuture = ProcessCameraProvider.getInstance(appContext).apply {
            addListener({
                // CameraProvider

                val cameraProvider: ProcessCameraProvider = get()
                var preview: Preview? = null
                if (view_finder != null) {
                    preview = Preview.Builder()
                        // We request aspect ratio but no resolution
                        // Set initial target rotation
//                        .setTargetAspectRatio(RATIO_4_3)
                        .setTargetResolution(Size(1080, 1920))
                        .build()
                    // Attach the viewfinder's surface provider to preview use case
                    preview.setSurfaceProvider(view_finder.surfaceProvider)
                }


                // ImageAnalysis
                imageAnalyzer = ImageAnalysis.Builder()
                    .setImageQueueDepth(1)
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    // We request aspect ratio but no resolution
//					.setTargetAspectRatio(screenAspectRatio)
//                    .setTargetAspectRatio(RATIO_4_3)
                    .setTargetResolution(Size(1080, 1920))
// Set initial target rotation, we will have to call this again if rotation changes
                    // during the lifecycle of this use case
                    .build() // The analyzer can then be assigned to the instance
                imageAnalyzer?.setAnalyzer(cameraExecutor, previewAnalysis)

                // Must unbind the use-cases before rebinding them
                cameraProvider.unbindAll()

                kotlin.runCatching {
                    // A variable number of use-cases can be passed here -
                    // camera provides access to CameraControl & CameraInfo
                    camera = if (preview != null) {
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            preview,
                            imageAnalyzer
                        )
                    } else {
                        cameraProvider.bindToLifecycle(
                            lifecycleOwner,
                            cameraSelector,
                            imageAnalyzer
                        )
                    }.apply {
                        cameraControl.enableTorch(mIsFlashOpen)
                        if (mIsZoomEnabled && mZoomSize >= 0 && mZoomSize <= 100) {
                            cameraControl.setLinearZoom(mZoomSize / 100)
                        }
                    }

                }.onFailure {
                    Log.e("TAG", "Use case binding failed", it)
                }
            }, ContextCompat.getMainExecutor(appContext))
        }

    }


    fun openCamera(
        lifecycleOwner: LifecycleOwner? = null,
        preview: PreviewView? = null,
        listener: Nv21ImageListener,
    ) {
        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry.currentState = Lifecycle.State.CREATED
        mLifecycleRegistry.currentState = Lifecycle.State.STARTED
        previewAnalysis.reset()
        mListener = listener
        mIsFlashOpen = false
        if (lifecycleOwner == null) {
            preview?.addOnAttachStateChangeListener(object : View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View?) {
                    mLifecycleRegistry.currentState = Lifecycle.State.STARTED

//                    orientationEventListener.enable()
                }

                override fun onViewDetachedFromWindow(v: View?) {
                    mLifecycleRegistry.currentState = Lifecycle.State.CREATED
//                    orientationEventListener.disable()
                }

            })
            bindCameraUseCases(this, preview)
        } else {
            bindCameraUseCases(lifecycleOwner, preview)
        }
    }

    fun closeCamera() {
        cameraProviderFuture?.get()?.unbindAll()
    }


    override fun getLifecycle(): Lifecycle = mLifecycleRegistry

    class PreviewAnalyzer(val listener: Nv21ImageListener) : ImageAnalysis.Analyzer {
        private var isCanInit: AtomicBoolean = AtomicBoolean(true)

        fun reset() {
            isCanInit.set(true)
        }

        //image默认高480  宽640
        @SuppressLint("UnsafeOptInUsageError", "UnsafeExperimentalUsageError")
        override fun analyze(image: ImageProxy) {
            image.image?.takeIf { it.width > 0 && it.height > 0 }
                ?.also {
                    //如果不是yuv_420_888格式直接不处理
                    if (ImageFormat.YUV_420_888 != image.format) {
                        Log.e("BarcodeAnalyzer", "expect YUV_420_888, now = ${image.format}")
                        image.close()
                        return
                    }

                    //将buffer数据写入数组
                    val data = image.planes[0].buffer.toByteArray()

                    //获取图片宽高
                    val height = image.height
                    val width = image.width

                    //将图片旋转，这是竖屏扫描的关键一步，因为默认输出图像是横的，我们需要将其旋转90度
                    val rotationData = ByteArray(data.size)
                    Log.i("TAG", "rotationDataSize :${data.size}  ## height:$height ## width:$width")
                    var j: Int
                    var k: Int
                    for (y in 0 until height) {
                        for (x in 0 until width) {
                            j = x * height + height - y - 1
                            k = x + y * width
                            rotationData[j] = data[k]
                        }
                    }
                    listener(rotationData, height, width)

//                    val nv21 = it.yuv420888ToNv21()
//                    val rotateNV21 = ByteArray(nv21.size)
//
//                    when (0) {
//                        0 -> {
//                            listener(nv21, it.width, it.height)
//                        }
//                        90 -> {
//                            Nv21Utils.NV21_rotate_to_90(nv21, rotateNV21, it.width, it.height)
//                            listener(rotateNV21, it.height, it.width)
//                        }
//                        180 -> {
//                            Nv21Utils.NV21_rotate_to_180(nv21, rotateNV21, it.width, it.height)
//                            listener(rotateNV21, it.width, it.height)
//                        }
//                        270 -> {
//                            Nv21Utils.NV21_rotate_to_270(nv21, rotateNV21, it.width, it.height)
//                            listener(rotateNV21, it.height, it.width)
//                        }
//                    }
                }
            image.close()
        }
    }


}