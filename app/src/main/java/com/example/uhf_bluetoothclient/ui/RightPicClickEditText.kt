package com.example.uhf_bluetoothclient.ui

import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatEditText
import com.seuic.core.utils.ext.appContext


class RightPicClickEditText : AppCompatEditText {
    private val DRAWABLE_LEFT = 0
    private val DRAWABLE_TOP = 1
    private val DRAWABLE_RIGHT = 2
    private val DRAWABLE_BOTTOM = 3

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var drawableListener: (() -> Unit)? = null

    fun setDrawableRightClickListener(listener: (() -> Unit)?) {
        drawableListener = listener
    }

    private val mSimpleOnGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
            val drawableRight = compoundDrawables[DRAWABLE_RIGHT]
            if (drawableRight != null && e.getRawX() >= right - drawableRight.bounds.width()) {
                drawableListener?.invoke()
                return true
            }
            return true
        }
    }

    private val touchGestureDetector = GestureDetector(appContext, mSimpleOnGestureListener)


    override fun onTouchEvent(event: MotionEvent): Boolean {
        touchGestureDetector.onTouchEvent(event)
        return true
    }
}