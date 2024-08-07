package net.bi4vmr.tool.android.ability.privacy

import android.content.Context
import net.bi4vmr.tool.android.ability.privacy.appops.AppOps
import net.bi4vmr.tool.android.ability.privacy.appops.OpEntity
import net.bi4vmr.tool.android.ability.privacy.monitor.AppOpsFilterCallback
import net.bi4vmr.tool.android.ability.privacy.monitor.AppOpsMonitor

/**
 * 定位权限使用状况监视器。
 *
 * @author bi4vmr@outlook.com
 */
class LocationPrivacyMonitor(mContext: Context) : AppOpsMonitor(mContext, OPS_LOCATION) {

    companion object {

        /**
         * 定位权限OP列表
         *
         * AOSP SystemUI源码中权限监控模块包含“粗略位置”与精确位置，但它们是瞬态的，没有持续状态，根据通用需求此处只监控正在使用“高精度
         * 定位”权限的应用即可。
         */
        private val OPS_LOCATION: IntArray = intArrayOf(
            // AppOps.COARSE_LOCATION.code,
            // AppOps.FINE_LOCATION.code,
            AppOps.MONITOR_HIGH_POWER_LOCATION.code
        )
    }

    init {
        setAppOpsFilter(Filter())
    }

    /**
     * OP筛选器。
     */
    private inner class Filter : AppOpsFilterCallback {

        override fun test(item: OpEntity): Boolean {
            // 仅当程序正在使用“高精度定位”权限，将其加入结果列表。
            return (item.opCode == AppOps.MONITOR_HIGH_POWER_LOCATION.code) && item.isRunning
        }
    }
}
