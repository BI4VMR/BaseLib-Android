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

    private val opManagerExt: AppOpsManagerExt = AppOpsManagerExt.getInstance(mContext)
    private val listeners: MutableSet<AppOpsListener> = CopyOnWriteArraySet()
    // 轮询任务
    private var job: Job? = null
    // 缓存上次查询到的OpEntity列表
    private var lastOpList: List<OpEntity> = listOf()
    // 是否对查询结果去重
    var needDistinctResult: Boolean = false

    fun getAppOps(): List<OpEntity> {
        // 如果缓存列表还没有更新，则先进行更新
        if (lastOpList.isEmpty()) {
            lastOpList = opManagerExt.getPackagesOps(ops)
        }

        return lastOpList
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

    fun startWatchOps(interval: Long = 2000L) {
        synchronized(AppOpsMonitor::class.java) {
            stopWatchOps()

            job = CoroutineScope(Dispatchers.Default).launch {
                while (true) {
                    var opList: List<OpEntity> = opManagerExt.getPackagesOps(ops)

                    // 根据包名去重
                    if (needDistinctResult) {
                        opList = opList.distinctBy { it.packageName }
                    }

                    // 仅当OpEntity列表有变化时通知监听者
                    if (opList != lastOpList) {
                        lastOpList = opList
                        notifyAppOpsChange(opList)
                    }

                    delay(interval)
                }
            }
        }
    }

    fun stopWatchOps() {
        job?.cancel()
    }
}
