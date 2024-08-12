package net.bi4vmr.tool.android.ability.privacymonitor

import android.app.AppOpsManager.OnOpActiveChangedListener
import android.content.Context
import android.graphics.drawable.Drawable
import net.bi4vmr.tool.android.ability.privacymonitor.appops.AppOpsManagerExt
import net.bi4vmr.tool.android.ability.privacymonitor.appops.OpEntity
import net.bi4vmr.tool.android.ability.privacymonitor.appops.OpMode
import net.bi4vmr.tool.android.ability.privacymonitor.util.AppInfoHelper
import net.bi4vmr.tool.android.ability.privacymonitor.util.PrivacyLog
import java.util.concurrent.CopyOnWriteArraySet

/**
 * 敏感权限使用状况监视器。
 *
 * 扩展了[AppOpsManagerExt]，为OP信息添加了应用名称等更多属性，并提供黑名单等过滤规则。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
open class PrivacyMonitor(

    /**
     * 上下文环境。
     */
    private val mContext: Context,

    /**
     * 感兴趣的OP类型。
     */
    private val ops: IntArray
) {

    companion object {
        private val TAG = "${PrivacyLog.TAG_PREFIX}${PrivacyMonitor::class.java.simpleName}"
    }

    private val opManagerExt: AppOpsManagerExt = AppOpsManagerExt.getInstance(mContext)
    private val appInfoHelper: AppInfoHelper = AppInfoHelper.getInstance(mContext)
    // 敏感权限事件监听器的具体实现
    private val eventListeners: MutableSet<PrivacyEventListener> = CopyOnWriteArraySet()
    // 缓存上次查询到的数据
    private var itemsCache: MutableList<PrivacyItem> = mutableListOf()
    // OP运行状态监听器
    private val opActiveStateListener: OnOpActiveChangedListener = OPActiveStateListener()

    // 默认的应用图标
    private var defaultIcon: Drawable = mContext.resources.getDrawable(R.drawable.ic_app_default, null)
    // OP过滤器
    private var opFilter: AppOpsFilterCallback = DefaultOpsFilter()

    /**
     * 包名黑名单。
     *
     * 集合中的项将被过滤。
     */
    private val blackList: MutableSet<String> = CopyOnWriteArraySet()

    /**
     * 是否根据包名去重。
     */
    private var distinctByPackageName: Boolean = false

    /**
     * 是否忽略系统应用。
     */
    private var ignoreSystemApp: Boolean = false

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
     * 获取是否忽略系统应用。
     *
     * @return 开关状态。
     */
    fun isIgnoreSystemApp(): Boolean {
        return ignoreSystemApp
    }

    /**
     * 设置是否忽略系统应用。
     *
     * @param[state] 开关状态。
     */
    fun setIgnoreSystemApp(state: Boolean) {
        ignoreSystemApp = state
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
    fun isDistinctByPackageName(distinct: Boolean) {
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
     * 设置未知应用的图标。
     *
     * @param[drawable] 图标。
     */
    fun setDefaultIcon(drawable: Drawable) {
        defaultIcon = drawable
    }

    /**
     * 将未知应用的图标重置为默认资源。
     */
    fun resetDefaultIcon() {
        defaultIcon = mContext.resources.getDrawable(R.drawable.ic_app_default, null)
    }

    /**
     * 获取敏感权限应用列表。
     *
     * @return 应用列表。
     */
    fun getPrivacyItems(): List<PrivacyItem> {
        return processOPList(opManagerExt.getPackagesOps(ops))
    }

    /**
     * 根据过滤规则处理原始列表，并将[OpEntity]对象转换为[PrivacyItem]对象。
     *
     * @param[rawList] 从[AppOpsManagerExt.getPackagesOps]获取到的原始OP信息列表。
     */
    private fun processOPList(rawList: List<OpEntity>): List<PrivacyItem> {
        var opList: MutableList<OpEntity> = rawList.toMutableList()

        // 包名黑名单
        if (blackList.isNotEmpty()) {
            // 如果包名在黑名单中，则将其丢弃。
            opList = opList.filter {
                !blackList.contains(it.packageName)
            }.toMutableList()
        }

        // 根据包名去重
        if (distinctByPackageName) {
            opList = opList.distinctBy { it.packageName }.toMutableList()
        }

        // 忽略系统应用
        if (ignoreSystemApp) {
            opList = opList.filter {
                !appInfoHelper.isSystemApp(it.packageName)
            }.toMutableList()
        }

        // 自定义过滤规则
        opList = opList.filter { opFilter.test(it) }.toMutableList()

        // 转换数据
        val result: MutableList<PrivacyItem> = mutableListOf()
        opList.forEach {
            result.add(PrivacyItem.parseFromOpEntity(it, appInfoHelper, defaultIcon))
        }

        return result
    }

    /**
     * 注册敏感权限事件监听器。
     *
     * @param[l] 监听器实现。
     */
    fun registerPrivacyEventListener(l: PrivacyEventListener) {
        // 如果为首个回调，则初始化OP监听器。
        if (eventListeners.isEmpty()) {
            // 更新缓存
            itemsCache = getPrivacyItems().toMutableList()
            // 注册状态变化监听器
            opManagerExt.startWatchingActive(ops, opActiveStateListener)
        }

        eventListeners.add(l)
    }

    /**
     * 注销敏感权限事件监听器。
     *
     * @param[l] 监听器实现。
     */
    fun unregisterPrivacyEventListener(l: PrivacyEventListener) {
        eventListeners.remove(l)

        // 如果没有监听者了，则注销OP监听器。
        if (eventListeners.isEmpty()) {
            opManagerExt.stopWatchingActive(opActiveStateListener)
        }
    }

    /**
     * 通知[PrivacyEventListener]的监听者应用列表发生变化。
     *
     * @param[items] 应用列表。
     */
    private fun notifyPrivacyListChange(items: List<PrivacyItem>) {
        eventListeners.forEach {
            it.onListChange(items)
        }
    }

    /**
     * 通知[PrivacyEventListener]的监听者权限使用状态发生变化。
     *
     * @param[state] 使用状态。
     */
    private fun notifyPrivacyStateChange(state: Boolean) {
        eventListeners.forEach {
            it.onStateChange(state)
        }
    }

    /**
     * OP运行状态监听器实现类。
     */
    private inner class OPActiveStateListener : OnOpActiveChangedListener {

        override fun onOpActiveChanged(op: String, uid: Int, packageName: String, active: Boolean) {
            val opCode: Int = opManagerExt.opNameToCode(op)
            PrivacyLog.printDebug(
                TAG,
                "OnOpActiveChanged. APP:[$packageName] UID:[$uid] Code:[$opCode] Name:[$op] State:[$active]"
            )
            // 查找缓存中是否有与当前事件匹配的项
            itemsCache.find {
                it.op.packageName == packageName &&
                        it.op.uid == uid &&
                        it.op.opCode == opCode
            }?.let { item ->
                // 最新状态与缓存状态不同时，触发后续操作，否则忽略该事件。
                if (item.op.running != active) {
                    // 如果OP没有被过滤规则丢弃，则更新至列表中。
                    if (active) {
                        val opEntity = OpEntity(packageName, uid, opCode, OpMode.ALLOWED.code, true)
                        itemsCache.add(PrivacyItem.parseFromOpEntity(opEntity, appInfoHelper, defaultIcon))
                    } else {
                        itemsCache.remove(item)
                    }
                    // 通知监听器
                    notifyPrivacyListChange(itemsCache)
                    notifyPrivacyStateChange(itemsCache.isNotEmpty())
                }
            }
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
