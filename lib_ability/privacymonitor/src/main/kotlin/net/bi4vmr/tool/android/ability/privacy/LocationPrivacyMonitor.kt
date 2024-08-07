package net.bi4vmr.tool.android.ability.privacy

import android.content.Context
import net.bi4vmr.tool.android.ability.privacy.appops.AppOps
import net.bi4vmr.tool.android.ability.privacy.monitor.AppOpsMonitor

/**
 * 位置权限使用状况监视器。
 *
 * @author bi4vmr@outlook.com
 */
class LocationPrivacyMonitor(mContext: Context) : AppOpsMonitor(mContext, OPS_LOCATION) {

    companion object {
        // 定位权限
        private val OPS_LOCATION: IntArray = intArrayOf(
            // AppOps.COARSE_LOCATION.code,
            // AppOps.FINE_LOCATION.code,
            AppOps.MONITOR_HIGH_POWER_LOCATION.code
        )
    }
}
