package net.bi4vmr.tool.android.ability.privacy

import android.content.Context
import android.graphics.drawable.Drawable
import net.bi4vmr.tool.android.ability.privacy.appops.OpEntity
import net.bi4vmr.tool.android.ability.privacy.monitor.AppOpsMonitor
import net.bi4vmr.tool.android.ability.privacy.tool.AppInfoHelper
import net.bi4vmr.tool.android.ability.privacymonitor.R
import java.util.concurrent.CopyOnWriteArraySet

/**
 * 敏感权限监视器。
 *
 * 扩展了[AppOpsMonitor]，为OP信息添加了应用名称等更多属性。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
open class PrivacyMonitor(
    // 上下文环境
    private val mContext: Context,
    // 调用者关注的OP项
    ops: IntArray,
    // 更新间隔（毫秒）
    private var interval: Long = 2000L
) : AppOpsMonitor(mContext, ops) {

    companion object {
        private val TAG = "BaseLib-${PrivacyMonitor::class.java.simpleName}"
    }

    private var defaultIcon: Drawable = mContext.resources.getDrawable(R.drawable.ic_app_default, null)
    private val appInfoHelper: AppInfoHelper = AppInfoHelper.getInstance(mContext)
    // 敏感权限应用监听器回调集合
    private val listeners: MutableSet<PrivacyItemListener> = CopyOnWriteArraySet()

    /**
     * 是否忽略系统应用。
     *
     */
    private var ignoreSystemApp: Boolean = false

    init {
        registerAppOpsListener { list ->
            val appList: List<PrivacyItem> = opListToPrivacyList(list)
            notifyPrivacyListChange(appList)
            notifyPrivacyStateChange(appList.isNotEmpty())
        }
    }

    /**
     * 获取敏感权限应用列表。
     *
     * @return 敏感权限应用列表。
     */
    fun getPrivacyItems(): List<PrivacyItem> {
        return opListToPrivacyList(getAppOps())
    }

    fun isIgnoreSystemApp(): Boolean {
        return ignoreSystemApp
    }

    fun setIgnoreSystemApp(state: Boolean) {
        ignoreSystemApp = state
    }

    /**
     * 更新刷新间隔。
     *
     * @param[interval] 刷新间隔，单位为毫秒。
     */
    fun updateInterval(interval: Long) {
        this.interval = interval
        startWatchOps(interval)
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
     * 将OP列表转换为PrivacyItem列表。
     *
     * @param[opList] OP列表。
     */
    private fun opListToPrivacyList(opList: List<OpEntity>): List<PrivacyItem> {
        val datas: MutableList<PrivacyItem> = mutableListOf()
        // 为原始OP信息添加应用名称与图标
        for (op: OpEntity in opList) {
            // 忽略系统应用
            if (ignoreSystemApp && appInfoHelper.isSystemApp(op.packageName)) {
                continue
            }

            // 获取应用名称，如果获取失败则填写为包名。
            val appName: String = appInfoHelper.getLabel(op.packageName) ?: op.packageName
            // 获取应用图标，如果获取失败则填写为默认图片。
            val icon: Drawable = appInfoHelper.getIcon(op.packageName) ?: defaultIcon
            val item = PrivacyItem(appName, icon, op)
            datas.add(item)
        }

        return datas
    }

    /**
     * 敏感权限应用监听器。
     *
     * @since 1.0.0
     * @author bi4vmr@outlook.com
     */
    abstract class PrivacyItemListener {

        /**
         * 正在使用敏感权限的应用列表改变事件。
         *
         * @param[items] 正在使用敏感权限的应用列表。
         */
        open fun onListChange(items: List<PrivacyItem>) {
            // 需要子类重写
        }

        /**
         * 敏感权限使用状态改变事件。
         *
         * @param[state] 使用状态。
         */
        open fun onStateChange(state: Boolean) {
            // 需要子类重写
        }
    }

    /**
     * 注册敏感权限应用监听器。
     *
     * @param[l] 监听器实现。
     */
    fun registerPrivacyItemListener(l: PrivacyItemListener) {
        // 如果列表为空，则自动开启监视任务。
        if (listeners.isEmpty()) {
            startWatchOps(interval)
        }

        listeners.add(l)
    }

    /**
     * 注销敏感权限应用监听器。
     *
     * @param[l] 监听器实现。
     */
    fun unregisterPrivacyItemListener(l: PrivacyItemListener) {
        listeners.remove(l)

        // 如果列表为空，则停止监视任务。
        if (listeners.isEmpty()) {
            stopWatchOps()
        }
    }

    /**
     * 通知[PrivacyItemListener]的监听者权限应用列表发生变化。
     *
     * @param[items] 应用列表。
     */
    private fun notifyPrivacyListChange(items: List<PrivacyItem>) {
        listeners.forEach {
            it.onListChange(items)
        }
    }

    /**
     * 通知[PrivacyItemListener]的监听者权限使用状态发生变化。
     *
     * @param[state] 使用状态。
     */
    private fun notifyPrivacyStateChange(state: Boolean) {
        listeners.forEach {
            it.onStateChange(state)
        }
    }
}
