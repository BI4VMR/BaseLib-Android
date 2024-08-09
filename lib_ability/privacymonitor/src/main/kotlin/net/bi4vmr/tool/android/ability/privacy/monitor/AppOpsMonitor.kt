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
 * 本类实现了对AppOps列表的轮询监控，每当OP列表发生变化时，将会通过回调通知调用者，并且增加了黑名单等过滤机制。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
open class AppOpsMonitor(
    /**
     * 上下文环境。
     */
    mContext: Context,

    /**
     * 调用者关注的OP类型列表。
     *
     * 如果需要监控所有OP类型，可以传入空值。
     */
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

    /**
     * 包名黑名单。
     *
     * 集合中的项将被过滤。
     */
    private val blackList: MutableSet<String> = CopyOnWriteArraySet()
    // OP监听器回调集合
    private val listeners: MutableSet<AppOpsListener> = CopyOnWriteArraySet()

    // 是否根据包名对查询结果去重
    private var distinctByPackageName: Boolean = false

    /**
     * 获取OP列表。
     *
     * 立即刷新当前Monitor关心的OP列表，如果OP列表与上一次相比有更新，将会触发[AppOpsListener]监听器。
     *
     * @return 最新的[OpEntity]列表。
     */
    fun getAppOps(): List<OpEntity> {
        val opList: List<OpEntity> = opManagerExt.getPackagesOps(ops)
        processOPList(opList)

        return lastOpList
    }

    /**
     * 开始监控OP信息。
     *
     * 如果已经开始监控，需要重新设置刷新间隔，可以再次调用本方法。
     *
     * @param[interval] 刷新间隔，默认为2秒。
     */
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

    /**
     * 停止监控OP信息。
     */
    fun stopWatchOps() {
        job?.cancel()
    }

    /**
     * 根据过滤规则处理OP列表，并将结果通知给监听者。
     *
     * @param[rawList] 从系统API获取到的原始OP列表。
     */
    private fun processOPList(rawList: List<OpEntity>) {
        var result: MutableList<OpEntity> = if (blackList.isNotEmpty()) {
            // 如果包名在黑名单中，则将其丢弃。
            rawList.filter {
                !blackList.contains(it.packageName)
            }.toMutableList()
        } else {
            rawList.toMutableList()
        }

        // 根据包名去重
        result = if (distinctByPackageName) {
            result.distinctBy { it.packageName }.toMutableList()
        } else {
            result.toMutableList()
        }

        // 根据过滤规则筛选有效表项
        result = result.filter { opFilter.test(it) }.toMutableList()

        // 仅当OpEntity列表有变化时通知监听者
        if (result != lastOpList) {
            lastOpList = result
            notifyAppOpsChange(result)
        }
    }

    fun addAppToBlackList(name: String) {
        blackList.add(name)
    }

    fun removeAppFromBlackList(name: String) {
        blackList.remove(name)
    }

    fun clearBlackList() {
        blackList.clear()
    }

    /**
     * 是否根据包名去重。
     *
     * 以录音权限为例，我们需要监控多个OP是否被使用，但用户界面只需要显示应用是否正在录音，并不关心其触发了几项与录音有关的OP，此时可以开
     * 启该功能，结果列表中每个包名只会出现一次。
     *
     * 去重功能将先于过滤器触发，默认状态为关闭。
     *
     * @param[distinct] 去重开关。
     */
    fun needDistinctByPackageName(distinct: Boolean) {
        distinctByPackageName = distinct
    }

    /**
     * 设置自定义过滤器。
     *
     * 使用自定义过滤器替换默认实现（默认实现为不过滤任何表项）。
     *
     * @param[callback] 过滤器实现。
     */
    fun setAppOpsFilter(callback: AppOpsFilterCallback) {
        opFilter = callback
    }

    /**
     * 将过滤器重置为默认实现。
     *
     * 使用自定义过滤器替换默认实现（默认实现为不过滤任何表项）。
     */
    fun resetAppOpsFilter() {
        opFilter = DefaultOpsFilter()
    }

    /**
     * 注册OP列表监听器。
     *
     * @param[l] 监听器实现。
     */
    fun registerAppOpsListener(l: AppOpsListener) {
        listeners.add(l)
    }

    /**
     * 注销OP列表监听器。
     *
     * @param[l] 监听器实现。
     */
    fun unregisterAppOpsListener(l: AppOpsListener) {
        listeners.remove(l)
    }

    /**
     * 通知[AppOpsListener]的监听者OP列表发生了变化。
     *
     * @param[ops] 处理后的OP列表。
     */
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
