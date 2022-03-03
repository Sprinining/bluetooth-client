package com.example.uhf_bluetoothclient.ui.adapter.nodeprovider

import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.aitsuki.swipe.SwipeLayout
import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.uhf_bluetoothclient.R
import com.example.uhf_bluetoothclient.entity.ExportBean
import com.example.uhf_bluetoothclient.entity.RootNodeBean
import com.seuic.core.utils.ext.getResourceColor

/**
 *
 * @ProjectName:    seuic-ocr
 * @Description:     数据集根目录
 * @Author:         KP
 * @CreateDate:     2021/10/19 11:08
 * @Version:        1.0
 */
class DatasetRootNodeProvider(
    override val itemViewType: Int = 0,
    override val layoutId: Int = R.layout.dataset_item
) : BaseNodeProvider() {

    override fun onViewHolderCreated(viewHolder: BaseViewHolder, viewType: Int) {
        super.onViewHolderCreated(viewHolder, viewType)
        viewHolder.getView<ConstraintLayout>(R.id.root_cl).setOnClickListener {
            viewHolder.getView<SwipeLayout>(R.id.swipe_layout).apply {
                if (!isLeftMenuOpened() && !isRightMenuOpened()) {
                    getAdapter()?.expandOrCollapse(viewHolder.bindingAdapterPosition, true, true, 123)
                }
            }
        }
        viewHolder.getView<TextView>(R.id.delete_tv).setOnClickListener {
            mListener?.onItemChildClick(getAdapter()!!, it, viewHolder.bindingAdapterPosition)
        }
    }

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        if (item is RootNodeBean) {
            helper.setText(R.id.rootnode_title_tv, item.sn)
        }
    }

    override fun convert(helper: BaseViewHolder, item: BaseNode, payloads: List<Any>) {
        super.convert(helper, item, payloads)
        payloads.onEach {
            if (it == 123 && item is RootNodeBean) {
                helper.getView<SwipeLayout>(R.id.swipe_layout).apply {
                    swipeEnable = !item.isExpanded
                }
            }
        }
    }

    override fun onClick(helper: BaseViewHolder, view: View, data: BaseNode, position: Int) {
        super.onClick(helper, view, data, position)
        if (data is BaseExpandNode && !data.childNode.isNullOrEmpty()) {
            if (data.isExpanded) {
                getAdapter()?.collapseAndChild(position, true, true)
            } else {
                getAdapter()?.expandAndCollapseOther(position, false, true)
            }
        }
    }

    private var mListener: OnItemChildClickListener? = null

    fun setOnItemChildClickListener(listener: OnItemChildClickListener?) {
        mListener = listener
    }

}