package net.bi4vmr.tool.android.ability.privacymonitor

import android.content.Context
import net.bi4vmr.tool.android.ability.privacymonitor.appops.AppOps
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
}
