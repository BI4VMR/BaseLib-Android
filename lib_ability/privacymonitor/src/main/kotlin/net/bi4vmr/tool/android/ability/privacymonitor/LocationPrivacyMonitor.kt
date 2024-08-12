package net.bi4vmr.tool.android.ability.privacymonitor

import android.content.Context
import net.bi4vmr.tool.android.ability.privacymonitor.appops.AppOps
import net.bi4vmr.tool.android.ability.privacymonitor.appops.OpEntity
import net.bi4vmr.tool.android.ability.privacymonitor.util.PrivacyLog

/**
 * 位置权限使用状况监视器。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
class LocationPrivacyMonitor(mContext: Context) : PrivacyMonitor(mContext, OPS_LOCATION) {

    companion object {

        /**
         * 位置OP列表。
         */
        private val OPS_LOCATION: IntArray = intArrayOf(
            // 高精度GNSS位置
            AppOps.MONITOR_HIGH_POWER_LOCATION.code
        )

        private val TAG = "${PrivacyLog.TAG_PREFIX}${LocationPrivacyMonitor::class.java.simpleName}"
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
            return (item.opCode == AppOps.MONITOR_HIGH_POWER_LOCATION.code) && item.running
        }
    }
}
