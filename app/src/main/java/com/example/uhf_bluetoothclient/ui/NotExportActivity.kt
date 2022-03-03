package com.example.uhf_bluetoothclient.ui

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.uhf_bluetoothclient.R
import com.example.uhf_bluetoothclient.entity.RootNodeBean
import com.example.uhf_bluetoothclient.http.ErrorInfo
import com.example.uhf_bluetoothclient.initializer.exportInfoDao
import com.example.uhf_bluetoothclient.ui.adapter.NodeInfoAdapter
import com.example.uhf_bluetoothclient.util.toDatasetRootNodeBean
import com.lxj.xpopup.XPopup
import com.seuic.util.common.SPUtils
import com.seuic.util.common.ext.singleClick
import com.seuic.util.common.ext.toJsonStr
import kotlinx.coroutines.launch
import org.json.JSONObject
import rxhttp.RxHttp
import rxhttp.awaitResult
import rxhttp.toResponse

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
        findViewById<ImageView>(R.id.toolbar_back).singleClick {
            finish()
        }
        findViewById<RecyclerView>(R.id.not_export_rv).apply {
            layoutManager =
                LinearLayoutManager(this@NotExportActivity, LinearLayoutManager.VERTICAL, false)
            this.adapter = this@NotExportActivity.adapter
        }

        val list = exportInfoDao.getAll().toMutableList()
        adapter.setOnItemChildClickListener { _, view, position ->
            when (view.id) {
                R.id.delete_tv -> {
                    XPopup.Builder(this)
                        .asConfirm("", "确认是否删除?") {
                            adapter.getItemOrNull(position)?.also { item ->
                                if (item is RootNodeBean) {
                                    list.firstOrNull { it.snNum == item.sn }?.also {
                                        list.remove(it)
                                        exportInfoDao.deleteItem(it)
                                        adapter.data.remove(item)
                                        adapter.notifyDataSetChanged()
                                    }
                                }
                            }
                        }.show()
                }
            }
        }

        adapter.setNewInstance(list.map { it.toDatasetRootNodeBean() }
            .toMutableList())

        findViewById<TextView>(R.id.toolbar_more_tv)?.singleClick {
            lifecycleScope.launch {
                val ip = SPUtils.getInstance().getString("lastIP", "")
                val port = SPUtils.getInstance().getString("lastPort", "")
                exportInfoDao.getAll().forEach { bean ->
                    showLoading("")
                    RxHttp.postJson("https://${ip}:${port}/server/information/save")
                        .addAll(
                            JSONObject().put(
                                "jsonString",
                                bean.toJsonStr()
                            ).toString()
                        ).toResponse<Any>().awaitResult {
                            exportInfoDao.deleteItem(bean)
                            list.remove(bean)
                            adapter.data.removeIf { it is RootNodeBean && it.sn == bean.snNum }
                            adapter.notifyDataSetChanged()
                            hideLoading()
                        }.onFailure {
                            ErrorInfo(it)
                            hideLoading()
                        }
                }

            }
        }
    }

    private val loadingView by lazy {
        XPopup.Builder(this).asLoading()
    }

    fun hideLoading() {
        index--
        if (index <= 0)
            loadingView.dismiss()
    }

    var index = 0
    fun showLoading(message: String) {
        index++
        loadingView.setTitle(message)
        if (!loadingView.isShow)
            loadingView.show()
    }
}