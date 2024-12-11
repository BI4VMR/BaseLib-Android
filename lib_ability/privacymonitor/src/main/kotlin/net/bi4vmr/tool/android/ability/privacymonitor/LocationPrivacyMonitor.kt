package net.bi4vmr.tool.android.ability.privacymonitor

import net.bi4vmr.tool.android.ability.framework.appops.AppOps

/**
 * 位置权限使用状况监视器。
 *
 * @since 1.0.0
 * @author bi4vmr@outlook.com
 */
class LocationPrivacyMonitor : PrivacyMonitor(OPS_LOCATION) {

    companion object {

        /**
         * 位置OP列表。
         */
        private val OPS_LOCATION: IntArray = intArrayOf(
            // 高精度GNSS位置
            AppOps.MONITOR_HIGH_POWER_LOCATION.code
        )
    }
}
