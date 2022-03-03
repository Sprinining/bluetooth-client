package com.example.uhf_bluetoothclient.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uhf_bluetoothclient.R
import com.example.uhf_bluetoothclient.entity.RootNodeBean
import com.example.uhf_bluetoothclient.initializer.exportInfoDao
import com.example.uhf_bluetoothclient.ui.adapter.NodeInfoAdapter
import com.example.uhf_bluetoothclient.util.toDatasetRootNodeBean
import com.lxj.xpopup.XPopup

/**
 *
 * @ProjectName:    UHF_BluetoothClient
 * @Description:     类作用描述
 * @Author:         KP
 * @CreateDate:     2022/2/25 10:32
 * @Version:        1.0
 */
class NotExportActivity : AppCompatActivity() {
    private val adapter: NodeInfoAdapter = NodeInfoAdapter()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notexport)

        findViewById<RecyclerView>(R.id.not_export_rv).apply {
            layoutManager =
                LinearLayoutManager(this@NotExportActivity, LinearLayoutManager.VERTICAL, false)
            this.adapter = this@NotExportActivity.adapter
        }

        val list = exportInfoDao.getAll().toMutableList()
        adapter.setOnItemChildClickListener { adapter, view, position ->
            when (view.id) {
                R.id.delete_tv -> {
                    XPopup.Builder(this)
                        .asConfirm("", "确认是否删除?") {
                            adapter.getItemOrNull(position)?.apply {
                                if (this is RootNodeBean) {
                                    list.firstOrNull { it.snNum == this.sn }?.also {
                                        list.remove(it)
                                        exportInfoDao.deleteItem(it)
                                        adapter.remove(it)
                                    }
                                }
                            }
                        }.show()
                }
            }
        }

        adapter.setNewInstance(list.map { it.toDatasetRootNodeBean() }
            .toMutableList())


    }
}