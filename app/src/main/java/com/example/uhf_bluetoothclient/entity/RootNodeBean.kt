package com.example.uhf_bluetoothclient.entity

import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode


 class RootNodeBean(
    val sn: String,
) : BaseExpandNode() {
    init {
        isExpanded=false
    }
    override var childNode: MutableList<BaseNode>? = null
}