package com.example.uhf_bluetoothclient.ui.adapter

import com.chad.library.adapter.base.BaseNodeAdapter
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.example.uhf_bluetoothclient.entity.RootNodeBean
import com.example.uhf_bluetoothclient.entity.SecondNodeBean
import com.example.uhf_bluetoothclient.ui.adapter.nodeprovider.DatasetRootNodeProvider
import com.example.uhf_bluetoothclient.ui.adapter.nodeprovider.DatasetSecondNodeProvider

/**
 * @author KP
 * @Desc
 * @date 2021/7/14 15:44
 */
class NodeInfoAdapter : BaseNodeAdapter() {
    private val rootNode = DatasetRootNodeProvider()

    init {
        animationEnable = true
        addNodeProvider(rootNode)
        addNodeProvider(DatasetSecondNodeProvider())
    }

    override fun getItemType(data: List<BaseNode>, position: Int): Int {
        return when (getItem(position)) {
            is RootNodeBean -> 0
            is SecondNodeBean -> 1
            else -> -1
        }
    }

    override fun setOnItemChildClickListener(listener: OnItemChildClickListener?) {
        super.setOnItemChildClickListener(listener)
        rootNode.setOnItemChildClickListener(listener)
    }
}