package net.bi4vmr.tool.android.ability.privacymonitor

import android.content.Context
import net.bi4vmr.tool.android.ability.privacymonitor.appops.AppOps
import net.bi4vmr.tool.android.ability.privacymonitor.appops.OpEntity
import net.bi4vmr.tool.android.ability.privacymonitor.monitor.AppOpsFilterCallback

/**
 * 录像权限使用状况监视器。
 *
 * @author bi4vmr@outlook.com
 */
class CameraPrivacyMonitor(mContext: Context) : PrivacyMonitor(mContext, OPS_CAMERA) {

    companion object {

        /**
         * 录像权限列表。
         */
        val OPS_CAMERA: IntArray = intArrayOf(
            AppOps.CAMERA.code,
            AppOps.PHONE_CALL_CAMERA.code
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
            val isInCalling: Boolean = (item.opCode == AppOps.PHONE_CALL_CAMERA.code) && item.isRunning
            val isInRecording: Boolean = (item.opCode == AppOps.CAMERA.code) && item.isRunning
            return isInCalling || isInRecording
        }
    }
}
