package com.example.uhf_bluetoothclient.ui

import android.content.Context
import android.util.SparseBooleanArray
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.uhf_bluetoothclient.R
import com.lxj.xpopup.core.AttachPopupView

abstract class MyAttachListPopupView<T>(context: Context) : AttachPopupView(context) {
	private val adapter: BaseQuickAdapter<T, BaseViewHolder>
	private val isCheckedList = SparseBooleanArray()
	private var isMultiable //是否可以多选
			= false
	private var popWidth = 0

	constructor(context: Context, isMultiable: Boolean) : this(context) {
		this.isMultiable = isMultiable
	}

	private lateinit var recyclerView: RecyclerView
	override fun getImplLayoutId(): Int {
		return R.layout._xpopup_attach_impl_list
	}

	override fun onCreate() {
		super.onCreate()
		recyclerView = findViewById(R.id.recyclerView)
		if (popWidth > 0) {
			val params = recyclerView.layoutParams as LayoutParams
			params.width = popWidth
			recyclerView.layoutParams = params
		}
		adapter.setOnItemClickListener { adapter1: BaseQuickAdapter<*, *>?, view: View?, position: Int ->
			if (selectListener != null) {
				selectListener?.invoke(position, adapter.getItem(position))
			}
			if (isMultiable) {
				isCheckedList.put(position, !isCheckedList[position, false])
				adapter.notifyDataSetChanged()
			}
			if (popupInfo.autoDismiss || !isMultiable) dismiss()
		}
		recyclerView.setAdapter(adapter)
		attachPopupContainer.setBackgroundResource(R.color.color_33)
	}

	override fun getMaxWidth(): Int {
		return popupInfo.maxWidth
	}

	/**
	 * 设置宽度
	 *
	 * @param width px
	 */
	fun setWidth(width: Int): MyAttachListPopupView<T> {
		this.popWidth = width
		val params = recyclerView.layoutParams as RecyclerView.LayoutParams
		params.width = width
		recyclerView.layoutParams = params
		return this
	}

	var icons: List<*>? = null

	/**
	 * 默认去getIcon
	 *
	 * @param data
	 * @return
	 */
	fun setStringData(data: MutableList<T>?): MyAttachListPopupView<T> {
		adapter.setNewInstance(data)
		isCheckedList.clear()
		return this
	}

	/**
	 * 传入icon  不会再去找icon url
	 *
	 * @param data
	 * @param icons 传入int类型或者string类型  int代表res string代表url
	 * @return
	 */
	fun setStringData(data: MutableList<T>?, icons: List<*>?): MyAttachListPopupView<T> {
		adapter.setNewInstance(data)
		this.icons = icons
		isCheckedList.clear()
		return this
	}

	fun setOffsetXAndY(offsetX: Int, offsetY: Int): MyAttachListPopupView<T> {
		defaultOffsetX += offsetX
		defaultOffsetY += offsetY
		return this
	}

	private var selectListener: ((position: Int, data: T) -> Unit)? = null
	fun setOnSelectListener(selectListener: (position: Int, data: T) -> Unit): MyAttachListPopupView<T> {
		this.selectListener = selectListener
		return this
	}

	/**
	 * 获取选中的item
	 *
	 * @param isSort true按照data顺序排列  false按照选择顺序排列
	 * @return
	 */
	fun getCheckedList(isSort: Boolean): List<T>? {
		if (isMultiable && isCheckedList.size() > 0) {
			val list: MutableList<T> = ArrayList()
			if (isSort) {
				for (i in 0 until adapter.itemCount) {
					if (isCheckedList[i, false]) {
						list.add(adapter.getItem(isCheckedList.keyAt(i)))
					}
				}
			} else {
				for (i in 0 until isCheckedList.size()) {
					if (isCheckedList.valueAt(i)) {
						list.add(adapter.getItem(isCheckedList.keyAt(i)))
					}
				}
			}
			return list
		}
		return null
	}

	abstract fun getDataString(data: T): String?

	/**
	 * 传出int类型或者string类型  int代表res string代表url
	 *
	 * @param data
	 * @return
	 */
	abstract fun getIcon(data: T): Any?

	init {
		adapter = object : BaseQuickAdapter<T, BaseViewHolder>(R.layout.my_xpopup_adapter_text) {
			protected override fun convert(helper: BaseViewHolder, item: T) {
				val tv_text = helper.getView<TextView>(R.id.tv_text)
				helper.setText(R.id.tv_text, getDataString(item))
				helper.setTextColorRes(R.id.tv_text, R.color.white)
				tv_text.requestLayout()
				val icon: Any? = if (icons != null && icons!!.size > helper.adapterPosition) {
					icons!![helper.adapterPosition]!!
				} else {
					getIcon(item)
				}
				when (icon) {
					is Int -> {
						helper.setGone(R.id.iv_image, false)
						helper.setImageResource(R.id.iv_image, icon)
					}
					is String -> {
						helper.setGone(R.id.iv_image, false)
					}
					else -> {
						helper.setGone(R.id.iv_image, true)
						helper.setImageResource(R.id.iv_image, 0)
					}
				}
				if (isMultiable) {
					val b = isCheckedList[helper.adapterPosition, false]
					helper.setVisible(R.id.check_state_iv, b)
					tv_text.isActivated = b
				}
				helper.setGone(R.id.xpopup_divider, true)
			}
		}
	}
}