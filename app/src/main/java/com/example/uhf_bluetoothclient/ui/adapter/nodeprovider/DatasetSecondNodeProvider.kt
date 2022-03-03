package com.example.uhf_bluetoothclient.ui.adapter.nodeprovider

import android.view.View
import com.chad.library.adapter.base.entity.node.BaseExpandNode
import com.chad.library.adapter.base.entity.node.BaseNode
import com.chad.library.adapter.base.provider.BaseNodeProvider
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.example.uhf_bluetoothclient.R
import com.example.uhf_bluetoothclient.entity.SecondNodeBean

/**
 *
 * @ProjectName:    seuic-ocr
 * @Description:     数据集详情目录
 * @Author:         KP
 * @CreateDate:     2021/10/19 11:08
 *
 * @Version:        1.0
 */
class DatasetSecondNodeProvider(
    override val itemViewType: Int = 1,
    override val layoutId: Int = R.layout.dataset_second_item,
) : BaseNodeProvider() {

    override fun convert(helper: BaseViewHolder, item: BaseNode) {
        if (item is SecondNodeBean) {
            helper.setText(R.id.details_title_tv, item.title)
            helper.setText(R.id.details_content_tv, item.content)
        }
    }
}