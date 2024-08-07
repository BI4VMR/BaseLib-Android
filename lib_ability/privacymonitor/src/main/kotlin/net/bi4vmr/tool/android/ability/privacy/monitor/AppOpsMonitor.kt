package net.bi4vmr.tool.android.ability.privacy.monitor

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.bi4vmr.tool.android.ability.privacy.appops.AppOpsManagerExt
import net.bi4vmr.tool.android.ability.privacy.appops.OpEntity
import java.util.concurrent.CopyOnWriteArraySet

/**
 * 应用权限使用状况监视器。
 *
 * @author bi4vmr@outlook.com
 */
open class AppOpsMonitor(
    mContext: Context,
    private val ops: IntArray? = null
) {

    companion object {
        private val TAG = "BaseLib-${AppOpsMonitor::class.java.simpleName}"
    }

    private val opManagerExt: AppOpsManagerExt = AppOpsManagerExt.getInstance(mContext)
    // 轮询任务
    private var job: Job? = null
    // OP过滤器
    private var opFilter: AppOpsFilterCallback = DefaultOpsFilter()
    // 缓存上次查询到的OpEntity列表
    private var lastOpList: List<OpEntity> = listOf()
    // OP监听器回调集合
    private val listeners: MutableSet<AppOpsListener> = CopyOnWriteArraySet()

    // 是否对查询结果去重
    var needDistinctByPackageName: Boolean = false

    fun getAppOps(): List<OpEntity> {
        // 如果缓存列表还没有更新，则先进行更新
        if (lastOpList.isEmpty()) {
            val opList: List<OpEntity> = opManagerExt.getPackagesOps(ops)
            processOPList(opList)
        }

        return lastOpList
    }

    fun startWatchOps(interval: Long = 2000L) {
        synchronized(AppOpsMonitor::class.java) {
            stopWatchOps()

            job = CoroutineScope(Dispatchers.Default).launch {
                while (true) {
                    val opList: List<OpEntity> = opManagerExt.getPackagesOps(ops)
                    processOPList(opList)

                    delay(interval)
                }
            }
        }
    }

    fun stopWatchOps() {
        job?.cancel()
    }

    private fun processOPList(rawList: List<OpEntity>) {
        // 根据包名去重
        var result: MutableList<OpEntity> = if (needDistinctByPackageName) {
            rawList.distinctBy { it.packageName }.toMutableList()
        } else {
            rawList.toMutableList()
        }

        // 根据过滤规则筛选有效表项
        result = result.filter { opFilter.test(it) }.toMutableList()

        // 仅当OpEntity列表有变化时通知监听者
        if (result != lastOpList) {
            lastOpList = result
            notifyAppOpsChange(result)
        }
    }

    fun setAppOpsFilter(callback: AppOpsFilterCallback) {
        opFilter = callback
    }

    fun resetAppOpsFilter() {
        opFilter = DefaultOpsFilter()
    }

    fun registerAppOpsListener(l: AppOpsListener) {
        listeners.add(l)
    }

    fun unregisterAppOpsListener(l: AppOpsListener) {
        listeners.remove(l)
    }

    private fun notifyAppOpsChange(ops: List<OpEntity>) {
        listeners.forEach {
            it.onChange(ops)
        }
    }

    /**
     * 默认的OP过滤规则。
     */
    private inner class DefaultOpsFilter : AppOpsFilterCallback {

        override fun test(item: OpEntity): Boolean {
            // 默认不过滤任何表项
            return true
        }
    }
}
